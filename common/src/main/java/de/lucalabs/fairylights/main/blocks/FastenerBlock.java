package de.lucalabs.fairylights.main.blocks;

import com.mojang.serialization.MapCodec;
import de.lucalabs.fairylights.main.blocks.entity.FairyLightBlockEntities;
import de.lucalabs.fairylights.main.blocks.entity.FastenerBlockEntity;
import de.lucalabs.fairylights.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class FastenerBlock extends DirectionalBlock implements EntityBlock {
    public static final MapCodec<FastenerBlock> CODEC = simpleCodec(FastenerBlock::new);
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

    private static final VoxelShape NORTH_BOX = Block.box(6.0D, 6.0D, 12.0D, 10.0D, 10.0D, 16.0D);
    private static final VoxelShape SOUTH_BOX = Block.box(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 4.0D);
    private static final VoxelShape WEST_BOX = Block.box(12.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D);
    private static final VoxelShape EAST_BOX = Block.box(0.0D, 6.0D, 6.0D, 4.0D, 10.0D, 10.0D);
    private static final VoxelShape DOWN_BOX = Block.box(6.0D, 12.0D, 6.0D, 10.0D, 16.0D, 10.0D);
    private static final VoxelShape UP_BOX = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 4.0D, 10.0D);

    public FastenerBlock(final BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(TRIGGERED, false)
        );
    }

    @Override
    protected @NotNull MapCodec<FastenerBlock> codec() {
        return CODEC;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> actual, BlockEntityType<E> expect, BlockEntityTicker<? super E> ticker) {
        return expect == actual ? (BlockEntityTicker<A>) ticker : null;
    }

    public static Vec3 getFastenerOffset(final Direction facing, final float offset) {
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
        return new Vec3(x, y, z);
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TRIGGERED);
    }

    @Override
    public @NotNull BlockState rotate(final BlockState state, final Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(final BlockState state, final Mirror mirrorIn) {
        return state.setValue(FACING, mirrorIn.mirror(state.getValue(FACING)));
    }

    @Override
    public @NotNull VoxelShape getShape(final BlockState state, final BlockGetter worldIn, final BlockPos pos, final CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case NORTH -> NORTH_BOX;
            case SOUTH -> SOUTH_BOX;
            case WEST -> WEST_BOX;
            case EAST -> EAST_BOX;
            case DOWN -> DOWN_BOX;
            default -> UP_BOX;
        };
    }

    @Override
    public BlockEntity newBlockEntity(final BlockPos pos, final BlockState state) {
        return new FastenerBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            final Level world,
            final @NotNull BlockState state,
            final @NotNull BlockEntityType<T> type) {

        if (world.isClientSide()) {
            return createTickerHelper(type, FairyLightBlockEntities.FASTENER, FastenerBlockEntity::tickClient);
        }
        return createTickerHelper(type, FairyLightBlockEntities.FASTENER, FastenerBlockEntity::tick);
    }

    @Override
    public void onRemove(
            final BlockState state,
            final @NotNull Level world,
            final @NotNull BlockPos pos,
            final BlockState newState,
            final boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            final BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof FastenerBlockEntity) {
                Services.COMPONENTS.maybeGet(entity, Services.KEYS.FASTENER()).ifPresent(f -> {
                    f.remove();
                    f.dropItems(world, pos);
                });
            }
            super.onRemove(state, world, pos, newState, isMoving);
        }
    }

    @Override
    public boolean canSurvive(final BlockState state, final LevelReader world, final BlockPos pos) {
        final Direction facing = state.getValue(FACING);
        final BlockPos attachedPos = pos.relative(facing.getOpposite());
        final BlockState attachedState = world.getBlockState(attachedPos);
        return attachedState.is(BlockTags.LEAVES)
                || attachedState.isFaceSturdy(world, attachedPos, facing)
                || facing == Direction.UP && attachedState.is(BlockTags.WALLS);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext context) {
        BlockState result = this.defaultBlockState();
        final Level world = context.getLevel();
        final BlockPos pos = context.getClickedPos();
        for (final Direction dir : context.getNearestLookingDirections()) {
            result = result.setValue(FACING, dir.getOpposite());
            if (result.canSurvive(world, pos)) {
                return result.setValue(TRIGGERED, world.hasNeighborSignal(pos.relative(dir)));
            }
        }
        return null;
    }

    @Override
    public void neighborChanged(
            final BlockState state,
            final @NotNull Level world,
            final @NotNull BlockPos pos,
            final @NotNull Block blockIn,
            final @NotNull BlockPos fromPos,
            final boolean isMoving) {
        if (state.canSurvive(world, pos)) {
            final boolean receivingPower = world.hasNeighborSignal(pos);
            final boolean isPowered = state.getValue(TRIGGERED);
            if (receivingPower && !isPowered) {
                world.scheduleTick(pos, this, 2);
                world.setBlock(pos, state.setValue(TRIGGERED, true), 4);
            } else if (!receivingPower && isPowered) {
                world.setBlock(pos, state.setValue(TRIGGERED, false), 4);
            }
        } else {
            final BlockEntity entity = world.getBlockEntity(pos);
            dropResources(state, world, pos, entity);
            world.removeBlock(pos, false);
        }
    }

    public Vec3 getOffset(final Direction facing, final float offset) {
        return getFastenerOffset(facing, offset);
    }
}
