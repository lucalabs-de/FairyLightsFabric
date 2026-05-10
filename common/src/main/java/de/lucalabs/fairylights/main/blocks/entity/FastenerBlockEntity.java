package de.lucalabs.fairylights.main.blocks.entity;

import de.lucalabs.fairylights.main.blocks.FairyLightBlocks;
import de.lucalabs.fairylights.main.blocks.FastenerBlock;
import de.lucalabs.fairylights.main.fastener.Fastener;
import de.lucalabs.fairylights.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public final class FastenerBlockEntity extends BlockEntity {
    public FastenerBlockEntity(final BlockPos pos, final BlockState state) {
        super(FairyLightBlockEntities.FASTENER, pos, state);
    }

    public static void tick(Level world, BlockPos pos, BlockState state, FastenerBlockEntity be) {
        be.getFastener().ifPresent(fastener -> {
            if (!world.isClientSide() && fastener.hasNoConnections()) {
                world.removeBlock(pos, false);
            } else if (!world.isClientSide() && fastener.update()) {
                be.setChanged();
                world.sendBlockUpdated(pos, state, state, 3);
            }
        });
    }

    public static void tickClient(Level level, BlockPos pos, BlockState state, FastenerBlockEntity be) {
        be.getFastener().ifPresent(Fastener::update);
    }

    public Vec3 getOffset() {
        return FairyLightBlocks.FASTENER.getOffset(this.getFacing(), 0.125F);
    }

    public Direction getFacing() {
        final BlockState state = this.level.getBlockState(this.worldPosition);
        if (state.getBlock() != FairyLightBlocks.FASTENER) {
            return Direction.UP;
        }
        return state.getValue(FastenerBlock.FACING);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void setLevel(final Level world) {
        super.setLevel(world);
        this.getFastener().ifPresent(fastener -> fastener.setWorld(world));
    }

    private Optional<Fastener<?>> getFastener() {
        return Services.COMPONENTS.maybeGet(this, Services.KEYS.FASTENER());
    }
}
