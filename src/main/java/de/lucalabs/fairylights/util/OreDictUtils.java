package de.lucalabs.fairylights.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.DyeColor;

public final class OreDictUtils {
    private OreDictUtils() {
    }

    public static boolean isDye(final ItemStack stack) {
        if (!stack.isEmpty()) {
            if (stack.getItem() instanceof DyeItem) {
                return true;
            }
            return stack.isIn(Tags.DYES);
        }
        return false;
    }

    public static DyeColor getDyeColor(final ItemStack stack) {
        if (!stack.isEmpty()) {
            if (stack.getItem() instanceof DyeItem) {
                return ((DyeItem) stack.getItem()).getColor();
            }
            for (final Dye dye : Dye.values()) {
                if (stack.isIn(dye.getName())) {
                    return dye.getColor();
                }
            }
        }
        return DyeColor.YELLOW;
    }

    public static ImmutableList<ItemStack> getDyes(final DyeColor color) {
        return getDyeItemStacks().get(color).asList();
    }

    public static ImmutableList<ItemStack> getAllDyes() {
        return getDyeItemStacks().values().asList();
    }

    private static ImmutableMultimap<DyeColor, ItemStack> getDyeItemStacks() {
        final ImmutableMultimap.Builder<DyeColor, ItemStack> bob = ImmutableMultimap.builder();
        for (final Dye dye : Dye.values()) {
            for (final RegistryEntry<Item> holder : Registries.ITEM.iterateEntries(dye.getName())) {
                bob.put(dye.getColor(), new ItemStack(holder));
            }
        }
        return bob.build();
    }

    private enum Dye {
        WHITE(Tags.DYES_WHITE, DyeColor.WHITE),
        ORANGE(Tags.DYES_ORANGE, DyeColor.ORANGE),
        MAGENTA(Tags.DYES_MAGENTA, DyeColor.MAGENTA),
        LIGHT_BLUE(Tags.DYES_LIGHT_BLUE, DyeColor.LIGHT_BLUE),
        YELLOW(Tags.DYES_YELLOW, DyeColor.YELLOW),
        LIME(Tags.DYES_LIME, DyeColor.LIME),
        PINK(Tags.DYES_PINK, DyeColor.PINK),
        GRAY(Tags.DYES_GRAY, DyeColor.GRAY),
        LIGHT_GRAY(Tags.DYES_LIGHT_GRAY, DyeColor.LIGHT_GRAY),
        CYAN(Tags.DYES_CYAN, DyeColor.CYAN),
        PURPLE(Tags.DYES_PURPLE, DyeColor.PURPLE),
        BLUE(Tags.DYES_BLUE, DyeColor.BLUE),
        BROWN(Tags.DYES_BROWN, DyeColor.BROWN),
        GREEN(Tags.DYES_GREEN, DyeColor.GREEN),
        RED(Tags.DYES_RED, DyeColor.RED),
        BLACK(Tags.DYES_BLACK, DyeColor.BLACK);

        private final TagKey<Item> name;

        private final DyeColor color;

        Dye(final TagKey<Item> name, final DyeColor color) {
            this.name = name;
            this.color = color;
        }

        private TagKey<Item> getName() {
            return this.name;
        }

        private DyeColor getColor() {
            return this.color;
        }
    }
}
