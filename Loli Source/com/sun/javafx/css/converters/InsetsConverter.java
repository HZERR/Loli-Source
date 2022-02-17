/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.css.converters;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.geometry.Insets;
import javafx.scene.text.Font;

public final class InsetsConverter
extends StyleConverterImpl<ParsedValue[], Insets> {
    public static StyleConverter<ParsedValue[], Insets> getInstance() {
        return Holder.INSTANCE;
    }

    private InsetsConverter() {
    }

    @Override
    public Insets convert(ParsedValue<ParsedValue[], Insets> parsedValue, Font font) {
        ParsedValue[] arrparsedValue = parsedValue.getValue();
        double d2 = ((Size)arrparsedValue[0].convert(font)).pixels(font);
        double d3 = arrparsedValue.length > 1 ? ((Size)arrparsedValue[1].convert(font)).pixels(font) : d2;
        double d4 = arrparsedValue.length > 2 ? ((Size)arrparsedValue[2].convert(font)).pixels(font) : d2;
        double d5 = arrparsedValue.length > 3 ? ((Size)arrparsedValue[3].convert(font)).pixels(font) : d3;
        return new Insets(d2, d3, d4, d5);
    }

    public String toString() {
        return "InsetsConverter";
    }

    public static final class SequenceConverter
    extends StyleConverterImpl<ParsedValue<ParsedValue[], Insets>[], Insets[]> {
        public static SequenceConverter getInstance() {
            return Holder.SEQUENCE_INSTANCE;
        }

        private SequenceConverter() {
        }

        @Override
        public Insets[] convert(ParsedValue<ParsedValue<ParsedValue[], Insets>[], Insets[]> parsedValue, Font font) {
            ParsedValue<ParsedValue[], Insets>[] arrparsedValue = parsedValue.getValue();
            Insets[] arrinsets = new Insets[arrparsedValue.length];
            for (int i2 = 0; i2 < arrparsedValue.length; ++i2) {
                arrinsets[i2] = InsetsConverter.getInstance().convert(arrparsedValue[i2], font);
            }
            return arrinsets;
        }

        public String toString() {
            return "InsetsSequenceConverter";
        }
    }

    private static class Holder {
        static final InsetsConverter INSTANCE = new InsetsConverter();
        static final SequenceConverter SEQUENCE_INSTANCE = new SequenceConverter();

        private Holder() {
        }
    }
}

