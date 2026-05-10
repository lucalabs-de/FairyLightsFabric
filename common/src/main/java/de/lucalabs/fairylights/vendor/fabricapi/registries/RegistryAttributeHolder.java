package de.lucalabs.fairylights.vendor.fabricapi.registries;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface RegistryAttributeHolder {
    static RegistryAttributeHolder get(ResourceKey<?> registryKey) {
        return RegistryAttributeImpl.getHolder(registryKey);
    }

    static RegistryAttributeHolder get(Registry<?> registry) {
        return get(registry.key());
    }

    RegistryAttributeHolder addAttribute(RegistryAttribute var1);

    boolean hasAttribute(RegistryAttribute var1);
}
