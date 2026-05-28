package de.lucalabs.fairylights.vendor.fabricapi.registries;

import com.mojang.serialization.Lifecycle;
import de.lucalabs.fairylights.mixin.RegistriesAccessor;
import net.minecraft.core.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.EnumSet;

public final class RegistryBuilder<T, R extends WritableRegistry<T>> {
    private final R registry;
    private final EnumSet<RegistryAttribute> attributes = EnumSet.noneOf(RegistryAttribute.class);

    public static <T, R extends WritableRegistry<T>> RegistryBuilder<T, R> from(R registry) {
        return new RegistryBuilder<>(registry);
    }

    public static <T> RegistryBuilder<T, MappedRegistry<T>> createSimple(ResourceKey<Registry<T>> registryKey) {
        return from(new MappedRegistry<>(registryKey, Lifecycle.stable(), false));
    }

    public static <T> RegistryBuilder<T, DefaultedMappedRegistry<T>> createDefaulted(ResourceKey<Registry<T>> registryKey, ResourceLocation defaultId) {
        return from(new DefaultedMappedRegistry<>(defaultId.toString(), registryKey, Lifecycle.stable(), false));
    }

    /** @deprecated */
    @Deprecated
    public static <T> RegistryBuilder<T, MappedRegistry<T>> createSimple(Class<T> type, ResourceLocation registryId) {
        return createSimple(ResourceKey.createRegistryKey(registryId));
    }

    /** @deprecated */
    @Deprecated
    public static <T> RegistryBuilder<T, DefaultedMappedRegistry<T>> createDefaulted(Class<T> type, ResourceLocation registryId, ResourceLocation defaultId) {
        return createDefaulted(ResourceKey.createRegistryKey(registryId), defaultId);
    }

    private RegistryBuilder(R registry) {
        this.registry = registry;
        this.attribute(RegistryAttribute.MODDED);
    }

    public RegistryBuilder<T, R> attribute(RegistryAttribute attribute) {
        this.attributes.add(attribute);
        return this;
    }

    public R buildAndRegister() {
        ResourceKey<?> key = this.registry.key();

        for (RegistryAttribute attribute : this.attributes) {
            RegistryAttributeHolder.get(key).addAttribute(attribute);
        }

        //noinspection unchecked
        RegistriesAccessor.getWRITABLE_REGISTRY().register((ResourceKey<WritableRegistry<?>>) key, this.registry, RegistrationInfo.BUILT_IN);
        return this.registry;
    }

}
