package de.lucalabs.fairylights.util.compat;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface NonNullSupplier<T> {
    @NotNull T get();
}
