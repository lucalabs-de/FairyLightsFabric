package de.lucalabs.fairylights.main.registries.vendor;

import net.minecraft.resources.ResourceKey;

import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class RegistryAttributeImpl implements RegistryAttributeHolder {
    private static final Map<ResourceKey<?>, RegistryAttributeHolder> HOLDER_MAP = new ConcurrentHashMap();
    private final EnumSet<RegistryAttribute> attributes = EnumSet.noneOf(RegistryAttribute.class);

    public static RegistryAttributeHolder getHolder(ResourceKey<?> registryKey) {
        return (RegistryAttributeHolder)HOLDER_MAP.computeIfAbsent(registryKey, (key) -> {
            return new RegistryAttributeImpl();
        });
    }

    private RegistryAttributeImpl() {
    }

    public RegistryAttributeHolder addAttribute(RegistryAttribute attribute) {
        this.attributes.add(attribute);
        return this;
    }

    public boolean hasAttribute(RegistryAttribute attribute) {
        return this.attributes.contains(attribute);
    }
}
