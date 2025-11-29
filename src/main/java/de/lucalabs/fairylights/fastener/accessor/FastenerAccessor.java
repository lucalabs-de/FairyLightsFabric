package de.lucalabs.fairylights.fastener.accessor;

import com.mojang.serialization.Codec;
import de.lucalabs.fairylights.fastener.Fastener;
import de.lucalabs.fairylights.fastener.FastenerType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import java.util.Optional;

public interface FastenerAccessor {
    default Optional<Fastener<?>> get(final World world) {
        return this.get(world, true);
    }

    Optional<Fastener<?>> get(final World world, final boolean load);

    boolean isGone(final World world);

    FastenerType getType();

    NbtCompound serialize();

    void deserialize(NbtCompound compound);
}
