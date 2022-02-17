/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.graphics;

public class WCGlyphBuffer {
    public int[] glyphs;
    public float[] advances;
    public float initialAdvance;

    public WCGlyphBuffer(int[] arrn, float[] arrf, float f2) {
        this.glyphs = arrn;
        this.advances = arrf;
        this.initialAdvance = f2;
    }
}

