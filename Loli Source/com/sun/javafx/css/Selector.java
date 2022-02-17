/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.css;

import com.sun.javafx.css.Combinator;
import com.sun.javafx.css.CompoundSelector;
import com.sun.javafx.css.Match;
import com.sun.javafx.css.Rule;
import com.sun.javafx.css.SimpleSelector;
import com.sun.javafx.css.StringStore;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import javafx.css.PseudoClass;
import javafx.css.Styleable;

public abstract class Selector {
    private Rule rule;
    private int ordinal = -1;
    private static final int TYPE_SIMPLE = 1;
    private static final int TYPE_COMPOUND = 2;

    public static Selector getUniversalSelector() {
        return UniversalSelector.INSTANCE;
    }

    void setRule(Rule rule) {
        this.rule = rule;
    }

    Rule getRule() {
        return this.rule;
    }

    void setOrdinal(int n2) {
        this.ordinal = n2;
    }

    int getOrdinal() {
        return this.ordinal;
    }

    abstract Match createMatch();

    public abstract boolean applies(Styleable var1);

    abstract boolean applies(Styleable var1, Set<PseudoClass>[] var2, int var3);

    public abstract boolean stateMatches(Styleable var1, Set<PseudoClass> var2);

    protected void writeBinary(DataOutputStream dataOutputStream, StringStore stringStore) throws IOException {
        if (this instanceof SimpleSelector) {
            dataOutputStream.writeByte(1);
        } else {
            dataOutputStream.writeByte(2);
        }
    }

    static Selector readBinary(int n2, DataInputStream dataInputStream, String[] arrstring) throws IOException {
        byte by = dataInputStream.readByte();
        if (by == 1) {
            return SimpleSelector.readBinary(n2, dataInputStream, arrstring);
        }
        return CompoundSelector.readBinary(n2, dataInputStream, arrstring);
    }

    public static Selector createSelector(String string) {
        int n2;
        if (string == null || string.length() == 0) {
            return null;
        }
        ArrayList<SimpleSelector> arrayList = new ArrayList<SimpleSelector>();
        ArrayList<Combinator> arrayList2 = new ArrayList<Combinator>();
        ArrayList<String> arrayList3 = new ArrayList<String>();
        int n3 = 0;
        int n4 = -1;
        char c2 = '\u0000';
        for (n2 = 0; n2 < string.length(); ++n2) {
            char c3 = string.charAt(n2);
            if (c3 == ' ') {
                if (c2 != '\u0000') continue;
                c2 = c3;
                n4 = n2;
                continue;
            }
            if (c3 == '>') {
                if (c2 == '\u0000') {
                    n4 = n2;
                }
                c2 = c3;
                continue;
            }
            if (c2 == '\u0000') continue;
            arrayList3.add(string.substring(n3, n4));
            n3 = n2;
            arrayList2.add(c2 == ' ' ? Combinator.DESCENDANT : Combinator.CHILD);
            c2 = '\u0000';
        }
        arrayList3.add(string.substring(n3));
        for (n2 = 0; n2 < arrayList3.size(); ++n2) {
            String string2 = (String)arrayList3.get(n2);
            if (string2 == null || string2.equals("")) continue;
            String[] arrstring = string2.split(":");
            ArrayList<String> arrayList4 = new ArrayList<String>();
            for (int i2 = 1; i2 < arrstring.length; ++i2) {
                if (arrstring[i2] == null || arrstring[i2].equals("")) continue;
                arrayList4.add(arrstring[i2].trim());
            }
            String string3 = arrstring[0].trim();
            String[] arrstring2 = string3.split("\\.");
            ArrayList<String> arrayList5 = new ArrayList<String>();
            for (int i3 = 1; i3 < arrstring2.length; ++i3) {
                if (arrstring2[i3] == null || arrstring2[i3].equals("")) continue;
                arrayList5.add(arrstring2[i3].trim());
            }
            String string4 = null;
            String string5 = null;
            if (!arrstring2[0].equals("")) {
                if (arrstring2[0].charAt(0) == '#') {
                    string5 = arrstring2[0].substring(1).trim();
                } else {
                    string4 = arrstring2[0].trim();
                }
            }
            arrayList.add(new SimpleSelector(string4, arrayList5, arrayList4, string5));
        }
        if (arrayList.size() == 1) {
            return (Selector)arrayList.get(0);
        }
        return new CompoundSelector(arrayList, arrayList2);
    }

    private static class UniversalSelector {
        private static final Selector INSTANCE = new SimpleSelector("*", null, null, null);

        private UniversalSelector() {
        }
    }
}

