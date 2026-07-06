package gdn.hypercube.digamma.content.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.*;
import net.minecraft.recipe.input.SmithingRecipeInput;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class UpgradingRecipe extends AbstractSmithingRecipe {

    private static final Codec<Integer> ENCHANTMENT_LEVEL_CODEC = Codec.intRange(1, 255);
    public static final MapCodec<UpgradingRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Settings.CODEC.forGetter((recipe) -> recipe.settings),
                    Ingredient.CODEC.optionalFieldOf("template").forGetter((recipe) -> recipe.template),
                    Ingredient.CODEC.fieldOf("base").forGetter((recipe) -> recipe.base),
                    Ingredient.CODEC.optionalFieldOf("addition").forGetter((recipe) -> recipe.addition),
                    Codec.unboundedMap(Enchantment.ENTRY_CODEC, ENCHANTMENT_LEVEL_CODEC).xmap(Object2IntOpenHashMap::new, Function.identity()).fieldOf("enchantments").forGetter((recipe) -> recipe.enchantments)
            ).apply(instance, UpgradingRecipe::new));

    public static final PacketCodec<RegistryByteBuf, UpgradingRecipe> PACKET_CODEC = PacketCodec.tuple(
            Settings.PACKET_CODEC, (recipe) -> recipe.settings,
            Ingredient.OPTIONAL_PACKET_CODEC, (recipe) -> recipe.template,
            Ingredient.PACKET_CODEC, (recipe) -> recipe.base,
            Ingredient.OPTIONAL_PACKET_CODEC, (recipe) -> recipe.addition,
            PacketCodecs.map(Object2IntOpenHashMap::new, Enchantment.ENTRY_PACKET_CODEC, PacketCodecs.VAR_INT), (recipe) -> recipe.enchantments,
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
        return craft(input.base());
    }

    public ItemStack craft(final ItemStack stack) {
        ItemStack result = stack.copy();
        EnchantmentHelper.apply(result, builder -> this.enchantments.forEach(builder::add));
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

    // FIX LATER (it's fine because was added before deadline)
    /*@Override
    public List<RecipeDisplay> getDisplays() {
        return List.of(new SmithingRecipeDisplay(Ingredient.toDisplay(this.template), this.base.toDisplay(), Ingredient.toDisplay(this.addition), new SlotDisplay.StackSlotDisplay(this.result), new SlotDisplay.ItemSlotDisplay(Items.SMITHING_TABLE)));
    }*/
}
