package de.lucalabs.fairylights.items;

import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.feature.light.*;
import de.lucalabs.fairylights.items.components.FairyLightItemComponents;
import de.lucalabs.fairylights.registries.FairyLightRegistries;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;

import java.util.Objects;
import java.util.function.Function;

public class SimpleLightVariant<T extends LightBehavior> extends LightVariant<T> {
    private static final Identifier FAIRY_LIGHT_ID = Identifier.of(FairyLights.ID, "var_fairy_light");
    public static final LightVariant<StandardLightBehavior> FAIRY_LIGHT = Registry.register(
            FairyLightRegistries.LIGHT_VARIANTS,
            FAIRY_LIGHT_ID,
            new SimpleLightVariant<>(
                    FAIRY_LIGHT_ID,
                    true,
                    1.0F,
                    new Box(-0.138D, -0.138D, -0.138D, 0.138D, 0.138D, 0.138D),
                    0.044D,
                    SimpleLightVariant::standardBehavior,
                    true)
    );

    private static final Identifier OIL_LANTERN_ID = Identifier.of(FairyLights.ID, "var_oil_lantern");
    public static final LightVariant<BrightnessLightBehavior> OIL_LANTERN = Registry.register(
            FairyLightRegistries.LIGHT_VARIANTS,
            OIL_LANTERN_ID,
            new SimpleLightVariant<>(
                    OIL_LANTERN_ID,
                    false,
                    1.5F,
                    new Box(-0.219D, -0.656D, -0.188D, 0.219D, 0.091D, 0.188D),
                    0.000D,
                    stack -> new TorchLightBehavior(0.13D))
    );

    private static final Identifier INCANDESCENT_LIGHT_ID = Identifier.of(FairyLights.ID, "var_incandescent");
    public static final LightVariant<BrightnessLightBehavior> INCANDESCENT_LIGHT = Registry.register(
            FairyLightRegistries.LIGHT_VARIANTS,
            INCANDESCENT_LIGHT_ID,
            new SimpleLightVariant<>(
                    INCANDESCENT_LIGHT_ID,
                    true,
                    1.0F,
                    new Box(-0.166D, -0.291D, -0.166D, 0.166D, 0.062D, 0.166D),
                    0.103D,
                    stack -> new IncandescentBehavior(),
                    true)
    );

    private final Identifier id;

    private final boolean parallelsCord;

    private final float spacing;

    private final Box bounds;

    private final double floorOffset;

    private final Function<ItemStack, T> behaviorFactory;

    private final boolean orientable;

    SimpleLightVariant(final Identifier id, final boolean parallelsCord, final float spacing, final Box bounds, final double floorOffset, final Function<ItemStack, T> behaviorFactory) {
        this(id, parallelsCord, spacing, bounds, floorOffset, behaviorFactory, false);
    }

    SimpleLightVariant(final Identifier id, final boolean parallelsCord, final float spacing, final Box bounds, final double floorOffset, final Function<ItemStack, T> behaviorFactory, final boolean orientable) {
        this.id = id;
        this.parallelsCord = parallelsCord;
        this.spacing = spacing;
        this.bounds = bounds;
        this.floorOffset = floorOffset;
        this.behaviorFactory = behaviorFactory;
        this.orientable = orientable;
    }

    public static LightVariant<?> getLightVariantOrDefault(ItemStack i) {
        LightVariant<?> variant = LightVariant.getLightVariant(i.get(FairyLightItemComponents.LIGHT_VARIANT));
        return Objects.requireNonNullElse(variant, FAIRY_LIGHT);
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
    public Identifier getId() {
        return id;
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
