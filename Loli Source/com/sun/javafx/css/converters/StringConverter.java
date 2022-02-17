/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.css.converters;

import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.util.Utils;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.text.Font;

public final class StringConverter
extends StyleConverterImpl<String, String> {
    public static StyleConverter<String, String> getInstance() {
        return Holder.INSTANCE;
    }

    private StringConverter() {
    }

    @Override
    public String convert(ParsedValue<String, String> parsedValue, Font font) {
        String string = parsedValue.getValue();
        if (string == null) {
            return null;
        }
        return Utils.convertUnicode(string);
    }

    public String toString() {
        return "StringConverter";
    }

    public static final class SequenceConverter
    extends StyleConverterImpl<ParsedValue<String, String>[], String[]> {
        public static SequenceConverter getInstance() {
            return Holder.SEQUENCE_INSTANCE;
        }

        private SequenceConverter() {
        }

        @Override
        public String[] convert(ParsedValue<ParsedValue<String, String>[], String[]> parsedValue, Font font) {
            ParsedValue<String, String>[] arrparsedValue = parsedValue.getValue();
            String[] arrstring = new String[arrparsedValue.length];
            for (int i2 = 0; i2 < arrparsedValue.length; ++i2) {
                arrstring[i2] = StringConverter.getInstance().convert(arrparsedValue[i2], font);
            }
            return arrstring;
        }

        public String toString() {
            return "String.SequenceConverter";
        }
    }

    private static class Holder {
        static final StringConverter INSTANCE = new StringConverter();
        static final SequenceConverter SEQUENCE_INSTANCE = new SequenceConverter();

        private Holder() {
        }
    }
}

