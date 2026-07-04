package gdn.hypercube.digamma.content;

import gdn.hypercube.solaris.generator.content.ReflectiveRegistry;
import gdn.hypercube.solaris.generator.content.RegistryInitializer;
import gdn.hypercube.solaris.util.Priority;
import java.util.function.Supplier;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Priority(-200)
public class GroupRegistry extends ReflectiveRegistry<ItemGroup> {
    public final ItemGroup BLOCKS = this.simple("blocks");

    public ItemGroup simple(String name) {
        return this.create(name, () -> FabricCreativeModeTab.builder().displayName(Text.translatable("itemGroup.digamma." + name)).icon(() -> new ItemStack(Blocks.STONE)).build());
    }

    @Override
    public ItemGroup create(String name, Supplier<ItemGroup> input) {
        return super.create(name, input);
    }

    protected GroupRegistry() {
        super("digamma");
        this.registrar = (name, group) -> {
            Registry.register(registry, RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of("digamma", name)), group);
        };
    }
}
