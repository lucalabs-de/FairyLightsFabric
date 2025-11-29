package de.lucalabs.fairylights.mixin;


import de.lucalabs.fairylights.components.FairyLightComponents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "worldChanged", at = @At("HEAD"))
    private void onDimensionChange(ServerWorld origin, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        FairyLightComponents.FASTENER.get(player).get().ifPresent(f -> f.setWorld(player.getWorld()));
    }
}
