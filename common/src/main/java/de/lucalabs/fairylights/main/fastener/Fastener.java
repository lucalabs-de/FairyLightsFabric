package de.lucalabs.fairylights.main.fastener;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import de.lucalabs.fairylights.main.connection.Connection;
import de.lucalabs.fairylights.main.connection.ConnectionType;
import de.lucalabs.fairylights.main.fastener.accessor.FastenerAccessor;
import de.lucalabs.fairylights.main.items.components.ComponentRecords;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public interface Fastener<F extends FastenerAccessor> {
    void writeToNbt(CompoundTag tag);

    void readFromNbt(CompoundTag tag);

    Optional<Connection> get(final UUID id);

    List<Connection> getOwnConnections();

    List<Connection> getAllConnections();

    default Optional<Connection> getFirstConnection() {
        return this.getAllConnections().stream().findFirst();
    }

    AABB getBounds();

    Vec3 getConnectionPoint();

    BlockPos getPos();

    Direction getFacing();

    void setWorld(Level world);

    Level getWorld();

    F createAccessor();

    boolean isMoving();

    default void resistSnap(final Vec3 from) {}

    boolean update();

    void setDirty();

    void dropItems(Level world, BlockPos pos);

    void remove();

    boolean hasNoConnections();

    boolean hasConnectionWith(Fastener<?> fastener);

    Connection getConnectionTo(FastenerAccessor destination);

    boolean removeConnection(UUID uuid);

    boolean removeConnection(Connection connection);

    boolean reconnect(final Level world, Connection connection, Fastener<?> newDestination);

    Connection connect(Level world, Fastener<?> destination, ConnectionType<?> type, ComponentRecords.ConnectionLogic compound, final boolean drop);

    Connection createOutgoingConnection(
            Level world,
            UUID uuid,
            Fastener<?> destination,
            ConnectionType<?> type,
            ComponentRecords.ConnectionLogic logic,
            final boolean drop);

    void createIncomingConnection(Level world, UUID uuid, Fastener<?> destination, ConnectionType<?> type);
}
