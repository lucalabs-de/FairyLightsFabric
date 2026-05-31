package de.lucalabs.fairylights.main.net;

import de.lucalabs.fairylights.Common;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

/**
 * Clientbound: pushes a serialized {@link de.lucalabs.fairylights.main.fastener.Fastener} for an
 * entity (fence fastener or player) to tracking clients. Block entities sync through the vanilla
 * block-entity update tag instead and do not use this payload.
 */
public record FastenerSyncPayload(int entityId, CompoundTag fastener) implements CustomPacketPayload {

    public static final Type<FastenerSyncPayload> ID = new Type<>(Common.id("pl_fastener_sync"));

    public static final StreamCodec<ByteBuf, FastenerSyncPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, FastenerSyncPayload::entityId,
            ByteBufCodecs.COMPOUND_TAG, FastenerSyncPayload::fastener,
            FastenerSyncPayload::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
