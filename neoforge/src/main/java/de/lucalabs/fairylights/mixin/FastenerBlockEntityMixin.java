package de.lucalabs.fairylights.mixin;

import de.lucalabs.fairylights.main.blocks.entity.FastenerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

/**
 * The fastener is stored as a data attachment. NeoForge writes serializable attachments into the
 * block entity's save data, but {@link BlockEntity#getUpdateTag} returns an empty tag by default, so
 * the attachment would never reach clients. Returning the full custom-data tag (which includes
 * "neoforge:attachments") lets the existing chunk-load and {@code sendBlockUpdated} paths sync the
 * fastener; the client applies it via {@code loadWithComponents}. This replaces the automatic block
 * sync Cardinal Components provides on Fabric.
 */
@Mixin(FastenerBlockEntity.class)
public abstract class FastenerBlockEntityMixin extends BlockEntity {

    private FastenerBlockEntityMixin(final BlockEntityType<?> type, final BlockPos pos, final BlockState state) {
        super(type, pos, state);
    }

    @Override
    public CompoundTag getUpdateTag(final HolderLookup.Provider registries) {
        return this.saveCustomOnly(registries);
    }
}
