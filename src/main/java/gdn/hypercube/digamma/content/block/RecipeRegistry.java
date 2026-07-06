package gdn.hypercube.digamma.content.block;

import gdn.hypercube.digamma.content.recipe.UpgradingRecipe;
import gdn.hypercube.solaris.generator.content.ReflectiveRegistry;
import net.minecraft.recipe.RecipeSerializer;

@SuppressWarnings("rawtypes")
public class RecipeRegistry extends ReflectiveRegistry<RecipeSerializer> {
    public final RecipeSerializer UPGRADING = create("upgrading", () -> UpgradingRecipe.SERIALIZER);

    protected RecipeRegistry() {
        super("digamma");
    }
}
