package gdn.hypercube.digamma.content;

import gdn.hypercube.digamma.content.item.DatapadItem;
import gdn.hypercube.digamma.content.item.GenericItem;
import gdn.hypercube.solaris.generator.content.ReflectiveRegistry;
import gdn.hypercube.solaris.generator.content.RegistryInitializer;
import gdn.hypercube.solaris.util.ChainedList;
import gdn.hypercube.solaris.util.UsedImplicitly;
import java.util.Map;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.MergedComponentMap;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@UsedImplicitly
@SuppressWarnings("CodeBlock2Expr")
public class ItemRegistry extends ReflectiveRegistry<Item> {
    public final Item DATAPAD = this.create("datapad", DatapadItem::new);

    {
        for (String item : new String[]{
            "armour_spikes", "counterweight", "diving_kit",
            "elastic_layering", "elastic_soles", "firedamp",
            "laminated_padding", "plaited_string", "quilted_cover",
            "reinforced_limbs", "reinforcement", "scuba_tank",
            "sharpening_kit"
        }) {
            this.create("upgrade/" + item, () -> new GenericItem(
                "upgrade/" + item,
                new ChainedList<Text>()
                    .add(
                        Text.translatable("tooltip.artifice.upgrade." + item + ".flavour")
                        .formatted(Formatting.DARK_GRAY)
                        .formatted(Formatting.ITALIC)
                    )
                    .arrayify()
                )
            );
        }
    }

    protected ItemRegistry() {
        super("digamma");
    }

    @Override
    public void init() {
        super.init();
        Map<String, ItemGroup> groups = RegistryInitializer.get(ItemGroup.class).contents();
        ItemGroup items = groups.get("items");
        ItemGroup blocks = groups.get("blocks");

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
        // TODO: clean these two up to not be carbon copies...
        items.entryCollector = (_, entries) -> {
            this.contents.forEach((_, item) -> {
                if (!(item instanceof BlockItem)) {
                    ItemStack stack;
                    try {
                        stack = new ItemStack(item);
                    } catch (Exception ignored) {
                        stack = new ItemStack(item.asItem().getRegistryEntry(), 1, new MergedComponentMap(ComponentMap.EMPTY));
                    }
                    entries.add(stack);
                }
            });
        };
    }
}