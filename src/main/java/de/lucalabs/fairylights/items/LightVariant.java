package de.lucalabs.fairylights.items;

import de.lucalabs.fairylights.feature.light.LightBehavior;
import de.lucalabs.fairylights.registries.FairyLightRegistries;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;

public abstract class LightVariant<T extends LightBehavior> {

    public abstract boolean parallelsCord();

    public abstract float getSpacing();

    public abstract Box getBounds();

    public abstract double getFloorOffset();

    public abstract LightBehavior createBehavior(final ItemStack stack);

    public abstract boolean isOrientable();

    public abstract Identifier getId();

    public static <R extends LightBehavior> LightVariant<R> getLightVariant(Identifier id) {
        return FairyLightRegistries.LIGHT_VARIANTS.get(id);
    }
}
