package de.lucalabs.fairylights.items;

import de.lucalabs.fairylights.feature.light.LightBehavior;
import de.lucalabs.fairylights.registries.FairyLightRegistries;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;

public abstract class LightVariant<T extends LightBehavior> {

    // TODO | this is not great, but something like this is inevitable when serializing generic types. The best approach
    // TODO | would be to eliminate generics from the class hierarchy, but this leads to problems in other places.
    // TODO | The whole architecture should be refactored. There is no reason to store such a large object
    // TODO | as LightVariant + associated LightBehavior in an item component. Ideally, one would store an enum value
    // TODO | that specifies a specific type of light, and corresponding data.
    @SuppressWarnings("unchecked")
    public static <R extends LightBehavior> LightVariant<R> getLightVariant(Identifier id) {
        return (LightVariant<R>) FairyLightRegistries.LIGHT_VARIANTS.get(id);
    }

    public abstract boolean parallelsCord();

    public abstract float getSpacing();

    public abstract Box getBounds();

    public abstract double getFloorOffset();

    public abstract T createBehavior(final ItemStack stack);

    public abstract boolean isOrientable();

    public abstract Identifier getId();
}
