package de.lucalabs.fairylights.main.items.crafting.ingredient;

import com.google.common.collect.ImmutableList;
import de.lucalabs.fairylights.main.items.crafting.GenericRecipe;
import de.lucalabs.fairylights.main.items.crafting.RegularIngredient;
import java.util.Collections;
import java.util.Objects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

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
