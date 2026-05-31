package de.lucalabs.fairylights.main.attachments;

import de.lucalabs.fairylights.Constants;
import de.lucalabs.fairylights.main.blocks.entity.FastenerBlockEntity;
import de.lucalabs.fairylights.main.entity.FenceFastenerEntity;
import de.lucalabs.fairylights.main.fastener.BlockFastener;
import de.lucalabs.fairylights.main.fastener.Fastener;
import de.lucalabs.fairylights.main.fastener.FenceFastener;
import de.lucalabs.fairylights.main.fastener.PlayerFastener;
import de.lucalabs.fairylights.main.fastener.RegularBlockView;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * NeoForge replacement for the Cardinal Components fastener storage used on Fabric. The fastener is
 * stored as a data attachment on the block entity / fence entity / player. A holder-aware default
 * constructs the correct Fastener subtype, and the serializer persists it (NeoForge writes
 * serializable attachments into the holder's save data automatically).
 */
public final class FairyLightsAttachments {

    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Constants.MOD_ID);

    public static final Supplier<AttachmentType<Fastener<?>>> FASTENER = ATTACHMENT_TYPES.register(
            "fastener", () -> AttachmentType.<Fastener<?>>builder(FairyLightsAttachments::createFor)
                    .serialize(new FastenerAttachmentSerializer())
                    .build());

    private FairyLightsAttachments() {
    }

    /**
     * Builds the Fastener for a holder, matching the Fabric component factories. Returns {@code null}
     * for unsupported holders; the key only ever requests the attachment for the three types below.
     */
    @Nullable
    public static Fastener<?> createFor(final IAttachmentHolder holder) {
        if (holder instanceof final FastenerBlockEntity be) {
            return new BlockFastener(be, new RegularBlockView());
        }
        if (holder instanceof final FenceFastenerEntity entity) {
            return new FenceFastener(entity);
        }
        if (holder instanceof final Player player) {
            return new PlayerFastener(player);
        }
        return null;
    }

    public static void init(final IEventBus modBus) {
        ATTACHMENT_TYPES.register(modBus);
    }
}
