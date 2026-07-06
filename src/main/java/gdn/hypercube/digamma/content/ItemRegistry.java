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
import org.apache.commons.lang3.text.WordUtils;

@UsedImplicitly
@SuppressWarnings({"CodeBlock2Expr", "deprecation"})
public class ItemRegistry extends ReflectiveRegistry<Item> {
    public final Item DATAPAD = this.create("datapad", DatapadItem::new);

    {
        for (String coin : new String[]{"copper", "silver", "gold", "platinum"}) {
            this.create("coin/" + coin, () -> new GenericItem(
                "coin/" + coin,
                new ChainedList<Text>()
                .add(Text.translatable("tooltip.artifice.coin", WordUtils.capitalize(coin)).formatted(Formatting.GRAY))
                .add(
                    Text.translatable("tooltip.artifice.coin." + coin + ".flavour")
                    .formatted(Formatting.DARK_GRAY)
                    .formatted(Formatting.ITALIC)
                ).arrayify())
            );
        }

        for (String upgrade : new String[]{
            "armour_spikes", "conductive_filament", "counterweight",
            "diving_kit", "elastic_layering", "elastic_soles",
            "firedamp", "hardened_tips", "laminated_padding",
            "plaited_string", "quilted_cover", "reinforced_limbs",
            "reinforcement", "scuba_tank", "sharpening_kit"
        }) {
            this.create("upgrade/" + upgrade, () -> new GenericItem(
                "upgrade/" + upgrade,
                new ChainedList<Text>()
                .add(
                    Text.translatable("tooltip.artifice.upgrade." + upgrade + ".flavour")
                    .formatted(Formatting.DARK_GRAY)
                    .formatted(Formatting.ITALIC)
                ).arrayify())
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
        blocks.iconSupplier = () -> this.contents.get("artifice/wall_advanced").getDefaultStack();
        dungeons.iconSupplier = () -> this.contents.get("dungeon/abductor").getDefaultStack();
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