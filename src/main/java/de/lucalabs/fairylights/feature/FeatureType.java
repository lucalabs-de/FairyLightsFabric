package de.lucalabs.fairylights.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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

    public static Codec<FeatureType> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("id").forGetter(FeatureType::getId)
            ).apply(instance, FeatureType::fromId));

    private FeatureType() {
    }

    public static FeatureType register(final String name) {
        return Registry.register(REGISTRY, Identifier.of(name), new FeatureType());
    }

    public static FeatureType fromId(final int id) {
        return REGISTRY.get(id);
    }

    public int getId() {
        return REGISTRY.getRawId(this);
    }
}
