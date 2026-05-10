package de.lucalabs.fairylights.main.items.components;

import com.mojang.serialization.Codec;
import de.lucalabs.fairylights.Common;
import java.util.List;

import de.lucalabs.fairylights.Constants;
import de.lucalabs.fairylights.main.string.StringType;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public final class FairyLightItemComponents {

    public static final DataComponentType<List<Integer>> COLORS = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Common.id("colors"),
            DataComponentType.<List<Integer>>builder().persistent(Codec.INT.listOf()).build()
    );

    public static final DataComponentType<Integer> COLOR = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Common.id("color"),
            DataComponentType.<Integer>builder().persistent(Codec.INT).build()
    );

    public static final DataComponentType<StringType> STRING = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Common.id("string"),
            DataComponentType.<StringType>builder().persistent(StringType.CODEC).build()
    );

    public static final DataComponentType<List<ItemStack>> PATTERN = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Common.id("pattern"),
            DataComponentType.<List<ItemStack>>builder().persistent(ItemStack.CODEC.listOf()).build()
    );

    public static final DataComponentType<Boolean> TWINKLE = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Common.id("twinkle"),
            DataComponentType.<Boolean>builder().persistent(Codec.BOOL).build()
    );

    public static final DataComponentType<ResourceLocation> LIGHT_VARIANT = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Common.id("variant"),
            DataComponentType.<ResourceLocation>builder().persistent(ResourceLocation.CODEC).build()
    );

    private FairyLightItemComponents() {

    }

    public static void initialize() {
        Constants.LOG.info("initializing item components");
    }
}
