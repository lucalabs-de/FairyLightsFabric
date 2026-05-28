package de.lucalabs.fairylights.platform;

import de.lucalabs.fairylights.client.net.ClientNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class FabricClientNetworking implements ClientNetworking {
    @Override
    public void send(CustomPacketPayload payload) {
        ClientPlayNetworking.send(payload);
    }
}
