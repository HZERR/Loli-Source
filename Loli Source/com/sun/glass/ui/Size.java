/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui;

public final class Size {
    public int width;
    public int height;

    public Size(int n2, int n3) {
        this.width = n2;
        this.height = n3;
    }

    public Size() {
        this(0, 0);
    }

    public String toString() {
        return "Size(" + this.width + ", " + this.height + ")";
    }
}

