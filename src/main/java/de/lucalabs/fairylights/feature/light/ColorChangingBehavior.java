package de.lucalabs.fairylights.feature.light;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

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
        final NbtCompound tag = stack.getNbt();
        if (tag == null) {
            return new FixedColorBehavior(1.0F, 1.0F, 1.0F);
        }
        final NbtList list = tag.getList("colors", NbtElement.INT_TYPE);
        final float[] red = new float[list.size()];
        final float[] green = new float[list.size()];
        final float[] blue = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            final int color = list.getInt(i);
            red[i] = (color >> 16 & 0xFF) / 255.0F;
            green[i] = (color >> 8 & 0xFF) / 255.0F;
            blue[i] = (color & 0xFF) / 255.0F;
        }
        return new ColorChangingBehavior(red, green, blue, list.size() / 960.0F);
    }

    public static int animate(final ItemStack stack) {
        final NbtCompound tag = stack.getNbt();
        if (tag == null) {
            return 0xFFFFFF;
        }
        final NbtList list = tag.getList("colors", NbtElement.INT_TYPE);
        if (list.isEmpty()) {
            return 0xFFFFFF;
        }
        if (list.size() == 1) {
            return list.getInt(0);
        }
        final float p = de.lucalabs.fairylights.util.MathHelper.mod(Util.getMeasuringTimeMs() * (20.0F / 1000.0F) * (list.size() / 960.0F), list.size());
        final int i = (int) p;
        final int c0 = list.getInt(i % list.size());
        final float r0 = (c0 >> 16 & 0xFF) / 255.0F;
        final float g0 = (c0 >> 8 & 0xFF) / 255.0F;
        final float b0 = (c0 & 0xFF) / 255.0F;
        final int c1 = list.getInt((i + 1) % list.size());
        final float r1 = (c1 >> 16 & 0xFF) / 255.0F;
        final float g1 = (c1 >> 8 & 0xFF) / 255.0F;
        final float b1 = (c1 & 0xFF) / 255.0F;
        return (int) (MathHelper.lerp(p - i, r0, r1) * 255.0F) << 16 |
                (int) (MathHelper.lerp(p - i, g0, g1) * 255.0F) << 8 |
                (int) (MathHelper.lerp(p - i, b0, b1) * 255.0F);
    }

    public static boolean exists(final ItemStack stack) {
        final NbtCompound tag = stack.getNbt();
        return tag != null && tag.contains("colors", NbtElement.LIST_TYPE);
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
