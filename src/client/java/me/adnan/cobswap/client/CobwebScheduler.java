package me.adnan.cobswap.client;

import me.adnan.cobswap.CobSwap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

/**
 * Spaces the swap-place-swap sequence out over real client ticks,
 * using the delay configured in CobSwapConfig, instead of doing
 * everything instantly in one tick.
 */
public class CobwebScheduler {

    private enum Step {
        WAITING_TO_PLACE,
        WAITING_TO_SWITCH_BACK
    }

    private static class Pending {
        LocalPlayer player;
        BlockPos clickedPos;
        Direction clickedFace;
        int originalSlot;
        Step step;
        int ticksRemaining;
    }

    private static Pending pending = null;

    public static boolean isBusy() {
        return pending != null;
    }

    /**
     * Kicks off a new swap-place-swap sequence. Call this the instant a valid
     * cobweb click is detected — it immediately switches to the cobweb slot,
     * then waits CobSwapConfig.getDelayTicks() before actually placing.
     */
    public static void start(LocalPlayer player, BlockPos clickedPos, Direction clickedFace, int cobwebSlot) {
        if (pending != null) {
            return; // a sequence is already running, don't overlap
        }

        Pending p = new Pending();
        p.player = player;
        p.clickedPos = clickedPos;
        p.clickedFace = clickedFace;
        p.originalSlot = player.getInventory().getSelectedSlot();
        p.step = Step.WAITING_TO_PLACE;
        p.ticksRemaining = CobSwapConfig.getDelayTicks();
        pending = p;

        CobwebPlacer.switchToSlot(player, cobwebSlot);
    }

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> tick());
    }

    private static void tick() {
        if (pending == null) {
            return;
        }

        if (pending.ticksRemaining > 0) {
            pending.ticksRemaining--;
            return;
        }

        switch (pending.step) {
            case WAITING_TO_PLACE -> {
                CobwebPlacer.placeAt(pending.player, pending.clickedPos, pending.clickedFace);
                CobSwap.LOGGER.info("CobSwap: placed cobweb at {}", pending.clickedPos.relative(pending.clickedFace));

                pending.step = Step.WAITING_TO_SWITCH_BACK;
                pending.ticksRemaining = CobSwapConfig.getDelayTicks();
            }
            case WAITING_TO_SWITCH_BACK -> {
                CobwebPlacer.switchToSlot(pending.player, pending.originalSlot);
                pending = null;
            }
        }
    }
}
