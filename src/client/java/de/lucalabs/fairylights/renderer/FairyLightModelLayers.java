package de.lucalabs.fairylights.renderer;

import de.lucalabs.fairylights.FairyLights;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public final class FairyLightModelLayers {

    public static final EntityModelLayer BOW = of("bow");
    public static final EntityModelLayer FAIRY_LIGHT = of("fairy_light");
    public static final EntityModelLayer PAPER_LANTERN = of("paper_lantern");
    public static final EntityModelLayer PENNANT_WIRE = of("pennant_wire");
    public static final EntityModelLayer LIGHTS_WIRE = of("lights_wire");
    public static final EntityModelLayer INCANDESCENT_LIGHT = of("incandescent_light");

    private FairyLightModelLayers() {}

    private static EntityModelLayer of(String name) {
        return layer(name);
    }

    private static EntityModelLayer layer(String name) {
        return new EntityModelLayer(new Identifier(FairyLights.ID, name), "main");
    }
}
