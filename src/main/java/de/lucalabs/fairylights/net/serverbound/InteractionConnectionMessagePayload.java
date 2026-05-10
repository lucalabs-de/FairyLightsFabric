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
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public record InteractionConnectionMessagePayload(
        UUID uuid,
        BlockPos pos,
        FastenerAccessor accessor,
        PlayerAction action,
        Vec3 hit,
        FeatureType featureType,
        int featureId
) implements CustomPacketPayload {

    public static final ResourceLocation PAYLOAD_ID = ResourceLocation.fromNamespaceAndPath(FairyLights.ID, "pl_interaction_connection");
    public static final CustomPacketPayload.Type<InteractionConnectionMessagePayload> ID
            = new CustomPacketPayload.Type<>(PAYLOAD_ID);

    public static final StreamCodec<ByteBuf, InteractionConnectionMessagePayload> CODEC = ByteBufCodecs.fromCodec(
            RecordCodecBuilder.create(i -> i.group(
                    UUIDUtil.AUTHLIB_CODEC.fieldOf("uuid").forGetter(InteractionConnectionMessagePayload::uuid),
                    BlockPos.CODEC.fieldOf("pos").forGetter(InteractionConnectionMessagePayload::pos),
                    ComponentRecords.FastenerAccessorData.CODEC.fieldOf("accessor").forGetter(x -> ComponentRecords.FastenerAccessorData.from(x.accessor())),
                    Codec.INT.fieldOf("action").forGetter(x -> x.action.ordinal()),
                    Vec3.CODEC.fieldOf("hit").forGetter(InteractionConnectionMessagePayload::hit),
                    Codec.INT.fieldOf("type").forGetter(x -> x.featureType.getId()),
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
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
