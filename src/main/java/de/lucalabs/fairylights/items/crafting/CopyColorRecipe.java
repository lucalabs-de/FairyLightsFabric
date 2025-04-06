package de.lucalabs.fairylights.items.crafting;

import de.lucalabs.fairylights.items.DyeableItem;
import de.lucalabs.fairylights.util.Tags;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class CopyColorRecipe extends SpecialCraftingRecipe {
    public CopyColorRecipe(final Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(final RecipeInputInventory inv, final World world) {
        int count = 0;
        for (int i = 0; i < inv.size(); i++) {
            final ItemStack stack = inv.getStack(i);
            if (!stack.isEmpty() && (!stack.isIn(Tags.DYEABLE) || count++ >= 2)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack craft(final RecipeInputInventory inv, final DynamicRegistryManager registryAccess) {
        ItemStack original = ItemStack.EMPTY;
        for (int i = 0; i < inv.size(); i++) {
            final ItemStack stack = inv.getStack(i);
            if (!stack.isEmpty()) {
                if (stack.isIn(Tags.DYEABLE)) {
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
    public DefaultedList<ItemStack> getRemainder(final RecipeInputInventory inv) {
        ItemStack original = ItemStack.EMPTY;
        final DefaultedList<ItemStack> remaining = DefaultedList.ofSize(inv.size(), ItemStack.EMPTY);
        for (int i = 0; i < remaining.size(); i++) {
            final ItemStack stack = inv.getStack(i);
            if (stack.getItem().hasRecipeRemainder()) {
                remaining.set(i, stack.getItem().getRecipeRemainder(stack));
            } else if (original.isEmpty() && !stack.isEmpty() && stack.isIn(Tags.DYEABLE)) {
                final ItemStack rem = stack.copy();
                rem.setCount(1);
                remaining.set(i, rem);
                original = stack;
            }
        }
        return remaining;
    }

    @Override
    public boolean fits(final int width, final int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return FairyLightCraftingRecipes.COPY_COLOR;
    }
}
