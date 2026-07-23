package de.lucalabs.fairylights.client;

import de.lucalabs.fairylights.Constants;
import de.lucalabs.fairylights.client.items.ItemColorManager;
import de.lucalabs.fairylights.client.model.light.*;
import de.lucalabs.fairylights.client.renderer.FairyLightModelLayers;
import de.lucalabs.fairylights.client.renderer.block.entity.NeoForgeFastenerBlockEntityRenderer;
import de.lucalabs.fairylights.client.renderer.block.entity.GarlandTinselRenderer;
import de.lucalabs.fairylights.client.renderer.block.entity.GarlandVineRenderer;
import de.lucalabs.fairylights.client.renderer.block.entity.HangingLightsRenderer;
import de.lucalabs.fairylights.client.renderer.block.entity.LightBlockEntityRenderer;
import de.lucalabs.fairylights.client.renderer.block.entity.PennantBuntingRenderer;
import de.lucalabs.fairylights.client.renderer.entity.FenceFastenerRenderer;
import de.lucalabs.fairylights.main.blocks.entity.FairyLightBlockEntities;
import de.lucalabs.fairylights.main.entity.FairyLightEntities;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

/**
 * Client-side registration, mirroring the Fabric {@code FairyLightsClient}. Only loaded on the
 * client ({@link Dist#CLIENT}); all rendering logic itself lives in the common module.
 */
@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public final class FairyLightsClient {

    private FairyLightsClient() {
    }

    @SubscribeEvent
    public static void onRegisterRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(FairyLightBlockEntities.FASTENER, NeoForgeFastenerBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(FairyLightBlockEntities.LIGHT, LightBlockEntityRenderer::new);
        event.registerEntityRenderer(FairyLightEntities.FASTENER, FenceFastenerRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(FairyLightModelLayers.BOW, BowModel::createLayer);
        event.registerLayerDefinition(FairyLightModelLayers.GARLAND_RINGS, GarlandVineRenderer.RingsModel::createLayer);
        event.registerLayerDefinition(FairyLightModelLayers.TINSEL_STRIP, GarlandTinselRenderer.StripModel::createLayer);
        event.registerLayerDefinition(FairyLightModelLayers.FAIRY_LIGHT, FairyLightModel::createLayer);
        event.registerLayerDefinition(FairyLightModelLayers.PAPER_LANTERN, PaperLanternModel::createLayer);
        event.registerLayerDefinition(FairyLightModelLayers.ORB_LANTERN, OrbLanternModel::createLayer);
        event.registerLayerDefinition(FairyLightModelLayers.FLOWER_LIGHT, FlowerLightModel::createLayer);
        event.registerLayerDefinition(FairyLightModelLayers.CANDLE_LANTERN_LIGHT, ColorCandleLanternModel::createLayer);
        event.registerLayerDefinition(FairyLightModelLayers.JACK_O_LANTERN, JackOLanternLightModel::createLayer);
        event.registerLayerDefinition(FairyLightModelLayers.SKULL_LIGHT, SkullLightModel::createLayer);
        event.registerLayerDefinition(FairyLightModelLayers.GHOST_LIGHT, GhostLightModel::createLayer);
        event.registerLayerDefinition(FairyLightModelLayers.SPIDER_LIGHT, SpiderLightModel::createLayer);
        event.registerLayerDefinition(FairyLightModelLayers.WITCH_LIGHT, WitchLightModel::createLayer);
        event.registerLayerDefinition(FairyLightModelLayers.SNOWFLAKE_LIGHT, SnowflakeLightModel::createLayer);
        event.registerLayerDefinition(FairyLightModelLayers.HEART_LIGHT, HeartLightModel::createLayer);
        event.registerLayerDefinition(FairyLightModelLayers.MOON_LIGHT, MoonLightModel::createLayer);
        event.registerLayerDefinition(FairyLightModelLayers.STAR_LIGHT, StarLightModel::createLayer);
        event.registerLayerDefinition(FairyLightModelLayers.ICICLE_LIGHTS_1, () -> IcicleLightsModel.createLayer(1));
        event.registerLayerDefinition(FairyLightModelLayers.ICICLE_LIGHTS_2, () -> IcicleLightsModel.createLayer(2));
        event.registerLayerDefinition(FairyLightModelLayers.ICICLE_LIGHTS_3, () -> IcicleLightsModel.createLayer(3));
        event.registerLayerDefinition(FairyLightModelLayers.ICICLE_LIGHTS_4, () -> IcicleLightsModel.createLayer(4));
        event.registerLayerDefinition(FairyLightModelLayers.METEOR_LIGHT, MeteorLightModel::createLayer);
        event.registerLayerDefinition(FairyLightModelLayers.CANDLE_LANTERN, CandleLanternModel::createLayer);
        event.registerLayerDefinition(FairyLightModelLayers.OIL_LANTERN, OilLanternModel::createLayer);
        event.registerLayerDefinition(FairyLightModelLayers.INCANDESCENT_LIGHT, IncandescentLightModel::createLayer);
        event.registerLayerDefinition(FairyLightModelLayers.TINSEL_WIRE, GarlandTinselRenderer::wireLayer);
        event.registerLayerDefinition(FairyLightModelLayers.VINE_WIRE, GarlandVineRenderer::wireLayer);
        event.registerLayerDefinition(FairyLightModelLayers.PENNANT_WIRE, PennantBuntingRenderer::wireLayer);
        event.registerLayerDefinition(FairyLightModelLayers.LIGHTS_WIRE, HangingLightsRenderer::wireLayer);
    }

    @SubscribeEvent
    public static void onRegisterAdditionalModels(final ModelEvent.RegisterAdditional event) {
        event.register(ModelResourceLocation.standalone(FenceFastenerRenderer.MODEL));
        for (final ResourceLocation model : PennantBuntingRenderer.MODELS) {
            event.register(ModelResourceLocation.standalone(model));
        }
    }

    @SubscribeEvent
    public static void onRegisterItemColors(final RegisterColorHandlersEvent.Item event) {
        ItemColorManager.setupColors(event);
    }
}
