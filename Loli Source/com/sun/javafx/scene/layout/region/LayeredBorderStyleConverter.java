/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.text.Font;

public final class LayeredBorderStyleConverter
extends StyleConverterImpl<ParsedValue<ParsedValue<ParsedValue[], BorderStrokeStyle>[], BorderStrokeStyle[]>[], BorderStrokeStyle[][]> {
    private static final LayeredBorderStyleConverter LAYERED_BORDER_STYLE_CONVERTER = new LayeredBorderStyleConverter();

    public static LayeredBorderStyleConverter getInstance() {
        return LAYERED_BORDER_STYLE_CONVERTER;
    }

    private LayeredBorderStyleConverter() {
    }

    @Override
    public BorderStrokeStyle[][] convert(ParsedValue<ParsedValue<ParsedValue<ParsedValue[], BorderStrokeStyle>[], BorderStrokeStyle[]>[], BorderStrokeStyle[][]> parsedValue, Font font) {
        ParsedValue<ParsedValue<ParsedValue[], BorderStrokeStyle>[], BorderStrokeStyle[]>[] arrparsedValue = parsedValue.getValue();
        BorderStrokeStyle[][] arrborderStrokeStyle = new BorderStrokeStyle[arrparsedValue.length][0];
        for (int i2 = 0; i2 < arrparsedValue.length; ++i2) {
            arrborderStrokeStyle[i2] = arrparsedValue[i2].convert(font);
        }
        return arrborderStrokeStyle;
    }

    public String toString() {
        return "LayeredBorderStyleConverter";
    }
}

