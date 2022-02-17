/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.css.converters;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.text.Font;
import javafx.util.Duration;

public final class DurationConverter
extends StyleConverterImpl<ParsedValue<?, Size>, Duration> {
    public static StyleConverter<ParsedValue<?, Size>, Duration> getInstance() {
        return Holder.INSTANCE;
    }

    private DurationConverter() {
    }

    @Override
    public Duration convert(ParsedValue<ParsedValue<?, Size>, Duration> parsedValue, Font font) {
        ParsedValue<?, Size> parsedValue2 = parsedValue.getValue();
        Size size = parsedValue2.convert(font);
        double d2 = size.getValue();
        Duration duration = null;
        if (d2 < Double.POSITIVE_INFINITY) {
            switch (size.getUnits()) {
                case S: {
                    duration = Duration.seconds(d2);
                    break;
                }
                case MS: {
                    duration = Duration.millis(d2);
                    break;
                }
                default: {
                    duration = Duration.UNKNOWN;
                    break;
                }
            }
        } else {
            duration = Duration.INDEFINITE;
        }
        return duration;
    }

    public String toString() {
        return "DurationConverter";
    }

    private static class Holder {
        static final DurationConverter INSTANCE = new DurationConverter();

        private Holder() {
        }
    }
}

