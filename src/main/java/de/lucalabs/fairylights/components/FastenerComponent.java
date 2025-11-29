package de.lucalabs.fairylights.components;

import de.lucalabs.fairylights.fastener.Fastener;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public final class FastenerComponent extends GenericComponent<Fastener<?>> implements AutoSyncedComponent {

    @Override
    public void readFromNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup lookup) {
        if (delegate != null) {
            delegate.readFromNbt(nbtCompound);
        }
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup lookup) {
        if (delegate != null) {
            delegate.writeToNbt(nbtCompound);
        }
    }

    public FastenerComponent setFastener(Fastener<?> fastener) {
        return (FastenerComponent) super.set(fastener);
    }
}
