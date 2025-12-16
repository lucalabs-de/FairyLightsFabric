package de.lucalabs.fairylights.items;

import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.blocks.FairyLightBlocks;
import de.lucalabs.fairylights.blocks.LightBlock;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class FairyLightItems {

    public static final ConnectionItem HANGING_LIGHTS = register("hanging_lights", () -> new HangingLightsConnectionItem(defaultProperties()));
    public static final ConnectionItem PENNANT_BUNTING = register("pennant_bunting", () -> new PennantBuntingConnectionItem(defaultProperties()));

    public static final ConnectionItem TINSEL = register("tinsel", () -> new TinselConnectionItem(defaultProperties()));
    public static final ConnectionItem GARLAND = register("garland", () -> new GarlandConnectionItem(defaultProperties()));

    public static final LightItem FAIRY_LIGHT = register("fairy_light", createColorLight(FairyLightBlocks.FAIRY_LIGHT));
    public static final LightItem PAPER_LANTERN = register("paper_lantern", createColorLight(FairyLightBlocks.PAPER_LANTERN));
    public static final LightItem ORB_LANTERN = register("orb_lantern", createColorLight(FairyLightBlocks.ORB_LANTERN));
    public static final LightItem FLOWER_LIGHT = register("flower_light", createColorLight(FairyLightBlocks.FLOWER_LIGHT));
    public static final LightItem CANDLE_LANTERN_LIGHT = register("candle_lantern_light", createColorLight(FairyLightBlocks.CANDLE_LANTERN_LIGHT));
    public static final LightItem JACK_O_LANTERN = register("jack_o_lantern", createColorLight(FairyLightBlocks.JACK_O_LANTERN));
    public static final LightItem SKULL_LIGHT = register("skull_light", createColorLight(FairyLightBlocks.SKULL_LIGHT));
    public static final LightItem GHOST_LIGHT = register("ghost_light", createColorLight(FairyLightBlocks.GHOST_LIGHT));
    public static final LightItem SPIDER_LIGHT = register("spider_light", createColorLight(FairyLightBlocks.SPIDER_LIGHT));
    public static final LightItem WITCH_LIGHT = register("witch_light", createColorLight(FairyLightBlocks.WITCH_LIGHT));
    public static final LightItem SNOWFLAKE_LIGHT = register("snowflake_light", createColorLight(FairyLightBlocks.SNOWFLAKE_LIGHT));
    public static final LightItem HEART_LIGHT = register("heart_light", createColorLight(FairyLightBlocks.HEART_LIGHT));
    public static final LightItem MOON_LIGHT = register("moon_light", createColorLight(FairyLightBlocks.MOON_LIGHT));
    public static final LightItem STAR_LIGHT = register("star_light", createColorLight(FairyLightBlocks.STAR_LIGHT));
    public static final LightItem ICICLE_LIGHTS = register("icicle_lights", createColorLight(FairyLightBlocks.ICICLE_LIGHTS));
    public static final LightItem METEOR_LIGHT = register("meteor_light", createColorLight(FairyLightBlocks.METEOR_LIGHT));
    public static final LightItem CANDLE_LANTERN = register("candle_lantern", createLight(FairyLightBlocks.CANDLE_LANTERN, LightItem::new));
    public static final LightItem OIL_LANTERN = register("oil_lantern", createLight(FairyLightBlocks.OIL_LANTERN, LightItem::new));
    public static final LightItem INCANDESCENT_LIGHT = register("incandescent_light", createLight(FairyLightBlocks.INCANDESCENT_LIGHT, LightItem::new));

    public static final Item TRIANGLE_PENNANT = register("triangle_pennant", () -> new PennantItem(defaultProperties()));
    public static final Item SQUARE_PENNANT = register("square_pennant", () -> new PennantItem(defaultProperties()));

    private FairyLightItems() {
    }

    private static Item.Settings defaultProperties() {
        return new Item.Settings();
    }

    private static Supplier<LightItem> createLight(
            final LightBlock block,
            final BiFunction<LightBlock, Item.Settings, LightItem> factory) {
        return () -> factory.apply(block, defaultProperties().maxCount(16));
    }

    private static Supplier<LightItem> createColorLight(final LightBlock block) {
        return createLight(block, ColorLightItem::new);
    }

    private static <T extends Item> T register(final String name, Supplier<T> supplier) {
        Identifier identifier = Identifier.of(FairyLights.ID, name);
        return Registry.register(Registries.ITEM, identifier, supplier.get());
    }

    public static void initialize() {
        FairyLights.LOGGER.info("Registering items");
    }

    public static Stream<LightItem> lights() {
        return Registries.ITEM.getEntrySet().stream()
                .map(Map.Entry::getValue)
                .filter(LightItem.class::isInstance)
                .map(LightItem.class::cast);
    }
}
