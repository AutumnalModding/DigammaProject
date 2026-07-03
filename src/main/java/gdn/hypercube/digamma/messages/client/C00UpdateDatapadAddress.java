package gdn.hypercube.digamma.messages.client;

import gdn.hypercube.digamma.content.item.DatapadItem;
import gdn.hypercube.solaris.messaging.SolarisPacket;
import gdn.hypercube.solaris.util.UsedImplicitly;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record C00UpdateDatapadAddress(int address) implements SolarisPacket {
    public static final Id<C00UpdateDatapadAddress> IDENT = new Id<>(Identifier.of("epsilon", "c00"));
    public static final PacketCodec<RegistryByteBuf, C00UpdateDatapadAddress> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, C00UpdateDatapadAddress::address, C00UpdateDatapadAddress::new);

    @UsedImplicitly
    public C00UpdateDatapadAddress() {
        this(0);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return IDENT;
    }

    public Direction direction() {
        return Direction.server;
    }

    public Handler<? extends SolarisPacket> handler() {
        return (payload, context) -> {
            if (payload instanceof C00UpdateDatapadAddress(int target)) {
                PlayerEntity player = context.player();
                ItemStack stack = player.getStackInHand(player.getActiveHand());
                if (stack.getItem() instanceof DatapadItem) {
                    stack.set(DatapadItem.ADDRESS, target);
                }
            }
        };
    }

    @Override
    public PacketCodec<RegistryByteBuf, ? extends SolarisPacket> codec() {
        return CODEC;
    }

    @Override
    public Id<? extends SolarisPacket> ident() {
        return IDENT;
    }
}
