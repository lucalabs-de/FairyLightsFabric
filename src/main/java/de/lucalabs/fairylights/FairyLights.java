package de.lucalabs.fairylights;

import de.lucalabs.fairylights.net.InteractionConnectionMessage;
import de.lucalabs.fairylights.sounds.FairyLightSounds;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FairyLights implements ModInitializer {
    public static final String ID = "fairylights";

    public static final Logger LOGGER = LoggerFactory.getLogger(ID);

    @Override
    public void onInitialize() {
        FairyLightSounds.initialize();

        ServerPlayNetworking.registerGlobalReceiver(InteractionConnectionMessage.ID, InteractionConnectionMessage::apply);
    }
}