package de.lucalabs.fairylights.items.components;

import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.string.StringType;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.List;

public final class FairyLightItemComponents {

    public static final ComponentType<List<Integer>> COLORS = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(FairyLights.ID, "colors"),
            ComponentType.<List<Integer>>builder().build()
    );

    public static final ComponentType<Integer> COLOR = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(FairyLights.ID, "color"),
            ComponentType.<Integer>builder().build()
    );

    public static final ComponentType<StringType> STRING = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(FairyLights.ID, "string"),
            ComponentType.<StringType>builder().build()
    );

    public static final ComponentType<List<ItemStack>> PATTERN = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(FairyLights.ID, "string"),
            ComponentType.<List<ItemStack>>builder().build()
    );

    public static final ComponentType<Boolean> TWINKLE = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(FairyLights.ID, "twinkle"),
            ComponentType.<Boolean>builder().build()
    );

    public static final ComponentType<ComponentRecords.ConnectionLogic> LOGIC = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(FairyLights.ID, "logic"),
            ComponentType.<ComponentRecords.ConnectionLogic>builder().build()
    );

    private FairyLightItemComponents() {

    }

    public static void initialize() {
        FairyLights.LOGGER.info("initializing item components");
    }
}
