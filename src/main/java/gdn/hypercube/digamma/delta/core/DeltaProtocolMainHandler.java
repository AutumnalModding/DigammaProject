package gdn.hypercube.digamma.delta.core;

import gdn.hypercube.digamma.delta.util.DeltaProtocolDrawInfo;
import gdn.hypercube.digamma.util.MutableReference;
import gdn.hypercube.epsilon.core.handler.AbstractPlatform;
import gdn.hypercube.epsilon.core.handler.PlatformSoundInstance;
import gdn.hypercube.epsilon.core.util.Pair;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;

public class DeltaProtocolMainHandler extends AbstractPlatform {
protected static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    protected final MutableReference<DrawContext> CONTEXT = new MutableReference<>();
    protected final MutableReference<RenderTickCounter> COUNTER = new MutableReference<>();

    @Override public int framerate() { return CLIENT.getCurrentFps(); }
    @Override public int getWidth(String character) { return CLIENT.textRenderer.getWidth(character); }
    @Override public float getDynamicDeltaTicks() { return COUNTER.value == null ? 0 : COUNTER.value.getFixedDeltaTicks(); }

    @Override public void drawText(int index, List<Pair<String, Integer>> characters, int x, int y) {
        DeltaProtocolBootSequence.DPDI.put(index, new DeltaProtocolDrawInfo(characters, x, y));
    }

    @Override public void drawCharacter(String character, int x, int y, int colour) { if (this.CONTEXT.value != null) this.CONTEXT.value.drawTextWithShadow(CLIENT.textRenderer, character, x, y, colour); }

    @Override public void playSound(PlatformSoundInstance instance, float volume, float pitch) {
        DeltaProtocolBootSequence.LOGGER.debug("Driving audio engine: target {}, volume {} pitch {}", instance.name(), volume, pitch);
        DeltaProtocolAudioEngine.SoundInstance target = new DeltaProtocolAudioEngine.SoundInstance(Identifier.of("epsilon", instance.name()));
        target.repitch(pitch);
        target.volumize(volume);
        DeltaProtocolAudioEngine.drive(target);
    }
}
