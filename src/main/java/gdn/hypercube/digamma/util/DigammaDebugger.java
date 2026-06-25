package gdn.hypercube.digamma.util;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import gdn.hypercube.digamma.init.DigammaProjectLoader;
import gdn.hypercube.digamma.messages.server.S01JumpAddress;
import gdn.hypercube.digamma.messages.server.S02JumpToLabel;
import gdn.hypercube.digamma.messages.server.S03ChangeEngineState;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;

public class DigammaDebugger {
    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, _, _) -> dispatcher.register(CommandManager.literal("epsilon")
                .then(CommandManager.literal("jumpAddress")
                        .requires(source -> CommandManager.requirePermissionLevel(CommandManager.OWNERS_CHECK).test(source))
                        .then(CommandManager.argument("address", IntegerArgumentType.integer())
                                .executes(context -> {
                                    int address = IntegerArgumentType.getInteger(context, "address");
                                    ServerPlayerEntity target = context.getSource().getPlayer();
                                    DigammaProjectLoader.LOGGER.info("Sending jump request to {} with address {}", target, address);
                                    if (target != null) {
                                        S01JumpAddress payload = new S01JumpAddress(address);
                                        ServerPlayNetworking.send(target, payload);
                                    }
                                    return 0;
                                })
                        ))
                .then(CommandManager.literal("jumpLabel")
                        .requires(source -> CommandManager.requirePermissionLevel(CommandManager.OWNERS_CHECK).test(source))
                        .then(CommandManager.argument("label", StringArgumentType.string())
                                .executes(context -> {
                                    String label = StringArgumentType.getString(context, "label");
                                    ServerPlayerEntity target = context.getSource().getPlayer();
                                    DigammaProjectLoader.LOGGER.info("Sending jump request to {} with label {}", target, label);
                                    if (target != null) {
                                        S02JumpToLabel payload = new S02JumpToLabel(label);
                                        ServerPlayNetworking.send(target, payload);
                                    }
                                    return 0;
                                })
                        ))
                .then(CommandManager.literal("state")
                        .requires(source -> CommandManager.requirePermissionLevel(CommandManager.OWNERS_CHECK).test(source))
                        .then(CommandManager.argument("state", StringArgumentType.string())
                                .executes(context -> {
                                    String state = StringArgumentType.getString(context, "state");
                                    ServerPlayerEntity target = context.getSource().getPlayer();
                                    DigammaProjectLoader.LOGGER.info("Sending state change request to {} with target state {}", target, state);
                                    if (target != null) {
                                        S03ChangeEngineState payload = new S03ChangeEngineState(state);
                                        ServerPlayNetworking.send(target, payload);
                                    }
                                    return 0;
                                })
                        ))
        ));
    }
}
