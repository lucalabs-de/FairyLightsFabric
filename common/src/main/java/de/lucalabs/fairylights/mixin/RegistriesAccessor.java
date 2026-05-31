package de.lucalabs.fairylights.mixin;

import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({BuiltInRegistries.class})
public interface RegistriesAccessor<T> {
    @Accessor
    static WritableRegistry<WritableRegistry<?>> getWRITABLE_REGISTRY() {
        throw new UnsupportedOperationException();
    }
}
