package de.lucalabs.fairylights.net.serverbound;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.collision.Intersection;
import de.lucalabs.fairylights.connection.Connection;
import de.lucalabs.fairylights.connection.PlayerAction;
import de.lucalabs.fairylights.fastener.accessor.FastenerAccessor;
import de.lucalabs.fairylights.feature.FeatureType;
import de.lucalabs.fairylights.items.components.ComponentRecords;
import de.lucalabs.fairylights.util.Utils;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;

public record InteractionConnectionMessagePayload(
        UUID uuid,
        BlockPos pos,
        FastenerAccessor accessor,
        PlayerAction action,
        Vec3d hit,
        FeatureType type,
        int featureId
) implements CustomPayload {

    public static final Identifier PAYLOAD_ID = Identifier.of(FairyLights.ID, "pl_interaction_connection");
    public static final CustomPayload.Id<InteractionConnectionMessagePayload> ID
            = new CustomPayload.Id<>(PAYLOAD_ID);

    public static final PacketCodec<ByteBuf, InteractionConnectionMessagePayload> CODEC = PacketCodecs.codec(
            RecordCodecBuilder.create(i -> i.group(
                    Uuids.CODEC.fieldOf("uuid").forGetter(InteractionConnectionMessagePayload::uuid),
                    BlockPos.CODEC.fieldOf("pos").forGetter(InteractionConnectionMessagePayload::pos),
                    ComponentRecords.FastenerAccessorData.CODEC.fieldOf("accessor").forGetter(x -> ComponentRecords.FastenerAccessorData.from(x.accessor())),
                    Codec.INT.fieldOf("action").forGetter(x -> x.action.ordinal()),
                    Vec3d.CODEC.fieldOf("hit").forGetter(InteractionConnectionMessagePayload::hit),
                    Codec.INT.fieldOf("type").forGetter(x -> x.type().getId()),
                    Codec.INT.fieldOf("featureId").forGetter(InteractionConnectionMessagePayload::featureId)
            ).apply(i, (u, p, a, ac, h, t, fi) ->
                    new InteractionConnectionMessagePayload(
                            u, p, a.accessor(), Utils.getEnumValue(PlayerAction.class, ac), h, FeatureType.fromId(t), fi)))
    );

    public InteractionConnectionMessagePayload(Connection connection, PlayerAction action, Intersection intersection) {
        this(
                connection.getUUID(),
                connection.getFastener().getPos(),
                connection.getFastener().createAccessor(),
                action,
                intersection.result(),
                intersection.featureType(),
                intersection.feature().getId());
    }


    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
