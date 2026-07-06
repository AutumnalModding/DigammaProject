package gdn.hypercube.digamma.content;

import gdn.hypercube.digamma.content.item.DatapadItem;
import gdn.hypercube.digamma.content.item.DungeonBlockItem;
import gdn.hypercube.digamma.content.item.GenericItem;
import gdn.hypercube.digamma.content.item.TooltippedBlockItem;
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
        ItemGroup dungeons = groups.get("blocks/dungeon");
        collect(items, this.contents, DatapadItem.class, GenericItem.class);
        collect(blocks, this.contents, TooltippedBlockItem.class, BlockItem.class);
        collect(dungeons, this.contents, DungeonBlockItem.class);
        items.iconSupplier = DATAPAD::getDefaultStack;
    }

    @SafeVarargs @SuppressWarnings("deprecation")
    public static void collect(ItemGroup target, Map<String, ? extends Item> contents, Class<? extends Item>... valid) {
        target.entryCollector = (_, entries) -> {
            contents.forEach((_, item) -> {
                for (Class<? extends Item> clazz : valid) {
                    if (item.getClass().isAssignableFrom(clazz)) {
                        ItemStack stack;
                        try {
                            stack = new ItemStack(item);
                        } catch (Exception ignored) {
                            stack = new ItemStack(item.asItem().getRegistryEntry(), 1, new MergedComponentMap(ComponentMap.EMPTY));
                        }
                        entries.add(stack);
                        break;
                    }
                }

            });
        };
    }
}