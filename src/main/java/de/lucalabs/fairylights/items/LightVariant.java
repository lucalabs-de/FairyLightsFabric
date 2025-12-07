package de.lucalabs.fairylights.items;

import de.lucalabs.fairylights.feature.light.LightBehavior;
import de.lucalabs.fairylights.items.components.ComponentRecords;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public abstract class LightVariant<T extends LightBehavior> {

    public abstract boolean parallelsCord();

    public abstract float getSpacing();

    public abstract Box getBounds();

    public abstract double getFloorOffset();

    public abstract T createBehavior(final ItemStack stack);

    public abstract boolean isOrientable();

    @Contract(value = "null -> null; !null -> !null")
    public static <R extends LightBehavior> LightVariant<R> getLightVariant(@Nullable ComponentRecords.LightVariantWrapper data) {
        if (data == null) {
            return null;
        }
        return null; // TODO
    }

    public ComponentRecords.LightVariantWrapper wrap() {

    }
}
