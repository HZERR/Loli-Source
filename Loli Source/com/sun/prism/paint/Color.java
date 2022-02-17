/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.paint;

import com.sun.prism.paint.Paint;
import java.nio.ByteBuffer;

public final class Color
extends Paint {
    public static final Color WHITE = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    public static final Color BLACK = new Color(0.0f, 0.0f, 0.0f, 1.0f);
    public static final Color RED = new Color(1.0f, 0.0f, 0.0f, 1.0f);
    public static final Color GREEN = new Color(0.0f, 1.0f, 0.0f, 1.0f);
    public static final Color BLUE = new Color(0.0f, 0.0f, 1.0f, 1.0f);
    public static final Color TRANSPARENT = new Color(0.0f, 0.0f, 0.0f, 0.0f);
    private final int argb;
    private final float r;
    private final float g;
    private final float b;
    private final float a;

    public Color(float f2, float f3, float f4, float f5) {
        super(Paint.Type.COLOR, false, false);
        int n2 = (int)(255.0 * (double)f5);
        int n3 = (int)(255.0 * (double)f2 * (double)f5);
        int n4 = (int)(255.0 * (double)f3 * (double)f5);
        int n5 = (int)(255.0 * (double)f4 * (double)f5);
        this.argb = n2 << 24 | n3 << 16 | n4 << 8 | n5 << 0;
        this.r = f2;
        this.g = f3;
        this.b = f4;
        this.a = f5;
    }

    public int getIntArgbPre() {
        return this.argb;
    }

    public void putRgbaPreBytes(byte[] arrby, int n2) {
        arrby[n2 + 0] = (byte)(this.argb >> 16 & 0xFF);
        arrby[n2 + 1] = (byte)(this.argb >> 8 & 0xFF);
        arrby[n2 + 2] = (byte)(this.argb & 0xFF);
        arrby[n2 + 3] = (byte)(this.argb >> 24 & 0xFF);
    }

    public void putBgraPreBytes(ByteBuffer byteBuffer) {
        byteBuffer.put((byte)(this.argb & 0xFF));
        byteBuffer.put((byte)(this.argb >> 8 & 0xFF));
        byteBuffer.put((byte)(this.argb >> 16 & 0xFF));
        byteBuffer.put((byte)(this.argb >> 24 & 0xFF));
    }

    public float getRed() {
        return this.r;
    }

    public float getRedPremult() {
        return this.r * this.a;
    }

    public float getGreen() {
        return this.g;
    }

    public float getGreenPremult() {
        return this.g * this.a;
    }

    public float getBlue() {
        return this.b;
    }

    public float getBluePremult() {
        return this.b * this.a;
    }

    public float getAlpha() {
        return this.a;
    }

    @Override
    public boolean isOpaque() {
        return this.a >= 1.0f;
    }

    public boolean equals(Object object) {
        if (!(object instanceof Color)) {
            return false;
        }
        Color color = (Color)object;
        return this.r == color.r && this.g == color.g && this.b == color.b && this.a == color.a;
    }

    public int hashCode() {
        int n2 = 3;
        n2 = 53 * n2 + Float.floatToIntBits(this.r);
        n2 = 53 * n2 + Float.floatToIntBits(this.g);
        n2 = 53 * n2 + Float.floatToIntBits(this.b);
        n2 = 53 * n2 + Float.floatToIntBits(this.a);
        return n2;
    }

    public String toString() {
        return "Color[r=" + this.r + ", g=" + this.g + ", b=" + this.b + ", a=" + this.a + "]";
    }
}

