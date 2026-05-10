package de.lucalabs.fairylights.main.blocks.entity;

import de.lucalabs.fairylights.Common;
import de.lucalabs.fairylights.Constants;
import de.lucalabs.fairylights.main.blocks.FairyLightBlocks;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class FairyLightBlockEntities {

    public static final BlockEntityType<FastenerBlockEntity> FASTENER = register(
            "fastener",
            () -> BlockEntityType.Builder.of(FastenerBlockEntity::new, FairyLightBlocks.FASTENER).build(null));

    public static final BlockEntityType<LightBlockEntity> LIGHT = register("light", () -> BlockEntityType.Builder.of(LightBlockEntity::new,
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
        ResourceLocation identifier = Common.id(name);
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, identifier, supplier.get());
    }

    public static void initialize() {
        Constants.LOG.info("Registering block entities");
    }
}
