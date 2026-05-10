package de.lucalabs.fairylights.main.fastener.accessor;

import de.lucalabs.fairylights.main.fastener.Fastener;
import de.lucalabs.fairylights.main.fastener.FastenerType;
import java.util.Optional;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

public interface FastenerAccessor {
    default Optional<Fastener<?>> get(final Level world) {
        return this.get(world, true);
    }

    Optional<Fastener<?>> get(final Level world, final boolean load);

    boolean isGone(final Level world);

    FastenerType getType();

    CompoundTag serialize();

    void deserialize(CompoundTag compound);
}
