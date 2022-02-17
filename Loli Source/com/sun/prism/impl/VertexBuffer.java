/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl;

import com.sun.javafx.geom.transform.AffineBase;
import com.sun.prism.impl.BaseContext;
import com.sun.prism.paint.Color;
import java.util.Arrays;

public final class VertexBuffer {
    protected static final int VERTS_PER_QUAD = 4;
    protected static final int FLOATS_PER_TC = 2;
    protected static final int FLOATS_PER_VC = 3;
    protected static final int FLOATS_PER_VERT = 7;
    protected static final int BYTES_PER_VERT = 4;
    protected static final int VCOFF = 0;
    protected static final int TC1OFF = 3;
    protected static final int TC2OFF = 5;
    protected int capacity;
    protected int index;
    protected byte r;
    protected byte g;
    protected byte b;
    protected byte a;
    protected byte[] colorArray;
    protected float[] coordArray;
    private final BaseContext ownerCtx;

    public VertexBuffer(BaseContext baseContext, int n2) {
        this.ownerCtx = baseContext;
        this.capacity = n2 * 4;
        this.index = 0;
        this.colorArray = new byte[this.capacity * 4];
        this.coordArray = new float[this.capacity * 7];
    }

    public final void setPerVertexColor(Color color, float f2) {
        float f3 = color.getAlpha() * f2;
        this.r = (byte)(color.getRed() * f3 * 255.0f);
        this.g = (byte)(color.getGreen() * f3 * 255.0f);
        this.b = (byte)(color.getBlue() * f3 * 255.0f);
        this.a = (byte)(f3 * 255.0f);
    }

    public final void setPerVertexColor(float f2) {
        this.b = this.a = (byte)(f2 * 255.0f);
        this.g = this.a;
        this.r = this.a;
    }

    public final void updateVertexColors(int n2) {
        for (int i2 = 0; i2 != n2; ++i2) {
            this.putColor(i2);
        }
    }

    private void putColor(int n2) {
        int n3 = n2 * 4;
        this.colorArray[n3 + 0] = this.r;
        this.colorArray[n3 + 1] = this.g;
        this.colorArray[n3 + 2] = this.b;
        this.colorArray[n3 + 3] = this.a;
    }

    public final void flush() {
        if (this.index > 0) {
            this.ownerCtx.drawQuads(this.coordArray, this.colorArray, this.index);
            this.index = 0;
        }
    }

    public final void rewind() {
        this.index = 0;
    }

    private void grow() {
        this.capacity *= 2;
        this.colorArray = Arrays.copyOf(this.colorArray, this.capacity * 4);
        this.coordArray = Arrays.copyOf(this.coordArray, this.capacity * 7);
    }

    public final void addVert(float f2, float f3) {
        if (this.index == this.capacity) {
            this.grow();
        }
        int n2 = 7 * this.index;
        this.coordArray[n2 + 0] = f2;
        this.coordArray[n2 + 1] = f3;
        this.coordArray[n2 + 2] = 0.0f;
        this.putColor(this.index);
        ++this.index;
    }

    public final void addVert(float f2, float f3, float f4, float f5) {
        if (this.index == this.capacity) {
            this.grow();
        }
        int n2 = 7 * this.index;
        this.coordArray[n2 + 0] = f2;
        this.coordArray[n2 + 1] = f3;
        this.coordArray[n2 + 2] = 0.0f;
        this.coordArray[n2 + 3] = f4;
        this.coordArray[n2 + 4] = f5;
        this.putColor(this.index);
        ++this.index;
    }

    public final void addVert(float f2, float f3, float f4, float f5, float f6, float f7) {
        if (this.index == this.capacity) {
            this.grow();
        }
        int n2 = 7 * this.index;
        this.coordArray[n2 + 0] = f2;
        this.coordArray[n2 + 1] = f3;
        this.coordArray[n2 + 2] = 0.0f;
        this.coordArray[n2 + 3] = f4;
        this.coordArray[n2 + 4] = f5;
        this.coordArray[n2 + 5] = f6;
        this.coordArray[n2 + 6] = f7;
        this.putColor(this.index);
        ++this.index;
    }

    private void addVertNoCheck(float f2, float f3) {
        int n2 = 7 * this.index;
        this.coordArray[n2 + 0] = f2;
        this.coordArray[n2 + 1] = f3;
        this.coordArray[n2 + 2] = 0.0f;
        this.putColor(this.index);
        ++this.index;
    }

    private void addVertNoCheck(float f2, float f3, float f4, float f5) {
        int n2 = 7 * this.index;
        this.coordArray[n2 + 0] = f2;
        this.coordArray[n2 + 1] = f3;
        this.coordArray[n2 + 2] = 0.0f;
        this.coordArray[n2 + 3] = f4;
        this.coordArray[n2 + 4] = f5;
        this.putColor(this.index);
        ++this.index;
    }

