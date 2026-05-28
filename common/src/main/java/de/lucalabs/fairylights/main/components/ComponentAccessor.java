package de.lucalabs.fairylights.main.components;

import java.util.Optional;
import java.util.function.Supplier;

public interface ComponentAccessor {
    <T> Optional<T> maybeGet(Object o, Key<T> key);

    <T, X extends Throwable> T getOrThrow(Object o, Key<T> key, Supplier<? extends X> e) throws X;

    <T> void sync(Object o, Key<T> key);
}
