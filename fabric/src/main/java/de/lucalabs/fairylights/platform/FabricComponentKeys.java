package de.lucalabs.fairylights.platform;

import de.lucalabs.fairylights.main.components.FastenerKey;
import de.lucalabs.fairylights.main.components.Key;
import de.lucalabs.fairylights.main.components.Keys;
import de.lucalabs.fairylights.main.fastener.Fastener;

public class FabricComponentKeys extends Keys {
    public Key<Fastener<?>> FASTENER() {
        return new FastenerKey();
    }
}
