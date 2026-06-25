package gdn.hypercube.digamma.messages.server;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record S00LoadEngine(byte[] binary) implements CustomPayload {
    public static final CustomPayload.Id<S00LoadEngine> IDENT = new CustomPayload.Id<>(Identifier.of("epsilon", "load"));
    public static final PacketCodec<RegistryByteBuf, S00LoadEngine> CODEC = PacketCodec.tuple(PacketCodecs.BYTE_ARRAY, S00LoadEngine::binary, S00LoadEngine::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return IDENT;
    }
}
