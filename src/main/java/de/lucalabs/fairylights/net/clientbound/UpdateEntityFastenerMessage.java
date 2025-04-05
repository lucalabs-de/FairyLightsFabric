package de.lucalabs.fairylights.net.clientbound;

import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.fastener.Fastener;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class UpdateEntityFastenerMessage extends PacketByteBuf {

    public static Identifier ID = Identifier.of(FairyLights.ID, "update_fastener");

    public UpdateEntityFastenerMessage(Entity entity, Fastener<?> fastener) {
        super(Unpooled.buffer());
        writeVarInt(entity.getId());

        NbtCompound fastenerNbt = new NbtCompound();
        fastener.writeToNbt(fastenerNbt);
        writeNbt(fastenerNbt);
    }
}
