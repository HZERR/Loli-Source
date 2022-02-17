/*
 * Decompiled with CFR 0.150.
 */
package com.sun.pisces;

import com.sun.pisces.AbstractSurface;
import com.sun.pisces.GradientColorMap;
import com.sun.pisces.Transform6;

public final class PiscesRenderer {
    public static final int ARC_OPEN = 0;
    public static final int ARC_CHORD = 1;
    public static final int ARC_PIE = 2;
    private long nativePtr = 0L;
    private AbstractSurface surface;

    public PiscesRenderer(AbstractSurface abstractSurface) {
        this.surface = abstractSurface;
        this.initialize();
    }

    private native void initialize();

    public void setColor(int n2, int n3, int n4, int n5) {
        this.checkColorRange(n2, "RED");
        this.checkColorRange(n3, "GREEN");
        this.checkColorRange(n4, "BLUE");
        this.checkColorRange(n5, "ALPHA");
        this.setColorImpl(n2, n3, n4, n5);
    }

    private native void setColorImpl(int var1, int var2, int var3, int var4);

    private void checkColorRange(int n2, String string) {
        if (n2 < 0 || n2 > 255) {
            throw new IllegalArgumentException(string + " color component is out of range");
        }
    }

    public void setColor(int n2, int n3, int n4) {
        this.setColor(n2, n3, n4, 255);
    }

    public void setCompositeRule(int n2) {
        if (n2 != 0 && n2 != 1 && n2 != 2) {
            throw new IllegalArgumentException("Invalid value for Composite-Rule");
        }
        this.setCompositeRuleImpl(n2);
    }

    private native void setCompositeRuleImpl(int var1);

    private native void setLinearGradientImpl(int var1, int var2, int var3, int var4, int[] var5, int var6, Transform6 var7);

    public void setLinearGradient(int n2, int n3, int n4, int n5, int[] arrn, int[] arrn2, int n6, Transform6 transform6) {
        GradientColorMap gradientColorMap = new GradientColorMap(arrn, arrn2, n6);
        this.setLinearGradientImpl(n2, n3, n4, n5, gradientColorMap.colors, n6, transform6 == null ? new Transform6(65536, 0, 0, 65536, 0, 0) : transform6);
    }

    public void setLinearGradient(int n2, int n3, int n4, int n5, GradientColorMap gradientColorMap, Transform6 transform6) {
        this.setLinearGradientImpl(n2, n3, n4, n5, gradientColorMap.colors, gradientColorMap.cycleMethod, transform6 == null ? new Transform6(65536, 0, 0, 65536, 0, 0) : transform6);
    }

    public void setLinearGradient(int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        int[] arrn = new int[]{0, 65536};
        int[] arrn2 = new int[]{n4, n7};
        Transform6 transform6 = new Transform6(65536, 0, 0, 65536, 0, 0);
        this.setLinearGradient(n2, n3, n5, n6, arrn, arrn2, n8, transform6);
    }

    private native void setRadialGradientImpl(int var1, int var2, int var3, int var4, int var5, int[] var6, int var7, Transform6 var8);

    public void setRadialGradient(int n2, int n3, int n4, int n5, int n6, int[] arrn, int[] arrn2, int n7, Transform6 transform6) {
        GradientColorMap gradientColorMap = new GradientColorMap(arrn, arrn2, n7);
        this.setRadialGradientImpl(n2, n3, n4, n5, n6, gradientColorMap.colors, n7, transform6 == null ? new Transform6(65536, 0, 0, 65536, 0, 0) : transform6);
    }

    public void setRadialGradient(int n2, int n3, int n4, int n5, int n6, GradientColorMap gradientColorMap, Transform6 transform6) {
        this.setRadialGradientImpl(n2, n3, n4, n5, n6, gradientColorMap.colors, gradientColorMap.cycleMethod, transform6 == null ? new Transform6(65536, 0, 0, 65536, 0, 0) : transform6);
    }

    public void setTexture(int n2, int[] arrn, int n3, int n4, int n5, Transform6 transform6, boolean bl, boolean bl2) {
        this.inputImageCheck(n3, n4, 0, n5, arrn.length);
        this.setTextureImpl(n2, arrn, n3, n4, n5, transform6, bl, bl2);
    }

    private native void setTextureImpl(int var1, int[] var2, int var3, int var4, int var5, Transform6 var6, boolean var7, boolean var8);

    public void setClip(int n2, int n3, int n4, int n5) {
        int n6 = Math.max(n2, 0);
        int n7 = Math.max(n3, 0);
        int n8 = Math.min(n2 + n4, this.surface.getWidth());
        int n9 = Math.min(n3 + n5, this.surface.getHeight());
        this.setClipImpl(n6, n7, n8 - n6, n9 - n7);
    }

    private native void setClipImpl(int var1, int var2, int var3, int var4);

    public void resetClip() {
        this.setClipImpl(0, 0, this.surface.getWidth(), this.surface.getHeight());
    }

