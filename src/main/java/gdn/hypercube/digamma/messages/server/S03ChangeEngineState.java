package gdn.hypercube.digamma.messages.server;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record S03ChangeEngineState(String state) implements CustomPayload {
    public static final Id<S03ChangeEngineState> IDENT = new Id<>(Identifier.of("epsilon", "engine_state"));
    public static final PacketCodec<RegistryByteBuf, S03ChangeEngineState> CODEC = PacketCodec.tuple(PacketCodecs.STRING, S03ChangeEngineState::state, S03ChangeEngineState::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return IDENT;
    }
}
