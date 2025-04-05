package de.lucalabs.fairylights.net.clientbound;

import de.lucalabs.fairylights.FairyLights;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class FastenerEntitySpawnMessage extends PacketByteBuf {

    public static Identifier ID = Identifier.of(FairyLights.ID, "spawn_fastener");

    public FastenerEntitySpawnMessage() {
        super(Unpooled.buffer());
    }

    public static void apply(
            MinecraftServer server,
            ServerPlayerEntity player,
            ServerPlayNetworkHandler handler,
            PacketByteBuf buf,
            PacketSender responseSender) {

    }
}
