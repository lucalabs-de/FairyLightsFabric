package de.lucalabs.fairylights.client.data;

import de.lucalabs.fairylights.main.items.FairyLightItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class FairyLightCraftingProvider extends RecipeProvider {
    public FairyLightCraftingProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput exporter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, FairyLightItems.INCANDESCENT_LIGHT, 4)
                .pattern(" I ")
                .pattern("ITI")
                .pattern(" G ")
                .define('I', Items.IRON_INGOT)
                .define('G', Items.GLASS_PANE)
                .define('T', Items.TORCH)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .unlockedBy("has_torch", has(Items.TORCH))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, FairyLightItems.OIL_LANTERN, 4)
                .pattern(" I ")
                .pattern("STS")
                .pattern("IGI")
                .define('I', Items.IRON_INGOT)
                .define('G', Items.GLASS_PANE)
                .define('T', Items.TORCH)
                .define('S', Items.STICK)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .unlockedBy("has_torch", has(Items.TORCH))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, FairyLightItems.CANDLE_LANTERN, 4)
                .pattern(" I ")
                .pattern("GTG")
                .pattern("IGI")
                .define('I', Items.IRON_INGOT)
                .define('G', Items.GOLD_NUGGET)
                .define('T', Items.TORCH)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .unlockedBy("has_torch", has(Items.TORCH))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, FairyLightItems.GARLAND, 2)
                .pattern("I-I")
                .define('I', Items.IRON_INGOT)
                .define('-', Items.VINE)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .unlockedBy("has_vine", has(Items.VINE))
                .save(exporter);
    }

}
