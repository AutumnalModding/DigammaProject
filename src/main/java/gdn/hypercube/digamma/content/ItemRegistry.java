package gdn.hypercube.digamma.content;

import gdn.hypercube.digamma.content.item.DatapadItem;
import gdn.hypercube.solaris.generator.content.ReflectiveRegistry;
import gdn.hypercube.solaris.util.UsedImplicitly;
import net.minecraft.item.Item;

@UsedImplicitly
public class ItemRegistry extends ReflectiveRegistry<Item> {
    public final Item DATAPAD = this.create("datapad", DatapadItem::new);
}