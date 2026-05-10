package de.lucalabs.fairylights.main.collision;

import de.lucalabs.fairylights.main.feature.Feature;
import de.lucalabs.fairylights.main.feature.FeatureType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public record Intersection(Vec3 result, AABB hitBox, FeatureType featureType, Feature feature) {
}
