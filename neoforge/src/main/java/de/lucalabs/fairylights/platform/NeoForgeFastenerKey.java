package de.lucalabs.fairylights.platform;

import de.lucalabs.fairylights.main.attachments.FairyLightsAttachments;
import de.lucalabs.fairylights.main.blocks.entity.FastenerBlockEntity;
import de.lucalabs.fairylights.main.components.Key;
import de.lucalabs.fairylights.main.entity.FenceFastenerEntity;
import de.lucalabs.fairylights.main.fastener.Fastener;
import de.lucalabs.fairylights.main.net.FastenerSyncPayload;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

public class NeoForgeFastenerKey implements Key<Fastener<?>> {

    @Override
    public @Nullable Fastener<?> tryGetFor(final Object o) {
        if (o instanceof FastenerBlockEntity || o instanceof FenceFastenerEntity || o instanceof Player) {
            // getData lazily constructs the holder-specific Fastener (see FairyLightsAttachments#createFor),
            // matching the lazy component creation Cardinal Components performs on Fabric.
            return ((IAttachmentHolder) o).getData(FairyLightsAttachments.FASTENER.get());
        }
        return null;
    }

    /**
     * Block entities sync through the block-entity update tag (see FastenerBlockEntityMixin); entities
     * and players have no NBT live-sync, so push the fastener to tracking clients with a payload.
     */
    public void syncFor(final Object o) {
        if (o instanceof final Entity entity && entity.level() instanceof ServerLevel) {
            final Fastener<?> fastener = this.tryGetFor(o);
            if (fastener != null) {
                final CompoundTag tag = new CompoundTag();
                fastener.writeToNbt(tag);
                PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, new FastenerSyncPayload(entity.getId(), tag));
            }
        }
    }
}
