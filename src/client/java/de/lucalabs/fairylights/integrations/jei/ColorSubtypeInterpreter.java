package de.lucalabs.fairylights.integrations.jei;

import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.NotNull;

public final class ColorSubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {
    @Override
    public @NotNull String apply(final ItemStack stack, final UidContext context) {
        final NbtCompound compound = stack.getNbt();
        if (compound != null && compound.contains("color", NbtElement.INT_TYPE)) {
            return String.format("%06x", compound.getInt("color"));
        }
        return NONE;
    }
}
