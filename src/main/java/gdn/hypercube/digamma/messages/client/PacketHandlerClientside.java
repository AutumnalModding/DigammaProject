package gdn.hypercube.digamma.messages.client;

import gdn.hypercube.digamma.delta.core.DeltaProtocolBootSequence;
import gdn.hypercube.digamma.messages.server.PacketHandler;
import gdn.hypercube.digamma.messages.server.S00LoadEngine;
import gdn.hypercube.digamma.messages.server.S01JumpAddress;
import gdn.hypercube.digamma.messages.server.S02JumpToLabel;
import gdn.hypercube.digamma.messages.server.S03ChangeEngineState;
import gdn.hypercube.epsilon.core.EpsilonEngine;
import java.nio.ByteBuffer;
import java.util.HexFormat;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class PacketHandlerClientside {
    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(S00LoadEngine.IDENT, (payload, _) -> {
            byte[] data = payload.binary();
            HexFormat hex = HexFormat.ofDelimiter(" ");

            PacketHandler.LOGGER.debug("Dumping CCScript binary...");
            for (int i = 0; i < data.length; i += 15) {
                int end = Math.min(i + 15, data.length);
                byte[] chunk = java.util.Arrays.copyOfRange(data, i, end);
                PacketHandler.LOGGER.debug(hex.formatHex(chunk));
            }

            DeltaProtocolBootSequence.ENGINE.loadBin(ByteBuffer.wrap(data));
        });
        ClientPlayNetworking.registerGlobalReceiver(S01JumpAddress.IDENT, (payload, _) -> DeltaProtocolBootSequence.ENGINE.jump(payload.address()));
        ClientPlayNetworking.registerGlobalReceiver(S02JumpToLabel.IDENT, (payload, _) -> DeltaProtocolBootSequence.ENGINE.jump(payload.label()));
    }
}
