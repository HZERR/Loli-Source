/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.ParsedValueImpl;
import com.sun.javafx.css.Size;
import com.sun.javafx.css.StyleConverterImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.css.ParsedValue;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;

public class BorderStyleConverter
extends StyleConverterImpl<ParsedValue[], BorderStrokeStyle> {
    public static final ParsedValueImpl<ParsedValue[], Number[]> NONE = new ParsedValueImpl(null, null);
    public static final ParsedValueImpl<ParsedValue[], Number[]> HIDDEN = new ParsedValueImpl(null, null);
    public static final ParsedValueImpl<ParsedValue[], Number[]> DOTTED = new ParsedValueImpl(null, null);
    public static final ParsedValueImpl<ParsedValue[], Number[]> DASHED = new ParsedValueImpl(null, null);
    public static final ParsedValueImpl<ParsedValue[], Number[]> SOLID = new ParsedValueImpl(null, null);
    private static final BorderStyleConverter BORDER_STYLE_CONVERTER = new BorderStyleConverter();

    public static BorderStyleConverter getInstance() {
        return BORDER_STYLE_CONVERTER;
    }

    private BorderStyleConverter() {
    }

    @Override
    public BorderStrokeStyle convert(ParsedValue<ParsedValue[], BorderStrokeStyle> parsedValue, Font font) {
        double d2;
        List<Double> list;
        boolean bl;
        ParsedValue[] arrparsedValue = parsedValue.getValue();
        ParsedValue parsedValue2 = arrparsedValue[0];
        boolean bl2 = bl = arrparsedValue[1] == null && arrparsedValue[2] == null && arrparsedValue[3] == null && arrparsedValue[4] == null && arrparsedValue[5] == null;
        if (NONE == parsedValue2) {
            return BorderStrokeStyle.NONE;
        }
        if (DOTTED == parsedValue2 && bl) {
            return BorderStrokeStyle.DOTTED;
        }
        if (DASHED == parsedValue2 && bl) {
            return BorderStrokeStyle.DASHED;
        }
        if (SOLID == parsedValue2 && bl) {
            return BorderStrokeStyle.SOLID;
        }
        ParsedValue[] arrparsedValue2 = (ParsedValue[])arrparsedValue[0].getValue();
        if (arrparsedValue2 == null) {
            list = DOTTED == parsedValue2 ? BorderStrokeStyle.DOTTED.getDashArray() : (DASHED == parsedValue2 ? BorderStrokeStyle.DASHED.getDashArray() : (SOLID == parsedValue2 ? BorderStrokeStyle.SOLID.getDashArray() : Collections.emptyList()));
        } else {
            list = new ArrayList<Double>(arrparsedValue2.length);
            for (int i2 = 0; i2 < arrparsedValue2.length; ++i2) {
                Size size = (Size)arrparsedValue2[i2].convert(font);
                list.add(size.pixels(font));
            }
        }
        double d3 = arrparsedValue[1] != null ? (Double)arrparsedValue[1].convert(font) : 0.0;
        StrokeType strokeType = arrparsedValue[2] != null ? (StrokeType)((Object)arrparsedValue[2].convert(font)) : StrokeType.INSIDE;
        StrokeLineJoin strokeLineJoin = arrparsedValue[3] != null ? (StrokeLineJoin)((Object)arrparsedValue[3].convert(font)) : StrokeLineJoin.MITER;
        double d4 = d2 = arrparsedValue[4] != null ? (Double)arrparsedValue[4].convert(font) : 10.0;
        StrokeLineCap strokeLineCap = arrparsedValue[5] != null ? (StrokeLineCap)((Object)arrparsedValue[5].convert(font)) : (DOTTED == parsedValue2 ? StrokeLineCap.ROUND : StrokeLineCap.BUTT);
        BorderStrokeStyle borderStrokeStyle = new BorderStrokeStyle(strokeType, strokeLineJoin, strokeLineCap, d2, d3, list);
        if (BorderStrokeStyle.SOLID.equals(borderStrokeStyle)) {
            return BorderStrokeStyle.SOLID;
        }
        return borderStrokeStyle;
    }

    public String toString() {
        return "BorderStyleConverter";
    }
}

