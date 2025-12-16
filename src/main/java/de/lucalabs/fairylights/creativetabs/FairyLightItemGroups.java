package de.lucalabs.fairylights.creativetabs;

import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.items.DyeableItem;
import de.lucalabs.fairylights.items.FairyLightItems;
import de.lucalabs.fairylights.items.crafting.FairyLightCraftingRecipes;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class FairyLightItemGroups {

    public static final ItemGroup FAIRY_LIGHTS = register(
            Identifier.of(FairyLights.ID, "item_group"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(FairyLightItems.HANGING_LIGHTS))
                    .displayName(Text.translatable("itemGroup.fairylights"))
                    .entries((context, entries) -> {
                        for (final DyeColor color : DyeColor.values()) {
                            entries.add(FairyLightCraftingRecipes.makeHangingLights(new ItemStack(FairyLightItems.HANGING_LIGHTS), color));
                        }

                        for (final DyeColor color : DyeColor.values()) {
                            final ItemStack stack = new ItemStack(FairyLightItems.PENNANT_BUNTING);
                            DyeableItem.setColor(stack, color);
                            entries.add(FairyLightCraftingRecipes.makePennant(stack, color));
                        }

                        entries.add(new ItemStack(FairyLightItems.GARLAND));
                        entries.add(new ItemStack(FairyLightItems.TINSEL));

                        entries.addAll(generateCollection(FairyLightItems.TRIANGLE_PENNANT));
                        entries.addAll(generateCollection(FairyLightItems.SQUARE_PENNANT));

                        entries.addAll(generateCollection(FairyLightItems.FAIRY_LIGHT));
                        entries.addAll(generateCollection(FairyLightItems.PAPER_LANTERN));
                        entries.addAll(generateCollection(FairyLightItems.ORB_LANTERN));
                        entries.addAll(generateCollection(FairyLightItems.FLOWER_LIGHT));
                        entries.addAll(generateCollection(FairyLightItems.CANDLE_LANTERN_LIGHT));
                        entries.addAll(generateCollection(FairyLightItems.JACK_O_LANTERN));
                        entries.addAll(generateCollection(FairyLightItems.SKULL_LIGHT));
                        entries.addAll(generateCollection(FairyLightItems.GHOST_LIGHT));
                        entries.addAll(generateCollection(FairyLightItems.SPIDER_LIGHT));
                        entries.addAll(generateCollection(FairyLightItems.WITCH_LIGHT));
                        entries.addAll(generateCollection(FairyLightItems.SNOWFLAKE_LIGHT));
                        entries.addAll(generateCollection(FairyLightItems.HEART_LIGHT));
                        entries.addAll(generateCollection(FairyLightItems.MOON_LIGHT));
                        entries.addAll(generateCollection(FairyLightItems.STAR_LIGHT));
                        entries.addAll(generateCollection(FairyLightItems.ICICLE_LIGHTS));
                        entries.addAll(generateCollection(FairyLightItems.METEOR_LIGHT));

                        entries.add(new ItemStack(FairyLightItems.OIL_LANTERN));
                        entries.add(new ItemStack(FairyLightItems.CANDLE_LANTERN));
                        entries.add(new ItemStack(FairyLightItems.INCANDESCENT_LIGHT));
                    }));

    private FairyLightItemGroups() {
    }

    private static Collection<ItemStack> generateCollection(final @NotNull Item item) {
        final List<ItemStack> stacks = new ArrayList<>();
        for (final DyeColor color : DyeColor.values()) {
            stacks.add(DyeableItem.setColor(new ItemStack(item), color));
        }
        return stacks;
    }

    private static ItemGroup register(final Identifier name, final ItemGroup.Builder g) {
        return Registry.register(Registries.ITEM_GROUP, name, g.build());
    }

    public static void initialize() {
        FairyLights.LOGGER.info("Initializing inventory tabs");
    }
}
