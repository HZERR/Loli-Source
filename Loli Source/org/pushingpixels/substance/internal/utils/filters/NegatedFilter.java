/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils.filters;

import java.awt.image.BufferedImage;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;
import org.pushingpixels.substance.internal.utils.filters.AbstractFilter;

public class NegatedFilter
extends AbstractFilter {
    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        if (dst == null) {
            dst = this.createCompatibleDestImage(src, null);
        }
        int width = src.getWidth();
        int height = src.getHeight();
        int[] pixels = new int[width * height];
        this.getPixels(src, 0, 0, width, height, pixels);
        this.negateColor(pixels);
        this.setPixels(dst, 0, 0, width, height, pixels);
        return dst;
    }

    private void negateColor(int[] pixels) {
        for (int i2 = 0; i2 < pixels.length; ++i2) {
            int argb = pixels[i2];
            pixels[i2] = SubstanceColorUtilities.getNegativeColor(argb);
        }
    }
}

