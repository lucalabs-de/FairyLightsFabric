package de.lucalabs.fairylights.main.feature.light;

public interface BrightnessLightBehavior extends LightBehavior {
    float getBrightness(final float delta);
}
