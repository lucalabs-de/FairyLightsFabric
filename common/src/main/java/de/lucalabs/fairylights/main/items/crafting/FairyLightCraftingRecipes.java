package de.lucalabs.fairylights.main.items.crafting;

import com.google.common.collect.ImmutableList;
import de.lucalabs.fairylights.Common;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import de.lucalabs.fairylights.Constants;
import de.lucalabs.fairylights.main.items.DyeableItem;
import de.lucalabs.fairylights.main.items.FairyLightItems;
import de.lucalabs.fairylights.main.items.crafting.ingredient.BasicAuxiliaryIngredient;
import de.lucalabs.fairylights.main.items.crafting.ingredient.BasicRegularIngredient;
import de.lucalabs.fairylights.main.items.crafting.ingredient.InertBasicAuxiliaryIngredient;
import de.lucalabs.fairylights.main.items.crafting.ingredient.LazyTagIngredient;
import de.lucalabs.fairylights.main.string.StringType;
import de.lucalabs.fairylights.main.string.StringTypes;
import de.lucalabs.fairylights.main.util.Blender;
import de.lucalabs.fairylights.main.util.OreDictUtils;
import de.lucalabs.fairylights.main.util.Tags;
import de.lucalabs.fairylights.main.util.Utils;
import net.minecraft.core.Registry;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

import static de.lucalabs.fairylights.main.items.components.FairyLightItemComponents.*;

public final class FairyLightCraftingRecipes {

    public static final RecipeSerializer<GenericRecipe> HANGING_LIGHTS = register(
            "crafting_special_hanging_lights",
            id -> new SimpleCraftingRecipeSerializer<>(x -> createHangingLights(id, x)));

    public static final RecipeSerializer<GenericRecipe> HANGING_LIGHTS_AUGMENTATION = register(
            "crafting_special_hanging_lights_augmentation",
            id -> new SimpleCraftingRecipeSerializer<>(x -> createHangingLightsAugmentation(id, x)));

    public static final RecipeSerializer<GenericRecipe> PENNANT_BUNTING = register(
            "crafting_special_pennant_bunting",
            id -> new SimpleCraftingRecipeSerializer<>(x -> createPennantBunting(id, x)));

    public static final RecipeSerializer<GenericRecipe> PENNANT_BUNTING_AUGMENTATION = register(
            "crafting_special_pennant_bunting_augmentation",
            id -> new SimpleCraftingRecipeSerializer<>(x -> createPennantBuntingAugmentation(id, x)));

    public static final RecipeSerializer<GenericRecipe> TINSEL_GARLAND = register(
            "crafting_special_tinsel_garland",
            id -> new SimpleCraftingRecipeSerializer<>(x -> createTinselGarland(id, x)));

    public static final RecipeSerializer<GenericRecipe> TRIANGLE_PENNANT = register(
            "crafting_special_triangle_pennant",
            id -> new SimpleCraftingRecipeSerializer<>(x -> createTrianglePennant(id, x)));

    public static final RecipeSerializer<GenericRecipe> SQUARE_PENNANT = register(
            "crafting_special_square_pennant",
            id -> new SimpleCraftingRecipeSerializer<>(x -> createSquarePennant(id, x)));

    public static final RecipeSerializer<GenericRecipe> FAIRY_LIGHT = register(
            "crafting_special_fairy_light",
            id -> new SimpleCraftingRecipeSerializer<>(x -> createFairyLight(id, x)));

    public static final RecipeSerializer<GenericRecipe> PAPER_LANTERN = register(
            "crafting_special_paper_lantern",
            id -> new SimpleCraftingRecipeSerializer<>(x -> createPaperLantern(id, x)));

    public static final RecipeSerializer<GenericRecipe> ORB_LANTERN = register(
            "crafting_special_orb_lantern",
            id -> new SimpleCraftingRecipeSerializer<>(x -> createOrbLantern(id, x)));

