package gdn.hypercube.digamma.content;

import gdn.hypercube.digamma.content.recipe.UpgradingRecipe;
import gdn.hypercube.solaris.generator.content.ReflectiveRegistry;
import net.minecraft.recipe.display.SlotDisplay;

@SuppressWarnings("rawtypes")
public class SlotDisplaySerializerRegistry extends ReflectiveRegistry<SlotDisplay.Serializer> {
    public final SlotDisplay.Serializer UPGRADING = create("upgrading", () -> UpgradingRecipe.UpgradingSampleSlotDisplay.SERIALIZER);

    protected SlotDisplaySerializerRegistry() {
        super("digamma");
    }
}
