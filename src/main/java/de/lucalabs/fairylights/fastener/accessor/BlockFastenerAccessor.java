package de.lucalabs.fairylights.fastener.accessor;

import de.lucalabs.fairylights.fastener.Fastener;
import de.lucalabs.fairylights.util.compat.LazyOptional;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class BlockFastenerAccessor implements FastenerAccessor {
    private BlockPos pos = BlockPos.ORIGIN;

    public BlockFastenerAccessor() {}

    public BlockFastenerAccessor(final BlockFastener fastener) {
        this(fastener.getPos());
    }

    public BlockFastenerAccessor(final BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public LazyOptional<Fastener<?>> get(final World world, final boolean load) {
        if (load || world.canSetBlock(this.pos)) {
            final BlockEntity entity = world.getBlockEntity(this.pos);
            if (entity != null) {
                return entity.getCapability(CapabilityHandler.FASTENER_CAP);
            }
        }
        return LazyOptional.empty();
    }

    @Override
    public boolean isGone(final Level world) {
        if (world.isClientSide() || !world.isLoaded(this.pos)) return false;
        final BlockEntity entity = world.getBlockEntity(this.pos);
        return entity == null || !entity.getCapability(CapabilityHandler.FASTENER_CAP).isPresent();
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
        return NbtUtils.writeBlockPos(this.pos);
    }

    @Override
    public void deserialize(final CompoundTag nbt) {
        this.pos = NbtUtils.readBlockPos(nbt);
    }
}
