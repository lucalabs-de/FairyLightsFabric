package de.lucalabs.fairylights.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public final class ItemHelper {
    private ItemHelper() {}

    public static void giveItemToPlayer(PlayerEntity player, ItemStack stack) {
        boolean added = player.getInventory().insertStack(stack);

        if (!added && !stack.isEmpty()) {
            // Drop the item if the inventory is full
            player.dropItem(stack, false);
        }
    }
}
