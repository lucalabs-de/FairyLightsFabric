package de.lucalabs.fairylights.util;

import net.minecraft.nbt.NbtCompound;

public interface NbtSerializable {
    NbtCompound serialize();

    void deserialize(NbtCompound compound);
}
