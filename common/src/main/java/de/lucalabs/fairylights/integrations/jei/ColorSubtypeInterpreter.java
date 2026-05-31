package de.lucalabs.fairylights.integrations.jei;

import de.lucalabs.fairylights.main.items.components.FairyLightItemComponents;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ColorSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
    @Override
    public @Nullable Object getSubtypeData(ItemStack stack, @NotNull UidContext context) {
        return stack.get(FairyLightItemComponents.COLOR);
    }

    @Override
    public @NotNull String getLegacyStringSubtypeInfo(@NotNull ItemStack stack, @NotNull UidContext context) {
        if (stack.has(FairyLightItemComponents.COLOR)) {
            return String.format("%06x", stack.get(FairyLightItemComponents.COLOR));
        }
        return "";
    }
}
