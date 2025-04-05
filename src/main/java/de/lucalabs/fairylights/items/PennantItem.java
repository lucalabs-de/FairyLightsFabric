package de.lucalabs.fairylights.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class PennantItem extends Item {
    public PennantItem(final Item.Settings settings) {
        super(settings);
    }

    @Override
    public Text getName(final ItemStack stack) {
        return DyeableItem.getDisplayName(stack, super.getName(stack));
    }
}
