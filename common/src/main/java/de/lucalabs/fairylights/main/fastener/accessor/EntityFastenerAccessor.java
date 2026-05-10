package de.lucalabs.fairylights.main.fastener.accessor;

import de.lucalabs.fairylights.main.fastener.EntityFastener;
import de.lucalabs.fairylights.main.fastener.Fastener;
import de.lucalabs.fairylights.platform.Services;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.ServiceConfigurationError;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public abstract class EntityFastenerAccessor<E extends Entity> implements FastenerAccessor {

    private final Class<? extends E> entityClass;

    private UUID uuid;

    @Nullable
    private E entity;

    @Nullable
    private Vec3 pos;

    public EntityFastenerAccessor(final Class<? extends E> entityClass) {
        this(entityClass, (UUID) null);
    }

    public EntityFastenerAccessor(final Class<? extends E> entityClass, final EntityFastener<E> fastener) {
        this(entityClass, fastener.getEntity().getUUID());
        this.entity = fastener.getEntity();
        this.pos = this.entity.position();
    }

    public EntityFastenerAccessor(final Class<? extends E> entityClass, final UUID uuid) {
        this.entityClass = entityClass;
        this.uuid = uuid;
    }

    @Override
    public Optional<Fastener<?>> get(final Level world, final boolean load) {
        if (this.entity == null) {
            if (world instanceof ServerLevel) {
                final Entity e = ((ServerLevel) world).getEntity(this.uuid);
                if (this.entityClass.isInstance(e)) {
                    this.entity = this.entityClass.cast(e);
                }
            } else if (this.pos != null) {
                List<? extends E> relevantEntities = world.getEntitiesOfClass(
                        this.entityClass,
                        new AABB(this.pos.subtract(1.0D, 1.0D, 1.0D),
                                this.pos.add(1.0D, 1.0D, 1.0D)));

                for (final E entity : relevantEntities) {
                    if (this.uuid.equals(entity.getUUID())) {
                        this.entity = entity;
                        break;
                    }
                }
            }
        }

        if (this.entity != null && this.entity.level() == world) {
            this.pos = this.entity.position();
            return Services.COMPONENTS.maybeGet(entity, Services.KEYS.FASTENER());
        }

        return Optional.empty();
    }

    @Override
    public boolean isGone(final Level world) {
        return !world.isClientSide()
                && this.entity != null
                && (Services.COMPONENTS.maybeGet(this.entity, Services.KEYS.FASTENER()).isEmpty() || this.entity.level() != world);
    }

    @Override
    public boolean equals(@Nullable final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof EntityFastenerAccessor<?>) {
            return this.uuid.equals(((EntityFastenerAccessor<?>) obj).uuid);
        }
        return false;
    }

    @Override
    public CompoundTag serialize() {
        final CompoundTag tag = new CompoundTag();
        tag.putUUID("UUID", this.uuid);
        if (this.pos != null) {
            final ListTag pos = new ListTag();
            pos.add(DoubleTag.valueOf(this.pos.x));
            pos.add(DoubleTag.valueOf(this.pos.y));
            pos.add(DoubleTag.valueOf(this.pos.z));
            tag.put("Pos", pos);
        }
        return tag;
    }

    @Override
    public void deserialize(final CompoundTag tag) {
        this.uuid = tag.getUUID("UUID");
        if (tag.contains("Pos", Tag.TAG_LIST)) {
            final ListTag pos = tag.getList("Pos", Tag.TAG_DOUBLE);
            this.pos = new Vec3(pos.getDouble(0), pos.getDouble(1), pos.getDouble(2));
        } else {
            this.pos = null;
        }
        this.entity = null;
    }
}
