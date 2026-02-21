package de.lucalabs.fairylights.events;

import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.components.FairyLightComponents;
import de.lucalabs.fairylights.entity.FenceFastenerEntity;
import de.lucalabs.fairylights.items.ConnectionItem;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.LeashKnotEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public final class ServerEventHandler {
    private ServerEventHandler() {
    }

    public static ActionResult onRightClickBlock(PlayerEntity player, World world, Hand hand, HitResult hitResult) {
        boolean shouldFail = false;

        final BlockPos pos = new BlockPos((int) hitResult.getPos().x, (int) hitResult.getPos().y, (int) hitResult.getPos().z);
        if (!(world.getBlockState(pos).getBlock() instanceof FenceBlock)) {
            return ActionResult.PASS;
        }

        final ItemStack stack = player.getStackInHand(hand);
        boolean checkHanging = stack.getItem() == Items.LEAD;
        if (hand == Hand.MAIN_HAND) {
            final ItemStack offhandStack = player.getOffHandStack();
            if (offhandStack.getItem() instanceof ConnectionItem) {
                if (checkHanging) {
                    return ActionResult.SUCCESS;
                } else {
//                    event.setUseBlock(Event.Result.DENY);
                    shouldFail = true;
                }
            }
        }

        if (!checkHanging && !world.isClient()) {
            final double range = 7;
            final int x = pos.getX();
            final int y = pos.getY();
            final int z = pos.getZ();
            final Box area = new Box(x - range, y - range, z - range, x + range, y + range, z + range);
            for (final MobEntity entity : world.getNonSpectatingEntities(MobEntity.class, area)) {
                if (entity.isLeashed() && entity.getHoldingEntity() == player) {
                    checkHanging = true;
                    break;
                }
            }
        }

        if (checkHanging) {
            final AbstractDecorationEntity entity = FenceFastenerEntity.findHanging(world, pos);
            if (entity != null && !(entity instanceof LeashKnotEntity)) {
                return shouldFail ? ActionResult.FAIL : ActionResult.SUCCESS;
//                event.setCanceled(true);
            }
        }

        return shouldFail ? ActionResult.FAIL : ActionResult.PASS;
    }

    public static void initialize() {
        FairyLights.LOGGER.info("initializing event listener");
        UseBlockCallback.EVENT.register(ServerEventHandler::onRightClickBlock);

        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(((player, origin, dest) -> {
            FairyLights.LOGGER.info("changed dimension to {}", dest.getRegistryKey());
            FairyLightComponents.FASTENER.get(player).get().ifPresent(f -> f.setWorld(dest));
        }));
    }
}
