package de.lucalabs.fairylights.integrations.jei;

import de.lucalabs.fairylights.FairyLights;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class FairyLightJeiPlugin implements IModPlugin {

    @Override
    public @NotNull Identifier getPluginUid() {
        return Identifier.of(FairyLights.ID, "plugin");
    }

    @Override
    public void registerVanillaCategoryExtensions(final IVanillaCategoryExtensionRegistration registration) {
//        registration.getCraftingCategory().addCategoryExtension(GenericRecipe.class, GenericRecipeWrapper::new);
    }

    @Override
    public void registerRecipes(final IRecipeRegistration registration) {
        final ClientWorld world = MinecraftClient.getInstance().world;
        final RecipeManager recipeManager = world.getRecipeManager();
//        registration.addRecipes(
//                RecipeTypes.CRAFTING,
//                recipeManager.values().stream()
//                        .filter(GenericRecipe.class::isInstance)
//                        .map(GenericRecipe.class::cast)
//                        .filter(GenericRecipe::isIgnoredInRecipeBook)
//                        .collect(Collectors.toList()));
    }

    @Override
    public void registerItemSubtypes(final ISubtypeRegistration registry) {
//        registry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, FairyLightItems.TRIANGLE_PENNANT, new ColorSubtypeInterpreter());
//        registry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, FairyLightItems.SQUARE_PENNANT, new ColorSubtypeInterpreter());
//        FairyLightItems.lights().forEach(i -> registry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, i, new ColorSubtypeInterpreter()));
    }
}
