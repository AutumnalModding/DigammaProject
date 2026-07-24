package gdn.hypercube.digamma.content.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class GenericBlock extends Block {
    public GenericBlock(Block target, String name) {
        this(name, AbstractBlock.Settings.copy(target).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("digamma", name))));
    }

    public GenericBlock(String name, Settings settings) {
        super(settings.registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("digamma", name))));
    }
}
