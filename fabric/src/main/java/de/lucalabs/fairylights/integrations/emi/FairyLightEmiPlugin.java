package de.lucalabs.fairylights.integrations.emi;

import de.lucalabs.fairylights.main.items.FairyLightItems;
import de.lucalabs.fairylights.main.items.crafting.GenericRecipe;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@EmiEntrypoint
public class FairyLightEmiPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        final ClientLevel world = Minecraft.getInstance().level;
        final RecipeManager recipeManager = world.getRecipeManager();

        simplifyFastenerRecipes(registry);

        recipeManager.getRecipes().stream()
                .map(RecipeHolder::value)
                .filter(GenericRecipe.class::isInstance)
                .map(GenericRecipe.class::cast)
                .flatMap(r -> {
                    ItemStack output = r.getOutput();
                    List<EmiCraftingRecipe> recipes = new LinkedList<>();

//                    if (output.isIn(Tags.DYEABLE)) {
//                        for (final DyeColor color : DyeColor.values()) {
//                            ItemStack coloredOutput = DyeableItem.setColor(output, color);
//
//                            recipes.add(new EmiCraftingRecipe(
//                                    r.getIngredients().stream()
//                                            .map(i -> {
//                                                if (i.getMatchingStacks().length > 0 && Arrays.stream(i.getMatchingStacks()).allMatch(it -> it.isIn(Tags.DYES))) {
//                                                    return Ingredient.ofItems(DyeItem.byColor(color));
//                                                }
//
//                                                return i;
//                                            })
//                                            .map(EmiIngredient::of).toList(),
//                                    EmiStack.of(coloredOutput),
//                                    r.getId().withPath("/" + r.getId().getPath() + "_" + color.getName()),
//                                    false
//                            ));
//                        }
//                    } else {
                        recipes.add(new EmiCraftingRecipe(
                                r.getIngredients().stream().map(EmiIngredient::of).toList(),
                                EmiStack.of(output),
                                r.getId().withPath("/" + r.getId().getPath()),
                                false
                        ));
//                    }

                    return recipes.stream();
                })
                .forEach(registry::addRecipe);
    }

    private void simplifyFastenerRecipes(EmiRegistry registry) {
        // don't show all NBT recipes
        EmiStack hl = EmiStack.of(FairyLightItems.HANGING_LIGHTS).comparison(Comparison.of((i1, i2) -> !i1.getComponentChanges().isEmpty() || !i2.getComponentChanges().isEmpty()));
        registry.removeEmiStacks(hl);

        EmiStack pb = EmiStack.of(FairyLightItems.PENNANT_BUNTING).comparison(Comparison.of((i1, i2) -> !i1.getComponentChanges().isEmpty() || !i2.getComponentChanges().isEmpty()));
        registry.removeEmiStacks(pb);

        EmiStack newHl = EmiStack.of(FairyLightItems.HANGING_LIGHTS);
        registry.addEmiStackAfter(newHl, pb);

        EmiStack newPb = EmiStack.of(FairyLightItems.PENNANT_BUNTING);
        registry.addEmiStackAfter(newPb, pb);

        // Don't show augmentation recipes
        String[] ignoredRecipes = {
                "/crafting_special_pennant_bunting_augmentation",
                "/pennant_bunting_augmentation",
                "/crafting_special_hanging_lights_augmentation",
                "/hanging_lights_augmentation"};
        registry.removeRecipes(r -> Arrays.stream(ignoredRecipes).anyMatch(i -> r.getId() != null && i.equals(r.getId().getPath())));
    }
}
