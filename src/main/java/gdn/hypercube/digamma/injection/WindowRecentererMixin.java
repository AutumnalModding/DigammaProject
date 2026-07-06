package gdn.hypercube.digamma.injection;

import gdn.hypercube.digamma.delta.core.DeltaProtocolBootSequence;
import gdn.hypercube.epsilon.core.util.Pair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.window.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class WindowRecentererMixin {
    @Inject(method = "onResolutionChanged", at = @At("TAIL"))
    public void recenter(CallbackInfo info) {
        Window window = ((MinecraftClient) (Object) this).getWindow();
        if (DeltaProtocolBootSequence.LOCATION instanceof DeltaProtocolBootSequence.DrawPosition old) {
            Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> coordinates = new Pair<>(
                new Pair<>(
                    old.x(),
                    old.y()
                ),
                new Pair<>(
                    DeltaProtocolBootSequence.DrawPosition.CENTER.x(),
                    DeltaProtocolBootSequence.DrawPosition.CENTER.y()
                )
            );
            DeltaProtocolBootSequence.DrawPosition.CENTER = new DeltaProtocolBootSequence.DrawPosition((window.getWidth() / 4) - (328 / 2), (window.getHeight() / 4) - (72 / 2));
            if (coordinates.left().left().equals(coordinates.right().left()) && coordinates.left().right().equals(coordinates.right().right())) {
                DeltaProtocolBootSequence.LOCATION = DeltaProtocolBootSequence.DrawPosition.CENTER;
            }
        }
    }
}
