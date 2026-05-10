package de.lucalabs.fairylights.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.components.FairyLightComponents;
import de.lucalabs.fairylights.entity.FenceFastenerEntity;
import de.lucalabs.fairylights.renderer.block.entity.FastenerRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LightLayer;
import org.jetbrains.annotations.NotNull;

public final class FenceFastenerRenderer extends EntityRenderer<FenceFastenerEntity> {
    public static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(FairyLights.ID, "block/fence_fastener");

    private final FastenerRenderer renderer;

    public FenceFastenerRenderer(final EntityRendererProvider.Context context) {
        super(context);
        this.renderer = new FastenerRenderer(context::bakeLayer);
    }

    @Override
    protected int getBlockLightLevel(final FenceFastenerEntity entity, final BlockPos delta) {
        return entity.level().getBrightness(LightLayer.BLOCK, entity.blockPosition());
    }

    @Override
    public void render(
            final FenceFastenerEntity entity,
            final float yaw,
            final float delta,
            final PoseStack matrix,
            final MultiBufferSource source,
            final int packedLight) {

        final VertexConsumer buf = source.getBuffer(Sheets.cutoutBlockSheet());
        matrix.pushPose();
        FastenerRenderer.renderBakedModel(MODEL, matrix, buf, 1.0F, 1.0F, 1.0F, packedLight, OverlayTexture.NO_OVERLAY);
        matrix.popPose();
        FairyLightComponents.FASTENER.get(entity).get().ifPresent(
                f -> this.renderer.render(f, delta, matrix, source, packedLight, OverlayTexture.NO_OVERLAY));
        super.render(entity, yaw, delta, matrix, source, packedLight);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull ResourceLocation getTextureLocation(final FenceFastenerEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
