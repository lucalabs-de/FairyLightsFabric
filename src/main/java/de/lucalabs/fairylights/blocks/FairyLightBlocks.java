package de.lucalabs.fairylights.blocks;

import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.items.LightVariant;
import de.lucalabs.fairylights.items.SimpleLightVariant;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.block.MapColor;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public final class FairyLightBlocks {

    public static final FastenerBlock FASTENER = register("fastener", () ->
            new FastenerBlock(
                    AbstractBlock.Settings.create()
                            .mapColor(MapColor.IRON_GRAY)
                            .solid()
                            .strength(3.5F)
                            .sounds(BlockSoundGroup.LANTERN)));

    public static final LightBlock FAIRY_LIGHT = register("fairy_light", createLight(SimpleLightVariant.FAIRY_LIGHT));
    public static final LightBlock PAPER_LANTERN = register("paper_lantern", createLight(SimpleLightVariant.PAPER_LANTERN));
    public static final LightBlock INCANDESCENT_LIGHT = register("incandescent_light", createLight(SimpleLightVariant.INCANDESCENT_LIGHT));

    private FairyLightBlocks() {}

    private static Supplier<LightBlock> createLight(final LightVariant<?> variant, final BiFunction<AbstractBlock.Settings, LightVariant<?>, LightBlock> factory) {
        return () -> factory.apply(
                AbstractBlock.Settings.create()
                        .mapColor(MapColor.IRON_GRAY)
                        .solid()
                        .strength(3.5F)
                        .sounds(BlockSoundGroup.LANTERN)
                        .luminance(state -> state.get(LightBlock.LIT) ? 15 : 0).noCollision(), variant);
    }

    private static Supplier<LightBlock> createLight(final LightVariant<?> variant) {
        return createLight(variant, LightBlock::new);
    }

    private static <T extends Block> T register(final String name, Supplier<T> supplier) {
        Identifier identifier = Identifier.of(FairyLights.ID, name);
        return Registry.register(Registries.BLOCK, identifier, supplier.get());
    }

    public static void initialize() {
        FairyLights.LOGGER.info("Registering blocks");
    }
}
