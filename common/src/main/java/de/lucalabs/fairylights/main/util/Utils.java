package de.lucalabs.fairylights.main.util;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;

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

    public static Component formatRecipeTooltip(final String key) {
        return formatRecipeTooltipValue(Language.getInstance().getOrDefault(key));
    }

    private static Component formatRecipeTooltipValue(final String value) {
        return Component.translatable("recipe.ingredient.tooltip", value);
    }

}
