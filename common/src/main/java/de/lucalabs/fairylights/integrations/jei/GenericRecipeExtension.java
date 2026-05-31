package de.lucalabs.fairylights.integrations.jei;

import de.lucalabs.fairylights.main.items.crafting.GenericIngredient;
import de.lucalabs.fairylights.main.items.crafting.GenericRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class GenericRecipeExtension implements ICraftingCategoryExtension<GenericRecipe> {

    private final int width = 3;
    private final int height = 3;

    @Override
    public void setRecipe(@NotNull RecipeHolder<GenericRecipe> recipeHolder, @NotNull IRecipeLayoutBuilder builder, @NotNull ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
        GenericRecipeWrapper recipe = new GenericRecipeWrapper(recipeHolder.value());

        focuses.getFocuses(VanillaTypes.ITEM_STACK).flatMap(focus -> {
            ItemStack stack = focus.getTypedValue().getIngredient();
            GenericRecipeWrapper.Input input = null;
            if (focus.getRole() == RecipeIngredientRole.INPUT) {
                input = recipe.getInputsForIngredient(stack);
            } else if (focus.getRole() == RecipeIngredientRole.OUTPUT) {
                input = recipe.getInputsForOutput(stack);
            }
            return Stream.ofNullable(input);
        }).findFirst().ifPresentOrElse(input -> {
            craftingGridHelper.createAndSetOutputs(builder, VanillaTypes.ITEM_STACK, recipe.getOutput(input.inputs));
            List<IRecipeSlotBuilder> slots = craftingGridHelper.createAndSetInputs(builder, VanillaTypes.ITEM_STACK, input.inputs, this.width, this.height);
            for (int i = 0; i < 9; i++) {
                GenericIngredient<?, ?> ingredient = input.ingredients[i];
                IRecipeSlotBuilder slot = slots.get(i);
                slot.addRichTooltipCallback((recipeSlotView, tooltip) -> {
                    if (recipeSlotView.getRole() == RecipeIngredientRole.INPUT) {
                        tooltip.addAll(ingredient.getTooltip());
                    }
                });
            }
        }, () -> {
            craftingGridHelper.createAndSetOutputs(builder, VanillaTypes.ITEM_STACK, recipe.getOutputs());
            craftingGridHelper.createAndSetInputs(builder, VanillaTypes.ITEM_STACK, recipe.getAllInputs(), this.width, this.height);
        });
    }

    @Override
    public int getWidth(@NotNull RecipeHolder<GenericRecipe> holder) {
        return this.width;
    }

    @Override
    public int getHeight(@NotNull RecipeHolder<GenericRecipe> holder) {
        return this.height;
    }
}
