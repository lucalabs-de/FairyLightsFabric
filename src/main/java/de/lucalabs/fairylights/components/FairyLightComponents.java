package de.lucalabs.fairylights.components;

import com.mojang.serialization.Codec;
import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.blocks.entity.FastenerBlockEntity;
import de.lucalabs.fairylights.entity.FenceFastenerEntity;
import de.lucalabs.fairylights.fastener.BlockFastener;
import de.lucalabs.fairylights.fastener.FenceFastener;
import de.lucalabs.fairylights.fastener.PlayerFastener;
import de.lucalabs.fairylights.fastener.RegularBlockView;
import net.minecraft.component.Component;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.block.BlockComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.block.BlockComponentInitializer;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;

import java.util.List;

public class FairyLightComponents implements EntityComponentInitializer, BlockComponentInitializer {

    public static final Identifier FASTENER_ID = Identifier.of(FairyLights.ID, "fastener");
    public static final ComponentKey<FastenerComponent> FASTENER =
            ComponentRegistry.getOrCreate(FASTENER_ID, FastenerComponent.class);

    public static final ComponentType<NbtCompound> RAW_NBT = fromCodec(NbtCompound.CODEC);

    public static class Lights {
        public static final ComponentType<Boolean> TWINKLE = fromCodec(Codec.BOOL);
    }

    public static class Pennants {
    }

    public static class Connection {
        public static final ComponentType<String> STRING = fromCodec(Codec.STRING);
        public static final ComponentType<List<ItemStack>> PATTERN = fromCodec(ItemStack.CODEC.listOf());
    }

    public static class Dyeable {
        public static final ComponentType<List<Integer>> COLORS = fromCodec(Codec.INT.listOf());
        public static final ComponentType<Integer> COLOR = fromCodec(Codec.INT);
    }

    public static void initialize() {
       FairyLights.LOGGER.info("Initializing Components");
    }

    @Override
    public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {
        registry.registerFor(
                FastenerBlockEntity.class,
                FASTENER,
                be -> new FastenerComponent().setFastener(new BlockFastener(be, new RegularBlockView())));
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(PlayerEntity.class, FASTENER, e -> new FastenerComponent().setFastener(new PlayerFastener(e)));
        registry.registerFor(FenceFastenerEntity.class, FASTENER, e -> new FastenerComponent().setFastener(new FenceFastener(e)));
    }

    private static <T> ComponentType<T> fromCodec(Codec<T> c) {
        return ComponentType.<T>builder().codec(c).packetCodec(PacketCodecs.codec(c)).build();
    }
}
