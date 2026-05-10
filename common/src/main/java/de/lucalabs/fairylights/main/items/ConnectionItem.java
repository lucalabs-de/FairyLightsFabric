package de.lucalabs.fairylights.main.items;

import de.lucalabs.fairylights.main.blocks.FairyLightBlocks;
import de.lucalabs.fairylights.main.blocks.FastenerBlock;
import de.lucalabs.fairylights.main.connection.Connection;
import de.lucalabs.fairylights.main.connection.ConnectionType;
import de.lucalabs.fairylights.main.entity.FenceFastenerEntity;
import de.lucalabs.fairylights.main.fastener.Fastener;
import de.lucalabs.fairylights.main.items.components.ComponentRecords;
import de.lucalabs.fairylights.main.sounds.FairyLightSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

import static de.lucalabs.fairylights.platform.Services.COMPONENTS;
import static de.lucalabs.fairylights.platform.Services.KEYS;

public abstract class ConnectionItem extends Item {
    private final ConnectionType<?> type;

    public ConnectionItem(final Properties properties, final ConnectionType<?> type) {
        super(properties);
        this.type = type;
    }

    @SuppressWarnings("deprecation")
    public static boolean isFence(final BlockState state) {
        return state.isSolid() && state.is(BlockTags.FENCES);
    }

    public final ConnectionType<?> getConnectionType() {
        return this.type;
    }

    @Override
    public InteractionResult useOn(final UseOnContext context) {
        final Player user = context.getPlayer();
        if (user == null) {
            return super.useOn(context);
        }

        final Level world = context.getLevel();
        final Direction side = context.getClickedFace();
        final BlockPos clickPos = context.getClickedPos();
        final ItemStack stack = context.getItemInHand();

        if (this.isConnectionInOtherHand(world, user, stack)) {
            return InteractionResult.PASS;
        }

        final BlockState fastenerState = FairyLightBlocks.FASTENER
                .defaultBlockState()
                .setValue(FastenerBlock.FACING, side);

        final BlockState currentBlockState = world.getBlockState(clickPos);
        final BlockPlaceContext blockContext = new BlockPlaceContext(context);
        final BlockPos placePos = blockContext.getClickedPos();

        if (currentBlockState.getBlock() == FairyLightBlocks.FASTENER) {
            if (!world.isClientSide()) {
                this.connect(stack, user, world, clickPos);
            }
            return InteractionResult.SUCCESS;
        } else if (blockContext.canPlace() && fastenerState.canSurvive(world, placePos)) {
            if (!world.isClientSide()) {
                this.connect(stack, user, world, placePos, fastenerState);
            }
            return InteractionResult.SUCCESS;
        } else if (isFence(currentBlockState)) {
            final HangingEntity entity = FenceFastenerEntity.findHanging(world, clickPos);
            if (entity == null || entity instanceof FenceFastenerEntity) {
                if (!world.isClientSide()) {
                    this.connectFence(stack, user, world, clickPos, (FenceFastenerEntity) entity);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    private boolean isConnectionInOtherHand(final Level world, final Player user, final ItemStack stack) {
        final Fastener<?> attacher = COMPONENTS.getOrThrow(user, KEYS.FASTENER(), IllegalStateException::new);
        return attacher.getFirstConnection().filter(connection -> !connection.serializeLogic().build().matchesItemStack(stack)).isPresent();
    }

    private void connect(final ItemStack stack, final Player user, final Level world, final BlockPos pos) {
        final BlockEntity entity = world.getBlockEntity(pos);
        if (entity != null) {
            COMPONENTS.maybeGet(user, KEYS.FASTENER()).ifPresent(fastener -> this.connect(stack, user, world, fastener));
        }
    }

    private void connect(final ItemStack stack, final Player user, final Level world, final BlockPos pos, final BlockState state) {
        if (world.setBlock(pos, state, 3)) {
            state.getBlock().setPlacedBy(world, pos, state, user, stack);
            final SoundType sound = state.getSoundType();
            world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    sound.getPlaceSound(),
                    SoundSource.BLOCKS,
                    (sound.getVolume() + 1) / 2,
                    sound.getPitch() * 0.8F
            );
            final BlockEntity entity = world.getBlockEntity(pos);
            if (entity != null) {
                COMPONENTS.maybeGet(entity, KEYS.FASTENER())
                        .ifPresent(destination -> this.connect(stack, user, world, destination, false));
            }
        }
    }

    public void connect(final ItemStack stack, final Player user, final Level world, final Fastener<?> fastener) {
        this.connect(stack, user, world, fastener, true);
    }

    public void connect(final ItemStack stack, final Player user, final Level world, final Fastener<?> fastener, final boolean playConnectSound) {
        COMPONENTS.maybeGet(user, KEYS.FASTENER()).ifPresent(attacher -> {
            boolean playSound = playConnectSound;
            final Optional<Connection> placing = attacher.getFirstConnection();
            if (placing.isPresent()) {
                final Connection conn = placing.get();
                if (conn.reconnect(fastener)) {
                    conn.onConnect(world, user, stack);
                    stack.shrink(1);
                } else {
                    playSound = false;
                }

                attacher.removeConnection(placing.get());
            } else {
                fastener.connect(world, attacher, this.getConnectionType(), ComponentRecords.ConnectionLogic.fromItemStack(stack), false);
            }
            if (playSound) {
                final Vec3 pos = fastener.getConnectionPoint();
                world.playSound(null, pos.x, pos.y, pos.z, FairyLightSounds.CORD_CONNECT, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        });
    }

    private void connectFence(
            final ItemStack stack,
            final Player user,
            final Level world,
            final BlockPos pos,
            FenceFastenerEntity fastener) {
        final boolean playConnectSound;
        if (fastener == null) {
            fastener = FenceFastenerEntity.create(world, pos);
            playConnectSound = false;
        } else {
            playConnectSound = true;
        }

        this.connect(
                stack,
                user,
                world,
                COMPONENTS.getOrThrow(fastener, KEYS.FASTENER(), IllegalStateException::new),
                playConnectSound);
    }
}
