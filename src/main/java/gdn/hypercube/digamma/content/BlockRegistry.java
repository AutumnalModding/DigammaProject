package gdn.hypercube.digamma.content;

import gdn.hypercube.digamma.content.block.GenericBlock;
import gdn.hypercube.digamma.content.block.DungeonBlock;
import gdn.hypercube.digamma.content.item.DungeonBlockItem;
import gdn.hypercube.digamma.content.item.TooltippedBlockItem;
import gdn.hypercube.solaris.generator.content.DualRegistry;
import gdn.hypercube.solaris.util.ChainedList;
import gdn.hypercube.solaris.data.Priority;
import gdn.hypercube.solaris.data.UsedImplicitly;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import static gdn.hypercube.digamma.content.block.DungeonBlock.Type;

@Priority(5) @UsedImplicitly
@SuppressWarnings("CodeBlock2Expr") // p5 for after item init
public class BlockRegistry extends DualRegistry<Block, Item> {
    private Block dungeon(String name, Type type) {
        Block target = new DungeonBlock("dungeon/", name, type);
        return this.create("dungeon/" + name, () -> target, (_, _) -> {
            return new DungeonBlockItem(target, new Item.Settings().registryKey(
                RegistryKey.of(RegistryKeys.ITEM, Identifier.of("digamma", "dungeon/" + name))
            ));
        });
    }

