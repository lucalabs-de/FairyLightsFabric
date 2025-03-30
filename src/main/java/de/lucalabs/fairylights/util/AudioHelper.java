package de.lucalabs.fairylights.util;

import de.lucalabs.fairylights.connection.HangingLightsConnection;
import de.lucalabs.fairylights.feature.light.Light;
import de.lucalabs.fairylights.jingle.Jingle;
import de.lucalabs.fairylights.jingle.JingleManager;
import de.lucalabs.fairylights.net.FilteredServerPlayNetworking;
import de.lucalabs.fairylights.net.clientbound.JingleMessage;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public final class AudioHelper {

    private AudioHelper() {}

    public static boolean tryJingle(final World world, final HangingLightsConnection hangingLights) {
        return tryJingle(world, hangingLights, "");
    }

    public static boolean tryJingle(final World world, final HangingLightsConnection hangingLights, final String lib) {
        if (world.isClient()) return false;
        final Light<?>[] lights = hangingLights.getFeatures();
        final Jingle jingle = JingleManager.INSTANCE.get(lib).getRandom(world.random, lights.length);
        if (jingle != null) {
            final int lightOffset = lights.length / 2 - jingle.getRange() / 2;
            hangingLights.play(jingle, lightOffset);
            FilteredServerPlayNetworking.sendToPlayersTrackingChunk(
                    (ServerWorld) world,
                    new ChunkPos(hangingLights.getFastener().getPos()),
                    JingleMessage.ID,
                    new JingleMessage(hangingLights, lightOffset, jingle)
            );
            return true;
        }
        return false;
    }

}