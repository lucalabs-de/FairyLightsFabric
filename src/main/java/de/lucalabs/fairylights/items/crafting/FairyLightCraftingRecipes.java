package de.lucalabs.fairylights.items.crafting;

import com.google.common.collect.ImmutableList;
import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.components.FairyLightComponents;
import de.lucalabs.fairylights.items.DyeableItem;
import de.lucalabs.fairylights.items.FairyLightItems;
import de.lucalabs.fairylights.items.HangingLightsConnectionItem;
import de.lucalabs.fairylights.items.crafting.ingredient.BasicAuxiliaryIngredient;
import de.lucalabs.fairylights.items.crafting.ingredient.BasicRegularIngredient;
import de.lucalabs.fairylights.items.crafting.ingredient.InertBasicAuxiliaryIngredient;
import de.lucalabs.fairylights.items.crafting.ingredient.LazyTagIngredient;
import de.lucalabs.fairylights.string.StringTypes;
import de.lucalabs.fairylights.util.Blender;
import de.lucalabs.fairylights.util.OreDictUtils;
import de.lucalabs.fairylights.util.Tags;
import de.lucalabs.fairylights.util.Utils;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class FairyLightCraftingRecipes {

    public static final RecipeSerializer<SpecialCraftingRecipe> COPY_COLOR = register(
            "crafting_special_copy_color",
            () -> new SpecialRecipeSerializer<>(CopyColorRecipe::new));
    public static final RegularIngredient DYE_SUBTYPE_INGREDIENT = new BasicRegularIngredient(LazyTagIngredient.of(Tags.DYES)) {
        @Override
        public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
            return DyeableItem.getDyeColor(output).map(dye -> ImmutableList.of(OreDictUtils.getDyes(dye))).orElse(ImmutableList.of());
        }

        @Override
        public boolean dictatesOutputType() {
            return true;
        }

        @Override
        public void matched(final ItemStack ingredient, final ComponentMapImpl comps) {
            DyeableItem.setColor(comps, OreDictUtils.getDyeColor(ingredient));
        }
    };
    private FairyLightCraftingRecipes() {
    }    public static final RecipeSerializer<GenericRecipe> HANGING_LIGHTS = register(
            "crafting_special_hanging_lights",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createHangingLights));

    private static GenericRecipe createDyeColor(CraftingRecipeCategory craftingBookCategory) {
        return new GenericRecipeBuilder(() -> EDIT_COLOR)
                .withShape("I")
                .withIngredient('I', Tags.DYEABLE).withOutput('I')
                .withAuxiliaryIngredient(new BasicAuxiliaryIngredient<Blender>(LazyTagIngredient.of(Tags.DYES), true, 8) {
                    @Override
                    public Blender accumulator() {
                        return new Blender();
                    }

                    @Override
                    public void consume(final Blender data, final ItemStack ingredient) {
                        data.add(DyeableItem.getColor(OreDictUtils.getDyeColor(ingredient)));
                    }

                    @Override
                    public boolean finish(final Blender data, final ComponentMapImpl comps) {
                        DyeableItem.setColor(comps, data.blend());
                        return false;
                    }
                })
                .build();
    }

    private static GenericRecipe createLightTwinkle(CraftingRecipeCategory craftingBookCategory) {
        return new GenericRecipeBuilder(() -> LIGHT_TWINKLE)
                .withShape("L")
                .withIngredient('L', Tags.TWINKLING_LIGHTS).withOutput('L')
                .withAuxiliaryIngredient(new InertBasicAuxiliaryIngredient(Ingredient.ofItems(Items.GLOWSTONE_DUST), true, 1) {
                    @Override
                    public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
                        return Boolean.TRUE.equals(output.get(FairyLightComponents.Lights.TWINKLE)) ? super.getInput(output) : ImmutableList.of();
                    }

                    @Override
                    public void present(final ComponentMapImpl comps) {
                        comps.set(FairyLightComponents.Lights.TWINKLE, true);
                    }

                    @Override
                    public void absent(final ComponentMapImpl comps) {
                        comps.set(FairyLightComponents.Lights.TWINKLE, false);
                    }

                    @Override
                    public void addTooltip(final List<Text> tooltip) {
                        super.addTooltip(tooltip);
                        tooltip.add(Utils.formatRecipeTooltip("recipe.fairylights.twinkling_lights.glowstone"));
                    }
                })
                .build();
    }

    private static GenericRecipe createColorChangingLight(CraftingRecipeCategory craftingBookCategory) {
        return new GenericRecipeBuilder(() -> COLOR_CHANGING_LIGHT)
                .withShape("IG")
                .withIngredient('I', Tags.DYEABLE_LIGHTS).withOutput('I')
                .withIngredient('G', Items.GOLD_NUGGET)
                .withAuxiliaryIngredient(new BasicAuxiliaryIngredient<List<Integer>>(LazyTagIngredient.of(Tags.DYES), true, 8) {
                    @Override
                    public List<Integer> accumulator() {
                        return Collections.emptyList();
                    }

                    @Override
                    public void consume(final List<Integer> data, final ItemStack ingredient) {
                        data.add(DyeableItem.getColor(OreDictUtils.getDyeColor(ingredient)));
                    }

                    @Override
                    public boolean finish(final List<Integer> data, final ComponentMapImpl comps) {
                        if (!data.isEmpty()) {
                            if (comps.contains(FairyLightComponents.Dyeable.COLOR)) {
                                data.addFirst(comps.get(FairyLightComponents.Dyeable.COLOR));
                                comps.remove(FairyLightComponents.Dyeable.COLOR);
                            }
                            comps.set(FairyLightComponents.Dyeable.COLORS, data);
                        }
                        return false;
                    }
                })
                .build();
    }    public static final RecipeSerializer<GenericRecipe> HANGING_LIGHTS_AUGMENTATION = register(
            "crafting_special_hanging_lights_augmentation",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createHangingLightsAugmentation));

    private static GenericRecipe createHangingLights(CraftingRecipeCategory craftingBookCategory) {
        return new GenericRecipeBuilder(() -> HANGING_LIGHTS, FairyLightItems.HANGING_LIGHTS)
                .withShape("I-I")
                .withIngredient('I', Items.IRON_INGOT)
                .withIngredient('-', Items.STRING)
                .withAuxiliaryIngredient(new LightIngredient(true))
                .withAuxiliaryIngredient(new InertBasicAuxiliaryIngredient(LazyTagIngredient.of(Tags.DYES_WHITE), false, 1) {
                    @Override
                    public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
                        final var tag = output.getComponents();
                        return tag != null && HangingLightsConnectionItem.getString(output.getComponents()) == StringTypes.WHITE_STRING ? super.getInput(output) : ImmutableList.of();
                    }

                    @Override
                    public void present(final ComponentMapImpl comps) {
                        HangingLightsConnectionItem.setString(comps, StringTypes.WHITE_STRING);
                    }

                    @Override
                    public void absent(final ComponentMapImpl comps) {
                        HangingLightsConnectionItem.setString(comps, StringTypes.BLACK_STRING);
                    }

                    @Override
                    public void addTooltip(final List<Text> tooltip) {
                        super.addTooltip(tooltip);
                        tooltip.add(Utils.formatRecipeTooltip("recipe.fairylights.hangingLights.string"));
                    }
                })
                .build();
    }

    /*
     *  The JEI shown recipe is adding glowstone, eventually I should allow a recipe to provide a number of
     *  different recipe layouts the the input ingredients can be generated for so I could show applying a
     *  new light pattern as well.
     */
    private static GenericRecipe createHangingLightsAugmentation(CraftingRecipeCategory craftingBookCategory) {
        return new GenericRecipeBuilder(() -> HANGING_LIGHTS_AUGMENTATION, FairyLightItems.HANGING_LIGHTS)
                .withShape("F")
                .withIngredient('F', new BasicRegularIngredient(Ingredient.ofItems(FairyLightItems.HANGING_LIGHTS)) {
                    @Override
                    public ImmutableList<ItemStack> getInputs() {
                        return Arrays.stream(this.ingredient.getMatchingStacks())
                                .map(ItemStack::copy)
                                .flatMap(stack -> makeHangingLightsExamples(stack).stream())
                                .collect(ImmutableList.toImmutableList());
                    }

                    @Override
                    public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
                        final ItemStack stack = output.copy();
//                        final NbtCompound compound = stack.getNbt();
//                        if (compound == null) {
//                            return ImmutableList.of();
//                        } TODO check if this can just be removed
                        stack.setCount(1);
                        return ImmutableList.of(ImmutableList.of(stack));
                    }

                    @Override
                    public void matched(final ItemStack ingredient, final ComponentMapImpl comps) {
                        comps.setAll(ingredient.getComponents());
                    }
                })
                .withAuxiliaryIngredient(new LightIngredient(true) {
                    @Override
                    public ImmutableList<ItemStack> getInputs() {
                        return ImmutableList.of();
                    }

                    @Override
                    public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
                        return ImmutableList.of();
                    }
                })
                .build();
    }

    private static ImmutableList<ItemStack> makeHangingLightsExamples(final ItemStack stack) {
        return ImmutableList.of(
                makeHangingLights(stack, DyeColor.CYAN, DyeColor.MAGENTA, DyeColor.CYAN, DyeColor.WHITE),
                makeHangingLights(stack, DyeColor.CYAN, DyeColor.LIGHT_BLUE, DyeColor.CYAN, DyeColor.LIGHT_BLUE),
                makeHangingLights(stack, DyeColor.LIGHT_GRAY, DyeColor.PINK, DyeColor.CYAN, DyeColor.GREEN),
                makeHangingLights(stack, DyeColor.LIGHT_GRAY, DyeColor.PURPLE, DyeColor.LIGHT_GRAY, DyeColor.GREEN),
                makeHangingLights(stack, DyeColor.CYAN, DyeColor.YELLOW, DyeColor.CYAN, DyeColor.PURPLE)
        );
    }    public static final RecipeSerializer<GenericRecipe> PENNANT_BUNTING = register(
            "crafting_special_pennant_bunting",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createPennantBunting));

    public static ItemStack makeHangingLights(final ItemStack base, final DyeColor... colors) {
        final ItemStack stack = base.copy();

        final List<ItemStack> lights = new ArrayList<>();
        for (final DyeColor color : colors) {
            lights.add(DyeableItem.setColor(new ItemStack(FairyLightItems.FAIRY_LIGHT), color));
        }

        stack.set(FairyLightComponents.Connection.PATTERN, lights);
        HangingLightsConnectionItem.setString(stack, StringTypes.BLACK_STRING);

        return stack;
    }

    private static GenericRecipe createPennantBunting(CraftingRecipeCategory craftingBookCategory) {
        return new GenericRecipeBuilder(() -> PENNANT_BUNTING, FairyLightItems.PENNANT_BUNTING)
                .withShape("I-I")
                .withIngredient('I', Items.IRON_INGOT)
                .withIngredient('-', Items.STRING)
                .withAuxiliaryIngredient(new PennantIngredient())
                .build();
    }

    private static GenericRecipe createPennantBuntingAugmentation(CraftingRecipeCategory craftingBookCategory) {
        return new GenericRecipeBuilder(() -> PENNANT_BUNTING_AUGMENTATION, FairyLightItems.PENNANT_BUNTING)
                .withShape("B")
                .withIngredient('B', new BasicRegularIngredient(Ingredient.ofItems(FairyLightItems.PENNANT_BUNTING)) {
                    @Override
                    public ImmutableList<ItemStack> getInputs() {
                        return Arrays.stream(this.ingredient.getMatchingStacks())
                                .map(ItemStack::copy)
                                .flatMap(stack -> {
                                    return makePennantExamples(stack).stream();
                                }).collect(ImmutableList.toImmutableList());
                    }

                    @Override
                    public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
                        return ImmutableList.of(makePennantExamples(output));
                    }

                    @Override
                    public void matched(final ItemStack ingredient, final ComponentMapImpl comps) {
                        comps.setAll(ingredient.getComponents());
                    }
                })
                .withAuxiliaryIngredient(new PennantIngredient())
                .build();
    }    public static final RecipeSerializer<GenericRecipe> PENNANT_BUNTING_AUGMENTATION = register(
            "crafting_special_pennant_bunting_augmentation",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createPennantBuntingAugmentation));

    private static ImmutableList<ItemStack> makePennantExamples(final ItemStack stack) {
        return ImmutableList.of(
                makePennant(stack, DyeColor.BLUE, DyeColor.YELLOW, DyeColor.RED),
                makePennant(stack, DyeColor.PINK, DyeColor.LIGHT_BLUE),
                makePennant(stack, DyeColor.ORANGE, DyeColor.WHITE),
                makePennant(stack, DyeColor.LIME, DyeColor.YELLOW)
        );
    }

    public static ItemStack makePennant(final ItemStack base, final DyeColor... colors) {
        final ItemStack stack = base.copy();
        final List<ItemStack> pennants = new ArrayList<>();

        for (final DyeColor color : colors) {
            final ItemStack pennant = new ItemStack(FairyLightItems.TRIANGLE_PENNANT);
            DyeableItem.setColor(pennant, color);
            pennants.add(pennant);
        }

        stack.set(FairyLightComponents.Connection.PATTERN, pennants);

        return stack;
    }

    private static GenericRecipe createPennant(final Supplier<RecipeSerializer<GenericRecipe>> serializer, final Item item, final String pattern) {
        return new GenericRecipeBuilder(serializer, item)
                .withShape("- -", "PDP", pattern)
                .withIngredient('P', Items.PAPER)
                .withIngredient('-', Items.STRING)
                .withIngredient('D', DYE_SUBTYPE_INGREDIENT)
                .build();
    }    public static final RecipeSerializer<GenericRecipe> TRIANGLE_PENNANT = register(
            "crafting_special_triangle_pennant",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createTrianglePennant));

    private static GenericRecipe createTrianglePennant(CraftingRecipeCategory craftingBookCategory) {
        return createPennant(() -> TRIANGLE_PENNANT, FairyLightItems.TRIANGLE_PENNANT, " P ");
    }

    private static GenericRecipe createSquarePennant(CraftingRecipeCategory craftingBookCategory) {
        return createPennant(() -> SQUARE_PENNANT, FairyLightItems.SQUARE_PENNANT, "PPP");
    }

    private static GenericRecipe createFairyLight(CraftingRecipeCategory craftingBookCategory) {
        return createLight(() -> FAIRY_LIGHT, () -> FairyLightItems.FAIRY_LIGHT, b -> b
                .withShape(" I ", "IDI", " G ")
                .withIngredient('G', Items.GLASS_PANE)
        );
    }    public static final RecipeSerializer<GenericRecipe> SQUARE_PENNANT = register(
            "crafting_special_square_pennant",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createSquarePennant));

    private static GenericRecipe createLight(final Supplier<? extends RecipeSerializer<GenericRecipe>> serializer, final Supplier<? extends Item> variant, final UnaryOperator<GenericRecipeBuilder> recipe) {
        return recipe.apply(new GenericRecipeBuilder(serializer))
                .withIngredient('I', Items.IRON_INGOT)
                .withIngredient('D', DYE_SUBTYPE_INGREDIENT)
                .withOutput(variant.get(), 4)
                .build();
    }

    private static <T extends RecipeSerializer<?>> T register(final String name, Supplier<T> supplier) {
        Identifier identifier = Identifier.of(FairyLights.ID, name);
        return Registry.register(Registries.RECIPE_SERIALIZER, identifier, supplier.get());
    }

    public static void initialize() {
        FairyLights.LOGGER.info("Registering items");
    }    public static final RecipeSerializer<GenericRecipe> FAIRY_LIGHT = register(
            "crafting_special_fairy_light",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createFairyLight));

    private static class LightIngredient extends BasicAuxiliaryIngredient<List<ItemStack>> {
        private LightIngredient(final boolean isRequired) {
            super(LazyTagIngredient.of(Tags.LIGHTS), isRequired, 8);
        }

        @Override
        public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
            final List<ItemStack> pattern = output.get(FairyLightComponents.Connection.PATTERN);
            if (pattern == null || pattern.isEmpty()) {
                return ImmutableList.of();
            }
            final ImmutableList.Builder<ImmutableList<ItemStack>> lights = ImmutableList.builder();
            for (ItemStack i : pattern) {
                lights.add(ImmutableList.of(i));
            }
            return lights.build();
        }

        @Override
        public boolean dictatesOutputType() {
            return true;
        }

        @Override
        public List<ItemStack> accumulator() {
            return Collections.emptyList();
        }

        @Override
        public void consume(final List<ItemStack> patternList, final ItemStack ingredient) {
            patternList.add(ingredient);
        }

        @Override
        public boolean finish(final List<ItemStack> pattern, final ComponentMapImpl comps) {
            if (!pattern.isEmpty()) {
                comps.set(FairyLightComponents.Connection.PATTERN, pattern);
            }
            return false;
        }

        @Override
        public void addTooltip(final List<Text> tooltip) {
            tooltip.add(Utils.formatRecipeTooltip("recipe.fairylights.hangingLights.light"));
        }
    }

    private static class PennantIngredient extends BasicAuxiliaryIngredient<List<ItemStack>> {
        private PennantIngredient() {
            super(LazyTagIngredient.of(Tags.PENNANTS), true, 8);
        }

        @Override
        public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
            final List<ItemStack> pattern = output.get(FairyLightComponents.Connection.PATTERN);
            if (pattern == null || pattern.isEmpty()) {
                return ImmutableList.of();
            }
            final ImmutableList.Builder<ImmutableList<ItemStack>> pennants = ImmutableList.builder();
            for (ItemStack i : pattern) {
                pennants.add(ImmutableList.of(i));
            }
            return pennants.build();
        }

        @Override
        public boolean dictatesOutputType() {
            return true;
        }

        @Override
        public List<ItemStack> accumulator() {
            return Collections.emptyList();
        }

        @Override
        public void consume(final List<ItemStack> patternList, final ItemStack ingredient) {
            patternList.add(ingredient);
        }

        @Override
        public boolean finish(final List<ItemStack> pattern, final ComponentMapImpl comps) {
            if (!pattern.isEmpty()) {
                comps.set(FairyLightComponents.Connection.PATTERN, pattern);
            }
            return false;
        }

        @Override
        public void addTooltip(final List<Text> tooltip) {
            tooltip.add(Utils.formatRecipeTooltip("recipe.fairylights.pennantBunting.pennant"));
        }
    }

    public static final RecipeSerializer<GenericRecipe> LIGHT_TWINKLE = register(
            "crafting_special_light_twinkle",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createLightTwinkle));





    public static final RecipeSerializer<GenericRecipe> COLOR_CHANGING_LIGHT = register(
            "crafting_special_color_changing_light",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createColorChangingLight));





    public static final RecipeSerializer<GenericRecipe> EDIT_COLOR = register(
            "crafting_special_edit_color",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createDyeColor));








}
