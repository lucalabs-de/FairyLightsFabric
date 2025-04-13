package de.lucalabs.fairylights.items;

import de.lucalabs.fairylights.feature.light.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;

import java.util.function.Function;

public class SimpleLightVariant<T extends LightBehavior> implements LightVariant<T> {
    public static final LightVariant<StandardLightBehavior> FAIRY_LIGHT = new SimpleLightVariant<>(
            true,
            1.0F,
            new Box(-0.138D, -0.138D, -0.138D, 0.138D, 0.138D, 0.138D),
            0.044D, SimpleLightVariant::standardBehavior,
            true);

    public static final LightVariant<BrightnessLightBehavior> OIL_LANTERN = new SimpleLightVariant<>(
            false,
            1.5F,
            new Box(-0.219D, -0.656D, -0.188D, 0.219D, 0.091D, 0.188D),
            0.000D,
            stack -> new TorchLightBehavior(0.13D));

    public static final LightVariant<BrightnessLightBehavior> INCANDESCENT_LIGHT = new SimpleLightVariant<>(
            true,
            1.0F,
            new Box(-0.166D, -0.291D, -0.166D, 0.166D, 0.062D, 0.166D),
            0.103D,
            stack -> new IncandescentBehavior(),
            true);

    private final boolean parallelsCord;

    private final float spacing;

    private final Box bounds;

    private final double floorOffset;

    private final Function<ItemStack, T> behaviorFactory;

    private final boolean orientable;

    SimpleLightVariant(final boolean parallelsCord, final float spacing, final Box bounds, final double floorOffset, final Function<ItemStack, T> behaviorFactory) {
        this(parallelsCord, spacing, bounds, floorOffset, behaviorFactory, false);
    }

    SimpleLightVariant(final boolean parallelsCord, final float spacing, final Box bounds, final double floorOffset, final Function<ItemStack, T> behaviorFactory, final boolean orientable) {
        this.parallelsCord = parallelsCord;
        this.spacing = spacing;
        this.bounds = bounds;
        this.floorOffset = floorOffset;
        this.behaviorFactory = behaviorFactory;
        this.orientable = orientable;
    }

    // TODO check if the commented-out behaviours are necessary for anything
    private static StandardLightBehavior standardBehavior(final ItemStack stack) {
        final BrightnessLightBehavior brightness;
        if (TwinkleBehavior.exists(stack)) {
            brightness = new TwinkleBehavior(0.05F, 40);
        } else {
        brightness = new DefaultBrightnessBehavior();
        }
        final ColorLightBehavior color;
//        if (ColorChangingBehavior.exists(stack)) {
//            color = ColorChangingBehavior.create(stack);
//        } else {
            color = FixedColorBehavior.create(stack);
//        }
        return new CompositeBehavior(brightness, color);
    }

    @Override
    public boolean parallelsCord() {
        return this.parallelsCord;
    }

    @Override
    public float getSpacing() {
        return this.spacing;
    }

    @Override
    public Box getBounds() {
        return this.bounds;
    }

    @Override
    public double getFloorOffset() {
        return this.floorOffset;
    }

    @Override
    public T createBehavior(final ItemStack stack) {
        return this.behaviorFactory.apply(stack);
    }

    @Override
    public boolean isOrientable() {
        return this.orientable;
    }
}
