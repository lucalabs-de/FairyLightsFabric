package de.lucalabs.fairylights.util;

import de.lucalabs.fairylights.FairyLights;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public final class Tags {
    public static final TagKey<Item> DYES = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "dyes"));
    public static final TagKey<Item> DYES_WHITE = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "dyes/white"));
    public static final TagKey<Item> DYES_ORANGE = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "dyes/orange"));
    public static final TagKey<Item> DYES_MAGENTA = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "dyes/magenta"));
    public static final TagKey<Item> DYES_LIGHT_BLUE = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "dyes/light_blue"));
    public static final TagKey<Item> DYES_YELLOW = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "dyes/yellow"));
    public static final TagKey<Item> DYES_LIME = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "dyes/lime"));
    public static final TagKey<Item> DYES_PINK = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "dyes/pink"));
    public static final TagKey<Item> DYES_GRAY = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "dyes/gray"));
    public static final TagKey<Item> DYES_LIGHT_GRAY = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "dyes/light_gray"));
    public static final TagKey<Item> DYES_CYAN = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "dyes/cyan"));
    public static final TagKey<Item> DYES_PURPLE = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "dyes/purple"));
    public static final TagKey<Item> DYES_BLUE = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "dyes/blue"));
    public static final TagKey<Item> DYES_BROWN = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "dyes/brown"));
    public static final TagKey<Item> DYES_GREEN = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "dyes/green"));
    public static final TagKey<Item> DYES_RED = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "dyes/red"));
    public static final TagKey<Item> DYES_BLACK = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "dyes/black"));

    public static final TagKey<Block> FENCES = TagKey.of(RegistryKeys.BLOCK, Identifier.of("c", "fences"));

    public static final TagKey<Item> LIGHTS = TagKey.of(RegistryKeys.ITEM, Identifier.of(FairyLights.ID, "lights"));
    public static final TagKey<Item> TWINKLING_LIGHTS = TagKey.of(RegistryKeys.ITEM, Identifier.of(FairyLights.ID, "twinkling_lights"));
    public static final TagKey<Item> PENNANTS = TagKey.of(RegistryKeys.ITEM, Identifier.of(FairyLights.ID, "pennants"));
    public static final TagKey<Item> DYEABLE = TagKey.of(RegistryKeys.ITEM, Identifier.of(FairyLights.ID, "dyeable"));
    public static final TagKey<Item> DYEABLE_LIGHTS = TagKey.of(RegistryKeys.ITEM, Identifier.of(FairyLights.ID, "dyeable_lights"));

    private Tags() {
    }

    public static void initialize() {
        FairyLights.LOGGER.info("Initializing tags");
    }
}