    public static final RecipeSerializer<GenericRecipe> FLOWER_LIGHT = register(
            "crafting_special_flower_light",
            id -> new SimpleCraftingRecipeSerializer<>(x -> createFlowerLight(id, x)));

    public static final RecipeSerializer<GenericRecipe> CANDLE_LANTERN_LIGHT = register(
            "crafting_special_candle_lantern_light",
            id -> new SimpleCraftingRecipeSerializer<>(x -> createCandleLanternLight(id, x)));

    public static final RecipeSerializer<GenericRecipe> JACK_O_LANTERN = register(
            "crafting_special_jack_o_lantern",
            id -> new SimpleCraftingRecipeSerializer<>(x -> createJackOLantern(id, x)));

    public static final RecipeSerializer<GenericRecipe> SKULL_LIGHT = register(
            "crafting_special_skull_light",
            id -> new SimpleCraftingRecipeSerializer<>(x -> createSkullLight(id, x)));

    public static final RecipeSerializer<GenericRecipe> GHOST_LIGHT = register(
            "crafting_special_ghost_light",
            id -> new SimpleCraftingRecipeSerializer<>(x -> createGhostLight(id, x)));

    public static final RecipeSerializer<GenericRecipe> SPIDER_LIGHT = register(
            "crafting_special_spider_light",
            id -> new SimpleCraftingRecipeSerializer<>(x -> createSpiderLight(id, x)));

    public static final RecipeSerializer<GenericRecipe> WITCH_LIGHT = register(
            "crafting_special_witch_light",
            id -> new SimpleCraftingRecipeSerializer<>(x -> createWitchLight(id, x)));

    public static final RecipeSerializer<GenericRecipe> SNOWFLAKE_LIGHT = register(
            "crafting_special_snowflake_light",
            id -> new SimpleCraftingRecipeSerializer<>(x -> createSnowflakeLight(id, x)));

    public static final RecipeSerializer<GenericRecipe> HEART_LIGHT = register(
            "crafting_special_heart_light",
            id -> new SimpleCraftingRecipeSerializer<>(x -> createHeartLight(id, x)));

    public static final RecipeSerializer<GenericRecipe> MOON_LIGHT = register(
            "crafting_special_moon_light",
            id -> new SimpleCraftingRecipeSerializer<>(x -> createMoonLight(id, x)));

    public static final RecipeSerializer<GenericRecipe> STAR_LIGHT = register(
            "crafting_special_star_light",
            id -> new SimpleCraftingRecipeSerializer<>(x -> createStarLight(id, x)));

    public static final RecipeSerializer<GenericRecipe> ICICLE_LIGHTS = register(
            "crafting_special_icicle_lights",
            id -> new SimpleCraftingRecipeSerializer<>(x -> createIcicleLights(id, x)));

    public static final RecipeSerializer<GenericRecipe> METEOR_LIGHT = register(
            "crafting_special_meteor_light",
            id -> new SimpleCraftingRecipeSerializer<>(x -> createMeteorLight(id, x)));

    public static final RecipeSerializer<GenericRecipe> LIGHT_TWINKLE = register(
            "crafting_special_light_twinkle",
            id -> new SimpleCraftingRecipeSerializer<>(x -> createLightTwinkle(id, x)));

    public static final RecipeSerializer<GenericRecipe> COLOR_CHANGING_LIGHT = register(
            "crafting_special_color_changing_light",
            id -> new SimpleCraftingRecipeSerializer<>(x -> createColorChangingLight(id, x)));

    public static final RecipeSerializer<GenericRecipe> EDIT_COLOR = register(
            "crafting_special_edit_color",
            id -> new SimpleCraftingRecipeSerializer<>(x -> createDyeColor(id, x)));

