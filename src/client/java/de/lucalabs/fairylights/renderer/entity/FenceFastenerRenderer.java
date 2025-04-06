package de.lucalabs.fairylights.renderer.entity;

import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.components.FairyLightComponents;
import de.lucalabs.fairylights.entity.FenceFastenerEntity;
import de.lucalabs.fairylights.renderer.block.entity.FastenerRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;

public final class FenceFastenerRenderer extends EntityRenderer<FenceFastenerEntity> {
    public static final Identifier MODEL = new Identifier(FairyLights.ID, "block/fence_fastener");

    private final FastenerRenderer renderer;

    public FenceFastenerRenderer(final EntityRendererFactory.Context context) {
        super(context);
        this.renderer = new FastenerRenderer(context::getPart);
    }

    @Override
    protected int getBlockLight(final FenceFastenerEntity entity, final BlockPos delta) {
        return entity.getWorld().getLightLevel(LightType.BLOCK, entity.getBlockPos());
    }

    @Override
    public void render(
            final FenceFastenerEntity entity,
            final float yaw,
            final float delta,
            final MatrixStack matrix,
            final VertexConsumerProvider source,
            final int packedLight) {

        final VertexConsumer buf = source.getBuffer(TexturedRenderLayers.getEntityCutout());
        matrix.push();
        FastenerRenderer.renderBakedModel(MODEL, matrix, buf, 1.0F, 1.0F, 1.0F, packedLight, OverlayTexture.DEFAULT_UV);
        matrix.pop();
        FairyLightComponents.FASTENER.get(entity).get().ifPresent(
                f -> this.renderer.render(f, delta, matrix, source, packedLight, OverlayTexture.DEFAULT_UV));
        super.render(entity, yaw, delta, matrix, source, packedLight);
    }

    @SuppressWarnings("deprecation")
    @Override
    public Identifier getTexture(final FenceFastenerEntity entity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}
