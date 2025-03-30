package de.lucalabs.fairylights;

import de.lucalabs.fairylights.fastener.Fastener;
import de.lucalabs.fairylights.net.InteractionConnectionMessage;
import de.lucalabs.fairylights.sounds.FairyLightSounds;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FairyLights implements ModInitializer {
    public static final String ID = "fairylights";

    public static final Logger LOGGER = LoggerFactory.getLogger(ID);

    public static final ComponentKey<Fastener<?>> FASTENER_COMPONENT =
            ComponentRegistry.getOrCreate(Identifier.of(ID, "fastener"), Fastener.class);

    @Override
    public void onInitialize() {
        FairyLightSounds.initialize();

        ServerPlayNetworking.registerGlobalReceiver(InteractionConnectionMessage.ID, InteractionConnectionMessage::apply);
    }
}