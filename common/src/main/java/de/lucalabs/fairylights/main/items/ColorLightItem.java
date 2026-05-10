package de.lucalabs.fairylights.main.items;

import de.lucalabs.fairylights.main.blocks.LightBlock;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import static de.lucalabs.fairylights.main.items.components.FairyLightItemComponents.COLORS;

public class ColorLightItem extends LightItem {
    public ColorLightItem(final LightBlock light, final Item.Properties properties) {
        super(light, properties);
    }

    @Override
    public Component getName(final ItemStack stack) {
        List<Integer> colors = stack.get(COLORS);

        if (colors != null) {
            return Component.translatable("format.fairylights.color_changing", super.getName(stack));
        }

        return DyeableItem.getDisplayName(stack, super.getName(stack));
    }
}
