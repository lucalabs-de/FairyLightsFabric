package de.lucalabs.fairylights.components;

import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.blocks.entity.FastenerBlockEntity;
import de.lucalabs.fairylights.entity.FenceFastenerEntity;
import de.lucalabs.fairylights.fastener.BlockFastener;
import de.lucalabs.fairylights.fastener.FenceFastener;
import de.lucalabs.fairylights.fastener.PlayerFastener;
import de.lucalabs.fairylights.fastener.RegularBlockView;
import dev.onyxstudios.cca.api.v3.block.BlockComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.block.BlockComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class FairyLightComponents implements EntityComponentInitializer, BlockComponentInitializer {

    public static final Identifier FASTENER_ID = Identifier.of(FairyLights.ID, "fastener");
    public static final ComponentKey<FastenerComponent> FASTENER =
            ComponentRegistry.getOrCreate(FASTENER_ID, FastenerComponent.class);

    public static final Identifier LIGHT_VARIANT_ID = Identifier.of(FairyLights.ID, "fastener");
    public static final ComponentKey<FastenerComponent> LIGHT_VARIANT =
            ComponentRegistry.getOrCreate(LIGHT_VARIANT_ID, FastenerComponent.class);

    @Override
    public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {
        registry.registerFor(
                FastenerBlockEntity.class,
                FASTENER,
                be -> FastenerComponent.DEFAULT.setFastener(new BlockFastener(be, new RegularBlockView())));
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(PlayerEntity.class, FASTENER, e -> FastenerComponent.DEFAULT.setFastener(new PlayerFastener(e)));
        registry.registerFor(FenceFastenerEntity.class, FASTENER, e -> FastenerComponent.DEFAULT.setFastener(new FenceFastener(e)));
    }
}
