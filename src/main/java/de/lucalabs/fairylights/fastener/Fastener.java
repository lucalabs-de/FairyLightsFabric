package de.lucalabs.fairylights.fastener;

import de.lucalabs.fairylights.connection.Connection;
import de.lucalabs.fairylights.connection.ConnectionType;
import de.lucalabs.fairylights.fastener.accessor.FastenerAccessor;
import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Fastener<F extends FastenerAccessor> {
    void writeToNbt(NbtCompound tag);

    void readFromNbt(NbtCompound tag);

    Optional<Connection> get(final UUID id);

    List<Connection> getOwnConnections();

    List<Connection> getAllConnections();

    default Optional<Connection> getFirstConnection() {
        return this.getAllConnections().stream().findFirst();
    }

    Box getBounds();

    Vec3d getConnectionPoint();

    BlockPos getPos();

    Direction getFacing();

    void setWorld(World world);

    World getWorld();

    F createAccessor();

    boolean isMoving();

    default void resistSnap(final Vec3d from) {}

    boolean update();

    void setDirty();

    void dropItems(World world, BlockPos pos);

    void remove();

    boolean hasNoConnections();

    boolean hasConnectionWith(Fastener<?> fastener);

    Connection getConnectionTo(FastenerAccessor destination);

    boolean removeConnection(UUID uuid);

    boolean removeConnection(Connection connection);

    boolean reconnect(final World world, Connection connection, Fastener<?> newDestination);

    Connection connect(World world, Fastener<?> destination, ConnectionType<?> type, NbtCompound compound, final boolean drop);

    Connection createOutgoingConnection(
            World world,
            UUID uuid,
            Fastener<?> destination,
            ConnectionType<?> type,
            NbtCompound compound,
            final boolean drop);

    void createIncomingConnection(World world, UUID uuid, Fastener<?> destination, ConnectionType<?> type);
}
