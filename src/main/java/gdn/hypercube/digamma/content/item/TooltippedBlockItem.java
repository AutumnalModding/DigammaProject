package gdn.hypercube.digamma.content.item;

import java.util.List;
import java.util.function.Consumer;
import net.minecraft.block.Block;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

public class TooltippedBlockItem extends BlockItem {
    private final List<Text> tooltip;

    public TooltippedBlockItem(Block block, Settings settings, List<Text> tooltip) {
        super(block, settings);
        this.tooltip = tooltip;
    }

    @Override
    @SuppressWarnings("deprecation") // i do NOT care
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent component, Consumer<Text> consumer, TooltipType type) {
        super.appendTooltip(stack, context, component, consumer, type);
        this.tooltip.forEach(consumer);
    }

    @Override
    public Text getName(ItemStack stack) {
        String original = super.getName(stack).getString();
        return Text.translatable("block.digamma." + original.replace("item.digamma.", ""));
    }
}
