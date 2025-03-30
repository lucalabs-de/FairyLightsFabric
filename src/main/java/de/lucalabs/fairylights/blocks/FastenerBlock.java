package de.lucalabs.fairylights.blocks;

import de.lucalabs.fairylights.blocks.entity.FairyLightBlockEntities;
import de.lucalabs.fairylights.blocks.entity.FastenerBlockEntity;
import de.lucalabs.fairylights.components.FairyLightComponents;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public final class FastenerBlock extends FacingBlock implements BlockEntityProvider {
    public static final BooleanProperty TRIGGERED = Properties.TRIGGERED;

    private static final VoxelShape NORTH_BOX = Block.createCuboidShape(6.0D, 6.0D, 12.0D, 10.0D, 10.0D, 16.0D);
    private static final VoxelShape SOUTH_BOX = Block.createCuboidShape(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 4.0D);
    private static final VoxelShape WEST_BOX = Block.createCuboidShape(12.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D);
    private static final VoxelShape EAST_BOX = Block.createCuboidShape(0.0D, 6.0D, 6.0D, 4.0D, 10.0D, 10.0D);
    private static final VoxelShape DOWN_BOX = Block.createCuboidShape(6.0D, 12.0D, 6.0D, 10.0D, 16.0D, 10.0D);
    private static final VoxelShape UP_BOX = Block.createCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 4.0D, 10.0D);

    public FastenerBlock(final Block.Settings properties) {
        super(properties);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(TRIGGERED, false)
        );
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> actual, BlockEntityType<E> expect, BlockEntityTicker<? super E> ticker) {
        return expect == actual ? (BlockEntityTicker<A>) ticker : null;
    }

    public static Vec3d getFastenerOffset(final Direction facing, final float offset) {
        double x = offset, y = offset, z = offset;
        switch (facing) {
            case DOWN:
                y += 0.75F;
            case UP:
                x += 0.375F;
                z += 0.375F;
                break;
            case WEST:
                x += 0.75F;
            case EAST:
                z += 0.375F;
                y += 0.375F;
                break;
            case NORTH:
                z += 0.75F;
            case SOUTH:
                x += 0.375F;
                y += 0.375F;
        }
        return new Vec3d(x, y, z);
    }

    @Override
    protected void appendProperties(final StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, TRIGGERED);
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState rotate(final BlockState state, final BlockRotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState mirror(final BlockState state, final BlockMirror mirrorIn) {
        return state.with(FACING, mirrorIn.apply(state.get(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOutlineShape(final BlockState state, final BlockView worldIn, final BlockPos pos, final ShapeContext context) {
        return switch (state.get(FACING)) {
            case NORTH -> NORTH_BOX;
            case SOUTH -> SOUTH_BOX;
            case WEST -> WEST_BOX;
            case EAST -> EAST_BOX;
            case DOWN -> DOWN_BOX;
            default -> UP_BOX;
        };
    }

    @Override
    public BlockEntity createBlockEntity(final BlockPos pos, final BlockState state) {
        return new FastenerBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            final World world,
            final BlockState state,
            final BlockEntityType<T> type) {

        if (world.isClient()) {
            return createTickerHelper(type, FairyLightBlockEntities.FASTENER, FastenerBlockEntity::tickClient);
        }
        return createTickerHelper(type, FairyLightBlockEntities.FASTENER, FastenerBlockEntity::tick);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStateReplaced(
            final BlockState state,
            final World world,
            final BlockPos pos,
            final BlockState newState,
            final boolean isMoving) {
        if (!state.isOf(newState.getBlock())) {
            final BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof FastenerBlockEntity) {
                FairyLightComponents.FASTENER.get(entity).get().ifPresent(f -> f.dropItems(world, pos));
            }
            super.onStateReplaced(state, world, pos, newState, isMoving);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canPlaceAt(final BlockState state, final WorldView world, final BlockPos pos) {
        final Direction facing = state.get(FACING);
        final BlockPos attachedPos = pos.offset(facing.getOpposite());
        final BlockState attachedState = world.getBlockState(attachedPos);
        return attachedState.isIn(BlockTags.LEAVES)
                || attachedState.isSideSolidFullSquare(world, attachedPos, facing)
                || facing == Direction.UP && attachedState.isIn(BlockTags.WALLS);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(final ItemPlacementContext context) {
        BlockState result = this.getDefaultState();
        final World world = context.getWorld();
        final BlockPos pos = context.getBlockPos();
        for (final Direction dir : context.getPlacementDirections()) {
            result = result.with(FACING, dir.getOpposite());
            if (result.canPlaceAt(world, pos)) {
                return result.with(TRIGGERED, world.isReceivingRedstonePower(pos.offset(dir)));
            }
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborUpdate(
            final BlockState state,
            final World world,
            final BlockPos pos,
            final Block blockIn,
            final BlockPos fromPos,
            final boolean isMoving) {

        if (state.canPlaceAt(world, pos)) {
            final boolean receivingPower = world.isReceivingRedstonePower(pos);
            final boolean isPowered = state.get(TRIGGERED);
            if (receivingPower && !isPowered) {
                world.scheduleBlockTick(pos, this, 2);
                world.setBlockState(pos, state.with(TRIGGERED, true), 4);
            } else if (!receivingPower && isPowered) {
                world.setBlockState(pos, state.with(TRIGGERED, false), 4);
            }
        } else {
            final BlockEntity entity = world.getBlockEntity(pos);
            dropStacks(state, world, pos, entity);
            world.removeBlock(pos, false);
        }
    }

    public Vec3d getOffset(final Direction facing, final float offset) {
        return getFastenerOffset(facing, offset);
    }
}
