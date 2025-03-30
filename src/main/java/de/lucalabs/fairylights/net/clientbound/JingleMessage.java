package de.lucalabs.fairylights.net.clientbound;

import de.lucalabs.fairylights.FairyLights;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class JingleMessage extends PacketByteBuf {

    public static Identifier ID = Identifier.of(FairyLights.ID, "jingle_message");

    public JingleMessage() {
        super(Unpooled.buffer());
        // TODO
    }
}
