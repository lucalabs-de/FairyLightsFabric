package de.lucalabs.fairylights.items.crafting.ingredient;

import com.google.common.collect.ImmutableList;
import de.lucalabs.fairylights.items.crafting.GenericRecipe;
import de.lucalabs.fairylights.items.crafting.RegularIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;

import java.util.Collections;
import java.util.Objects;

public class BasicRegularIngredient implements RegularIngredient {
    protected final Ingredient ingredient;

    public BasicRegularIngredient(final Ingredient ingredient) {
        this.ingredient = Objects.requireNonNull(ingredient, "ingredient");
    }

    @Override
    public final GenericRecipe.MatchResultRegular matches(final ItemStack input) {
        return new GenericRecipe.MatchResultRegular(this, input, this.ingredient.test(input), Collections.emptyList());
    }

    @Override
    public ImmutableList<ItemStack> getInputs() {
        return this.getMatchingSubtypes(this.ingredient);
    }

    @Override
    public String toString() {
        return this.ingredient.toString();
    }
}
