package de.lucalabs.fairylights.main.entity;

import de.lucalabs.fairylights.main.blocks.FairyLightBlocks;
import de.lucalabs.fairylights.main.fastener.Fastener;
import de.lucalabs.fairylights.main.items.ConnectionItem;
import de.lucalabs.fairylights.platform.Services;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.phys.AABB;

public final class FenceFastenerEntity extends HangingEntity {
    private int surfaceCheckTime;

    public FenceFastenerEntity(final EntityType<? extends FenceFastenerEntity> type, final Level world) {
        super(type, world);
    }

    public FenceFastenerEntity(final Level world) {
        this(FairyLightEntities.FASTENER, world);
    }

    public FenceFastenerEntity(final Level world, final BlockPos pos) {
        this(world);
        this.setPos(pos.getX(), pos.getY(), pos.getZ());
    }

    public static FenceFastenerEntity create(final Level world, final BlockPos fence) {
        final FenceFastenerEntity fastener = new FenceFastenerEntity(world, fence);
        //fastener.forceSpawn = true;
        world.addFreshEntity(fastener);
        fastener.playPlacementSound();
        return fastener;
    }

    @Nullable
    public static FenceFastenerEntity find(final Level world, final BlockPos pos) {
        final HangingEntity entity = findHanging(world, pos);
        if (entity instanceof FenceFastenerEntity) {
            return (FenceFastenerEntity) entity;
        }
        return null;
    }

    @Nullable
    public static HangingEntity findHanging(final Level world, final BlockPos pos) {
        for (final HangingEntity e : world.getEntitiesOfClass(HangingEntity.class, new AABB(pos).inflate(2))) {
            if (e.getPos().equals(pos)) {
                return e;
            }
        }
        return null;
    }

//    @Override
//    public int getWidthPixels() {
//        return 9;
//    }
//
//    @Override
//    public int getHeightPixels() {
//        return 9;
//    }

    @Override
    public boolean shouldRenderAtSqrDistance(final double distance) {
        return distance < 4096;
    }

    @Override
    public boolean canUsePortal(boolean allowVehicles) {
        return false;
    }

    @Override
    public boolean ignoreExplosion(Explosion exp) {
        return true;
    }

