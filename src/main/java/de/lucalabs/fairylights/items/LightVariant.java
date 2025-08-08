package de.lucalabs.fairylights.items;

import de.lucalabs.fairylights.components.FairyLightComponents;
import de.lucalabs.fairylights.feature.light.LightBehavior;
import dev.onyxstudios.cca.api.v3.component.ComponentProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;

import java.util.Optional;

public abstract class LightVariant<T extends LightBehavior> {

    public abstract boolean parallelsCord();

    public abstract float getSpacing();

    public abstract Box getBounds();

    public abstract double getFloorOffset();

    public abstract T createBehavior(final ItemStack stack);

    public abstract boolean isOrientable();

    public static synchronized Optional<LightVariant<?>> get(final ItemStack provider) {
        return FairyLightComponents.LIGHT_VARIANT.get(provider).get();
    }
}
