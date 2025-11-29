package de.lucalabs.fairylights.items;

import de.lucalabs.fairylights.blocks.LightBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

import static de.lucalabs.fairylights.items.components.FairyLightItemComponents.COLORS;

public class ColorLightItem extends LightItem {
    public ColorLightItem(final LightBlock light, final Item.Settings properties) {
        super(light, properties);
    }

    @Override
    public Text getName(final ItemStack stack) {
        List<Integer> colors = stack.get(COLORS);

        if (colors != null) {
            return Text.translatable("format.fairylights.color_changing", super.getName(stack));
        }

        return DyeableItem.getDisplayName(stack, super.getName(stack));
    }
}
