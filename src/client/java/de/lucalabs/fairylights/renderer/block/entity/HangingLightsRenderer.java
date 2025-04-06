package de.lucalabs.fairylights.renderer.block.entity;

import de.lucalabs.fairylights.connection.HangingLightsConnection;
import de.lucalabs.fairylights.feature.light.Light;
import de.lucalabs.fairylights.items.SimpleLightVariant;
import de.lucalabs.fairylights.renderer.FairyLightModelLayers;
import de.lucalabs.fairylights.util.MathHelper;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

import java.util.function.Function;

public class HangingLightsRenderer extends ConnectionRenderer<HangingLightsConnection> {
    private final LightRenderer lights;

    public HangingLightsRenderer(final Function<EntityModelLayer, ModelPart> baker) {
        super(baker, FairyLightModelLayers.LIGHTS_WIRE);
        this.lights = new LightRenderer(baker);
    }

    @Override
    protected int getWireColor(final HangingLightsConnection conn) {
        return conn.getString().color();
    }

    @Override
    public void render(
            final HangingLightsConnection conn,
            final float delta,
            final MatrixStack matrix,
            final VertexConsumerProvider source,
            final int packedLight,
            final int packedOverlay) {

        super.render(conn, delta, matrix, source, packedLight, packedOverlay);
        final Light<?>[] lights = conn.getFeatures();
        if (lights == null) {
            return;
        }
        final LightRenderer.Data data = this.lights.start(source);
        for (int i = 0; i < lights.length; i++) {
            final Light<?> light = lights[i];
            final Vec3d pos = light.getPoint(delta);
            matrix.push();
            matrix.translate(pos.x, pos.y, pos.z);
            matrix.multiply(RotationAxis.POSITIVE_Y.rotation(-light.getYaw(delta)));
            if (light.parallelsCord()) {
                matrix.multiply(RotationAxis.POSITIVE_Z.rotation(light.getPitch(delta)));
            }
            matrix.multiply(RotationAxis.POSITIVE_X.rotation(light.getRoll(delta)));
            if (light.getVariant() != SimpleLightVariant.FAIRY_LIGHT) { // FIXME
                matrix.multiply(RotationAxis.POSITIVE_Y.rotation(MathHelper.mod(MathHelper.hash(i) * MathHelper.DEG_TO_RAD, MathHelper.TAU) + MathHelper.PI / 4.0F));
            }
            matrix.translate(0.0D, -light.getDescent(), 0.0D);
            this.lights.render(matrix, data, light, i, delta, packedLight, packedOverlay);
            matrix.pop();
        }
    }

    public static TexturedModelData wireLayer() {
        return WireModel.createLayer(0, 0, 2);
    }
}
