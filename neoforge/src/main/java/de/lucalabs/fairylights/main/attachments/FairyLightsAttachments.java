package de.lucalabs.fairylights.main.attachments;

import com.mojang.serialization.Codec;
import de.lucalabs.fairylights.Constants;
import de.lucalabs.fairylights.main.fastener.Fastener;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class FairyLightsAttachments {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Constants.MOD_ID);

    private static final Supplier<AttachmentType<Fastener<?>>> FASTENER = ATTACHMENT_TYPES.register(
            "fastener", () -> AttachmentType.builder(() -> null).serialize(Codec.INT.fieldOf("fastener")).build()
    );

    public static void init(IEventBus modBus) {
        ATTACHMENT_TYPES.register(modBus);
    }
}
