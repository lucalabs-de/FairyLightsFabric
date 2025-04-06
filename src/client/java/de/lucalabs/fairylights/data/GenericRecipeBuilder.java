package de.lucalabs.fairylights.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.lucalabs.fairylights.FairyLights;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.CriterionMerger;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class GenericRecipeBuilder {
    private final RecipeSerializer<?> serializer;

    private final Advancement.Builder advancementBuilder = Advancement.Builder.create();

    public GenericRecipeBuilder(final RecipeSerializer<?> serializer) {
        this.serializer = Objects.requireNonNull(serializer, "serializer");
    }

    public GenericRecipeBuilder unlockedBy(final String name, final CriterionConditions criterion) {
        this.advancementBuilder.criterion(name, criterion);
        return this;
    }

    public void build(final Consumer<RecipeJsonProvider> consumer, final Identifier id) {
        final Supplier<JsonObject> advancementBuilder;
        final Identifier advancementId;
        if (this.advancementBuilder.getCriteria().isEmpty()) {
            advancementBuilder = () -> null;
            advancementId = new Identifier("");
        } else {
            advancementBuilder = this.advancementBuilder.parent(new Identifier("recipes/root"))
                    .criterion("has_the_recipe", new RecipeUnlockedCriterion.Conditions(LootContextPredicate.EMPTY, id))
                    .rewards(AdvancementRewards.Builder.recipe(id))
                    .criteriaMerger(CriterionMerger.OR)
                    ::toJson;
            advancementId = new Identifier(id.getNamespace(), "recipes/" + FairyLights.ID + "/" + id.getPath());
        }
        consumer.accept(new Result(this.serializer, id, advancementBuilder, advancementId));
    }

    public static GenericRecipeBuilder customRecipe(final RecipeSerializer<?> serializer) {
        return new GenericRecipeBuilder(serializer);
    }

    static class Result implements RecipeJsonProvider {
        final RecipeSerializer<?> serializer;

        final Identifier id;
        final Supplier<JsonObject> advancementJson;
        final Identifier advancementId;

        public Result(
                final RecipeSerializer<?> serializer,
                final Identifier id,
                final Supplier<JsonObject> advancementJson,
                final Identifier advancementId) {

            this.serializer = serializer;
            this.id = id;
            this.advancementJson = advancementJson;
            this.advancementId = advancementId;
        }

        @Override
        public void serialize(final JsonObject json) {
            json.addProperty("category", this.craftingCategory.asString());

            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }

            JsonArray jsonArray = new JsonArray();
            Iterator var3 = this.pattern.iterator();

            while(var3.hasNext()) {
                String string = (String)var3.next();
                jsonArray.add(string);
            }

            json.add("pattern", jsonArray);
            JsonObject jsonObject = new JsonObject();
            Iterator var7 = this.inputs.entrySet().iterator();

            while(var7.hasNext()) {
                Map.Entry<Character, Ingredient> entry = (Map.Entry)var7.next();
                jsonObject.add(String.valueOf(entry.getKey()), ((Ingredient)entry.getValue()).toJson());
            }

            json.add("key", jsonObject);
            JsonObject jsonObject2 = new JsonObject();
            jsonObject2.addProperty("item", Registries.ITEM.getId(this.output).toString());
            if (this.resultCount > 1) {
                jsonObject2.addProperty("count", this.resultCount);
            }

            json.add("result", jsonObject2);
            json.addProperty("show_notification", this.showNotification);
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return this.serializer;
        }

        @Override
        public Identifier getRecipeId() {
            return this.id;
        }

        @Override
        public JsonObject toAdvancementJson() {
            return this.advancementJson.get();
        }

        @Override
        public Identifier getAdvancementId() {
            return this.advancementId;
        }
    }
}
