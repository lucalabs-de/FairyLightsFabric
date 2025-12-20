package de.lucalabs.fairylights.renderer.block.entity;

import de.lucalabs.fairylights.connection.*;
import de.lucalabs.fairylights.fastener.Fastener;
import de.lucalabs.fairylights.fastener.FenceFastener;
import de.lucalabs.fairylights.model.light.BowModel;
import de.lucalabs.fairylights.renderer.FairyLightModelLayers;
import de.lucalabs.fairylights.renderer.RenderConstants;
import de.lucalabs.fairylights.util.Tags;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.function.Function;

public class FastenerRenderer {
    private final HangingLightsRenderer hangingLights;
    private final PennantBuntingRenderer pennants;
    private final GarlandVineRenderer garland;
    private final GarlandTinselRenderer tinsel;
    private final BowModel bow;

    public FastenerRenderer(final Function<EntityModelLayer, ModelPart> baker) {
        this.hangingLights = new HangingLightsRenderer(baker);
        this.pennants = new PennantBuntingRenderer(baker);
        this.garland = new GarlandVineRenderer(baker);
        this.tinsel = new GarlandTinselRenderer(baker);
        this.bow = new BowModel(baker.apply(FairyLightModelLayers.BOW));
    }

    public void render(
            final Fastener<?> fastener,
            final float delta,
            final MatrixStack matrix,
            final VertexConsumerProvider source,
            final int packedLight,
            final int packedOverlay) {

        boolean renderBow = true;
        for (final Connection conn : fastener.getAllConnections()) {
            if (conn.getFastener() == fastener) {
                this.renderConnection(delta, matrix, source, packedLight, packedOverlay, conn);
            }
            if (renderBow && conn instanceof GarlandVineConnection && this.renderBow(fastener, matrix, source, packedLight, packedOverlay)) {
                renderBow = false;
            }
        }
    }

    private boolean renderBow(
            Fastener<?> fastener,
            MatrixStack matrix,
            VertexConsumerProvider source,
            int packedLight,
            int packedOverlay) {

        if (fastener instanceof FenceFastener) {
            final World world = fastener.getWorld();
            if (world == null) {
                return false;
            }
            final BlockState state = world.getBlockState(fastener.getPos());
            if (!state.isIn(Tags.FENCES)) {
                return false;
            }
            final VertexConsumer buf = RenderConstants.SOLID_TEXTURE.getVertexConsumer(source, RenderLayer::getEntityCutout);
            final float offset = -1.5F / 16.0F;
            final boolean north = state.get(FenceBlock.NORTH);
            final boolean east = state.get(FenceBlock.EAST);
            final boolean south = state.get(FenceBlock.SOUTH);
            final boolean west = state.get(FenceBlock.WEST);
            boolean tryDirX = true;
            boolean bow = false;
            if (!north && (east || west)) {
                this.bow(matrix, Direction.NORTH, offset, buf, packedLight, packedOverlay);
                tryDirX = false;
                bow = true;
            }
            if (!south && (east || west)) {
                this.bow(matrix, Direction.SOUTH, offset, buf, packedLight, packedOverlay);
                tryDirX = false;
                bow = true;
            }
            if (tryDirX) {
                if (!east && (north || south)) {
                    this.bow(matrix, Direction.EAST, offset, buf, packedLight, packedOverlay);
                    bow = true;
                }
                if (!west && (north || south)) {
                    this.bow(matrix, Direction.WEST, offset, buf, packedLight, packedOverlay);
                    bow = true;
                }
            }
            return bow;
        } else if (fastener.getFacing().getAxis() != Direction.Axis.Y) {
            final VertexConsumer buf = RenderConstants.SOLID_TEXTURE.getVertexConsumer(source, RenderLayer::getEntityCutout);
            this.bow(matrix, fastener.getFacing(), 0.0F, buf, packedLight, packedOverlay);
            return true;
        }
        return false;
    }

    private void bow(MatrixStack matrix, Direction dir, float offset, VertexConsumer buf, int packedLight, int packedOverlay) {
        matrix.push();
        matrix.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - dir.asRotation()));
        if (offset != 0.0F) {
            matrix.translate(0.0D, 0.0D, offset);
        }
        this.bow.render(matrix, buf, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        matrix.pop();
    }

    private void renderConnection(final float delta, final MatrixStack matrix, final VertexConsumerProvider source, final int packedLight, final int packedOverlay, final Connection conn) {
        if (conn instanceof HangingLightsConnection) {
            this.hangingLights.render((HangingLightsConnection) conn, delta, matrix, source, packedLight, packedOverlay);
        } else if (conn instanceof PennantBuntingConnection) {
            this.pennants.render((PennantBuntingConnection) conn, delta, matrix, source, packedLight, packedOverlay);
        } else if (conn instanceof GarlandTinselConnection) {
            this.tinsel.render((GarlandTinselConnection) conn, delta, matrix, source, packedLight, packedOverlay);
        } else if (conn instanceof GarlandVineConnection) {
            this.garland.render((GarlandVineConnection) conn, delta, matrix, source, packedLight, packedOverlay);
        }
    }

    public static void renderBakedModel(final Identifier path, final MatrixStack matrix, final VertexConsumer buf, final float r, final float g, final float b, final int packedLight, final int packedOverlay) {
        renderBakedModel(MinecraftClient.getInstance().getBakedModelManager().getModel(path), matrix, buf, r, g, b, packedLight, packedOverlay);
    }

    public static void renderBakedModel(final BakedModel model, final MatrixStack matrix, final VertexConsumer buf, final float r, final float g, final float b, final int packedLight, final int packedOverlay) {
        renderBakedModel(model, ModelTransformationMode.FIXED, matrix, buf, r, g, b, packedLight, packedOverlay);
    }

    // (refusing to use handlePerspective due to IForgeTransformationMatrix#push superfluous undocumented MatrixStack#push)
    public static void renderBakedModel(
            final BakedModel model,
            final ModelTransformationMode type,
            final MatrixStack matrix,
            final VertexConsumer buf,
            final float r,
            final float g,
            final float b,
            final int packedLight,
            final int packedOverlay) {

        model.getTransformation().getTransformation(type).apply(false, matrix);

        MatrixStack.Entry lastStack = matrix.peek();

        Random randSource = Random.create();
        for (final Direction side : Direction.values()) {
            randSource.setSeed(42L);
            for (final BakedQuad quad : model.getQuads(null, side, randSource)) {
                buf.quad(lastStack, quad, r, g, b, packedLight, packedOverlay);
            }
        }

        randSource.setSeed(42L);
        for (final BakedQuad quad : model.getQuads(null, null, randSource)) {
            buf.quad(lastStack, quad, r, g, b, packedLight, packedOverlay);
        }
    }
}
