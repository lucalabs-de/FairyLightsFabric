package de.lucalabs.fairylights.model.light;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.TexturedModelData;

public class FairyLightModel extends ColorLightModel {
    public FairyLightModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData createLayer() {
        final LightMeshHelper helper = LightMeshHelper.create();
        final BulbBuilder bulb = helper.createBulb();
        bulb.setUV(46, 0);
        bulb.addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F);
        return helper.build();
    }
}
