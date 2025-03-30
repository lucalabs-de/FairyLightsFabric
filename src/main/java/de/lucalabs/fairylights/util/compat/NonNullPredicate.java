package de.lucalabs.fairylights.util.compat;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface NonNullPredicate<T> {
    boolean test(@NotNull T var1);
}
