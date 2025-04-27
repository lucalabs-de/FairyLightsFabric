package de.lucalabs.fairylights.integrations.jei;

import de.lucalabs.fairylights.components.FairyLightComponents;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class ColorSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
    @Override
    public @NotNull Object getSubtypeData(ItemStack itemStack, UidContext uidContext) {
        return getLegacyStringSubtypeInfo(itemStack, uidContext);
    }

    @Override
    public @NotNull String getLegacyStringSubtypeInfo(ItemStack itemStack, UidContext uidContext) {
        var color = itemStack.get(FairyLightComponents.Dyeable.COLOR);
        if (color != null) {
            return String.format("%06x", color);
        }
        return "";
    }
}
