/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.SizeUnits;
import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.scene.layout.CornerRadii;
import javafx.scene.text.Font;

public final class CornerRadiiConverter
extends StyleConverterImpl<ParsedValue<ParsedValue<?, Size>[][], CornerRadii>[], CornerRadii[]> {
    private static final CornerRadiiConverter INSTANCE = new CornerRadiiConverter();

    public static CornerRadiiConverter getInstance() {
        return INSTANCE;
    }

    private CornerRadiiConverter() {
    }

    @Override
    public CornerRadii[] convert(ParsedValue<ParsedValue<ParsedValue<?, Size>[][], CornerRadii>[], CornerRadii[]> parsedValue, Font font) {
        ParsedValue<ParsedValue<?, Size>[][], CornerRadii>[] arrparsedValue = parsedValue.getValue();
        CornerRadii[] arrcornerRadii = new CornerRadii[arrparsedValue.length];
        for (int i2 = 0; i2 < arrparsedValue.length; ++i2) {
            ParsedValue<?, Size>[][] arrparsedValue2 = arrparsedValue[i2].getValue();
            Size size = arrparsedValue2[0][0].convert(font);
            Size size2 = arrparsedValue2[0][1].convert(font);
            Size size3 = arrparsedValue2[0][2].convert(font);
            Size size4 = arrparsedValue2[0][3].convert(font);
            Size size5 = arrparsedValue2[1][0].convert(font);
            Size size6 = arrparsedValue2[1][1].convert(font);
            Size size7 = arrparsedValue2[1][2].convert(font);
            Size size8 = arrparsedValue2[1][3].convert(font);
            arrcornerRadii[i2] = new CornerRadii(size.pixels(), size5.pixels(), size6.pixels(), size2.pixels(), size3.pixels(), size7.pixels(), size8.pixels(), size4.pixels(), size.getUnits() == SizeUnits.PERCENT, size5.getUnits() == SizeUnits.PERCENT, size6.getUnits() == SizeUnits.PERCENT, size2.getUnits() == SizeUnits.PERCENT, size3.getUnits() == SizeUnits.PERCENT, size7.getUnits() == SizeUnits.PERCENT, size7.getUnits() == SizeUnits.PERCENT, size4.getUnits() == SizeUnits.PERCENT);
        }
        return arrcornerRadii;
    }
}

