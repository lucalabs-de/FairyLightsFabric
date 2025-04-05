package de.lucalabs.fairylights;

import de.lucalabs.fairylights.blocks.FairyLightBlocks;
import de.lucalabs.fairylights.blocks.entity.FairyLightBlockEntities;
import de.lucalabs.fairylights.connection.ConnectionTypes;
import de.lucalabs.fairylights.entity.FairyLightEntities;
import de.lucalabs.fairylights.items.FairyLightItems;
import de.lucalabs.fairylights.net.serverbound.InteractionConnectionMessage;
import de.lucalabs.fairylights.registries.FairyLightRegistries;
import de.lucalabs.fairylights.sounds.FairyLightSounds;
import de.lucalabs.fairylights.string.StringTypes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FairyLights implements ModInitializer {
    public static final String ID = "fairylights";

    public static final Logger LOGGER = LoggerFactory.getLogger(ID);

    @Override
    public void onInitialize() {
        FairyLightSounds.initialize();
        FairyLightEntities.initialize();
        FairyLightBlockEntities.initialize();
        FairyLightRegistries.initialize();
        FairyLightItems.initialize();
        FairyLightBlocks.initialize();

        ConnectionTypes.initialize();
        StringTypes.initialize();

        ServerPlayNetworking.registerGlobalReceiver(InteractionConnectionMessage.ID, InteractionConnectionMessage::apply);
    }
}