package de.lucalabs.fairylights.fastener.accessor;

import de.lucalabs.fairylights.fastener.Fastener;
import de.lucalabs.fairylights.fastener.FastenerType;
import de.lucalabs.fairylights.util.compat.LazyOptional;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public interface FastenerAccessor {
    default LazyOptional<Fastener<?>> get(final World world) {
        return this.get(world, true);
    }

    LazyOptional<Fastener<?>> get(final World world, final boolean load);

    boolean isGone(final World world);

    FastenerType getType();

    NbtCompound serialize();

    void deserialize(NbtCompound compound);
}
