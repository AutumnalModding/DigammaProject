package gdn.hypercube.digamma.content;

import gdn.hypercube.solaris.generator.content.ReflectiveRegistry;
import gdn.hypercube.solaris.generator.content.RegistryInitializer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

// This actually ISN'T a good use case for ReflectiveRegistry.
public class BlockRegistry extends ReflectiveRegistry<Block> {
    public final Block WALL_WOOD = this.create("wall_wood", () -> new Block(AbstractBlock.Settings.copy(Blocks.OAK_PLANKS).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("digamma", "wall_wood")))));

    @Override
    public void init() {
        this.contents.forEach((name, obj) -> { // TODO: can we make this.. proper?
            Registry.register(this.registry, Identifier.of("digamma", name), obj);
            Registry.register(Registries.ITEM, Identifier.of("digamma", name), new BlockItem(obj, new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of("digamma", name)))));
            RegistryInitializer.LOGGER.debug("Registered {} {}", this.registry.getClass().getCanonicalName(), "digamma:" + name);
        });
    }
}
