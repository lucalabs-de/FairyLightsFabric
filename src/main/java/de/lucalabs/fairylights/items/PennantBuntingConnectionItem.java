package de.lucalabs.fairylights.items;

import de.lucalabs.fairylights.components.FairyLightComponents;
import de.lucalabs.fairylights.connection.ConnectionTypes;
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
        final var items = stack.get(FairyLightComponents.Connection.PATTERN);
        if (items != null) {
            if (!items.isEmpty()) {
                tooltip.add(Text.empty());
            }
            for (ItemStack item : items) {
                tooltip.add(item.getName());
            }
        }
    }
}
