package de.lucalabs.fairylights.fastener;

import de.lucalabs.fairylights.entity.FenceFastenerEntity;
import de.lucalabs.fairylights.fastener.accessor.EntityFastenerAccessor;
import de.lucalabs.fairylights.fastener.accessor.FenceFastenerAccessor;
import net.minecraft.util.math.BlockPos;

public final class FenceFastener extends EntityFastener<FenceFastenerEntity> {
    public FenceFastener(final FenceFastenerEntity entity) {
        super(entity);
    }

    @Override
    public EntityFastenerAccessor<FenceFastenerEntity> createAccessor() {
        return new FenceFastenerAccessor(this);
    }

    @Override
    public BlockPos getPos() {
        return this.entity.getDecorationBlockPos();
    }

    @Override
    public boolean isMoving() {
        return false;
    }
}
