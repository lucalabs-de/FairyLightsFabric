package de.lucalabs.fairylights.util.compat;

import de.lucalabs.fairylights.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;

// TODO check if this is even needed
public final class ForgeRendering {
    private ForgeRendering() {}

    public static Box getRenderBoundingBox(BlockEntity be) {
        Box bb = Constants.INFINITE_BOX;
        BlockState state = be.getCachedState();
        Block block = state.getBlock();
        BlockPos pos = be.getPos();
        if (block == Blocks.ENCHANTING_TABLE) {
            bb = new Box(pos, pos.add(1, 1, 1));
        } else if (block != Blocks.CHEST && block != Blocks.TRAPPED_CHEST) {
            if (block == Blocks.STRUCTURE_BLOCK) {
                bb = Constants.INFINITE_BOX;
            } else if (block != null && block != Blocks.BEACON) {
                Box cbb = null;

                try {
                    VoxelShape collisionShape = state.getCollisionShape(be.getWorld(), pos);
                    if (!collisionShape.isEmpty()) {
                        cbb = collisionShape.getBoundingBox().offset(pos);
                    }
                } catch (Exception var7) {
                    cbb = new Box(pos.add(-1, 0, -1), pos.add(1, 1, 1));
                }

                if (cbb != null) {
                    bb = cbb;
                }
            }
        } else {
            bb = new Box(pos.add(-1, 0, -1), pos.add(2, 2, 2));
        }

        return bb;
    }
}
