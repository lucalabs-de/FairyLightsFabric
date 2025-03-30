package de.lucalabs.fairylights.fastener;

import de.lucalabs.fairylights.fastener.accessor.EntityFastenerAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public abstract class EntityFastener<E extends Entity> extends AbstractFastener<EntityFastenerAccessor<E>> {
    protected final E entity;

    public EntityFastener(final E entity) {
        this.entity = entity;
        this.setWorld(entity.getWorld());
    }

    @Override
    public Direction getFacing() {
        return Direction.UP;
    }

    public E getEntity() {
        return this.entity;
    }

    @Override
    public BlockPos getPos() {
        return this.entity.getBlockPos();
    }

    @Override
    public Vec3d getConnectionPoint() {
        return this.entity.getPos();
    }
}
