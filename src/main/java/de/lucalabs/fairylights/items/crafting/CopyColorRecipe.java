package de.lucalabs.fairylights.items.crafting;

import de.lucalabs.fairylights.items.DyeableItem;
import de.lucalabs.fairylights.util.Tags;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class CopyColorRecipe extends SpecialCraftingRecipe {
    public CopyColorRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(final CraftingRecipeInput inv, final World world) {
        int count = 0;
        for (int i = 0; i < inv.getSize(); i++) {
            final ItemStack stack = inv.getStacks().get(i);
            if (!stack.isEmpty() && (!stack.isIn(Tags.DYEABLE) || count++ >= 2)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack craft(final CraftingRecipeInput inv, final RegistryWrapper.WrapperLookup lookup) {
        ItemStack original = ItemStack.EMPTY;
        for (int i = 0; i < inv.getSize(); i++) {
            final ItemStack stack = inv.getStacks().get(i);
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
    public DefaultedList<ItemStack> getRemainder(final CraftingRecipeInput inv) {
        ItemStack original = ItemStack.EMPTY;
        final DefaultedList<ItemStack> remaining = DefaultedList.ofSize(inv.getSize(), ItemStack.EMPTY);
        for (int i = 0; i < remaining.size(); i++) {
            final ItemStack stack = inv.getStacks().get(i);
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
