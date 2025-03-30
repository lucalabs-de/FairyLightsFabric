package de.lucalabs.fairylights.collision;

import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public interface Collidable {
    @Nullable
    Intersection intersect(final Vec3d origin, final Vec3d end);

    static Collidable empty() {
        return (o, e) -> null;
    }
}
