package de.lucalabs.fairylights.blocks.entity;

import de.lucalabs.fairylights.blocks.LightBlock;
import de.lucalabs.fairylights.feature.light.Light;
import de.lucalabs.fairylights.items.LightVariant;
import de.lucalabs.fairylights.items.SimpleLightVariant;
import de.lucalabs.fairylights.sounds.FairyLightSounds;
import de.lucalabs.fairylights.util.MathHelper;
import de.lucalabs.fairylights.util.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.BlockFace;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Objects;

import static de.lucalabs.fairylights.items.components.FairyLightItemComponents.LIGHT_VARIANT;

public class LightBlockEntity extends BlockEntity {
    private Light<?> light;

    private boolean on = true;

    public LightBlockEntity(BlockPos pos, BlockState state) {
        super(FairyLightBlockEntities.LIGHT, pos, state);
        this.light = new Light<>(0, Vec3d.ZERO, 0.0F, 0.0F, ItemStack.EMPTY, SimpleLightVariant.FAIRY_LIGHT, 0.0F);
    }

    public Light<?> getLight() {
        return this.light;
    }

    public void setItemStack(final ItemStack stack) {
        this.light = new Light<>(0, Vec3d.ZERO, 0.0F, 0.0F, stack, Objects.requireNonNullElse(LightVariant.getLightVariant(stack.get(LIGHT_VARIANT)), SimpleLightVariant.FAIRY_LIGHT), 0.0F);
        this.markDirty();
    }

    private void setOn(final boolean on) {
        this.on = on;
        this.light.power(on, true);
        this.markDirty();
    }

    public void interact(final World world, final BlockPos pos, final BlockState state, final PlayerEntity player, final BlockHitResult hit) {
        this.setOn(!this.on);
        world.setBlockState(pos, state.with(LightBlock.LIT, this.on));
        final SoundEvent lightSnd;
        final float pitch;
        if (this.on) {
            lightSnd = FairyLightSounds.FEATURE_LIGHT_TURNON;
            pitch = 0.6F;
        } else {
            lightSnd = FairyLightSounds.FEATURE_LIGHT_TURNOFF;
            pitch = 0.5F;
        }
        this.world.playSound(null, pos, lightSnd, SoundCategory.BLOCKS, 1.0F, pitch);
    }

    public void animateTick() {
        final BlockState state = this.getCachedState();
        final BlockFace face = state.get(LightBlock.FACE);
        final float rotation = state.get(LightBlock.FACING).asRotation();
        final MatrixStack matrix = new MatrixStack();
        matrix.translate(0.5F, 0.5F, 0.5F);
        matrix.rotate((float) Math.toRadians(180.0F - rotation), 0.0F, 1.0F, 0.0F);
        if (this.light.getVariant().isOrientable()) {
            if (face == BlockFace.WALL) {
                matrix.rotate(MathHelper.HALF_PI, 1.0F, 0.0F, 0.0F);
            } else if (face == BlockFace.FLOOR) {
                matrix.rotate(-MathHelper.PI, 1.0F, 0.0F, 0.0F);
            }
            matrix.translate(0.0F, 0.5F, 0.0F);
        } else {
            if (face == BlockFace.CEILING) {
                matrix.translate(0.0F, 0.25F, 0.0F);
            } else if (face == BlockFace.WALL) {
                matrix.translate(0.0F, 3.0F / 16.0F, 0.125F);
            } else {
                matrix.translate(0.0F, -(float) this.light.getVariant().getBounds().minY - 0.5F, 0.0F);
            }
        }
        this.light.getBehavior().animateTick(this.world, Vec3d.of(this.pos).add(matrix.transform(Vec3d.ZERO)), this.light);
    }

    @Override
    protected void writeNbt(NbtCompound compound, RegistryWrapper.WrapperLookup wrapper) {
        super.writeNbt(compound, wrapper);
        compound.put("item", this.light.getItem().encode(wrapper));
        compound.putBoolean("on", this.on);
    }

    @Override
    public void readNbt(NbtCompound compound, RegistryWrapper.WrapperLookup wrapper) {
        super.readNbt(compound, wrapper);
        ItemStack.fromNbt(wrapper, compound.getCompound("item")).ifPresent(this::setItemStack);
        this.setOn(compound.getBoolean("on"));
    }
}
