package gdn.hypercube.digamma.delta.util;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public enum DeltaProtocolSound {
    DING(Identifier.of("minecraft", "entity.ender_dragon.death"))
    ;

    final Identifier ident;

    DeltaProtocolSound(Identifier ident) {
        this.ident = ident;
    }

    @Nullable
    public Identifier get() {
        return this.ident;
    }
}
