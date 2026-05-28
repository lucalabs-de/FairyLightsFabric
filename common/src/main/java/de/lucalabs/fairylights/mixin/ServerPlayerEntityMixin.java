package de.lucalabs.fairylights.mixin;


import de.lucalabs.fairylights.platform.Services;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "triggerDimensionChangeTriggers", at = @At("HEAD"))
    private void onDimensionChange(ServerLevel origin, CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        Services.COMPONENTS.maybeGet(player, Services.KEYS.FASTENER()).ifPresent(f -> f.setWorld(player.level()));
    }
}
