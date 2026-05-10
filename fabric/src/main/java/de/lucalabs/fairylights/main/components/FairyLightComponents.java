package de.lucalabs.fairylights.main.components;

import de.lucalabs.fairylights.main.blocks.entity.FastenerBlockEntity;
import de.lucalabs.fairylights.main.entity.FenceFastenerEntity;
import de.lucalabs.fairylights.main.fastener.BlockFastener;
import de.lucalabs.fairylights.main.fastener.FenceFastener;
import de.lucalabs.fairylights.main.fastener.PlayerFastener;
import de.lucalabs.fairylights.main.fastener.RegularBlockView;
import net.minecraft.world.entity.player.Player;
import org.ladysnake.cca.api.v3.block.BlockComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.block.BlockComponentInitializer;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;

public class FairyLightComponents implements EntityComponentInitializer, BlockComponentInitializer {

    @Override
    public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {
        registry.registerFor(
                FastenerBlockEntity.class,
                FastenerKey.KEY,
                be -> new FastenerComponent().setFastener(new BlockFastener(be, new RegularBlockView())));
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(Player.class, FastenerKey.KEY, e -> new FastenerComponent().setFastener(new PlayerFastener(e)));
        registry.registerFor(FenceFastenerEntity.class, FastenerKey.KEY, e -> new FastenerComponent().setFastener(new FenceFastener(e)));
    }
}
