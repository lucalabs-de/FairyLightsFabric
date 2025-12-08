package de.lucalabs.fairylights.util;

import net.minecraft.text.Text;
import net.minecraft.util.Language;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public final class Utils {
    private Utils() {
    }

    public static <E extends Enum<E>> E getEnumValue(final Class<E> clazz, final int ordinal) {
        final E[] values = Objects.requireNonNull(clazz, "clazz").getEnumConstants();
        return values[ordinal < 0 || ordinal >= values.length ? 0 : ordinal];
    }

    public static <T> List<T> deepCopyList(List<T> list, Function<T, T> copyFunc) {
        return list.stream().map(copyFunc).toList();
    }

    public static Text formatRecipeTooltip(final String key) {
        return formatRecipeTooltipValue(Language.getInstance().get(key));
    }

    private static Text formatRecipeTooltipValue(final String value) {
        return Text.translatable("recipe.ingredient.tooltip", value);
    }

}
