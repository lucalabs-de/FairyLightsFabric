package de.lucalabs.fairylights.integrations.jei;

import de.lucalabs.fairylights.items.components.FairyLightItemComponents;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ColorSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {

    @Override
    public @Nullable Object getSubtypeData(ItemStack stack, UidContext uidContext) {
        return stack.get(FairyLightItemComponents.COLOR);
    }

    @Override
    public @NotNull String getLegacyStringSubtypeInfo(ItemStack stack, UidContext uidContext) {
        if (stack.contains(FairyLightItemComponents.COLOR)) {
            return String.format("%06x", stack.get(FairyLightItemComponents.COLOR));
        }

        return "";
    }
}
