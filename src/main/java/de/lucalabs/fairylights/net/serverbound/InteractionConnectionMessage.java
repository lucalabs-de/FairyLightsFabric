package de.lucalabs.fairylights.net.serverbound;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.collision.Intersection;
import de.lucalabs.fairylights.connection.Connection;
import de.lucalabs.fairylights.connection.PlayerAction;
import de.lucalabs.fairylights.fastener.Fastener;
import de.lucalabs.fairylights.fastener.FastenerType;
import de.lucalabs.fairylights.fastener.accessor.FastenerAccessor;
import de.lucalabs.fairylights.feature.FeatureType;
import mezz.jei.common.codecs.EnumCodec;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class InteractionConnectionMessage {

    public static final CustomPayload.Id<PacketData> ID
            = new CustomPayload.Id<>(Identifier.of(FairyLights.ID, "interaction_connection"));

    private static final float RANGE = (Connection.MAX_LENGTH + 1) * (Connection.MAX_LENGTH + 1);
    private static final float REACH = 6 * 6;

    public static PacketData getPayload(final Connection connection,
                                        final PlayerAction action,
                                        final Intersection intersection) {

        final Fastener<?> fastener = connection.getFastener();

        return new PacketData(
                fastener.getPos(),
                FastenerType.serialize(fastener.createAccessor()),
                connection.getUUID(),
                intersection.result(),
                action,
                intersection.featureType(),
                intersection.feature().getId()
        );
    }


    public static void apply(InteractionConnectionMessage.PacketData m, ServerPlayNetworking.Context c) {
        final FastenerAccessor fastenerAcc = FastenerType.deserialize(m.accessorNbt);

        c.server().execute(() -> {
            getConnection(fastenerAcc, m.uuid, d -> true, c.player().getWorld()).ifPresent(connection -> {
                if (connection.isModifiable(c.player()) &&
                        c.player().squaredDistanceTo(Vec3d.of(connection.getFastener().getPos())) < RANGE &&
                        c.player().squaredDistanceTo(m.hit) < REACH
                ) {
                    if (m.action == PlayerAction.ATTACK) {
                        connection.disconnect(c.player(), m.hit);
                    } else {
                        interact(c.player(), connection, m.featureType, m.featureId, m.hit);
                    }
                }
            });
        });
    }

    @SuppressWarnings("unchecked")
    public static <C extends Connection> Optional<C> getConnection(
            final FastenerAccessor accessor,
            final UUID id,
            final Predicate<? super Connection> typePredicate,
            final World world) {
        return accessor.get(world, false).flatMap(f -> (Optional<C>) f.get(id).filter(typePredicate));
    }

    private static void interact(
            final PlayerEntity player,
            final Connection connection,
            final FeatureType featureType,
            final int featureId,
            final Vec3d hit) {
        for (final Hand hand : Hand.values()) {
            final ItemStack stack = player.getStackInHand(hand);
            final ItemStack oldStack = stack.copy();
            if (connection.interact(player, hit, featureType, featureId, stack, hand)) {
                updateItem(player, oldStack, stack, hand);
                break;
            }
        }
    }

    private static void updateItem(
            final PlayerEntity player,
            final ItemStack oldStack,
            final ItemStack stack,
            final Hand hand) {
        if (stack.getCount() <= 0 && !player.getAbilities().creativeMode) {
//            ForgeEventFactory.onPlayerDestroyItem(player, stack, hand); // TODO check if relevant
            player.setStackInHand(hand, ItemStack.EMPTY);
        } else if (stack.getCount() < oldStack.getCount() && player.getAbilities().creativeMode) {
            stack.setCount(oldStack.getCount());
        }
    }

    public record PacketData(
            BlockPos pos,
            NbtCompound accessorNbt,
            UUID uuid,
            Vec3d hit,
            PlayerAction action,
            FeatureType featureType,
            int featureId)
            implements CustomPayload {

        public static final PacketCodec<RegistryByteBuf, PacketData> PACKET_CODEC = PacketCodecs.registryCodec(
                RecordCodecBuilder.create(instance ->
                        instance.group(
                                BlockPos.CODEC.fieldOf("pos").forGetter(PacketData::pos),
                                NbtCompound.CODEC.fieldOf("accessorNbt").forGetter(PacketData::accessorNbt),
                                Uuids.CODEC.fieldOf("uuid").forGetter(PacketData::uuid),
                                Vec3d.CODEC.fieldOf("hit").forGetter(PacketData::hit),
                                EnumCodec.create(PlayerAction.class, PlayerAction::valueOf).fieldOf("action").forGetter(PacketData::action),
                                FeatureType.CODEC.fieldOf("featureType").forGetter(PacketData::featureType),
                                Codec.INT.fieldOf("featureId").forGetter(PacketData::featureId)
                        ).apply(instance, PacketData::new))
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
}