package de.lucalabs.fairylights.items.crafting;

import com.google.common.collect.ImmutableList;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface GenericIngredient<I extends GenericIngredient<I, M>, M extends GenericRecipe.MatchResult<I, M>> {
    /**
     * Provides an immutable list of stacks that will match this ingredient.
     *
     * @return Immutable list of potential inputs for this ingredient
     */
    ImmutableList<ItemStack> getInputs();

    /**
     * Provides an immutable list of stacks which are required to craft the given output stack.
     * <p>
     * Only auxiliary ingredients should provide multiple.
     * <p>
     * Must be overriden by implementors which modify the output stack to provide accurate recipes for JEI.
     *
     * @return Immutable copy of stacks required to produce output
     */
    default ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
        return ImmutableList.of(this.getInputs());
    }

    M matches(final ItemStack input);

    default boolean dictatesOutputType() {
        return false;
    }

    default void present(final ComponentMapImpl comps) {
    }

    default void absent(final ComponentMapImpl comps) {
    }

    default ImmutableList<ItemStack> getMatchingSubtypes(final Ingredient stack) {
        Objects.requireNonNull(stack, "stack");
        return ImmutableList.copyOf(stack.getMatchingStacks());
    }

    default List<StringVisitable> getTooltip() {
        return Collections.emptyList();
    }
}
