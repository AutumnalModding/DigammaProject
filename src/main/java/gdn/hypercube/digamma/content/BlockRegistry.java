package gdn.hypercube.digamma.content;

import com.google.common.collect.Lists;
import gdn.hypercube.digamma.content.block.GenericBlock;
import gdn.hypercube.digamma.content.block.TypedBlock;
import gdn.hypercube.digamma.content.item.TooltippedBlockItem;
import gdn.hypercube.solaris.generator.content.DualRegistry;
import gdn.hypercube.solaris.util.ChainedList;
import gdn.hypercube.solaris.util.Priority;
import gdn.hypercube.solaris.util.UsedImplicitly;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import static gdn.hypercube.digamma.content.block.TypedBlock.Type;

@Priority(5) @UsedImplicitly
@SuppressWarnings("CodeBlock2Expr") // p5 for after item init
public class BlockRegistry extends DualRegistry<Block, Item> {

    private Block typed(String prefix, String name, Type type) {
        return this.create(prefix + name, () -> new TypedBlock(prefix, name, type));
    }

    private Block typed(String prefix, String name, Block parent) {
        return this.create(prefix + name, () -> new TypedBlock(prefix, name, parent));
    }

    private Block tooltipped(String name, Supplier<Block> input, List<Text> tooltip) {
        Block target = input.get();
        return this.create(name, () -> target, (_, _) -> {
            return new TooltippedBlockItem(target, new Item.Settings().registryKey(
                RegistryKey.of(RegistryKeys.ITEM, Identifier.of("digamma", name))
            ), tooltip);
        });
    }

    private Block wall(String name, Type type) {
        return typed("generic/", name, type);
    }

    private Block tile(String name, Type type) {
        return typed("generic/", name, type);
    }

    protected BlockRegistry() {
        super("digamma", (name, block) -> {
            return new BlockItem(block, new Item.Settings().registryKey(
                RegistryKey.of(RegistryKeys.ITEM, Identifier.of("digamma", name))
            ));
        });
    }

    {
        for (int i = 0; i <= 3; i++) {
            for (int j = 4; j <= 7; j++) {
                String type = switch(i) {
                    case 0 -> "frame";
                    case 1 -> "glass";
                    case 2 -> "scaffold";
                    case 3 -> "wall";
                    default -> throw new IllegalStateException("Unexpected value: " + i);
                };

                String kind = switch (j) {
                    case 4 -> "basic";
                    case 5 -> "reinforced";
                    case 6 -> "industrial";
                    case 7 -> "advanced";
                    default -> throw new IllegalStateException("Unexpected value: " + j);
                };

                String name = type + "_" + kind;
                List<Text> tooltip = new ChainedList<Text>()
                    .add(Text.translatable("tooltip.artifice." + type + "." + kind).formatted(Formatting.GRAY))
                    .add(Text.translatable("tooltip.artifice." + type + "." + kind + ".flavour").formatted(Formatting.DARK_GRAY).formatted(Formatting.ITALIC))
                    .arrayify();

                tooltipped("artifice/" + name, () -> new GenericBlock(Blocks.STONE, name), tooltip);
            }
        }
    }

