package de.lucalabs.fairylights.fastener;

import de.lucalabs.fairylights.fastener.accessor.BlockFastenerAccessor;
import de.lucalabs.fairylights.fastener.accessor.FastenerAccessor;
import de.lucalabs.fairylights.fastener.accessor.FenceFastenerAccessor;
import de.lucalabs.fairylights.fastener.accessor.PlayerFastenerAccessor;
import net.minecraft.nbt.NbtCompound;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

public enum FastenerType {
    BLOCK(BlockFastenerAccessor::new),
    FENCE(FenceFastenerAccessor::new),
    PLAYER(PlayerFastenerAccessor::new);

    private static final Map<String, FastenerType> NAME_TO_TYPE = new HashMap<>();

    static {
        for (final FastenerType type : values()) {
            NAME_TO_TYPE.put(type.name, type);
        }
    }

    private final String name;

    FastenerType(final Supplier<? extends FastenerAccessor> supplier) {
        this.name = this.name().toLowerCase(Locale.ENGLISH);
    }

    public static FastenerType fromName(final String name) {
        return NAME_TO_TYPE.get(name);
    }
}
