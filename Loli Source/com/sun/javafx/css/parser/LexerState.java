/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.css.parser;

import com.sun.javafx.css.parser.Recognizer;

class LexerState {
    private final int type;
    private final String name;
    private final Recognizer[] recognizers;

    public boolean accepts(int n2) {
        int n3 = this.recognizers != null ? this.recognizers.length : 0;
        for (int i2 = 0; i2 < n3; ++i2) {
            if (!this.recognizers[i2].recognize(n2)) continue;
            return true;
        }
        return false;
    }

    public int getType() {
        return this.type;
    }

    public LexerState(int n2, String string, Recognizer recognizer, Recognizer ... arrrecognizer) {
        assert (string != null);
        this.type = n2;
        this.name = string;
        if (recognizer != null) {
            int n3 = 1 + (arrrecognizer != null ? arrrecognizer.length : 0);
            this.recognizers = new Recognizer[n3];
            this.recognizers[0] = recognizer;
            for (int i2 = 1; i2 < this.recognizers.length; ++i2) {
                this.recognizers[i2] = arrrecognizer[i2 - 1];
            }
        } else {
            this.recognizers = null;
        }
    }

    public LexerState(String string, Recognizer recognizer, Recognizer ... arrrecognizer) {
        this(0, string, recognizer, arrrecognizer);
    }

    private LexerState() {
        this(0, "invalid", null, new Recognizer[0]);
    }

    public String toString() {
        return this.name;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        return object instanceof LexerState ? this.name.equals(((LexerState)object).name) : false;
    }

    public int hashCode() {
        return this.name.hashCode();
    }
}

