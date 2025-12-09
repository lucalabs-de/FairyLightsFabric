package de.lucalabs.fairylights.integrations.jei;

import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.items.FairyLightItems;
import de.lucalabs.fairylights.items.crafting.GenericRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

@JeiPlugin
public class FairyLightJeiPlugin implements IModPlugin {

    @Override
    public @NotNull Identifier getPluginUid() {
        return Identifier.of(FairyLights.ID, "plugin");
    }

    @Override
    public void registerVanillaCategoryExtensions(final IVanillaCategoryExtensionRegistration registration) {
        registration.getCraftingCategory().addExtension(GenericRecipe.class, new GenericRecipeWrapper());
    }

    @Override
    public void registerRecipes(final IRecipeRegistration registration) {
        final ClientWorld world = MinecraftClient.getInstance().world;
        final RecipeManager recipeManager = world.getRecipeManager();
        registration.addRecipes(
                RecipeTypes.CRAFTING,
                recipeManager.values().stream()
                        .map(RecipeEntry::value)
                        .filter(GenericRecipe.class::isInstance)
                        .map(GenericRecipe.class::cast)
                        .filter(GenericRecipe::isIgnoredInRecipeBook)
                        .map(x -> new RecipeEntry<CraftingRecipe>(x.getId(), x))
                        .collect(Collectors.toList()));
    }

    @Override
    public void registerItemSubtypes(final ISubtypeRegistration registry) {
        registry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, FairyLightItems.TRIANGLE_PENNANT, new ColorSubtypeInterpreter());
        registry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, FairyLightItems.SQUARE_PENNANT, new ColorSubtypeInterpreter());
        FairyLightItems.lights().forEach(i -> registry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, i, new ColorSubtypeInterpreter()));
    }
}
