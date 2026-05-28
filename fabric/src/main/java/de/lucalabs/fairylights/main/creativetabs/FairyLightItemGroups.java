package de.lucalabs.fairylights.main.creativetabs;

import de.lucalabs.fairylights.Common;
import de.lucalabs.fairylights.Constants;
import de.lucalabs.fairylights.main.items.DyeableItem;
import de.lucalabs.fairylights.main.items.FairyLightItems;
import de.lucalabs.fairylights.main.items.crafting.FairyLightCraftingRecipes;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class FairyLightItemGroups {

    public static final CreativeModeTab FAIRY_LIGHTS = register(
            Common.id("item_group"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(FairyLightItems.HANGING_LIGHTS))
                    .title(Component.translatable("itemGroup.fairylights"))
                    .displayItems((context, entries) -> {
                        for (final DyeColor color : DyeColor.values()) {
                            entries.accept(FairyLightCraftingRecipes.makeHangingLights(new ItemStack(FairyLightItems.HANGING_LIGHTS), color));
                        }

                        for (final DyeColor color : DyeColor.values()) {
                            final ItemStack stack = new ItemStack(FairyLightItems.PENNANT_BUNTING);
                            entries.accept(FairyLightCraftingRecipes.makePennant(stack, color));
                        }

                        entries.accept(new ItemStack(FairyLightItems.GARLAND));

                        entries.acceptAll(generateCollection(FairyLightItems.TINSEL));

                        entries.acceptAll(generateCollection(FairyLightItems.TRIANGLE_PENNANT));
                        entries.acceptAll(generateCollection(FairyLightItems.SQUARE_PENNANT));

                        entries.acceptAll(generateCollection(FairyLightItems.FAIRY_LIGHT));
                        entries.acceptAll(generateCollection(FairyLightItems.PAPER_LANTERN));
                        entries.acceptAll(generateCollection(FairyLightItems.ORB_LANTERN));
                        entries.acceptAll(generateCollection(FairyLightItems.FLOWER_LIGHT));
                        entries.acceptAll(generateCollection(FairyLightItems.CANDLE_LANTERN_LIGHT));
                        entries.acceptAll(generateCollection(FairyLightItems.JACK_O_LANTERN));
                        entries.acceptAll(generateCollection(FairyLightItems.SKULL_LIGHT));
                        entries.acceptAll(generateCollection(FairyLightItems.GHOST_LIGHT));
                        entries.acceptAll(generateCollection(FairyLightItems.SPIDER_LIGHT));
                        entries.acceptAll(generateCollection(FairyLightItems.WITCH_LIGHT));
                        entries.acceptAll(generateCollection(FairyLightItems.SNOWFLAKE_LIGHT));
                        entries.acceptAll(generateCollection(FairyLightItems.HEART_LIGHT));
                        entries.acceptAll(generateCollection(FairyLightItems.MOON_LIGHT));
                        entries.acceptAll(generateCollection(FairyLightItems.STAR_LIGHT));
                        entries.acceptAll(generateCollection(FairyLightItems.ICICLE_LIGHTS));
                        entries.acceptAll(generateCollection(FairyLightItems.METEOR_LIGHT));

                        entries.accept(new ItemStack(FairyLightItems.OIL_LANTERN));
                        entries.accept(new ItemStack(FairyLightItems.INCANDESCENT_LIGHT));
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

    private static CreativeModeTab register(final ResourceLocation name, final CreativeModeTab.Builder g) {
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, name, g.build());
    }

    public static void initialize() {
        Constants.LOG.info("Initializing inventory tabs");
    }
}
