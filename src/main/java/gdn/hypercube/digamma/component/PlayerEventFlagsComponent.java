package gdn.hypercube.digamma.component;

import java.util.BitSet;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.entity.C2SSelfMessagingComponent;

@SuppressWarnings("UnstableApiUsage")
public class PlayerEventFlagsComponent implements C2SSelfMessagingComponent, AutoSyncedComponent {
    private final PlayerEntity player;
    private BitSet flags = new BitSet(0xFFFFFF);

    public PlayerEventFlagsComponent(PlayerEntity player) {
        this.player = player;
    }

    public void set(char index, boolean value) {
        this.flags.set(index, value);
        sendC2SMessage(buf -> {
            buf.writeEnumConstant(MessageType.UPDATE);
            buf.writeChar(index);
            buf.writeBoolean(value);
        });
    }

    public boolean get(char index) {
        boolean value = false;
        try {
            value = this.flags.get(index);
        } catch (IndexOutOfBoundsException ignored) {}
        return value;
    }

    public void sync() {
        sendC2SMessage(buf -> {
            buf.writeEnumConstant(MessageType.RESYNC);
            buf.writeBitSet(this.flags);
        });
    }

    @Override
    public void handleC2SMessage(@NotNull RegistryByteBuf buffer) {
        MessageType type = buffer.readEnumConstant(MessageType.class);

        switch (type) {
            case UPDATE -> {
                char flag = buffer.readChar();
                boolean value = buffer.readBoolean();
                this.flags.set(flag, value);
            }

            case RESYNC -> this.flags = buffer.readBitSet();
        }
    }

    @Override public void writeData(WriteView view) { view.putLongArray("flags", this.flags.toLongArray()); }
    @Override public boolean shouldSyncWith(@NonNull ServerPlayerEntity player) { return player == this.player; }
    @Override public void readData(ReadView view) { this.flags = BitSet.valueOf(view.getOptionalLongArray("flags").orElseGet(() -> new long[]{})); }

    enum MessageType {
        RESYNC,
        UPDATE
    }
}