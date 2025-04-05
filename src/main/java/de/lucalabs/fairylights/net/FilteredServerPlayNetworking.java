package de.lucalabs.fairylights.net;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;

public final class FilteredServerPlayNetworking {
    private FilteredServerPlayNetworking() {
    }

    public static void sendToPlayersTrackingChunk(ServerWorld world, ChunkPos chunkPos, Identifier packetId, PacketByteBuf packet) {
        for (ServerPlayerEntity player : PlayerLookup.tracking(world, chunkPos)) {
            ServerPlayNetworking.send(player, packetId, packet);
        }
    }

    public static void sendToPlayersWatchingEntity(Entity entity, Identifier packetId, PacketByteBuf packet) {
        for (ServerPlayerEntity player : PlayerLookup.tracking(entity)) {
            ServerPlayNetworking.send(player, packetId, packet);
        }
    }
}
