/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class StrokeBorderPaintConverter
extends StyleConverterImpl<ParsedValue<?, Paint>[], Paint[]> {
    private static final StrokeBorderPaintConverter STROKE_BORDER_PAINT_CONVERTER = new StrokeBorderPaintConverter();

    public static StrokeBorderPaintConverter getInstance() {
        return STROKE_BORDER_PAINT_CONVERTER;
    }

    private StrokeBorderPaintConverter() {
    }

    @Override
    public Paint[] convert(ParsedValue<ParsedValue<?, Paint>[], Paint[]> parsedValue, Font font) {
        Paint[] arrpaint;
        ParsedValue<?, Paint>[] arrparsedValue = parsedValue.getValue();
        arrpaint = new Paint[]{arrparsedValue.length > 0 ? arrparsedValue[0].convert(font) : Color.BLACK, arrparsedValue.length > 1 ? arrparsedValue[1].convert(font) : arrpaint[0], arrparsedValue.length > 2 ? arrparsedValue[2].convert(font) : arrpaint[0], arrparsedValue.length > 3 ? arrparsedValue[3].convert(font) : arrpaint[1]};
        return arrpaint;
    }

    public String toString() {
        return "StrokeBorderPaintConverter";
    }
}