    private Block tooltipped(String name, Supplier<Block> input, List<Text> tooltip) {
        Block target = input.get();
        return this.create(name, () -> target, (_, _) -> {
            return new TooltippedBlockItem(target, new Item.Settings().registryKey(
                RegistryKey.of(RegistryKeys.ITEM, Identifier.of("digamma", name))
            ), tooltip);
        });
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
            for (int j = 0; j <= 3; j++) {
                String type = switch(i) {
                    case 0 -> "frame";
                    case 1 -> "glass";
                    case 2 -> "scaffold";
                    case 3 -> "wall";
                    default -> throw new IllegalStateException("Unexpected value: " + i);
                };

                String kind = switch (j) {
                    case 0 -> "basic";
                    case 1 -> "reinforced";
                    case 2 -> "industrial";
                    case 3 -> "advanced";
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
        dungeon("abductor", Type.METAL_HARD); dungeon("asteroid_rock", Type.STONE); dungeon("bananium", Type.METAL);
        dungeon("barricade", Type.WOOD); dungeon("brick", Type.STONE); dungeon("card", Type.WOOD);
        dungeon("clock", Type.METAL_SOFT); dungeon("cobblebrick_andesite", Type.STONE); dungeon("cobblebrick_asteroid", Type.STONE);
        dungeon("cobblebrick_basalt", Type.STONE); dungeon("cobblebrick_chromite", Type.STONE); dungeon("cobblebrick", Type.STONE);
        dungeon("cobblebrick_sand", Type.STONE); dungeon("cobblebrick_snow", Type.STONE); dungeon("crystallineabyss", Type.STONE);
        dungeon("cult", Type.METAL); dungeon("diamond", Type.METAL_SOFT); dungeon("drywall", Type.WOOD);
        dungeon("elevator", Type.METAL); dungeon("gold", Type.METAL_SOFT); dungeon("ice", Type.STONE);
        dungeon("meat", Type.WOOD); dungeon("metal", Type.METAL); dungeon("mining", Type.METAL_HARD);
        dungeon("mountain_rock", Type.STONE); dungeon("necropolis", Type.METAL); dungeon("paper", Type.WOOD);
        dungeon("plasma", Type.METAL); dungeon("plastic", Type.WOOD); dungeon("plastitanium", Type.METAL_HARD);
        dungeon("riveted", Type.METAL); dungeon("rplas", Type.METAL_HARD); dungeon("rwall", Type.METAL);
        dungeon("sandstone", Type.STONE); dungeon("silver", Type.METAL); dungeon("solid", Type.METAL);
        dungeon("solid_rust", Type.METAL); dungeon("uranium", Type.METAL); dungeon("web", Type.VERY_SOFT);
        dungeon("wood", Type.WOOD); dungeon("xenoborg", Type.METAL_HARD); dungeon("xeno", Type.STONE);

        dungeon("abductor_tile", Type.METAL_HARD); dungeon("arcadeblue2", Type.SOFT); dungeon("arcadeblue", Type.SOFT);
        dungeon("arcadered", Type.SOFT); dungeon("asphalt", Type.STONE); dungeon("bananiumtile", Type.METAL);
        dungeon("bar", Type.STONE); dungeon("bedrock", Type.STONE); dungeon("blue_circuit", Type.STONE);
        dungeon("blue", Type.STONE); dungeon("cafeteria", Type.WOOD); dungeon("carpetclown", Type.SOFT);
        dungeon("carpet_deco", Type.SOFT); dungeon("carpetoffice", Type.SOFT); dungeon("checkerboard", Type.WOOD);
        dungeon("checker_dark", Type.WOOD); dungeon("chromite", Type.STONE); dungeon("clockwork_floor_filled", Type.METAL_SOFT);
        dungeon("clockwork_floor", Type.METAL_SOFT); dungeon("concrete", Type.STONE); dungeon("concrete_smooth", Type.STONE);
        dungeon("cropped_parallax", Type.WOOD); dungeon("abysstile", Type.STONE); dungeon("dark_diagonal_mini", Type.STONE);
        dungeon("dark_diagonal", Type.STONE); dungeon("darkgrass", Type.VERY_SOFT); dungeon("dark_herringbone", Type.WOOD);
        dungeon("dark_marble", Type.STONE); dungeon("dark_mini", Type.STONE); dungeon("dark_offset", Type.STONE);
        dungeon("dark_pavement", Type.STONE); dungeon("dark_pavement_vertical", Type.STONE); dungeon("dark", Type.STONE);
        dungeon("darkstone", Type.STONE); dungeon("darkwood_broken", Type.WOOD); dungeon("darkwood_large", Type.WOOD);
        dungeon("darkwood", Type.WOOD); dungeon("darkwood_tile", Type.WOOD); dungeon("eighties", Type.SOFT);
        dungeon("elevator_shaft", Type.METAL); dungeon("exoborg", Type.METAL_HARD); dungeon("fancywood", Type.WOOD);
        dungeon("freezer", Type.STONE); dungeon("goldtile", Type.METAL); dungeon("grassdark", Type.VERY_SOFT);
        dungeon("grassjungle", Type.VERY_SOFT); dungeon("grasslight", Type.VERY_SOFT); dungeon("grass", Type.VERY_SOFT);
        dungeon("grating_maint", Type.METAL); dungeon("grayconcrete", Type.STONE); dungeon("grayconcrete_smooth", Type.STONE);
        dungeon("green_circuit", Type.METAL); dungeon("hierophant", Type.STONE); dungeon("hull", Type.METAL);
        dungeon("hull_reinforced", Type.METAL_HARD); dungeon("hydro", Type.STONE); dungeon("kitchen", Type.STONE);
        dungeon("laundry", Type.STONE); dungeon("light-mosaic", Type.WOOD); dungeon("lime", Type.STONE);
        dungeon("lino", Type.STONE); dungeon("meattile", Type.STONE); dungeon("metal_dark_monke", Type.METAL);
        dungeon("metaldiamond", Type.METAL_HARD); dungeon("metal_engie_monke", Type.METAL); dungeon("metal_med_monke", Type.METAL);
        dungeon("metal_sci_monke", Type.METAL); dungeon("metal_sec_monke", Type.METAL); dungeon("metal_service_monke", Type.METAL);
        dungeon("mining_floor_dark", Type.METAL); dungeon("mining_floor_light", Type.METAL); dungeon("mining_floor", Type.METAL);
        dungeon("mono", Type.STONE); dungeon("oldconcrete", Type.STONE); dungeon("oldconcrete_smooth", Type.STONE);
        dungeon("plasmarble", Type.STONE); dungeon("red_circuit", Type.METAL); dungeon("reebe", Type.METAL_SOFT);
        dungeon("reinforced", Type.METAL_HARD); dungeon("sepia", Type.STONE); dungeon("showroom", Type.STONE);
        dungeon("silvertile", Type.METAL); dungeon("steel_diagonal_mini", Type.STONE); dungeon("steel_diagonal", Type.STONE);
        dungeon("steel_dirty", Type.STONE); dungeon("steel_herringbone", Type.STONE); dungeon("steel_maint", Type.STONE);
        dungeon("steel_mini", Type.STONE); dungeon("steel_offset", Type.STONE); dungeon("steel_pavement", Type.STONE);
        dungeon("steel_pavement_vertical", Type.STONE); dungeon("steel", Type.STONE); dungeon("super_reinforced", Type.METAL_HARD);
        dungeon("tech_maint_dark", Type.METAL); dungeon("tech_maint", Type.METAL); dungeon("terracotta_diagonal", Type.STONE);
        dungeon("terracotta", Type.STONE); dungeon("terracotta_small", Type.STONE); dungeon("uranium_marble", Type.STONE);
        dungeon("versailles", Type.WOOD); dungeon("web_tile", Type.VERY_SOFT); dungeon("white_diagonal_mini", Type.STONE);
        dungeon("white_diagonal", Type.STONE); dungeon("white_herringbone", Type.WOOD); dungeon("white_marble", Type.STONE);
        dungeon("white_mini", Type.STONE); dungeon("white_offset", Type.STONE); dungeon("white_pavement", Type.STONE);
        dungeon("white_pavement_vertical", Type.STONE); dungeon("white", Type.WOOD); dungeon("wood_black", Type.WOOD);
        dungeon("wood_broken", Type.WOOD); dungeon("wood_chess_black", Type.WOOD); dungeon("wood_chess_dark", Type.WOOD);
        dungeon("wood_chess_light", Type.WOOD); dungeon("wood_chess", Type.WOOD); dungeon("wood_chess_red", Type.WOOD);
        dungeon("wood_dark", Type.WOOD); dungeon("wood_large_black", Type.WOOD); dungeon("wood_large_dark", Type.WOOD);
        dungeon("wood_large_light", Type.WOOD); dungeon("wood_large", Type.WOOD); dungeon("wood_large_red", Type.WOOD);
        dungeon("wood_light", Type.WOOD); dungeon("wood_parquet_black", Type.WOOD); dungeon("wood_parquet_dark", Type.WOOD);
        dungeon("wood_parquet_light", Type.WOOD); dungeon("wood_parquet", Type.WOOD); dungeon("wood_parquet_red", Type.WOOD);
        dungeon("woodtile", Type.WOOD); dungeon("wood_red", Type.WOOD); dungeon("wood_tile", Type.WOOD); dungeon("xeno_steel", Type.METAL);
        dungeon("xeno_flooring", Type.STONE); dungeon("xeno_maint", Type.STONE); dungeon("xeno_steel_corner", Type.METAL);
    }
}