    private void addVertNoCheck(float f2, float f3, float f4, float f5, float f6, float f7) {
        int n2 = 7 * this.index;
        this.coordArray[n2 + 0] = f2;
        this.coordArray[n2 + 1] = f3;
        this.coordArray[n2 + 2] = 0.0f;
        this.coordArray[n2 + 3] = f4;
        this.coordArray[n2 + 4] = f5;
        this.coordArray[n2 + 5] = f6;
        this.coordArray[n2 + 6] = f7;
        this.putColor(this.index);
        ++this.index;
    }

    private void ensureCapacityForQuad() {
        if (this.index + 4 > this.capacity) {
            this.ownerCtx.drawQuads(this.coordArray, this.colorArray, this.index);
            this.index = 0;
        }
    }

    public final void addQuad(float f2, float f3, float f4, float f5) {
        this.ensureCapacityForQuad();
        this.addVertNoCheck(f2, f3);
        this.addVertNoCheck(f2, f5);
        this.addVertNoCheck(f4, f3);
        this.addVertNoCheck(f4, f5);
    }

    public final void addQuad(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13) {
        this.ensureCapacityForQuad();
        this.addVertNoCheck(f2, f3, f6, f7, f10, f11);
        this.addVertNoCheck(f2, f5, f6, f9, f10, f13);
        this.addVertNoCheck(f4, f3, f8, f7, f12, f11);
        this.addVertNoCheck(f4, f5, f8, f9, f12, f13);
    }

    public final void addMappedQuad(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13) {
        this.ensureCapacityForQuad();
        this.addVertNoCheck(f2, f3, f6, f7);
        this.addVertNoCheck(f2, f5, f10, f11);
        this.addVertNoCheck(f4, f3, f8, f9);
        this.addVertNoCheck(f4, f5, f12, f13);
    }

    public final void addMappedQuad(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17, float f18, float f19, float f20, float f21) {
        this.ensureCapacityForQuad();
        this.addVertNoCheck(f2, f3, f6, f7, f14, f15);
        this.addVertNoCheck(f2, f5, f10, f11, f18, f19);
        this.addVertNoCheck(f4, f3, f8, f9, f16, f17);
        this.addVertNoCheck(f4, f5, f12, f13, f20, f21);
    }

    public final void addQuad(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, AffineBase affineBase) {
        this.addQuad(f2, f3, f4, f5, f6, f7, f8, f9);
        if (affineBase != null) {
            int n2 = 7 * this.index - 7;
            affineBase.transform(this.coordArray, n2 + 0, this.coordArray, n2 + 5, 1);
            affineBase.transform(this.coordArray, (n2 -= 7) + 0, this.coordArray, n2 + 5, 1);
            affineBase.transform(this.coordArray, (n2 -= 7) + 0, this.coordArray, n2 + 5, 1);
            affineBase.transform(this.coordArray, (n2 -= 7) + 0, this.coordArray, n2 + 5, 1);
        }
    }

    public final void addSuperQuad(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, boolean bl) {
        int n2 = this.index;
        if (n2 + 4 > this.capacity) {
            this.ownerCtx.drawQuads(this.coordArray, this.colorArray, n2);
            this.index = 0;
            n2 = 0;
        }
        int n3 = 7 * n2;
        float[] arrf = this.coordArray;
        float f10 = bl ? 1.0f : 0.0f;
        float f11 = bl ? 0.0f : 1.0f;
        arrf[n3] = f2;
        arrf[++n3] = f3;
        arrf[++n3] = 0.0f;
        arrf[++n3] = f6;
        arrf[++n3] = f7;
        arrf[++n3] = f11;
        arrf[++n3] = f10;
        arrf[++n3] = f2;
        arrf[++n3] = f5;
        arrf[++n3] = 0.0f;
        arrf[++n3] = f6;
        arrf[++n3] = f9;
        arrf[++n3] = f11;
        arrf[++n3] = f10;
        arrf[++n3] = f4;
        arrf[++n3] = f3;
        arrf[++n3] = 0.0f;
        arrf[++n3] = f8;
        arrf[++n3] = f7;
        arrf[++n3] = f11;
        arrf[++n3] = f10;
        arrf[++n3] = f4;
        arrf[++n3] = f5;
        arrf[++n3] = 0.0f;
        arrf[++n3] = f8;
        arrf[++n3] = f9;
        arrf[++n3] = f11;
        arrf[++n3] = f10;
        ++n3;
        byte[] arrby = this.colorArray;
        byte by = this.r;
        byte by2 = this.g;
        byte by3 = this.b;
        byte by4 = this.a;
        int n4 = 4 * n2;
        arrby[n4] = by;
        arrby[++n4] = by2;
        arrby[++n4] = by3;
        arrby[++n4] = by4;
        arrby[++n4] = by;
        arrby[++n4] = by2;
        arrby[++n4] = by3;
        arrby[++n4] = by4;
        arrby[++n4] = by;
        arrby[++n4] = by2;
        arrby[++n4] = by3;
        arrby[++n4] = by4;
        arrby[++n4] = by;
        arrby[++n4] = by2;
        arrby[++n4] = by3;
        arrby[++n4] = by4;
        this.index = n2 + 4;
    }

