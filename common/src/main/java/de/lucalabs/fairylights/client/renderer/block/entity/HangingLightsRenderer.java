package de.lucalabs.fairylights.client.renderer.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import de.lucalabs.fairylights.client.renderer.FairyLightModelLayers;
import de.lucalabs.fairylights.main.connection.HangingLightsConnection;
import de.lucalabs.fairylights.main.feature.light.Light;
import de.lucalabs.fairylights.main.items.SimpleLightVariant;
import de.lucalabs.fairylights.main.util.MathHelper;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;

public class HangingLightsRenderer extends ConnectionRenderer<HangingLightsConnection> {
    private final LightRenderer lights;

    public HangingLightsRenderer(final Function<ModelLayerLocation, ModelPart> baker) {
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
            final PoseStack matrix,
            final MultiBufferSource source,
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
            final Vec3 pos = light.getPoint(delta);
            matrix.pushPose();
            matrix.translate(pos.x, pos.y, pos.z);
            matrix.mulPose(Axis.YP.rotation(-light.getYaw(delta)));
            if (light.parallelsCord()) {
                matrix.mulPose(Axis.ZP.rotation(light.getPitch(delta)));
            }
            matrix.mulPose(Axis.XP.rotation(light.getRoll(delta)));
            if (light.getVariant() != SimpleLightVariant.FAIRY_LIGHT) { // FIXME
                matrix.mulPose(Axis.YP.rotation(MathHelper.mod(MathHelper.hash(i) * MathHelper.DEG_TO_RAD, MathHelper.TAU) + MathHelper.PI / 4.0F));
            }
            matrix.translate(0.0D, -light.getDescent(), 0.0D);
            this.lights.render(matrix, data, light, i, delta, packedLight, packedOverlay);
            matrix.popPose();
        }
    }

    public static LayerDefinition wireLayer() {
        return WireModel.createLayer(0, 0, 2);
    }
}
