package de.lucalabs.fairylights.main.sounds;

import de.lucalabs.fairylights.Common;
import de.lucalabs.fairylights.Constants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

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
        ResourceLocation identifier = Common.id(name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
    }

    public static void initialize() {
        Constants.LOG.info("Registering sounds");
    }
}
