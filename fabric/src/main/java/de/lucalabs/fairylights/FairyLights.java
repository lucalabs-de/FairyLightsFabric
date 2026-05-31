package de.lucalabs.fairylights;

import de.lucalabs.fairylights.main.blocks.FairyLightBlocks;
import de.lucalabs.fairylights.main.blocks.entity.FairyLightBlockEntities;
import de.lucalabs.fairylights.main.connection.ConnectionTypes;
import de.lucalabs.fairylights.main.creativetabs.FairyLightItemGroups;
import de.lucalabs.fairylights.main.entity.FairyLightEntities;
import de.lucalabs.fairylights.main.events.ServerEventHandler;
import de.lucalabs.fairylights.main.items.FairyLightItems;
import de.lucalabs.fairylights.main.items.components.FairyLightItemComponents;
import de.lucalabs.fairylights.main.items.crafting.FairyLightCraftingRecipes;
import de.lucalabs.fairylights.main.net.InteractionConnectionMessagePayload;
import de.lucalabs.fairylights.main.net.serverbound.InteractionConnectionMessage;
import de.lucalabs.fairylights.main.registries.FairyLightRegistries;
import de.lucalabs.fairylights.main.sounds.FairyLightSounds;
import de.lucalabs.fairylights.main.string.StringTypes;
import de.lucalabs.fairylights.main.util.Tags;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FairyLights implements ModInitializer {
    public static final String ID = "fairylights";

    public static final Logger LOGGER = LoggerFactory.getLogger(ID);

    @Override
    public void onInitialize() {
        ServerEventHandler.initialize();

        Tags.initialize();

        FairyLightSounds.initialize();
        FairyLightEntities.initialize();
        FairyLightItems.initialize();
        FairyLightBlockEntities.initialize();
        FairyLightRegistries.initialize();
        FairyLightBlocks.initialize();
        FairyLightCraftingRecipes.initialize();
        FairyLightItemGroups.initialize();
        FairyLightItemComponents.initialize();

        ConnectionTypes.initialize();
        StringTypes.initialize();

        PayloadTypeRegistry.playC2S().register(InteractionConnectionMessagePayload.ID, InteractionConnectionMessagePayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(InteractionConnectionMessagePayload.ID, InteractionConnectionMessage::apply);
    }
}