package de.lucalabs.fairylights.events;

import de.lucalabs.fairylights.FairyLights;

public final class ServerEventHandler {
    private ServerEventHandler() {}

    public static void initialize() {
        FairyLights.LOGGER.info("initializing event listener");
    }
}
