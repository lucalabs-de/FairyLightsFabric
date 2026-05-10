package de.lucalabs.fairylights.main.util;

import java.util.function.Function;
import net.minecraft.util.FastColor;

public final class ColorUtils {
    private ColorUtils() {}

    public static int WHITE = FastColor.ARGB32.color(255, 255, 255, 255);

    public static int transformArgb(
            int color,
            Function<Float, Float> fA,
            Function<Float, Float> fR,
            Function<Float, Float> fG,
            Function<Float, Float> fB) {
        float a = FastColor.ARGB32.alpha(color) / 255F;
        float r = FastColor.ARGB32.red(color) / 255F;
        float g = FastColor.ARGB32.green(color) / 255F;
        float b = FastColor.ARGB32.blue(color) / 255F;
        return FastColor.ARGB32.colorFromFloat(fA.apply(a), fR.apply(r), fG.apply(g), fB.apply(b));
    }
}
