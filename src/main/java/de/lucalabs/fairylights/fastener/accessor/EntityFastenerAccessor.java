package de.lucalabs.fairylights.fastener.accessor;

import de.lucalabs.fairylights.components.FairyLightComponents;
import de.lucalabs.fairylights.components.GenericComponent;
import de.lucalabs.fairylights.fastener.EntityFastener;
import de.lucalabs.fairylights.fastener.Fastener;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class EntityFastenerAccessor<E extends Entity> implements FastenerAccessor {

    private final Class<? extends E> entityClass;

    private UUID uuid;

    @Nullable
    private E entity;

    @Nullable
    private Vec3d pos;

    public EntityFastenerAccessor(final Class<? extends E> entityClass) {
        this(entityClass, (UUID) null);
    }

    public EntityFastenerAccessor(final Class<? extends E> entityClass, final EntityFastener<E> fastener) {
        this(entityClass, fastener.getEntity().getUuid());
        this.entity = fastener.getEntity();
        this.pos = this.entity.getPos();
    }

    public EntityFastenerAccessor(final Class<? extends E> entityClass, final UUID uuid) {
        this.entityClass = entityClass;
        this.uuid = uuid;
    }

    @Override
    public Optional<Fastener<?>> get(final World world, final boolean load) {
        if (this.entity == null) {
            if (world instanceof ServerWorld) {
                final Entity e = ((ServerWorld) world).getEntity(this.uuid);
                if (this.entityClass.isInstance(e)) {
                    this.entity = this.entityClass.cast(e);
                }
            } else if (this.pos != null) {
                List<? extends E> relevantEntities = world.getNonSpectatingEntities(
                        this.entityClass,
                        new Box(this.pos.subtract(1.0D, 1.0D, 1.0D),
                                this.pos.add(1.0D, 1.0D, 1.0D)));

                for (final E entity : relevantEntities) {
                    if (this.uuid.equals(entity.getUuid())) {
                        this.entity = entity;
                        break;
                    }
                }
            }
        }

        if (this.entity != null && this.entity.getWorld() == world) {
            this.pos = this.entity.getPos();
            return FairyLightComponents.FASTENER.maybeGet(entity).flatMap(GenericComponent::get);
        }

        return Optional.empty();
    }

    @Override
    public boolean isGone(final World world) {
        return !world.isClient()
                && this.entity != null
                && (FairyLightComponents.FASTENER.get(this.entity).get().isEmpty() || this.entity.getWorld() != world);
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
    public NbtCompound serialize() {
        final NbtCompound tag = new NbtCompound();
        tag.putUuid("UUID", this.uuid);
        if (this.pos != null) {
            final NbtList pos = new NbtList();
            pos.add(NbtDouble.of(this.pos.x));
            pos.add(NbtDouble.of(this.pos.y));
            pos.add(NbtDouble.of(this.pos.z));
            tag.put("Pos", pos);
        }
        return tag;
    }

    @Override
    public void deserialize(final NbtCompound tag) {
        this.uuid = tag.getUuid("UUID");
        if (tag.contains("Pos", NbtElement.LIST_TYPE)) {
            final NbtList pos = tag.getList("Pos", NbtElement.DOUBLE_TYPE);
            this.pos = new Vec3d(pos.getDouble(0), pos.getDouble(1), pos.getDouble(2));
        } else {
            this.pos = null;
        }
        this.entity = null;
    }
}
