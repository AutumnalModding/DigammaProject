package gdn.hypercube.digamma.messages.server;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record S01JumpAddress(int address) implements CustomPayload {
    public static final CustomPayload.Id<S01JumpAddress> IDENT = new CustomPayload.Id<>(Identifier.of("epsilon", "jmp"));
    public static final PacketCodec<RegistryByteBuf, S01JumpAddress> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, S01JumpAddress::address, S01JumpAddress::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return IDENT;
    }
}
