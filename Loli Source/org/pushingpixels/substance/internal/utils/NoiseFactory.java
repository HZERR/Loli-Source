/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.DataBufferInt;
import java.awt.image.Kernel;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.internal.utils.PerlinNoiseGenerator;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;

public class NoiseFactory {
    public static BufferedImage getNoiseImage(SubstanceSkin skin, int width, int height, double xFactor, double yFactor, boolean hasConstantZ, boolean toBlur, boolean isPreview) {
        SubstanceColorScheme scheme = skin.getWatermarkColorScheme();
        Color c1 = scheme.getWatermarkDarkColor();
        Color c3 = scheme.getWatermarkLightColor();
        BufferedImage dst = SubstanceCoreUtilities.getBlankImage(width, height);
        int[] dstBuffer = ((DataBufferInt)dst.getRaster().getDataBuffer()).getData();
        double m2 = xFactor * (double)width * xFactor * (double)width + yFactor * (double)height * yFactor * (double)height;
        int pos = 0;
        for (int j2 = 0; j2 < height; ++j2) {
            double jj = yFactor * (double)j2;
            for (int i2 = 0; i2 < width; ++i2) {
                double ii = xFactor * (double)i2;
                double z = hasConstantZ ? 1.0 : Math.sqrt(m2 - ii * ii - jj * jj);
                double noise = 0.5 + 0.5 * PerlinNoiseGenerator.noise(ii, jj, z);
                double likeness = Math.max(0.0, Math.min(1.0, 2.0 * noise));
                dstBuffer[pos++] = SubstanceColorUtilities.getInterpolatedRGB(c3, c1, likeness);
            }
        }
        if (toBlur) {
            ConvolveOp convolve = new ConvolveOp(new Kernel(3, 3, new float[]{0.08f, 0.08f, 0.08f, 0.08f, 0.38f, 0.08f, 0.08f, 0.08f, 0.08f}), 1, null);
            dst = convolve.filter(dst, null);
        }
        return dst;
    }
}