    @Override
    public boolean survives() {
        return !this.level().isLoaded(this.pos) || ConnectionItem.isFence(this.level().getBlockState(this.pos));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    public void remove(final RemovalReason reason) {
        this.getFastener().ifPresent(Fastener::remove);
        super.remove(reason);
    }

    // Copy from super but remove() moved to after onBroken()
    @Override
    public boolean hurt(final DamageSource source, final float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        if (!this.level().isClientSide() && this.isAlive()) {
            this.markHurt();
            this.dropItem(source.getEntity());
            this.remove(RemovalReason.KILLED);
        }
        return true;
    }

    @Override
    public void dropItem(@Nullable final Entity breaker) {
        this.getFastener().ifPresent(fastener -> fastener.dropItems(this.level(), this.pos));
        if (breaker != null) {
            this.level().levelEvent(2001, this.pos, Block.getId(FairyLightBlocks.FASTENER.defaultBlockState()));
        }
    }

    @Override
    public void playPlacementSound() {
        final SoundType sound = FairyLightBlocks.FASTENER.defaultBlockState().getSoundType();
        this.playSound(sound.getPlaceSound(), (sound.getVolume() + 1) / 2, sound.getPitch() * 0.8F);
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.BLOCKS;
    }

    @Override
    public void setPos(final double x, final double y, final double z) {
        super.setPos(Mth.floor(x) + 0.5, Mth.floor(y) + 0.5, Mth.floor(z) + 0.5);
    }

    @Override
    public void setDirection(final Direction facing) {
    }

//    @Override
//    protected void updateAttachmentPosition() {
//        final double posX = this.attachedBlockPos.getX() + 0.5;
//        final double posY = this.attachedBlockPos.getY() + 0.5;
//        final double posZ = this.attachedBlockPos.getZ() + 0.5;
//        this.setPos(posX, posY, posZ);
//        final float w = 3 / 16F;
//        final float h = 3 / 16F;
//        this.setBoundingBox(new Box(posX - w, posY - h, posZ - w, posX + w, posY + h, posZ + w));
//    }

    // TODO verify that this is fine
    @Override
    protected @NotNull AABB calculateBoundingBox(@NotNull BlockPos pos, @NotNull Direction side) {
        final double posX = this.pos.getX() + 0.5;
        final double posY = this.pos.getY() + 0.5;
        final double posZ = this.pos.getZ() + 0.5;
        this.setPosRaw(posX, posY, posZ);
        final float w = 3 / 16F;
        final float h = 3 / 16F;
        return new AABB(posX - w, posY - h, posZ - w, posX + w, posY + h, posZ + w);
    }

    @Override
    public @NotNull AABB getBoundingBoxForCulling() {
        return this.getFastener().map(fastener -> fastener.getBounds().inflate(1)).orElseGet(super::getBoundingBoxForCulling);
    }

    @Override
    public void tick() {
        this.getFastener().ifPresent(fastener -> {
            if (!this.level().isClientSide() && (fastener.hasNoConnections() || this.checkSurface())) {
                this.dropItem(null);
                this.remove(RemovalReason.DISCARDED);
            } else if (fastener.update() && !this.level().isClientSide()) {
                // TODO probably not needed because of auto syncing
//                final UpdateEntityFastenerMessage msg = new UpdateEntityFastenerMessage(this, fastener);
//                FilteredServerPlayNetworking.sendToPlayersWatchingEntity(this, UpdateEntityFastenerMessage.ID, msg);
                // TODO I think this is needed instead though?
                Services.COMPONENTS.sync(this, Services.KEYS.FASTENER());
            }
        });
    }

    private boolean checkSurface() {
        if (this.surfaceCheckTime++ == 100) {
            this.surfaceCheckTime = 0;
            return !this.survives();
        }
        return false;
    }

    @Override
    public @NotNull InteractionResult interact(final Player player, final @NotNull InteractionHand hand) {
        final ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof ConnectionItem) {
            if (this.level().isClientSide()) {
                player.swing(hand);
            } else {
                this.getFastener().ifPresent(fastener -> ((ConnectionItem) stack.getItem()).connect(stack, player, this.level(), fastener));
            }
            return InteractionResult.SUCCESS;
        }
        return super.interact(player, hand);
    }

    // TODO seems to be implemented in superclass now, verify that everything still works as desired.
//    @Override
//    public void writeCustomDataToNbt(final NbtCompound compound) {
//        compound.put("pos", NbtHelper.fromBlockPos(this.attachedBlockPos));
//    }
//
//    @Override
//    public void readCustomDataFromNbt(final NbtCompound compound) {
//        this.attachedBlockPos = NbtHelper.toBlockPos(compound.getCompound("pos"));
//    }

    // TODO probably also no longer needed? Verify.
//    @Override
//    public Packet<ClientPlayPacketListener> createSpawnPacket() {
//        return new EntitySpawnS2CPacket(this);
//    }

    // TODO check if this breaks something. It is supposed to replace the setShouldReceiveVelocityUpdates(false) in EntityType registration
    @Override
    public boolean isNoGravity() {
        return true;
    }

    // TODO this should not be needed, as the components api does serialization and sychronisation, verify that this is true
//    @Override
//    public void writeCustomDataToNbt(NbtCompound nbt) {
//        this.getFastener().ifPresent(fastener -> {
//           fastener.writeToNbt(nbt);
//        });
//    }
//
//    @Override
//    public void readCustomDataFromNbt(NbtCompound nbt) {
//        this.getFastener().ifPresent(fastener -> {
//            fastener.readFromNbt(nbt);
//        });
//    }

    private Optional<Fastener<?>> getFastener() {
        return Services.COMPONENTS.maybeGet(this, Services.KEYS.FASTENER());
    }
}
