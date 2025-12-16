package de.lucalabs.fairylights.items.crafting;

import com.google.common.collect.ImmutableList;
import de.lucalabs.fairylights.FairyLights;
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
import de.lucalabs.fairylights.util.styled.StyledString;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class FairyLightCraftingRecipes {

    public static final RecipeSerializer<GenericRecipe> HANGING_LIGHTS = register(
            "crafting_special_hanging_lights",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createHangingLights));

    public static final RecipeSerializer<GenericRecipe> HANGING_LIGHTS_AUGMENTATION = register(
            "crafting_special_hanging_lights_augmentation",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createHangingLightsAugmentation));

    public static final RecipeSerializer<GenericRecipe> PENNANT_BUNTING = register(
            "crafting_special_pennant_bunting",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createPennantBunting));

    public static final RecipeSerializer<GenericRecipe> PENNANT_BUNTING_AUGMENTATION = register(
            "crafting_special_pennant_bunting_augmentation",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createPennantBuntingAugmentation));

    public static final RecipeSerializer<GenericRecipe> TINSEL_GARLAND = register(
            "crafting_special_tinsel_garland",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createTinselGarland));

    public static final RecipeSerializer<GenericRecipe> TRIANGLE_PENNANT = register(
            "crafting_special_triangle_pennant",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createTrianglePennant));

    public static final RecipeSerializer<GenericRecipe> SQUARE_PENNANT = register(
            "crafting_special_square_pennant",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createSquarePennant));

    public static final RecipeSerializer<GenericRecipe> FAIRY_LIGHT = register(
            "crafting_special_fairy_light",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createFairyLight));

    public static final RecipeSerializer<GenericRecipe> PAPER_LANTERN = register(
            "crafting_special_paper_lantern",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createPaperLantern));

    public static final RecipeSerializer<GenericRecipe> ORB_LANTERN = register(
            "crafting_special_orb_lantern",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createOrbLantern));

    public static final RecipeSerializer<GenericRecipe> FLOWER_LIGHT = register(
            "crafting_special_flower_light",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createFlowerLight));

    public static final RecipeSerializer<GenericRecipe> CANDLE_LANTERN_LIGHT = register(
            "crafting_special_candle_lantern_light",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createCandleLanternLight));

    public static final RecipeSerializer<GenericRecipe> JACK_O_LANTERN = register(
            "crafting_special_jack_o_lantern",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createJackOLantern));

    public static final RecipeSerializer<GenericRecipe> SKULL_LIGHT = register(
            "crafting_special_skull_light",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createSkullLight));

    public static final RecipeSerializer<GenericRecipe> GHOST_LIGHT = register(
            "crafting_special_ghost_light",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createGhostLight));

    public static final RecipeSerializer<GenericRecipe> SPIDER_LIGHT = register(
            "crafting_special_spider_light",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createSpiderLight));

    public static final RecipeSerializer<GenericRecipe> WITCH_LIGHT = register(
            "crafting_special_witch_light",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createWitchLight));

    public static final RecipeSerializer<GenericRecipe> SNOWFLAKE_LIGHT = register(
            "crafting_special_snowflake_light",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createSnowflakeLight));

    public static final RecipeSerializer<GenericRecipe> HEART_LIGHT = register(
            "crafting_special_heart_light",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createHeartLight));

    public static final RecipeSerializer<GenericRecipe> MOON_LIGHT = register(
            "crafting_special_moon_light",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createMoonLight));

    public static final RecipeSerializer<GenericRecipe> STAR_LIGHT = register(
            "crafting_special_star_light",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createStarLight));

    public static final RecipeSerializer<GenericRecipe> ICICLE_LIGHTS = register(
            "crafting_special_icicle_lights",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createIcicleLights));

    public static final RecipeSerializer<GenericRecipe> METEOR_LIGHT = register(
            "crafting_special_meteor_light",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createMeteorLight));

    public static final RecipeSerializer<GenericRecipe> LIGHT_TWINKLE = register(
            "crafting_special_light_twinkle",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createLightTwinkle));

    public static final RecipeSerializer<GenericRecipe> COLOR_CHANGING_LIGHT = register(
            "crafting_special_color_changing_light",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createColorChangingLight));

    public static final RecipeSerializer<GenericRecipe> EDIT_COLOR = register(
            "crafting_special_edit_color",
            () -> new SpecialRecipeSerializer<>(FairyLightCraftingRecipes::createDyeColor));

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
        public void matched(final ItemStack ingredient, final NbtCompound nbt) {
            DyeableItem.setColor(nbt, OreDictUtils.getDyeColor(ingredient));
        }
    };

    private FairyLightCraftingRecipes() {}


    private static GenericRecipe createDyeColor(final Identifier name, CraftingRecipeCategory craftingBookCategory) {
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
                    public boolean finish(final Blender data, final NbtCompound nbt) {
                        DyeableItem.setColor(nbt, data.blend());
                        return false;
                    }
                })
                .build();
    }

    private static GenericRecipe createLightTwinkle(final Identifier name, CraftingRecipeCategory craftingBookCategory) {
        return new GenericRecipeBuilder(name, () -> LIGHT_TWINKLE)
                .withShape("L")
                .withIngredient('L', Tags.TWINKLING_LIGHTS).withOutput('L')
                .withAuxiliaryIngredient(new InertBasicAuxiliaryIngredient(Ingredient.ofItems(Items.GLOWSTONE_DUST), true, 1) {
                    @Override
                    public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
                        return useInputsForTagBool(output, "twinkle", true) ? super.getInput(output) : ImmutableList.of();
                    }

                    @Override
                    public void present(final NbtCompound nbt) {
                        nbt.putBoolean("twinkle", true);
                    }

                    @Override
                    public void absent(final NbtCompound nbt) {
                        nbt.putBoolean("twinkle", false);
                    }

                    @Override
                    public void addTooltip(final List<Text> tooltip) {
                        super.addTooltip(tooltip);
                        tooltip.add(Utils.formatRecipeTooltip("recipe.fairylights.twinkling_lights.glowstone"));
                    }
                })
                .build();
    }

    private static GenericRecipe createColorChangingLight(final Identifier name, CraftingRecipeCategory craftingBookCategory) {
        return new GenericRecipeBuilder(name, () -> COLOR_CHANGING_LIGHT)
                .withShape("IG")
                .withIngredient('I', Tags.DYEABLE_LIGHTS).withOutput('I')
                .withIngredient('G', Items.GOLD_NUGGET)
                .withAuxiliaryIngredient(new BasicAuxiliaryIngredient<NbtList>(LazyTagIngredient.of(Tags.DYES), true, 8) {
                    @Override
                    public NbtList accumulator() {
                        return new NbtList();
                    }

                    @Override
                    public void consume(final NbtList data, final ItemStack ingredient) {
                        data.add(NbtInt.of(DyeableItem.getColor(OreDictUtils.getDyeColor(ingredient))));
                    }

                    @Override
                    public boolean finish(final NbtList data, final NbtCompound nbt) {
                        if (!data.isEmpty()) {
                            if (nbt.contains("color", NbtElement.INT_TYPE)) {
                                data.add(0, NbtInt.of(nbt.getInt("color")));
                                nbt.remove("color");
                            }
                            nbt.put("colors", data);
                        }
                        return false;
                    }
                })
                .build();
    }

    private static GenericRecipe createHangingLights(Identifier name, CraftingRecipeCategory craftingBookCategory) {
        return new GenericRecipeBuilder(name, () -> HANGING_LIGHTS, FairyLightItems.HANGING_LIGHTS)
                .withShape("I-I")
                .withIngredient('I', Items.IRON_INGOT)
                .withIngredient('-', Items.STRING)
                .withAuxiliaryIngredient(new LightIngredient(true))
                .withAuxiliaryIngredient(new InertBasicAuxiliaryIngredient(LazyTagIngredient.of(Tags.DYES_WHITE), false, 1) {
                    @Override
                    public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
                        final NbtCompound tag = output.getNbt();
                        return tag != null && HangingLightsConnectionItem.getString(tag) == StringTypes.WHITE_STRING ? super.getInput(output) : ImmutableList.of();
                    }

                    @Override
                    public void present(final NbtCompound nbt) {
                        HangingLightsConnectionItem.setString(nbt, StringTypes.WHITE_STRING);
                    }

                    @Override
                    public void absent(final NbtCompound nbt) {
                        HangingLightsConnectionItem.setString(nbt, StringTypes.BLACK_STRING);
                    }

                    @Override
                    public void addTooltip(final List<Text> tooltip) {
                        super.addTooltip(tooltip);
                        tooltip.add(Utils.formatRecipeTooltip("recipe.fairylights.hangingLights.string"));
                    }
                })
                .build();
    }

    private static boolean useInputsForTagBool(final ItemStack output, final String key, final boolean value) {
        final NbtCompound compound = output.getNbt();
        return compound != null && compound.getBoolean(key) == value;
    }

    /*
     *  The JEI shown recipe is adding glowstone, eventually I should allow a recipe to provide a number of
     *  different recipe layouts the the input ingredients can be generated for so I could show applying a
     *  new light pattern as well.
     */
    private static GenericRecipe createHangingLightsAugmentation(final Identifier name, CraftingRecipeCategory craftingBookCategory) {
        return new GenericRecipeBuilder(name, () -> HANGING_LIGHTS_AUGMENTATION, FairyLightItems.HANGING_LIGHTS)
                .withShape("F")
                .withIngredient('F', new BasicRegularIngredient(Ingredient.ofItems(FairyLightItems.HANGING_LIGHTS)) {
                    @Override
                    public ImmutableList<ItemStack> getInputs() {
                        return Arrays.stream(this.ingredient.getMatchingStacks())
                                .map(ItemStack::copy)
                                .flatMap(stack -> {
                                    stack.setNbt(new NbtCompound());
                                    return makeHangingLightsExamples(stack).stream();
                                }).collect(ImmutableList.toImmutableList());
                    }

                    @Override
                    public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
                        final ItemStack stack = output.copy();
                        final NbtCompound compound = stack.getNbt();
                        if (compound == null) {
                            return ImmutableList.of();
                        }
                        stack.setCount(1);
                        return ImmutableList.of(ImmutableList.of(stack));
                    }

                    @Override
                    public void matched(final ItemStack ingredient, final NbtCompound nbt) {
                        final NbtCompound compound = ingredient.getNbt();
                        if (compound != null) {
                            nbt.copyFrom(compound);
                        }
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
        NbtCompound compound = stack.getNbt();
        final NbtList lights = new NbtList();
        for (final DyeColor color : colors) {
            lights.add(DyeableItem.setColor(new ItemStack(FairyLightItems.FAIRY_LIGHT), color).writeNbt(new NbtCompound()));
        }
        if (compound == null) {
            compound = new NbtCompound();
            stack.setNbt(compound);
        }
        compound.put("pattern", lights);
        HangingLightsConnectionItem.setString(compound, StringTypes.BLACK_STRING);
        return stack;
    }


    private static GenericRecipe createPennantBunting(final Identifier name, CraftingRecipeCategory craftingBookCategory) {
        return new GenericRecipeBuilder(name, () -> PENNANT_BUNTING, FairyLightItems.PENNANT_BUNTING)
                .withShape("I-I")
                .withIngredient('I', Items.IRON_INGOT)
                .withIngredient('-', Items.STRING)
                .withAuxiliaryIngredient(new PennantIngredient())
                .build();
    }

    private static GenericRecipe createTinselGarland(final Identifier name, CraftingRecipeCategory craftingBookCategory) {
        return new GenericRecipeBuilder(name, () -> TINSEL_GARLAND, FairyLightItems.TINSEL)
                .withShape(" P ", "I-I", " D ")
                .withIngredient('P', Items.PAPER)
                .withIngredient('I', Items.IRON_INGOT)
                .withIngredient('-', Items.STRING)
                .withIngredient('D', DYE_SUBTYPE_INGREDIENT)
                .build();
    }

    private static GenericRecipe createPennantBuntingAugmentation(final Identifier name, CraftingRecipeCategory craftingBookCategory) {
        return new GenericRecipeBuilder(name, () -> PENNANT_BUNTING_AUGMENTATION, FairyLightItems.PENNANT_BUNTING)
                .withShape("B")
                .withIngredient('B', new BasicRegularIngredient(Ingredient.ofItems(FairyLightItems.PENNANT_BUNTING)) {
                    @Override
                    public ImmutableList<ItemStack> getInputs() {
                        return Arrays.stream(this.ingredient.getMatchingStacks())
                                .map(ItemStack::copy)
                                .flatMap(stack -> {
                                    stack.setNbt(new NbtCompound());
                                    return makePennantExamples(stack).stream();
                                }).collect(ImmutableList.toImmutableList());
                    }

                    @Override
                    public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
                        final NbtCompound compound = output.getNbt();
                        if (compound == null) {
                            return ImmutableList.of();
                        }
                        return ImmutableList.of(makePennantExamples(output));
                    }

                    @Override
                    public void matched(final ItemStack ingredient, final NbtCompound nbt) {
                        final NbtCompound compound = ingredient.getNbt();
                        if (compound != null) {
                            nbt.copyFrom(compound);
                        }
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
        NbtCompound compound = stack.getNbt();
        final NbtList pennants = new NbtList();
        for (final DyeColor color : colors) {
            final ItemStack pennant = new ItemStack(FairyLightItems.TRIANGLE_PENNANT);
            DyeableItem.setColor(pennant, color);
            pennants.add(pennant.writeNbt(new NbtCompound()));
        }
        if (compound == null) {
            compound = new NbtCompound();
            stack.setNbt(compound);
        }
        compound.put("pattern", pennants);
        compound.put("text", StyledString.serialize(new StyledString()));
        return stack;
    }

    private static GenericRecipe createPennant(final Identifier name, final Supplier<RecipeSerializer<GenericRecipe>> serializer, final Item item, final String pattern) {
        return new GenericRecipeBuilder(name, serializer, item)
                .withShape("- -", "PDP", pattern)
                .withIngredient('P', Items.PAPER)
                .withIngredient('-', Items.STRING)
                .withIngredient('D', DYE_SUBTYPE_INGREDIENT)
                .build();
    }

    private static GenericRecipe createTrianglePennant(final Identifier name, CraftingRecipeCategory craftingBookCategory) {
        return createPennant(name, () -> TRIANGLE_PENNANT, FairyLightItems.TRIANGLE_PENNANT, " P ");
    }

    private static GenericRecipe createSquarePennant(final Identifier name, CraftingRecipeCategory craftingBookCategory) {
        return createPennant(name, () -> SQUARE_PENNANT, FairyLightItems.SQUARE_PENNANT, "PPP");
    }

    private static GenericRecipe createFairyLight(final Identifier name, CraftingRecipeCategory craftingBookCategory) {
        return createLight(name, () -> FAIRY_LIGHT, () -> FairyLightItems.FAIRY_LIGHT, b -> b
                .withShape(" I ", "IDI", " G ")
                .withIngredient('G', Items.GLASS_PANE)
        );
    }

    private static GenericRecipe createPaperLantern(final Identifier name, CraftingRecipeCategory craftingBookCategory) {
        return createLight(name, () -> PAPER_LANTERN, () -> FairyLightItems.PAPER_LANTERN, b -> b
                .withShape(" I ", "PDP", "PPP")
                .withIngredient('P', Items.PAPER)
        );
    }

    private static GenericRecipe createOrbLantern(final Identifier name, CraftingRecipeCategory craftingBookCategory) {
        return createLight(name, () -> ORB_LANTERN, () -> FairyLightItems.ORB_LANTERN, b -> b
                .withShape(" I ", "SDS", " W ")
                .withIngredient('S', Items.STRING)
                .withIngredient('W', Items.WHITE_WOOL)
        );
    }

    private static GenericRecipe createFlowerLight(final Identifier name, CraftingRecipeCategory craftingBookCategory) {
        return createLight(name, () -> FLOWER_LIGHT, () -> FairyLightItems.FLOWER_LIGHT, b -> b
                .withShape(" I ", "RDB", " Y ")
                .withIngredient('R', Items.POPPY)
                .withIngredient('Y', Items.DANDELION)
                .withIngredient('B', Items.BLUE_ORCHID)
        );
    }

    private static GenericRecipe createCandleLanternLight(final Identifier name, CraftingRecipeCategory craftingBookCategory) {
        return createLight(name, () -> CANDLE_LANTERN_LIGHT, () -> FairyLightItems.CANDLE_LANTERN_LIGHT, b -> b
                .withShape(" I ", "GDG", "IGI")
                .withIngredient('G', Items.GOLD_NUGGET)
        );
    }

    private static GenericRecipe createJackOLantern(final Identifier name, CraftingRecipeCategory craftingBookCategory) {
        return createLight(name, () -> JACK_O_LANTERN, () -> FairyLightItems.JACK_O_LANTERN, b -> b
                .withShape(" I ", "SDS", "GPG")
                .withIngredient('S', ItemTags.WOODEN_SLABS)
                .withIngredient('G', Items.TORCH)
                .withIngredient('P', Items.JACK_O_LANTERN)
        );
    }

    private static GenericRecipe createSkullLight(final Identifier name, CraftingRecipeCategory craftingBookCategory) {
        return createLight(name, () -> SKULL_LIGHT, () -> FairyLightItems.SKULL_LIGHT, b -> b
                .withShape(" I ", "IDI", " B ")
                .withIngredient('B', Items.BONE)
        );
    }

    private static GenericRecipe createGhostLight(final Identifier name, CraftingRecipeCategory craftingBookCategory) {
        return createLight(name, () -> GHOST_LIGHT, () -> FairyLightItems.GHOST_LIGHT, b -> b
                .withShape(" I ", "PDP", "IGI")
                .withIngredient('P', Items.PAPER)
                .withIngredient('G', Items.WHITE_STAINED_GLASS_PANE)
        );
    }

    private static GenericRecipe createSpiderLight(final Identifier name, CraftingRecipeCategory craftingBookCategory) {
        return createLight(name, () -> SPIDER_LIGHT, () -> FairyLightItems.SPIDER_LIGHT, b -> b
                .withShape(" I ", "WDW", "SES")
                .withIngredient('W', Items.COBWEB)
                .withIngredient('S', Items.STRING)
                .withIngredient('E', Items.SPIDER_EYE)
        );
    }

    private static GenericRecipe createWitchLight(final Identifier name, CraftingRecipeCategory craftingBookCategory) {
        return createLight(name, () -> WITCH_LIGHT, () -> FairyLightItems.WITCH_LIGHT, b -> b
                .withShape(" I ", "BDW", " S ")
                .withIngredient('B', Items.GLASS_BOTTLE)
                .withIngredient('W', Items.WHEAT)
                .withIngredient('S', Items.STICK)
        );
    }

    private static GenericRecipe createSnowflakeLight(final Identifier name, CraftingRecipeCategory craftingBookCategory) {
        return createLight(name, () -> SNOWFLAKE_LIGHT, () -> FairyLightItems.SNOWFLAKE_LIGHT, b -> b
                .withShape(" I ", "SDS", " G ")
                .withIngredient('S', Items.SNOWBALL)
                .withIngredient('G', Items.WHITE_STAINED_GLASS_PANE)
        );
    }

    private static GenericRecipe createHeartLight(final Identifier name, CraftingRecipeCategory craftingBookCategory) {
        return createLight(name, () -> HEART_LIGHT, () -> FairyLightItems.HEART_LIGHT, b -> b
                .withShape(" I ", "IDI", " G ")
                .withIngredient('G', Items.RED_STAINED_GLASS_PANE)
        );
    }

    private static GenericRecipe createMoonLight(final Identifier name, CraftingRecipeCategory craftingBookCategory) {
        return createLight(name, () -> MOON_LIGHT, () -> FairyLightItems.MOON_LIGHT, b -> b
                .withShape(" I ", "GDG", " C ")
                .withIngredient('G', Items.WHITE_STAINED_GLASS_PANE)
                .withIngredient('C', Items.CLOCK)
        );
    }


    private static GenericRecipe createStarLight(final Identifier name, CraftingRecipeCategory craftingBookCategory) {
        return createLight(name, () -> STAR_LIGHT, () -> FairyLightItems.STAR_LIGHT, b -> b
                .withShape(" I ", "PDP", " G ")
                .withIngredient('P', Items.WHITE_STAINED_GLASS_PANE)
                .withIngredient('G', Items.GOLD_NUGGET)
        );
    }

    private static GenericRecipe createIcicleLights(final Identifier name, CraftingRecipeCategory craftingBookCategory) {
        return createLight(name, () -> ICICLE_LIGHTS, () -> FairyLightItems.ICICLE_LIGHTS, b -> b
                .withShape(" I ", "GDG", " B ")
                .withIngredient('G', Items.GLASS_PANE)
                .withIngredient('B', Items.WATER_BUCKET)
        );
    }

    private static GenericRecipe createMeteorLight(final Identifier name, CraftingRecipeCategory craftingBookCategory) {
        return createLight(name, () -> METEOR_LIGHT, () -> FairyLightItems.METEOR_LIGHT, b -> b
                .withShape(" I ", "GDG", "IPI")
                .withIngredient('G', Items.GLOWSTONE_DUST)
                .withIngredient('P', Items.PAPER)
        );
    }

    private static GenericRecipe createLight(final Identifier name, final Supplier<? extends RecipeSerializer<GenericRecipe>> serializer, final Supplier<? extends Item> variant, final UnaryOperator<GenericRecipeBuilder> recipe) {
        return recipe.apply(new GenericRecipeBuilder(name, serializer))
                .withIngredient('I', Items.IRON_INGOT)
                .withIngredient('D', DYE_SUBTYPE_INGREDIENT)
                .withOutput(variant.get(), 4)
                .build();
    }

    private static class LightIngredient extends BasicAuxiliaryIngredient<NbtList> {
        private LightIngredient(final boolean isRequired) {
            super(LazyTagIngredient.of(Tags.LIGHTS), isRequired, 8);
        }

        @Override
        public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
            final NbtCompound compound = output.getNbt();
            if (compound == null) {
                return ImmutableList.of();
            }
            final NbtList pattern = compound.getList("pattern", NbtElement.COMPOUND_TYPE);
            if (pattern.isEmpty()) {
                return ImmutableList.of();
            }
            final ImmutableList.Builder<ImmutableList<ItemStack>> lights = ImmutableList.builder();
            for (int i = 0; i < pattern.size(); i++) {
                lights.add(ImmutableList.of(ItemStack.fromNbt(pattern.getCompound(i))));
            }
            return lights.build();
        }

        @Override
        public boolean dictatesOutputType() {
            return true;
        }

        @Override
        public NbtList accumulator() {
            return new NbtList();
        }

        @Override
        public void consume(final NbtList patternList, final ItemStack ingredient) {
            patternList.add(ingredient.writeNbt(new NbtCompound()));
        }

        @Override
        public boolean finish(final NbtList pattern, final NbtCompound nbt) {
            if (!pattern.isEmpty()) {
                nbt.put("pattern", pattern);
            }
            return false;
        }

        @Override
        public void addTooltip(final List<Text> tooltip) {
            tooltip.add(Utils.formatRecipeTooltip("recipe.fairylights.hangingLights.light"));
        }
    }

    private static class PennantIngredient extends BasicAuxiliaryIngredient<NbtList> {
        private PennantIngredient() {
            super(LazyTagIngredient.of(Tags.PENNANTS), true, 8);
        }

        @Override
        public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
            final NbtCompound compound = output.getNbt();
            if (compound == null) {
                return ImmutableList.of();
            }
            final NbtList pattern = compound.getList("pattern", NbtElement.COMPOUND_TYPE);
            if (pattern.isEmpty()) {
                return ImmutableList.of();
            }
            final ImmutableList.Builder<ImmutableList<ItemStack>> pennants = ImmutableList.builder();
            for (int i = 0; i < pattern.size(); i++) {
                pennants.add(ImmutableList.of(ItemStack.fromNbt(pattern.getCompound(i))));
            }
            return pennants.build();
        }

        @Override
        public boolean dictatesOutputType() {
            return true;
        }

        @Override
        public NbtList accumulator() {
            return new NbtList();
        }

        @Override
        public void consume(final NbtList patternList, final ItemStack ingredient) {
            patternList.add(ingredient.writeNbt(new NbtCompound()));
        }

        @Override
        public boolean finish(final NbtList pattern, final NbtCompound nbt) {
            if (!pattern.isEmpty()) {
                nbt.put("pattern", pattern);
                nbt.put("text", StyledString.serialize(new StyledString()));
            }
            return false;
        }

        @Override
        public void addTooltip(final List<Text> tooltip) {
            tooltip.add(Utils.formatRecipeTooltip("recipe.fairylights.pennantBunting.pennant"));
        }
    }

    private static <T extends RecipeSerializer<?>> T register(final String name, Supplier<T> supplier) {
        Identifier identifier = Identifier.of(FairyLights.ID, name);
        return Registry.register(Registries.RECIPE_SERIALIZER, identifier, supplier.get());
    }

    public static void initialize() {
        FairyLights.LOGGER.info("Registering items");
    }
}
