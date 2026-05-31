package de.lucalabs.fairylights.main.net;

import de.lucalabs.fairylights.Constants;
import de.lucalabs.fairylights.main.net.serverbound.InteractionConnectionMessage;
import de.lucalabs.fairylights.platform.Services;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class NeoForgeNetworking {

    private NeoForgeNetworking() {
    }

    public static void init(final IEventBus modBus) {
        modBus.addListener(RegisterPayloadHandlersEvent.class, NeoForgeNetworking::register);
    }

    private static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(Constants.MOD_ID);
        registrar.playToServer(
                InteractionConnectionMessagePayload.ID,
                InteractionConnectionMessagePayload.CODEC,
                NeoForgeNetworking::handleInteraction);
        registrar.playToClient(
                FastenerSyncPayload.ID,
                FastenerSyncPayload.CODEC,
                NeoForgeNetworking::handleFastenerSync);
    }

    private static void handleInteraction(final InteractionConnectionMessagePayload payload, final IPayloadContext context) {
        if (context.player() instanceof final ServerPlayer player) {
            context.enqueueWork(() -> InteractionConnectionMessage.apply(payload, player));
        }
    }

    private static void handleFastenerSync(final FastenerSyncPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            final Entity entity = context.player().level().getEntity(payload.entityId());
            if (entity != null) {
                Services.COMPONENTS.maybeGet(entity, Services.KEYS.FASTENER())
                        .ifPresent(fastener -> fastener.readFromNbt(payload.fastener()));
            }
        });
    }
}
