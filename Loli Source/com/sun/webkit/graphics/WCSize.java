/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.graphics;

public final class WCSize {
    private final float width;
    private final float height;

    public WCSize(float f2, float f3) {
        this.width = f2;
        this.height = f3;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public int getIntWidth() {
        return (int)this.width;
    }

    public int getIntHeight() {
        return (int)this.height;
    }
}

