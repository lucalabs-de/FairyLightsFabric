package de.lucalabs.fairylights.main.items;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

import static de.lucalabs.fairylights.main.items.components.FairyLightItemComponents.*;

public final class DyeableItem {
    private DyeableItem() {
    }

    public static Component getColorName(final int color) {
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
        final Component colorName = Component.translatable("color.fairylights." + closest.getName());
        return closestDist == 0 ? colorName : Component.translatable("format.fairylights.dyed_colored", colorName);
    }

    public static Component getDisplayName(final ItemStack stack, final Component name) {
        return Component.translatable("format.fairylights.colored", getColorName(getColor(stack)), name);
    }

    public static int getColor(final DyeColor color) {
        if (color == DyeColor.BLACK) {
            return 0xFF323232;
        }
        if (color == DyeColor.GRAY) {
            return 0xFF606060;
        }

        return color.getTextureDiffuseColor();
    }

    public static Optional<DyeColor> getDyeColor(final ItemStack stack) {
        final int color = getColor(stack);
        return Arrays.stream(DyeColor.values()).filter(dye -> getColor(dye) == color).findFirst();
    }

    public static ItemStack setColor(final ItemStack stack, final DyeColor dye) {
        stack.set(COLOR, getColor(dye));
        return stack;
    }

    public static ItemStack setColor(final ItemStack stack, final int color) {
        stack.set(COLOR, color);
        return stack;
    }

    public static int getColor(final ItemStack stack) {
       return Objects.requireNonNullElse(stack.get(COLOR), 0xFFFFFFFF);
    }

    public static int getColor(final CompoundTag tag) {
        return tag.contains("color", Tag.TAG_INT) ? tag.getInt("color") : 0xFFFFFFFF;
    }
}
