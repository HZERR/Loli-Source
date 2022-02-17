/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.paint;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Gradient;

abstract class MultipleGradientContext {
    protected int cycleMethod;
    protected float a00;
    protected float a01;
    protected float a10;
    protected float a11;
    protected float a02;
    protected float a12;
    protected boolean isSimpleLookup;
    protected int fastGradientArraySize;
    protected int[] gradient;
    private int[][] gradients;
    private float[] normalizedIntervals;
    private float[] fractions;
    private int transparencyTest;
    protected static final int GRADIENT_SIZE = 256;
    protected static final int GRADIENT_SIZE_INDEX = 255;
    private static final int MAX_GRADIENT_ARRAY_SIZE = 5000;

    protected MultipleGradientContext(Gradient gradient, BaseTransform baseTransform, float[] arrf, Color[] arrcolor, int n2) {
        BaseTransform baseTransform2;
        if (baseTransform == null) {
            throw new NullPointerException("Transform cannot be null");
        }
        try {
            baseTransform2 = baseTransform.createInverse();
        }
        catch (NoninvertibleTransformException noninvertibleTransformException) {
            baseTransform2 = BaseTransform.IDENTITY_TRANSFORM;
        }
        this.a00 = (float)baseTransform2.getMxx();
        this.a10 = (float)baseTransform2.getMyx();
        this.a01 = (float)baseTransform2.getMxy();
        this.a11 = (float)baseTransform2.getMyy();
        this.a02 = (float)baseTransform2.getMxt();
        this.a12 = (float)baseTransform2.getMyt();
        this.cycleMethod = n2;
        this.fractions = arrf;
        this.calculateLookupData(arrcolor);
    }

    private void calculateLookupData(Color[] arrcolor) {
        Color[] arrcolor2 = arrcolor;
        this.normalizedIntervals = new float[this.fractions.length - 1];
        for (int i2 = 0; i2 < this.normalizedIntervals.length; ++i2) {
            this.normalizedIntervals[i2] = this.fractions[i2 + 1] - this.fractions[i2];
        }
        this.transparencyTest = -16777216;
        this.gradients = new int[this.normalizedIntervals.length][];
        float f2 = 1.0f;
        for (int i3 = 0; i3 < this.normalizedIntervals.length; ++i3) {
            f2 = f2 > this.normalizedIntervals[i3] ? this.normalizedIntervals[i3] : f2;
        }
        float f3 = 0.0f;
        for (int i4 = 0; i4 < this.normalizedIntervals.length && Float.isFinite(f3); ++i4) {
            f3 += this.normalizedIntervals[i4] / f2 * 256.0f;
        }
        if (f3 <= 5000.0f) {
            this.calculateSingleArrayGradient(arrcolor2, f2);
        } else {
            this.calculateMultipleArrayGradient(arrcolor2);
        }
    }

    private void calculateSingleArrayGradient(Color[] arrcolor, float f2) {
        int n2;
        int n3;
        this.isSimpleLookup = true;
        int n4 = 1;
        for (n3 = 0; n3 < this.gradients.length; ++n3) {
            n2 = (int)(this.normalizedIntervals[n3] / f2 * 255.0f);
            n4 += n2;
            this.gradients[n3] = new int[n2];
            int n5 = arrcolor[n3].getIntArgbPre();
            int n6 = arrcolor[n3 + 1].getIntArgbPre();
            this.interpolate(n5, n6, this.gradients[n3]);
            this.transparencyTest &= n5;
            this.transparencyTest &= n6;
        }
        this.gradient = new int[n4];
        n3 = 0;
        for (n2 = 0; n2 < this.gradients.length; ++n2) {
            System.arraycopy(this.gradients[n2], 0, this.gradient, n3, this.gradients[n2].length);
            n3 += this.gradients[n2].length;
        }
        this.gradient[this.gradient.length - 1] = arrcolor[arrcolor.length - 1].getIntArgbPre();
        this.fastGradientArraySize = this.gradient.length - 1;
    }

    private void calculateMultipleArrayGradient(Color[] arrcolor) {
        this.isSimpleLookup = false;
        for (int i2 = 0; i2 < this.gradients.length; ++i2) {
            this.gradients[i2] = new int[256];
            int n2 = arrcolor[i2].getIntArgbPre();
            int n3 = arrcolor[i2 + 1].getIntArgbPre();
            this.interpolate(n2, n3, this.gradients[i2]);
            this.transparencyTest &= n2;
            this.transparencyTest &= n3;
        }
    }

    private void interpolate(int n2, int n3, int[] arrn) {
        float f2 = 1.0f / (float)arrn.length;
        int n4 = n2 >> 24 & 0xFF;
        int n5 = n2 >> 16 & 0xFF;
        int n6 = n2 >> 8 & 0xFF;
        int n7 = n2 & 0xFF;
        int n8 = (n3 >> 24 & 0xFF) - n4;
        int n9 = (n3 >> 16 & 0xFF) - n5;
        int n10 = (n3 >> 8 & 0xFF) - n6;
        int n11 = (n3 & 0xFF) - n7;
        for (int i2 = 0; i2 < arrn.length; ++i2) {
            arrn[i2] = (int)((double)((float)n4 + (float)(i2 * n8) * f2) + 0.5) << 24 | (int)((double)((float)n5 + (float)(i2 * n9) * f2) + 0.5) << 16 | (int)((double)((float)n6 + (float)(i2 * n10) * f2) + 0.5) << 8 | (int)((double)((float)n7 + (float)(i2 * n11) * f2) + 0.5);
        }
    }

    protected final int indexIntoGradientsArrays(float f2) {
        int n2;
        if (this.cycleMethod == 0) {
            if (f2 > 1.0f) {
                f2 = 1.0f;
            } else if (f2 < 0.0f) {
                f2 = 0.0f;
            }
        } else if (this.cycleMethod == 2) {
            if ((f2 -= (float)((int)f2)) < 0.0f) {
                f2 += 1.0f;
            }
        } else {
            if (f2 < 0.0f) {
                f2 = -f2;
            }
            n2 = (int)f2;
            f2 -= (float)n2;
            if ((n2 & 1) == 1) {
                f2 = 1.0f - f2;
            }
        }
        if (this.isSimpleLookup) {
            return this.gradient[(int)(f2 * (float)this.fastGradientArraySize)];
        }
        if (f2 < this.fractions[0]) {
            return this.gradients[0][0];
        }
        for (n2 = 0; n2 < this.gradients.length; ++n2) {
            if (!(f2 < this.fractions[n2 + 1])) continue;
            float f3 = f2 - this.fractions[n2];
            int n3 = (int)(f3 / this.normalizedIntervals[n2] * 255.0f);
            return this.gradients[n2][n3];
        }
        return this.gradients[this.gradients.length - 1][255];
    }

    protected abstract void fillRaster(int[] var1, int var2, int var3, int var4, int var5, int var6, int var7);
}

