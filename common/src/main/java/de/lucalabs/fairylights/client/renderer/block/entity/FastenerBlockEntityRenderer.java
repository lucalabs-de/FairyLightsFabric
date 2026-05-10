package de.lucalabs.fairylights.client.renderer.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import de.lucalabs.fairylights.main.blocks.entity.FastenerBlockEntity;
import de.lucalabs.fairylights.main.fastener.BlockView;
import de.lucalabs.fairylights.platform.Services;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public final class FastenerBlockEntityRenderer implements BlockEntityRenderer<FastenerBlockEntity> {

    private final BlockView view;
    private final FastenerRenderer renderer;

    public FastenerBlockEntityRenderer(final BlockEntityRendererProvider.Context context, final BlockView view) {
        this.view = view;
        this.renderer = new FastenerRenderer(context::bakeLayer);
    }

    @Override
    public boolean shouldRenderOffScreen(final @NotNull FastenerBlockEntity fastener) {
        return true;
    }

    @Override
    public void render(
            final @NotNull FastenerBlockEntity fastener,
            final float delta,
            final @NotNull PoseStack matrix,
            final @NotNull MultiBufferSource bufferSource,
            final int packedLight,
            final int packedOverlay) {

        Services.COMPONENTS.maybeGet(fastener, Services.KEYS.FASTENER()).ifPresent(f -> {
            //this.bindTexture(FastenerRenderer.TEXTURE);
            matrix.pushPose();
            final Vec3 offset = fastener.getOffset();
            matrix.translate(offset.x, offset.y, offset.z);
            //this.view.unrotate(this.getWorld(), f.getPos(), FastenerBlockEntityRenderer.GlMatrix.INSTANCE, delta);
            this.renderer.render(f, delta, matrix, bufferSource, packedLight, packedOverlay);
            matrix.popPose();
        });
    }
}
