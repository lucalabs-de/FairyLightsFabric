package de.lucalabs.fairylights.main.util;

import de.lucalabs.fairylights.Common;
import de.lucalabs.fairylights.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class Tags {
    public static final TagKey<Item> DYES = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "dyes"));
    public static final TagKey<Item> DYES_WHITE = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "dyes/white"));
    public static final TagKey<Item> DYES_ORANGE = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "dyes/orange"));
    public static final TagKey<Item> DYES_MAGENTA = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "dyes/magenta"));
    public static final TagKey<Item> DYES_LIGHT_BLUE = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "dyes/light_blue"));
    public static final TagKey<Item> DYES_YELLOW = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "dyes/yellow"));
    public static final TagKey<Item> DYES_LIME = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "dyes/lime"));
    public static final TagKey<Item> DYES_PINK = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "dyes/pink"));
    public static final TagKey<Item> DYES_GRAY = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "dyes/gray"));
    public static final TagKey<Item> DYES_LIGHT_GRAY = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "dyes/light_gray"));
    public static final TagKey<Item> DYES_CYAN = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "dyes/cyan"));
    public static final TagKey<Item> DYES_PURPLE = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "dyes/purple"));
    public static final TagKey<Item> DYES_BLUE = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "dyes/blue"));
    public static final TagKey<Item> DYES_BROWN = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "dyes/brown"));
    public static final TagKey<Item> DYES_GREEN = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "dyes/green"));
    public static final TagKey<Item> DYES_RED = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "dyes/red"));
    public static final TagKey<Item> DYES_BLACK = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "dyes/black"));

    public static final TagKey<Block> FENCES = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", "fences"));

    public static final TagKey<Item> LIGHTS = TagKey.create(Registries.ITEM, Common.id("lights"));
    public static final TagKey<Item> TWINKLING_LIGHTS = TagKey.create(Registries.ITEM, Common.id("twinkling_lights"));
    public static final TagKey<Item> PENNANTS = TagKey.create(Registries.ITEM, Common.id("pennants"));
    public static final TagKey<Item> DYEABLE = TagKey.create(Registries.ITEM, Common.id("dyeable"));
    public static final TagKey<Item> DYEABLE_LIGHTS = TagKey.create(Registries.ITEM, Common.id("dyeable_lights"));

    private Tags() {
    }

    public static void initialize() {
        Constants.LOG.info("Initializing tags");
    }
}
