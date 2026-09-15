package me.adnan.cobswap.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Individual, reusable steps of the swap-place-swap sequence.
 * Split apart so CobwebScheduler can space them out over real ticks
 * instead of firing all three in a single instant.
 */
public class CobwebPlacer {

    public static void switchToSlot(LocalPlayer player, int slot) {
        Minecraft client = Minecraft.getInstance();
        if (client.getConnection() == null) {
            return;
        }
        player.getInventory().setSelectedSlot(slot);
        client.getConnection().send(new ServerboundSetCarriedItemPacket(slot));
    }

    public static void placeAt(LocalPlayer player, BlockPos clickedPos, Direction clickedFace) {
        Minecraft client = Minecraft.getInstance();
        if (client.gameMode == null) {
            return;
        }

        Vec3 hitVec = Vec3.atCenterOf(clickedPos).add(
                clickedFace.getStepX() * 0.5,
                clickedFace.getStepY() * 0.5,
                clickedFace.getStepZ() * 0.5
        );
        BlockHitResult hitResult = new BlockHitResult(hitVec, clickedFace, clickedPos, false);
        client.gameMode.useItemOn(player, InteractionHand.MAIN_HAND, hitResult);
    }
}
