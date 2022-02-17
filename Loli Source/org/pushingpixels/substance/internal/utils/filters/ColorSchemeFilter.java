/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils.filters;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.internal.utils.HashMapKey;
import org.pushingpixels.substance.internal.utils.LazyResettableHashMap;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.filters.AbstractFilter;

public class ColorSchemeFilter
extends AbstractFilter {
    private int[] interpolated;
    public static final int MAPSTEPS = 512;
    protected static final LazyResettableHashMap<ColorSchemeFilter> filters = new LazyResettableHashMap("ColorSchemeFilter");
    protected float originalBrightnessFactor;

    public static ColorSchemeFilter getColorSchemeFilter(SubstanceColorScheme scheme, float originalBrightnessFactor) {
        HashMapKey key = SubstanceCoreUtilities.getHashKey(scheme.getDisplayName(), Float.valueOf(originalBrightnessFactor));
        ColorSchemeFilter filter = filters.get(key);
        if (filter == null) {
            filter = new ColorSchemeFilter(scheme, originalBrightnessFactor);
            filters.put(key, filter);
        }
        return filter;
    }

    private ColorSchemeFilter(SubstanceColorScheme scheme, float originalBrightnessFactor) {
        if (scheme == null) {
            throw new IllegalArgumentException("mixColor cannot be null");
        }
        this.originalBrightnessFactor = originalBrightnessFactor;
        TreeMap<Integer, Color> schemeColorMapping = new TreeMap<Integer, Color>();
        schemeColorMapping.put(SubstanceColorUtilities.getColorBrightness(scheme.getUltraLightColor().getRGB()), scheme.getUltraLightColor());
        schemeColorMapping.put(SubstanceColorUtilities.getColorBrightness(scheme.getExtraLightColor().getRGB()), scheme.getExtraLightColor());
        schemeColorMapping.put(SubstanceColorUtilities.getColorBrightness(scheme.getLightColor().getRGB()), scheme.getLightColor());
        schemeColorMapping.put(SubstanceColorUtilities.getColorBrightness(scheme.getMidColor().getRGB()), scheme.getMidColor());
        schemeColorMapping.put(SubstanceColorUtilities.getColorBrightness(scheme.getDarkColor().getRGB()), scheme.getDarkColor());
        schemeColorMapping.put(SubstanceColorUtilities.getColorBrightness(scheme.getUltraDarkColor().getRGB()), scheme.getUltraDarkColor());
        ArrayList schemeBrightness = new ArrayList();
        schemeBrightness.addAll(schemeColorMapping.keySet());
        Collections.sort(schemeBrightness);
        int lowestSchemeBrightness = (Integer)schemeBrightness.get(0);
        int highestSchemeBrightness = (Integer)schemeBrightness.get(schemeBrightness.size() - 1);
        boolean hasSameBrightness = highestSchemeBrightness == lowestSchemeBrightness;
        TreeMap stretchedColorMapping = new TreeMap();
        for (Map.Entry entry : schemeColorMapping.entrySet()) {
            int brightness = (Integer)entry.getKey();
            int stretched = hasSameBrightness ? brightness : 255 - 255 * (highestSchemeBrightness - brightness) / (highestSchemeBrightness - lowestSchemeBrightness);
            stretchedColorMapping.put(stretched, entry.getValue());
        }
        schemeBrightness = new ArrayList();
        schemeBrightness.addAll(stretchedColorMapping.keySet());
        Collections.sort(schemeBrightness);
        this.interpolated = new int[512];
        block1: for (int i2 = 0; i2 < 512; ++i2) {
            int brightness = (int)(256.0 * (double)i2 / 512.0);
            if (schemeBrightness.contains(brightness)) {
                this.interpolated[i2] = ((Color)stretchedColorMapping.get(brightness)).getRGB();
                continue;
            }
            if (hasSameBrightness) {
                this.interpolated[i2] = ((Color)stretchedColorMapping.get(lowestSchemeBrightness)).getRGB();
                continue;
            }
            int currIndex = 0;
            while (true) {
                int currStopValue = (Integer)schemeBrightness.get(currIndex);
                int nextStopValue = (Integer)schemeBrightness.get(currIndex + 1);
                if (brightness > currStopValue && brightness < nextStopValue) {
                    Color currStopColor = (Color)stretchedColorMapping.get(currStopValue);
                    Color nextStopColor = (Color)stretchedColorMapping.get(nextStopValue);
                    this.interpolated[i2] = SubstanceColorUtilities.getInterpolatedRGB(currStopColor, nextStopColor, 1.0 - (double)(brightness - currStopValue) / (double)(nextStopValue - currStopValue));
                    continue block1;
                }
                ++currIndex;
            }
        }
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
        this.mixColor(pixels);
        this.setPixels(dst, 0, 0, width, height, pixels);
        return dst;
    }

    private void mixColor(int[] pixels) {
        for (int i2 = 0; i2 < pixels.length; ++i2) {
            int argb = pixels[i2];
            int brightness = SubstanceColorUtilities.getColorBrightness(argb);
            int r2 = argb >>> 16 & 0xFF;
            int g2 = argb >>> 8 & 0xFF;
            int b2 = argb >>> 0 & 0xFF;
            float[] hsb = Color.RGBtoHSB(r2, g2, b2, null);
            int pixelColor = this.interpolated[brightness * 512 / 256];
            int ri = pixelColor >>> 16 & 0xFF;
            int gi = pixelColor >>> 8 & 0xFF;
            int bi = pixelColor >>> 0 & 0xFF;
            float[] hsbi = Color.RGBtoHSB(ri, gi, bi, null);
            hsb[0] = hsbi[0];
            hsb[1] = hsbi[1];
            hsb[2] = this.originalBrightnessFactor * hsb[2] + (1.0f - this.originalBrightnessFactor) * hsbi[2];
            int result = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
            pixels[i2] = argb & 0xFF000000 | (result >> 16 & 0xFF) << 16 | (result >> 8 & 0xFF) << 8 | result & 0xFF;
        }
    }
}

