package de.lucalabs.fairylights.components;

import de.lucalabs.fairylights.fastener.Fastener;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.NbtCompound;

public final class FastenerComponent extends GenericComponent<Fastener<?>> implements AutoSyncedComponent {

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        if (delegate != null) {
            delegate.readFromNbt(nbtCompound);
        }
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        if (delegate != null) {
            delegate.writeToNbt(nbtCompound);
        }
    }

    public FastenerComponent setFastener(Fastener<?> fastener) {
        return (FastenerComponent) super.set(fastener);
    }
}
