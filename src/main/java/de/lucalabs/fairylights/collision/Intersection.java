package de.lucalabs.fairylights.collision;

import de.lucalabs.fairylights.feature.Feature;
import de.lucalabs.fairylights.feature.FeatureType;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public record Intersection(Vec3d result, Box hitBox, FeatureType featureType, Feature feature) {
}
