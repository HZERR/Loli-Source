/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.SizeUnits;
import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.geometry.Side;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.text.Font;

public final class BackgroundPositionConverter
extends StyleConverterImpl<ParsedValue[], BackgroundPosition> {
    private static final BackgroundPositionConverter BACKGROUND_POSITION_CONVERTER = new BackgroundPositionConverter();

    public static BackgroundPositionConverter getInstance() {
        return BACKGROUND_POSITION_CONVERTER;
    }

    private BackgroundPositionConverter() {
    }

    @Override
    public BackgroundPosition convert(ParsedValue<ParsedValue[], BackgroundPosition> parsedValue, Font font) {
        ParsedValue[] arrparsedValue = parsedValue.getValue();
        Size size = (Size)arrparsedValue[0].convert(font);
        Size size2 = (Size)arrparsedValue[1].convert(font);
        Size size3 = (Size)arrparsedValue[2].convert(font);
        Size size4 = (Size)arrparsedValue[3].convert(font);
        boolean bl = size3.getValue() > 0.0 && size3.getUnits() == SizeUnits.PERCENT || size.getValue() > 0.0 && size.getUnits() == SizeUnits.PERCENT || size.getValue() == 0.0 && size3.getValue() == 0.0;
        boolean bl2 = size2.getValue() > 0.0 && size2.getUnits() == SizeUnits.PERCENT || size4.getValue() > 0.0 && size4.getUnits() == SizeUnits.PERCENT || size4.getValue() == 0.0 && size2.getValue() == 0.0;
        double d2 = size.pixels(font);
        double d3 = size2.pixels(font);
        double d4 = size3.pixels(font);
        double d5 = size4.pixels(font);
        return new BackgroundPosition(d5 == 0.0 && d3 != 0.0 ? Side.RIGHT : Side.LEFT, d5 == 0.0 && d3 != 0.0 ? d3 : d5, bl2, d2 == 0.0 && d4 != 0.0 ? Side.BOTTOM : Side.TOP, d2 == 0.0 && d4 != 0.0 ? d4 : d2, bl);
    }

    public String toString() {
        return "BackgroundPositionConverter";
    }
}

