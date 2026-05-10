package de.lucalabs.fairylights.main.items;

import de.lucalabs.fairylights.main.connection.ConnectionTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public final class TinselConnectionItem extends ConnectionItem
{

    public TinselConnectionItem(final Properties properties) {
        super(properties, ConnectionTypes.TINSEL_GARLAND);
    }

    @Override
    public Component getName(final ItemStack stack) {
        return DyeableItem.getDisplayName(stack, super.getName(stack));
    }
}
