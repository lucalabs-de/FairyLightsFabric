package de.lucalabs.fairylights.items;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.MathHelper;

import java.util.Arrays;
import java.util.Optional;

public final class DyeableItem {
    private DyeableItem() {
    }

    public static Text getColorName(final int color) {
        final int r = color >> 16 & 0xFF;
        final int g = color >> 8 & 0xFF;
        final int b = color & 0xFF;
        DyeColor closest = DyeColor.WHITE;
        int closestDist = Integer.MAX_VALUE;
        for (final DyeColor dye : DyeColor.values()) {
            final int dyeColor = getColor(dye);
            if (dyeColor == color) {
                closest = dye;
                closestDist = 0;
                break;
            }
            final int dr = dyeColor >> 16 & 0xFF;
            final int dg = dyeColor >> 8 & 0xFF;
            final int db = dyeColor & 0xFF;
            final int dist = (dr - r) * (dr - r) + (dg - g) * (dg - g) + (db - b) * (db - b);
            if (dist < closestDist) {
                closest = dye;
                closestDist = dist;
            }
        }
        final Text colorName = Text.translatable("color.fairylights." + closest.getName());
        return closestDist == 0 ? colorName : Text.translatable("format.fairylights.dyed_colored", colorName);
    }

    public static Text getDisplayName(final ItemStack stack, final Text name) {
        return Text.translatable("format.fairylights.colored", getColorName(getColor(stack)), name);
    }

    public static int getColor(final DyeColor color) {
        if (color == DyeColor.BLACK) {
            return 0x323232;
        }
        if (color == DyeColor.GRAY) {
            return 0x606060;
        }
        float[] colors = color.getColorComponents();
        return MathHelper.floor(colors[0] * 255.0F) << 16
                | MathHelper.floor(colors[1] * 255.0F) << 8
                | MathHelper.floor(colors[2] * 255.0F);
    }

    public static Optional<DyeColor> getDyeColor(final ItemStack stack) {
        final int color = getColor(stack);
        return Arrays.stream(DyeColor.values()).filter(dye -> getColor(dye) == color).findFirst();
    }

    public static ItemStack setColor(final ItemStack stack, final DyeColor dye) {
        return setColor(stack, getColor(dye));
    }

    public static ItemStack setColor(final ItemStack stack, final int color) {
        setColor(stack.getOrCreateNbt(), color);
        return stack;
    }

    public static NbtCompound setColor(final NbtCompound tag, final DyeColor dye) {
        return setColor(tag, getColor(dye));
    }

    public static NbtCompound setColor(final NbtCompound tag, final int color) {
        tag.putInt("color", color);
        return tag;
    }

    public static int getColor(final ItemStack stack) {
        final NbtCompound tag = stack.getNbt();
        return tag != null ? getColor(tag) : 0xFFFFFF;
    }

    public static int getColor(final NbtCompound tag) {
        return tag.contains("color", NbtElement.INT_TYPE) ? tag.getInt("color") : 0xFFFFFF;
    }
}
