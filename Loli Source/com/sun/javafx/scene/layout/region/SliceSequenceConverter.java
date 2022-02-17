/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.scene.layout.region.BorderImageSliceConverter;
import com.sun.javafx.scene.layout.region.BorderImageSlices;
import javafx.css.ParsedValue;
import javafx.scene.text.Font;

public final class SliceSequenceConverter
extends StyleConverterImpl<ParsedValue<ParsedValue[], BorderImageSlices>[], BorderImageSlices[]> {
    private static final SliceSequenceConverter BORDER_IMAGE_SLICE_SEQUENCE_CONVERTER = new SliceSequenceConverter();

    public static SliceSequenceConverter getInstance() {
        return BORDER_IMAGE_SLICE_SEQUENCE_CONVERTER;
    }

    @Override
    public BorderImageSlices[] convert(ParsedValue<ParsedValue<ParsedValue[], BorderImageSlices>[], BorderImageSlices[]> parsedValue, Font font) {
        ParsedValue<ParsedValue[], BorderImageSlices>[] arrparsedValue = parsedValue.getValue();
        BorderImageSlices[] arrborderImageSlices = new BorderImageSlices[arrparsedValue.length];
        for (int i2 = 0; i2 < arrparsedValue.length; ++i2) {
            arrborderImageSlices[i2] = BorderImageSliceConverter.getInstance().convert((ParsedValue)arrparsedValue[i2], font);
        }
        return arrborderImageSlices;
    }

    public String toString() {
        return "BorderImageSliceSequenceConverter";
    }
}

