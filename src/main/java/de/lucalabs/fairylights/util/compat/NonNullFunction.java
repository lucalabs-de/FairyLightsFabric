package de.lucalabs.fairylights.util.compat;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface NonNullFunction<T, R> {
    @NotNull R apply(@NotNull T var1);
}
