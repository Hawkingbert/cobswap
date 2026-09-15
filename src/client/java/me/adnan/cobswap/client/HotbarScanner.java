package me.adnan.cobswap.client;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;

public class HotbarScanner {

    private static final int HOTBAR_SIZE = 9;

    /**
     * Scans hotbar slots 0-8 for a cobweb.
     * Returns the slot index (0-8) if found, or -1 if no cobweb is in the hotbar.
     */
    public static int findCobwebSlot(Inventory inventory) {
        for (int slot = 0; slot < HOTBAR_SIZE; slot++) {
            if (inventory.getItem(slot).is(Items.COBWEB)) {
                return slot;
            }
        }
        return -1;
    }
}
