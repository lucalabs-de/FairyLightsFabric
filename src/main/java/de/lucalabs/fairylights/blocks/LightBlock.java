package de.lucalabs.fairylights.blocks;

import de.lucalabs.fairylights.blocks.entity.LightBlockEntity;
import de.lucalabs.fairylights.items.DyeableItem;
import de.lucalabs.fairylights.items.LightVariant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class LightBlock extends WallMountedBlock implements BlockEntityProvider {
    public static final BooleanProperty LIT = Properties.LIT;

    private static final VoxelShape MIN_ANCHOR_SHAPE = Block.createCuboidShape(7.0D, 0.0D, 7.0D, 9.0D, 16.0D, 9.0D);

    private final VoxelShape floorShape, eastWallShape, westWallShape, northWallShape, southWallShape, ceilingShape;

    private final LightVariant<?> variant;

    public LightBlock(final Settings properties, final LightVariant<?> variant) {
        super(properties);
        this.variant = variant;
        final Box bb = this.variant.getBounds();
        final double w = Math.max(bb.getXLength(), bb.getZLength());
        final double w0 = 0.5D - w * 0.5D;
        final double w1 = 0.5D + w * 0.5D;
        if (variant.isOrientable()) {
            this.floorShape = clampBox(w0, 0.0D, w0, w1, -bb.minY, w1);
            this.eastWallShape = clampBox(0.0D, w0, w0, -bb.minY, w1, w1);
            this.westWallShape = clampBox(1.0D + bb.minY, w0, w0, 1.0D, w1, w1);
            this.southWallShape = clampBox(w0, w0, 0.0D, w1, w1, -bb.minY);
            this.northWallShape = clampBox(w0, w0, 1.0D + bb.minY, w1, w1, 1.0D);
            this.ceilingShape = clampBox(w0, 1.0D + bb.minY, w0, w1, 1.0D, w1);
        } else {
            final double t = 0.125D;
            final double u = 11.0D / 16.0D;
            this.floorShape = clampBox(w0, 0.0D, w0, w1, bb.getYLength() - this.variant.getFloorOffset(), w1);
            this.eastWallShape = clampBox(w0 - t, u + bb.minY, w0, w1 - t, u + bb.maxY, w1);
            this.westWallShape = clampBox(w0 + t, u + bb.minY, w0, w1 + t, u + bb.maxY, w1);
            this.southWallShape = clampBox(w0, u + bb.minY, w0 - t, w1, u + bb.maxY, w1 - t);
            this.northWallShape = clampBox(w0, u + bb.minY, w0 + t, w1, u + bb.maxY, w1 + t);
            this.ceilingShape = clampBox(w0, 1.0D + bb.minY - 4.0D / 16.0D, w0, w1, 1.0D, w1);
        }
        this.setDefaultState(this
                .getStateManager()
                .getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(FACE, WallMountLocation.WALL)
                .with(LIT, true));
    }

    private static VoxelShape clampBox(double x0, double y0, double z0, double x1, double y1, double z1) {
        return VoxelShapes.cuboid(MathHelper.clamp(x0, 0.0D, 1.0D), MathHelper.clamp(y0, 0.0D, 1.0D), MathHelper.clamp(z0, 0.0D, 1.0D),
                MathHelper.clamp(x1, 0.0D, 1.0D), MathHelper.clamp(y1, 0.0D, 1.0D), MathHelper.clamp(z1, 0.0D, 1.0D));
    }

    public LightVariant<?> getVariant() {
        return this.variant;
    }

    @Override
    public BlockEntity createBlockEntity(final BlockPos pos, final BlockState state) {
        return new LightBlockEntity(pos, state);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        final WallMountLocation value = state.get(FACE);
        if (value == WallMountLocation.WALL) {
            final Direction facing = state.get(FACING);
            final BlockPos anchorPos = pos.offset(facing.getOpposite());
            BlockState anchorState = world.getBlockState(anchorPos);
            if (anchorState.isIn(BlockTags.LEAVES)) {
                return true;
            }
            final VoxelShape shape = anchorState.getSidesShape(world, anchorPos);
            return Block.isFaceFullSquare(shape, facing);
        }
        final Direction facing = value == WallMountLocation.FLOOR ? Direction.DOWN : Direction.UP;
        final BlockPos anchorPos = pos.offset(facing);
        BlockState anchorState = world.getBlockState(anchorPos);
        if (anchorState.isIn(BlockTags.LEAVES)) {
            return true;
        }
        final VoxelShape shape = anchorState.getSidesShape(world, anchorPos);
        return !VoxelShapes.matchesAnywhere(shape.getFace(facing.getOpposite()), MIN_ANCHOR_SHAPE, BooleanBiFunction.ONLY_SECOND);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(final ItemPlacementContext context) {
        for (final Direction dir : context.getPlacementDirections()) {
            final BlockState state;
            if (dir.getAxis() == Direction.Axis.Y) {
                state = this.getDefaultState()
                        .with(FACE, dir == Direction.UP ? WallMountLocation.CEILING : WallMountLocation.FLOOR)
                        .with(FACING, context.getHorizontalPlayerFacing().getOpposite());
            } else {
                state = this.getDefaultState()
                        .with(FACE, WallMountLocation.WALL)
                        .with(FACING, dir.getOpposite());
            }
            if (state.canPlaceAt(context.getWorld(), context.getBlockPos())) {
                return state;
            }
        }
        return null;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onPlaced(world, pos, state, placer, stack);
        final BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof LightBlockEntity) {
            final ItemStack lightItem = stack.copy();
            lightItem.setCount(1);
            ((LightBlockEntity) entity).setItemStack(lightItem);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public List<ItemStack> getDroppedStacks(final BlockState state, final LootContextParameterSet.Builder builder) {
        final BlockEntity entity = builder.getOptional(LootContextParameters.BLOCK_ENTITY);
        if (entity instanceof LightBlockEntity) {
            return Collections.singletonList(((LightBlockEntity) entity).getLight().getItem().copy());
        }
        return Collections.emptyList();
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(
            final BlockState state,
            final World world,
            final BlockPos pos,
            final PlayerEntity player,
            final Hand hand,
            final BlockHitResult hit) {
        final BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof LightBlockEntity) {
            ((LightBlockEntity) entity).interact(world, pos, state, player, hand, hit);
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void randomDisplayTick(final BlockState state, final World world, final BlockPos pos, final Random rng) {
        super.randomDisplayTick(state, world, pos, rng);
        final BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof LightBlockEntity) {
            ((LightBlockEntity) entity).animateTick();
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOutlineShape(final BlockState state, final BlockView world, final BlockPos pos, final ShapeContext context) {
        return switch (state.get(FACE)) {
            case WALL -> switch (state.get(FACING)) {
                case WEST -> this.westWallShape;
                case SOUTH -> this.southWallShape;
                case NORTH -> this.northWallShape;
                default -> this.eastWallShape;
            };
            case CEILING -> this.ceilingShape;
            default -> this.floorShape;
        };
    }

    @Override
    public ItemStack getPickStack(
            final BlockView world,
            final BlockPos pos,
            final BlockState state) {
        final BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof LightBlockEntity) {
            return ((LightBlockEntity) entity).getLight().getItem().copy();
        }
        final ItemStack stack = new ItemStack(this);
        DyeableItem.setColor(stack, DyeColor.YELLOW);
        return stack;
    }

    @Override
    protected void appendProperties(final StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACE, FACING, LIT);
    }
}
