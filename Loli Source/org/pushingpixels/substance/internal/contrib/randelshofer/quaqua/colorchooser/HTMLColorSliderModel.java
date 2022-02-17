/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser;

import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.RGBColorSliderModel;

public class HTMLColorSliderModel
extends RGBColorSliderModel {
    private boolean isWebSaveOnly = true;

    @Override
    public int getRGB() {
        return this.getRGB(this.components[0].getValue(), this.components[1].getValue(), this.components[2].getValue());
    }

    @Override
    public int getInterpolatedRGB(int component, float value) {
        if (this.isWebSaveOnly) {
            int n2 = this.getComponentCount();
            for (int i2 = 0; i2 < n2; ++i2) {
                this.values[i2] = Math.round((float)this.components[i2].getValue() / 51.0f) * 51;
            }
            this.values[component] = Math.round(value * (float)this.components[component].getMaximum() / 51.0f) * 51;
            return this.toRGB(this.values);
        }
        return super.getInterpolatedRGB(component, value);
    }

    @Override
    protected int getRGB(int r2, int g2, int b2) {
        if (this.isWebSaveOnly) {
            return 0xFF000000 | Math.round((float)r2 / 51.0f) * 51 << 16 | Math.round((float)g2 / 51.0f) * 51 << 8 | Math.round((float)b2 / 51.0f) * 51;
        }
        return super.getRGB(r2, g2, b2);
    }

    @Override
    public void setRGB(int rgb) {
        if (this.isWebSaveOnly) {
            this.components[0].setValue(Math.round((float)(rgb & 0xFF0000) / 51.0f) * 51 >> 16);
            this.components[1].setValue(Math.round((float)(rgb & 0xFF00) / 51.0f) * 51 >> 8);
            this.components[2].setValue(Math.round((float)(rgb & 0xFF) / 51.0f) * 51);
        } else {
            super.setRGB(rgb);
        }
    }

    @Override
    public int toRGB(int[] values) {
        if (this.isWebSaveOnly) {
            return 0xFF000000 | Math.round((float)values[0] / 51.0f) * 51 << 16 | Math.round((float)values[1] / 51.0f) * 51 << 8 | Math.round((float)values[2] / 51.0f) * 51;
        }
        return super.toRGB(values);
    }

    public void setWebSaveOnly(boolean b2) {
        this.isWebSaveOnly = b2;
        if (b2) {
            this.setRGB(this.getRGB());
        }
        this.fireColorChanged(-1);
    }

    public boolean isWebSaveOnly() {
        return this.isWebSaveOnly;
    }

    public static boolean isWebSave(int rgb) {
        return (rgb & 0xFFFFFF) == (HTMLColorSliderModel.toWebSave(rgb) & 0xFFFFFF);
    }

    public static int toWebSave(int rgb) {
        return rgb & 0xFF000000 | Math.round((float)((rgb & 0xFF0000) >> 16) / 51.0f) * 51 << 16 | Math.round((float)((rgb & 0xFF00) >> 8) / 51.0f) * 51 << 8 | Math.round((float)(rgb & 0xFF) / 51.0f) * 51;
    }
}

