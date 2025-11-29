package de.lucalabs.fairylights.model.light;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class BowModel extends Model {
    private final ModelPart root;

    public BowModel(ModelPart root) {
        super(RenderLayer::getEntityCutout);
        this.root = root;
    }

    public static TexturedModelData createLayer() {
        ModelData mesh = new ModelData();
        ModelPartData root = mesh.getRoot().addChild("root", ModelPartBuilder.create()
                .uv(6, 72)
                .cuboid(-2.0F, -1.5F, -1.0F, 4.0F, 3.0F, 2.0F), ModelTransform.pivot(0.0F, 0.5F, -3.25F));
        root.addChild("bone", ModelPartBuilder.create()
                .uv(0, 77)
                .cuboid(-5.0F, -4.0F, 0.0F, 5.0F, 5.0F, 1.0F), ModelTransform.of(-1.0F, 1.0F, 0.0F, 0.0F, 0.1745F, -0.5236F));
        root.addChild("bone2", ModelPartBuilder.create()
                .uv(0, 77)
                .cuboid(0.0F, -4.0F, 0.0F, 5.0F, 5.0F, 1.0F), ModelTransform.of(1.0F, 1.0F, 0.0F, 0.0F, -0.1745F, 0.5236F));
        root.addChild("bone3", ModelPartBuilder.create()
                .uv(0, 72)
                .cuboid(-2.0F, -4.0F, 0.0F, 2.0F, 4.0F, 1.0F), ModelTransform.of(0.0F, -1.0F, 0.0F, 0.0873F, 0.0873F, -0.1745F));
        root.addChild("bone4", ModelPartBuilder.create()
                .uv(0, 72)
                .cuboid(0.0F, -4.0F, 0.0F, 2.0F, 4.0F, 1.0F), ModelTransform.of(0.0F, -1.0F, 0.0F, 0.0873F, -0.0873F, 0.1745F));
        return TexturedModelData.of(mesh, 128, 128);
    }

    @Override
    public void render(
            final MatrixStack matrix,
            final VertexConsumer builder,
            final int light,
            final int overlay,
            final int color) {

        this.root.render(matrix, builder, light, overlay, color);
    }
}
