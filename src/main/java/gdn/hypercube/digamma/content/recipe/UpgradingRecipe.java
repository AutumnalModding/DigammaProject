package gdn.hypercube.digamma.content.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.*;
import net.minecraft.recipe.display.DisplayedItemFactory;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.recipe.display.SmithingRecipeDisplay;
import net.minecraft.recipe.input.SmithingRecipeInput;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.context.ContextParameterMap;
import net.minecraft.util.math.random.Random;

import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class UpgradingRecipe extends AbstractSmithingRecipe {

    private static final Codec<Integer> ENCHANTMENT_LEVEL_CODEC = Codec.intRange(1, 255);

    private static final Codec<Object2IntOpenHashMap<RegistryEntry<Enchantment>>> ENCHANTMENTS_CODEC = Codec.unboundedMap(Enchantment.ENTRY_CODEC, ENCHANTMENT_LEVEL_CODEC).xmap(Object2IntOpenHashMap::new, Function.identity());
    private static final PacketCodec<RegistryByteBuf, Object2IntOpenHashMap<RegistryEntry<Enchantment>>> ENCHANTMENTS_PACKET_CODEC = PacketCodecs.map(Object2IntOpenHashMap::new, Enchantment.ENTRY_PACKET_CODEC, PacketCodecs.VAR_INT);

    public static final MapCodec<UpgradingRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Settings.CODEC.forGetter(recipe -> recipe.settings),
                    Ingredient.CODEC.optionalFieldOf("template").forGetter(recipe -> recipe.template),
                    Ingredient.CODEC.fieldOf("base").forGetter(recipe -> recipe.base),
                    Ingredient.CODEC.optionalFieldOf("addition").forGetter(recipe -> recipe.addition),
                    ENCHANTMENTS_CODEC.fieldOf("enchantments").forGetter(recipe -> recipe.enchantments)
            ).apply(instance, UpgradingRecipe::new)
    );

    public static final PacketCodec<RegistryByteBuf, UpgradingRecipe> PACKET_CODEC = PacketCodec.tuple(
            Settings.PACKET_CODEC, recipe -> recipe.settings,
            Ingredient.OPTIONAL_PACKET_CODEC, recipe -> recipe.template,
            Ingredient.PACKET_CODEC, recipe -> recipe.base,
            Ingredient.OPTIONAL_PACKET_CODEC, recipe -> recipe.addition,
            ENCHANTMENTS_PACKET_CODEC, recipe -> recipe.enchantments,
            UpgradingRecipe::new
    );

    public static final RecipeSerializer<UpgradingRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, PACKET_CODEC);
    private final Optional<Ingredient> template;
    private final Ingredient base;
    private final Optional<Ingredient> addition;
    private final Object2IntOpenHashMap<RegistryEntry<Enchantment>> enchantments;

    protected UpgradingRecipe(Settings settings, Optional<Ingredient> template, Ingredient base, Optional<Ingredient> addition, Object2IntOpenHashMap<RegistryEntry<Enchantment>> enchantments) {
        super(settings);
        this.template = template;
        this.base = base;
        this.addition = addition;
        this.enchantments = enchantments;
    }

    public ItemStack craft(final SmithingRecipeInput input) {
        return craft(input.base(), this.enchantments);
    }

    public static ItemStack craft(final ItemStack stack, Object2IntOpenHashMap<RegistryEntry<Enchantment>> enchantments) {
        ItemStack result = stack.copy();
        EnchantmentHelper.apply(result, builder -> enchantments.forEach(builder::add));
        return result;
    }

    @Override
    public RecipeSerializer<? extends AbstractSmithingRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Optional<Ingredient> template() {
        return this.template;
    }

    @Override
    public Ingredient base() {
        return this.base;
    }

    @Override
    public Optional<Ingredient> addition() {
        return this.addition;
    }

    @Override
    protected IngredientPlacement createIngredientPlacement() {
        return IngredientPlacement.forMultipleSlots(List.of(this.template, Optional.of(this.base), this.addition));
    }

    @Override
    public List<RecipeDisplay> getDisplays() {
        SlotDisplay template = Ingredient.toDisplay(this.template);
        SlotDisplay base = this.base.toDisplay();
        SlotDisplay addition = Ingredient.toDisplay(this.addition);
        return List.of(new SmithingRecipeDisplay(template, base, addition, new UpgradingSampleSlotDisplay(base, this.enchantments), new SlotDisplay.ItemSlotDisplay(Items.SMITHING_TABLE)));
    }

    public record UpgradingSampleSlotDisplay(SlotDisplay base, Object2IntOpenHashMap<RegistryEntry<Enchantment>> enchantments) implements SlotDisplay {
        public static final MapCodec<UpgradingSampleSlotDisplay> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                SlotDisplay.CODEC.fieldOf("base").forGetter(display -> display.base),
                ENCHANTMENTS_CODEC.fieldOf("enchantments").forGetter(display -> display.enchantments)
            ).apply(instance, UpgradingSampleSlotDisplay::new)
        );

        public static final PacketCodec<RegistryByteBuf, UpgradingSampleSlotDisplay> PACKET_CODEC = PacketCodec.tuple(
            SlotDisplay.PACKET_CODEC, display -> display.base,
            ENCHANTMENTS_PACKET_CODEC, display -> display.enchantments,
            UpgradingSampleSlotDisplay::new
        );

        public static final Serializer<UpgradingSampleSlotDisplay> SERIALIZER = new Serializer<>(CODEC, PACKET_CODEC);

        /**
         * @see SlotDisplay.SmithingTrimSlotDisplay#appendStacks(ContextParameterMap, DisplayedItemFactory) 
         */
        @Override
        public <T> Stream<T> appendStacks(ContextParameterMap parameters, DisplayedItemFactory<T> factory) {
            Random randomSource = Random.createLocal(System.identityHashCode(this));
            BinaryOperator<ItemStack> transformation = (base, _) -> craft(base, this.enchantments);
            return SlotDisplay.getSampleSmithingCombinations(parameters, factory, this.base, EmptySlotDisplay.INSTANCE, randomSource, transformation);
        }

        @Override
        public Serializer<? extends SlotDisplay> serializer() {
            return SERIALIZER;
        }
    }
}
