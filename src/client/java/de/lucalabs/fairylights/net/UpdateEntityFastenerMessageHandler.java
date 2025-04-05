package de.lucalabs.fairylights.net;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class UpdateEntityFastenerMessageHandler {

    public static void apply(
            MinecraftClient server,
            ClientPlayNetworkHandler handler,
            PacketByteBuf buf,
            PacketSender responseSender) {
        // TODO probably not needed because of auto sync, see other TODOs
    }
}
