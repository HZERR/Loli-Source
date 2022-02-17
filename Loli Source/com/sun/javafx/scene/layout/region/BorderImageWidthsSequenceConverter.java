/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.scene.layout.region.BorderImageWidthConverter;
import javafx.css.ParsedValue;
import javafx.scene.layout.BorderWidths;
import javafx.scene.text.Font;

public class BorderImageWidthsSequenceConverter
extends StyleConverterImpl<ParsedValue<ParsedValue[], BorderWidths>[], BorderWidths[]> {
    private static final BorderImageWidthsSequenceConverter CONVERTER = new BorderImageWidthsSequenceConverter();

    public static BorderImageWidthsSequenceConverter getInstance() {
        return CONVERTER;
    }

    @Override
    public BorderWidths[] convert(ParsedValue<ParsedValue<ParsedValue[], BorderWidths>[], BorderWidths[]> parsedValue, Font font) {
        ParsedValue<ParsedValue[], BorderWidths>[] arrparsedValue = parsedValue.getValue();
        BorderWidths[] arrborderWidths = new BorderWidths[arrparsedValue.length];
        for (int i2 = 0; i2 < arrparsedValue.length; ++i2) {
            arrborderWidths[i2] = BorderImageWidthConverter.getInstance().convert((ParsedValue)arrparsedValue[i2], font);
        }
        return arrborderWidths;
    }

    public String toString() {
        return "BorderImageWidthsSequenceConverter";
    }
}

