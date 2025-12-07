package de.lucalabs.fairylights.entity;

import de.lucalabs.fairylights.FairyLights;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public final class FairyLightEntities {

    public static final EntityType<FenceFastenerEntity> FASTENER = register("fastener", () ->
            EntityType.Builder.<FenceFastenerEntity>create(FenceFastenerEntity::new, SpawnGroup.MISC)
                    .dimensions(1.15F, 2.8F)
                    .eyeHeight(1)
                    .maxTrackingRange(10)
                    .trackingTickInterval(Integer.MAX_VALUE)
                    .build(FairyLights.ID + ":fastener")
    );

    private FairyLightEntities() {
    }

    private static <T extends Entity> EntityType<T> register(final String name, Supplier<EntityType<T>> supplier) {
        Identifier identifier = Identifier.of(FairyLights.ID, name);
        return Registry.register(Registries.ENTITY_TYPE, identifier, supplier.get());
    }

    public static void initialize() {
        FairyLights.LOGGER.info("Registering entities");
    }
}
