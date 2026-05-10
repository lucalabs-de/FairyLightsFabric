package de.lucalabs.fairylights.client.renderer;

import de.lucalabs.fairylights.Common;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

public class RenderConstants {

    public static final float HIGHLIGHT_ALPHA = 0.4F;

    @SuppressWarnings("deprecation")
    public static final Material SOLID_TEXTURE = new Material(
            TextureAtlas.LOCATION_BLOCKS,
            Common.id("entity/connections"));

    @SuppressWarnings("deprecation")
    public static final Material TRANSLUCENT_TEXTURE = new Material(
            TextureAtlas.LOCATION_BLOCKS,
            Common.id("entity/connections"));

}
