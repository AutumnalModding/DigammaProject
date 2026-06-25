package gdn.hypercube.digamma.messages.server;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record S02JumpToLabel(String label) implements CustomPayload {
    public static final Id<S02JumpToLabel> IDENT = new Id<>(Identifier.of("epsilon", "jmp_label"));
    public static final PacketCodec<RegistryByteBuf, S02JumpToLabel> CODEC = PacketCodec.tuple(PacketCodecs.STRING, S02JumpToLabel::label, S02JumpToLabel::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return IDENT;
    }
}
