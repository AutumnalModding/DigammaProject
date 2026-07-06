package gdn.hypercube.digamma.content.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;

public class DungeonBlock extends GenericBlock {
    public Type type;

    public DungeonBlock(String prefix, String name, Type type) {
        this(prefix, name, type.parent, type.toughness.getLeft(), type.toughness.getRight());
        this.type = type;
    }

    public DungeonBlock(String prefix, String name, Block parent, float hardness, float resistance) {
        super(parent, prefix + name);
        this.settings = this.settings.hardness(hardness).resistance(resistance);
    }

    @Override
    public MutableText getName() {
        String original = super.getName().getString();
        return Text.translatable("block.digamma.dungeon", original.replace("block.digamma.dungeon.", ""));
    }

    public enum Type {
        VERY_SOFT(Blocks.GRASS_BLOCK),
        SOFT(Blocks.WHITE_WOOL),
        WOOD(Blocks.OAK_PLANKS),
        STONE(Blocks.STONE),
        METAL_SOFT(Blocks.COPPER_BLOCK),
        METAL(Blocks.IRON_BLOCK),
        METAL_HARD(Blocks.NETHERITE_BLOCK)
        ;

        private final Block parent;
        private final Pair<Float, Float> toughness;

        Type(Block parent, float hardness, float resistance) {
            this.parent = parent;
            this.toughness = new Pair<>(hardness, resistance);
        }

        Type(Block parent) {
            this(parent, parent.getHardness(), parent.getBlastResistance());
        }
    }
}
