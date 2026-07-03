package gdn.hypercube.digamma.delta.util;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public enum DeltaProtocolPortrait {
    EMPTY("missingno"),
    ;

    final String name;

    DeltaProtocolPortrait(String name) {
        this.name = name;
    }

    @Nullable
    public Identifier get() {
        return this.name.isEmpty() ? null : Identifier.of("digamma", "textures/gui/portrait/" + this.name + ".png");
    }
}
