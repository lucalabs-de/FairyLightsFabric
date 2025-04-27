package de.lucalabs.fairylights.items;

import de.lucalabs.fairylights.blocks.LightBlock;
import de.lucalabs.fairylights.components.FairyLightComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;

public class ColorLightItem extends LightItem {
    public ColorLightItem(final LightBlock light, final Item.Settings properties) {
        super(light, properties);
    }

    @Override
    public Text getName(final ItemStack stack) {
        if (stack.contains(FairyLightComponents.Dyeable.COLORS)) {
            return Text.translatable("format.fairylights.color_changing", super.getName(stack));
        }
        return DyeableItem.getDisplayName(stack, super.getName(stack));
    }
}
