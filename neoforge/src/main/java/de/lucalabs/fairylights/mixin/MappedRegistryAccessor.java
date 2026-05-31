package de.lucalabs.fairylights.mixin;

import net.minecraft.core.MappedRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * NeoForge keeps the root registry frozen outside of its own internal registry handling, but the
 * common module creates its custom registries (connection/string/light-variant) by writing to the
 * root directly (a Fabric idiom, via the vendored RegistryBuilder). This accessor lets the NeoForge
 * entry point briefly toggle the root's frozen flag around that registration so the eager,
 * loader-agnostic common code can run unchanged.
 */
@Mixin(MappedRegistry.class)
public interface MappedRegistryAccessor {
    @Accessor("frozen")
    void fairylights$setFrozen(boolean frozen);
}
