package de.lucalabs.fairylights.main.events;

import de.lucalabs.fairylights.Constants;
import de.lucalabs.fairylights.main.entity.FenceFastenerEntity;
import de.lucalabs.fairylights.main.items.ConnectionItem;
import de.lucalabs.fairylights.platform.Services;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;

public final class ServerEventHandler {
    private ServerEventHandler() {
    }

    // TODO no fucking idea what this function does
    public static InteractionResult onRightClickBlock(Player player, Level world, InteractionHand hand, HitResult hitResult) {
        boolean shouldFail = false;

        final BlockPos pos = new BlockPos((int) hitResult.getLocation().x, (int) hitResult.getLocation().y, (int) hitResult.getLocation().z);
        if (!(world.getBlockState(pos).getBlock() instanceof FenceBlock)) {
            return InteractionResult.PASS;
        }

        final ItemStack stack = player.getItemInHand(hand);
        boolean checkHanging = stack.getItem() == Items.LEAD;
        if (hand == InteractionHand.MAIN_HAND) {
            final ItemStack offhandStack = player.getOffhandItem();
            if (offhandStack.getItem() instanceof ConnectionItem) {
                if (checkHanging) {
                    return InteractionResult.SUCCESS;
                } else {
//                    event.setUseBlock(Event.Result.DENY);
                    shouldFail = true;
                }
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
                return shouldFail ? InteractionResult.FAIL : InteractionResult.SUCCESS;
//                event.setCanceled(true);
            }
        }

        return shouldFail ? InteractionResult.FAIL : InteractionResult.PASS;
    }

    public static void initialize() {
        Constants.LOG.info("initializing event listener");
        UseBlockCallback.EVENT.register(ServerEventHandler::onRightClickBlock);

        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(((player, origin, dest) -> {
            Constants.LOG.info("changed dimension to {}", dest.dimension());
            Services.COMPONENTS.maybeGet(player, Services.KEYS.FASTENER()).ifPresent(f -> f.setWorld(dest));
        }));
    }
}
