package de.lucalabs.fairylights.mixin;

import de.lucalabs.fairylights.components.FairyLightComponents;
import de.lucalabs.fairylights.net.FilteredServerPlayNetworking;
import de.lucalabs.fairylights.net.clientbound.UpdateEntityFastenerMessage;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
