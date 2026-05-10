package de.lucalabs.fairylights.main.components;

import de.lucalabs.fairylights.main.fastener.Fastener;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public final class FastenerComponent extends GenericComponent<Fastener<?>> implements AutoSyncedComponent {

    @Override
    public void readFromNbt(CompoundTag nbtCompound, HolderLookup.Provider lookup) {
        if (delegate != null) {
            delegate.readFromNbt(nbtCompound);
        }
    }

    @Override
    public void writeToNbt(CompoundTag nbtCompound, HolderLookup.Provider lookup) {
        if (delegate != null) {
            delegate.writeToNbt(nbtCompound);
        }
    }

    public FastenerComponent setFastener(Fastener<?> fastener) {
        return (FastenerComponent) super.set(fastener);
    }
}
