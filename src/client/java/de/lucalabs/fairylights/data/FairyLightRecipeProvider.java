package de.lucalabs.fairylights.data;

import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.items.FairyLightItems;
import de.lucalabs.fairylights.items.crafting.FairyLightCraftingRecipes;
import de.lucalabs.fairylights.util.Tags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public final class FairyLightRecipeProvider extends FabricRecipeProvider {
    public FairyLightRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> consumer) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, FairyLightItems.INCANDESCENT_LIGHT, 4)
                .pattern(" I ")
                .pattern("ITI")
                .pattern(" G ")
                .input('I', Items.IRON_INGOT)
                .input('G', Items.GLASS_PANE)
                .input('T', Items.TORCH)
                .criterion("has_iron", conditionsFromItem(Items.IRON_INGOT))
                .criterion("has_torch", conditionsFromItem(Items.TORCH))
                .offerTo(consumer);

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, FairyLightItems.OIL_LANTERN, 4)
                .pattern(" I ")
                .pattern("STS")
                .pattern("IGI")
                .input('I', Items.IRON_INGOT)
                .input('G', Items.GLASS_PANE)
                .input('T', Items.TORCH)
                .input('S', Items.STICK)
                .criterion("has_iron", conditionsFromItem(Items.IRON_INGOT))
                .criterion("has_torch", conditionsFromItem(Items.TORCH))
                .offerTo(consumer);

        GenericRecipeBuilder.customRecipe(FairyLightCraftingRecipes.HANGING_LIGHTS)
                .unlockedBy("has_lights", conditionsFromTag(Tags.LIGHTS))
                .build(consumer, new Identifier(FairyLights.ID, "hanging_lights"));

        GenericRecipeBuilder.customRecipe(FairyLightCraftingRecipes.HANGING_LIGHTS_AUGMENTATION)
                .build(consumer, new Identifier(FairyLights.ID, "hanging_lights_augmentation"));

        GenericRecipeBuilder.customRecipe(FairyLightCraftingRecipes.PENNANT_BUNTING)
                .unlockedBy("has_pennants", conditionsFromTag(Tags.PENNANTS))
                .build(consumer, new Identifier(FairyLights.ID, "pennant_bunting"));

        GenericRecipeBuilder.customRecipe(FairyLightCraftingRecipes.PENNANT_BUNTING_AUGMENTATION)
                .build(consumer, new Identifier(FairyLights.ID, "pennant_bunting_augmentation"));

        GenericRecipeBuilder.customRecipe(FairyLightCraftingRecipes.EDIT_COLOR)
                .build(consumer, new Identifier(FairyLights.ID, "edit_color"));

        GenericRecipeBuilder.customRecipe(FairyLightCraftingRecipes.COPY_COLOR)
                .build(consumer, new Identifier(FairyLights.ID, "copy_color"));

        this.pennantRecipe(FairyLightCraftingRecipes.TRIANGLE_PENNANT)
                .build(consumer, new Identifier(FairyLights.ID, "triangle_pennant"));

        this.pennantRecipe(FairyLightCraftingRecipes.SQUARE_PENNANT)
                .build(consumer, new Identifier(FairyLights.ID, "square_pennant"));

        this.lightRecipe(FairyLightCraftingRecipes.FAIRY_LIGHT)
                .build(consumer, new Identifier(FairyLights.ID, "fairy_light"));

        GenericRecipeBuilder.customRecipe(FairyLightCraftingRecipes.LIGHT_TWINKLE)
                .build(consumer, new Identifier(FairyLights.ID, "light_twinkle"));

        GenericRecipeBuilder.customRecipe(FairyLightCraftingRecipes.COLOR_CHANGING_LIGHT)
                .build(consumer, new Identifier(FairyLights.ID, "color_changing_light"));
    }

    GenericRecipeBuilder lightRecipe(final RecipeSerializer<?> serializer) {
        return GenericRecipeBuilder.customRecipe(serializer)
                .unlockedBy("has_iron", conditionsFromItem(Items.IRON_INGOT))
                .unlockedBy("has_dye", conditionsFromTag(Tags.DYES));
    }

    GenericRecipeBuilder pennantRecipe(final RecipeSerializer<?> serializer) {
        return GenericRecipeBuilder.customRecipe(serializer)
                .unlockedBy("has_paper", conditionsFromItem(Items.PAPER))
                .unlockedBy("has_string", conditionsFromItem(Items.STRING));
    }
}
