package de.lucalabs.fairylights.util;

import net.minecraft.nbt.CompoundTag;

public interface NbtSerializable {
    CompoundTag serialize();

    void deserialize(CompoundTag compound);
}
