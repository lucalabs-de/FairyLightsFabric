package de.lucalabs.fairylights.model.light;

import de.lucalabs.fairylights.feature.light.BrightnessLightBehavior;
import de.lucalabs.fairylights.feature.light.Light;
import de.lucalabs.fairylights.util.ColorUtils;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class IncandescentLightModel extends LightModel<BrightnessLightBehavior> {
    final ModelPart bulb;
    final ModelPart filament;

    public IncandescentLightModel(final ModelPart root) {
        super(root);
        this.bulb = root.getChild("bulb");
        this.filament = root.getChild("filament");
    }

    public static TexturedModelData createLayer() {
        final LightMeshHelper helper = LightMeshHelper.create();
        helper.unlit().setTextureOffset(90, 10);
        helper.unlit().addBox(-1.0F, -0.01F, -1.0F, 2.0F, 1.0F, 2.0F);
        EasyMeshBuilder bulb = new EasyMeshBuilder("bulb", 98, 10);
        bulb.addBox(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F);
        helper.extra().add(bulb);
        EasyMeshBuilder filament = new EasyMeshBuilder("filament", 90, 13);
        filament.addBox(-1.0F, -3.0F, 0.0F, 2.0F, 3.0F, 0.0F);
        helper.extra().add(filament);
        return helper.build();
    }

    @Override
    public void animate(final Light<?> light, final BrightnessLightBehavior behavior, final float delta) {
        super.animate(light, behavior, delta);
        this.brightness = behavior.getBrightness(delta);
    }

    @Override
    protected int getLight(final int packedLight) {
        return (int) Math.max((this.brightness * 15.0F * 16.0F), packedLight & 255) | packedLight & (255 << 16);
    }

    @Override
    public void render(final MatrixStack matrix, final VertexConsumer builder, final int light, final int overlay, int color) {
        super.render(matrix, builder, light, overlay, color);
        final int emissiveLight = this.getLight(light);

        final float cr = 0.23F, cg = 0.18F, cb = 0.14F;
        final float br = this.brightness;
        this.filament.render(matrix, builder, emissiveLight, overlay,
                ColorUtils.transformArgb(
                        color,
                        a -> a,
                        r -> r * (cr * (1.0F - br) + br),
                        g -> g * (cg * (1.0F - br) + br),
                        b -> b * (cb * (1.0F - br) + br)
                ));
    }

    @Override
    public void renderTranslucent(final MatrixStack matrix, final VertexConsumer builder, final int light, final int overlay, int color) {
        final float bi = this.brightness;
        final int emissiveLight = this.getLight(light);
        final float br = 1.0F, bg = 0.73F, bb = 0.3F;
        this.bulb.render(matrix, builder, emissiveLight, overlay,
                ColorUtils.transformArgb(
                        color,
                        a -> bi * 0.4F + 0.25F,
                        r -> r * (br * bi + (1.0F - bi)),
                        g -> g * (bg * bi + (1.0F - bi)),
                        b -> b * (bb * bi + (1.0F - bi))
                ));
        super.renderTranslucent(matrix, builder, light, overlay, color);
    }
}
