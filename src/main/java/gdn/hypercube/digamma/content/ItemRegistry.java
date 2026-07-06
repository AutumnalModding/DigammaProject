package gdn.hypercube.digamma.content;

import gdn.hypercube.digamma.content.item.DatapadItem;
import gdn.hypercube.digamma.content.item.GenericItem;
import gdn.hypercube.solaris.generator.content.ReflectiveRegistry;
import gdn.hypercube.solaris.generator.content.RegistryInitializer;
import gdn.hypercube.solaris.util.UsedImplicitly;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.MergedComponentMap;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;

@UsedImplicitly
@SuppressWarnings("CodeBlock2Expr")
public class ItemRegistry extends ReflectiveRegistry<Item> {
    public final Item DATAPAD = this.create("datapad", DatapadItem::new);

    public final Item SHARPENING_KIT = this.create("upgrade/sharpening_kit", () -> new GenericItem("upgrade/sharpening_kit"));

    protected ItemRegistry() {
        super("digamma");
    }

    @Override
    public void init() {
        super.init();
        ItemGroup blocks = RegistryInitializer.get(ItemGroup.class).contents().get("blocks");
        blocks.entryCollector = (_, entries) -> {
            this.contents.forEach((_, item) -> {
                if (item instanceof BlockItem block) {
                    ItemStack stack;
                    try {
                        stack = new ItemStack(block);
                    } catch (Exception ignored) {
                        stack = new ItemStack(item.asItem().getRegistryEntry(), 1, new MergedComponentMap(ComponentMap.EMPTY));
                    }
                    entries.add(stack);
                }
            });
        };
    }
}