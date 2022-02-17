/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser;

import java.awt.Color;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.DefaultBoundedRangeModel;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.ColorSliderModel;

public class ICC_CMYKColorSliderModel
extends ColorSliderModel {
    private ICC_ColorSpace colorSpace;
    float[] cmyk = new float[4];
    float[] rgb = new float[3];

    public ICC_CMYKColorSliderModel(InputStream iccProfile) throws IOException {
        super(new DefaultBoundedRangeModel[]{new DefaultBoundedRangeModel(0, 0, 0, 100), new DefaultBoundedRangeModel(0, 0, 0, 100), new DefaultBoundedRangeModel(0, 0, 0, 100), new DefaultBoundedRangeModel(0, 0, 0, 100)});
        this.colorSpace = new ICC_ColorSpace(ICC_Profile.getInstance(iccProfile));
    }

    @Override
    public int getRGB() {
        this.cmyk[0] = (float)this.components[0].getValue() / 100.0f;
        this.cmyk[1] = (float)this.components[1].getValue() / 100.0f;
        this.cmyk[2] = (float)this.components[2].getValue() / 100.0f;
        this.cmyk[3] = (float)this.components[3].getValue() / 100.0f;
        this.rgb = this.colorSpace.toRGB(this.cmyk);
        return 0xFF000000 | (int)(this.rgb[0] * 255.0f) << 16 | (int)(this.rgb[1] * 255.0f) << 8 | (int)(this.rgb[2] * 255.0f);
    }

    @Override
    public void setRGB(int newRGB) {
        this.rgb[0] = (float)((newRGB & 0xFF0000) >>> 16) / 255.0f;
        this.rgb[1] = (float)((newRGB & 0xFF00) >>> 8) / 255.0f;
        this.rgb[2] = (float)(newRGB & 0xFF) / 255.0f;
        this.cmyk = this.colorSpace.fromRGB(this.rgb);
        System.out.print("rgb in:" + new Color(newRGB));
        this.components[0].setValue((int)(this.cmyk[0] * 100.0f));
        this.components[1].setValue((int)(this.cmyk[1] * 100.0f));
        this.components[2].setValue((int)(this.cmyk[2] * 100.0f));
        this.components[3].setValue((int)(this.cmyk[3] * 100.0f));
        this.rgb = this.colorSpace.toRGB(this.cmyk);
        System.out.println(" out:" + new Color((int)(this.rgb[0] * 255.0f), (int)(this.rgb[1] * 255.0f), (int)(this.rgb[2] * 255.0f)));
    }

    @Override
    public int toRGB(int[] values) {
        this.cmyk[0] = (float)values[0] / 100.0f;
        this.cmyk[1] = (float)values[1] / 100.0f;
        this.cmyk[2] = (float)values[2] / 100.0f;
        this.cmyk[3] = (float)values[3] / 100.0f;
        this.rgb = this.colorSpace.toRGB(this.cmyk);
        return 0xFF000000 | (int)(this.rgb[0] * 255.0f) << 16 | (int)(this.rgb[1] * 255.0f) << 8 | (int)(this.rgb[2] * 255.0f);
    }
}

