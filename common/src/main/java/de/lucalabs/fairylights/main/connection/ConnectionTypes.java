package de.lucalabs.fairylights.main.connection;

import de.lucalabs.fairylights.Common;
import java.util.function.Supplier;

import de.lucalabs.fairylights.Constants;
import de.lucalabs.fairylights.main.items.FairyLightItems;
import de.lucalabs.fairylights.main.registries.FairyLightRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public final class ConnectionTypes {

    public static final ConnectionType<HangingLightsConnection> HANGING_LIGHTS = register("hanging_lights",
            () -> ConnectionType.Builder.create(HangingLightsConnection::new).item(() -> FairyLightItems.HANGING_LIGHTS).build()
    );

    public static final ConnectionType<PennantBuntingConnection> PENNANT_BUNTING = register("pennant_bunting",
            () -> ConnectionType.Builder.create(PennantBuntingConnection::new).item(() -> FairyLightItems.PENNANT_BUNTING).build()
    );

    public static final ConnectionType<GarlandTinselConnection> TINSEL_GARLAND = register("tinsel_garland",
            () -> ConnectionType.Builder.create(GarlandTinselConnection::new).item(() -> FairyLightItems.TINSEL).build()
    );

    public static final ConnectionType<GarlandVineConnection> VINE_GARLAND = register("vine_garland",
            () -> ConnectionType.Builder.create(GarlandVineConnection::new).item(() -> FairyLightItems.GARLAND).build()
    );

    private ConnectionTypes() {
    }

    private static <T extends ConnectionType<?>> T register(final String name, Supplier<T> supplier) {
        ResourceLocation identifier = Common.id(name);
        return Registry.register(FairyLightRegistries.CONNECTION_TYPES, identifier, supplier.get());
    }

    public static void initialize() {
        Constants.LOG.info("Initializing connection types");
    }
}