    public final void addQuad(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        int n2 = this.index;
        if (n2 + 4 > this.capacity) {
            this.ownerCtx.drawQuads(this.coordArray, this.colorArray, n2);
            this.index = 0;
            n2 = 0;
        }
        int n3 = 7 * n2;
        float[] arrf = this.coordArray;
        arrf[n3] = f2;
        arrf[++n3] = f3;
        arrf[++n3] = 0.0f;
        arrf[++n3] = f6;
        arrf[++n3] = f7;
        arrf[n3 += 3] = f2;
        arrf[++n3] = f5;
        arrf[++n3] = 0.0f;
        arrf[++n3] = f6;
        arrf[++n3] = f9;
        arrf[n3 += 3] = f4;
        arrf[++n3] = f3;
        arrf[++n3] = 0.0f;
        arrf[++n3] = f8;
        arrf[++n3] = f7;
        arrf[n3 += 3] = f4;
        arrf[++n3] = f5;
        arrf[++n3] = 0.0f;
        arrf[++n3] = f8;
        arrf[++n3] = f9;
        byte[] arrby = this.colorArray;
        byte by = this.r;
        byte by2 = this.g;
        byte by3 = this.b;
        byte by4 = this.a;
        int n4 = 4 * n2;
        arrby[n4] = by;
        arrby[++n4] = by2;
        arrby[++n4] = by3;
        arrby[++n4] = by4;
        arrby[++n4] = by;
        arrby[++n4] = by2;
        arrby[++n4] = by3;
        arrby[++n4] = by4;
        arrby[++n4] = by;
        arrby[++n4] = by2;
        arrby[++n4] = by3;
        arrby[++n4] = by4;
        arrby[++n4] = by;
        arrby[++n4] = by2;
        arrby[++n4] = by3;
        arrby[++n4] = by4;
        this.index = n2 + 4;
    }

    public final void addQuadVO(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11) {
        int n2 = this.index;
        if (n2 + 4 > this.capacity) {
            this.ownerCtx.drawQuads(this.coordArray, this.colorArray, n2);
            this.index = 0;
            n2 = 0;
        }
        int n3 = 7 * n2;
        float[] arrf = this.coordArray;
        arrf[n3] = f4;
        arrf[++n3] = f5;
        arrf[++n3] = 0.0f;
        arrf[++n3] = f8;
        arrf[++n3] = f9;
        arrf[n3 += 3] = f4;
        arrf[++n3] = f7;
        arrf[++n3] = 0.0f;
        arrf[++n3] = f8;
        arrf[++n3] = f11;
        arrf[n3 += 3] = f6;
        arrf[++n3] = f5;
        arrf[++n3] = 0.0f;
        arrf[++n3] = f10;
        arrf[++n3] = f9;
        arrf[n3 += 3] = f6;
        arrf[++n3] = f7;
        arrf[++n3] = 0.0f;
        arrf[++n3] = f10;
        arrf[++n3] = f11;
        byte[] arrby = this.colorArray;
        int n4 = 4 * n2;
        byte by = (byte)(f2 * 255.0f);
        byte by2 = (byte)(f3 * 255.0f);
        arrby[n4] = by;
        arrby[++n4] = by;
        arrby[++n4] = by;
        arrby[++n4] = by;
        arrby[++n4] = by2;
        arrby[++n4] = by2;
        arrby[++n4] = by2;
        arrby[++n4] = by2;
        arrby[++n4] = by;
        arrby[++n4] = by;
        arrby[++n4] = by;
        arrby[++n4] = by;
        arrby[++n4] = by2;
        arrby[++n4] = by2;
        arrby[++n4] = by2;
        arrby[++n4] = by2;
        this.index = n2 + 4;
    }

    public final void addMappedPgram(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17, float f18, float f19, float f20, float f21, AffineBase affineBase) {
        this.addMappedPgram(f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18, f19, f20, f19, f18, f21, f20, f21);
        int n2 = 7 * this.index - 7;
        affineBase.transform(this.coordArray, n2 + 5, this.coordArray, n2 + 5, 1);
        affineBase.transform(this.coordArray, (n2 -= 7) + 5, this.coordArray, n2 + 5, 1);
        affineBase.transform(this.coordArray, (n2 -= 7) + 5, this.coordArray, n2 + 5, 1);
        affineBase.transform(this.coordArray, (n2 -= 7) + 5, this.coordArray, n2 + 5, 1);
    }

