package gdn.hypercube.digamma.patch.transformers;

import gdn.hypercube.solaris.core.SolarisTransformer;
import gdn.hypercube.solaris.util.UsedImplicitly;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

public class CardinalComponentsRemapper implements SolarisTransformer.Class {
    @Override
    public String internal$transformerTarget() {
        return "org/ladysnake/cca/internal/base/ComponentRegistryImpl";
    }

    void solaris$metadata(ClassNode node) {
        if (!FabricLoader.getInstance().isDevelopmentEnvironment()) return;
        String old = "net/minecraft/resources/Identifier";
        String target = "net/minecraft/util/Identifier";
        for (MethodNode segment : node.methods) {
            segment.desc = segment.desc.replace(old, target);
            for (AbstractInsnNode insn : segment.instructions) {
                switch (insn) {
                    case TypeInsnNode type -> {
                        if (type.desc.equals(old)) type.desc = target;
                    }

                    case MethodInsnNode method -> {
                        if (method.owner.equals(old)) method.owner = target;
                        method.desc = method.desc.replace(old, target);
                    }

                    case FieldInsnNode field -> {
                        if (field.owner.equals(old)) field.owner = target;
                        field.desc = field.desc.replace(old, target);
                    }

                    case LdcInsnNode ldc -> {
                        if (ldc.cst instanceof Type type) {
                            String intern = type.getInternalName();
                            if (intern.equals(old)) {
                                ldc.cst = Type.getObjectType(target);
                            }
                        } else if (ldc.cst instanceof String str && str.equals(old)) {
                            ldc.cst = target;
                        }
                    }

                    default -> {}
                }
            }
        }
    }

    public static class Bootstrap implements SolarisTransformer.Class {
        @UsedImplicitly void solaris$metadata(ClassNode node) {
            new CardinalComponentsRemapper().solaris$metadata(node);
        }

        @Override
        public String internal$transformerTarget() {
            return "org/ladysnake/cca/internal/base/asm/CcaBootstrap";
        }
    }

    public static class ASMHelper implements SolarisTransformer.Class {
        @UsedImplicitly void solaris$metadata(ClassNode node) {
            new CardinalComponentsRemapper().solaris$metadata(node);
        }

        @Override
        public String internal$transformerTarget() {
            return "org/ladysnake/cca/internal/base/asm/CcaAsmHelper";
        }
    }
}
