/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.text.Font;

public final class LayeredBackgroundSizeConverter
extends StyleConverterImpl<ParsedValue<ParsedValue[], BackgroundSize>[], BackgroundSize[]> {
    private static final LayeredBackgroundSizeConverter LAYERED_BACKGROUND_SIZE_CONVERTER = new LayeredBackgroundSizeConverter();

    public static LayeredBackgroundSizeConverter getInstance() {
        return LAYERED_BACKGROUND_SIZE_CONVERTER;
    }

    private LayeredBackgroundSizeConverter() {
    }

    @Override
    public BackgroundSize[] convert(ParsedValue<ParsedValue<ParsedValue[], BackgroundSize>[], BackgroundSize[]> parsedValue, Font font) {
        ParsedValue<ParsedValue[], BackgroundSize>[] arrparsedValue = parsedValue.getValue();
        BackgroundSize[] arrbackgroundSize = new BackgroundSize[arrparsedValue.length];
        for (int i2 = 0; i2 < arrparsedValue.length; ++i2) {
            arrbackgroundSize[i2] = arrparsedValue[i2].convert(font);
        }
        return arrbackgroundSize;
    }
}

