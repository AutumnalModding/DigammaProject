package gdn.hypercube.digamma.content.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;

public class GlassBlock extends GenericBlock {
    public GlassBlock(String name) {
        super(name, AbstractBlock.Settings.copy(Blocks.GLASS).nonOpaque());
    }
}
