package gdn.hypercube.digamma.content;

import gdn.hypercube.digamma.content.item.PillItem;
import gdn.hypercube.solaris.generator.content.ReflectiveRegistry;
import gdn.hypercube.solaris.util.UsedImplicitly;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

@UsedImplicitly
public class ItemRegistry extends ReflectiveRegistry<Item> {
    public final Item PILL = this.create("pill", PillItem::new);
    public final Item NANOCRYSTAL = this.create("nanocrystal",
        () -> new Item(new Item.Settings()
        .registryKey(RegistryKey.of
        (RegistryKeys.ITEM, Identifier.of("digamma", "nanocrystal"))
    )));
}