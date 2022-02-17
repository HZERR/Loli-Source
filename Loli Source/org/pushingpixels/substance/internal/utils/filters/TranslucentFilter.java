/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils.filters;

import java.awt.image.BufferedImage;
import org.pushingpixels.substance.internal.utils.filters.AbstractFilter;

public class TranslucentFilter
extends AbstractFilter {
    private double alpha;

    public TranslucentFilter(double alpha) {
        this.alpha = alpha;
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        if (dst == null) {
            dst = this.createCompatibleDestImage(src, null);
        }
        int width = src.getWidth();
        int height = src.getHeight();
        int[] pixels = new int[width * height];
        this.getPixels(src, 0, 0, width, height, pixels);
        this.translucentColor(pixels);
        this.setPixels(dst, 0, 0, width, height, pixels);
        return dst;
    }

    private void translucentColor(int[] pixels) {
        for (int i2 = 0; i2 < pixels.length; ++i2) {
            int argb = pixels[i2];
            int transp = (int)(this.alpha * (double)(argb >>> 24 & 0xFF));
            int r2 = argb >>> 16 & 0xFF;
            int g2 = argb >>> 8 & 0xFF;
            int b2 = argb >>> 0 & 0xFF;
            pixels[i2] = transp << 24 | r2 << 16 | g2 << 8 | b2;
        }
    }
}

