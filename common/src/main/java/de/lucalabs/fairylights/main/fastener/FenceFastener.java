package de.lucalabs.fairylights.main.fastener;

import de.lucalabs.fairylights.main.entity.FenceFastenerEntity;
import de.lucalabs.fairylights.main.fastener.accessor.EntityFastenerAccessor;
import de.lucalabs.fairylights.main.fastener.accessor.FenceFastenerAccessor;
import net.minecraft.core.BlockPos;

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
        return this.entity.getPos();
    }

    @Override
    public boolean isMoving() {
        return false;
    }
}
