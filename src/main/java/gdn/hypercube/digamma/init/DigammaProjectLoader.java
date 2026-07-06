package gdn.hypercube.digamma.init;

import gdn.hypercube.digamma.component.PlayerEventFlagsComponent;
import gdn.hypercube.digamma.messages.server.S00LoadEngine;
import gdn.hypercube.digamma.messages.server.PacketHandler;
import gdn.hypercube.digamma.util.DigammaDebugger;
import java.io.IOException;
import java.io.InputStream;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public class DigammaProjectLoader implements ModInitializer, EntityComponentInitializer {
	public static final Logger LOGGER = LogManager.getLogger("Digamma Project");

    public static final ComponentKey<PlayerEventFlagsComponent> EVENT_FLAGS =
            ComponentRegistry.getOrCreate(Identifier.of("digamma", "flags"), PlayerEventFlagsComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(EVENT_FLAGS, PlayerEventFlagsComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }

	@Override
    @SuppressWarnings("DataFlowIssue")
	public void onInitialize() {
		LOGGER.info("Initializing...");

		PacketHandler.init();
		DigammaDebugger.init();

        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, _) -> {
            MinecraftServer server = player.getEntityWorld().getServer();
            ResourceManager manager = server.getResourceManager();
            try {
                LOGGER.info("Loading CCScript binary...");
                InputStream stream = manager.getInputStreamOrThrow(Identifier.of("digamma", "ccscript.bin"));
                byte[] data = stream.readAllBytes();
                S00LoadEngine payload = new S00LoadEngine(data);
                ServerPlayNetworking.send(player, payload);
            } catch (IOException exception) {
                LOGGER.fatal("Failed loading CCScript binary - kicking player {}!!", player.getPlayerListName(), exception);
                player.networkHandler.disconnect(Text.of("Failed loading CCScript binary, ask your server administrator."));
            }
        });
	}
}