/*
 * Decompiled with CFR 0.150.
 */
package javafx.css;

import javafx.css.StyleConverter;
import javafx.scene.text.Font;

public class ParsedValue<V, T> {
    protected final V value;
    protected final StyleConverter<V, T> converter;

    public final V getValue() {
        return this.value;
    }

    public final StyleConverter<V, T> getConverter() {
        return this.converter;
    }

    public T convert(Font font) {
        return (T)(this.converter != null ? this.converter.convert(this, font) : this.value);
    }

    protected ParsedValue(V v2, StyleConverter<V, T> styleConverter) {
        this.value = v2;
        this.converter = styleConverter;
    }
}

