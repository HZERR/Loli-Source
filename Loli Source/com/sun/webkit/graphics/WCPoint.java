/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.graphics;

public final class WCPoint {
    final float x;
    final float y;

    public WCPoint(float f2, float f3) {
        this.x = f2;
        this.y = f3;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public int getIntX() {
        return (int)this.x;
    }

    public int getIntY() {
        return (int)this.y;
    }
}

