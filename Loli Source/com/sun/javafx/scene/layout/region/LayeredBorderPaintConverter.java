/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.scene.layout.region.StrokeBorderPaintConverter;
import javafx.css.ParsedValue;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public final class LayeredBorderPaintConverter
extends StyleConverterImpl<ParsedValue<ParsedValue<?, Paint>[], Paint[]>[], Paint[][]> {
    private static final LayeredBorderPaintConverter LAYERED_BORDER_PAINT_CONVERTER = new LayeredBorderPaintConverter();

    public static LayeredBorderPaintConverter getInstance() {
        return LAYERED_BORDER_PAINT_CONVERTER;
    }

    private LayeredBorderPaintConverter() {
    }

    @Override
    public Paint[][] convert(ParsedValue<ParsedValue<ParsedValue<?, Paint>[], Paint[]>[], Paint[][]> parsedValue, Font font) {
        ParsedValue<ParsedValue<?, Paint>[], Paint[]>[] arrparsedValue = parsedValue.getValue();
        Paint[][] arrpaint = new Paint[arrparsedValue.length][0];
        for (int i2 = 0; i2 < arrparsedValue.length; ++i2) {
            arrpaint[i2] = StrokeBorderPaintConverter.getInstance().convert(arrparsedValue[i2], font);
        }
        return arrpaint;
    }

    public String toString() {
        return "LayeredBorderPaintConverter";
    }
}

