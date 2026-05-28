package de.lucalabs.fairylights.platform;

import de.lucalabs.fairylights.main.components.ComponentAccessor;
import de.lucalabs.fairylights.main.components.FastenerKey;
import de.lucalabs.fairylights.main.components.Key;

import java.util.Optional;
import java.util.function.Supplier;

public class FabricComponentAccessor implements ComponentAccessor {
    @Override
    public <T> Optional<T> maybeGet(Object o, Key<T> key) {
        return Optional.ofNullable(key.tryGetFor(o));
    }

    @Override
    public <T, X extends Throwable> T getOrThrow(Object o, Key<T> key, Supplier<? extends X> e) throws X {
        var result = key.tryGetFor(o);
        if (result == null) {
            throw e.get();
        }
        return result;
    }

    @Override
    public <T> void sync(Object o, Key<T> key) {
        if (! (key instanceof FastenerKey)) {
           throw new IllegalStateException("key type not supported");
        }
        ((FastenerKey) key).syncFor(o);
    }
}
