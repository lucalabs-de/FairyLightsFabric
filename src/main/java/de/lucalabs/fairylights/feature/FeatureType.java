package de.lucalabs.fairylights.feature;

import com.mojang.serialization.Lifecycle;
import de.lucalabs.fairylights.FairyLights;
import net.minecraft.core.DefaultedMappedRegistry;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public final class FeatureType {
    private static final DefaultedRegistry<FeatureType> REGISTRY = new DefaultedMappedRegistry<>(
            "default",
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(FairyLights.ID, "feature")),
            Lifecycle.experimental(),
            false
    );

    public static final FeatureType DEFAULT = register("default");

    private FeatureType() {}

    public int getId() {
        return REGISTRY.getId(this);
    }

    public static FeatureType register(final String name) {
        return Registry.register(REGISTRY, ResourceLocation.parse(name), new FeatureType());
    }

    public static FeatureType fromId(final int id) {
        return REGISTRY.byId(id);
    }
}
