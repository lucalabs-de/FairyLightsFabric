package de.lucalabs.fairylights.main.connection;

import de.lucalabs.fairylights.main.fastener.Fastener;
import java.util.UUID;
import net.minecraft.world.level.Level;

public final class GarlandVineConnection extends Connection {
    public GarlandVineConnection(final ConnectionType<? extends GarlandVineConnection> type, final Level world, final Fastener<?> fastener, final UUID uuid) {
        super(type, world, fastener, uuid);
    }

    @Override
    public float getRadius() {
        return 2.5F / 16.0F;
    }
}
