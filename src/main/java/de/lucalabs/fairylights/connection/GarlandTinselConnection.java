package de.lucalabs.fairylights.connection;

import de.lucalabs.fairylights.fastener.Fastener;
import de.lucalabs.fairylights.items.DyeableItem;
import de.lucalabs.fairylights.items.components.ComponentRecords;
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
    public ComponentRecords.ConnectionLogic.Builder serializeLogic() {
        return super.serializeLogic().color(this.color);
    }

    @Override
    public void deserializeLogic(final ComponentRecords.ConnectionLogic logic) {
        super.deserializeLogic(logic);
        logic.color().ifPresent(c -> this.color = c);
    }
}
