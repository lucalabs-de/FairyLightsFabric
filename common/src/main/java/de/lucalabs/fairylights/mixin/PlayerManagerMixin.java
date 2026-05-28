package de.lucalabs.fairylights.mixin;

import de.lucalabs.fairylights.Constants;
import de.lucalabs.fairylights.platform.Services;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class PlayerManagerMixin {
    @Inject(
            method = "placeNewPlayer",
            at = @At("TAIL")
    )
    private void onPlayerJoin(Connection connection, ServerPlayer player, CommonListenerCookie clientData, CallbackInfo ci) {
        Constants.LOG.info("spawned in dimension {}", player.level().dimension());
        Services.COMPONENTS.maybeGet(player, Services.KEYS.FASTENER()).ifPresent(f -> f.setWorld(player.level()));
    }
}
