package de.lucalabs.fairylights.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import de.lucalabs.fairylights.client.events.ClientEventHandler;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class WorldRendererMixin {
    @Inject(
            method = "renderLevel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;endLastBatch()V"
            ))
    private void onDrawEntityHighlight(
            CallbackInfo ci,
            @Local(argsOnly = true) Camera camera,
            @Local(argsOnly = true) DeltaTracker tickCounter,
            @Local PoseStack matrix,
            @Local MultiBufferSource.BufferSource buf) {
        HitResult target = Minecraft.getInstance().hitResult;
        if (target != null && target.getType() == HitResult.Type.ENTITY) {
            if (target instanceof EntityHitResult entityTarget) {
                ClientEventHandler.onDrawEntityHighlight(entityTarget.getEntity(), camera, tickCounter.getGameTimeDeltaPartialTick(true), matrix, buf);
            }
        }
    }
}
