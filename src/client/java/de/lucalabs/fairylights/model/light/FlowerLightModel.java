package de.lucalabs.fairylights.model.light;

import de.lucalabs.fairylights.util.MathHelper;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class FlowerLightModel extends ColorLightModel {
    public FlowerLightModel(final ModelPart root) {
        super(root);
    }

    public static TexturedModelData createLayer() {
        final LightMeshHelper helper = LightMeshHelper.create();
        helper.unlit().setTextureOffset(12, 0);
        helper.unlit().addBox(-1.5F, -1.0F, -1.5F, 3.0F, 3.0F, 3.0F);
        final BulbBuilder bulb = helper.createBulb();
        final Vector3f vec = new Vector3f(-1.0F, 0.0F, 1.0F);
        vec.normalize();
        final Quaternionf droop = RotationAxis.of(vec).rotation(-MathHelper.PI / 6.0F);
        final int petalCount = 5;
        for (int p = 0; p < petalCount; p++) {
            final Quaternionf q = RotationAxis.POSITIVE_Y.rotation(p * MathHelper.TAU / petalCount);
            q.mul(droop);
            final float[] magicAngles = toEuler(q);
            final BulbBuilder petalModel = bulb.createChild("petal_" + p, 24, 0);
            petalModel.addBox(0.0F, 0.0F, 0.0F, 5.0F, 1.0F, 5.0F);
            petalModel.setPosition(0.0F, 1.0F, 0.0F);
            petalModel.setAngles(magicAngles[0], magicAngles[1], magicAngles[2]);
        }
        return helper.build();
    }
}
