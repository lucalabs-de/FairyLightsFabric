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
    public static final TagKey<Item> DYES_ORANGE = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "orange_dyes"));
    public static final TagKey<Item> DYES_MAGENTA = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "magenta_dyes"));
    public static final TagKey<Item> DYES_LIGHT_BLUE = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "light_blue_dyes"));
    public static final TagKey<Item> DYES_YELLOW = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "yellow_dyes"));
    public static final TagKey<Item> DYES_LIME = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "lime_dyes"));
    public static final TagKey<Item> DYES_PINK = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "pink_dyes"));
    public static final TagKey<Item> DYES_GRAY = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "gray_dyes"));
    public static final TagKey<Item> DYES_LIGHT_GRAY = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "light_gray_dyes"));
    public static final TagKey<Item> DYES_CYAN = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "cyan_dyes"));
    public static final TagKey<Item> DYES_PURPLE = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "purple_dyes"));
    public static final TagKey<Item> DYES_BLUE = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "blue_dyes"));
    public static final TagKey<Item> DYES_BROWN = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "brown_dyes"));
    public static final TagKey<Item> DYES_GREEN = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "green_dyes"));
    public static final TagKey<Item> DYES_RED = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "red_dyes"));
    public static final TagKey<Item> DYES_BLACK = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "black_dyes"));

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
