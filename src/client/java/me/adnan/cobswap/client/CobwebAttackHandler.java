package me.adnan.cobswap.client;

import me.adnan.cobswap.CobSwap;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;

public class CobwebAttackHandler {

    public static void register() {

        AttackBlockCallback.EVENT.register((player, level, hand, pos, direction) -> {

            // Only care about our main-hand left clicks while the toggle is on.
            if (!ToggleManager.isEnabled()) {
                return InteractionResult.PASS;
            }

            // Don't start a new sequence while one is already mid-flight.
            if (CobwebScheduler.isBusy()) {
                return InteractionResult.PASS;
            }

            if (!(player instanceof LocalPlayer localPlayer)) {
                return InteractionResult.PASS;
            }

            ItemStack heldItem = player.getMainHandItem();
            boolean holdingSwordOrAxe = heldItem.is(ItemTags.SWORDS) || heldItem.is(ItemTags.AXES);

            if (!holdingSwordOrAxe) {
                return InteractionResult.PASS;
            }

            var placementPos = CobwebPlacementValidator.getValidPlacementPos(player, level, pos, direction);

            if (placementPos == null) {
                return InteractionResult.PASS;
            }

            int cobwebSlot = HotbarScanner.findCobwebSlot(player.getInventory());

            if (cobwebSlot == -1) {
                CobSwap.LOGGER.info(
                        "CobSwap: valid spot at {} but no cobweb in hotbar, skipping",
                        placementPos
                );
                return InteractionResult.PASS;
            }

            CobwebScheduler.start(localPlayer, pos, direction, cobwebSlot);

            // We're handling this click ourselves — cancel vanilla mining entirely.
            return InteractionResult.FAIL;
        });
    }
}
