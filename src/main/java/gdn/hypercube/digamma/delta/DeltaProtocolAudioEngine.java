package gdn.hypercube.digamma.delta;

import gdn.hypercube.epsilon.core.handler.PlatformSoundInstance;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.LocalRandom;
import net.minecraft.util.math.random.Random;

public class DeltaProtocolAudioEngine {
    private static final Random RNG = new LocalRandom(System.currentTimeMillis());

    public static void drive(SoundInstance instance) { // TODO: do more
        MinecraftClient.getInstance().getSoundManager().play(instance);
    }

    public static class SoundInstance extends AbstractSoundInstance implements PlatformSoundInstance {
        protected SoundInstance(Identifier sound) {
            super(sound, SoundCategory.MASTER, RNG); // TODO: custom sound category
        }

        protected float volumize(float target) {
            float old = this.getVolume();
            this.volume = target;
            return old;
        }

        protected float repitch(float target) {
            float old = this.getPitch();
            this.pitch = target;
            return old;
        }

        @Override
        public String name() {
            return this.id.getPath();
        }
    }
}
