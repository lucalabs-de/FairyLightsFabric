package de.lucalabs.fairylights.main.items;

import de.lucalabs.fairylights.main.connection.ConnectionTypes;
import de.lucalabs.fairylights.main.items.components.ComponentRecords;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class PennantBuntingConnectionItem extends ConnectionItem {
    public PennantBuntingConnectionItem(final Item.Properties properties) {
        super(properties, ConnectionTypes.PENNANT_BUNTING);
    }

    @Override
    public void appendHoverText(final ItemStack stack, final TooltipContext context, final List<Component> tooltip, final TooltipFlag type) {
        ComponentRecords.ConnectionLogic logic = ComponentRecords.ConnectionLogic.fromItemStack(stack);
        if (logic == null) {
            return;
        }

        if (!logic.pattern().isEmpty()) {
            tooltip.add(Component.empty());
        }

        for (ItemStack item : logic.pattern()) {
            tooltip.add(item.getHoverName());
        }
    }
}
