package de.lucalabs.fairylights.client.net;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface ClientNetworking {
    void send(CustomPacketPayload payload);
}
