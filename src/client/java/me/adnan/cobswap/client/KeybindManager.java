package me.adnan.cobswap.client;

import com.mojang.blaze3d.platform.InputConstants;
import me.adnan.cobswap.CobSwap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class KeybindManager {

    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath(CobSwap.MOD_ID, "main")
    );

    public static final KeyMapping TOGGLE_KEY = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.cobswap.toggle",
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_G,
            CATEGORY
    ));

    public static final KeyMapping DELAY_UP_KEY = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.cobswap.delay_up",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_BRACKET,
            CATEGORY
    ));

    public static final KeyMapping DELAY_DOWN_KEY = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.cobswap.delay_down",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_BRACKET,
            CATEGORY
    ));

    public static void register() {

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            while (TOGGLE_KEY.consumeClick()) {
                boolean nowEnabled = ToggleManager.toggle();

                if (client.player != null) {
                    String message = nowEnabled ? "\u00A7aCobSwap: ON" : "\u00A7cCobSwap: OFF";
                    client.player.displayClientMessage(Component.literal(message), true);
                }

                CobSwap.LOGGER.info("CobSwap toggled: {}", nowEnabled ? "ON" : "OFF");
            }

            while (DELAY_UP_KEY.consumeClick()) {
                int newDelay = CobSwapConfig.increaseDelay();
                sendDelayFeedback(client, newDelay);
            }

            while (DELAY_DOWN_KEY.consumeClick()) {
                int newDelay = CobSwapConfig.decreaseDelay();
                sendDelayFeedback(client, newDelay);
            }

        });

    }

    private static void sendDelayFeedback(net.minecraft.client.Minecraft client, int delayTicks) {
        if (client.player != null) {
            client.player.displayClientMessage(
                    Component.literal("\u00A7bCobSwap delay: " + delayTicks + " ticks"),
                    true
            );
        }
        CobSwap.LOGGER.info("CobSwap delay set to {} ticks", delayTicks);
    }
}
