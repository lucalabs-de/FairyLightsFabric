package de.lucalabs.fairylights.items;

import de.lucalabs.fairylights.components.FairyLightComponents;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;

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

        return color.getEntityColor();
    }

    public static Optional<DyeColor> getDyeColor(final ItemStack stack) {
        final int color = getColor(stack);
        return Arrays.stream(DyeColor.values()).filter(dye -> getColor(dye) == color).findFirst();
    }

    public static ItemStack setColor(final ItemStack stack, final DyeColor dye) {
        return setColor(stack, getColor(dye));
    }

    public static ItemStack setColor(final ItemStack stack, final int color) {
        stack.set(FairyLightComponents.Dyeable.COLOR, color);
        return stack;
    }

    public static ComponentMapImpl setColor(final ComponentMapImpl comps, final DyeColor dye) {
        return setColor(comps, getColor(dye));
    }

    public static ComponentMapImpl setColor(final ComponentMapImpl comps, final int color) {
        comps.set(FairyLightComponents.Dyeable.COLOR, color);
        return comps;
    }

    public static int getColor(final ItemStack stack) {
        final var color = stack.get(FairyLightComponents.Dyeable.COLOR);
        return color != null ? color : 0xFFFFFF;
    }
}
