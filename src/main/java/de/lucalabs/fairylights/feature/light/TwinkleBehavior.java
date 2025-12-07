package de.lucalabs.fairylights.feature.light;

import de.lucalabs.fairylights.util.CubicBezier;
import de.lucalabs.fairylights.util.MathHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static de.lucalabs.fairylights.items.components.FairyLightItemComponents.TWINKLE;

public class TwinkleBehavior implements BrightnessLightBehavior {
    private static final CubicBezier EASE_IN_OUT = new CubicBezier(0.4F, 0, 0.6F, 1);

    private final TwinkleLogic logic;

    private boolean powered = true;

    public TwinkleBehavior(final float chance, final int duration) {
        this.logic = new TwinkleLogic(chance, duration);
    }

    public static boolean exists(final ItemStack stack) {
        return Boolean.TRUE.equals(stack.get(TWINKLE));
    }

    @Override
    public float getBrightness(final float delta) {
        if (this.powered) {
            final float x = this.logic.get(delta);
            return x < 0.25F ? 1.0F - EASE_IN_OUT.eval(x / 0.25F) : EASE_IN_OUT.eval(MathHelper.transform(x, 0.25F, 1.0F, 0.0F, 1.0F));
        }
        return 0.0F;
    }

    @Override
    public void power(final boolean powered, final boolean now, final Light<?> light) {
        this.powered = powered;
    }

    @Override
    public void tick(final World world, final Vec3d origin, final Light<?> light) {
        this.logic.tick(world.random, this.powered);
    }
}
