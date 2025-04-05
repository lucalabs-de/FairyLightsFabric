package de.lucalabs.fairylights.renderer;

import de.lucalabs.fairylights.FairyLights;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

public class RenderConstants {

    @SuppressWarnings("deprecation")
    public static final SpriteIdentifier SOLID_TEXTURE = new SpriteIdentifier(
            SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE,
            new Identifier(FairyLights.ID, "entity/connections"));

}
