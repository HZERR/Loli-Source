/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.t2k;

import com.sun.javafx.font.Glyph;
import com.sun.javafx.font.t2k.T2KFontStrike;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;

class T2KGlyph
implements Glyph {
    private T2KFontStrike strike;
    private int gc;
    private float userAdvance;
    private float deviceXAdvance;
    private float deviceYAdvance;
    byte[] pixelData;
    private int width;
    private int height;
    private int originX;
    private int originY;
    private boolean isLCDGlyph;
    private RectBounds b2d;

    public T2KGlyph(T2KFontStrike t2KFontStrike, int n2, float f2) {
        this.strike = t2KFontStrike;
        this.gc = n2;
        this.userAdvance = f2;
    }

    T2KGlyph(T2KFontStrike t2KFontStrike, int n2, long l2) {
        this.strike = t2KFontStrike;
        this.gc = n2;
        int[] arrn = this.getGlyphInfo(l2);
        this.width = arrn[0];
        this.height = arrn[1];
        this.originX = arrn[2];
        this.originY = arrn[3];
        int n3 = arrn[4];
        this.isLCDGlyph = false;
        if (n3 > this.width) {
            this.width = n3;
            this.isLCDGlyph = true;
        }
        this.deviceXAdvance = this.getGlyphPixelXAdvance(l2);
        this.deviceYAdvance = this.getGlyphPixelYAdvance(l2);
        this.userAdvance = t2KFontStrike.getGlyphUserAdvance(this.deviceXAdvance, this.deviceYAdvance);
        this.pixelData = this.getGlyphPixelData(l2);
        this.freeGlyph(l2);
    }

    @Override
    public int getGlyphCode() {
        return this.gc;
    }

    @Override
    public RectBounds getBBox() {
        if (this.b2d == null) {
            this.b2d = this.strike.getGlyphBounds(this.gc);
        }
        return this.b2d;
    }

    private native int[] getGlyphInfo(long var1);

    private native byte[] getGlyphPixelData(long var1);

    private native float getGlyphPixelXAdvance(long var1);

    private native float getGlyphPixelYAdvance(long var1);

    private native void freeGlyph(long var1);

    void setAdvance(float f2, float f3, float f4) {
        this.userAdvance = f2;
        this.deviceXAdvance = f3;
        this.deviceYAdvance = f4;
    }

    @Override
    public float getAdvance() {
        return this.userAdvance;
    }

    @Override
    public Shape getShape() {
        return this.strike.createGlyphOutline(this.gc);
    }

    @Override
    public float getPixelXAdvance() {
        return this.deviceXAdvance;
    }

    @Override
    public float getPixelYAdvance() {
        return this.deviceYAdvance;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public int getOriginX() {
        return this.originX;
    }

    @Override
    public int getOriginY() {
        return this.originY;
    }

    @Override
    public byte[] getPixelData() {
        return this.pixelData;
    }

    @Override
    public byte[] getPixelData(int n2) {
        return this.pixelData;
    }

    @Override
    public boolean isLCDGlyph() {
        return this.isLCDGlyph;
    }
}

