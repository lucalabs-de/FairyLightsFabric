package de.lucalabs.fairylights.renderer.block.entity;

import de.lucalabs.fairylights.connection.GarlandTinselConnection;
import de.lucalabs.fairylights.renderer.FairyLightModelLayers;
import de.lucalabs.fairylights.renderer.RenderConstants;
import de.lucalabs.fairylights.util.Catenary;
import de.lucalabs.fairylights.util.RandomArray;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import java.util.function.Function;

public class GarlandTinselRenderer extends ConnectionRenderer<GarlandTinselConnection> {

    private static final RandomArray RAND = new RandomArray(9171, 128);

    private final StripModel strip;

    public GarlandTinselRenderer(final Function<EntityModelLayer, ModelPart> baker) {
        super(baker, FairyLightModelLayers.TINSEL_WIRE);
        this.strip = new StripModel(baker.apply(FairyLightModelLayers.TINSEL_STRIP));
    }

    @Override
    protected int getWireColor(final GarlandTinselConnection conn) {
        return conn.getColor();
    }

    @Override
    protected void renderSegment(
            final GarlandTinselConnection connection,
            final Catenary.SegmentView it,
            final float delta,
            final MatrixStack matrix,
            final int packedLight,
            final VertexConsumerProvider source,
            final int packedOverlay) {
        super.renderSegment(connection, it, delta, matrix, packedLight, source, packedOverlay);
        final int color = connection.getColor();
        final float r = ((color >> 16) & 0xFF) / 255.0F;
        final float g = ((color >> 8) & 0xFF) / 255.0F;
        final float b = (color & 0xFF) / 255.0F;
        matrix.push();
        matrix.translate(it.getX(0.0F), it.getY(0.0F), it.getZ(0.0F));
        matrix.multiply(RotationAxis.POSITIVE_Y.rotation(-it.getYaw()));
        matrix.multiply(RotationAxis.POSITIVE_Z.rotation(it.getPitch()));
        final float length = it.getLength();
        final int rings = MathHelper.ceil(length * 64);
        final int hash = connection.getUUID().hashCode();
        final int index = it.getIndex();
        final VertexConsumer buf = RenderConstants.SOLID_TEXTURE.getVertexConsumer(source, RenderLayer::getEntityCutout);
        for (int i = 0; i < rings; i++) {
            final double t = i / (float) rings * length;
            matrix.push();
            matrix.translate(t, 0.0F, 0.0F);
            final float rotX = RAND.get(31 * (index + 31 * i) + hash) * 22;
            final float rotY = RAND.get(31 * (index + 3 + 31 * i) + hash) * 180;
            final float rotZ = RAND.get(31 * (index + 7 + 31 * i) + hash) * 180;
            matrix.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotZ));
            matrix.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotY));
            matrix.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotX));
            matrix.scale(1.0F, RAND.get(i * 63) * 0.1F + 1.0F, 0.5F);
            this.strip.render(matrix, buf, packedLight, packedOverlay, ColorHelper.Argb.fromFloats(1.0F, r, g, b));
            matrix.pop();
        }

        matrix.pop();
    }

    public static TexturedModelData wireLayer() {
        return WireModel.createLayer(62, 0, 1);
    }

    public static class StripModel extends Model {
        final ModelPart root;

        StripModel(final ModelPart root) {
            super(RenderLayer::getEntityCutout);
            this.root = root;
        }

        public static TexturedModelData createLayer() {
            ModelData mesh = new ModelData();
            mesh.getRoot().addChild("root", ModelPartBuilder.create()
                    .uv(62, 0)
                    .cuboid(-0.5F, -3.0F, 0.0F, 1.0F, 6.0F, 0.0F), ModelTransform.NONE);
            return TexturedModelData.of(mesh, 128, 128);
        }

        @Override
        public void render(final MatrixStack matrix, final VertexConsumer builder, final int light, final int overlay, int color) {
            this.root.render(matrix, builder, light, overlay, color);
        }
    }
}
