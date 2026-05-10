package de.lucalabs.fairylights.main.util;

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
