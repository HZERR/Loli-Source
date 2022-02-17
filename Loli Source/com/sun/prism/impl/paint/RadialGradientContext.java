/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.paint;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.impl.paint.MultipleGradientContext;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.RadialGradient;

final class RadialGradientContext
extends MultipleGradientContext {
    private boolean isSimpleFocus = false;
    private boolean isNonCyclic = false;
    private float radius;
    private float centerX;
    private float centerY;
    private float focusX;
    private float focusY;
    private float radiusSq;
    private float constA;
    private float constB;
    private float gDeltaDelta;
    private float trivial;
    private static final float SCALEBACK = 0.99f;
    private static final int SQRT_LUT_SIZE = 2048;
    private static float[] sqrtLut = new float[2049];

    RadialGradientContext(RadialGradient radialGradient, BaseTransform baseTransform, float f2, float f3, float f4, float f5, float f6, float[] arrf, Color[] arrcolor, int n2) {
        super(radialGradient, baseTransform, arrf, arrcolor, n2);
        this.centerX = f2;
        this.centerY = f3;
        this.focusX = f5;
        this.focusY = f6;
        this.radius = f4;
        this.isSimpleFocus = this.focusX == this.centerX && this.focusY == this.centerY;
        this.isNonCyclic = n2 == 0;
        this.radiusSq = this.radius * this.radius;
        float f7 = this.focusX - this.centerX;
        float f8 = this.focusY - this.centerY;
        double d2 = f7 * f7 + f8 * f8;
        if (d2 > (double)(this.radiusSq * 0.99f)) {
            float f9 = (float)Math.sqrt((double)(this.radiusSq * 0.99f) / d2);
            this.focusX = this.centerX + (f7 *= f9);
            this.focusY = this.centerY + (f8 *= f9);
        }
        this.trivial = (float)Math.sqrt(this.radiusSq - f7 * f7);
        this.constA = this.a02 - this.centerX;
        this.constB = this.a12 - this.centerY;
        this.gDeltaDelta = 2.0f * (this.a00 * this.a00 + this.a10 * this.a10) / this.radiusSq;
    }

    @Override
    protected void fillRaster(int[] arrn, int n2, int n3, int n4, int n5, int n6, int n7) {
        if (this.isSimpleFocus && this.isNonCyclic && this.isSimpleLookup) {
            this.simpleNonCyclicFillRaster(arrn, n2, n3, n4, n5, n6, n7);
        } else {
            this.cyclicCircularGradientFillRaster(arrn, n2, n3, n4, n5, n6, n7);
        }
    }

    private void simpleNonCyclicFillRaster(int[] arrn, int n2, int n3, int n4, int n5, int n6, int n7) {
        float f2 = this.a00 * (float)n4 + this.a01 * (float)n5 + this.constA;
        float f3 = this.a10 * (float)n4 + this.a11 * (float)n5 + this.constB;
        float f4 = this.gDeltaDelta;
        n3 += n6;
        int n8 = this.gradient[this.fastGradientArraySize];
        for (int i2 = 0; i2 < n7; ++i2) {
            int n9;
            float f5 = (f2 * f2 + f3 * f3) / this.radiusSq;
            float f6 = 2.0f * (this.a00 * f2 + this.a10 * f3) / this.radiusSq + f4 / 2.0f;
            for (n9 = 0; n9 < n6 && f5 >= 1.0f; ++n9) {
                arrn[n2 + n9] = n8;
                f5 += f6;
                f6 += f4;
            }
            while (n9 < n6 && f5 < 1.0f) {
                int n10;
                if (f5 <= 0.0f) {
                    n10 = 0;
                } else {
                    float f7 = f5 * 2048.0f;
                    int n11 = (int)f7;
                    float f8 = sqrtLut[n11];
                    float f9 = sqrtLut[n11 + 1] - f8;
                    f7 = f8 + (f7 - (float)n11) * f9;
                    n10 = (int)(f7 * (float)this.fastGradientArraySize);
                }
                arrn[n2 + n9] = this.gradient[n10];
                f5 += f6;
                f6 += f4;
                ++n9;
            }
            while (n9 < n6) {
                arrn[n2 + n9] = n8;
                ++n9;
            }
            n2 += n3;
            f2 += this.a01;
            f3 += this.a11;
        }
    }

    private void cyclicCircularGradientFillRaster(int[] arrn, int n2, int n3, int n4, int n5, int n6, int n7) {
        double d2 = -this.radiusSq + this.centerX * this.centerX + this.centerY * this.centerY;
        float f2 = this.a00 * (float)n4 + this.a01 * (float)n5 + this.a02;
        float f3 = this.a10 * (float)n4 + this.a11 * (float)n5 + this.a12;
        float f4 = 2.0f * this.centerY;
        float f5 = -2.0f * this.centerX;
        int n8 = n2;
        int n9 = n6 + n3;
        for (int i2 = 0; i2 < n7; ++i2) {
            float f6 = this.a01 * (float)i2 + f2;
            float f7 = this.a11 * (float)i2 + f3;
            for (int i3 = 0; i3 < n6; ++i3) {
                double d3;
                double d4;
                if (f6 == this.focusX) {
                    d4 = this.focusX;
                    d3 = this.centerY;
                    d3 += f7 > this.focusY ? (double)this.trivial : (double)(-this.trivial);
                } else {
                    double d5 = (f7 - this.focusY) / (f6 - this.focusX);
                    double d6 = (double)f7 - d5 * (double)f6;
                    double d7 = d5 * d5 + 1.0;
                    double d8 = (double)f5 + -2.0 * d5 * ((double)this.centerY - d6);
                    double d9 = d2 + d6 * (d6 - (double)f4);
                    float f8 = (float)Math.sqrt(d8 * d8 - 4.0 * d7 * d9);
                    d4 = -d8;
                    d4 += f6 < this.focusX ? (double)(-f8) : (double)f8;
                    d3 = d5 * (d4 /= 2.0 * d7) + d6;
                }
                float f9 = f6 - this.focusX;
                f9 *= f9;
                float f10 = f7 - this.focusY;
                f10 *= f10;
                float f11 = f9 + f10;
                f9 = (float)d4 - this.focusX;
                f9 *= f9;
                f10 = (float)d3 - this.focusY;
                f10 *= f10;
                float f12 = f9 + f10;
                float f13 = (float)Math.sqrt(f11 / f12);
                arrn[n8 + i3] = this.indexIntoGradientsArrays(f13);
                f6 += this.a00;
                f7 += this.a10;
            }
            n8 += n9;
        }
    }

    static {
        for (int i2 = 0; i2 < sqrtLut.length; ++i2) {
            RadialGradientContext.sqrtLut[i2] = (float)Math.sqrt((float)i2 / 2048.0f);
        }
    }
}

