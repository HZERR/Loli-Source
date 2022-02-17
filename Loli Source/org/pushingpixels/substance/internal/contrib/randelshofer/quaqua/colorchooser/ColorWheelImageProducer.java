/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser;

import java.awt.Color;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;

public class ColorWheelImageProducer
extends MemoryImageSource {
    private int[] pixels;
    private int w;
    private int h;
    private float brightness = 1.0f;
    private boolean isDirty = true;
    private float[] hues;
    private float[] saturations;
    private int[] alphas;

    public ColorWheelImageProducer(int w2, int h2) {
        super(w2, h2, null, 0, w2);
        this.pixels = new int[w2 * h2];
        this.w = w2;
        this.h = h2;
        this.generateLookupTables();
        this.newPixels(this.pixels, ColorModel.getRGBdefault(), 0, w2);
        this.setAnimated(true);
        this.generateColorWheel();
    }

    public int getRadius() {
        return Math.min(this.w, this.h) / 2 - 2;
    }

    private void generateLookupTables() {
        this.saturations = new float[this.w * this.h];
        this.hues = new float[this.w * this.h];
        this.alphas = new int[this.w * this.h];
        float radius = this.getRadius();
        float blend = (radius + 2.0f) / radius - 1.0f;
        int cx = this.w / 2;
        int cy = this.h / 2;
        for (int x2 = 0; x2 < this.w; ++x2) {
            int kx = x2 - cx;
            int squarekx = kx * kx;
            for (int y2 = 0; y2 < this.h; ++y2) {
                int ky = cy - y2;
                int index = x2 + y2 * this.w;
                this.saturations[index] = (float)Math.sqrt(squarekx + ky * ky) / radius;
                if (this.saturations[index] <= 1.0f) {
                    this.alphas[index] = -16777216;
                } else {
                    this.alphas[index] = (int)((blend - Math.min(blend, this.saturations[index] - 1.0f)) * 255.0f / blend) << 24;
                    this.saturations[index] = 1.0f;
                }
                if (this.alphas[index] == 0) continue;
                this.hues[index] = (float)(Math.atan2(ky, kx) / Math.PI / 2.0);
            }
        }
    }

    public void setBrightness(float newValue) {
        this.isDirty = this.isDirty || this.brightness != newValue;
        this.brightness = newValue;
    }

    public boolean needsGeneration() {
        return this.isDirty;
    }

    public void regenerateColorWheel() {
        if (this.isDirty) {
            this.generateColorWheel();
        }
    }

    public void generateColorWheel() {
        float radius = Math.min(this.w, this.h);
        for (int index = 0; index < this.pixels.length; ++index) {
            if (this.alphas[index] == 0) continue;
            this.pixels[index] = this.alphas[index] | 0xFFFFFF & Color.HSBtoRGB(this.hues[index], this.saturations[index], this.brightness);
        }
        this.newPixels();
        this.isDirty = false;
    }
}

