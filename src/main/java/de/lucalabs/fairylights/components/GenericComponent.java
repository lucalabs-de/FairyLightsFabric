package de.lucalabs.fairylights.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class GenericComponent<T> implements Component {

    @Nullable
    protected T delegate;

    GenericComponent() {
        this.delegate = null;
    }

    public Optional<T> get() {
        return Optional.ofNullable(this.delegate);
    }

    public boolean isEmpty() {
        return delegate == null;
    }
}
