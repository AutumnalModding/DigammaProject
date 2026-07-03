package gdn.hypercube.digamma.delta.input;

import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

public class DeltaProtocolInputConsumer {
    private static final KeyBinding.Category CATEGORY = KeyBinding.Category.create(
            Identifier.of("digamma", "keybinds")
    );

    public final Runnable executor;
    public final KeyBinding binding;
    public boolean pressed = false;

    public DeltaProtocolInputConsumer(String name, int keycode, Runnable executor) {
        this.executor = executor;

        this.binding = KeyMappingHelper.registerKeyMapping(new KeyBinding(
                "key.digamma." + name,
                InputUtil.Type.KEYSYM,
                keycode,
                CATEGORY
        ));
    }
}
