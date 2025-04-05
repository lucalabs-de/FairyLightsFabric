package de.lucalabs.fairylights.components;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.item.ItemComponent;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class GenericItemComponent<T> extends ItemComponent {

    @Nullable
    protected T delegate;

    public GenericItemComponent(ItemStack stack, ComponentKey<?> key) {
       super(stack, key);
       this.delegate = null;
    }

    public Optional<T> get() {
        return Optional.ofNullable(this.delegate);
    }

    public GenericItemComponent<T> set(T delegate) {
        this.delegate = delegate;
        return this;
    }

    public boolean isEmpty() {
        return delegate == null;
    }

}
