/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.colorscheme.BaseColorScheme;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;

public class SaturatedColorScheme
extends BaseColorScheme {
    private double saturationFactor;
    private Color mainUltraLightColor;
    private Color mainExtraLightColor;
    private Color mainLightColor;
    private Color mainMidColor;
    private Color mainDarkColor;
    private Color mainUltraDarkColor;
    private Color foregroundColor;
    private SubstanceColorScheme origScheme;

    public SaturatedColorScheme(SubstanceColorScheme origScheme, double saturationFactor) {
        super("Saturated (" + (int)(100.0 * saturationFactor) + "%) " + origScheme.getDisplayName(), SaturatedColorScheme.getResolver(origScheme));
        this.saturationFactor = saturationFactor;
        this.origScheme = origScheme;
        this.foregroundColor = origScheme.getForegroundColor();
        this.mainUltraDarkColor = SubstanceColorUtilities.getSaturatedColor(origScheme.getUltraDarkColor(), saturationFactor);
        this.mainDarkColor = SubstanceColorUtilities.getSaturatedColor(origScheme.getDarkColor(), saturationFactor);
        this.mainMidColor = SubstanceColorUtilities.getSaturatedColor(origScheme.getMidColor(), saturationFactor);
        this.mainLightColor = SubstanceColorUtilities.getSaturatedColor(origScheme.getLightColor(), saturationFactor);
        this.mainExtraLightColor = SubstanceColorUtilities.getSaturatedColor(origScheme.getExtraLightColor(), saturationFactor);
        this.mainUltraLightColor = SubstanceColorUtilities.getSaturatedColor(origScheme.getUltraLightColor(), saturationFactor);
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

    public double getSaturationFactor() {
        return this.saturationFactor;
    }
}

