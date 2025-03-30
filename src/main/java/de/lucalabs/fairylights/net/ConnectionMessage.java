package de.lucalabs.fairylights.net;

import de.lucalabs.fairylights.connection.Connection;
import de.lucalabs.fairylights.fastener.Fastener;
import de.lucalabs.fairylights.fastener.FastenerType;
import de.lucalabs.fairylights.fastener.accessor.FastenerAccessor;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public abstract class ConnectionMessage extends PacketByteBuf {

    public ConnectionMessage(final Connection connection) {
        super(Unpooled.buffer());
        final Fastener<?> fastener = connection.getFastener();
        writeBlockPos(fastener.getPos());
        writeNbt(FastenerType.serialize(fastener.createAccessor()));
        writeUuid(connection.getUUID());
    }

    @SuppressWarnings("unchecked")
    public static <C extends Connection> Optional<C> getConnection(
            final FastenerAccessor accessor,
            final UUID id,
            final Predicate<? super Connection> typePredicate,
            final World world) {
        return accessor.get(world, false).flatMap(f -> (Optional<C>) f.get(id).filter(typePredicate));
    }

    protected static ParsedData parse(PacketByteBuf buf) {
        final BlockPos pos = buf.readBlockPos();
        final FastenerAccessor fastenerAcc = FastenerType.deserialize(Objects.requireNonNull(buf.readNbt()));
        final UUID id = buf.readUuid();

        return new ParsedData(pos, fastenerAcc, id);
    }

    protected record ParsedData(BlockPos pos, FastenerAccessor accessor, UUID id) {
    }
}
