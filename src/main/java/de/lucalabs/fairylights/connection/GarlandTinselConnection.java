package de.lucalabs.fairylights.connection;

import de.lucalabs.fairylights.fastener.Fastener;
import de.lucalabs.fairylights.items.DyeableItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.DyeColor;
import net.minecraft.world.World;

import java.util.UUID;

public final class GarlandTinselConnection extends Connection {
    private int color;

    public GarlandTinselConnection(final ConnectionType<? extends GarlandTinselConnection> type, final World world, final Fastener<?> fastener, final UUID uuid) {
        super(type, world, fastener, uuid);
        this.color = DyeableItem.getColor(DyeColor.LIGHT_GRAY);
    }

    public int getColor() {
        return this.color;
    }

    @Override
    public float getRadius() {
        return 0.125F;
    }

    @Override
    public NbtCompound serializeLogic() {
        return DyeableItem.setColor(super.serializeLogic(), this.color);
    }

    @Override
    public void deserializeLogic(final NbtCompound compound) {
        super.deserializeLogic(compound);
        this.color = DyeableItem.getColor(compound);
    }
}
