/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.fxml.expression;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.util.AbstractList;
import java.util.ArrayList;

public class KeyPath
extends AbstractList<String> {
    private ArrayList<String> elements;

    public KeyPath(ArrayList<String> arrayList) {
        if (arrayList == null) {
            throw new NullPointerException();
        }
        this.elements = arrayList;
    }

    @Override
    public String get(int n2) {
        return this.elements.get(n2);
    }

    @Override
    public int size() {
        return this.elements.size();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        int n2 = this.elements.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            String string = this.elements.get(i2);
            if (Character.isDigit(string.charAt(0))) {
                stringBuilder.append("[");
                stringBuilder.append(string);
                stringBuilder.append("]");
                continue;
            }
            if (i2 > 0) {
                stringBuilder.append(".");
            }
            stringBuilder.append(string);
        }
        return stringBuilder.toString();
    }

    public static KeyPath parse(String string) {
        KeyPath keyPath;
        try (PushbackReader pushbackReader = new PushbackReader(new StringReader(string));){
            keyPath = KeyPath.parse(pushbackReader);
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
        return keyPath;
    }

    protected static KeyPath parse(PushbackReader pushbackReader) throws IOException {
        ArrayList<String> arrayList = new ArrayList<String>();
        int n2 = pushbackReader.read();
        while (n2 != -1 && (Character.isJavaIdentifierStart(n2) || n2 == 91)) {
            boolean bl;
            StringBuilder stringBuilder = new StringBuilder();
            boolean bl2 = bl = n2 == 91;
            if (bl) {
                char c2;
                boolean bl3;
                n2 = pushbackReader.read();
                boolean bl4 = bl3 = n2 == 34 || n2 == 39;
                if (bl3) {
                    c2 = (char)n2;
                    n2 = pushbackReader.read();
                } else {
                    c2 = '\u0000';
                }
                while (n2 != -1 && bl) {
                    if (Character.isISOControl(n2)) {
                        throw new IllegalArgumentException("Illegal identifier character.");
                    }
                    if (!bl3 && !Character.isDigit(n2)) {
                        throw new IllegalArgumentException("Illegal character in index value.");
                    }
                    stringBuilder.append((char)n2);
                    n2 = pushbackReader.read();
                    if (bl3) {
                        boolean bl5 = bl3 = n2 != c2;
                        if (!bl3) {
                            n2 = pushbackReader.read();
                        }
                    }
                    bl = n2 != 93;
                }
                if (bl3) {
                    throw new IllegalArgumentException("Unterminated quoted identifier.");
                }
                if (bl) {
                    throw new IllegalArgumentException("Unterminated bracketed identifier.");
                }
                n2 = pushbackReader.read();
            } else {
                while (n2 != -1 && n2 != 46 && n2 != 91 && Character.isJavaIdentifierPart(n2)) {
                    stringBuilder.append((char)n2);
                    n2 = pushbackReader.read();
                }
            }
            if (n2 == 46 && (n2 = pushbackReader.read()) == -1) {
                throw new IllegalArgumentException("Illegal terminator character.");
            }
            if (stringBuilder.length() == 0) {
                throw new IllegalArgumentException("Missing identifier.");
            }
            arrayList.add(stringBuilder.toString());
        }
        if (arrayList.size() == 0) {
            throw new IllegalArgumentException("Invalid path.");
        }
        if (n2 != -1) {
            pushbackReader.unread(n2);
        }
        return new KeyPath(arrayList);
    }
}

