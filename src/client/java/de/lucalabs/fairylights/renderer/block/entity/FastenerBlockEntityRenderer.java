package de.lucalabs.fairylights.renderer.block.entity;

import de.lucalabs.fairylights.blocks.entity.FastenerBlockEntity;
import de.lucalabs.fairylights.components.FairyLightComponents;
import de.lucalabs.fairylights.fastener.BlockView;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public final class FastenerBlockEntityRenderer implements BlockEntityRenderer<FastenerBlockEntity> {

    private final BlockView view;
    private final FastenerRenderer renderer;

    public FastenerBlockEntityRenderer(final BlockEntityRendererFactory.Context context, final BlockView view) {
        this.view = view;
        this.renderer = new FastenerRenderer(context::getLayerModelPart);
    }

    @Override
    public boolean rendersOutsideBoundingBox(final FastenerBlockEntity fastener) {
        return true;
    }

    @Override
    public void render(
            final FastenerBlockEntity fastener,
            final float delta,
            final MatrixStack matrix,
            final VertexConsumerProvider bufferSource,
            final int packedLight,
            final int packedOverlay) {

        FairyLightComponents.FASTENER.get(fastener).get().ifPresent(f -> {
            //this.bindTexture(FastenerRenderer.TEXTURE);
            matrix.push();
            final Vec3d offset = fastener.getOffset();
            matrix.translate(offset.x, offset.y, offset.z);
            //this.view.unrotate(this.getWorld(), f.getPos(), FastenerBlockEntityRenderer.GlMatrix.INSTANCE, delta);
            this.renderer.render(f, delta, matrix, bufferSource, packedLight, packedOverlay);
            matrix.pop();
        });
    }
}
