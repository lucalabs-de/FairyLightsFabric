package de.lucalabs.fairylights.components;

import de.lucalabs.fairylights.fastener.Fastener;
import de.lucalabs.fairylights.fastener.accessor.FastenerAccessor;
import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class FastenerComponent implements Component {

    public static final FastenerComponent DEFAULT = new FastenerComponent();

    @Nullable
    Fastener<?> fastener;

    FastenerComponent() {
        this.fastener = null;
    }

    public Optional<Fastener<?>> get() {
        return Optional.ofNullable(this.fastener);
    }

    public boolean isEmpty() {
        return fastener == null;
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        if (fastener != null) {
            fastener.readFromNbt(nbtCompound);
        }
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        if (fastener != null) {
            fastener.writeToNbt(nbtCompound);
        }
    }
}
