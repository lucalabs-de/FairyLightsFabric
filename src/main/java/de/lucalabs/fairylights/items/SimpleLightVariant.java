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
    public static final LightVariant<StandardLightBehavior> FAIRY_LIGHT = getRegistered(
            "var_fairy_light",
            true,
            1.0F,
            new Box(-0.138D, -0.138D, -0.138D, 0.138D, 0.138D, 0.138D),
            0.044D,
            SimpleLightVariant::standardBehavior,
            true
    );

    public static final LightVariant<StandardLightBehavior> PAPER_LANTERN = getRegistered(
            "var_paper_lantern",
            false,
            1.0F,
            new Box(-0.250D, -0.906D, -0.250D, 0.250D, 0.091D, 0.250D),
            0.000D,
            SimpleLightVariant::standardBehavior);

    public static final LightVariant<StandardLightBehavior> ORB_LANTERN = getRegistered(
            "var_orb_lantern",
            false,
            1.0F,
            new Box(-0.262D, -0.512D, -0.262D, 0.262D, 0.091D, 0.262D),
            0.044D,
            SimpleLightVariant::standardBehavior);

    public static final LightVariant<StandardLightBehavior> FLOWER_LIGHT = getRegistered(
            "var_flower_light",
            true,
            1.0F,
            new Box(-0.483D, -0.227D, -0.483D, 0.436D, 0.185D, 0.436D),
            0.069D,
            SimpleLightVariant::standardBehavior,
            true);

    public static final LightVariant<StandardLightBehavior> CANDLE_LANTERN_LIGHT = getRegistered(
            "var_candle_lantern",
            false,
            1.5F,
            new Box(-0.198D, -0.531D, -0.198D, 0.198D, 0.091D, 0.198D),
            0.000D,
            SimpleLightVariant::standardBehavior);

    public static final LightVariant<StandardLightBehavior> JACK_O_LANTERN = getRegistered(
            "var_jack_o_lantern",
            true,
            1.0F,
            new Box(-0.231D, -0.419D, -0.231D, 0.231D, 0.122D, 0.231D),
            0.044D,
            SimpleLightVariant::standardBehavior);

    public static final LightVariant<StandardLightBehavior> SKULL_LIGHT = getRegistered(
            "var_skull_light",
            true,
            1.0F,
            new Box(-0.200D, -0.404D, -0.200D, 0.200D, 0.122D, 0.200D), 0.044D,
            SimpleLightVariant::standardBehavior);

    public static final LightVariant<StandardLightBehavior> GHOST_LIGHT = getRegistered(
            "var_ghost_light",
            true,
            1.0F,
            new Box(-0.270D, -0.390D, -0.270D, 0.270D, 0.169D, 0.270D), 0.075D,
            SimpleLightVariant::standardBehavior);

    public static final LightVariant<StandardLightBehavior> SPIDER_LIGHT = getRegistered(
            "var_spider_light",
            true,
            1.0F,
            new Box(-0.575D, -0.834D, -0.200D, 0.575D, 0.122D, 0.200D),
            0.060D,
            SimpleLightVariant::standardBehavior);

    public static final LightVariant<StandardLightBehavior> WITCH_LIGHT = getRegistered(
            "var_witch_light",
            true,
            1.0F,
            new Box(-0.294D, -0.419D, -0.294D, 0.294D, 0.173D, 0.294D),
            0.044D,
            SimpleLightVariant::standardBehavior);

    public static final LightVariant<StandardLightBehavior> SNOWFLAKE_LIGHT = getRegistered(
            "var_snowflake_light",
            true,
            1.0F,
            new Box(-0.518D, -1.050D, -0.082D, 0.517D, 0.072D, 0.082D),
            0.044D,
            SimpleLightVariant::standardBehavior);

    public static final LightVariant<StandardLightBehavior> HEART_LIGHT = getRegistered(
            "var_heart_light",
            true,
            1.0F,
            new Box(-0.280D, -0.408D, -0.106D, 0.274D, 0.063D, 0.106D),
            0.062D,
            SimpleLightVariant::standardBehavior,
            true);

    public static final LightVariant<StandardLightBehavior> MOON_LIGHT = getRegistered(
            "var_moon_light",
            true,
            1.0F,
            new Box(-0.200D, -0.669D, -0.144D, 0.300D, 0.063D, 0.144D),
            0.044D,
            SimpleLightVariant::standardBehavior,
            true);

    public static final LightVariant<StandardLightBehavior> STAR_LIGHT = getRegistered(
            "var_star_light",
            true,
            1.0F,
            new Box(-0.200D, -0.669D, -0.144D, 0.300D, 0.063D, 0.144D),
            0.044D,
            SimpleLightVariant::standardBehavior,
            true);

    public static final LightVariant<MultiLightBehavior> ICICLE_LIGHTS = getRegistered(
            "var_icicle_lights",
            false,
            0.625F,
            new Box(-0.264D, -1.032D, -0.253D, 0.276D, 0.091D, 0.266D), 0.012D,
            stack -> MultiLightBehavior.create(4, () -> standardBehavior(stack)));

    public static final LightVariant<MeteorLightBehavior> METEOR_LIGHT = getRegistered(
            "var_meteor_light",
            false,
            1.5F,
            new Box(-0.090D, -1.588D, -0.090D, 0.090D, 0.091D, 0.090D),
            0.000D,
            stack -> {
                final ColorLightBehavior color;
                if (ColorChangingBehavior.exists(stack)) {
                    color = ColorChangingBehavior.create(stack);
                } else {
                    color = FixedColorBehavior.create(stack);
                }
                return new MeteorLightBehavior(color);
            });

    public static final LightVariant<BrightnessLightBehavior> CANDLE_LANTERN = getRegistered(
            "var_candle_lantern",
            false,
            1.5F, new Box(-0.198D, -0.531D, -0.198D, 0.198D, 0.091D, 0.198D),
            0.000D,
            stack -> new TorchLightBehavior(0.2D));

    public static final LightVariant<BrightnessLightBehavior> OIL_LANTERN = getRegistered(
            "var_oil_lantern",
            false,
            1.5F,
            new Box(-0.219D, -0.656D, -0.188D, 0.219D, 0.091D, 0.188D),
            0.000D,
            stack -> new TorchLightBehavior(0.13D));

    public static final LightVariant<BrightnessLightBehavior> INCANDESCENT_LIGHT = getRegistered(
            "var_incandescent",
            true,
            1.0F,
            new Box(-0.166D, -0.291D, -0.166D, 0.166D, 0.062D, 0.166D),
            0.103D,
            stack -> new IncandescentBehavior(),
            true);

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
        if (ColorChangingBehavior.exists(stack)) {
            color = ColorChangingBehavior.create(stack);
        } else {
            color = FixedColorBehavior.create(stack);
        }
        return new CompositeBehavior(brightness, color);
    }

    private static <T extends LightBehavior> LightVariant<T> getRegistered(String id, final boolean parallelsCord, final float spacing, final Box bounds, final double floorOffset, final Function<ItemStack, T> behaviorFactory) {
        return getRegistered(id, parallelsCord, spacing, bounds, floorOffset, behaviorFactory, false);
    }

    private static <T extends LightBehavior> LightVariant<T> getRegistered(String id, final boolean parallelsCord, final float spacing, final Box bounds, final double floorOffset, final Function<ItemStack, T> behaviorFactory, final boolean orientable) {
        Identifier identifier = Identifier.of(FairyLights.ID, id);
        return Registry.register(
                FairyLightRegistries.LIGHT_VARIANTS,
                identifier,
                new SimpleLightVariant<>(
                        identifier,
                        parallelsCord,
                        spacing,
                        bounds,
                        floorOffset,
                        behaviorFactory,
                        orientable
                ));
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
