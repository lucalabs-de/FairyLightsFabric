package de.lucalabs.fairylights.main.events;

import de.lucalabs.fairylights.Constants;
import de.lucalabs.fairylights.main.entity.FenceFastenerEntity;
import de.lucalabs.fairylights.main.items.ConnectionItem;
import de.lucalabs.fairylights.main.net.FastenerSyncPayload;
import de.lucalabs.fairylights.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * Game-bus event handling, replacing the Fabric ServerEventHandler. Auto-registered via
 * {@link EventBusSubscriber}. Also performs the initial fastener sync for entities/players when a
 * client starts tracking them (the ongoing sync runs from {@code NeoForgeFastenerKey#syncFor}).
 */
@EventBusSubscriber(modid = Constants.MOD_ID)
public final class NeoForgeServerEventHandler {

    private NeoForgeServerEventHandler() {
    }

    @SubscribeEvent
    public static void onRightClickBlock(final PlayerInteractEvent.RightClickBlock event) {
        final Player player = event.getEntity();
        final Level world = event.getLevel();
        final InteractionHand hand = event.getHand();
        final BlockPos pos = event.getPos();

        if (!(world.getBlockState(pos).getBlock() instanceof FenceBlock)) {
            return;
        }

        final ItemStack stack = player.getItemInHand(hand);
        boolean checkHanging = stack.getItem() == Items.LEAD;
        if (hand == InteractionHand.MAIN_HAND) {
            final ItemStack offhandStack = player.getOffhandItem();
            if (offhandStack.getItem() instanceof ConnectionItem) {
                if (checkHanging) {
                    event.setCanceled(true);
                    event.setCancellationResult(InteractionResult.SUCCESS);
                    return;
                }
                // Let the offhand connection item act instead of the fence's vanilla use.
                event.setUseBlock(TriState.FALSE);
            }
        }

        if (!checkHanging && !world.isClientSide()) {
            final double range = 7;
            final int x = pos.getX();
            final int y = pos.getY();
            final int z = pos.getZ();
            final AABB area = new AABB(x - range, y - range, z - range, x + range, y + range, z + range);
            for (final Mob entity : world.getEntitiesOfClass(Mob.class, area)) {
                if (entity.isLeashed() && entity.getLeashHolder() == player) {
                    checkHanging = true;
                    break;
                }
            }
        }

        if (checkHanging) {
            final HangingEntity entity = FenceFastenerEntity.findHanging(world, pos);
            if (entity != null) {
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }
    }

    @SubscribeEvent
    public static void onChangeDimension(final PlayerEvent.PlayerChangedDimensionEvent event) {
        final Player player = event.getEntity();
        Services.COMPONENTS.maybeGet(player, Services.KEYS.FASTENER()).ifPresent(f -> f.setWorld(player.level()));
    }

    @SubscribeEvent
    public static void onStartTracking(final PlayerEvent.StartTracking event) {
        final Entity target = event.getTarget();
        if ((target instanceof FenceFastenerEntity || target instanceof Player) && event.getEntity() instanceof final ServerPlayer watcher) {
            Services.COMPONENTS.maybeGet(target, Services.KEYS.FASTENER()).ifPresent(fastener -> {
                final CompoundTag tag = new CompoundTag();
                fastener.writeToNbt(tag);
                PacketDistributor.sendToPlayer(watcher, new FastenerSyncPayload(target.getId(), tag));
            });
        }
    }
}