    public final void addMappedPgram(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17, float f18, float f19) {
        int n2 = this.index;
        if (n2 + 4 > this.capacity) {
            this.ownerCtx.drawQuads(this.coordArray, this.colorArray, n2);
            this.index = 0;
            n2 = 0;
        }
        int n3 = 7 * n2;
        float[] arrf = this.coordArray;
        arrf[n3] = f2;
        arrf[++n3] = f3;
        arrf[++n3] = 0.0f;
        arrf[++n3] = f10;
        arrf[++n3] = f11;
        arrf[++n3] = f18;
        arrf[++n3] = f19;
        arrf[++n3] = f6;
        arrf[++n3] = f7;
        arrf[++n3] = 0.0f;
        arrf[++n3] = f14;
        arrf[++n3] = f15;
        arrf[++n3] = f18;
        arrf[++n3] = f19;
        arrf[++n3] = f4;
        arrf[++n3] = f5;
        arrf[++n3] = 0.0f;
        arrf[++n3] = f12;
        arrf[++n3] = f13;
        arrf[++n3] = f18;
        arrf[++n3] = f19;
        arrf[++n3] = f8;
        arrf[++n3] = f9;
        arrf[++n3] = 0.0f;
        arrf[++n3] = f16;
        arrf[++n3] = f17;
        arrf[++n3] = f18;
        arrf[++n3] = f19;
        byte[] arrby = this.colorArray;
        byte by = this.r;
        byte by2 = this.g;
        byte by3 = this.b;
        byte by4 = this.a;
        int n4 = 4 * n2;
        arrby[n4] = by;
        arrby[++n4] = by2;
        arrby[++n4] = by3;
        arrby[++n4] = by4;
        arrby[++n4] = by;
        arrby[++n4] = by2;
        arrby[++n4] = by3;
        arrby[++n4] = by4;
        arrby[++n4] = by;
        arrby[++n4] = by2;
        arrby[++n4] = by3;
        arrby[++n4] = by4;
        arrby[++n4] = by;
        arrby[++n4] = by2;
        arrby[++n4] = by3;
        arrby[++n4] = by4;
        this.index = n2 + 4;
    }

    public final void addMappedPgram(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17, float f18, float f19, float f20, float f21, float f22, float f23, float f24, float f25) {
        int n2 = this.index;
        if (n2 + 4 > this.capacity) {
            this.ownerCtx.drawQuads(this.coordArray, this.colorArray, n2);
            this.index = 0;
            n2 = 0;
        }
        int n3 = 7 * n2;
        float[] arrf = this.coordArray;
        arrf[n3] = f2;
        arrf[++n3] = f3;
        arrf[++n3] = 0.0f;
        arrf[++n3] = f10;
        arrf[++n3] = f11;
        arrf[++n3] = f18;
        arrf[++n3] = f19;
        arrf[++n3] = f6;
        arrf[++n3] = f7;
        arrf[++n3] = 0.0f;
        arrf[++n3] = f14;
        arrf[++n3] = f15;
        arrf[++n3] = f22;
        arrf[++n3] = f23;
        arrf[++n3] = f4;
        arrf[++n3] = f5;
        arrf[++n3] = 0.0f;
        arrf[++n3] = f12;
        arrf[++n3] = f13;
        arrf[++n3] = f20;
        arrf[++n3] = f21;
        arrf[++n3] = f8;
        arrf[++n3] = f9;
        arrf[++n3] = 0.0f;
        arrf[++n3] = f16;
        arrf[++n3] = f17;
        arrf[++n3] = f24;
        arrf[++n3] = f25;
        byte[] arrby = this.colorArray;
        byte by = this.r;
        byte by2 = this.g;
        byte by3 = this.b;
        byte by4 = this.a;
        int n4 = 4 * n2;
        arrby[n4] = by;
        arrby[++n4] = by2;
        arrby[++n4] = by3;
        arrby[++n4] = by4;
        arrby[++n4] = by;
        arrby[++n4] = by2;
        arrby[++n4] = by3;
        arrby[++n4] = by4;
        arrby[++n4] = by;
        arrby[++n4] = by2;
        arrby[++n4] = by3;
        arrby[++n4] = by4;
        arrby[++n4] = by;
        arrby[++n4] = by2;
        arrby[++n4] = by3;
        arrby[++n4] = by4;
        this.index = n2 + 4;
    }
}

