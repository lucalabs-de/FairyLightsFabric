package de.lucalabs.fairylights.renderer.compat;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.function.Function;

public final class AltRenderLayers {
    private AltRenderLayers() {}

    public static RenderLayer getUnsortedTranslucent(Identifier textureLocation)
    {
        return Internal.UNSORTED_TRANSLUCENT.apply(textureLocation);
    }

    private static class Internal extends RenderLayer {

        private Internal(String name, VertexFormat fmt, VertexFormat.DrawMode glMode, int size, boolean doCrumbling, boolean depthSorting, Runnable onEnable, Runnable onDisable)
        {
            super(name, fmt, glMode, size, doCrumbling, depthSorting, onEnable, onDisable);
            throw new IllegalStateException("This class must not be instantiated");
        }

        public static Function<Identifier, RenderLayer> UNSORTED_TRANSLUCENT = Util.memoize(Internal::unsortedTranslucent);
        private static RenderLayer unsortedTranslucent(Identifier textureLocation)
        {
            final boolean sortingEnabled = false;
            MultiPhaseParameters renderState = MultiPhaseParameters.builder()
                    .program(RenderLayer.ENTITY_TRANSLUCENT_PROGRAM)
                    .texture(new Texture(textureLocation, false, false))
                    .transparency(TRANSLUCENT_TRANSPARENCY)
                    .cull(DISABLE_CULLING)
                    .lightmap(ENABLE_LIGHTMAP)
                    .overlay(ENABLE_OVERLAY_COLOR)
                    .build(true);
            return of(
                    "forge_entity_unsorted_translucent",
                    VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                    VertexFormat.DrawMode.QUADS,
                    256,
                    true,
                    sortingEnabled,
                    renderState);
        }
    }
}
