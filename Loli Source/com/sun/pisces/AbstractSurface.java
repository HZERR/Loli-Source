/*
 * Decompiled with CFR 0.150.
 */
package com.sun.pisces;

import com.sun.pisces.Surface;

public abstract class AbstractSurface
implements Surface {
    private long nativePtr = 0L;
    private int width;
    private int height;

    AbstractSurface(int n2, int n3) {
        if (n2 < 0) {
            throw new IllegalArgumentException("WIDTH must be positive");
        }
        if (n3 < 0) {
            throw new IllegalArgumentException("HEIGHT must be positive");
        }
        int n4 = 32 - Integer.numberOfLeadingZeros(n2) + 32 - Integer.numberOfLeadingZeros(n3);
        if (n4 > 31) {
            throw new IllegalArgumentException("WIDTH * HEIGHT is too large");
        }
        this.width = n2;
        this.height = n3;
    }

    @Override
    public final void getRGB(int[] arrn, int n2, int n3, int n4, int n5, int n6, int n7) {
        this.rgbCheck(arrn.length, n2, n3, n4, n5, n6, n7);
        this.getRGBImpl(arrn, n2, n3, n4, n5, n6, n7);
    }

    private native void getRGBImpl(int[] var1, int var2, int var3, int var4, int var5, int var6, int var7);

    @Override
    public final void setRGB(int[] arrn, int n2, int n3, int n4, int n5, int n6, int n7) {
        this.rgbCheck(arrn.length, n2, n3, n4, n5, n6, n7);
        this.setRGBImpl(arrn, n2, n3, n4, n5, n6, n7);
    }

    private native void setRGBImpl(int[] var1, int var2, int var3, int var4, int var5, int var6, int var7);

    private void rgbCheck(int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        if (n5 < 0 || n5 >= this.width) {
            throw new IllegalArgumentException("X is out of surface");
        }
        if (n6 < 0 || n6 >= this.height) {
            throw new IllegalArgumentException("Y is out of surface");
        }
        if (n7 < 0) {
            throw new IllegalArgumentException("WIDTH must be positive");
        }
        if (n8 < 0) {
            throw new IllegalArgumentException("HEIGHT must be positive");
        }
        if (n5 + n7 > this.width) {
            throw new IllegalArgumentException("X+WIDTH is out of surface");
        }
        if (n6 + n8 > this.height) {
            throw new IllegalArgumentException("Y+HEIGHT is out of surface");
        }
        if (n3 < 0) {
            throw new IllegalArgumentException("OFFSET must be positive");
        }
        if (n4 < 0) {
            throw new IllegalArgumentException("SCAN-LENGTH must be positive");
        }
        if (n4 < n7) {
            throw new IllegalArgumentException("SCAN-LENGTH must be >= WIDTH");
        }
        int n9 = 32 - Integer.numberOfLeadingZeros(n4) + 32 - Integer.numberOfLeadingZeros(n8);
        if (n9 > 31) {
            throw new IllegalArgumentException("SCAN-LENGTH * HEIGHT is too large");
        }
        if (n3 + n4 * (n8 - 1) + n7 > n2) {
            throw new IllegalArgumentException("STRIDE * HEIGHT exceeds length of data");
        }
    }

    protected void finalize() {
        this.nativeFinalize();
    }

    @Override
    public final int getWidth() {
        return this.width;
    }

    @Override
    public final int getHeight() {
        return this.height;
    }

    private native void nativeFinalize();
}

