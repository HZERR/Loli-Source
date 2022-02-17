/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser;

import java.awt.Color;
import javax.swing.DefaultBoundedRangeModel;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.ColorSliderModel;

public class HSBColorSliderModel
extends ColorSliderModel {
    public HSBColorSliderModel() {
        super(new DefaultBoundedRangeModel[]{new DefaultBoundedRangeModel(0, 0, 0, 359), new DefaultBoundedRangeModel(0, 0, 0, 100), new DefaultBoundedRangeModel(0, 0, 0, 100)});
    }

    @Override
    public int getRGB() {
        return Color.HSBtoRGB((float)this.components[0].getValue() / 360.0f, (float)this.components[1].getValue() / 100.0f, (float)this.components[2].getValue() / 100.0f);
    }

    @Override
    public void setRGB(int rgb) {
        float[] hsb = Color.RGBtoHSB((rgb & 0xFF0000) >>> 16, (rgb & 0xFF00) >>> 8, rgb & 0xFF, new float[3]);
        this.components[0].setValue((int)(hsb[0] * 360.0f));
        this.components[1].setValue((int)(hsb[1] * 100.0f));
        this.components[2].setValue((int)(hsb[2] * 100.0f));
    }

    @Override
    public int toRGB(int[] values) {
        return Color.HSBtoRGB((float)values[0] / 360.0f, (float)values[1] / 100.0f, (float)values[2] / 100.0f);
    }
}

