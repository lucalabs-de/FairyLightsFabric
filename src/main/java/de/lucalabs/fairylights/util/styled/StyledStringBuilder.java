package de.lucalabs.fairylights.util.styled;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class StyledStringBuilder implements Appendable, CharSequence {

    private final StringBuilder stringBuilder;
    private final List<Style> styling;

    private Style currentStyle;

    public StyledStringBuilder(final String str) {
        this();
        this.append(str);
    }

    public StyledStringBuilder() {
        this(16);
    }

    public StyledStringBuilder(final int capacity) {
        this(new StringBuilder(capacity), new ArrayList<>(capacity), new Style());
    }

    private StyledStringBuilder(final StringBuilder stringBuilder, final List<Style> styling, final Style currentStyle) {
        this.stringBuilder = stringBuilder;
        this.styling = styling;
        this.currentStyle = currentStyle;
    }

    public StyledStringBuilder setStyle(final Style style) {
        this.currentStyle = Objects.requireNonNull(style);
        return this;
    }

    @Override
    public int length() {
        return this.stringBuilder.length();
    }

    @Override
    public char charAt(final int index) {
        return this.stringBuilder.charAt(index);
    }

    @Override
    public @NotNull StyledStringBuilder subSequence(final int start, final int end) {
        return new StyledStringBuilder(new StringBuilder(this.stringBuilder.substring(start, end)), new ArrayList<>(this.styling).subList(start, end), this.styling.get(start));
    }

    public StyledStringBuilder insert(final int index, final String str) {
        this.stringBuilder.insert(0, str);
        for (int i = str.length(); i > 0; i--) {
            this.styling.add(index, this.currentStyle);
        }
        return this;
    }

    public StyledStringBuilder insert(final int index, final StyledString str) {
        this.stringBuilder.insert(0, str.toUnstyledString());
        final Style[] styling = str.getStyling();
        for (int i = styling.length - 1; i >= 0; i--) {
            this.styling.add(index, styling[i]);
        }
        return this;
    }

    public StyledStringBuilder append(final StyledString str) {
        this.stringBuilder.append(str.toUnstyledString());
        this.styling.addAll(Arrays.asList(str.getStyling()));
        return this;
    }

    public StyledStringBuilder append(final CharSequence csq, Style style) {
        this.stringBuilder.append(csq);
        for (int i = 0; i < csq.length(); i++) {
            this.styling.add(style);
        }
        return this;
    }

    @Override
    public StyledStringBuilder append(final CharSequence csq) {
        this.stringBuilder.append(csq);
        for (int i = 0; i < csq.length(); i++) {
            this.styling.add(this.currentStyle);
        }
        return this;
    }

    @Override
    public StyledStringBuilder append(final CharSequence csq, final int start, final int end) {
        return this.append(csq.subSequence(start, end));
    }

    @Override
    public StyledStringBuilder append(final char c) {
        return this.append(c, this.currentStyle);
    }

    public StyledStringBuilder append(final char c, final Style s) {
        this.stringBuilder.append(c);
        this.styling.add(s);
        return this;
    }

    @Override
    public @NotNull String toString() {
        return this.stringBuilder.toString();
    }

    public StyledString toStyledString() {
        return new StyledString(this.stringBuilder.toString(), this.styling.toArray(new Style[0]));
    }
}
