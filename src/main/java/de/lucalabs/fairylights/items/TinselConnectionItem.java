package de.lucalabs.fairylights.items;

import de.lucalabs.fairylights.connection.ConnectionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public final class TinselConnectionItem extends ConnectionItem
{

    public TinselConnectionItem(final Settings properties) {
        super(properties, ConnectionTypes.TINSEL_GARLAND);
    }

    @Override
    public Text getName(final ItemStack stack) {
        return DyeableItem.getDisplayName(stack, super.getName(stack));
    }
}
