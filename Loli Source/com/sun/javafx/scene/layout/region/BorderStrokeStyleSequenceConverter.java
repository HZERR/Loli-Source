/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.text.Font;

public final class BorderStrokeStyleSequenceConverter
extends StyleConverterImpl<ParsedValue<ParsedValue[], BorderStrokeStyle>[], BorderStrokeStyle[]> {
    private static final BorderStrokeStyleSequenceConverter BORDER_STYLE_SEQUENCE_CONVERTER = new BorderStrokeStyleSequenceConverter();

    public static BorderStrokeStyleSequenceConverter getInstance() {
        return BORDER_STYLE_SEQUENCE_CONVERTER;
    }

    private BorderStrokeStyleSequenceConverter() {
    }

    @Override
    public BorderStrokeStyle[] convert(ParsedValue<ParsedValue<ParsedValue[], BorderStrokeStyle>[], BorderStrokeStyle[]> parsedValue, Font font) {
        BorderStrokeStyle[] arrborderStrokeStyle;
        ParsedValue<ParsedValue[], BorderStrokeStyle>[] arrparsedValue = parsedValue.getValue();
        arrborderStrokeStyle = new BorderStrokeStyle[]{arrparsedValue.length > 0 ? arrparsedValue[0].convert(font) : BorderStrokeStyle.SOLID, arrparsedValue.length > 1 ? arrparsedValue[1].convert(font) : arrborderStrokeStyle[0], arrparsedValue.length > 2 ? arrparsedValue[2].convert(font) : arrborderStrokeStyle[0], arrparsedValue.length > 3 ? arrparsedValue[3].convert(font) : arrborderStrokeStyle[1]};
        return arrborderStrokeStyle;
    }

    public String toString() {
        return "BorderStrokeStyleSequenceConverter";
    }
}

