package de.lucalabs.fairylights.items;

import de.lucalabs.fairylights.components.FairyLightComponents;
import de.lucalabs.fairylights.feature.light.LightBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;

import java.util.Optional;

public interface LightVariant<T extends LightBehavior> {

    boolean parallelsCord();

    float getSpacing();

    Box getBounds();

    double getFloorOffset();

    T createBehavior(final ItemStack stack);

    boolean isOrientable();

    static Optional<LightVariant<?>> get(final ItemStack provider) {
        return FairyLightComponents.LIGHT_VARIANT.get(provider).get();
    }
}
