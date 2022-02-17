/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.css.parser;

final class Token {
    static final int EOF = -1;
    static final int INVALID = 0;
    static final int SKIP = 1;
    static final Token EOF_TOKEN = new Token(-1, "EOF");
    static final Token INVALID_TOKEN = new Token(0, "INVALID");
    static final Token SKIP_TOKEN = new Token(1, "SKIP");
    private final String text;
    private int offset;
    private int line;
    private final int type;

    Token(int n2, String string, int n3, int n4) {
        this.type = n2;
        this.text = string;
        this.line = n3;
        this.offset = n4;
    }

    Token(int n2, String string) {
        this(n2, string, -1, -1);
    }

    Token(int n2) {
        this(n2, null);
    }

    private Token() {
        this(0, "INVALID");
    }

    String getText() {
        return this.text;
    }

    int getType() {
        return this.type;
    }

    int getLine() {
        return this.line;
    }

    void setLine(int n2) {
        this.line = n2;
    }

    int getOffset() {
        return this.offset;
    }

    void setOffset(int n2) {
        this.offset = n2;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('[').append(this.line).append(',').append(this.offset).append(']').append(',').append(this.text).append(",<").append(this.type).append('>');
        return stringBuilder.toString();
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        Token token = (Token)object;
        if (this.type != token.type) {
            return false;
        }
        return !(this.text == null ? token.text != null : !this.text.equals(token.text));
    }

    public int hashCode() {
        int n2 = 7;
        n2 = 67 * n2 + this.type;
        n2 = 67 * n2 + (this.text != null ? this.text.hashCode() : 0);
        return n2;
    }
}

