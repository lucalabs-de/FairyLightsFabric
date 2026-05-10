package de.lucalabs.fairylights.main.items.crafting.ingredient;

import com.google.common.collect.ImmutableList;
import de.lucalabs.fairylights.main.items.crafting.GenericRecipe;
import de.lucalabs.fairylights.main.items.crafting.RegularIngredient;
import java.util.Collections;
import net.minecraft.world.item.ItemStack;

public class EmptyRegularIngredient implements RegularIngredient {
    @Override
    public GenericRecipe.MatchResultRegular matches(final ItemStack input) {
        return new GenericRecipe.MatchResultRegular(this, input, input.isEmpty(), Collections.emptyList());
    }

    @Override
    public ImmutableList<ItemStack> getInputs() {
        return ImmutableList.of();
    }
}
