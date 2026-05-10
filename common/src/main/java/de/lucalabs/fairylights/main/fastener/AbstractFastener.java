package de.lucalabs.fairylights.main.fastener;

import com.google.common.collect.ImmutableList;
import de.lucalabs.fairylights.main.connection.Connection;
import de.lucalabs.fairylights.main.connection.ConnectionType;
import de.lucalabs.fairylights.main.fastener.accessor.FastenerAccessor;
import de.lucalabs.fairylights.main.items.components.ComponentRecords;
import de.lucalabs.fairylights.main.registries.FairyLightRegistries;
import de.lucalabs.fairylights.main.util.BoxBuilder;
import de.lucalabs.fairylights.main.util.Constants;
import de.lucalabs.fairylights.main.util.Curve;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class AbstractFastener<F extends FastenerAccessor> implements Fastener<F> {

    private final Map<UUID, Connection> outgoing = new HashMap<>();
    private final Map<UUID, Incoming> incoming = new HashMap<>();

    protected AABB bounds = Constants.INFINITE_BOX;

    @Nullable
    private Level world;

    private boolean dirty;

    @Override
    public Optional<Connection> get(final UUID id) {
        return Optional.ofNullable(this.outgoing.get(id));
    }

    @Override
    public List<Connection> getOwnConnections() {
        return ImmutableList.copyOf(this.outgoing.values());
    }

    @Override
    public List<Connection> getAllConnections() {
        final ImmutableList.Builder<Connection> list = new ImmutableList.Builder<>();
        list.addAll(this.outgoing.values());
        if (this.world != null) {
            this.incoming.values().forEach(i -> i.get(this.world).ifPresent(list::add));
        }
        return list.build();
    }

    @Override
    public AABB getBounds() {
        return this.bounds;
    }

    @Override
    public abstract BlockPos getPos();

    @Nullable
    @Override
    public Level getWorld() {
        return this.world;
    }

    @Override
    public void setWorld(final Level world) {
        this.world = world;
        this.outgoing.values().forEach(c -> c.setWorld(world));
    }

    @Override
    public boolean update() {
        final Iterator<Connection> it = this.outgoing.values().iterator();
        final Vec3 fromOffset = this.getConnectionPoint();
        boolean dirty = this.dirty;

        this.dirty = false;
        while (it.hasNext()) {
            final Connection connection = it.next();
            if (connection.update(fromOffset)) {
                dirty = true;
            }
            if (connection.isRemoved()) {
                dirty = true;
                it.remove();
                this.incoming.remove(connection.getUUID());
                if (this.world != null) {
                    this.drop(this.world, this.getPos(), connection);
                }
            }
        }

        if (this.world != null) {
            this.incoming.values().removeIf(incoming -> incoming.gone(this.world));
        }

        if (dirty) {
            this.calculateBoundingBox();
        }

        return dirty;
    }

    @Override
    public void setDirty() {
        this.dirty = true;
    }

    protected void calculateBoundingBox() {
        if (this.outgoing.isEmpty()) {
            this.bounds = new AABB(this.getPos());
            return;
        }
        final BoxBuilder builder = new BoxBuilder();
        for (final Connection connection : this.outgoing.values()) {
            final Curve catenary = connection.getCatenary();
            if (catenary == null) {
                continue;
            }
            final Curve.SegmentIterator it = catenary.iterator();
            while (it.next()) {
                builder.include(it.getX(0.0F), it.getY(0.0F), it.getZ(0.0F));
                if (!it.hasNext()) {
                    builder.include(it.getX(1.0F), it.getY(1.0F), it.getZ(1.0F));
                }
            }
        }
        this.bounds = builder.add(this.getConnectionPoint()).build();
    }

    @Override
    public void dropItems(final Level world, final BlockPos pos) {
        for (final Connection connection : this.getAllConnections()) {
            this.drop(world, pos, connection);
        }
    }

    private void drop(final Level world, final BlockPos pos, final Connection connection) {
        if (!connection.shouldDrop()) return;
        final float offsetX = world.random.nextFloat() * 0.8F + 0.1F;
        final float offsetY = world.random.nextFloat() * 0.8F + 0.1F;
        final float offsetZ = world.random.nextFloat() * 0.8F + 0.1F;
        final ItemStack stack = connection.getItemStack();
        final ItemEntity entityItem = new ItemEntity(world, pos.getX() + offsetX, pos.getY() + offsetY, pos.getZ() + offsetZ, stack);
        final float scale = 0.05F;
        entityItem.setDeltaMovement(
                world.random.nextGaussian() * scale,
                world.random.nextGaussian() * scale + 0.2F,
                world.random.nextGaussian() * scale
        );
        world.addFreshEntity(entityItem);
        connection.noDrop();
    }

    @Override
    public void remove() {
        this.outgoing.values().forEach(Connection::remove);
    }

    @Override
    public boolean hasNoConnections() {
        return this.outgoing.isEmpty() && this.incoming.isEmpty();
    }

    @Override
    public boolean hasConnectionWith(final Fastener<?> fastener) {
        return this.getConnectionTo(fastener.createAccessor()) != null;
    }

    @Nullable
    @Override
    public Connection getConnectionTo(final FastenerAccessor destination) {
        for (final Connection connection : this.outgoing.values()) {
            if (connection.isDestination(destination)) {
                return connection;
            }
        }
        return null;
    }

    @Override
    public boolean removeConnection(final UUID uuid) {
        final Connection connection = this.outgoing.remove(uuid);
        if (connection != null) {
            connection.remove();
            this.setDirty();
            return true;
        } else if (this.incoming.remove(uuid) != null) {
            this.setDirty();
            return true;
        }
        return false;
    }

    @Override
    public boolean removeConnection(final Connection connection) {
        return this.removeConnection(connection.getUUID());
    }

    @Override
    public boolean reconnect(final Level world, final Connection connection, final Fastener<?> newDestination) {
        if (this.equals(newDestination) || newDestination.hasConnectionWith(this)) {
            return false;
        }
        final UUID uuid = connection.getUUID();
        if (connection.getDestination().get(world, false).filter(t -> {
            t.removeConnection(uuid);
            return true;
        }).isPresent()) {
            connection.setDestination(newDestination);
            connection.setDrop();
            newDestination.createIncomingConnection(this.world, uuid, this, connection.getType());
            this.setDirty();
            return true;
        }
        return false;
    }

    @Override
    public Connection connect(final Level world, final Fastener<?> destination, final ConnectionType<?> type, final ComponentRecords.ConnectionLogic logic, final boolean drop) {
        final UUID uuid = Mth.createInsecureUUID();
        final Connection connection = this.createOutgoingConnection(world, uuid, destination, type, logic, drop);
        destination.createIncomingConnection(world, uuid, this, type);
        return connection;
    }

    @Override
    public Connection createOutgoingConnection(final Level world, final UUID uuid, final Fastener<?> destination, final ConnectionType<?> type, final ComponentRecords.ConnectionLogic logic, final boolean drop) {
        final Connection c = type.create(world, this, uuid);
        c.deserialize(destination, logic, drop);
        this.outgoing.put(uuid, c);
        this.setDirty();
        return c;
    }

    @Override
    public void createIncomingConnection(final Level world, final UUID uuid, final Fastener<?> destination, final ConnectionType<?> type) {
        this.incoming.put(uuid, new Incoming(destination.createAccessor(), uuid));
        this.setDirty();
    }

    @Override
    public void writeToNbt(CompoundTag compound) {
        final ListTag outgoing = new ListTag();
        for (final Map.Entry<UUID, Connection> connectionEntry : this.outgoing.entrySet()) {
            final UUID uuid = connectionEntry.getKey();
            final Connection connection = connectionEntry.getValue();
            final CompoundTag connectionCompound = new CompoundTag();

            Tag connectionNbt = ComponentRecords.ConnectionStatus.CODEC
                    .encodeStart(NbtOps.INSTANCE, connection.serialize().build())
                    .getOrThrow();

            connectionCompound.put("connection", connectionNbt);
            Optional<ResourceLocation> connectionTypeId = FairyLightRegistries.CONNECTION_TYPES
                    .getResourceKey(connection.getType())
                    .map(ResourceKey::location);

            if (connectionTypeId.isPresent()) {
                connectionCompound.putString("type", connectionTypeId.get().toString());
            } else {
                continue;
            }

            connectionCompound.putUUID("uuid", uuid);
            outgoing.add(connectionCompound);
        }
        compound.put("outgoing", outgoing);
        final ListTag incoming = new ListTag();
        for (final Map.Entry<UUID, Incoming> e : this.incoming.entrySet()) {
            final CompoundTag tag = new CompoundTag();
            tag.putUUID("uuid", e.getKey());

            Tag fastenerNbt = ComponentRecords.FastenerAccessorData.CODEC
                    .encodeStart(NbtOps.INSTANCE, ComponentRecords.FastenerAccessorData.from(e.getValue().fastener()))
                    .getOrThrow();

            tag.put("fastener", fastenerNbt);
            incoming.add(tag);
        }
        compound.put("incoming", incoming);
    }

    @Override
    public void readFromNbt(final CompoundTag compound) {
        final ListTag listConnections = compound.getList("outgoing", Tag.TAG_COMPOUND);
        final List<UUID> nbtUuids = new ArrayList<>();
        for (int i = 0; i < listConnections.size(); i++) {
            final CompoundTag connectionCompound = listConnections.getCompound(i);
            final UUID uuid;
            if (connectionCompound.hasUUID("uuid")) {
                uuid = connectionCompound.getUUID("uuid");
            } else {
                uuid = Mth.createInsecureUUID();
            }
            nbtUuids.add(uuid);
            if (this.outgoing.containsKey(uuid)) {
                final Connection connection = this.outgoing.get(uuid);

                var status = ComponentRecords.ConnectionStatus.CODEC
                        .parse(NbtOps.INSTANCE, connectionCompound.getCompound("connection"))
                        .getOrThrow();

                connection.deserialize(status);
            } else {
                final ConnectionType<?> type = FairyLightRegistries.CONNECTION_TYPES
                        .get(ResourceLocation.tryParse(connectionCompound.getString("type")));

                if (type != null) {
                    final Connection connection = type.create(this.world, this, uuid);

                    var status = ComponentRecords.ConnectionStatus.CODEC
                            .parse(NbtOps.INSTANCE, connectionCompound.getCompound("connection"))
                            .getOrThrow();

                    connection.deserialize(status);
                    this.outgoing.put(uuid, connection);
                }
            }
        }
        final Iterator<Map.Entry<UUID, Connection>> connectionsIter = this.outgoing.entrySet().iterator();
        while (connectionsIter.hasNext()) {
            final Map.Entry<UUID, Connection> connection = connectionsIter.next();
            if (!nbtUuids.contains(connection.getKey())) {
                connectionsIter.remove();
                connection.getValue().remove();
            }
        }
        this.incoming.clear();
        final ListTag incoming = compound.getList("incoming", Tag.TAG_COMPOUND);
        for (int i = 0; i < incoming.size(); i++) {
            final CompoundTag incomingNbt = incoming.getCompound(i);
            final UUID uuid = incomingNbt.getUUID("uuid");

            final FastenerAccessor fastener = ComponentRecords.FastenerAccessorData.CODEC
                    .parse(NbtOps.INSTANCE, incomingNbt.getCompound("fastener"))
                    .getOrThrow()
                    .accessor();

            this.incoming.put(uuid, new Incoming(fastener, uuid));
        }
        this.setDirty();
    }

    record Incoming(FastenerAccessor fastener, UUID id) {

        boolean gone(final Level world) {
            return this.fastener.isGone(world);
        }

        Optional<Connection> get(final Level world) {
            return this.fastener.get(world, false).flatMap(f -> f.get(this.id));
        }
    }
}
