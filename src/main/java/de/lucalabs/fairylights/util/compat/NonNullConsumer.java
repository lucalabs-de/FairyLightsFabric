package de.lucalabs.fairylights.util.compat;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface NonNullConsumer<T> {
    void accept(@NotNull T var1);
}
