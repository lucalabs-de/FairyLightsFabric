package de.lucalabs.fairylights.registries;

import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.connection.ConnectionType;
import de.lucalabs.fairylights.string.StringType;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public final class FairyLightRegistries {

    private static final Identifier CONNECTION_TYPE_ID = Identifier.of(FairyLights.ID, "connection_type");
    private static final Identifier STRING_TYPE_ID = Identifier.of(FairyLights.ID, "string_type");

    public static final RegistryKey<Registry<ConnectionType<?>>> CONNECTION_TYPE_KEY
            = RegistryKey.ofRegistry(CONNECTION_TYPE_ID);
    public static final Registry<ConnectionType<?>> CONNECTION_TYPES
            = FabricRegistryBuilder.createSimple(CONNECTION_TYPE_KEY).buildAndRegister();

    public static final RegistryKey<Registry<StringType>> STRING_TYPE_KEY
            = RegistryKey.ofRegistry(STRING_TYPE_ID);
    public static final Registry<StringType> STRING_TYPES
            = FabricRegistryBuilder.createSimple(STRING_TYPE_KEY).buildAndRegister();

    private FairyLightRegistries() {}

    public static void initialize() {
       FairyLights.LOGGER.info("Initializing custom registries");
    }
}
