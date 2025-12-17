package de.lucalabs.fairylights.items;

import de.lucalabs.fairylights.feature.light.ColorChangingBehavior;
import de.lucalabs.fairylights.string.StringTypes;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Objects;

import static de.lucalabs.fairylights.items.components.FairyLightItemComponents.PATTERN;
import static de.lucalabs.fairylights.items.components.FairyLightItemComponents.STRING;

public final class ItemColorManager {

    private static final Item[] COLORABLE_LIGHTS = {
            FairyLightItems.FAIRY_LIGHT,
            FairyLightItems.PAPER_LANTERN,
            FairyLightItems.ORB_LANTERN,
            FairyLightItems.FLOWER_LIGHT,
            FairyLightItems.CANDLE_LANTERN_LIGHT,
            FairyLightItems.JACK_O_LANTERN,
            FairyLightItems.SKULL_LIGHT,
            FairyLightItems.GHOST_LIGHT,
            FairyLightItems.SPIDER_LIGHT,
            FairyLightItems.WITCH_LIGHT,
            FairyLightItems.SNOWFLAKE_LIGHT,
            FairyLightItems.HEART_LIGHT,
            FairyLightItems.MOON_LIGHT,
            FairyLightItems.STAR_LIGHT,
            FairyLightItems.ICICLE_LIGHTS,
            FairyLightItems.METEOR_LIGHT
    };

    private static final Item[] HANGING_LIGHTS_LINES = {
            FairyLightItems.HANGING_LIGHTS
    };

    private static final Item[] PENNANT_BUNTING_LINES = {
            FairyLightItems.PENNANT_BUNTING
    };

    private static final Item[] PENNANTS = {
            FairyLightItems.TRIANGLE_PENNANT,
            FairyLightItems.SQUARE_PENNANT
    };

    private ItemColorManager() {
    }

    public static void setupColors() {
        ColorProviderRegistry.ITEM.register((stack, index) -> {
            if (index == 1) {
                if (ColorChangingBehavior.exists(stack)) {
                    return ColorChangingBehavior.animate(stack);
                }
                return DyeableItem.getColor(stack);
            }

            return 0xFFFFFFFF;
        }, COLORABLE_LIGHTS);

        ColorProviderRegistry.ITEM.register((stack, index) -> {
            if (index == 0) {
                if (stack.contains(STRING)) {
                    return Objects.requireNonNull(stack.get(STRING)).color();
                }
                return StringTypes.BLACK_STRING.color();
            }

            final List<ItemStack> pattern = stack.get(PATTERN);
            if (pattern != null && !pattern.isEmpty()) {
                final ItemStack stack2 = pattern.get((index - 1) % pattern.size());
                if (ColorChangingBehavior.exists(stack2)) {
                    return ColorChangingBehavior.animate(stack2);
                }
                return DyeableItem.getColor(stack2);
            }

            return 0xFFFFD584;
        }, HANGING_LIGHTS_LINES);

        ColorProviderRegistry.ITEM.register((stack, index) -> {
            if (index == 0) {
                return 0xFFFFFFFF;
            }
            final List<ItemStack> pattern = stack.get(PATTERN);
            if (pattern != null && !pattern.isEmpty()) {
                final ItemStack light = pattern.get((index - 1) % pattern.size());
                return DyeableItem.getColor(light);
            }
            return 0xFFFFFFFF;
        }, PENNANT_BUNTING_LINES);

        ColorProviderRegistry.ITEM.register(
                (stack, index) -> index == 0 ? 0xFFFFFF : DyeableItem.getColor(stack),
                PENNANTS);
    }
}
