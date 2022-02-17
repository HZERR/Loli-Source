/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.css;

import com.sun.javafx.css.ParsedValueImpl;
import com.sun.javafx.css.Rule;
import com.sun.javafx.css.StringStore;
import com.sun.javafx.css.converters.URLConverter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.css.StyleOrigin;

public final class Declaration {
    final String property;
    final ParsedValueImpl parsedValue;
    final boolean important;
    Rule rule;

    public Declaration(String string, ParsedValueImpl parsedValueImpl, boolean bl) {
        this.property = string;
        this.parsedValue = parsedValueImpl;
        this.important = bl;
        if (string == null) {
            throw new IllegalArgumentException("propertyName cannot be null");
        }
        if (parsedValueImpl == null) {
            throw new IllegalArgumentException("parsedValue cannot be null");
        }
    }

    public ParsedValue getParsedValue() {
        return this.parsedValue;
    }

    ParsedValueImpl getParsedValueImpl() {
        return this.parsedValue;
    }

    public String getProperty() {
        return this.property;
    }

    public Rule getRule() {
        return this.rule;
    }

    public boolean isImportant() {
        return this.important;
    }

    private StyleOrigin getOrigin() {
        Rule rule = this.getRule();
        if (rule != null) {
            return rule.getOrigin();
        }
        return null;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        Declaration declaration = (Declaration)object;
        if (this.important != declaration.important) {
            return false;
        }
        if (this.getOrigin() != declaration.getOrigin()) {
            return false;
        }
        if (this.property == null ? declaration.property != null : !this.property.equals(declaration.property)) {
            return false;
        }
        return this.parsedValue == declaration.parsedValue || this.parsedValue != null && this.parsedValue.equals(declaration.parsedValue);
    }

    public int hashCode() {
        int n2 = 5;
        n2 = 89 * n2 + (this.property != null ? this.property.hashCode() : 0);
        n2 = 89 * n2 + (this.parsedValue != null ? this.parsedValue.hashCode() : 0);
        n2 = 89 * n2 + (this.important ? 1 : 0);
        return n2;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(this.property);
        stringBuilder.append(": ");
        stringBuilder.append(this.parsedValue);
        if (this.important) {
            stringBuilder.append(" !important");
        }
        return stringBuilder.toString();
    }

    void fixUrl(String string) {
        if (string == null) {
            return;
        }
        StyleConverter styleConverter = this.parsedValue.getConverter();
        if (styleConverter == URLConverter.getInstance()) {
            ParsedValue[] arrparsedValue = (ParsedValue[])this.parsedValue.getValue();
            arrparsedValue[1] = new ParsedValueImpl(string, null);
        } else if (styleConverter == URLConverter.SequenceConverter.getInstance()) {
            ParsedValue[] arrparsedValue = (ParsedValue[])this.parsedValue.getValue();
            for (int i2 = 0; i2 < arrparsedValue.length; ++i2) {
                ParsedValue[] arrparsedValue2 = (ParsedValue[])arrparsedValue[i2].getValue();
                arrparsedValue2[1] = new ParsedValueImpl(string, null);
            }
        }
    }

    final void writeBinary(DataOutputStream dataOutputStream, StringStore stringStore) throws IOException {
        dataOutputStream.writeShort(stringStore.addString(this.getProperty()));
        this.getParsedValueImpl().writeBinary(dataOutputStream, stringStore);
        dataOutputStream.writeBoolean(this.isImportant());
    }

    static Declaration readBinary(int n2, DataInputStream dataInputStream, String[] arrstring) throws IOException {
        String string = arrstring[dataInputStream.readShort()];
        ParsedValueImpl parsedValueImpl = ParsedValueImpl.readBinary(n2, dataInputStream, arrstring);
        boolean bl = dataInputStream.readBoolean();
        return new Declaration(string, parsedValueImpl, bl);
    }
}