    public void clearRect(int n2, int n3, int n4, int n5) {
        int n6 = Math.max(n2, 0);
        int n7 = Math.max(n3, 0);
        int n8 = Math.min(n2 + n4, this.surface.getWidth());
        int n9 = Math.min(n3 + n5, this.surface.getHeight());
        this.clearRectImpl(n6, n7, n8 - n6, n9 - n7);
    }

    private native void clearRectImpl(int var1, int var2, int var3, int var4);

    public void fillRect(int n2, int n3, int n4, int n5) {
        int n6 = Math.max(n2, 0);
        int n7 = Math.max(n3, 0);
        int n8 = Math.min(n2 + n4, this.surface.getWidth() << 16);
        int n9 = Math.min(n3 + n5, this.surface.getHeight() << 16);
        int n10 = n8 - n6;
        int n11 = n9 - n7;
        if (n10 > 0 && n11 > 0) {
            this.fillRectImpl(n6, n7, n10, n11);
        }
    }

    private native void fillRectImpl(int var1, int var2, int var3, int var4);

    public void emitAndClearAlphaRow(byte[] arrby, int[] arrn, int n2, int n3, int n4, int n5) {
        if (n4 - n3 > arrn.length) {
            throw new IllegalArgumentException("rendering range exceeds length of data");
        }
        this.emitAndClearAlphaRowImpl(arrby, arrn, n2, n3, n4, n5);
    }

    private native void emitAndClearAlphaRowImpl(byte[] var1, int[] var2, int var3, int var4, int var5, int var6);

    public void fillAlphaMask(byte[] arrby, int n2, int n3, int n4, int n5, int n6, int n7) {
        if (arrby == null) {
            throw new NullPointerException("Mask is NULL");
        }
        this.inputImageCheck(n4, n5, n6, n7, arrby.length);
        this.fillAlphaMaskImpl(arrby, n2, n3, n4, n5, n6, n7);
    }

    private native void fillAlphaMaskImpl(byte[] var1, int var2, int var3, int var4, int var5, int var6, int var7);

    public void setLCDGammaCorrection(float f2) {
        if (f2 <= 0.0f) {
            throw new IllegalArgumentException("Gamma must be greater than zero");
        }
        this.setLCDGammaCorrectionImpl(f2);
    }

    private native void setLCDGammaCorrectionImpl(float var1);

    public void fillLCDAlphaMask(byte[] arrby, int n2, int n3, int n4, int n5, int n6, int n7) {
        if (arrby == null) {
            throw new NullPointerException("Mask is NULL");
        }
        this.inputImageCheck(n4, n5, n6, n7, arrby.length);
        this.fillLCDAlphaMaskImpl(arrby, n2, n3, n4, n5, n6, n7);
    }

    private native void fillLCDAlphaMaskImpl(byte[] var1, int var2, int var3, int var4, int var5, int var6, int var7);

    public void drawImage(int n2, int n3, int[] arrn, int n4, int n5, int n6, int n7, Transform6 transform6, boolean bl, int n8, int n9, int n10, int n11, int n12, int n13, int n14, int n15, int n16, int n17, int n18, int n19, boolean bl2) {
        this.inputImageCheck(n4, n5, n6, n7, arrn.length);
        this.drawImageImpl(n2, n3, arrn, n4, n5, n6, n7, transform6, bl, n8, n9, n10, n11, n12, n13, n14, n15, n16, n17, n18, n19, bl2);
    }

    private native void drawImageImpl(int var1, int var2, int[] var3, int var4, int var5, int var6, int var7, Transform6 var8, boolean var9, int var10, int var11, int var12, int var13, int var14, int var15, int var16, int var17, int var18, int var19, int var20, int var21, boolean var22);

    private void inputImageCheck(int n2, int n3, int n4, int n5, int n6) {
        if (n2 < 0) {
            throw new IllegalArgumentException("WIDTH must be positive");
        }
        if (n3 < 0) {
            throw new IllegalArgumentException("HEIGHT must be positive");
        }
        if (n4 < 0) {
            throw new IllegalArgumentException("OFFSET must be positive");
        }
        if (n5 < 0) {
            throw new IllegalArgumentException("STRIDE must be positive");
        }
        if (n5 < n2) {
            throw new IllegalArgumentException("STRIDE must be >= WIDTH");
        }
        int n7 = 32 - Integer.numberOfLeadingZeros(n5) + 32 - Integer.numberOfLeadingZeros(n3);
        if (n7 > 31) {
            throw new IllegalArgumentException("STRIDE * HEIGHT is too large");
        }
        if (n4 + n5 * (n3 - 1) + n2 > n6) {
            throw new IllegalArgumentException("STRIDE * HEIGHT exceeds length of data");
        }
    }

    protected void finalize() {
        this.nativeFinalize();
    }

    private native void nativeFinalize();
}

