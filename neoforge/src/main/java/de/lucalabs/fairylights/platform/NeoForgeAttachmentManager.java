package de.lucalabs.fairylights.platform;

import de.lucalabs.fairylights.main.components.ComponentAccessor;
import de.lucalabs.fairylights.main.components.Key;

import java.util.Optional;
import java.util.function.Supplier;

public class NeoForgeAttachmentManager implements ComponentAccessor {

    @Override
    public <T> Optional<T> maybeGet(final Object o, final Key<T> key) {
        return Optional.ofNullable(key.tryGetFor(o));
    }

    @Override
    public <T, X extends Throwable> T getOrThrow(final Object o, final Key<T> key, final Supplier<? extends X> e) throws X {
        final T result = key.tryGetFor(o);
        if (result == null) {
            throw e.get();
        }
        return result;
    }

    @Override
    public <T> void sync(final Object o, final Key<T> key) {
        if (!(key instanceof NeoForgeFastenerKey)) {
            throw new IllegalStateException("key type not supported");
        }
        ((NeoForgeFastenerKey) key).syncFor(o);
    }
}
