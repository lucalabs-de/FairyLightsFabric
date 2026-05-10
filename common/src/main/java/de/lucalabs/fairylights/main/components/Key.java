package de.lucalabs.fairylights.main.components;

import org.jetbrains.annotations.Nullable;

public interface Key<T> {
    @Nullable T tryGetFor(Object o);
}
