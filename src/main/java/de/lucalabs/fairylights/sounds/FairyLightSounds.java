package de.lucalabs.fairylights.sounds;

import de.lucalabs.fairylights.FairyLights;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public final class FairyLightSounds {
    public static final SoundEvent CORD_STRETCH = register("cord.stretch");
    public static final SoundEvent CORD_CONNECT = register("cord.connect");
    public static final SoundEvent CORD_DISCONNECT = register("cord.disconnect");
    public static final SoundEvent CORD_SNAP = register("cord.snap");
    public static final SoundEvent JINGLE_BELL = register("jingle_bell");
    public static final SoundEvent FEATURE_COLOR_CHANGE = register("feature.color_change");
    public static final SoundEvent FEATURE_LIGHT_TURNON = register("feature.light_turnon");
    public static final SoundEvent FEATURE_LIGHT_TURNOFF = register("feature.light_turnoff");

    private FairyLightSounds() {
    }

    private static SoundEvent register(final String name) {
        Identifier identifier = Identifier.of(FairyLights.ID, name);
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }

    public static void initialize() {
        FairyLights.LOGGER.info("Registering sounds");
    }
}
