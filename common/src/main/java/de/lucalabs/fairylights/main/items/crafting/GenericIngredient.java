package de.lucalabs.fairylights.main.items.crafting;

import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

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

    default void present(final PatchedDataComponentMap comps) {
    }

    default void absent(final PatchedDataComponentMap comps) {
    }

    default ImmutableList<ItemStack> getMatchingSubtypes(final Ingredient stack) {
        Objects.requireNonNull(stack, "stack");
        return ImmutableList.copyOf(stack.getItems());
    }

    default List<FormattedText> getTooltip() {
        return Collections.emptyList();
    }
}
