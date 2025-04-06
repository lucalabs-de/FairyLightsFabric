package de.lucalabs.fairylights.items.crafting.ingredient;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class LazyTagIngredient extends Ingredient {
    private final TagKey<Item> tag;

    private LazyTagIngredient(final TagKey<Item> tag) {
        super(Stream.empty());
        this.tag = tag;
    }

    @Override
    public ItemStack[] getMatchingStacks() {
        return StreamSupport.stream(Registries.ITEM.iterateEntries(this.tag).spliterator(), false).map(ItemStack::new).toArray(ItemStack[]::new);
    }

    @Override
    public boolean test(@Nullable final ItemStack stack) {
        return stack != null && stack.isIn(this.tag);
    }

    @Override
    public IntList getMatchingItemIds() {
        final ItemStack[] stacks = this.getMatchingStacks();
        final IntList list = new IntArrayList(stacks.length);
        for (final ItemStack stack : stacks) {
            list.add(RecipeMatcher.getItemId(stack));
        }
        list.sort(IntComparators.NATURAL_COMPARATOR);
        return list;
    }

    @Override
    public boolean isEmpty() {
        return !Registries.ITEM.iterateEntries(this.tag).iterator().hasNext();
    }

    public static LazyTagIngredient of(final TagKey<Item> tag) {
        return new LazyTagIngredient(tag);
    }
}
