package de.lucalabs.fairylights.connection;

import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.items.FairyLightItems;
import de.lucalabs.fairylights.registries.FairyLightRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public final class ConnectionTypes {

    public static final ConnectionType<HangingLightsConnection> HANGING_LIGHTS = register("hanging_lights",
            () -> ConnectionType.Builder.create(HangingLightsConnection::new).item(() -> FairyLightItems.HANGING_LIGHTS).build()
    );

    public static final ConnectionType<PennantBuntingConnection> PENNANT_BUNTING = register("pennant_bunting",
            () -> ConnectionType.Builder.create(PennantBuntingConnection::new).item(() -> FairyLightItems.PENNANT_BUNTING).build()
    );

    private ConnectionTypes() {
    }

    private static <T extends ConnectionType<?>> T register(final String name, Supplier<T> supplier) {
        Identifier identifier = Identifier.of(FairyLights.ID, name);
        return Registry.register(FairyLightRegistries.CONNECTION_TYPES, identifier, supplier.get());
    }

    public static void initialize() {
        FairyLights.LOGGER.info("Initializing connection types");
    }
}
