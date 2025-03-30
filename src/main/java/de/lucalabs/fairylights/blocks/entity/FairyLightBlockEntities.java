package de.lucalabs.fairylights.blocks.entity;

import de.lucalabs.fairylights.FairyLights;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public final class FairyLightBlockEntities {

    public static final BlockEntityType<FastenerBlockEntity> FASTENER = register(
            "fastener",
            () -> BlockEntityType.Builder.create(FastenerBlockEntity::new, FLBlocks.FASTENER.get()).build(null));

    public static final BlockEntityType<LightBlockEntity> LIGHT = register("light", () -> BlockEntityType.Builder.create(LightBlockEntity::new,
            FLBlocks.FAIRY_LIGHT.get(),
            FLBlocks.PAPER_LANTERN.get(),
            FLBlocks.INCANDESCENT_LIGHT.get()).build(null));

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
