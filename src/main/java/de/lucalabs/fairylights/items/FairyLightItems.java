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

import static de.lucalabs.fairylights.items.components.FairyLightItemComponents.LIGHT_VARIANT;

public final class FairyLightItems {

    public static final ConnectionItem HANGING_LIGHTS = register("hanging_lights", () -> new HangingLightsConnectionItem(defaultProperties()));
    public static final ConnectionItem PENNANT_BUNTING = register("pennant_bunting", () -> new PennantBuntingConnectionItem(defaultProperties()));

    public static final LightItem FAIRY_LIGHT = register("fairy_light", createColorLight(FairyLightBlocks.FAIRY_LIGHT));
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
        return () -> factory.apply(block, defaultProperties().maxCount(16).component(LIGHT_VARIANT, block.getVariant().getId()));
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
