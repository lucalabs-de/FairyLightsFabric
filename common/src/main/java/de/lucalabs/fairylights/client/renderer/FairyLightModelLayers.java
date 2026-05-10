package de.lucalabs.fairylights.client.renderer;

import de.lucalabs.fairylights.Common;
import net.minecraft.client.model.geom.ModelLayerLocation;

public final class FairyLightModelLayers {

    public static final ModelLayerLocation BOW = of("bow");
    public static final ModelLayerLocation GARLAND_RINGS = of("garland_rings");
    public static final ModelLayerLocation TINSEL_STRIP = of("tinsel_strip");
    public static final ModelLayerLocation FAIRY_LIGHT = of("fairy_light");
    public static final ModelLayerLocation PAPER_LANTERN = of("paper_lantern");
    public static final ModelLayerLocation ORB_LANTERN = of("orb_lantern");
    public static final ModelLayerLocation FLOWER_LIGHT = of("flower_light");
    public static final ModelLayerLocation CANDLE_LANTERN_LIGHT = of("color_candle_lantern");
    public static final ModelLayerLocation JACK_O_LANTERN = of("jack_o_lantern");
    public static final ModelLayerLocation SKULL_LIGHT = of("skull_light");
    public static final ModelLayerLocation GHOST_LIGHT = of("ghost_light");
    public static final ModelLayerLocation SPIDER_LIGHT = of("spider_light");
    public static final ModelLayerLocation WITCH_LIGHT = of("witch_light");
    public static final ModelLayerLocation SNOWFLAKE_LIGHT = of("snowflake_light");
    public static final ModelLayerLocation HEART_LIGHT = of("heart_light");
    public static final ModelLayerLocation MOON_LIGHT = of("moon_light");
    public static final ModelLayerLocation STAR_LIGHT = of("star_light");
    public static final ModelLayerLocation ICICLE_LIGHTS_1 = of("icicle_lights_1");
    public static final ModelLayerLocation ICICLE_LIGHTS_2 = of("icicle_lights_2");
    public static final ModelLayerLocation ICICLE_LIGHTS_3 = of("icicle_lights_3");
    public static final ModelLayerLocation ICICLE_LIGHTS_4 = of("icicle_lights_4");
    public static final ModelLayerLocation METEOR_LIGHT = of("meteor_light");
    public static final ModelLayerLocation OIL_LANTERN = of("oil_lantern");
    public static final ModelLayerLocation CANDLE_LANTERN = of("candle_lantern");
    public static final ModelLayerLocation INCANDESCENT_LIGHT = of("incandescent_light");
    public static final ModelLayerLocation PENNANT_WIRE = of("pennant_wire");
    public static final ModelLayerLocation LIGHTS_WIRE = of("lights_wire");
    public static final ModelLayerLocation TINSEL_WIRE = of("tinsel_wire");
    public static final ModelLayerLocation VINE_WIRE = of("vine_wire");

    private FairyLightModelLayers() {}

    private static ModelLayerLocation of(String name) {
        return layer(name);
    }

    private static ModelLayerLocation layer(String name) {
        return new ModelLayerLocation(Common.id(name), "main");
    }
}
