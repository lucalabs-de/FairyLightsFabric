package de.lucalabs.fairylights.components;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.component.DataComponentTypes;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.TransientComponent;

import java.util.Optional;

public abstract class GenericItemComponent<T> implements TransientComponent {

    @Nullable
    protected T delegate;

    public GenericItemComponent(ItemStack stack) {
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
