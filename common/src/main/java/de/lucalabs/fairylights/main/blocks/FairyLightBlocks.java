package de.lucalabs.fairylights.main.blocks;

import de.lucalabs.fairylights.Common;
import de.lucalabs.fairylights.Constants;
import de.lucalabs.fairylights.main.items.LightVariant;
import de.lucalabs.fairylights.main.items.SimpleLightVariant;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public final class FairyLightBlocks {

    public static final FastenerBlock FASTENER = register("fastener", () ->
            new FastenerBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .forceSolidOn()
                            .strength(3.5F)
                            .sound(SoundType.LANTERN)));

    public static final LightBlock FAIRY_LIGHT = register("fairy_light", createLight(SimpleLightVariant.FAIRY_LIGHT));
    public static final LightBlock PAPER_LANTERN = register("paper_lantern", createLight(SimpleLightVariant.PAPER_LANTERN));
    public static final LightBlock ORB_LANTERN = register("orb_lantern", createLight(SimpleLightVariant.ORB_LANTERN));
    public static final LightBlock FLOWER_LIGHT = register("flower_light", createLight(SimpleLightVariant.FLOWER_LIGHT));
    public static final LightBlock CANDLE_LANTERN_LIGHT = register("candle_lantern_light", createLight(SimpleLightVariant.CANDLE_LANTERN_LIGHT));
    public static final LightBlock JACK_O_LANTERN = register("jack_o_lantern", createLight(SimpleLightVariant.JACK_O_LANTERN));
    public static final LightBlock SKULL_LIGHT = register("skull_light", createLight(SimpleLightVariant.SKULL_LIGHT));
    public static final LightBlock GHOST_LIGHT = register("ghost_light", createLight(SimpleLightVariant.GHOST_LIGHT));
    public static final LightBlock SPIDER_LIGHT = register("spider_light", createLight(SimpleLightVariant.SPIDER_LIGHT));
    public static final LightBlock WITCH_LIGHT = register("witch_light", createLight(SimpleLightVariant.WITCH_LIGHT));
    public static final LightBlock SNOWFLAKE_LIGHT = register("snowflake_light", createLight(SimpleLightVariant.SNOWFLAKE_LIGHT));
    public static final LightBlock HEART_LIGHT = register("heart_light", createLight(SimpleLightVariant.HEART_LIGHT));
    public static final LightBlock MOON_LIGHT = register("moon_light", createLight(SimpleLightVariant.MOON_LIGHT));
    public static final LightBlock STAR_LIGHT = register("star_light", createLight(SimpleLightVariant.STAR_LIGHT));
    public static final LightBlock ICICLE_LIGHTS = register("icicle_lights", createLight(SimpleLightVariant.ICICLE_LIGHTS));
    public static final LightBlock METEOR_LIGHT = register("meteor_light", createLight(SimpleLightVariant.METEOR_LIGHT));
    public static final LightBlock CANDLE_LANTERN = register("candle_lantern", createLight(SimpleLightVariant.CANDLE_LANTERN));
    public static final LightBlock OIL_LANTERN = register("oil_lantern", createLight(SimpleLightVariant.OIL_LANTERN));
    public static final LightBlock INCANDESCENT_LIGHT = register("incandescent_light", createLight(SimpleLightVariant.INCANDESCENT_LIGHT));

    private FairyLightBlocks() {}

    private static Supplier<LightBlock> createLight(final LightVariant<?> variant, final BiFunction<BlockBehaviour.Properties, LightVariant<?>, LightBlock> factory) {
        return () -> factory.apply(
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.METAL)
                        .forceSolidOn()
                        .strength(3.5F)
                        .sound(SoundType.LANTERN)
                        .lightLevel(state -> state.getValue(LightBlock.LIT) ? 15 : 0).noCollission(), variant);
    }

    private static Supplier<LightBlock> createLight(final LightVariant<?> variant) {
        return createLight(variant, LightBlock::new);
    }

    private static <T extends Block> T register(final String name, Supplier<T> supplier) {
        ResourceLocation identifier = Common.id(name);
        return Registry.register(BuiltInRegistries.BLOCK, identifier, supplier.get());
    }

    public static void initialize() {
        Constants.LOG.info("Registering blocks");
    }
}
