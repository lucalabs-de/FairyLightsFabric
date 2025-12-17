package de.lucalabs.fairylights.renderer;

import de.lucalabs.fairylights.FairyLights;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public final class FairyLightModelLayers {

    public static final EntityModelLayer BOW = of("bow");
    public static final EntityModelLayer GARLAND_RINGS = of("garland_rings");
    public static final EntityModelLayer TINSEL_STRIP = of("tinsel_strip");
    public static final EntityModelLayer FAIRY_LIGHT = of("fairy_light");
    public static final EntityModelLayer PAPER_LANTERN = of("paper_lantern");
    public static final EntityModelLayer ORB_LANTERN = of("orb_lantern");
    public static final EntityModelLayer FLOWER_LIGHT = of("flower_light");
    public static final EntityModelLayer CANDLE_LANTERN_LIGHT = of("color_candle_lantern");
    public static final EntityModelLayer JACK_O_LANTERN = of("jack_o_lantern");
    public static final EntityModelLayer SKULL_LIGHT = of("skull_light");
    public static final EntityModelLayer GHOST_LIGHT = of("ghost_light");
    public static final EntityModelLayer SPIDER_LIGHT = of("spider_light");
    public static final EntityModelLayer WITCH_LIGHT = of("witch_light");
    public static final EntityModelLayer SNOWFLAKE_LIGHT = of("snowflake_light");
    public static final EntityModelLayer HEART_LIGHT = of("heart_light");
    public static final EntityModelLayer MOON_LIGHT = of("moon_light");
    public static final EntityModelLayer STAR_LIGHT = of("star_light");
    public static final EntityModelLayer ICICLE_LIGHTS_1 = of("icicle_lights_1");
    public static final EntityModelLayer ICICLE_LIGHTS_2 = of("icicle_lights_2");
    public static final EntityModelLayer ICICLE_LIGHTS_3 = of("icicle_lights_3");
    public static final EntityModelLayer ICICLE_LIGHTS_4 = of("icicle_lights_4");
    public static final EntityModelLayer METEOR_LIGHT = of("meteor_light");
    public static final EntityModelLayer OIL_LANTERN = of("oil_lantern");
    public static final EntityModelLayer CANDLE_LANTERN = of("candle_lantern");
    public static final EntityModelLayer INCANDESCENT_LIGHT = of("incandescent_light");
    public static final EntityModelLayer PENNANT_WIRE = of("pennant_wire");
    public static final EntityModelLayer LIGHTS_WIRE = of("lights_wire");
    public static final EntityModelLayer TINSEL_WIRE = of("tinsel_wire");
    public static final EntityModelLayer VINE_WIRE = of("vine_wire");

    private FairyLightModelLayers() {}

    private static EntityModelLayer of(String name) {
        return layer(name);
    }

    private static EntityModelLayer layer(String name) {
        return new EntityModelLayer(Identifier.of(FairyLights.ID, name), "main");
    }
}
