package gdn.hypercube.digamma.content.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SimpleItemStackView;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.*;
import net.minecraft.recipe.crafting.TransmuteRecipe;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.recipe.display.SmithingRecipeDisplay;
import net.minecraft.recipe.input.SmithingRecipeInput;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.List;
import java.util.Optional;

public class EnchantingSmithingRecipe extends AbstractSmithingRecipe {

    public static final MapCodec<EnchantingSmithingRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Settings.CODEC.forGetter((recipe) -> recipe.settings),
                    Ingredient.CODEC.optionalFieldOf("template").forGetter((recipe) -> recipe.template),
                    Ingredient.CODEC.fieldOf("base").forGetter((recipe) -> recipe.base),
                    Ingredient.CODEC.optionalFieldOf("addition").forGetter((recipe) -> recipe.addition),
                    SimpleItemStackView.CODEC.fieldOf("result").forGetter((recipe) -> recipe.result)
            ).apply(instance, EnchantingSmithingRecipe::new));
    public static final PacketCodec<RegistryByteBuf, EnchantingSmithingRecipe> PACKET_CODEC = PacketCodec.tuple(
            Settings.PACKET_CODEC, (recipe) -> recipe.settings,
            Ingredient.OPTIONAL_PACKET_CODEC, (recipe) -> recipe.template,
            Ingredient.PACKET_CODEC, (recipe) -> recipe.base,
            Ingredient.OPTIONAL_PACKET_CODEC, (recipe) -> recipe.addition,
            SimpleItemStackView.PACKET_CODEC, (recipe) -> recipe.result,
            EnchantingSmithingRecipe::new
    );

    public static final RecipeSerializer<EnchantingSmithingRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, PACKET_CODEC);
    private final Optional<Ingredient> template;
    private final Ingredient base;
    private final Optional<Ingredient> addition;
    private final Object2IntOpenHashMap<RegistryEntry<Enchantment>> enchantments;

    protected EnchantingSmithingRecipe(Settings settings, Optional<Ingredient> template, Ingredient base, Optional<Ingredient> addition, Object2IntOpenHashMap<RegistryEntry<Enchantment>> enchantments) {
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
        return stack.copy().apply(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.builder().build(), component -> {
            ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(component);
        });
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
