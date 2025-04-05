package de.lucalabs.fairylights.components;

import de.lucalabs.fairylights.fastener.Fastener;
import dev.onyxstudios.cca.api.v3.component.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class GenericComponent<T> implements Component {

    @Nullable
    protected T delegate;

    public GenericComponent() {
        this.delegate = null;
    }

    public Optional<T> get() {
        return Optional.ofNullable(this.delegate);
    }

    public GenericComponent<T> set(T delegate) {
        this.delegate = delegate;
        return this;
    }

    public boolean isEmpty() {
        return delegate == null;
    }
}