    { // TODO: We should really have some kind of system to make this better. For now though, this "works".
        wall("abductor", Type.METAL_HARD); wall("asteroid_rock", Type.STONE); wall("bananium", Type.METAL);
        wall("barricade", Type.WOOD); wall("brick", Type.STONE); wall("card", Type.WOOD);
        wall("clock", Type.METAL_SOFT); wall("cobblebrick_andesite", Type.STONE); wall("cobblebrick_asteroid", Type.STONE);
        wall("cobblebrick_basalt", Type.STONE); wall("cobblebrick_chromite", Type.STONE); wall("cobblebrick", Type.STONE);
        wall("cobblebrick_sand", Type.STONE); wall("cobblebrick_snow", Type.STONE); wall("crystallineabyss", Type.STONE);
        wall("cult", Type.METAL); wall("diamond", Type.METAL_SOFT); wall("drywall", Type.WOOD);
        wall("elevator", Type.METAL); wall("gold", Type.METAL_SOFT); typed("generic/", "ice", Blocks.PACKED_ICE);
        wall("meat", Type.WOOD); wall("metal", Type.METAL); wall("mining", Type.METAL_HARD);
        wall("mountain_rock", Type.STONE); wall("necropolis", Type.METAL); wall("paper", Type.WOOD);
        wall("plasma", Type.METAL); wall("plastic", Type.WOOD); wall("plastitanium", Type.METAL_HARD);
        wall("riveted", Type.METAL); wall("rplas", Type.METAL_HARD); wall("rwall", Type.METAL);
        wall("sandstone", Type.STONE); wall("silver", Type.METAL); wall("solid", Type.METAL);
        wall("solid_rust", Type.METAL); wall("uranium", Type.METAL); wall("web", Type.VERY_SOFT);
        wall("wood", Type.WOOD); wall("xenoborg", Type.METAL_HARD); wall("xeno", Type.STONE);

        tile("abductor_tile", Type.METAL_HARD); tile("arcadeblue2", Type.SOFT); tile("arcadeblue", Type.SOFT);
        tile("arcadered", Type.SOFT); tile("asphalt", Type.STONE); tile("bananiumtile", Type.METAL);
        tile("bar", Type.STONE); tile("bedrock", Type.STONE); tile("blue_circuit", Type.STONE);
        tile("blue", Type.STONE); tile("cafeteria", Type.WOOD); tile("carpetclown", Type.SOFT);
        tile("carpet_deco", Type.SOFT); tile("carpetoffice", Type.SOFT); tile("checkerboard", Type.WOOD);
        tile("checker_dark", Type.WOOD); tile("chromite", Type.STONE); tile("clockwork_floor_filled", Type.METAL_SOFT);
        tile("clockwork_floor", Type.METAL_SOFT); tile("concrete", Type.STONE); tile("concrete_smooth", Type.STONE);
        tile("cropped_parallax", Type.WOOD); tile("abysstile", Type.STONE); tile("dark_diagonal_mini", Type.STONE);
        tile("dark_diagonal", Type.STONE); tile("darkgrass", Type.VERY_SOFT); tile("dark_herringbone", Type.WOOD);
        tile("dark_marble", Type.STONE); tile("dark_mini", Type.STONE); tile("dark_offset", Type.STONE);
        tile("dark_pavement", Type.STONE); tile("dark_pavement_vertical", Type.STONE); tile("dark", Type.STONE);
        tile("darkstone", Type.STONE); tile("darkwood_broken", Type.WOOD); tile("darkwood_large", Type.WOOD);
        tile("darkwood", Type.WOOD); tile("darkwood_tile", Type.WOOD); tile("eighties", Type.SOFT);
        tile("elevator_shaft", Type.METAL); tile("exoborg", Type.METAL_HARD); tile("fancywood", Type.WOOD);
        tile("freezer", Type.STONE); tile("goldtile", Type.METAL); tile("grassdark", Type.VERY_SOFT);
        tile("grassjungle", Type.VERY_SOFT); tile("grasslight", Type.VERY_SOFT); tile("grass", Type.VERY_SOFT);
        tile("grating_maint", Type.METAL); tile("grayconcrete", Type.STONE); tile("grayconcrete_smooth", Type.STONE);
        tile("green_circuit", Type.METAL); tile("hierophant", Type.STONE); tile("hull", Type.METAL);
        tile("hull_reinforced", Type.METAL_HARD); tile("hydro", Type.STONE); tile("kitchen", Type.STONE);
        tile("laundry", Type.STONE); tile("light-mosaic", Type.WOOD); tile("lime", Type.STONE);
        tile("lino", Type.STONE); tile("meattile", Type.STONE); tile("metal_dark_monke", Type.METAL);
        tile("metaldiamond", Type.METAL_HARD); tile("metal_engie_monke", Type.METAL); tile("metal_med_monke", Type.METAL);
        tile("metal_sci_monke", Type.METAL); tile("metal_sec_monke", Type.METAL); tile("metal_service_monke", Type.METAL);
        tile("mining_floor_dark", Type.METAL); tile("mining_floor_light", Type.METAL); tile("mining_floor", Type.METAL);
        tile("mono", Type.STONE); tile("oldconcrete", Type.STONE); tile("oldconcrete_smooth", Type.STONE);
        tile("plasmarble", Type.STONE); tile("red_circuit", Type.METAL); tile("reebe", Type.METAL_SOFT);
        tile("reinforced", Type.METAL_HARD); tile("sepia", Type.STONE); tile("showroom", Type.STONE);
        tile("silvertile", Type.METAL); tile("steel_diagonal_mini", Type.STONE); tile("steel_diagonal", Type.STONE);
        tile("steel_dirty", Type.STONE); tile("steel_herringbone", Type.STONE); tile("steel_maint", Type.STONE);
        tile("steel_mini", Type.STONE); tile("steel_offset", Type.STONE); tile("steel_pavement", Type.STONE);
        tile("steel_pavement_vertical", Type.STONE); tile("steel", Type.STONE); tile("super_reinforced", Type.METAL_HARD);
        tile("tech_maint_dark", Type.METAL); tile("tech_maint", Type.METAL); tile("terracotta_diagonal", Type.STONE);
        tile("terracotta", Type.STONE); tile("terracotta_small", Type.STONE); tile("uranium_marble", Type.STONE);
        tile("versailles", Type.WOOD); tile("web_tile", Type.VERY_SOFT); tile("white_diagonal_mini", Type.STONE);
        tile("white_diagonal", Type.STONE); tile("white_herringbone", Type.WOOD); tile("white_marble", Type.STONE);
        tile("white_mini", Type.STONE); tile("white_offset", Type.STONE); tile("white_pavement", Type.STONE);
        tile("white_pavement_vertical", Type.STONE); tile("white", Type.WOOD); tile("wood_black", Type.WOOD);
        tile("wood_broken", Type.WOOD); tile("wood_chess_black", Type.WOOD); tile("wood_chess_dark", Type.WOOD);
        tile("wood_chess_light", Type.WOOD); tile("wood_chess", Type.WOOD); tile("wood_chess_red", Type.WOOD);
        tile("wood_dark", Type.WOOD); tile("wood_large_black", Type.WOOD); tile("wood_large_dark", Type.WOOD);
        tile("wood_large_light", Type.WOOD); tile("wood_large", Type.WOOD); tile("wood_large_red", Type.WOOD);
        tile("wood_light", Type.WOOD); tile("wood_parquet_black", Type.WOOD); tile("wood_parquet_dark", Type.WOOD);
        tile("wood_parquet_light", Type.WOOD); tile("wood_parquet", Type.WOOD); tile("wood_parquet_red", Type.WOOD);
        tile("woodtile", Type.WOOD); tile("wood_red", Type.WOOD); tile("wood_tile", Type.WOOD); tile("xeno_steel", Type.METAL);
        tile("xeno_flooring", Type.STONE); tile("xeno_maint", Type.STONE); tile("xeno_steel_corner", Type.METAL);
    }
}
