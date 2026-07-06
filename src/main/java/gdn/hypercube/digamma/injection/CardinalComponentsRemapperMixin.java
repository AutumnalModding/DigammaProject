package gdn.hypercube.digamma.injection;

import gdn.hypercube.digamma.util.RemapperHandler;
import gdn.hypercube.solaris.api.SolarisTransformer;
import gdn.hypercube.solaris.util.UsedImplicitly;
import org.ladysnake.cca.internal.base.ComponentRegistryImpl;
import org.ladysnake.cca.internal.base.asm.CcaAsmHelper;
import org.ladysnake.cca.internal.base.asm.CcaBootstrap;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ComponentRegistryImpl.class) @SuppressWarnings("UnstableApiUsage")
public class CardinalComponentsRemapperMixin implements SolarisTransformer.Class {
    @Unique private static final String base = "org/ladysnake/cca/internal/base/";
    @Override public String solaris$target() {return base + "asm/ComponentRegistryImpl"; }
    @Unique @UsedImplicitly void solaris$metadata(ClassNode node) { RemapperHandler.CCA(node); }

    @Mixin(CcaAsmHelper.class) public static class ASMHelper implements Class {
        @Override public String solaris$target() { return base + "asm/CcaBootstrap"; }
        @Unique @UsedImplicitly void solaris$metadata(ClassNode node) { RemapperHandler.CCA(node); }
    }

    @Mixin(CcaBootstrap.class) public static class Bootstrap implements Class {
        @Override public String solaris$target() { return base + "asm/CcaAsmHelper"; }
        @Unique @UsedImplicitly void solaris$metadata(ClassNode node) { RemapperHandler.CCA(node); }
    }
}


