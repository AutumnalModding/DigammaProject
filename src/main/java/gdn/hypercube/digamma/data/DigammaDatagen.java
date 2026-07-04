package gdn.hypercube.digamma.data;

import gdn.hypercube.digamma.content.block.TypedBlock;
import gdn.hypercube.solaris.core.SolarisBootstrap;
import gdn.hypercube.solaris.generator.content.RegistryInitializer;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.block.Block;
import net.minecraft.data.tag.ProvidedTagBuilder;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jspecify.annotations.NonNull;

public class DigammaDatagen implements DataGeneratorEntrypoint {
    public static final Logger LOGGER = LogManager.getLogger("Digamma Project Datagen");

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();
        pack.addProvider(BlockTags::new);
    }

    public static class BlockTags extends FabricTagsProvider.BlockTagsProvider {
        private final Map<String, ProvidedTagBuilder<Block, Block>> CACHE = new HashMap<>();

        public BlockTags(FabricPackOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> future) {
            super(output, future);
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void configure(RegistryWrapper.@NonNull WrapperLookup registries) {
            RegistryInitializer.get(Block.class).contents().forEach((name, block) -> {
                if (block instanceof TypedBlock typed) {
                    String kind = switch (typed.type) {
                        case VERY_SOFT, SOFT -> null;
                        case WOOD -> "AXE";
                        case STONE, METAL_SOFT, METAL, METAL_HARD -> "PICKAXE";
                    };

                    if (kind != null) {
                        ProvidedTagBuilder<Block, Block> builder = CACHE.computeIfAbsent(kind, _ -> {
                            try {
                                Field field = net.minecraft.registry.tag.BlockTags.class.getDeclaredField(kind + "_MINEABLE");
                                field.setAccessible(true);
                                TagKey<Block> key = (TagKey<Block>) field.get(null);
                                return valueLookupBuilder(key);
                            } catch (ReflectiveOperationException exception) {
                                SolarisBootstrap.oopsie(DigammaDatagen.LOGGER, "FAILED ACCESSING TAG BUILDER FOR: ", exception);
                                return null;
                            }
                        });

                        if (builder != null) builder.add(typed);
                    }
                }
            });
        }
    }
}
