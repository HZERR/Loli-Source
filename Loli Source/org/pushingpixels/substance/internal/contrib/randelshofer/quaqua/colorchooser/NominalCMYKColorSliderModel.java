/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser;

import javax.swing.DefaultBoundedRangeModel;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.ColorSliderModel;

public class NominalCMYKColorSliderModel
extends ColorSliderModel {
    public NominalCMYKColorSliderModel() {
        super(new DefaultBoundedRangeModel[]{new DefaultBoundedRangeModel(0, 0, 0, 100), new DefaultBoundedRangeModel(0, 0, 0, 100), new DefaultBoundedRangeModel(0, 0, 0, 100), new DefaultBoundedRangeModel(0, 0, 0, 100)});
    }

    @Override
    public int getRGB() {
        float cyan = (float)this.components[0].getValue() / 100.0f;
        float magenta = (float)this.components[1].getValue() / 100.0f;
        float yellow = (float)this.components[2].getValue() / 100.0f;
        float black = (float)this.components[3].getValue() / 100.0f;
        float red = 1.0f - cyan * (1.0f - black) - black;
        float green = 1.0f - magenta * (1.0f - black) - black;
        float blue = 1.0f - yellow * (1.0f - black) - black;
        return 0xFF000000 | (int)(red * 255.0f) << 16 | (int)(green * 255.0f) << 8 | (int)(blue * 255.0f);
    }

    @Override
    public void setRGB(int rgb) {
        float black;
        float cyan = 1.0f - (float)((rgb & 0xFF0000) >>> 16) / 255.0f;
        float magenta = 1.0f - (float)((rgb & 0xFF00) >>> 8) / 255.0f;
        float yellow = 1.0f - (float)(rgb & 0xFF) / 255.0f;
        if (Math.min(Math.min(cyan, magenta), yellow) >= 1.0f) {
            yellow = 0.0f;
            magenta = 0.0f;
            cyan = 0.0f;
            black = 1.0f;
        } else {
            black = Math.min(Math.min(cyan, magenta), yellow);
            if (black > 0.0f) {
                cyan = (cyan - black) / (1.0f - black);
                magenta = (magenta - black) / (1.0f - black);
                yellow = (yellow - black) / (1.0f - black);
            }
        }
        this.components[0].setValue((int)(cyan * 100.0f));
        this.components[1].setValue((int)(magenta * 100.0f));
        this.components[2].setValue((int)(yellow * 100.0f));
        this.components[3].setValue((int)(black * 100.0f));
    }

    @Override
    public int toRGB(int[] values) {
        float cyan = (float)values[0] / 100.0f;
        float magenta = (float)values[1] / 100.0f;
        float yellow = (float)values[2] / 100.0f;
        float black = (float)values[3] / 100.0f;
        float red = 1.0f - cyan * (1.0f - black) - black;
        float green = 1.0f - magenta * (1.0f - black) - black;
        float blue = 1.0f - yellow * (1.0f - black) - black;
        return 0xFF000000 | (int)(red * 255.0f) << 16 | (int)(green * 255.0f) << 8 | (int)(blue * 255.0f);
    }
}

