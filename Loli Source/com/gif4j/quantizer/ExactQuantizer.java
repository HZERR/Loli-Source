/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j.quantizer;

import com.gif4j.quantizer.c;
import com.gif4j.quantizer.q;
import java.awt.image.BufferedImage;

public final class ExactQuantizer {
    private static int a = 4;
    public static final int MEMORY_LOW_FAST = 0;
    public static final int MEMORY_NORMAL_FAST = 4;
    public static final int MEMORY_LOW_OPTIMIZED = 2;
    public static final int MEMORY_NORMAL_OPTIMIZED = 6;
    public static final int MEMORY_LOW_OPTIMIZED_DITHER = 3;
    public static final int MEMORY_NORMAL_OPTIMIZED_DITHER = 7;
    public static final int MEMORY_NORMAL_OPTIMIZED_DITHER_SOFT = 8;
    public static final int MEMORY_LOW_FAST_DITHER = 1;
    public static final int MEMORY_NORMAL_FAST_DITHER = 5;

    public static BufferedImage quantize(BufferedImage bufferedImage, int n2) {
        return ExactQuantizer.quantize(a, bufferedImage, n2, false);
    }

    public static BufferedImage quantize(BufferedImage bufferedImage, int n2, boolean bl) {
        return ExactQuantizer.quantize(a, bufferedImage, n2, bl);
    }

    public static BufferedImage quantize(int n2, BufferedImage bufferedImage, int n3) {
        return ExactQuantizer.quantize(n2, bufferedImage, n3, false);
    }

    public static BufferedImage quantize(int n2, BufferedImage bufferedImage, int n3, boolean bl) {
        if (bufferedImage == null) {
            throw new NullPointerException("source image is null");
        }
        if (n3 < 4 || n3 > 65536) {
            throw new IllegalArgumentException("number of colors must be between 4 and 65536.");
        }
        if (bufferedImage.getColorModel().hasAlpha() || bl) {
            return c.a(bufferedImage, n3, false, false, true);
        }
        return q.a(bufferedImage, n3, false, false, true);
    }

    public static void setDefaultMode(int n2) {
        if (n2 >= 0 && n2 <= 8) {
            a = n2;
        }
    }
}

