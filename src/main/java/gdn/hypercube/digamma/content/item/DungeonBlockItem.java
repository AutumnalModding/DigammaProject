package gdn.hypercube.digamma.content.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class DungeonBlockItem extends BlockItem {
    public DungeonBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public Text getName(ItemStack stack) {
        String original = super.getName(stack).getString();
        return Text.translatable("block.digamma.dungeon", original.replace("item.digamma.dungeon.", ""));
    }
}
