/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.scene.layout.region.RepeatStruct;
import javafx.css.ParsedValue;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.text.Font;

public final class RepeatStructConverter
extends StyleConverterImpl<ParsedValue<String, BackgroundRepeat>[][], RepeatStruct[]> {
    private static final RepeatStructConverter REPEAT_STRUCT_CONVERTER = new RepeatStructConverter();
    private final EnumConverter<BackgroundRepeat> repeatConverter = new EnumConverter<BackgroundRepeat>(BackgroundRepeat.class);

    public static RepeatStructConverter getInstance() {
        return REPEAT_STRUCT_CONVERTER;
    }

    private RepeatStructConverter() {
    }

    @Override
    public RepeatStruct[] convert(ParsedValue<ParsedValue<String, BackgroundRepeat>[][], RepeatStruct[]> parsedValue, Font font) {
        ParsedValue<String, BackgroundRepeat>[][] arrparsedValue = parsedValue.getValue();
        RepeatStruct[] arrrepeatStruct = new RepeatStruct[arrparsedValue.length];
        for (int i2 = 0; i2 < arrparsedValue.length; ++i2) {
            ParsedValue<String, BackgroundRepeat>[] arrparsedValue2 = arrparsedValue[i2];
            BackgroundRepeat backgroundRepeat = (BackgroundRepeat)((Object)this.repeatConverter.convert((ParsedValue)arrparsedValue2[0], (Font)null));
            BackgroundRepeat backgroundRepeat2 = (BackgroundRepeat)((Object)this.repeatConverter.convert((ParsedValue)arrparsedValue2[1], (Font)null));
            arrrepeatStruct[i2] = new RepeatStruct(backgroundRepeat, backgroundRepeat2);
        }
        return arrrepeatStruct;
    }

    public String toString() {
        return "RepeatStructConverter";
    }
}

