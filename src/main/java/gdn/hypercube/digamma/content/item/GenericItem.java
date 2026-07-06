package gdn.hypercube.digamma.content.item;

import java.util.List;
import java.util.function.Consumer;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

public class GenericItem extends Item {
    @Nullable public final List<Text> tooltip;

    public GenericItem(String name, @Nullable List<Text> tooltip) {
        Item.Settings settings = new Item.Settings();
        settings.registryKey(RegistryKey.of(
                RegistryKeys.ITEM, Identifier.of("digamma", name)
        ));

        this.tooltip = tooltip;
        super(settings);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent component, Consumer<Text> consumer, TooltipType type) {
        if (this.tooltip != null) this.tooltip.forEach(consumer);
    }
}
