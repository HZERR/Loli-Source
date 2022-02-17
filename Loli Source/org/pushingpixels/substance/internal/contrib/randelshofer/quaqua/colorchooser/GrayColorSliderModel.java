/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser;

import javax.swing.DefaultBoundedRangeModel;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.ColorSliderModel;

public class GrayColorSliderModel
extends ColorSliderModel {
    public GrayColorSliderModel() {
        super(new DefaultBoundedRangeModel[]{new DefaultBoundedRangeModel(0, 0, 0, 100)});
    }

    @Override
    public int getRGB() {
        int br = (int)((float)this.components[0].getValue() * 2.55f);
        return 0xFF000000 | br << 16 | br << 8 | br;
    }

    @Override
    public void setRGB(int rgb) {
        this.components[0].setValue((int)((float)(((rgb & 0xFF0000) >> 16) + ((rgb & 0xFF00) >> 8) + (rgb & 0xFF)) / 3.0f / 2.55f));
    }

    @Override
    public int toRGB(int[] values) {
        int br = (int)((float)values[0] * 2.55f);
        return 0xFF000000 | br << 16 | br << 8 | br;
    }
}

