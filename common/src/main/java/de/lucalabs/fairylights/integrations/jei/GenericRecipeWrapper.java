package de.lucalabs.fairylights.integrations.jei;

import com.google.common.collect.ImmutableList;
import de.lucalabs.fairylights.main.items.crafting.GenericIngredient;
import de.lucalabs.fairylights.main.items.crafting.GenericRecipe;
import de.lucalabs.fairylights.main.items.crafting.RegularIngredient;
import de.lucalabs.fairylights.main.items.crafting.ingredient.AuxiliaryIngredient;
import de.lucalabs.fairylights.main.util.MathHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public final class GenericRecipeWrapper  {
    private final GenericRecipe recipe;

    private final List<List<ItemStack>> allInputs;

    // Only minimal stacks, ingredients that support multiple will only have first taken unless dictatesOutputType
    private final List<List<ItemStack>> minimalInputStacks;

    private final List<ItemStack> outputs;

    private final GenericIngredient<?, ?>[] ingredientMatrix;

    private final int subtypeIndex;
    private final int width = 3;
    private final int height = 3;

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
                        minimalInputStacks.add(ImmutableList.of(ingInputs.getFirst()));
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
                minimalInputStacks.add(stacks.isEmpty() || dictator ? stacks : ImmutableList.of(stacks.getFirst()));
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

    Input getInputsForOutput(final ItemStack output) {
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
                inputs.add(ingInputs.getFirst());
                ingredientMat[i] = ingredient;
            }
        }

        return new Input(inputs, ingredientMat);
    }

    Input getInputsForIngredient(final ItemStack ingredient) {
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

            CraftingInput input = CraftingInput.of(
                    this.width,
                    this.height,
                    this.minimalInputStacks.stream()
                            .map(x -> x.isEmpty() ? ItemStack.EMPTY : x.getFirst()).toList());

            if (this.recipe.matches(input, null)) {
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

    List<ItemStack> getOutput(final List<List<ItemStack>> inputs) {
        int size = 1;
        for (final List<ItemStack> stack : inputs) {
            if (!stack.isEmpty()) {
                size = MathHelper.lcm(stack.size(), size);
            }
        }

        final List<ItemStack> outputs = new ArrayList<>(size);

        for (int n = 0; n < size; n++) {
            final int N = n;
            CraftingInput input = CraftingInput.of(
                    this.width,
                    this.height,
                    inputs.stream().map(x -> x.isEmpty() ? ItemStack.EMPTY : x.get(N % x.size())).toList());

            if (this.recipe.matches(input, null)) {
                outputs.add(this.recipe.assemble(input, null));
            }
        }

        return outputs;
    }

    List<ItemStack> getOutputs() {
        return this.outputs;
    }

    public List<List<ItemStack>> getAllInputs() {
        return this.allInputs;
    }

    private void forOutputMatches(final BiConsumer<ItemStack, ItemStack> outputConsumer) {
        if (this.subtypeIndex == -1) {
            CraftingInput input = CraftingInput.of(
                    this.width,
                    this.height,
                    this.minimalInputStacks.stream()
                            .map(x -> x.isEmpty() ? ItemStack.EMPTY : x.getFirst()).toList());

            if (this.recipe.matches(input, null)) {
                outputConsumer.accept(ItemStack.EMPTY, this.recipe.assemble(input, null));
            }
        } else {
            final List<ItemStack> dictators = this.minimalInputStacks.get(this.subtypeIndex);

            for (final ItemStack subtype : dictators) {
                List<ItemStack> inputs = new ArrayList<>();

                for (int i = 0; i < this.minimalInputStacks.size(); i++) {
                    if (i == this.subtypeIndex) {
                        inputs.add(i, subtype);
                    } else {
                        final List<ItemStack> stacks = this.minimalInputStacks.get(i);
                        inputs.add(i, stacks.isEmpty() ? ItemStack.EMPTY : stacks.getFirst());
                    }
                }

                CraftingInput input = CraftingInput.of(this.width, this.height, inputs);

                if (this.recipe.matches(input, null)) {
                    outputConsumer.accept(subtype, this.recipe.assemble(input, null));
                }
            }
        }
    }

    static final class Input {
        List<List<ItemStack>> inputs;

        GenericIngredient<?, ?>[] ingredients;

        private Input(final List<List<ItemStack>> inputs, final GenericIngredient<?, ?>[] ingredients) {
            this.inputs = inputs;
            this.ingredients = ingredients;
        }
    }
}
