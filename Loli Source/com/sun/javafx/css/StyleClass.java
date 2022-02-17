/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.css;

final class StyleClass {
    private final String styleClassName;
    private final int index;

    StyleClass(String string, int n2) {
        this.styleClassName = string;
        this.index = n2;
    }

    public String getStyleClassName() {
        return this.styleClassName;
    }

    public String toString() {
        return this.styleClassName;
    }

    public int getIndex() {
        return this.index;
    }
}

