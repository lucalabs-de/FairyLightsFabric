package de.lucalabs.fairylights.main.registries;

import de.lucalabs.fairylights.Common;
import de.lucalabs.fairylights.Constants;
import de.lucalabs.fairylights.main.connection.ConnectionType;
import de.lucalabs.fairylights.main.items.LightVariant;
import de.lucalabs.fairylights.vendor.fabricapi.registries.RegistryBuilder;
import de.lucalabs.fairylights.main.string.StringType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public final class FairyLightRegistries {

    private static final ResourceLocation CONNECTION_TYPE_ID = Common.id("connection_type");
    private static final ResourceLocation STRING_TYPE_ID = Common.id("string_type");
    private static final ResourceLocation LIGHT_VARIANT_ID = Common.id("variant");

    public static final ResourceKey<Registry<ConnectionType<?>>> CONNECTION_TYPE_KEY
            = ResourceKey.createRegistryKey(CONNECTION_TYPE_ID);
    public static final Registry<ConnectionType<?>> CONNECTION_TYPES
            = RegistryBuilder.createSimple(CONNECTION_TYPE_KEY).buildAndRegister();

    public static final ResourceKey<Registry<StringType>> STRING_TYPE_KEY
            = ResourceKey.createRegistryKey(STRING_TYPE_ID);
    public static final Registry<StringType> STRING_TYPES
            = RegistryBuilder.createSimple(STRING_TYPE_KEY).buildAndRegister();

    public static final ResourceKey<Registry<LightVariant<?>>> LIGHT_VARIANT_KEY
            = ResourceKey.createRegistryKey(LIGHT_VARIANT_ID);
    public static final Registry<LightVariant<?>> LIGHT_VARIANTS
            = RegistryBuilder.createSimple(LIGHT_VARIANT_KEY).buildAndRegister();

    private FairyLightRegistries() {}

    public static void initialize() {
       Constants.LOG.info("Initializing custom registries");
    }
}