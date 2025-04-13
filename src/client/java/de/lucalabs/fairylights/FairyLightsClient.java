package de.lucalabs.fairylights;

import de.lucalabs.fairylights.blocks.entity.FairyLightBlockEntities;
import de.lucalabs.fairylights.entity.FairyLightEntities;
import de.lucalabs.fairylights.fastener.RegularBlockView;
import de.lucalabs.fairylights.items.ItemColorManager;
import de.lucalabs.fairylights.model.light.*;
import de.lucalabs.fairylights.renderer.FairyLightModelLayers;
import de.lucalabs.fairylights.renderer.block.entity.FastenerBlockEntityRenderer;
import de.lucalabs.fairylights.renderer.block.entity.LightBlockEntityRenderer;
import de.lucalabs.fairylights.renderer.block.entity.PennantBuntingRenderer;
import de.lucalabs.fairylights.renderer.entity.FenceFastenerRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.util.Identifier;

public class FairyLightsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(
                FairyLightBlockEntities.FASTENER,
                context -> new FastenerBlockEntityRenderer(context, new RegularBlockView()));

        BlockEntityRendererFactories.register(
                FairyLightBlockEntities.LIGHT,
                LightBlockEntityRenderer::new
        );

        EntityRendererRegistry.register(FairyLightEntities.FASTENER, FenceFastenerRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.BOW, BowModel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.FAIRY_LIGHT, FairyLightModel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.OIL_LANTERN, OilLanternModel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.INCANDESCENT_LIGHT, IncandescentLightModel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.PENNANT_WIRE, PennantBuntingRenderer::wireLayer);
        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.LIGHTS_WIRE, PennantBuntingRenderer::wireLayer);

        ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {
            // Tell Fabric to load this model during resource reloading
            out.accept(FenceFastenerRenderer.MODEL);
            PennantBuntingRenderer.MODELS.forEach(out);
        });


        ItemColorManager.setupColors();
    }

}