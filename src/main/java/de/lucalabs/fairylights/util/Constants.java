package de.lucalabs.fairylights.util;

import net.minecraft.world.phys.AABB;

public final class Constants {

    public static final AABB INFINITE_BOX = new AABB(
            Double.NEGATIVE_INFINITY,
            Double.NEGATIVE_INFINITY,
            Double.NEGATIVE_INFINITY,
            Double.POSITIVE_INFINITY,
            Double.POSITIVE_INFINITY,
            Double.POSITIVE_INFINITY);

    private Constants() {}
}
