package de.lucalabs.fairylights.net.serverbound;

import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.connection.Connection;
import de.lucalabs.fairylights.connection.PlayerAction;
import de.lucalabs.fairylights.fastener.accessor.FastenerAccessor;
import de.lucalabs.fairylights.feature.FeatureType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class InteractionConnectionMessage {

    public static final Identifier ID = Identifier.of(FairyLights.ID, "interaction_connection");

    private static final float RANGE = (Connection.MAX_LENGTH + 1) * (Connection.MAX_LENGTH + 1);
    private static final float REACH = 6 * 6;

    public static void apply(InteractionConnectionMessagePayload payload, ServerPlayNetworking.Context context) {

        ServerPlayerEntity player = context.player();

        getConnection(payload.accessor(), payload.uuid(), c -> true, player.getWorld()).ifPresent(connection -> {
            if (connection.isModifiable(player) &&
                    player.squaredDistanceTo(Vec3d.of(connection.getFastener().getPos())) < RANGE &&
                    player.squaredDistanceTo(payload.hit().x, payload.hit().y, payload.hit().z) < REACH
            ) {
                if (payload.action() == PlayerAction.ATTACK) {
                    connection.disconnect(player, payload.hit());
                } else {
                    interact(player, connection, payload.type(), payload.featureId(), payload.hit());
                }
            }
        });
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
            player.setStackInHand(hand, ItemStack.EMPTY);
        } else if (stack.getCount() < oldStack.getCount() && player.getAbilities().creativeMode) {
            stack.setCount(oldStack.getCount());
        }
    }

    @SuppressWarnings("unchecked")
    public static <C extends Connection> Optional<C> getConnection(
            final FastenerAccessor accessor,
            final UUID id,
            final Predicate<? super Connection> typePredicate,
            final World world) {
        return accessor.get(world, false).flatMap(f -> (Optional<C>) f.get(id).filter(typePredicate));
    }
}
