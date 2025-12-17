package de.lucalabs.fairylights.blocks.entity;

import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.blocks.FairyLightBlocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public final class FairyLightBlockEntities {

    public static final BlockEntityType<FastenerBlockEntity> FASTENER = register(
            "fastener",
            () -> BlockEntityType.Builder.create(FastenerBlockEntity::new, FairyLightBlocks.FASTENER).build(null));

    public static final BlockEntityType<LightBlockEntity> LIGHT = register("light", () -> BlockEntityType.Builder.create(LightBlockEntity::new,
            FairyLightBlocks.FAIRY_LIGHT,
            FairyLightBlocks.PAPER_LANTERN,
            FairyLightBlocks.ORB_LANTERN,
            FairyLightBlocks.FLOWER_LIGHT,
            FairyLightBlocks.CANDLE_LANTERN_LIGHT,
            FairyLightBlocks.JACK_O_LANTERN,
            FairyLightBlocks.SKULL_LIGHT,
            FairyLightBlocks.GHOST_LIGHT,
            FairyLightBlocks.SPIDER_LIGHT,
            FairyLightBlocks.WITCH_LIGHT,
            FairyLightBlocks.SNOWFLAKE_LIGHT,
            FairyLightBlocks.HEART_LIGHT,
            FairyLightBlocks.MOON_LIGHT,
            FairyLightBlocks.STAR_LIGHT,
            FairyLightBlocks.ICICLE_LIGHTS,
            FairyLightBlocks.METEOR_LIGHT,
            FairyLightBlocks.OIL_LANTERN,
            FairyLightBlocks.CANDLE_LANTERN,
            FairyLightBlocks.INCANDESCENT_LIGHT).build(null));

    private FairyLightBlockEntities() {
    }

    private static <T extends BlockEntity> BlockEntityType<T> register(final String name, Supplier<BlockEntityType<T>> supplier) {
        Identifier identifier = Identifier.of(FairyLights.ID, name);
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, identifier, supplier.get());
    }

    public static void initialize() {
        FairyLights.LOGGER.info("Registering block entities");
    }
}
