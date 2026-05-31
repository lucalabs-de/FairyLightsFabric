package de.lucalabs.fairylights.main.creativetabs;

import de.lucalabs.fairylights.Constants;
import de.lucalabs.fairylights.main.items.DyeableItem;
import de.lucalabs.fairylights.main.items.FairyLightItems;
import de.lucalabs.fairylights.main.items.crafting.FairyLightCraftingRecipes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * NeoForge port of the Fabric creative tab: same contents, registered with a vanilla
 * {@link CreativeModeTab.Builder} through a DeferredRegister instead of FabricItemGroup.
 */
public final class FairyLightItemGroups {

    private static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);

    public static final Supplier<CreativeModeTab> FAIRY_LIGHTS = TABS.register("item_group", () ->
            CreativeModeTab.builder()
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
                    })
                    .build());

    private FairyLightItemGroups() {
    }

    private static Collection<ItemStack> generateCollection(final @NotNull Item item) {
        final List<ItemStack> stacks = new ArrayList<>();
        for (final DyeColor color : DyeColor.values()) {
            stacks.add(DyeableItem.setColor(new ItemStack(item), color));
        }
        return stacks;
    }

    public static void init(final IEventBus modBus) {
        TABS.register(modBus);
    }
}
