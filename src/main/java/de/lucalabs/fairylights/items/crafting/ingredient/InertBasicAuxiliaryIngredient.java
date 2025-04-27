package de.lucalabs.fairylights.items.crafting.ingredient;

import net.minecraft.component.ComponentMapImpl;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import org.jetbrains.annotations.Nullable;

public class InertBasicAuxiliaryIngredient extends BasicAuxiliaryIngredient<Void> {
    public InertBasicAuxiliaryIngredient(final Ingredient ingredient, final boolean isRequired, final int limit) {
        super(ingredient, isRequired, limit);
    }

    public InertBasicAuxiliaryIngredient(final Ingredient ingredient) {
        super(ingredient, true, Integer.MAX_VALUE);
    }

    @Nullable
    @Override
    public final Void accumulator() {
        return null;
    }

    @Override
    public final void consume(final Void v, final ItemStack ingredient) {}

    @Override
    public final boolean finish(final Void v, final ComponentMapImpl comps) {
        return false;
    }
}
