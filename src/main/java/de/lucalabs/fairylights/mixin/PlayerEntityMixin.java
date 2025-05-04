package de.lucalabs.fairylights.mixin;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    // TODO this is probably not needed because the fastener component gets auto synced. Verify that this is true.
//    @Inject(at = @At("TAIL"), method = "tick")
//    private void tick(CallbackInfo ci) {
//        PlayerEntity thisPlayer = (PlayerEntity) (Object) this;
//        FairyLightComponents.FASTENER.get(thisPlayer).get().ifPresent(fastener -> {
//            if (fastener.update() && !thisPlayer.getWorld().isClient()) {
//                FilteredServerPlayNetworking.sendToPlayersWatchingEntity(
//                        thisPlayer,
//                        UpdateEntityFastenerMessage.ID,
//                        new UpdateEntityFastenerMessage(thisPlayer, fastener));
//            }
//        });
//    }
}
