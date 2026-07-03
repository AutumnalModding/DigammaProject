package gdn.hypercube.digamma.messages.server;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PacketHandler {
    public static final Logger LOGGER = LogManager.getLogger("Digamma Project Packet Handler");

    public static void init() {
        PayloadTypeRegistry.clientboundPlay().register(S00LoadEngine.IDENT, S00LoadEngine.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(S01JumpAddress.IDENT, S01JumpAddress.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(S02JumpToLabel.IDENT, S02JumpToLabel.CODEC);
    }
}