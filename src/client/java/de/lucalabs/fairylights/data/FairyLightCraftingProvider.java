package de.lucalabs.fairylights.data;

import de.lucalabs.fairylights.items.FairyLightItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class FairyLightCraftingProvider extends FabricRecipeProvider {
    public FairyLightCraftingProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, FairyLightItems.INCANDESCENT_LIGHT, 4)
                .pattern(" I ")
                .pattern("ITI")
                .pattern(" G ")
                .input('I', Items.IRON_INGOT)
                .input('G', Items.GLASS_PANE)
                .input('T', Items.TORCH)
                .criterion("has_iron", conditionsFromItem(Items.IRON_INGOT))
                .criterion("has_torch", conditionsFromItem(Items.TORCH))
                .offerTo(exporter);

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
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, FairyLightItems.CANDLE_LANTERN, 4)
                .pattern(" I ")
                .pattern("GTG")
                .pattern("IGI")
                .input('I', Items.IRON_INGOT)
                .input('G', Items.GOLD_NUGGET)
                .input('T', Items.TORCH)
                .criterion("has_iron", conditionsFromItem(Items.IRON_INGOT))
                .criterion("has_torch", conditionsFromItem(Items.TORCH))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, FairyLightItems.GARLAND, 2)
                .pattern("I-I")
                .input('I', Items.IRON_INGOT)
                .input('-', Items.VINE)
                .criterion("has_iron", conditionsFromItem(Items.IRON_INGOT))
                .criterion("has_vine", conditionsFromItem(Items.VINE))
                .offerTo(exporter);
    }
}
