package de.lucalabs.fairylights.util;

import net.minecraft.util.math.ColorHelper;

import java.util.function.Function;

public final class ColorUtils {
    private ColorUtils() {}

    public static int WHITE = ColorHelper.Argb.getArgb(255, 255, 255, 255);

    public static int transformArgb(
            int color,
            Function<Float, Float> fA,
            Function<Float, Float> fR,
            Function<Float, Float> fG,
            Function<Float, Float> fB) {
        float a = ColorHelper.Argb.getAlpha(color) / 255F;
        float r = ColorHelper.Argb.getRed(color) / 255F;
        float g = ColorHelper.Argb.getGreen(color) / 255F;
        float b = ColorHelper.Argb.getBlue(color) / 255F;
        return ColorHelper.Argb.fromFloats(fA.apply(a), fR.apply(r), fG.apply(g), fB.apply(b));
    }
}
