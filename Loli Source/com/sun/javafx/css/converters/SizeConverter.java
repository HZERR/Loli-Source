/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.css.converters;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.text.Font;

public final class SizeConverter
extends StyleConverterImpl<ParsedValue<?, Size>, Number> {
    public static StyleConverter<ParsedValue<?, Size>, Number> getInstance() {
        return Holder.INSTANCE;
    }

    private SizeConverter() {
    }

    @Override
    public Number convert(ParsedValue<ParsedValue<?, Size>, Number> parsedValue, Font font) {
        ParsedValue<?, Size> parsedValue2 = parsedValue.getValue();
        return parsedValue2.convert(font).pixels(font);
    }

    public String toString() {
        return "SizeConverter";
    }

    public static final class SequenceConverter
    extends StyleConverterImpl<ParsedValue[], Number[]> {
        public static SequenceConverter getInstance() {
            return Holder.SEQUENCE_INSTANCE;
        }

        private SequenceConverter() {
        }

        @Override
        public Number[] convert(ParsedValue<ParsedValue[], Number[]> parsedValue, Font font) {
            ParsedValue[] arrparsedValue = parsedValue.getValue();
            Number[] arrnumber = new Number[arrparsedValue.length];
            for (int i2 = 0; i2 < arrparsedValue.length; ++i2) {
                arrnumber[i2] = ((Size)arrparsedValue[i2].convert(font)).pixels(font);
            }
            return arrnumber;
        }

        public String toString() {
            return "Size.SequenceConverter";
        }
    }

    private static class Holder {
        static final SizeConverter INSTANCE = new SizeConverter();
        static final SequenceConverter SEQUENCE_INSTANCE = new SequenceConverter();

        private Holder() {
        }
    }
}

