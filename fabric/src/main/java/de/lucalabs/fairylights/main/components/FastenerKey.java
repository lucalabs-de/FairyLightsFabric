package de.lucalabs.fairylights.main.components;

import de.lucalabs.fairylights.Common;
import de.lucalabs.fairylights.main.fastener.Fastener;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.ComponentAccess;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;

public class FastenerKey implements Key<Fastener<?>> {
    public static final ResourceLocation FASTENER_ID = Common.id("fastener");
    public static final ComponentKey<FastenerComponent> KEY =
            ComponentRegistry.getOrCreate(FASTENER_ID, FastenerComponent.class);

    @Override
    public @Nullable Fastener<?> tryGetFor(Object o) {
        if (! (o instanceof ComponentAccess)) {
            return null;
        }

        return KEY.maybeGet(o).flatMap(GenericComponent::get).orElse(null);
    }

    public void syncFor(Object o) {
        if (! (o instanceof ComponentAccess)) {
            return;
        }
        KEY.sync(o);
    }
}
