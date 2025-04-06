package de.lucalabs.fairylights.mixin.client;

import de.lucalabs.fairylights.events.ClientEventHandler;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(at = @At("RETURN"), method = "updateTargetedEntity")
    public void updateTargetedEntity(float delta, CallbackInfo ci) {
        ClientEventHandler.updateHitConnection();
    }
}
