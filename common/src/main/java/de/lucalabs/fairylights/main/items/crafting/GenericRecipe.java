package de.lucalabs.fairylights.main.items.crafting;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.math.IntMath;
import de.lucalabs.fairylights.main.items.crafting.ingredient.AuxiliaryIngredient;
import de.lucalabs.fairylights.main.items.crafting.ingredient.EmptyRegularIngredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public final class GenericRecipe extends CustomRecipe {
    public static final EmptyRegularIngredient EMPTY = new EmptyRegularIngredient();

    private final ResourceLocation id;

    private final Supplier<? extends RecipeSerializer<GenericRecipe>> serializer;
    private final ItemStack output;
    private final RegularIngredient[] ingredients;
    private final AuxiliaryIngredient<?>[] auxiliaryIngredients;

    private final int width;
    private final int height;
    private final int outputIngredient;
    private final ImmutableList<IntUnaryOperator> xFunctions = ImmutableList.of(IntUnaryOperator.identity(), i -> this.getWidth() - 1 - i);
    private ItemStack result = ItemStack.EMPTY;
    private int room;

    GenericRecipe(final ResourceLocation id, final Supplier<? extends RecipeSerializer<GenericRecipe>> serializer, final ItemStack output, final RegularIngredient[] ingredients, final AuxiliaryIngredient<?>[] auxiliaryIngredients, final int width, final int height, final int outputIngredient) {
        super(CraftingBookCategory.MISC);
        Preconditions.checkArgument(width > 0, "width must be greater than zero");
        Preconditions.checkArgument(height > 0, "height must be greater than zero");
        this.id = id;
        this.serializer = Objects.requireNonNull(serializer, "serializer");
        this.output = Objects.requireNonNull(output, "output");
        this.ingredients = Objects.requireNonNull(ingredients, "ingredients");
        this.auxiliaryIngredients = checkIngredients(ingredients, Objects.requireNonNull(auxiliaryIngredients, "auxiliaryIngredients"));
        this.width = width;
        this.height = height;
        this.outputIngredient = outputIngredient;
        this.room = -1;
    }

    private static AuxiliaryIngredient<?>[] checkIngredients(final RegularIngredient[] ingredients, final AuxiliaryIngredient<?>[] auxiliaryIngredients) {
        checkForNulls(ingredients);
        checkForNulls(auxiliaryIngredients);
        final boolean ingredientDictator = checkDictatorship(false, ingredients);
        checkDictatorship(ingredientDictator, auxiliaryIngredients);
        return auxiliaryIngredients;
    }

    private static void checkForNulls(final GenericIngredient<?, ?>[] ingredients) {
        for (int i = 0; i < ingredients.length; i++) {
            if (ingredients[i] == null) {
                throw new NullPointerException("Must not have null ingredients, found at index " + i);
            }
        }
    }

    private static boolean checkDictatorship(boolean foundDictator, final GenericIngredient<?, ?>[] ingredients) {
        for (final GenericIngredient<?, ?> ingredient : ingredients) {
            if (ingredient.dictatesOutputType()) {
                if (foundDictator) {
                    throw new IllegalRecipeException("Only one ingredient can dictate output type");
                }
                foundDictator = true;
            }
        }
        return foundDictator;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    private int getRoom() {
        if (this.room < 0) {
            int room = 0;
            for (final RegularIngredient ing : this.ingredients) {
                if (ing.getInputs().isEmpty()) {
                    room++;
                }
            }
            for (final AuxiliaryIngredient<?> aux : this.auxiliaryIngredients) {
                if (aux.isRequired()) {
                    room--;
                }
            }
            this.room = room;
        }
        return this.room;
    }

    private NonNullList<Ingredient> getDisplayIngredients() {
        // set regular ingredients according to pattern
        final NonNullList<Ingredient> ingredients = NonNullList.withSize(9, Ingredient.EMPTY);
        for (int i = 0; i < this.ingredients.length; i++) {
            final int x = i % this.width;
            final int y = i / this.width;
            final ItemStack[] stacks = this.ingredients[i].getInputs().toArray(new ItemStack[0]);
            ingredients.set(x + y * 3, Ingredient.of(stacks));
        }

        // fill empty slots with required auxiliary ingredients
        for (int i = 0, slot = 0; slot < ingredients.size(); slot++) {
            final Ingredient ing = ingredients.get(slot);
            if (ing.isEmpty()) {
                while (i < this.auxiliaryIngredients.length) {
                    final AuxiliaryIngredient<?> aux = this.auxiliaryIngredients[i++];
                    if (aux.isRequired()) {
                        final ItemStack[] stacks = aux.getInputs().toArray(new ItemStack[0]);
                        ingredients.set(slot, Ingredient.of(stacks));
                        break;
                    }
                }
            }
        }
        return ingredients;
    }

    @Override
    public boolean isSpecial() {
        return this.output.isEmpty();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return this.serializer.get();
    }

    public ItemStack getOutput() {
        return this.output.copy();
    }

    public RegularIngredient[] getGenericIngredients() {
        return this.ingredients.clone();
    }

    public AuxiliaryIngredient<?>[] getAuxiliaryIngredients() {
        return this.auxiliaryIngredients.clone();
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.getDisplayIngredients();
    }

    @Override
    public boolean canCraftInDimensions(final int width, final int height) {
        return this.width <= width
                && this.height <= height
                && (this.getRoom() >= 0 || width * height - this.width * this.height + this.getRoom() >= 0);
    }

    @Override
    public boolean matches(final CraftingInput inventory, @Nullable final Level world) {

        if (!this.canCraftInDimensions(inventory.width(), inventory.height())) {
            return false;
        }
        final int scanWidth = inventory.width() + 1 - this.width;
        final int scanHeight = inventory.height() + 1 - this.height;
        // iterate over possible origins for recipe (in case it is smaller than the crafting table)
        for (int i = 0, end = scanWidth * scanHeight; i < end; i++) {
            final int x = i % scanWidth;
            final int y = i / scanWidth;

            // recipes should work when mirrored horizontally. The first function is the identity, the other returns the index
            // counted from the back
            for (final IntUnaryOperator func : this.xFunctions) {
                final ItemStack result = this.getResult(inventory, x, y, func);
                if (!result.isEmpty()) {
                    this.result = result;
                    return true;
                }
            }
        }
        this.result = ItemStack.EMPTY;
        return false;
    }

    private ItemStack getResult(final CraftingInput inventory, final int originX, final int originY, final IntUnaryOperator funcX) {
        final MatchResultRegular[] match = new MatchResultRegular[this.ingredients.length];
        final Multimap<AuxiliaryIngredient<?>, MatchResultAuxiliary> auxMatchResults = LinkedListMultimap.create();
        final Map<AuxiliaryIngredient<?>, Integer> auxMatchTotals = new HashMap<>();
        final Set<GenericIngredient<?, ?>> presentCalled = new HashSet<>();
        final List<MatchResultAuxiliary> auxResults = new ArrayList<>();
        Item item = this.output.getItem();

        final PatchedDataComponentMap comps = new PatchedDataComponentMap(PatchedDataComponentMap.EMPTY);
        for (int i = 0, w = inventory.width(), size = w * inventory.height(); i < size; i++) {
            final int x = i % w;
            final int y = i / w;
            final int ingX = x - originX;
            final int ingY = y - originY;
            final ItemStack input = inventory.getItem(x, y);
            if (this.contains(ingX, ingY)) {
                final int index = funcX.applyAsInt(ingX) + ingY * this.width;
                final RegularIngredient ingredient = this.ingredients[index];
                final MatchResultRegular result = ingredient.matches(input);
                if (!result.doesMatch()) {
                    return ItemStack.EMPTY;
                }
                match[index] = result;
                result.forMatch(presentCalled, comps);
                if (index == this.outputIngredient) {
                    comps.setAll(input.getComponents());
                    item = input.getItem();
                }
            } else if (!EMPTY.matches(input).doesMatch()) {
                boolean nonAuxiliary = true;
                for (final AuxiliaryIngredient<?> auxiliaryIngredient : this.auxiliaryIngredients) {
                    final MatchResultAuxiliary result = auxiliaryIngredient.matches(input);
                    if (result.doesMatch()) {
                        if (result.isAtLimit(auxMatchTotals.getOrDefault(result.ingredient, 0))) {
                            return ItemStack.EMPTY;
                        }
                        result.forMatch(presentCalled, comps);
                        auxMatchTotals.merge(result.ingredient, 1, IntMath::checkedAdd);
                        nonAuxiliary = false;
                        result.propagate(auxMatchResults);
                    }
                    auxResults.add(result);
                }
                if (nonAuxiliary) {
                    return ItemStack.EMPTY;
                }
            }
        }
        final Set<GenericIngredient<?, ?>> absentCalled = new HashSet<>();

        for (final MatchResultRegular result : match) {
            result.notifyAbsence(presentCalled, absentCalled, comps);
        }

        for (final MatchResultAuxiliary result : auxResults) {
            result.notifyAbsence(presentCalled, absentCalled, comps);
        }

        for (final AuxiliaryIngredient<?> ingredient : this.auxiliaryIngredients) {
            if (ingredient.process(auxMatchResults, comps)) {
                return ItemStack.EMPTY;
            }
        }

        final ItemStack output = this.output.isEmpty() ? new ItemStack(item) : this.output.copy();
        output.applyComponents(comps);
        return output;
    }

    private boolean contains(final int x, final int y) {
        return x >= 0 && y >= 0 && x < this.width && y < this.height;
    }

    @Override
    public @NotNull ItemStack assemble(final @NotNull CraftingInput inventory, final HolderLookup.Provider lookup) {
        final ItemStack result = this.result;
        return result.isEmpty() ? result : result.copy();
    }

    public interface MatchResult<I extends GenericIngredient<I, M>, M extends MatchResult<I, M>> {
        I getIngredient();

        ItemStack getInput();

        boolean doesMatch();

        void forMatch(final Set<GenericIngredient<?, ?>> called, final PatchedDataComponentMap comps);

        void notifyAbsence(final Set<GenericIngredient<?, ?>> presentCalled, final Set<GenericIngredient<?, ?>> absentCalled, final PatchedDataComponentMap comps);

        M withParent(final M parent);
    }

    public static class MatchResultRegular implements MatchResult<RegularIngredient, MatchResultRegular> {
        protected final RegularIngredient ingredient;

        protected final ItemStack input;

        protected final boolean doesMatch;

        protected final ImmutableList<MatchResultRegular> supplementaryResults;

        public MatchResultRegular(final RegularIngredient ingredient, final ItemStack input, final boolean doesMatch, final List<MatchResultRegular> supplementaryResults) {
            this.ingredient = Objects.requireNonNull(ingredient, "ingredient");
            this.input = input;
            this.doesMatch = doesMatch;
            this.supplementaryResults = ImmutableList.copyOf(supplementaryResults);
        }

        @Override
        public final RegularIngredient getIngredient() {
            return this.ingredient;
        }

        @Override
        public final ItemStack getInput() {
            return this.input;
        }

        @Override
        public final boolean doesMatch() {
            return this.doesMatch;
        }

        @Override
        public void forMatch(final Set<GenericIngredient<?, ?>> called, final PatchedDataComponentMap comps) {
            this.ingredient.matched(this.input, comps);
            if (called.add(this.ingredient)) {
                this.ingredient.present(comps);
            }
        }

        @Override
        public void notifyAbsence(final Set<GenericIngredient<?, ?>> presentCalled, final Set<GenericIngredient<?, ?>> absentCalled, final PatchedDataComponentMap comps) {
            if (!presentCalled.contains(this.ingredient) && !absentCalled.contains(this.ingredient)) {
                this.ingredient.absent(comps);
                absentCalled.add(this.ingredient);
            }
            for (final MatchResultRegular result : this.supplementaryResults) {
                result.notifyAbsence(presentCalled, absentCalled, comps);
            }
        }

        @Override
        public MatchResultRegular withParent(final MatchResultRegular parent) {
            return new MatchResultParentedRegular(this.ingredient, this.input, this.doesMatch, this.supplementaryResults, parent);
        }
    }

    public static class MatchResultParentedRegular extends MatchResultRegular {
        protected final MatchResultRegular parent;

        public MatchResultParentedRegular(final RegularIngredient ingredient, final ItemStack input, final boolean doesMatch, final List<MatchResultRegular> supplementaryResults, final MatchResultRegular parent) {
            super(ingredient, input, doesMatch, supplementaryResults);
            this.parent = Objects.requireNonNull(parent, "parent");
        }

        @Override
        public void forMatch(final Set<GenericIngredient<?, ?>> called, final PatchedDataComponentMap comps) {
            super.forMatch(called, comps);
            this.parent.forMatch(called, comps);
        }

        @Override
        public void notifyAbsence(final Set<GenericIngredient<?, ?>> presentCalled, final Set<GenericIngredient<?, ?>> absentCalled, final PatchedDataComponentMap comps) {
            super.notifyAbsence(presentCalled, absentCalled, comps);
            this.parent.notifyAbsence(presentCalled, absentCalled, comps);
        }

        @Override
        public MatchResultRegular withParent(final MatchResultRegular parent) {
            return this.parent.withParent(new MatchResultParentedRegular(this.ingredient, this.input, this.doesMatch, this.supplementaryResults, parent));
        }
    }

    public static class MatchResultAuxiliary implements MatchResult<AuxiliaryIngredient<?>, MatchResultAuxiliary> {
        protected final AuxiliaryIngredient<?> ingredient;

        protected final ItemStack input;

        protected final boolean doesMatch;

        protected final ImmutableList<MatchResultAuxiliary> supplementaryResults;

        public MatchResultAuxiliary(final AuxiliaryIngredient<?> ingredient, final ItemStack input, final boolean doesMatch, final List<MatchResultAuxiliary> supplementaryResults) {
            this.ingredient = Objects.requireNonNull(ingredient, "ingredient");
            this.input = input;
            this.doesMatch = doesMatch;
            this.supplementaryResults = ImmutableList.copyOf(supplementaryResults);
        }

        @Override
        public final AuxiliaryIngredient<?> getIngredient() {
            return this.ingredient;
        }

        @Override
        public final ItemStack getInput() {
            return this.input;
        }

        @Override
        public final boolean doesMatch() {
            return this.doesMatch;
        }

        @Override
        public void forMatch(final Set<GenericIngredient<?, ?>> called, final PatchedDataComponentMap comps) {
            if (!called.contains(this.ingredient)) {
                this.ingredient.present(comps);
                called.add(this.ingredient);
            }
        }

        @Override
        public void notifyAbsence(final Set<GenericIngredient<?, ?>> presentCalled, final Set<GenericIngredient<?, ?>> absentCalled, final PatchedDataComponentMap comps) {
            if (!presentCalled.contains(this.ingredient) && !absentCalled.contains(this.ingredient)) {
                this.ingredient.absent(comps);
                absentCalled.add(this.ingredient);
            }
            for (final MatchResultAuxiliary result : this.supplementaryResults) {
                result.notifyAbsence(presentCalled, absentCalled, comps);
            }
        }

        @Override
        public MatchResultAuxiliary withParent(final MatchResultAuxiliary parent) {
            return new MatchResultParentedAuxiliary(this.ingredient, this.input, this.doesMatch, this.supplementaryResults, parent);
        }

        public boolean isAtLimit(final int count) {
            return count >= this.ingredient.getLimit();
        }

        public void propagate(final Multimap<AuxiliaryIngredient<?>, MatchResultAuxiliary> map) {
            map.put(this.ingredient, this);
        }
    }

    public static class MatchResultParentedAuxiliary extends MatchResultAuxiliary {
        protected final MatchResultAuxiliary parent;

        public MatchResultParentedAuxiliary(final AuxiliaryIngredient<?> ingredient, final ItemStack input, final boolean doesMatch, final List<MatchResultAuxiliary> supplementaryResults, final MatchResultAuxiliary parent) {
            super(ingredient, input, doesMatch, supplementaryResults);
            this.parent = Objects.requireNonNull(parent, "parent");
        }

        @Override
        public void forMatch(final Set<GenericIngredient<?, ?>> called, final PatchedDataComponentMap comps) {
            super.forMatch(called, comps);
            this.parent.forMatch(called, comps);
        }

        @Override
        public void notifyAbsence(final Set<GenericIngredient<?, ?>> presentCalled, final Set<GenericIngredient<?, ?>> absentCalled, final PatchedDataComponentMap comps) {
            super.notifyAbsence(presentCalled, absentCalled, comps);
            this.parent.notifyAbsence(presentCalled, absentCalled, comps);
        }

        @Override
        public MatchResultAuxiliary withParent(final MatchResultAuxiliary parent) {
            return this.parent.withParent(new MatchResultParentedAuxiliary(this.ingredient, this.input, this.doesMatch, this.supplementaryResults, parent));
        }

        @Override
        public boolean isAtLimit(final int count) {
            return super.isAtLimit(count) || this.parent.isAtLimit(count);
        }

        @Override
        public void propagate(final Multimap<AuxiliaryIngredient<?>, MatchResultAuxiliary> map) {
            super.propagate(map);
            this.parent.propagate(map);
        }
    }
}
