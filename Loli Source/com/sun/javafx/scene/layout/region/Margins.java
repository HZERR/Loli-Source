/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.SizeUnits;
import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.util.Logging;
import javafx.css.ParsedValue;
import javafx.scene.text.Font;
import sun.util.logging.PlatformLogger;

public class Margins {
    final double top;
    final double right;
    final double bottom;
    final double left;
    final boolean proportional;

    public final double getTop() {
        return this.top;
    }

    public final double getRight() {
        return this.right;
    }

    public final double getBottom() {
        return this.bottom;
    }

    public final double getLeft() {
        return this.left;
    }

    public final boolean isProportional() {
        return this.proportional;
    }

    public Margins(double d2, double d3, double d4, double d5, boolean bl) {
        this.top = d2;
        this.right = d3;
        this.bottom = d4;
        this.left = d5;
        this.proportional = bl;
    }

    public String toString() {
        return "top: " + this.top + "\nright: " + this.right + "\nbottom: " + this.bottom + "\nleft: " + this.left;
    }

    public static final class SequenceConverter
    extends StyleConverterImpl<ParsedValue<ParsedValue[], Margins>[], Margins[]> {
        public static SequenceConverter getInstance() {
            return Holder.SEQUENCE_CONVERTER_INSTANCE;
        }

        private SequenceConverter() {
        }

        @Override
        public Margins[] convert(ParsedValue<ParsedValue<ParsedValue[], Margins>[], Margins[]> parsedValue, Font font) {
            ParsedValue<ParsedValue[], Margins>[] arrparsedValue = parsedValue.getValue();
            Margins[] arrmargins = new Margins[arrparsedValue.length];
            for (int i2 = 0; i2 < arrparsedValue.length; ++i2) {
                arrmargins[i2] = Converter.getInstance().convert((ParsedValue)arrparsedValue[i2], font);
            }
            return arrmargins;
        }

        public String toString() {
            return "MarginsSequenceConverter";
        }
    }

    public static final class Converter
    extends StyleConverterImpl<ParsedValue[], Margins> {
        public static Converter getInstance() {
            return Holder.CONVERTER_INSTANCE;
        }

        private Converter() {
        }

        @Override
        public Margins convert(ParsedValue<ParsedValue[], Margins> parsedValue, Font font) {
            PlatformLogger platformLogger;
            boolean bl;
            ParsedValue[] arrparsedValue = parsedValue.getValue();
            Size size = arrparsedValue.length > 0 ? (Size)arrparsedValue[0].convert(font) : new Size(0.0, SizeUnits.PX);
            Size size2 = arrparsedValue.length > 1 ? (Size)arrparsedValue[1].convert(font) : size;
            Size size3 = arrparsedValue.length > 2 ? (Size)arrparsedValue[2].convert(font) : size;
            Size size4 = arrparsedValue.length > 3 ? (Size)arrparsedValue[3].convert(font) : size2;
            boolean bl2 = size.getUnits() == SizeUnits.PERCENT || size2.getUnits() == SizeUnits.PERCENT || size3.getUnits() == SizeUnits.PERCENT || size4.getUnits() == SizeUnits.PERCENT;
            boolean bl3 = bl = !bl2 || size.getUnits() == SizeUnits.PERCENT && size2.getUnits() == SizeUnits.PERCENT && size3.getUnits() == SizeUnits.PERCENT && size4.getUnits() == SizeUnits.PERCENT;
            if (!bl && (platformLogger = Logging.getCSSLogger()).isLoggable(PlatformLogger.Level.WARNING)) {
                String string = "units do no match: " + size.toString() + " ," + size2.toString() + " ," + size3.toString() + " ," + size4.toString();
                platformLogger.warning(string);
            }
            bl2 = bl2 && bl;
            double d2 = size.pixels(font);
            double d3 = size2.pixels(font);
            double d4 = size3.pixels(font);
            double d5 = size4.pixels(font);
            return new Margins(d2, d3, d4, d5, bl2);
        }

        public String toString() {
            return "MarginsConverter";
        }
    }

    private static class Holder {
        static Converter CONVERTER_INSTANCE = new Converter();
        static SequenceConverter SEQUENCE_CONVERTER_INSTANCE = new SequenceConverter();

        private Holder() {
        }
    }
}

