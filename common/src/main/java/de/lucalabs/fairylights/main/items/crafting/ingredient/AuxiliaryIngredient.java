package de.lucalabs.fairylights.main.items.crafting.ingredient;

import com.google.common.collect.Multimap;
import de.lucalabs.fairylights.main.items.crafting.GenericIngredient;
import de.lucalabs.fairylights.main.items.crafting.GenericRecipe;
import de.lucalabs.fairylights.main.util.Utils;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.item.ItemStack;

public interface AuxiliaryIngredient<A> extends GenericIngredient<AuxiliaryIngredient<?>, GenericRecipe.MatchResultAuxiliary> {
    boolean isRequired();

    int getLimit();

    @Nullable
    A accumulator();

    void consume(A accumulator, ItemStack ingredient);

    boolean finish(A accumulator, PatchedDataComponentMap comps);

    default boolean process(final Multimap<AuxiliaryIngredient<?>, GenericRecipe.MatchResultAuxiliary> map, final PatchedDataComponentMap comps) {
        final Collection<GenericRecipe.MatchResultAuxiliary> results = map.get(this);
        if (results.isEmpty() && this.isRequired()) {
            return true;
        }
        final A ax = this.accumulator();
        for (final GenericRecipe.MatchResultAuxiliary result : results) {
            this.consume(ax, result.getInput());
        }
        return this.finish(ax, comps);
    }

    @Override
    default List<FormattedText> getTooltip() {
        if (!this.isRequired()) {
            return Collections.singletonList(Utils.formatRecipeTooltip("recipe.fairylights.ingredient.auxiliary.optional"));
        }
        return Collections.emptyList();
    }
}
