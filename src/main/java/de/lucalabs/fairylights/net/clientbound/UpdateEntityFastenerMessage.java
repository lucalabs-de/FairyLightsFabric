package de.lucalabs.fairylights.net.clientbound;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.entity.FenceFastenerEntity;
import de.lucalabs.fairylights.fastener.Fastener;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class UpdateEntityFastenerMessage extends PacketByteBuf {

    public static Identifier ID = Identifier.of(FairyLights.ID, "update_fastener");

    public UpdateEntityFastenerMessage(FenceFastenerEntity entity, Fastener<?> fastener) {
        super(Unpooled.buffer());
        writeVarInt(entity.getId());

        NbtCompound fastenerNbt = new NbtCompound();
        fastener.writeToNbt(fastenerNbt);
        writeNbt(fastenerNbt);
    }

    public static void apply(
            MinecraftClient server,
            ServerPlayerEntity player,
            ServerPlayNetworkHandler handler,
            PacketByteBuf buf,
            PacketSender responseSender) {

    }
}
