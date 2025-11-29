package de.lucalabs.fairylights.feature;

import com.mojang.serialization.Lifecycle;
import de.lucalabs.fairylights.FairyLights;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleDefaultedRegistry;
import net.minecraft.util.Identifier;

public final class FeatureType {
    private static final DefaultedRegistry<FeatureType> REGISTRY = new SimpleDefaultedRegistry<>(
            "default",
            RegistryKey.ofRegistry(Identifier.of(FairyLights.ID, "feature")),
            Lifecycle.experimental(),
            false
    );

    public static final FeatureType DEFAULT = register("default");

    private FeatureType() {}

    public int getId() {
        return REGISTRY.getRawId(this);
    }

    public static FeatureType register(final String name) {
        return Registry.register(REGISTRY, Identifier.of(name), new FeatureType());
    }

    public static FeatureType fromId(final int id) {
        return REGISTRY.get(id);
    }
}
