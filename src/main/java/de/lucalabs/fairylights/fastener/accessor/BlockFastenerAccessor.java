package de.lucalabs.fairylights.fastener.accessor;

import de.lucalabs.fairylights.components.FairyLightComponents;
import de.lucalabs.fairylights.components.GenericComponent;
import de.lucalabs.fairylights.fastener.BlockFastener;
import de.lucalabs.fairylights.fastener.Fastener;
import de.lucalabs.fairylights.fastener.FastenerType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class BlockFastenerAccessor implements FastenerAccessor {
    private BlockPos pos = BlockPos.ZERO;

    public BlockFastenerAccessor() {
    }

    public BlockFastenerAccessor(final BlockFastener fastener) {
        this(fastener.getPos());
    }

    public BlockFastenerAccessor(final BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public Optional<Fastener<?>> get(final Level world, final boolean load) {
        if (load || world.isLoaded(this.pos)) {
            final BlockEntity entity = world.getBlockEntity(this.pos);
            if (entity != null) {
                return FairyLightComponents.FASTENER.maybeGet(entity).flatMap(GenericComponent::get);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean isGone(final Level world) {
        if (world.isClientSide() || !world.isLoaded(this.pos)) return false;
        final BlockEntity entity = world.getBlockEntity(this.pos);
        return entity == null || FairyLightComponents.FASTENER.get(entity).isEmpty();
    }

    @Override
    public FastenerType getType() {
        return FastenerType.BLOCK;
    }

    @Override
    public boolean equals(@Nullable final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof BlockFastenerAccessor) {
            return this.pos.equals(((BlockFastenerAccessor) obj).pos);
        }
        return false;
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag compound = new CompoundTag();
        compound.put("pos", NbtUtils.writeBlockPos(this.pos));
        return compound;
    }

    @Override
    public void deserialize(final CompoundTag nbt) {
        this.pos = NbtUtils.readBlockPos(nbt, "pos").orElseThrow();
    }
}
