package de.lucalabs.fairylights.components;

import de.lucalabs.fairylights.fastener.Fastener;
import net.minecraft.nbt.NbtCompound;

public final class FastenerComponent extends GenericComponent<Fastener<?>> {

    public static final FastenerComponent DEFAULT = new FastenerComponent();

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
       this.delegate = fastener;
       return this;
    }
}
