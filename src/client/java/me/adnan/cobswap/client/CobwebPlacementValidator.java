package me.adnan.cobswap.client;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class CobwebPlacementValidator {

    // How far around the placement spot we look for a living target.
    // Keeps this combat-scoped rather than firing on any random mined block.
    private static final double ENTITY_SEARCH_RADIUS = 3.0;

    /**
     * Returns the position a cobweb would be placed at, or null if
     * this isn't a valid combat cobweb placement.
     */
    public static BlockPos getValidPlacementPos(Player player, Level level, BlockPos clickedPos, Direction clickedFace) {

        BlockPos placementPos = clickedPos.relative(clickedFace);

        // Space has to actually be empty for a cobweb to go there.
        if (!level.getBlockState(placementPos).isAir()) {
            return null;
        }

        // Must be near a living entity (not the player themself) to count as combat-relevant.
        AABB searchBox = new AABB(placementPos).inflate(ENTITY_SEARCH_RADIUS);
        List<LivingEntity> nearby = level.getEntitiesOfClass(LivingEntity.class, searchBox,
                entity -> entity != player && entity.isAlive());

        if (nearby.isEmpty()) {
            return null;
        }

        return placementPos;
    }
}
