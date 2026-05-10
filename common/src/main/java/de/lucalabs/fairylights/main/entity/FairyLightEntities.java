package de.lucalabs.fairylights.main.entity;

import de.lucalabs.fairylights.Common;
import java.util.function.Supplier;

import de.lucalabs.fairylights.Constants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class FairyLightEntities {

    public static final EntityType<FenceFastenerEntity> FASTENER = register("fastener", () ->
            EntityType.Builder.<FenceFastenerEntity>of(FenceFastenerEntity::new, MobCategory.MISC)
                    .sized(1.15F, 2.8F)
                    .eyeHeight(1)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build(Constants.MOD_ID + ":fastener")
    );

    private FairyLightEntities() {
    }

    private static <T extends Entity> EntityType<T> register(final String name, Supplier<EntityType<T>> supplier) {
        ResourceLocation identifier = Common.id(name);
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, identifier, supplier.get());
    }

    public static void initialize() {
        Constants.LOG.info("Registering entities");
    }
}
