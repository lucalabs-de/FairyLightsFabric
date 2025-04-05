package de.lucalabs.fairylights;

import de.lucalabs.fairylights.net.UpdateEntityFastenerMessageHandler;
import de.lucalabs.fairylights.net.clientbound.UpdateEntityFastenerMessage;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.render.entity.EntityRenderers;

public class FairyLightsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(
				UpdateEntityFastenerMessage.ID,
				UpdateEntityFastenerMessageHandler::apply);

	}
}