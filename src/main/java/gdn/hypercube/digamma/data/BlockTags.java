package gdn.hypercube.digamma.data;

import gdn.hypercube.digamma.content.block.DungeonBlock;
import gdn.hypercube.solaris.generator.content.RegistryInitializer;
import gdn.hypercube.solaris.util.MiscHelpers;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.block.Block;
import net.minecraft.data.tag.ProvidedTagBuilder;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import org.jspecify.annotations.NonNull;

public class BlockTags extends FabricTagsProvider.BlockTagsProvider {
    private final Map<String, ProvidedTagBuilder<Block, Block>> CACHE = new HashMap<>();

    public BlockTags(FabricPackOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> future) {
        super(output, future);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void configure(RegistryWrapper.@NonNull WrapperLookup registries) {
        RegistryInitializer.get(Block.class).contents().forEach((_, block) -> {
            if (block instanceof DungeonBlock typed && typed.type != null) {
                List<String> possible = new ArrayList<>();
                possible.add(switch (typed.type) {
                    case VERY_SOFT, SOFT -> null;
                    case WOOD -> "AXE_MINEABLE";
                    case STONE, METAL_SOFT, METAL, METAL_HARD -> "PICKAXE_MINEABLE";
                });

                possible.add(switch (typed.type) {
                    case VERY_SOFT, SOFT, WOOD, STONE -> null;
                    case METAL_SOFT -> "NEEDS_STONE_TOOL";
                    case METAL -> "NEEDS_IRON_TOOL";
                    case METAL_HARD -> "NEEDS_DIAMOND_TOOL";
                });

                for (String tag : possible) {
                    if (tag != null) {
                        ProvidedTagBuilder<Block, Block> builder = CACHE.computeIfAbsent(tag, _ -> {
                            try {
                                Field field = net.minecraft.registry.tag.BlockTags.class.getDeclaredField(tag);
                                field.setAccessible(true);
                                TagKey<Block> key = (TagKey<Block>) field.get(null);
                                return valueLookupBuilder(key);
                            } catch (ReflectiveOperationException exception) {
                                MiscHelpers.oopsie(DigammaDatagen.LOGGER, "FAILED ACCESSING TAG BUILDER FOR: " + tag, exception);
                                return null;
                            }
                        });

                        if (builder != null) builder.add(typed);
                    }
                }
            }
        });
    }
}
