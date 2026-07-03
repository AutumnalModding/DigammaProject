package gdn.hypercube.digamma.messages.server;

import gdn.hypercube.digamma.delta.core.DeltaProtocolBootSequence;
import gdn.hypercube.epsilon.core.EpsilonEngine;
import gdn.hypercube.solaris.messaging.SolarisPacket;
import gdn.hypercube.solaris.util.UsedImplicitly;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record S03ChangeEngineState(String state) implements SolarisPacket {
    public static final Id<S03ChangeEngineState> IDENT = new Id<>(Identifier.of("epsilon", "s03"));
    public static final PacketCodec<RegistryByteBuf, S03ChangeEngineState> CODEC = PacketCodec.tuple(PacketCodecs.STRING, S03ChangeEngineState::state, S03ChangeEngineState::new);

    public Direction direction() {
        return Direction.client;
    }

    public Handler<? extends SolarisPacket> handler() {
        return (payload, _) -> {
            if (payload instanceof S03ChangeEngineState(String target)) {
                DeltaProtocolBootSequence.ENGINE.status = EpsilonEngine.Status.valueOf(target);
            }
        };
    }

    @UsedImplicitly
    public S03ChangeEngineState() {
        this("HALTED");
    }

    @Override
    public PacketCodec<RegistryByteBuf, ? extends SolarisPacket> codec() {
        return CODEC;
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return IDENT;
    }

    @Override
    public Id<? extends SolarisPacket> ident() {
        return IDENT;
    }
}
