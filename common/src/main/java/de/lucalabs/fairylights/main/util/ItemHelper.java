package de.lucalabs.fairylights.main.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class ItemHelper {
    private ItemHelper() {}

    public static void giveItemToPlayer(Player player, ItemStack stack) {
        boolean added = player.getInventory().add(stack);

        if (!added && !stack.isEmpty()) {
            // Drop the item if the inventory is full
            player.drop(stack, false);
        }
    }
}
