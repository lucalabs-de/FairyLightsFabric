package de.lucalabs.fairylights.string;

import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.registries.FairyLightRegistries;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public class StringTypes {

    public static final StringType BLACK_STRING = register("black_string", () -> new StringType(0xFF323232));
    public static final StringType WHITE_STRING = register("white_string", () -> new StringType(0xFFF0F0F0));

    private static StringType register(final String name, Supplier<? extends StringType> supplier) {
        ResourceLocation identifier = ResourceLocation.fromNamespaceAndPath(FairyLights.ID, name);
        return Registry.register(FairyLightRegistries.STRING_TYPES, identifier, supplier.get());
    }

    public static void initialize() {
        FairyLights.LOGGER.info("Initializing string types");
    }
}
