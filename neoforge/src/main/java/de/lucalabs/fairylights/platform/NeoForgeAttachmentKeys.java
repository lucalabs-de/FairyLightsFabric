package de.lucalabs.fairylights.platform;

import de.lucalabs.fairylights.main.components.Key;
import de.lucalabs.fairylights.main.components.Keys;
import de.lucalabs.fairylights.main.fastener.Fastener;

public class NeoForgeAttachmentKeys extends Keys {

    private final NeoForgeFastenerKey fastener = new NeoForgeFastenerKey();

    @Override
    public Key<Fastener<?>> FASTENER() {
        return this.fastener;
    }
}
