package de.lucalabs.fairylights.platform;

import de.lucalabs.fairylights.main.components.ComponentAccessor;
import de.lucalabs.fairylights.main.components.Key;

import java.util.Optional;

public class FabricComponentAccessor implements ComponentAccessor {
    @Override
    public <T> Optional<T> maybeGet(Object o, Key<T> key) {

        return Optional.ofNullable(key.tryGetFor(o));
    }
}
