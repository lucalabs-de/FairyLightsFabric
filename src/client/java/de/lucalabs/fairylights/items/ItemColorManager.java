package de.lucalabs.fairylights.items;

import de.lucalabs.fairylights.string.StringTypes;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

public final class ItemColorManager {

    private static final Item[] COLORABLE_LIGHTS = {
            FairyLightItems.FAIRY_LIGHT,
            FairyLightItems.PAPER_LANTERN
    };

    private static final Item[] HANGING_LIGHTS_LINES = {
            FairyLightItems.HANGING_LIGHTS
    };

    private static final Item[] PENNANT_BUNTING_LINES = {
            FairyLightItems.PENNANT_BUNTING
    };

    private static final Item[] PENNANTS = {
            FairyLightItems.TRIANGLE_PENNANT,
            FairyLightItems.SQUARE_PENNANT
    };

    private ItemColorManager() {
    }

    public static void setupColors() {
        ColorProviderRegistry.ITEM.register((stack, index) -> {
            if (index == 1) {
                return DyeableItem.getColor(stack);
            }

            return 0xFFFFFF;
        }, COLORABLE_LIGHTS);

        ColorProviderRegistry.ITEM.register((stack, index) -> {
            final NbtCompound tag = stack.getNbt();
            if (index == 0) {
                if (tag != null) {
                    return HangingLightsConnectionItem.getString(tag).color();
                }
                return StringTypes.BLACK_STRING.color();
            }
            if (tag != null) {
                final NbtList tagList = tag.getList("pattern", NbtElement.COMPOUND_TYPE);
                if (!tagList.isEmpty()) {
                    final ItemStack stack2 = ItemStack.fromNbt(tagList.getCompound((index - 1) % tagList.size()));

                    return DyeableItem.getColor(stack2);
                }
            }

            return 0xFFD584;
        }, HANGING_LIGHTS_LINES);

        ColorProviderRegistry.ITEM.register((stack, index) -> {
            if (index == 0) {
                return 0xFFFFFFFF;
            }
            final NbtCompound tag = stack.getNbt();
            if (tag != null) {
                final NbtList tagList = tag.getList("pattern", NbtElement.COMPOUND_TYPE);
                if (!tagList.isEmpty()) {
                    final ItemStack light = ItemStack.fromNbt(tagList.getCompound((index - 1) % tagList.size()));
                    return DyeableItem.getColor(light);
                }
            }
            return 0xFFFFFFFF;
        }, PENNANT_BUNTING_LINES);

        ColorProviderRegistry.ITEM.register(
                (stack, index) -> index == 0 ? 0xFFFFFF : DyeableItem.getColor(stack),
                PENNANTS);
    }
}
