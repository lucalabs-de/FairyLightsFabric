package de.lucalabs.fairylights.main.items.crafting;

import de.lucalabs.fairylights.main.items.DyeableItem;
import de.lucalabs.fairylights.main.util.Tags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class CopyColorRecipe extends CustomRecipe {
    public CopyColorRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(final CraftingInput inv, final Level world) {
        int count = 0;
        for (int i = 0; i < inv.size(); i++) {
            final ItemStack stack = inv.items().get(i);
            if (!stack.isEmpty() && (!stack.is(Tags.DYEABLE) || count++ >= 2)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull ItemStack assemble(final CraftingInput inv, final HolderLookup.@NotNull Provider lookup) {
        ItemStack original = ItemStack.EMPTY;
        for (int i = 0; i < inv.size(); i++) {
            final ItemStack stack = inv.items().get(i);
            if (!stack.isEmpty()) {
                if (stack.is(Tags.DYEABLE)) {
                    if (original.isEmpty()) {
                        original = stack;
                    } else {
                        final ItemStack copy = stack.copy();
                        copy.setCount(1);
                        DyeableItem.setColor(copy, DyeableItem.getColor(original));
                        return copy;
                    }
                } else {
                    break;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(final CraftingInput inv) {
        ItemStack original = ItemStack.EMPTY;
        final NonNullList<ItemStack> remaining = NonNullList.withSize(inv.size(), ItemStack.EMPTY);
        for (int i = 0; i < remaining.size(); i++) {
            final ItemStack stack = inv.items().get(i);
            if (stack.getItem().hasCraftingRemainingItem()) {
                remaining.set(i, getCraftingRemainingItem(stack.getItem()));
            } else if (original.isEmpty() && !stack.isEmpty() && stack.is(Tags.DYEABLE)) {
                final ItemStack rem = stack.copy();
                rem.setCount(1);
                remaining.set(i, rem);
                original = stack;
            }
        }
        return remaining;
    }

    @Override
    public boolean canCraftInDimensions(final int width, final int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return FairyLightCraftingRecipes.COPY_COLOR;
    }

    private ItemStack getCraftingRemainingItem(Item item) {
        return item.hasCraftingRemainingItem() ? item.getCraftingRemainingItem().getDefaultInstance() : ItemStack.EMPTY;
    }
}
