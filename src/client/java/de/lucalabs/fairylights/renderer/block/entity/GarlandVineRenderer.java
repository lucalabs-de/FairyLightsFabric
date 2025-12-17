package de.lucalabs.fairylights.renderer.block.entity;

import de.lucalabs.fairylights.connection.GarlandVineConnection;
import de.lucalabs.fairylights.renderer.FairyLightModelLayers;
import de.lucalabs.fairylights.renderer.RenderConstants;
import de.lucalabs.fairylights.util.Curve;
import de.lucalabs.fairylights.util.MathHelper;
import de.lucalabs.fairylights.util.RandomArray;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

import java.util.function.Function;

public class GarlandVineRenderer extends ConnectionRenderer<GarlandVineConnection> {
    private static final int RING_COUNT = 7;
    private static final RandomArray RAND = new RandomArray(8411, RING_COUNT * 4);

    private final RingsModel rings;

    protected GarlandVineRenderer(final Function<EntityModelLayer, ModelPart> baker) {
        super(baker, FairyLightModelLayers.VINE_WIRE);
        this.rings = new RingsModel(baker.apply(FairyLightModelLayers.GARLAND_RINGS));
    }

    @Override
    protected void render(
            final GarlandVineConnection conn,
            final Curve catenary,
            final float delta,
            final MatrixStack matrix,
            final VertexConsumerProvider source,
            final int packedLight,
            final int packedOverlay) {
        super.render(conn, catenary, delta, matrix, source, packedLight, packedOverlay);
        final int hash = conn.getUUID().hashCode();
        final VertexConsumer buf = RenderConstants.SOLID_TEXTURE.getVertexConsumer(source, RenderLayer::getEntityCutout);
        catenary.visitPoints(0.25F, false, (index, x, y, z, yaw, pitch) -> {
            matrix.push();
            matrix.translate(x, y, z);
            matrix.multiply(RotationAxis.POSITIVE_Y.rotation(-yaw));
            matrix.multiply(RotationAxis.POSITIVE_Z.rotation(pitch));
            matrix.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(RAND.get(index + hash) * 45.0F));
            matrix.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(RAND.get(index + 8 + hash) * 60.F + 90.0F));
            this.rings.setWhich(index % RING_COUNT);
            this.rings.render(matrix, buf, packedLight, packedOverlay, 0xFFFFFFFF);
            matrix.pop();
        });
    }

    public static TexturedModelData wireLayer() {
        return WireModel.createLayer(39, 0, 1);
    }

    public static class RingsModel extends Model {
        final ModelPart[] roots;
        int which;

        RingsModel(final ModelPart root) {
            super(RenderLayer::getEntityCutout);
            ModelPart[] roots = new ModelPart[RING_COUNT];
            for (int i = 0; i < RING_COUNT; i++) {
                roots[i] = root.getChild(Integer.toString(i));
            }
            this.roots = roots;
        }

        public static TexturedModelData createLayer() {
            final float size = 4.0F;
            ModelPartBuilder root = ModelPartBuilder.create()
                    .uv(14, 91)
                    .cuboid(-size / 2.0F, -size / 2.0F, -size / 2.0F, size, size, size);
            ModelTransform crossPose = ModelTransform.rotation(0.0F, 0.0F, MathHelper.HALF_PI);
            ModelData mesh = new ModelData();
            for (int i = 0; i < RING_COUNT; i++) {
                mesh.getRoot().addChild(Integer.toString(i), root, ModelTransform.NONE)
                        .addChild("cross_" + i, ModelPartBuilder.create()
                                .uv(i * 8, 64)
                                .cuboid(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F)
                                .cuboid(-4.0F, 0.0F, -4.0F, 8.0F, 0.0F, 8.0F), crossPose);
            }
            return TexturedModelData.of(mesh, 128, 128);
        }

        public void setWhich(int which) {
            this.which = which;
        }

        @Override
        public void render(final MatrixStack matrix, final VertexConsumer builder, final int light, final int overlay, int color) {
            this.roots[this.which].render(matrix, builder, light, overlay, color);
        }
    }
}
