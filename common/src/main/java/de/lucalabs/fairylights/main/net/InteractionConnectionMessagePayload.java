package de.lucalabs.fairylights.main.net;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.lucalabs.fairylights.Common;
import de.lucalabs.fairylights.main.collision.Intersection;
import de.lucalabs.fairylights.main.connection.Connection;
import de.lucalabs.fairylights.main.connection.PlayerAction;
import de.lucalabs.fairylights.main.fastener.accessor.FastenerAccessor;
import de.lucalabs.fairylights.main.feature.FeatureType;
import de.lucalabs.fairylights.main.items.components.ComponentRecords;
import de.lucalabs.fairylights.main.util.Utils;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public record InteractionConnectionMessagePayload(
        UUID uuid,
        BlockPos pos,
        FastenerAccessor accessor,
        PlayerAction action,
        Vec3 hit,
        FeatureType featureType,
        int featureId
) implements CustomPacketPayload {

    public static final ResourceLocation PAYLOAD_ID = Common.id("pl_interaction_connection");
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
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
