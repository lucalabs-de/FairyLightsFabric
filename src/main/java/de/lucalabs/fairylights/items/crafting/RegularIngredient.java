package de.lucalabs.fairylights.items.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public interface RegularIngredient extends GenericIngredient<RegularIngredient, GenericRecipe.MatchResultRegular> {
    default void matched(final ItemStack ingredient, final NbtCompound nbt) {}
}
