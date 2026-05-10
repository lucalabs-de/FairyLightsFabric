package de.lucalabs.fairylights.registries;

import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.connection.ConnectionType;
import de.lucalabs.fairylights.items.LightVariant;
import de.lucalabs.fairylights.string.StringType;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public final class FairyLightRegistries {

    private static final ResourceLocation CONNECTION_TYPE_ID = ResourceLocation.fromNamespaceAndPath(FairyLights.ID, "connection_type");
    private static final ResourceLocation STRING_TYPE_ID = ResourceLocation.fromNamespaceAndPath(FairyLights.ID, "string_type");
    private static final ResourceLocation LIGHT_VARIANT_ID = ResourceLocation.fromNamespaceAndPath(FairyLights.ID, "variant");

    public static final ResourceKey<Registry<ConnectionType<?>>> CONNECTION_TYPE_KEY
            = ResourceKey.createRegistryKey(CONNECTION_TYPE_ID);
    public static final Registry<ConnectionType<?>> CONNECTION_TYPES
            = FabricRegistryBuilder.createSimple(CONNECTION_TYPE_KEY).buildAndRegister();

    public static final ResourceKey<Registry<StringType>> STRING_TYPE_KEY
            = ResourceKey.createRegistryKey(STRING_TYPE_ID);
    public static final Registry<StringType> STRING_TYPES
            = FabricRegistryBuilder.createSimple(STRING_TYPE_KEY).buildAndRegister();

    public static final ResourceKey<Registry<LightVariant<?>>> LIGHT_VARIANT_KEY
            = ResourceKey.createRegistryKey(LIGHT_VARIANT_ID);
    public static final Registry<LightVariant<?>> LIGHT_VARIANTS
            = FabricRegistryBuilder.createSimple(LIGHT_VARIANT_KEY).buildAndRegister();

    private FairyLightRegistries() {}

    public static void initialize() {
       FairyLights.LOGGER.info("Initializing custom registries");
    }
}
