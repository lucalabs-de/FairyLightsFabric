package de.lucalabs.fairylights.items;

import de.lucalabs.fairylights.connection.ConnectionTypes;
import de.lucalabs.fairylights.items.components.ComponentRecords;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;

public class PennantBuntingConnectionItem extends ConnectionItem {
    public PennantBuntingConnectionItem(final Item.Settings properties) {
        super(properties, ConnectionTypes.PENNANT_BUNTING);
    }

    @Override
    public void appendTooltip(final ItemStack stack, final TooltipContext context, final List<Text> tooltip, final TooltipType type) {
        ComponentRecords.ConnectionLogic logic = ComponentRecords.ConnectionLogic.fromItemStack(stack);
        if (logic == null) {
            return;
        }

        if (!logic.pattern().isEmpty()) {
            tooltip.add(Text.empty());
        }

        for (ItemStack item : logic.pattern()) {
            tooltip.add(item.getName());
        }
    }
}
