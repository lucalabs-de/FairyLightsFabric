package de.lucalabs.fairylights.mixin;

import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.components.FairyLightComponents;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Inject(
            method = "onPlayerConnect",
            at = @At("TAIL")
    )
    private void onPlayerJoin(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        FairyLights.LOGGER.info("spawned in dimension {}", player.getWorld().getRegistryKey());
        FairyLightComponents.FASTENER.get(player).get().ifPresent(f -> f.setWorld(player.getWorld()));
    }
}
