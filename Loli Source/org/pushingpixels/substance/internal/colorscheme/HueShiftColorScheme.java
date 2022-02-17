/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.colorscheme.BaseColorScheme;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;

public class HueShiftColorScheme
extends BaseColorScheme {
    private double hueShiftFactor;
    private Color mainUltraLightColor;
    private Color mainExtraLightColor;
    private Color mainLightColor;
    private Color mainMidColor;
    private Color mainDarkColor;
    private Color mainUltraDarkColor;
    private Color foregroundColor;
    private SubstanceColorScheme origScheme;

    public HueShiftColorScheme(SubstanceColorScheme origScheme, double hueShiftFactor) {
        super("Hue-shift " + origScheme.getDisplayName() + " " + (int)(100.0 * hueShiftFactor) + "%", HueShiftColorScheme.getResolver(origScheme));
        this.hueShiftFactor = hueShiftFactor;
        this.origScheme = origScheme;
        this.foregroundColor = SubstanceColorUtilities.getHueShiftedColor(origScheme.getForegroundColor(), this.hueShiftFactor / 2.0);
        this.mainUltraDarkColor = SubstanceColorUtilities.getHueShiftedColor(origScheme.getUltraDarkColor(), this.hueShiftFactor);
        this.mainDarkColor = SubstanceColorUtilities.getHueShiftedColor(origScheme.getDarkColor(), this.hueShiftFactor);
        this.mainMidColor = SubstanceColorUtilities.getHueShiftedColor(origScheme.getMidColor(), this.hueShiftFactor);
        this.mainLightColor = SubstanceColorUtilities.getHueShiftedColor(origScheme.getLightColor(), this.hueShiftFactor);
        this.mainExtraLightColor = SubstanceColorUtilities.getHueShiftedColor(origScheme.getExtraLightColor(), this.hueShiftFactor);
        this.mainUltraLightColor = SubstanceColorUtilities.getHueShiftedColor(origScheme.getUltraLightColor(), this.hueShiftFactor);
    }

    @Override
    public Color getForegroundColor() {
        return this.foregroundColor;
    }

    @Override
    public Color getUltraLightColor() {
        return this.mainUltraLightColor;
    }

    @Override
    public Color getExtraLightColor() {
        return this.mainExtraLightColor;
    }

    @Override
    public Color getLightColor() {
        return this.mainLightColor;
    }

    @Override
    public Color getMidColor() {
        return this.mainMidColor;
    }

    @Override
    public Color getDarkColor() {
        return this.mainDarkColor;
    }

    @Override
    public Color getUltraDarkColor() {
        return this.mainUltraDarkColor;
    }

    public SubstanceColorScheme getOrigScheme() {
        return this.origScheme;
    }

    public double getHueShiftFactor() {
        return this.hueShiftFactor;
    }
}

