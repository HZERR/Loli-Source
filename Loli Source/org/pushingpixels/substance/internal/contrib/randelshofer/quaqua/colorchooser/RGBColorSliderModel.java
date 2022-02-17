/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser;

import javax.swing.DefaultBoundedRangeModel;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.ColorSliderModel;

public class RGBColorSliderModel
extends ColorSliderModel {
    public RGBColorSliderModel() {
        super(new DefaultBoundedRangeModel[]{new DefaultBoundedRangeModel(255, 0, 0, 255), new DefaultBoundedRangeModel(255, 0, 0, 255), new DefaultBoundedRangeModel(255, 0, 0, 255)});
    }

    @Override
    public int getRGB() {
        return this.getRGB(this.components[0].getValue(), this.components[1].getValue(), this.components[2].getValue());
    }

    protected int getRGB(int r2, int g2, int b2) {
        return 0xFF000000 | r2 << 16 | g2 << 8 | b2;
    }

    @Override
    public void setRGB(int rgb) {
        this.components[0].setValue((rgb & 0xFF0000) >> 16);
        this.components[1].setValue((rgb & 0xFF00) >> 8);
        this.components[2].setValue(rgb & 0xFF);
    }

    @Override
    public int toRGB(int[] values) {
        return 0xFF000000 | values[0] << 16 | values[1] << 8 | values[2];
    }
}

