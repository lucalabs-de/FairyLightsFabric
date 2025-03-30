package de.lucalabs.fairylights.components;

import de.lucalabs.fairylights.items.LightVariant;
import net.minecraft.nbt.NbtCompound;

public class LightVariantComponent extends GenericComponent<LightVariant<?>> {
    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        // no serialization needed // TODO check if that's true
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        // no serialization needed
    }
}
