package de.lucalabs.fairylights.client.renderer.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import de.lucalabs.fairylights.main.blocks.entity.FastenerBlockEntity;
import de.lucalabs.fairylights.main.fastener.RegularBlockView;
import de.lucalabs.fairylights.platform.Services;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.AABB;

/**
 * NeoForge frustum-culls block entities by {@link #getRenderBoundingBox}, whose default is the
 * single block. A fastener renders its connections (light strings) far beyond its own block, so the
 * common renderer's {@code shouldRenderOffScreen} alone is not enough on NeoForge — the string
 * disappears (and flickers while moving) whenever the fastener's block leaves the view frustum.
 *
 * This thin wrapper delegates the actual rendering to the common {@link FastenerBlockEntityRenderer}
 * and additionally reports a render box that spans the whole connection, the same way the fence
 * fastener entity does via {@code getBoundingBoxForCulling}. The render box hook only exists on
 * NeoForge, so it cannot live in the common renderer.
 */
public final class NeoForgeFastenerBlockEntityRenderer implements BlockEntityRenderer<FastenerBlockEntity> {

    private final FastenerBlockEntityRenderer delegate;

    public NeoForgeFastenerBlockEntityRenderer(final BlockEntityRendererProvider.Context context) {
        this.delegate = new FastenerBlockEntityRenderer(context, new RegularBlockView());
    }

    @Override
    public void render(final FastenerBlockEntity fastener, final float delta, final PoseStack matrix,
                       final MultiBufferSource bufferSource, final int packedLight, final int packedOverlay) {
        this.delegate.render(fastener, delta, matrix, bufferSource, packedLight, packedOverlay);
    }

    @Override
    public boolean shouldRenderOffScreen(final FastenerBlockEntity fastener) {
        return this.delegate.shouldRenderOffScreen(fastener);
    }

    @Override
    public AABB getRenderBoundingBox(final FastenerBlockEntity fastener) {
        return Services.COMPONENTS.maybeGet(fastener, Services.KEYS.FASTENER())
                .map(f -> f.getBounds().inflate(1.0D))
                .orElseGet(() -> new AABB(fastener.getBlockPos()));
    }
}
