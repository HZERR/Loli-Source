/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.css.parser;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.SizeUnits;
import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.scene.text.Font;

public final class DeriveSizeConverter
extends StyleConverterImpl<ParsedValue<Size, Size>[], Size> {
    public static DeriveSizeConverter getInstance() {
        return Holder.INSTANCE;
    }

    private DeriveSizeConverter() {
    }

    @Override
    public Size convert(ParsedValue<ParsedValue<Size, Size>[], Size> parsedValue, Font font) {
        ParsedValue<Size, Size>[] arrparsedValue = parsedValue.getValue();
        double d2 = arrparsedValue[0].convert(font).pixels(font);
        double d3 = arrparsedValue[1].convert(font).pixels(font);
        return new Size(d2 + d3, SizeUnits.PX);
    }

    public String toString() {
        return "DeriveSizeConverter";
    }

    private static class Holder {
        static final DeriveSizeConverter INSTANCE = new DeriveSizeConverter();

        private Holder() {
        }
    }
}

