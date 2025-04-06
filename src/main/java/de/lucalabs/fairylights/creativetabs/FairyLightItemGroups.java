package de.lucalabs.fairylights.creativetabs;

import de.lucalabs.fairylights.items.DyeableItem;
import de.lucalabs.fairylights.items.FairyLightItems;
import de.lucalabs.fairylights.items.crafting.FairyLightCraftingRecipes;
import de.lucalabs.fairylights.util.styled.StyledString;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class FairyLightItemGroups {

    public static final ItemGroup FAIRY_LIGHTS = FabricItemGroup.builder()
            .icon(() -> new ItemStack(FairyLightItems.HANGING_LIGHTS))
            .displayName(Text.literal("FairyLights"))
            .entries((context, entries) -> {
                for (final DyeColor color : DyeColor.values())
                {
                    entries.add(FairyLightCraftingRecipes.makeHangingLights(new ItemStack(FairyLightItems.HANGING_LIGHTS), color));
                }

                for (final DyeColor color : DyeColor.values())
                {
                    final ItemStack stack = new ItemStack(FairyLightItems.PENNANT_BUNTING);
                    DyeableItem.setColor(stack, color);
                    entries.add(FairyLightCraftingRecipes.makePennant(stack, color));
                }

                entries.addAll(generateCollection(FairyLightItems.TRIANGLE_PENNANT));
                entries.addAll(generateCollection(FairyLightItems.SQUARE_PENNANT));

                entries.addAll(generateCollection(FairyLightItems.FAIRY_LIGHT));
                entries.addAll(generateCollection(FairyLightItems.PAPER_LANTERN));

                entries.add(new ItemStack(FairyLightItems.INCANDESCENT_LIGHT));
            })
            .build();

    private FairyLightItemGroups() {}

    private static Collection<ItemStack> generateCollection(final @NotNull Item item)
    {
        final List<ItemStack> stacks = new ArrayList<>();
        for (final DyeColor color : DyeColor.values())
        {
            stacks.add(DyeableItem.setColor(new ItemStack(item), color));
        }
        return stacks;
    }
}
