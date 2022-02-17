/*
 * Decompiled with CFR 0.150.
 */
package javafx.util.converter;

import javafx.util.StringConverter;

public class IntegerStringConverter
extends StringConverter<Integer> {
    @Override
    public Integer fromString(String string) {
        if (string == null) {
            return null;
        }
        if ((string = string.trim()).length() < 1) {
            return null;
        }
        return Integer.valueOf(string);
    }

    @Override
    public String toString(Integer n2) {
        if (n2 == null) {
            return "";
        }
        return Integer.toString(n2);
    }
}

