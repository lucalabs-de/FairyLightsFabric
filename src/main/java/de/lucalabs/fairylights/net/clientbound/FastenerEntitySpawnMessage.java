package de.lucalabs.fairylights.net.clientbound;

import de.lucalabs.fairylights.FairyLights;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class FastenerEntitySpawnMessage extends PacketByteBuf {

    public static Identifier ID = Identifier.of(FairyLights.ID, "spawn_fastener");

    public FastenerEntitySpawnMessage() {
        super(Unpooled.buffer());
    }
}
