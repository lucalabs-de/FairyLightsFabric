package de.lucalabs.fairylights.main.collision;

import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public interface Collidable {
    @Nullable
    Intersection intersect(final Vec3 origin, final Vec3 end);

    static Collidable empty() {
        return (o, e) -> null;
    }
}
