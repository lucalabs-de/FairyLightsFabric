package de.lucalabs.fairylights.main.fastener;

import de.lucalabs.fairylights.main.util.matrix.Matrix;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public interface BlockView {
    boolean isMoving(final Level world, final BlockPos source);

    Vec3 getPosition(final Level world, final BlockPos source, final Vec3 pos);

    void unrotate(final Level world, final BlockPos source, final Matrix matrix, final float delta);
}
