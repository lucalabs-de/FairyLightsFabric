package de.lucalabs.fairylights.items.crafting;

import net.minecraft.component.ComponentMapImpl;
import net.minecraft.item.ItemStack;

public interface RegularIngredient extends GenericIngredient<RegularIngredient, GenericRecipe.MatchResultRegular> {
    default void matched(final ItemStack ingredient, final ComponentMapImpl comps) {}
}
