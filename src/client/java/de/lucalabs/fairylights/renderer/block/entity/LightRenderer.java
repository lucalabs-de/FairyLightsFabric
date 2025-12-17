package de.lucalabs.fairylights.renderer.block.entity;

import com.google.common.collect.ImmutableMap;
import de.lucalabs.fairylights.feature.light.Light;
import de.lucalabs.fairylights.feature.light.LightBehavior;
import de.lucalabs.fairylights.items.LightVariant;
import de.lucalabs.fairylights.items.SimpleLightVariant;
import de.lucalabs.fairylights.model.light.*;
import de.lucalabs.fairylights.renderer.FairyLightModelLayers;
import de.lucalabs.fairylights.renderer.RenderConstants;
import de.lucalabs.fairylights.util.ColorUtils;
import de.lucalabs.fairylights.util.MathHelper;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class LightRenderer {
    private final LightModelProvider<LightBehavior> defaultLight = LightModelProvider.of(new DefaultModel());
    private final Map<LightVariant<?>, LightModelProvider<?>> lights;

    public LightRenderer(final Function<EntityModelLayer, ModelPart> baker) {
        lights = new ImmutableMap.Builder<LightVariant<?>, LightModelProvider<?>>()
                .put(
                        SimpleLightVariant.FAIRY_LIGHT,
                        LightModelProvider.of(new FairyLightModel(baker.apply(FairyLightModelLayers.FAIRY_LIGHT))))
                .put(
                        SimpleLightVariant.INCANDESCENT_LIGHT,
                        LightModelProvider.of(new IncandescentLightModel(baker.apply(FairyLightModelLayers.INCANDESCENT_LIGHT))))
                .put(
                        SimpleLightVariant.PAPER_LANTERN,
                        LightModelProvider.of(new PaperLanternModel(baker.apply(FairyLightModelLayers.PAPER_LANTERN))))
                .put(
                        SimpleLightVariant.ORB_LANTERN,
                        LightModelProvider.of(new OrbLanternModel(baker.apply(FairyLightModelLayers.ORB_LANTERN))))
                .put(
                        SimpleLightVariant.FLOWER_LIGHT,
                        LightModelProvider.of(new FlowerLightModel(baker.apply(FairyLightModelLayers.FLOWER_LIGHT))))
                .put(
                        SimpleLightVariant.CANDLE_LANTERN_LIGHT,
                        LightModelProvider.of(new ColorCandleLanternModel(baker.apply(FairyLightModelLayers.CANDLE_LANTERN_LIGHT))))
                .put(
                        SimpleLightVariant.JACK_O_LANTERN,
                        LightModelProvider.of(new JackOLanternLightModel(baker.apply(FairyLightModelLayers.JACK_O_LANTERN))))
                .put(
                        SimpleLightVariant.SKULL_LIGHT,
                        LightModelProvider.of(new SkullLightModel(baker.apply(FairyLightModelLayers.SKULL_LIGHT))))
                .put(
                        SimpleLightVariant.GHOST_LIGHT,
                        LightModelProvider.of(new GhostLightModel(baker.apply(FairyLightModelLayers.GHOST_LIGHT))))
                .put(
                        SimpleLightVariant.SPIDER_LIGHT,
                        LightModelProvider.of(new SpiderLightModel(baker.apply(FairyLightModelLayers.SPIDER_LIGHT))))
                .put(
                        SimpleLightVariant.WITCH_LIGHT,
                        LightModelProvider.of(new WitchLightModel(baker.apply(FairyLightModelLayers.WITCH_LIGHT))))
                .put(
                        SimpleLightVariant.SNOWFLAKE_LIGHT,
                        LightModelProvider.of(new SnowflakeLightModel(baker.apply(FairyLightModelLayers.SNOWFLAKE_LIGHT))))
                .put(
                        SimpleLightVariant.HEART_LIGHT,
                        LightModelProvider.of(new HeartLightModel(baker.apply(FairyLightModelLayers.HEART_LIGHT))))
                .put(
                        SimpleLightVariant.MOON_LIGHT,
                        LightModelProvider.of(new MoonLightModel(baker.apply(FairyLightModelLayers.MOON_LIGHT))))
                .put(
                        SimpleLightVariant.STAR_LIGHT,
                        LightModelProvider.of(new StarLightModel(baker.apply(FairyLightModelLayers.STAR_LIGHT))))
                .put(
                        SimpleLightVariant.ICICLE_LIGHTS,
                        LightModelProvider.of(
                                new IcicleLightsModel[]{
                                        new IcicleLightsModel(baker.apply(FairyLightModelLayers.ICICLE_LIGHTS_1), 1),
                                        new IcicleLightsModel(baker.apply(FairyLightModelLayers.ICICLE_LIGHTS_2), 2),
                                        new IcicleLightsModel(baker.apply(FairyLightModelLayers.ICICLE_LIGHTS_3), 3),
                                        new IcicleLightsModel(baker.apply(FairyLightModelLayers.ICICLE_LIGHTS_4), 4)
                                },
                                (models, i) -> models[i < 0 ? 3 : MathHelper.mod(MathHelper.hash(i), 4)]
                        ))
                .put(
                        SimpleLightVariant.METEOR_LIGHT,
                        LightModelProvider.of(new MeteorLightModel(baker.apply(FairyLightModelLayers.METEOR_LIGHT))))
                .put(
                        SimpleLightVariant.OIL_LANTERN,
                        LightModelProvider.of(new OilLanternModel(baker.apply(FairyLightModelLayers.OIL_LANTERN)))
                )
                .put(
                        SimpleLightVariant.CANDLE_LANTERN,
                        LightModelProvider.of(new CandleLanternModel(baker.apply(FairyLightModelLayers.CANDLE_LANTERN))))
                .build();
    }

    public Data start(final VertexConsumerProvider source) {
        // TODO check if unsorted translucency is really needed here
//        final VertexConsumer buf = RenderConstants.TRANSLUCENT_TEXTURE.getVertexConsumer(source, ForgeRenderTypes::getUnsortedTranslucent);
        final VertexConsumer buf = RenderConstants.TRANSLUCENT_TEXTURE
                .getVertexConsumer(source, RenderLayer::getEntityTranslucent);

        ForwardingVertexConsumer translucent = new ForwardingVertexConsumer() {
            @Override
            protected VertexConsumer delegate() {
                return buf;
            }

            @Override
            public VertexConsumer normal(float x, float y, float z) {
                return super.normal(0.0F, 1.0F, 0.0F);
            }
        };

        return new Data(buf, translucent);
    }

    public <T extends LightBehavior> LightModel<T> getModel(final Light<?> light, final int index) {
        return this.getModel(light.getVariant(), index);
    }

    @SuppressWarnings("unchecked")
    public <T extends LightBehavior> LightModel<T> getModel(final LightVariant<?> variant, final int index) {
        return (LightModel<T>) this.lights.getOrDefault(variant, this.defaultLight).get(index);
    }

    public void render(final MatrixStack matrix, final Data data, final Light<?> light, final int index, final float delta, final int packedLight, final int packedOverlay) {
        this.render(matrix, data, light, this.getModel(light, index), delta, packedLight, packedOverlay);
    }

    public <T extends LightBehavior> void render(final MatrixStack matrix, final Data data, final Light<T> light, final LightModel<T> model, final float delta, final int packedLight, final int packedOverlay) {
        model.animate(light, light.getBehavior(), delta);
        model.render(matrix, data.solid, packedLight, packedOverlay, ColorUtils.WHITE);
        model.renderTranslucent(matrix, data.translucent, packedLight, packedOverlay, ColorUtils.WHITE);
    }

    interface LightModelProvider<T extends LightBehavior> {
        static <T extends LightBehavior> LightModelProvider<T> of(final LightModel<T> model) {
            return i -> model;
        }

        static <T extends LightBehavior> LightModelProvider<T> of(final Supplier<LightModel<T>> model) {
            return i -> model.get();
        }

        static <T extends LightBehavior, D> LightModelProvider<T> of(final D data, final BiFunction<? super D, Integer, LightModel<T>> function) {
            return i -> function.apply(data, i);
        }

        LightModel<T> get(final int index);
    }

    static class DefaultModel extends LightModel<LightBehavior> {
        private static final ModelPart EMPTY = new ModelPart(List.of(), Map.of());

        public DefaultModel() {
            super(new ModelPart(List.of(), Map.of(
                    "lit", EMPTY,
                    "lit_tint", EMPTY,
                    "lit_tint_glow", EMPTY,
                    "unlit", EMPTY
            )));
        }

        @Override
        public void render(
                final MatrixStack matrix,
                final VertexConsumer builder,
                final int light,
                final int overlay,
                final int color) {
        }
    }

    public record Data(VertexConsumer solid, VertexConsumer translucent) {
    }

}
