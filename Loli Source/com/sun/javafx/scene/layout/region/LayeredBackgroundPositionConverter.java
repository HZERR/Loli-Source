/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.text.Font;

public final class LayeredBackgroundPositionConverter
extends StyleConverterImpl<ParsedValue<ParsedValue[], BackgroundPosition>[], BackgroundPosition[]> {
    private static final LayeredBackgroundPositionConverter LAYERED_BACKGROUND_POSITION_CONVERTER = new LayeredBackgroundPositionConverter();

    public static LayeredBackgroundPositionConverter getInstance() {
        return LAYERED_BACKGROUND_POSITION_CONVERTER;
    }

    private LayeredBackgroundPositionConverter() {
    }

    @Override
    public BackgroundPosition[] convert(ParsedValue<ParsedValue<ParsedValue[], BackgroundPosition>[], BackgroundPosition[]> parsedValue, Font font) {
        ParsedValue<ParsedValue[], BackgroundPosition>[] arrparsedValue = parsedValue.getValue();
        BackgroundPosition[] arrbackgroundPosition = new BackgroundPosition[arrparsedValue.length];
        for (int i2 = 0; i2 < arrparsedValue.length; ++i2) {
            arrbackgroundPosition[i2] = arrparsedValue[i2].convert(font);
        }
        return arrbackgroundPosition;
    }

    public String toString() {
        return "LayeredBackgroundPositionConverter";
    }
}

