package de.lucalabs.fairylights.fastener.accessor;

import com.mojang.serialization.Codec;
import de.lucalabs.fairylights.entity.FenceFastenerEntity;
import de.lucalabs.fairylights.fastener.EntityFastener;
import de.lucalabs.fairylights.fastener.FastenerType;

public final class FenceFastenerAccessor extends EntityFastenerAccessor<FenceFastenerEntity> {
    public FenceFastenerAccessor() {
        super(FenceFastenerEntity.class);
    }

    public FenceFastenerAccessor(final EntityFastener<FenceFastenerEntity> fastener) {
        super(FenceFastenerEntity.class, fastener);
    }

    @Override
    public FastenerType getType() {
        return FastenerType.FENCE;
    }
}
