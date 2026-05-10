package de.lucalabs.fairylights.main.net.serverbound;

import de.lucalabs.fairylights.Common;
import de.lucalabs.fairylights.main.connection.Connection;
import de.lucalabs.fairylights.main.connection.PlayerAction;
import de.lucalabs.fairylights.main.fastener.accessor.FastenerAccessor;
import de.lucalabs.fairylights.main.feature.FeatureType;
import de.lucalabs.fairylights.main.net.InteractionConnectionMessagePayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class InteractionConnectionMessage {

    public static final ResourceLocation ID = Common.id("interaction_connection");

    private static final float RANGE = (Connection.MAX_LENGTH + 1) * (Connection.MAX_LENGTH + 1);
    private static final float REACH = 6 * 6;

    public static void apply(InteractionConnectionMessagePayload payload, ServerPlayNetworking.Context context) {

        ServerPlayer player = context.player();

        getConnection(payload.accessor(), payload.uuid(), c -> true, player.level()).ifPresent(connection -> {
            if (connection.isModifiable(player) &&
                    player.distanceToSqr(Vec3.atLowerCornerOf(connection.getFastener().getPos())) < RANGE &&
                    player.distanceToSqr(payload.hit().x, payload.hit().y, payload.hit().z) < REACH
            ) {
                if (payload.action() == PlayerAction.ATTACK) {
                    connection.disconnect(player, payload.hit());
                } else {
                    interact(player, connection, payload.featureType(), payload.featureId(), payload.hit());
                }
            }
        });
    }

    private static void interact(
            final Player player,
            final Connection connection,
            final FeatureType featureType,
            final int featureId,
            final Vec3 hit) {
        for (final InteractionHand hand : InteractionHand.values()) {
            final ItemStack stack = player.getItemInHand(hand);
            final ItemStack oldStack = stack.copy();
            if (connection.interact(player, hit, featureType, featureId, stack, hand)) {
                updateItem(player, oldStack, stack, hand);
                break;
            }
        }
    }

    private static void updateItem(
            final Player player,
            final ItemStack oldStack,
            final ItemStack stack,
            final InteractionHand hand) {
        if (stack.getCount() <= 0 && !player.getAbilities().instabuild) {
            player.setItemInHand(hand, ItemStack.EMPTY);
        } else if (stack.getCount() < oldStack.getCount() && player.getAbilities().instabuild) {
            stack.setCount(oldStack.getCount());
        }
    }

    @SuppressWarnings("unchecked")
    public static <C extends Connection> Optional<C> getConnection(
            final FastenerAccessor accessor,
            final UUID id,
            final Predicate<? super Connection> typePredicate,
            final Level world) {
        return accessor.get(world, false).flatMap(f -> (Optional<C>) f.get(id).filter(typePredicate));
    }
}
