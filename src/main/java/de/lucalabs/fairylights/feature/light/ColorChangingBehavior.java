package de.lucalabs.fairylights.feature.light;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

import static de.lucalabs.fairylights.items.components.FairyLightItemComponents.COLORS;

public class ColorChangingBehavior implements ColorLightBehavior {
    private final float[] red;
    private final float[] green;
    private final float[] blue;

    private final float rate;

    private boolean powered;

    public ColorChangingBehavior(final float[] red, final float[] green, final float[] blue, final float rate) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.rate = rate;
    }

    public static ColorLightBehavior create(final ItemStack stack) {
        List<Integer> colors = stack.get(COLORS);
        if (colors == null) {
            return new FixedColorBehavior(1.0F, 1.0F, 1.0F);
        }
        final float[] red = new float[colors.size()];
        final float[] green = new float[colors.size()];
        final float[] blue = new float[colors.size()];
        for (int i = 0; i < colors.size(); i++) {
            final int color = colors.get(i);
            red[i] = (color >> 16 & 0xFF) / 255.0F;
            green[i] = (color >> 8 & 0xFF) / 255.0F;
            blue[i] = (color & 0xFF) / 255.0F;
        }
        return new ColorChangingBehavior(red, green, blue, colors.size() / 960.0F);
    }

    public static int animate(final ItemStack stack) {
        List<Integer> colors = stack.get(COLORS);
        if (colors == null || colors.isEmpty()) {
            return 0xFFFFFF;
        }
        if (colors.size() == 1) {
            return colors.getFirst();
        }
        final float p = de.lucalabs.fairylights.util.MathHelper.mod(Util.getMeasuringTimeMs() * (20.0F / 1000.0F) * (colors.size() / 960.0F), colors.size());
        final int i = (int) p;
        final int c0 = colors.get(i % colors.size());
        final float r0 = (c0 >> 16 & 0xFF) / 255.0F;
        final float g0 = (c0 >> 8 & 0xFF) / 255.0F;
        final float b0 = (c0 & 0xFF) / 255.0F;
        final int c1 = colors.get((i + 1) % colors.size());
        final float r1 = (c1 >> 16 & 0xFF) / 255.0F;
        final float g1 = (c1 >> 8 & 0xFF) / 255.0F;
        final float b1 = (c1 & 0xFF) / 255.0F;
        return (int) (MathHelper.lerp(p - i, r0, r1) * 255.0F) << 16 |
                (int) (MathHelper.lerp(p - i, g0, g1) * 255.0F) << 8 |
                (int) (MathHelper.lerp(p - i, b0, b1) * 255.0F);
    }

    public static boolean exists(final ItemStack stack) {
        return stack.contains(COLORS);
    }

    @Override
    public float getRed(final float delta) {
        return this.get(this.red, delta);
    }

    @Override
    public float getGreen(final float delta) {
        return this.get(this.green, delta);
    }

    @Override
    public float getBlue(final float delta) {
        return this.get(this.blue, delta);
    }

    private float get(final float[] values, final float delta) {
        final float p = this.powered ? de.lucalabs.fairylights.util.MathHelper.mod(Util.getMeasuringTimeMs() * (20.0F / 1000.0F) * this.rate, values.length) : 0.0F;
        final int i = (int) p;
        return MathHelper.lerp(p - i, values[i % values.length], values[(i + 1) % values.length]);
    }

    @Override
    public void power(final boolean powered, final boolean now, final Light<?> light) {
        this.powered = powered;
    }

    @Override
    public void tick(final World world, final Vec3d origin, final Light<?> light) {
    }
}
