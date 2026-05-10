package de.lucalabs.fairylights.main.feature.light;

import de.lucalabs.fairylights.main.items.DyeableItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FixedColorBehavior implements ColorLightBehavior {
    private final float red;
    private final float green;
    private final float blue;

    public FixedColorBehavior(final float red, final float green, final float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public static ColorLightBehavior create(final ItemStack stack) {
        final int rgb = DyeableItem.getColor(stack);
        final float red = (rgb >> 16 & 0xFF) / 255.0F;
        final float green = (rgb >> 8 & 0xFF) / 255.0F;
        final float blue = (rgb & 0xFF) / 255.0F;
        return new FixedColorBehavior(red, green, blue);
    }

    @Override
    public float getRed(final float delta) {
        return this.red;
    }

    @Override
    public float getGreen(final float delta) {
        return this.green;
    }

    @Override
    public float getBlue(final float delta) {
        return this.blue;
    }

    @Override
    public void power(final boolean powered, final boolean now, final Light<?> light) {
    }

    @Override
    public void tick(final Level world, final Vec3 origin, final Light<?> light) {
    }
}
