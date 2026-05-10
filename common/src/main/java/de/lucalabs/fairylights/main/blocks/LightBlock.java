package de.lucalabs.fairylights.main.blocks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.lucalabs.fairylights.main.blocks.entity.LightBlockEntity;
import de.lucalabs.fairylights.main.items.DyeableItem;
import de.lucalabs.fairylights.main.items.LightVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class LightBlock extends FaceAttachedHorizontalDirectionalBlock implements EntityBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    private static final VoxelShape MIN_ANCHOR_SHAPE
            = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 16.0D, 9.0D);

    private final VoxelShape floorShape, eastWallShape, westWallShape, northWallShape, southWallShape, ceilingShape;

    private final LightVariant<?> variant;

    public LightBlock(final Properties properties, final LightVariant<?> variant) {
        super(properties);
        this.variant = variant;
        final AABB bb = this.variant.getBounds();
        final double w = Math.max(bb.getXsize(), bb.getZsize());
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
            this.floorShape = clampBox(w0, 0.0D, w0, w1, bb.getYsize() - this.variant.getFloorOffset(), w1);
            this.eastWallShape = clampBox(w0 - t, u + bb.minY, w0, w1 - t, u + bb.maxY, w1);
            this.westWallShape = clampBox(w0 + t, u + bb.minY, w0, w1 + t, u + bb.maxY, w1);
            this.southWallShape = clampBox(w0, u + bb.minY, w0 - t, w1, u + bb.maxY, w1 - t);
            this.northWallShape = clampBox(w0, u + bb.minY, w0 + t, w1, u + bb.maxY, w1 + t);
            this.ceilingShape = clampBox(w0, 1.0D + bb.minY - 4.0D / 16.0D, w0, w1, 1.0D, w1);
        }
        this.registerDefaultState(this
                .getStateDefinition()
                .any()
                .setValue(FACING, Direction.NORTH)
                .setValue(FACE, AttachFace.WALL)
                .setValue(LIT, true));
    }

    private static VoxelShape clampBox(double x0, double y0, double z0, double x1, double y1, double z1) {
        return Shapes.box(Mth.clamp(x0, 0.0D, 1.0D), Mth.clamp(y0, 0.0D, 1.0D), Mth.clamp(z0, 0.0D, 1.0D),
                Mth.clamp(x1, 0.0D, 1.0D), Mth.clamp(y1, 0.0D, 1.0D), Mth.clamp(z1, 0.0D, 1.0D));
    }

    public LightVariant<?> getVariant() {
        return this.variant;
    }

    @Override
    public BlockEntity newBlockEntity(final BlockPos pos, final BlockState state) {
        return new LightBlockEntity(pos, state);
    }

    @Override
    protected MapCodec<? extends FaceAttachedHorizontalDirectionalBlock> codec() {
        // TODO check if this is valid
        return RecordCodecBuilder.<LightBlock>mapCodec(i -> i.group(
                ResourceLocation.CODEC.fieldOf("variant").forGetter(x -> this.variant.getId())
        ).apply(i, c -> new LightBlock(Properties.of(), LightVariant.getLightVariant(c))));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        final AttachFace value = state.getValue(FACE);
        if (value == AttachFace.WALL) {
            final Direction facing = state.getValue(FACING);
            final BlockPos anchorPos = pos.relative(facing.getOpposite());
            BlockState anchorState = world.getBlockState(anchorPos);
            if (anchorState.is(BlockTags.LEAVES)) {
                return true;
            }
            final VoxelShape shape = anchorState.getBlockSupportShape(world, anchorPos);
            return Block.isFaceFull(shape, facing);
        }
        final Direction facing = value == AttachFace.FLOOR ? Direction.DOWN : Direction.UP;
        final BlockPos anchorPos = pos.relative(facing);
        BlockState anchorState = world.getBlockState(anchorPos);
        if (anchorState.is(BlockTags.LEAVES)) {
            return true;
        }
        final VoxelShape shape = anchorState.getBlockSupportShape(world, anchorPos);
        return !Shapes.joinIsNotEmpty(shape.getFaceShape(facing.getOpposite()), MIN_ANCHOR_SHAPE, BooleanOp.ONLY_SECOND);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext context) {
        for (final Direction dir : context.getNearestLookingDirections()) {
            final BlockState state;
            if (dir.getAxis() == Direction.Axis.Y) {
                state = this.defaultBlockState()
                        .setValue(FACE, dir == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR)
                        .setValue(FACING, context.getHorizontalDirection().getOpposite());
            } else {
                state = this.defaultBlockState()
                        .setValue(FACE, AttachFace.WALL)
                        .setValue(FACING, dir.getOpposite());
            }
            if (state.canSurvive(context.getLevel(), context.getClickedPos())) {
                return state;
            }
        }
        return null;
    }

    @Override
    public void setPlacedBy(@NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(world, pos, state, placer, stack);
        final BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof LightBlockEntity) {
            final ItemStack lightItem = stack.copy();
            lightItem.setCount(1);
            ((LightBlockEntity) entity).setItemStack(lightItem);
        }
    }

    @Override
    public List<ItemStack> getDrops(final BlockState state, final LootParams.Builder builder) {
        final BlockEntity entity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (entity instanceof LightBlockEntity) {
            return Collections.singletonList(((LightBlockEntity) entity).getLight().getItem().copy());
        }
        return Collections.emptyList();
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(
            final @NotNull BlockState state,
            final Level world,
            final @NotNull BlockPos pos,
            final @NotNull Player player,
            final @NotNull BlockHitResult hit) {
        final BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof LightBlockEntity) {
            ((LightBlockEntity) entity).interact(world, pos, state, player, hit);
            return InteractionResult.SUCCESS;
        }
        return super.useWithoutItem(state, world, pos, player, hit);
    }

    @Override
    public void animateTick(final @NotNull BlockState state, final @NotNull Level world, final @NotNull BlockPos pos, final RandomSource rng) {
        super.animateTick(state, world, pos, rng);
        final BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof LightBlockEntity) {
            ((LightBlockEntity) entity).animateTick();
        }
    }

    @Override
    public @NotNull VoxelShape getShape(final BlockState state, final @NotNull BlockGetter world, final @NotNull BlockPos pos, final CollisionContext context) {
        return switch (state.getValue(FACE)) {
            case WALL -> switch (state.getValue(FACING)) {
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
    public @NotNull ItemStack getCloneItemStack(
            final LevelReader world,
            final @NotNull BlockPos pos,
            final @NotNull BlockState state) {
        final BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof LightBlockEntity) {
            return ((LightBlockEntity) entity).getLight().getItem().copy();
        }
        final ItemStack stack = new ItemStack(this);
        DyeableItem.setColor(stack, DyeColor.YELLOW);
        return stack;
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACE, FACING, LIT);
    }
}
