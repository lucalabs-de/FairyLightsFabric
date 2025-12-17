package de.lucalabs.fairylights;

import de.lucalabs.fairylights.blocks.entity.FairyLightBlockEntities;
import de.lucalabs.fairylights.entity.FairyLightEntities;
import de.lucalabs.fairylights.fastener.RegularBlockView;
import de.lucalabs.fairylights.items.ItemColorManager;
import de.lucalabs.fairylights.model.light.*;
import de.lucalabs.fairylights.renderer.FairyLightModelLayers;
import de.lucalabs.fairylights.renderer.block.entity.*;
import de.lucalabs.fairylights.renderer.entity.FenceFastenerRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

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
        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.GARLAND_RINGS, GarlandVineRenderer.RingsModel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.TINSEL_STRIP, GarlandTinselRenderer.StripModel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.FAIRY_LIGHT, FairyLightModel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.PAPER_LANTERN, PaperLanternModel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.ORB_LANTERN, OrbLanternModel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.FLOWER_LIGHT, FlowerLightModel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.CANDLE_LANTERN_LIGHT, ColorCandleLanternModel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.JACK_O_LANTERN, JackOLanternLightModel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.SKULL_LIGHT, SkullLightModel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.GHOST_LIGHT, GhostLightModel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.SPIDER_LIGHT, SpiderLightModel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.WITCH_LIGHT, WitchLightModel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.SNOWFLAKE_LIGHT, SnowflakeLightModel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.HEART_LIGHT, HeartLightModel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.MOON_LIGHT, MoonLightModel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.STAR_LIGHT, StarLightModel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.ICICLE_LIGHTS_1, () -> IcicleLightsModel.createLayer(1));
        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.ICICLE_LIGHTS_2, () -> IcicleLightsModel.createLayer(2));
        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.ICICLE_LIGHTS_3, () -> IcicleLightsModel.createLayer(3));
        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.ICICLE_LIGHTS_4, () -> IcicleLightsModel.createLayer(4));
        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.METEOR_LIGHT, MeteorLightModel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.CANDLE_LANTERN, CandleLanternModel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.OIL_LANTERN, OilLanternModel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.INCANDESCENT_LIGHT, IncandescentLightModel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.TINSEL_WIRE, GarlandTinselRenderer::wireLayer);
        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.VINE_WIRE, GarlandVineRenderer::wireLayer);
        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.PENNANT_WIRE, PennantBuntingRenderer::wireLayer);
        EntityModelLayerRegistry.registerModelLayer(FairyLightModelLayers.LIGHTS_WIRE, HangingLightsRenderer::wireLayer);

        ModelLoadingPlugin.register(context -> {
            context.addModels(FenceFastenerRenderer.MODEL);
            context.addModels(PennantBuntingRenderer.MODELS);
        });

        ItemColorManager.setupColors();
    }

}