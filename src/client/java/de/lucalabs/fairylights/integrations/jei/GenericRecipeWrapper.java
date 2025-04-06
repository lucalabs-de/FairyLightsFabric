package de.lucalabs.fairylights.integrations.jei;

import com.google.common.collect.ImmutableList;
import de.lucalabs.fairylights.items.crafting.GenericIngredient;
import de.lucalabs.fairylights.items.crafting.GenericRecipe;
import de.lucalabs.fairylights.items.crafting.RegularIngredient;
import de.lucalabs.fairylights.items.crafting.ingredient.AuxiliaryIngredient;
import de.lucalabs.fairylights.util.MathHelper;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public final class GenericRecipeWrapper implements ICraftingCategoryExtension {
    private final GenericRecipe recipe;

    private final List<List<ItemStack>> allInputs;

    // Only minimal stacks, ingredients that support multiple will only have first taken unless dictatesOutputType
    private final List<List<ItemStack>> minimalInputStacks;

    private final List<ItemStack> outputs;

    private final GenericIngredient<?, ?>[] ingredientMatrix;

    private final int subtypeIndex;

    public GenericRecipeWrapper(final GenericRecipe recipe) {
        this.recipe = recipe;
        final List<List<ItemStack>> allInputs = new ArrayList<>();
        final List<List<ItemStack>> minimalInputStacks = new ArrayList<>();
        final RegularIngredient[] ingredients = recipe.getGenericIngredients();
        final AuxiliaryIngredient<?>[] aux = recipe.getAuxiliaryIngredients();
        this.ingredientMatrix = new GenericIngredient<?, ?>[9];
        int subtypeIndex = -1;
        for (int i = 0, auxIdx = 0; i < 9; i++) {
            final int x = i % 3;
            final int y = i / 3;
            boolean isEmpty = true;
            if (x < recipe.getWidth() && y < recipe.getHeight()) {
                final RegularIngredient ingredient = ingredients[x + y * recipe.getWidth()];
                final ImmutableList<ItemStack> ingInputs = ingredient.getInputs();
                if (!ingInputs.isEmpty()) {
                    if (ingredient.dictatesOutputType()) {
                        minimalInputStacks.add(ingInputs);
                        subtypeIndex = i;
                    } else {
                        minimalInputStacks.add(ImmutableList.of(ingInputs.get(0)));
                    }
                    this.ingredientMatrix[i] = ingredient;
                    allInputs.add(ingInputs);
                    isEmpty = false;
                }
            }
            if (isEmpty) {
                AuxiliaryIngredient<?> ingredient = null;
                ImmutableList<ItemStack> stacks = null;
                boolean dictator = false;
                while (auxIdx < aux.length) {
                    ingredient = aux[auxIdx++];
                    final ImmutableList<ItemStack> a = ingredient.getInputs();
                    if (!a.isEmpty()) {
                        stacks = a;
                        if (ingredient.dictatesOutputType()) {
                            subtypeIndex = i;
                            dictator = true;
                        }
                        break;
                    }
                }
                if (stacks == null) {
                    stacks = ImmutableList.of();
                    ingredient = null;
                }
                minimalInputStacks.add(stacks.isEmpty() || dictator ? stacks : ImmutableList.of(stacks.get(0)));
                this.ingredientMatrix[i] = ingredient;
                allInputs.add(stacks);
            }
        }
        this.allInputs = allInputs;
        this.minimalInputStacks = minimalInputStacks;
        this.subtypeIndex = subtypeIndex;
        final ImmutableList.Builder<ItemStack> outputs = ImmutableList.builder();
        this.forOutputMatches((v, output) -> outputs.add(output));
        this.outputs = outputs.build();
    }

    private void forOutputMatches(final BiConsumer<ItemStack, ItemStack> outputConsumer) {
        final RecipeInputInventory crafting = new CraftingInventory(new ScreenHandler(null, 0) {
            @Override
            public ItemStack quickMove(PlayerEntity p_38941_, int p_38942_) {
                return ItemStack.EMPTY;
            }

            @Override
            public boolean canUse(PlayerEntity player) {
                return true;
            }
        }, this.getWidth(), this.getHeight());
        if (this.subtypeIndex == -1) {
            for (int i = 0; i < this.minimalInputStacks.size(); i++) {
                final List<ItemStack> stacks = this.minimalInputStacks.get(i);
                crafting.setStack(i, stacks.isEmpty() ? ItemStack.EMPTY : stacks.get(0));
            }
            if (this.recipe.matches(crafting, null)) {
                outputConsumer.accept(ItemStack.EMPTY, this.recipe.craft(crafting, null));
            }
        } else {
            final List<ItemStack> dictators = this.minimalInputStacks.get(this.subtypeIndex);
            for (final ItemStack subtype : dictators) {
                crafting.clear();
                for (int i = 0; i < this.minimalInputStacks.size(); i++) {
                    if (i == this.subtypeIndex) {
                        crafting.setStack(i, subtype);
                    } else {
                        final List<ItemStack> stacks = this.minimalInputStacks.get(i);
                        crafting.setStack(i, stacks.isEmpty() ? ItemStack.EMPTY : stacks.get(0));
                    }
                }
                if (this.recipe.matches(crafting, null)) {
                    outputConsumer.accept(subtype, this.recipe.craft(crafting, null));
                }
            }
        }
    }

    @Override
    public int getWidth() {
        return 3;
    }

    @Override
    public int getHeight() {
        return 3;
    }

    private Input getInputsForOutput(final ItemStack output) {
        final RegularIngredient[] ingredients = this.recipe.getGenericIngredients();
        final List<List<ItemStack>> inputs = new ArrayList<>(9);
        final GenericIngredient<?, ?>[] ingredientMat = new GenericIngredient<?, ?>[9];
        final AuxiliaryIngredient<?>[] aux = this.recipe.getAuxiliaryIngredients();
        for (int i = 0, auxIngIdx = 0, auxIdx = 0; i < 9; i++) {
            final int x = i % 3;
            final int y = i / 3;
            final ImmutableList<ImmutableList<ItemStack>> ingInputs;
            GenericIngredient<?, ?> ingredient = null;
            if (x < this.recipe.getWidth() && y < this.recipe.getHeight()) {
                ingredient = ingredients[x + y * this.recipe.getWidth()];
                ingInputs = ingredient.getInput(output);
            } else {
                ingInputs = null;
            }
            if (ingInputs == null || ingInputs.isEmpty()) {
                boolean isEmpty = true;
                if (auxIngIdx < aux.length) {
                    ImmutableList<ImmutableList<ItemStack>> auxInputs = null;
                    AuxiliaryIngredient<?> ingredientAux = null;
                    for (; auxIngIdx < aux.length; auxIngIdx++) {
                        ingredientAux = aux[auxIngIdx];
                        auxInputs = ingredientAux.getInput(output);
                        if (!auxInputs.isEmpty()) {
                            break;
                        }
                    }
                    if (!auxInputs.isEmpty()) {
                        inputs.add(auxInputs.get(auxIdx++));
                        ingredientMat[i] = ingredientAux;
                        if (auxIdx == auxInputs.size()) {
                            auxIdx = 0;
                            auxIngIdx++;
                        }
                        isEmpty = false;
                    }
                }
                if (isEmpty) {
                    inputs.add(Collections.emptyList());
                }
            } else {
                inputs.add(ingInputs.get(0));
                ingredientMat[i] = ingredient;
            }
        }
        return new Input(inputs, ingredientMat);
    }

    private Input getInputsForIngredient(final ItemStack ingredient) {
        final RecipeInputInventory crafting = new CraftingInventory(new ScreenHandler(null, 0) {
            @Override
            public ItemStack quickMove(PlayerEntity p_38941_, int p_38942_) {
                return ItemStack.EMPTY;
            }

            @Override
            public boolean canUse(PlayerEntity player) {
                return true;
            }
        }, this.getWidth(), this.getHeight());
        for (int i = 0; i < this.allInputs.size(); i++) {
            final List<ItemStack> options = this.allInputs.get(i);
            ItemStack matched = null;
            for (final ItemStack o : options) {
                if (ingredient.getItem() == o.getItem()) {
                    matched = ingredient.copy();
                    matched.setCount(1);
                    break;
                }
            }
            if (matched == null) {
                continue;
            }
            crafting.clear();
            for (int n = 0; n < this.minimalInputStacks.size(); n++) {
                final List<ItemStack> stacks = this.minimalInputStacks.get(n);
                crafting.setStack(n, i == n ? matched : stacks.isEmpty() ? ItemStack.EMPTY : stacks.get(0));
            }
            if (this.recipe.matches(crafting, null)) {
                final List<List<ItemStack>> inputs = new ArrayList<>(this.allInputs.size());
                for (int n = 0; n < this.allInputs.size(); n++) {
                    final List<ItemStack> stacks = this.allInputs.get(n);
                    inputs.add(i == n ? Collections.singletonList(matched) : stacks);
                }
                return new Input(inputs, this.ingredientMatrix);
            }
        }
        return null;
    }

    public List<ItemStack> getOutput(final List<List<ItemStack>> inputs) {
        final RecipeInputInventory crafting = new CraftingInventory(new ScreenHandler(null, 0) {
            @Override
            public ItemStack quickMove(PlayerEntity p_38941_, int p_38942_) {
                return ItemStack.EMPTY;
            }

            @Override
            public boolean canUse(PlayerEntity player) {
                return true;
            }
        }, this.getWidth(), this.getHeight());

        int size = 1;
        for (final List<ItemStack> stack : inputs) {
            if (!stack.isEmpty()) {
                size = MathHelper.lcm(stack.size(), size);
            }
        }

        final List<ItemStack> outputs = new ArrayList<>(size);

        for (int n = 0; n < size; n++) {
            for (int i = 0; i < inputs.size(); i++) {
                final List<ItemStack> stacks = inputs.get(i);
                crafting.setStack(i, stacks.isEmpty() ? ItemStack.EMPTY : stacks.get(n % stacks.size()));
            }

            if (this.recipe.matches(crafting, null)) {
                outputs.add(this.recipe.craft(crafting, null));
            }
        }
        return outputs;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
        focuses.getFocuses(VanillaTypes.ITEM_STACK).flatMap(focus -> {
            ItemStack stack = focus.getTypedValue().getIngredient();
            Input input = null;
            if (focus.getRole() == RecipeIngredientRole.INPUT) {
                input = this.getInputsForIngredient(stack);
            } else if (focus.getRole() == RecipeIngredientRole.OUTPUT) {
                input = this.getInputsForOutput(stack);
            }
            return Stream.ofNullable(input);
        }).findFirst().ifPresentOrElse(input -> {
            craftingGridHelper.createAndSetOutputs(builder, VanillaTypes.ITEM_STACK, this.getOutput(input.inputs));
            List<IRecipeSlotBuilder> slots = craftingGridHelper.createAndSetInputs(builder, VanillaTypes.ITEM_STACK, input.inputs, this.getWidth(), this.getHeight());
            for (int i = 0; i < 9; i++) {
                GenericIngredient<?, ?> ingredient = input.ingredients[i];
                IRecipeSlotBuilder slot = slots.get(i);
                slot.addTooltipCallback((recipeSlotView, tooltip) -> {
                    if (recipeSlotView.getRole() == RecipeIngredientRole.INPUT) {
                        ingredient.addTooltip(tooltip);
                    }
                });
            }
        }, () -> {
            craftingGridHelper.createAndSetOutputs(builder, VanillaTypes.ITEM_STACK, this.outputs);
            craftingGridHelper.createAndSetInputs(builder, VanillaTypes.ITEM_STACK, this.allInputs, this.getWidth(), this.getHeight());
        });
    }

    private static final class Input {
        List<List<ItemStack>> inputs;

        GenericIngredient<?, ?>[] ingredients;

        private Input(final List<List<ItemStack>> inputs, final GenericIngredient<?, ?>[] ingredients) {
            this.inputs = inputs;
            this.ingredients = ingredients;
        }
    }
}
