package gdn.hypercube.digamma.delta;

import gdn.hypercube.digamma.delta.command.DeltaProtocolCommands;
import gdn.hypercube.digamma.delta.input.DeltaProtocolInputConsumer;
import gdn.hypercube.digamma.messages.client.PacketHandlerClientside;
import gdn.hypercube.epsilon.core.EpsilonEngine;
import gdn.hypercube.epsilon.core.util.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.window.Window;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

public class DeltaProtocolBootSequence implements ClientModInitializer {
    public static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final List<DeltaProtocolInputConsumer> keybinds = new ArrayList<>();
    public static final Logger LOGGER = LogManager.getLogger("Digamma Project Delta Protocol");

    protected static Map<Integer, DeltaProtocolDrawInfo> DPDI = new TreeMap<>();
    public static boolean DRAW_MAIN = true;
    public static boolean DRAW_MENU = false;
    public static Object LOCATION = null;

    private static final DeltaProtocolMainHandler PLATFORM = new DeltaProtocolMainHandler();
    public static final List<Supplier<String>> VARIABLES = new ArrayList<>();
    public static final EpsilonEngine ENGINE = new EpsilonEngine(PLATFORM);

    @Override
    public void onInitializeClient() {
        new DeltaProtocolCommands();
        ENGINE.speed = EpsilonEngine.Speed.FAST / 4;
        VARIABLES.add(() -> CLIENT.player.getStringifiedName());

        keybinds.add(new DeltaProtocolInputConsumer("decision_next", GLFW.GLFW_KEY_DOWN, () -> {
            if (!DRAW_MENU) return;
            int max = ENGINE.decisions.size() - 1;
            int target = ENGINE.decindex + 1;
            if (target > max) target = 0;
            if (target < 0) target = max;
            ENGINE.decindex = target;
        }));

        keybinds.add(new DeltaProtocolInputConsumer("decision_previous", GLFW.GLFW_KEY_UP, () -> {
            if (!DRAW_MENU) return;
            int max = ENGINE.decisions.size() - 1;
            int target = ENGINE.decindex - 1;
            if (target > max) target = 0;
            if (target < 0) target = max;
            ENGINE.decindex = target;
        }));

        keybinds.add(new DeltaProtocolInputConsumer("continue", GLFW.GLFW_KEY_ENTER, () -> {
            switch (ENGINE.status) {
                case HALTED:
                case PAUSED:
                case WAITING:
                    ENGINE.status = EpsilonEngine.Status.RUNNING;
                    DRAW_MENU = false;
                    break;

                default: break;
            }
        }));

        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, Identifier.of("digamma", "epsilon"), (context, counter) -> {
            PLATFORM.COUNTER.value = counter;
            if (DRAW_MAIN) {
                this.draw(context);
            }
        });

        ClientTickEvents.START_CLIENT_TICK.register(_ -> {
            if (LOCATION == null) {
                LOCATION = DrawPosition.CENTER;
            }

            Window window = CLIENT.getWindow();
            DrawPosition old = (DrawPosition) LOCATION;
            if (old.x == DrawPosition.CENTER.x && old.y == DrawPosition.CENTER.y) {
                DrawPosition.CENTER = new DrawPosition((window.getWidth() / 4) - (328 / 2), (window.getHeight() / 4) - (72 / 2));
                LOCATION = DrawPosition.TOP_LEFT;
            }
            DrawPosition.CENTER = new DrawPosition((window.getWidth() / 4) - (328 / 2), (window.getHeight() / 4) - (72 / 2));

            ENGINE.drawX = ((DrawPosition) LOCATION).x + 77;
            ENGINE.drawY = ((DrawPosition) LOCATION).y + 5;
            ENGINE.draw();
            ENGINE.update();
        });

        ClientTickEvents.END_CLIENT_TICK.register(_ -> keybinds.forEach(bind -> {
            if (bind.binding.isPressed()) {
                if (!bind.pressed) bind.executor.run();
                bind.pressed = true;
            } else {
                bind.pressed = false;
            }
        }));

        PacketHandlerClientside.init();
        LOGGER.info("Firing on all cylinders!");
    }

    private void draw(DrawContext context) {
        context.drawTexture(
            RenderPipelines.GUI_TEXTURED, Identifier.of("digamma", "gui/dialog_frame.png"),
            ((DrawPosition) LOCATION).x, ((DrawPosition) LOCATION).y,
            0F, 0F,
            328, 72, 328, 72
        );

        for (Map.Entry<Integer, DeltaProtocolDrawInfo> entry : DPDI.entrySet()) {
            int line = entry.getKey();
            DeltaProtocolDrawInfo info = entry.getValue();

            int offset = 0;
            for (Pair<String, Integer> target : info.targets()) {
                context.drawTextWithShadow(
                    CLIENT.textRenderer,
                    target.left(),
                    info.x() + offset,
                    info.y() + (11 * line),
                    target.right()
                );
                offset += CLIENT.textRenderer.getWidth(target.left());
            }
        }

        if (DRAW_MENU) {
            context.drawTexture(
                RenderPipelines.GUI_TEXTURED, Identifier.of("digamma", "gui/menu_frame.png"),
                ((DrawPosition) LOCATION).x + 264, ((DrawPosition) LOCATION).y,
                0F, 0F,
                72, 72, 72, 72
            );

            ENGINE.decisions.forEach((index, decision) -> context.drawTextWithShadow(
                CLIENT.textRenderer,
                (ENGINE.decindex == index ? "> " : "") + decision.left().replace("\00", ""),
                ((DrawPosition) LOCATION).x + 270,
                ((DrawPosition) LOCATION).y + 6 + (11 * index),
                0xFFFFFFFF
            ));
        }
    }

    public record DrawPosition(int x, int y) {
        public static DrawPosition CENTER;
        public static final DrawPosition TOP_LEFT = new DrawPosition(0, 0);

        static {
            Window window = CLIENT.getWindow();
            CENTER = new DrawPosition((window.getWidth() / 4) - (328 / 2), (window.getHeight() / 4) - (72 / 2));
        }
    }
}
