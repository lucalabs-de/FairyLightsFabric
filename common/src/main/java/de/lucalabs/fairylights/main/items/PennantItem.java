package de.lucalabs.fairylights.main.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class PennantItem extends Item {
    public PennantItem(final Item.Properties settings) {
        super(settings);
    }

    @Override
    public Component getName(final ItemStack stack) {
        return DyeableItem.getDisplayName(stack, super.getName(stack));
    }
}
