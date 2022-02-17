/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.j2d.paint;

import com.sun.prism.j2d.paint.MultipleGradientPaint;
import java.awt.Color;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.SinglePixelPackedSampleModel;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

abstract class MultipleGradientPaintContext
implements PaintContext {
    protected ColorModel model;
    private static ColorModel xrgbmodel = new DirectColorModel(24, 0xFF0000, 65280, 255);
    protected static ColorModel cachedModel;
    protected static WeakReference<Raster> cached;
    protected Raster saved;
    protected MultipleGradientPaint.CycleMethod cycleMethod;
    protected MultipleGradientPaint.ColorSpaceType colorSpace;
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
    private static final int[] SRGBtoLinearRGB;
    private static final int[] LinearRGBtoSRGB;
    protected static final int GRADIENT_SIZE = 256;
    protected static final int GRADIENT_SIZE_INDEX = 255;
    private static final int MAX_GRADIENT_ARRAY_SIZE = 5000;

    protected MultipleGradientPaintContext(MultipleGradientPaint multipleGradientPaint, ColorModel colorModel, Rectangle rectangle, Rectangle2D rectangle2D, AffineTransform affineTransform, RenderingHints renderingHints, float[] arrf, Color[] arrcolor, MultipleGradientPaint.CycleMethod cycleMethod, MultipleGradientPaint.ColorSpaceType colorSpaceType) {
        AffineTransform affineTransform2;
        if (rectangle == null) {
            throw new NullPointerException("Device bounds cannot be null");
        }
        if (rectangle2D == null) {
            throw new NullPointerException("User bounds cannot be null");
        }
        if (affineTransform == null) {
            throw new NullPointerException("Transform cannot be null");
        }
        try {
            affineTransform2 = affineTransform.createInverse();
        }
        catch (NoninvertibleTransformException noninvertibleTransformException) {
            affineTransform2 = new AffineTransform();
        }
        double[] arrd = new double[6];
        affineTransform2.getMatrix(arrd);
        this.a00 = (float)arrd[0];
        this.a10 = (float)arrd[1];
        this.a01 = (float)arrd[2];
        this.a11 = (float)arrd[3];
        this.a02 = (float)arrd[4];
        this.a12 = (float)arrd[5];
        this.cycleMethod = cycleMethod;
        this.colorSpace = colorSpaceType;
        this.fractions = arrf;
        this.gradient = multipleGradientPaint.gradient != null ? multipleGradientPaint.gradient.get() : null;
        int[][] arrn = this.gradients = multipleGradientPaint.gradients != null ? multipleGradientPaint.gradients.get() : (int[][])null;
        if (this.gradient == null && this.gradients == null) {
            this.calculateLookupData(arrcolor);
            multipleGradientPaint.model = this.model;
            multipleGradientPaint.normalizedIntervals = this.normalizedIntervals;
            multipleGradientPaint.isSimpleLookup = this.isSimpleLookup;
            if (this.isSimpleLookup) {
                multipleGradientPaint.fastGradientArraySize = this.fastGradientArraySize;
                multipleGradientPaint.gradient = new SoftReference<int[]>(this.gradient);
            } else {
                multipleGradientPaint.gradients = new SoftReference<int[][]>(this.gradients);
            }
        } else {
            this.model = multipleGradientPaint.model;
            this.normalizedIntervals = multipleGradientPaint.normalizedIntervals;
            this.isSimpleLookup = multipleGradientPaint.isSimpleLookup;
            this.fastGradientArraySize = multipleGradientPaint.fastGradientArraySize;
        }
    }

    private void calculateLookupData(Color[] arrcolor) {
        int n2;
        int n3;
        int n4;
        Color[] arrcolor2;
        if (this.colorSpace == MultipleGradientPaint.ColorSpaceType.LINEAR_RGB) {
            arrcolor2 = new Color[arrcolor.length];
            for (n4 = 0; n4 < arrcolor.length; ++n4) {
                n3 = arrcolor[n4].getRGB();
                n2 = n3 >>> 24;
                int n5 = SRGBtoLinearRGB[n3 >> 16 & 0xFF];
                int n6 = SRGBtoLinearRGB[n3 >> 8 & 0xFF];
                int n7 = SRGBtoLinearRGB[n3 & 0xFF];
                arrcolor2[n4] = new Color(n5, n6, n7, n2);
            }
        } else {
            arrcolor2 = arrcolor;
        }
        this.normalizedIntervals = new float[this.fractions.length - 1];
        for (n4 = 0; n4 < this.normalizedIntervals.length; ++n4) {
            this.normalizedIntervals[n4] = this.fractions[n4 + 1] - this.fractions[n4];
        }
        this.transparencyTest = -16777216;
        this.gradients = new int[this.normalizedIntervals.length][];
        float f2 = 1.0f;
        for (n3 = 0; n3 < this.normalizedIntervals.length; ++n3) {
            f2 = f2 > this.normalizedIntervals[n3] ? this.normalizedIntervals[n3] : f2;
        }
        n3 = 0;
        for (n2 = 0; n2 < this.normalizedIntervals.length; ++n2) {
            n3 = (int)((float)n3 + this.normalizedIntervals[n2] / f2 * 256.0f);
        }
        if (n3 > 5000) {
            this.calculateMultipleArrayGradient(arrcolor2);
        } else {
            this.calculateSingleArrayGradient(arrcolor2, f2);
        }
        this.model = this.transparencyTest >>> 24 == 255 ? xrgbmodel : ColorModel.getRGBdefault();
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
            int n5 = arrcolor[n3].getRGB();
            int n6 = arrcolor[n3 + 1].getRGB();
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
        this.gradient[this.gradient.length - 1] = arrcolor[arrcolor.length - 1].getRGB();
        if (this.colorSpace == MultipleGradientPaint.ColorSpaceType.LINEAR_RGB) {
            for (n2 = 0; n2 < this.gradient.length; ++n2) {
                this.gradient[n2] = this.convertEntireColorLinearRGBtoSRGB(this.gradient[n2]);
            }
        }
        this.fastGradientArraySize = this.gradient.length - 1;
    }

    private void calculateMultipleArrayGradient(Color[] arrcolor) {
        int n2;
        this.isSimpleLookup = false;
        for (n2 = 0; n2 < this.gradients.length; ++n2) {
            this.gradients[n2] = new int[256];
            int n3 = arrcolor[n2].getRGB();
            int n4 = arrcolor[n2 + 1].getRGB();
            this.interpolate(n3, n4, this.gradients[n2]);
            this.transparencyTest &= n3;
            this.transparencyTest &= n4;
        }
        if (this.colorSpace == MultipleGradientPaint.ColorSpaceType.LINEAR_RGB) {
            for (n2 = 0; n2 < this.gradients.length; ++n2) {
                for (int i2 = 0; i2 < this.gradients[n2].length; ++i2) {
                    this.gradients[n2][i2] = this.convertEntireColorLinearRGBtoSRGB(this.gradients[n2][i2]);
                }
            }
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

    private int convertEntireColorLinearRGBtoSRGB(int n2) {
        int n3 = n2 >> 24 & 0xFF;
        int n4 = n2 >> 16 & 0xFF;
        int n5 = n2 >> 8 & 0xFF;
        int n6 = n2 & 0xFF;
        n4 = LinearRGBtoSRGB[n4];
        n5 = LinearRGBtoSRGB[n5];
        n6 = LinearRGBtoSRGB[n6];
        return n3 << 24 | n4 << 16 | n5 << 8 | n6;
    }

    protected final int indexIntoGradientsArrays(float f2) {
        int n2;
        if (this.cycleMethod == MultipleGradientPaint.CycleMethod.NO_CYCLE) {
            if (f2 > 1.0f) {
                f2 = 1.0f;
            } else if (f2 < 0.0f) {
                f2 = 0.0f;
            }
        } else if (this.cycleMethod == MultipleGradientPaint.CycleMethod.REPEAT) {
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
        for (n2 = 0; n2 < this.gradients.length; ++n2) {
            if (!(f2 < this.fractions[n2 + 1])) continue;
            float f3 = f2 - this.fractions[n2];
            int n3 = (int)(f3 / this.normalizedIntervals[n2] * 255.0f);
            return this.gradients[n2][n3];
        }
        return this.gradients[this.gradients.length - 1][255];
    }

    private static int convertSRGBtoLinearRGB(int n2) {
        float f2 = (float)n2 / 255.0f;
        float f3 = f2 <= 0.04045f ? f2 / 12.92f : (float)Math.pow(((double)f2 + 0.055) / 1.055, 2.4);
        return Math.round(f3 * 255.0f);
    }

    private static int convertLinearRGBtoSRGB(int n2) {
        float f2 = (float)n2 / 255.0f;
        float f3 = (double)f2 <= 0.0031308 ? f2 * 12.92f : 1.055f * (float)Math.pow(f2, 0.4166666666666667) - 0.055f;
        return Math.round(f3 * 255.0f);
    }

    @Override
    public final Raster getRaster(int n2, int n3, int n4, int n5) {
        Raster raster = this.saved;
        if (raster == null || raster.getWidth() < n4 || raster.getHeight() < n5) {
            this.saved = raster = MultipleGradientPaintContext.getCachedRaster(this.model, n4, n5);
        }
        DataBufferInt dataBufferInt = (DataBufferInt)raster.getDataBuffer();
        int[] arrn = dataBufferInt.getData(0);
        int n6 = dataBufferInt.getOffset();
        int n7 = ((SinglePixelPackedSampleModel)raster.getSampleModel()).getScanlineStride();
        int n8 = n7 - n4;
        this.fillRaster(arrn, n6, n8, n2, n3, n4, n5);
        return raster;
    }

    protected abstract void fillRaster(int[] var1, int var2, int var3, int var4, int var5, int var6, int var7);

    private static synchronized Raster getCachedRaster(ColorModel colorModel, int n2, int n3) {
        Raster raster;
        if (colorModel == cachedModel && cached != null && (raster = (Raster)cached.get()) != null && raster.getWidth() >= n2 && raster.getHeight() >= n3) {
            cached = null;
            return raster;
        }
        return colorModel.createCompatibleWritableRaster(n2, n3);
    }

    private static synchronized void putCachedRaster(ColorModel colorModel, Raster raster) {
        Raster raster2;
        if (cached != null && (raster2 = (Raster)cached.get()) != null) {
            int n2 = raster2.getWidth();
            int n3 = raster2.getHeight();
            int n4 = raster.getWidth();
            int n5 = raster.getHeight();
            if (n2 >= n4 && n3 >= n5) {
                return;
            }
            if (n2 * n3 >= n4 * n5) {
                return;
            }
        }
        cachedModel = colorModel;
        cached = new WeakReference<Raster>(raster);
    }

    @Override
    public final void dispose() {
        if (this.saved != null) {
            MultipleGradientPaintContext.putCachedRaster(this.model, this.saved);
            this.saved = null;
        }
    }

    @Override
    public final ColorModel getColorModel() {
        return this.model;
    }

    static {
        SRGBtoLinearRGB = new int[256];
        LinearRGBtoSRGB = new int[256];
        for (int i2 = 0; i2 < 256; ++i2) {
            MultipleGradientPaintContext.SRGBtoLinearRGB[i2] = MultipleGradientPaintContext.convertSRGBtoLinearRGB(i2);
            MultipleGradientPaintContext.LinearRGBtoSRGB[i2] = MultipleGradientPaintContext.convertLinearRGBtoSRGB(i2);
        }
    }
}

