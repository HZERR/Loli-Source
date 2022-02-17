/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.paint;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.impl.paint.MultipleGradientContext;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.LinearGradient;

final class LinearGradientContext
extends MultipleGradientContext {
    private float dgdX;
    private float dgdY;
    private float gc;

    LinearGradientContext(LinearGradient linearGradient, BaseTransform baseTransform, float f2, float f3, float f4, float f5, float[] arrf, Color[] arrcolor, int n2) {
        super(linearGradient, baseTransform, arrf, arrcolor, n2);
        float f6 = f4 - f2;
        float f7 = f5 - f3;
        float f8 = f6 * f6 + f7 * f7;
        float f9 = f6 / f8;
        float f10 = f7 / f8;
        this.dgdX = this.a00 * f9 + this.a10 * f10;
        this.dgdY = this.a01 * f9 + this.a11 * f10;
        this.gc = (this.a02 - f2) * f9 + (this.a12 - f3) * f10;
    }

    @Override
    protected void fillRaster(int[] arrn, int n2, int n3, int n4, int n5, int n6, int n7) {
        float f2 = 0.0f;
        int n8 = n2 + n6;
        float f3 = this.dgdX * (float)n4 + this.gc;
        for (int i2 = 0; i2 < n7; ++i2) {
            f2 = f3 + this.dgdY * (float)(n5 + i2);
            while (n2 < n8) {
                arrn[n2++] = this.indexIntoGradientsArrays(f2);
                f2 += this.dgdX;
            }
            n8 = (n2 += n3) + n6;
        }
    }
}

