/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.SizeUnits;
import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.scene.layout.region.BorderImageSlices;
import javafx.css.ParsedValue;
import javafx.scene.layout.BorderWidths;
import javafx.scene.text.Font;

public final class BorderImageSliceConverter
extends StyleConverterImpl<ParsedValue[], BorderImageSlices> {
    private static final BorderImageSliceConverter BORDER_IMAGE_SLICE_CONVERTER = new BorderImageSliceConverter();

    public static BorderImageSliceConverter getInstance() {
        return BORDER_IMAGE_SLICE_CONVERTER;
    }

    private BorderImageSliceConverter() {
    }

    @Override
    public BorderImageSlices convert(ParsedValue<ParsedValue[], BorderImageSlices> parsedValue, Font font) {
        ParsedValue[] arrparsedValue = parsedValue.getValue();
        ParsedValue[] arrparsedValue2 = (ParsedValue[])arrparsedValue[0].getValue();
        Size size = (Size)arrparsedValue2[0].convert(font);
        Size size2 = (Size)arrparsedValue2[1].convert(font);
        Size size3 = (Size)arrparsedValue2[2].convert(font);
        Size size4 = (Size)arrparsedValue2[3].convert(font);
        return new BorderImageSlices(new BorderWidths(size.pixels(font), size2.pixels(font), size3.pixels(font), size4.pixels(font), size.getUnits() == SizeUnits.PERCENT, size2.getUnits() == SizeUnits.PERCENT, size3.getUnits() == SizeUnits.PERCENT, size4.getUnits() == SizeUnits.PERCENT), (Boolean)arrparsedValue[1].getValue());
    }

    public String toString() {
        return "BorderImageSliceConverter";
    }
}

