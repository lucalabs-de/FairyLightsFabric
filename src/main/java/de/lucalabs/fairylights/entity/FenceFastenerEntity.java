package de.lucalabs.fairylights.entity;

import de.lucalabs.fairylights.blocks.FairyLightBlocks;
import de.lucalabs.fairylights.components.FairyLightComponents;
import de.lucalabs.fairylights.fastener.Fastener;
import de.lucalabs.fairylights.items.ConnectionItem;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class FenceFastenerEntity extends AbstractDecorationEntity {
    private int surfaceCheckTime;

    public FenceFastenerEntity(final EntityType<? extends FenceFastenerEntity> type, final World world) {
        super(type, world);
    }

    public FenceFastenerEntity(final World world) {
        this(FairyLightEntities.FASTENER, world);
    }

    public FenceFastenerEntity(final World world, final BlockPos pos) {
        this(world);
        this.setPosition(pos.getX(), pos.getY(), pos.getZ());
    }

    public static FenceFastenerEntity create(final World world, final BlockPos fence) {
        final FenceFastenerEntity fastener = new FenceFastenerEntity(world, fence);
        //fastener.forceSpawn = true;
        world.spawnEntity(fastener);
        fastener.onPlace();
        return fastener;
    }

    @Nullable
    public static FenceFastenerEntity find(final World world, final BlockPos pos) {
        final AbstractDecorationEntity entity = findHanging(world, pos);
        if (entity instanceof FenceFastenerEntity) {
            return (FenceFastenerEntity) entity;
        }
        return null;
    }

    @Nullable
    public static AbstractDecorationEntity findHanging(final World world, final BlockPos pos) {
        for (final AbstractDecorationEntity e : world.getNonSpectatingEntities(AbstractDecorationEntity.class, new Box(pos).expand(2))) {
            if (e.getDecorationBlockPos().equals(pos)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public int getWidthPixels() {
        return 9;
    }

    @Override
    public int getHeightPixels() {
        return 9;
    }

    @Override
    public float getEyeHeight(final EntityPose pose, final EntityDimensions size) {
        /*
         * Because this entity is inside of a block when
         * EntityLivingBase#canEntityBeSeen performs its
         * raytracing it will always return false during
         * NetHandlerPlayServer#processUseEntity, making
         * the player reach distance be limited at three
         * blocks as opposed to the standard six blocks.
         * EntityLivingBase#canEntityBeSeen will add the
         * value given by getEyeHeight to the y position
         * of the entity to calculate the end point from
         * which to raytrace to. Returning one lets most
         * interactions with a player succeed, typically
         * for breaking the connection or creating a new
         * connection. I hope you enjoy my line lengths.
         */
        return 1;
    }

    @Override
    public boolean shouldRender(final double distance) {
        return distance < 4096;
    }

    @Override
    public boolean isImmuneToExplosion() {
        return true;
    }

    @Override
    public boolean canStayAttached() {
        return !this.getWorld().canSetBlock(this.attachmentPos) || ConnectionItem.isFence(this.getWorld().getBlockState(this.attachmentPos));
    }

    @Override
    public void remove(final RemovalReason reason) {
        this.getFastener().ifPresent(Fastener::remove);
        super.remove(reason);
    }

    // Copy from super but remove() moved to after onBroken()
    @Override
    public boolean damage(final DamageSource source, final float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        if (!this.getWorld().isClient() && this.isAlive()) {
            this.scheduleVelocityUpdate();
            this.onBreak(source.getAttacker());
            this.remove(RemovalReason.KILLED);
        }
        return true;
    }

    @Override
    public boolean canUsePortals() {
        return false;
    }

    @Override
    public void onBreak(@Nullable final Entity breaker) {
        this.getFastener().ifPresent(fastener -> fastener.dropItems(this.getWorld(), this.attachmentPos));
        if (breaker != null) {
            this.getWorld().syncWorldEvent(2001, this.attachmentPos, Block.getRawIdFromState(FairyLightBlocks.FASTENER.getDefaultState()));
        }
    }

    @Override
    public void onPlace() {
        final BlockSoundGroup sound = FairyLightBlocks.FASTENER
                .getSoundGroup(FairyLightBlocks.FASTENER.getDefaultState()); //, this.getWorld(), this.getPos(), null);
        this.playSound(sound.getPlaceSound(), (sound.getVolume() + 1) / 2, sound.getPitch() * 0.8F);
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.BLOCKS;
    }

    @Override
    public void setPosition(final double x, final double y, final double z) {
        super.setPosition(MathHelper.floor(x) + 0.5, MathHelper.floor(y) + 0.5, MathHelper.floor(z) + 0.5);
    }

    @Override
    public void setFacing(final Direction facing) {
    }

    @Override
    protected void updateAttachmentPosition() {
        final double posX = this.attachmentPos.getX() + 0.5;
        final double posY = this.attachmentPos.getY() + 0.5;
        final double posZ = this.attachmentPos.getZ() + 0.5;
        this.setPos(posX, posY, posZ);
        final float w = 3 / 16F;
        final float h = 3 / 16F;
        this.setBoundingBox(new Box(posX - w, posY - h, posZ - w, posX + w, posY + h, posZ + w));
    }

    @Override
    public Box getVisibilityBoundingBox() {
        return this.getFastener().map(fastener -> fastener.getBounds().expand(1)).orElseGet(super::getVisibilityBoundingBox);
    }

    @Override
    public void tick() {
        this.getFastener().ifPresent(fastener -> {
            if (!this.getWorld().isClient() && (fastener.hasNoConnections() || this.checkSurface())) {
                this.onBreak(null);
                this.remove(RemovalReason.DISCARDED);
            } else if (fastener.update() && !this.getWorld().isClient()) {
                // TODO probably not needed because of auto syncing
//                final UpdateEntityFastenerMessage msg = new UpdateEntityFastenerMessage(this, fastener);
//                FilteredServerPlayNetworking.sendToPlayersWatchingEntity(this, UpdateEntityFastenerMessage.ID, msg);
                // TODO I think this is needed instead though?
                FairyLightComponents.FASTENER.sync(this);
            }
        });
    }

    private boolean checkSurface() {
        if (this.surfaceCheckTime++ == 100) {
            this.surfaceCheckTime = 0;
            return !this.canStayAttached();
        }
        return false;
    }

    @Override
    public ActionResult interact(final PlayerEntity player, final Hand hand) {
        final ItemStack stack = player.getStackInHand(hand);
        if (stack.getItem() instanceof ConnectionItem) {
            if (this.getWorld().isClient()) {
                player.swingHand(hand);
            } else {
                this.getFastener().ifPresent(fastener -> ((ConnectionItem) stack.getItem()).connect(stack, player, this.getWorld(), fastener));
            }
            return ActionResult.SUCCESS;
        }
        return super.interact(player, hand);
    }

    @Override
    public void writeCustomDataToNbt(final NbtCompound compound) {
        compound.put("pos", NbtHelper.fromBlockPos(this.attachmentPos));
    }

    @Override
    public void readCustomDataFromNbt(final NbtCompound compound) {
        this.attachmentPos = NbtHelper.toBlockPos(compound.getCompound("pos"));
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    // TODO check if this breaks something. It is supposed to replace the setShouldReceiveVelocityUpdates(false) in EntityType registration
    @Override
    public boolean hasNoGravity() {
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
        return FairyLightComponents.FASTENER.get(this).get();
    }
}