    public static final RecipeSerializer<CustomRecipe> COPY_COLOR = register(
            "crafting_special_copy_color",
            id -> new SimpleCraftingRecipeSerializer<>(CopyColorRecipe::new));

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
        public void matched(final ItemStack ingredient, final PatchedDataComponentMap comps) {
            comps.set(COLOR, DyeableItem.getColor(OreDictUtils.getDyeColor(ingredient)));
        }
    };

    private FairyLightCraftingRecipes() {}


    private static GenericRecipe createDyeColor(final ResourceLocation name, CraftingBookCategory craftingBookCategory) {
        return new GenericRecipeBuilder(name, () -> EDIT_COLOR)
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
                    public boolean finish(final Blender data, final PatchedDataComponentMap comps) {
                        comps.set(COLOR, data.blend());
                        return false;
                    }
                })
                .build();
    }

    private static GenericRecipe createLightTwinkle(final ResourceLocation name, CraftingBookCategory craftingBookCategory) {
        return new GenericRecipeBuilder(name, () -> LIGHT_TWINKLE)
                .withShape("L")
                .withIngredient('L', Tags.TWINKLING_LIGHTS).withOutput('L')
                .withAuxiliaryIngredient(new InertBasicAuxiliaryIngredient(Ingredient.of(Items.GLOWSTONE_DUST), true, 1) {
                    @Override
                    public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
                        return Boolean.TRUE.equals(output.get(TWINKLE)) ? super.getInput(output) : ImmutableList.of();
                    }

                    @Override
                    public void present(final PatchedDataComponentMap comps) {
                        comps.set(TWINKLE, true);
                    }

                    @Override
                    public void absent(final PatchedDataComponentMap comps) {
                        comps.set(TWINKLE, false);
                    }

                    @Override
                    public List<FormattedText> getTooltip() {
                        var tooltips = super.getTooltip();
                        tooltips.add(Utils.formatRecipeTooltip("recipe.fairylights.twinkling_lights.glowstone"));
                        return tooltips;
                    }
                })
                .build();
    }

    private static GenericRecipe createColorChangingLight(final ResourceLocation name, CraftingBookCategory craftingBookCategory) {
        return new GenericRecipeBuilder(name, () -> COLOR_CHANGING_LIGHT)
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
                    public boolean finish(final List<Integer> data, final PatchedDataComponentMap comps) {
                        if (!data.isEmpty()) {
                            if (comps.has(COLOR)) {
                                data.addFirst(comps.get(COLOR));
                                comps.remove(COLOR);
                            }

                            comps.set(COLORS, Utils.deepCopyList(data, Function.identity()));
                        }
                        return false;
                    }
                })
                .build();
    }

    private static GenericRecipe createHangingLights(ResourceLocation name, CraftingBookCategory craftingBookCategory) {
        return new GenericRecipeBuilder(name, () -> HANGING_LIGHTS, FairyLightItems.HANGING_LIGHTS)
                .withShape("I-I")
                .withIngredient('I', Items.IRON_INGOT)
                .withIngredient('-', Items.STRING)
                .withAuxiliaryIngredient(new LightIngredient(true))
                .withAuxiliaryIngredient(new InertBasicAuxiliaryIngredient(LazyTagIngredient.of(Tags.DYES_WHITE), false, 1) {
                    @Override
                    public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
                        StringType string = output.get(STRING);
                        return string != null && string.equals(StringTypes.WHITE_STRING) ? super.getInput(output) : ImmutableList.of();
                    }

                    @Override
                    public void present(final PatchedDataComponentMap comps) {
                        comps.set(STRING, StringTypes.WHITE_STRING);
                    }

                    @Override
                    public void absent(final PatchedDataComponentMap comps) {
                        comps.set(STRING, StringTypes.BLACK_STRING);
                    }

                    @Override
                    public List<FormattedText> getTooltip() {
                        var tooltip = super.getTooltip();
                        tooltip.add(Utils.formatRecipeTooltip("recipe.fairylights.hangingLights.string"));
                        return tooltip;
                    }
                })
                .build();
    }

    /*
     *  The JEI shown recipe is adding glowstone, eventually I should allow a recipe to provide a number of
     *  different recipe layouts the the input ingredients can be generated for so I could show applying a
     *  new light pattern as well.
     */
    private static GenericRecipe createHangingLightsAugmentation(final ResourceLocation name, CraftingBookCategory craftingBookCategory) {
        return new GenericRecipeBuilder(name, () -> HANGING_LIGHTS_AUGMENTATION, FairyLightItems.HANGING_LIGHTS)
                .withShape("F")
                .withIngredient('F', new BasicRegularIngredient(Ingredient.of(FairyLightItems.HANGING_LIGHTS)) {
                    @Override
                    public ImmutableList<ItemStack> getInputs() {
                        return Arrays.stream(this.ingredient.getItems())
                                .map(ItemStack::copy)
                                .flatMap(stack -> makeHangingLightsExamples(stack).stream())
                                .collect(ImmutableList.toImmutableList());
                    }

                    @Override
                    public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
                        final ItemStack stack = output.copy();
                        if (stack.getComponents().isEmpty()) {
                            return ImmutableList.of();
                        }
                        stack.setCount(1);
                        return ImmutableList.of(ImmutableList.of(stack));
                    }

                    @Override
                    public void matched(final ItemStack ingredient, final PatchedDataComponentMap comps) {
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
    }

    public static ItemStack makeHangingLights(final ItemStack base, final DyeColor... colors) {
       final ItemStack stack = base.copy();

       final List<ItemStack> lights = new ArrayList<>();
       for (final DyeColor color : colors) {
           lights.add(DyeableItem.setColor(new ItemStack(FairyLightItems.FAIRY_LIGHT), color));
       }

       stack.set(PATTERN, lights);
       stack.set(STRING, StringTypes.BLACK_STRING);

       return stack;
    }

    private static GenericRecipe createPennantBunting(final ResourceLocation name, CraftingBookCategory craftingBookCategory) {
        return new GenericRecipeBuilder(name, () -> PENNANT_BUNTING, FairyLightItems.PENNANT_BUNTING)
                .withShape("I-I")
                .withIngredient('I', Items.IRON_INGOT)
                .withIngredient('-', Items.STRING)
                .withAuxiliaryIngredient(new PennantIngredient())
                .build();
    }

    private static GenericRecipe createTinselGarland(final ResourceLocation name, CraftingBookCategory craftingBookCategory) {
        return new GenericRecipeBuilder(name, () -> TINSEL_GARLAND, FairyLightItems.TINSEL)
                .withShape(" P ", "I-I", " D ")
                .withIngredient('P', Items.PAPER)
                .withIngredient('I', Items.IRON_INGOT)
                .withIngredient('-', Items.STRING)
                .withIngredient('D', DYE_SUBTYPE_INGREDIENT)
                .build();
    }

    private static GenericRecipe createPennantBuntingAugmentation(final ResourceLocation name, CraftingBookCategory craftingBookCategory) {
        return new GenericRecipeBuilder(name, () -> PENNANT_BUNTING_AUGMENTATION, FairyLightItems.PENNANT_BUNTING)
                .withShape("B")
                .withIngredient('B', new BasicRegularIngredient(Ingredient.of(FairyLightItems.PENNANT_BUNTING)) {
                    @Override
                    public ImmutableList<ItemStack> getInputs() {
                        return Arrays.stream(this.ingredient.getItems())
                                .map(ItemStack::copy)
                                .flatMap(stack -> makePennantExamples(stack).stream())
                                .collect(ImmutableList.toImmutableList());
                    }

                    @Override
                    public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
                        if (output.getComponents().isEmpty()) {
                            return ImmutableList.of();
                        }
                        return ImmutableList.of(makePennantExamples(output));
                    }

                    @Override
                    public void matched(final ItemStack ingredient, final PatchedDataComponentMap comps) {
                        comps.setAll(ingredient.getComponents());
                    }
                })
                .withAuxiliaryIngredient(new PennantIngredient())
                .build();
    }

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

        stack.set(PATTERN, pennants);

        return stack;
    }

    private static GenericRecipe createPennant(final ResourceLocation name, final Supplier<RecipeSerializer<GenericRecipe>> serializer, final Item item, final String pattern) {
        return new GenericRecipeBuilder(name, serializer, item)
                .withShape("- -", "PDP", pattern)
                .withIngredient('P', Items.PAPER)
                .withIngredient('-', Items.STRING)
                .withIngredient('D', DYE_SUBTYPE_INGREDIENT)
                .build();
    }

    private static GenericRecipe createTrianglePennant(final ResourceLocation name, CraftingBookCategory craftingBookCategory) {
        return createPennant(name, () -> TRIANGLE_PENNANT, FairyLightItems.TRIANGLE_PENNANT, " P ");
    }

    private static GenericRecipe createSquarePennant(final ResourceLocation name, CraftingBookCategory craftingBookCategory) {
        return createPennant(name, () -> SQUARE_PENNANT, FairyLightItems.SQUARE_PENNANT, "PPP");
    }

    private static GenericRecipe createFairyLight(final ResourceLocation name, CraftingBookCategory craftingBookCategory) {
        return createLight(name, () -> FAIRY_LIGHT, () -> FairyLightItems.FAIRY_LIGHT, b -> b
                .withShape(" I ", "IDI", " G ")
                .withIngredient('G', Items.GLASS_PANE)
        );
    }

    private static GenericRecipe createPaperLantern(final ResourceLocation name, CraftingBookCategory craftingBookCategory) {
        return createLight(name, () -> PAPER_LANTERN, () -> FairyLightItems.PAPER_LANTERN, b -> b
                .withShape(" I ", "PDP", "PPP")
                .withIngredient('P', Items.PAPER)
        );
    }

    private static GenericRecipe createOrbLantern(final ResourceLocation name, CraftingBookCategory craftingBookCategory) {
        return createLight(name, () -> ORB_LANTERN, () -> FairyLightItems.ORB_LANTERN, b -> b
                .withShape(" I ", "SDS", " W ")
                .withIngredient('S', Items.STRING)
                .withIngredient('W', Items.WHITE_WOOL)
        );
    }

    private static GenericRecipe createFlowerLight(final ResourceLocation name, CraftingBookCategory craftingBookCategory) {
        return createLight(name, () -> FLOWER_LIGHT, () -> FairyLightItems.FLOWER_LIGHT, b -> b
                .withShape(" I ", "RDB", " Y ")
                .withIngredient('R', Items.POPPY)
                .withIngredient('Y', Items.DANDELION)
                .withIngredient('B', Items.BLUE_ORCHID)
        );
    }

    private static GenericRecipe createCandleLanternLight(final ResourceLocation name, CraftingBookCategory craftingBookCategory) {
        return createLight(name, () -> CANDLE_LANTERN_LIGHT, () -> FairyLightItems.CANDLE_LANTERN_LIGHT, b -> b
                .withShape(" I ", "GDG", "IGI")
                .withIngredient('G', Items.GOLD_NUGGET)
        );
    }

    private static GenericRecipe createJackOLantern(final ResourceLocation name, CraftingBookCategory craftingBookCategory) {
        return createLight(name, () -> JACK_O_LANTERN, () -> FairyLightItems.JACK_O_LANTERN, b -> b
                .withShape(" I ", "SDS", "GPG")
                .withIngredient('S', ItemTags.WOODEN_SLABS)
                .withIngredient('G', Items.TORCH)
                .withIngredient('P', Items.JACK_O_LANTERN)
        );
    }

    private static GenericRecipe createSkullLight(final ResourceLocation name, CraftingBookCategory craftingBookCategory) {
        return createLight(name, () -> SKULL_LIGHT, () -> FairyLightItems.SKULL_LIGHT, b -> b
                .withShape(" I ", "IDI", " B ")
                .withIngredient('B', Items.BONE)
        );
    }

    private static GenericRecipe createGhostLight(final ResourceLocation name, CraftingBookCategory craftingBookCategory) {
        return createLight(name, () -> GHOST_LIGHT, () -> FairyLightItems.GHOST_LIGHT, b -> b
                .withShape(" I ", "PDP", "IGI")
                .withIngredient('P', Items.PAPER)
                .withIngredient('G', Items.WHITE_STAINED_GLASS_PANE)
        );
    }

    private static GenericRecipe createSpiderLight(final ResourceLocation name, CraftingBookCategory craftingBookCategory) {
        return createLight(name, () -> SPIDER_LIGHT, () -> FairyLightItems.SPIDER_LIGHT, b -> b
                .withShape(" I ", "WDW", "SES")
                .withIngredient('W', Items.COBWEB)
                .withIngredient('S', Items.STRING)
                .withIngredient('E', Items.SPIDER_EYE)
        );
    }

    private static GenericRecipe createWitchLight(final ResourceLocation name, CraftingBookCategory craftingBookCategory) {
        return createLight(name, () -> WITCH_LIGHT, () -> FairyLightItems.WITCH_LIGHT, b -> b
                .withShape(" I ", "BDW", " S ")
                .withIngredient('B', Items.GLASS_BOTTLE)
                .withIngredient('W', Items.WHEAT)
                .withIngredient('S', Items.STICK)
        );
    }

    private static GenericRecipe createSnowflakeLight(final ResourceLocation name, CraftingBookCategory craftingBookCategory) {
        return createLight(name, () -> SNOWFLAKE_LIGHT, () -> FairyLightItems.SNOWFLAKE_LIGHT, b -> b
                .withShape(" I ", "SDS", " G ")
                .withIngredient('S', Items.SNOWBALL)
                .withIngredient('G', Items.WHITE_STAINED_GLASS_PANE)
        );
    }

    private static GenericRecipe createHeartLight(final ResourceLocation name, CraftingBookCategory craftingBookCategory) {
        return createLight(name, () -> HEART_LIGHT, () -> FairyLightItems.HEART_LIGHT, b -> b
                .withShape(" I ", "IDI", " G ")
                .withIngredient('G', Items.RED_STAINED_GLASS_PANE)
        );
    }

    private static GenericRecipe createMoonLight(final ResourceLocation name, CraftingBookCategory craftingBookCategory) {
        return createLight(name, () -> MOON_LIGHT, () -> FairyLightItems.MOON_LIGHT, b -> b
                .withShape(" I ", "GDG", " C ")
                .withIngredient('G', Items.WHITE_STAINED_GLASS_PANE)
                .withIngredient('C', Items.CLOCK)
        );
    }


    private static GenericRecipe createStarLight(final ResourceLocation name, CraftingBookCategory craftingBookCategory) {
        return createLight(name, () -> STAR_LIGHT, () -> FairyLightItems.STAR_LIGHT, b -> b
                .withShape(" I ", "PDP", " G ")
                .withIngredient('P', Items.WHITE_STAINED_GLASS_PANE)
                .withIngredient('G', Items.GOLD_NUGGET)
        );
    }

    private static GenericRecipe createIcicleLights(final ResourceLocation name, CraftingBookCategory craftingBookCategory) {
        return createLight(name, () -> ICICLE_LIGHTS, () -> FairyLightItems.ICICLE_LIGHTS, b -> b
                .withShape(" I ", "GDG", " B ")
                .withIngredient('G', Items.GLASS_PANE)
                .withIngredient('B', Items.WATER_BUCKET)
        );
    }

    private static GenericRecipe createMeteorLight(final ResourceLocation name, CraftingBookCategory craftingBookCategory) {
        return createLight(name, () -> METEOR_LIGHT, () -> FairyLightItems.METEOR_LIGHT, b -> b
                .withShape(" I ", "GDG", "IPI")
                .withIngredient('G', Items.GLOWSTONE_DUST)
                .withIngredient('P', Items.PAPER)
        );
    }

    private static GenericRecipe createLight(final ResourceLocation name, final Supplier<? extends RecipeSerializer<GenericRecipe>> serializer, final Supplier<? extends Item> variant, final UnaryOperator<GenericRecipeBuilder> recipe) {
        return recipe.apply(new GenericRecipeBuilder(name, serializer))
                .withIngredient('I', Items.IRON_INGOT)
                .withIngredient('D', DYE_SUBTYPE_INGREDIENT)
                .withOutput(variant.get(), 4)
                .build();
    }

    private static class LightIngredient extends BasicAuxiliaryIngredient<List<ItemStack>> {
        private LightIngredient(final boolean isRequired) {
            super(LazyTagIngredient.of(Tags.LIGHTS), isRequired, 8);
        }

        @Override
        public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
            final List<ItemStack> pattern = output.get(PATTERN);
            if (pattern == null || pattern.isEmpty()) {
                return ImmutableList.of();
            }
            final ImmutableList.Builder<ImmutableList<ItemStack>> lights = ImmutableList.builder();
            for (ItemStack itemStack : pattern) {
                lights.add(ImmutableList.of(itemStack));
            }
            return lights.build();
        }

        @Override
        public boolean dictatesOutputType() {
            return true;
        }

        @Override
        public List<ItemStack> accumulator() {
            return new ArrayList<>();
        }

        @Override
        public void consume(final List<ItemStack> patternList, final ItemStack ingredient) {
            patternList.add(ingredient);
        }

        @Override
        public boolean finish(final List<ItemStack> pattern, final PatchedDataComponentMap comps) {
            if (!pattern.isEmpty()) {
                comps.set(PATTERN, Utils.deepCopyList(pattern, ItemStack::copy));
            }
            return false;
        }

        @Override
        public List<FormattedText> getTooltip() {
            return Collections.singletonList(Utils.formatRecipeTooltip("recipe.fairylights.hangingLights.light"));
        }
    }

    private static class PennantIngredient extends BasicAuxiliaryIngredient<List<ItemStack>> {
        private PennantIngredient() {
            super(LazyTagIngredient.of(Tags.PENNANTS), true, 8);
        }

        @Override
        public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
            final List<ItemStack> pattern = output.get(PATTERN);
            if (pattern == null || pattern.isEmpty()) {
                return ImmutableList.of();
            }
            final ImmutableList.Builder<ImmutableList<ItemStack>> pennants = ImmutableList.builder();
            for (ItemStack itemStack : pattern) {
                pennants.add(ImmutableList.of(itemStack));
            }
            return pennants.build();
        }

        @Override
        public boolean dictatesOutputType() {
            return true;
        }

        @Override
        public List<ItemStack> accumulator() {
            return new ArrayList<>();
        }

        @Override
        public void consume(final List<ItemStack> patternList, final ItemStack ingredient) {
            patternList.add(ingredient);
        }

        @Override
        public boolean finish(final List<ItemStack> pattern, final PatchedDataComponentMap comps) {
            if (!pattern.isEmpty()) {
                comps.set(PATTERN, Utils.deepCopyList(pattern, ItemStack::copy));
            }
            return false;
        }

        @Override
        public List<FormattedText> getTooltip() {
            return Collections.singletonList(Utils.formatRecipeTooltip("recipe.fairylights.pennantBunting.pennant"));
        }
    }

    private static <T extends RecipeSerializer<?>> T register(final String name, Function<ResourceLocation, T> supplier) {
        ResourceLocation identifier = Common.id(name);
        return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, identifier, supplier.apply(identifier));
    }

    public static void initialize() {
        Constants.LOG.info("Registering items");
    }
}
