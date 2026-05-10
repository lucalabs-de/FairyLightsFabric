package de.lucalabs.fairylights.main.connection;

import de.lucalabs.fairylights.main.fastener.Fastener;
import de.lucalabs.fairylights.main.items.DyeableItem;
import de.lucalabs.fairylights.main.items.components.ComponentRecords;
import java.util.UUID;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;

public final class GarlandTinselConnection extends Connection {
    private int color;

    public GarlandTinselConnection(final ConnectionType<? extends GarlandTinselConnection> type, final Level world, final Fastener<?> fastener, final UUID uuid) {
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
