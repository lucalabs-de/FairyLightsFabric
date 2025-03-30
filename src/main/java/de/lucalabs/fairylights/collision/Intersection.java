package de.lucalabs.fairylights.collision;

import de.lucalabs.fairylights.feature.Feature;
import de.lucalabs.fairylights.feature.FeatureType;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public final class Intersection {
    private final Vec3d result;

    private final Box hitBox;

    private final FeatureType featureType;

    private final Feature feature;

    public Intersection(final Vec3d result, final Box hitBox, final FeatureType featureType, final Feature feature) {
        this.result = result;
        this.hitBox = hitBox;
        this.featureType = featureType;
        this.feature = feature;
    }

    public Vec3d getResult() {
        return this.result;
    }

    public Box getHitBox() {
        return this.hitBox;
    }

    public FeatureType getFeatureType() {
        return this.featureType;
    }

    public Feature getFeature() {
        return this.feature;
    }
}
