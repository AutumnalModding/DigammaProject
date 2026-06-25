package gdn.hypercube.digamma.patch.mixin;

import org.ladysnake.cca.internal.base.ComponentRegistryImpl;
import org.ladysnake.cca.internal.base.asm.CcaAsmHelper;
import org.ladysnake.cca.internal.base.asm.CcaBootstrap;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ComponentRegistryImpl.class)
@SuppressWarnings("UnstableApiUsage")
public class CardinalComponentsRemapperMixin {
    @Mixin(CcaAsmHelper.class) public static class ASMHelper {}
    @Mixin(CcaBootstrap.class) public static class Bootstrap {}
}
