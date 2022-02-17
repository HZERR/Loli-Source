/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.colorscheme.BaseColorScheme;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;

public class BlendBiColorScheme
extends BaseColorScheme {
    private Color mainUltraLightColor;
    private Color mainExtraLightColor;
    private Color mainLightColor;
    private Color mainMidColor;
    private Color mainDarkColor;
    private Color mainUltraDarkColor;
    private Color foregroundColor;
    private SubstanceColorScheme firstScheme;
    private SubstanceColorScheme secondScheme;
    private double firstSchemeLikeness;

    public BlendBiColorScheme(SubstanceColorScheme firstScheme, SubstanceColorScheme secondScheme, double firstSchemeLikeness) {
        super("Blended " + firstScheme.getDisplayName() + " & " + secondScheme.getDisplayName() + " " + firstSchemeLikeness, firstScheme.isDark());
        this.firstScheme = firstScheme;
        this.secondScheme = secondScheme;
        this.firstSchemeLikeness = firstSchemeLikeness;
        this.foregroundColor = new Color(SubstanceColorUtilities.getInterpolatedRGB(firstScheme.getForegroundColor(), secondScheme.getForegroundColor(), firstSchemeLikeness));
        this.mainUltraDarkColor = new Color(SubstanceColorUtilities.getInterpolatedRGB(firstScheme.getUltraDarkColor(), secondScheme.getUltraDarkColor(), firstSchemeLikeness));
        this.mainDarkColor = new Color(SubstanceColorUtilities.getInterpolatedRGB(firstScheme.getDarkColor(), secondScheme.getDarkColor(), firstSchemeLikeness));
        this.mainMidColor = new Color(SubstanceColorUtilities.getInterpolatedRGB(firstScheme.getMidColor(), secondScheme.getMidColor(), firstSchemeLikeness));
        this.mainLightColor = new Color(SubstanceColorUtilities.getInterpolatedRGB(firstScheme.getLightColor(), secondScheme.getLightColor(), firstSchemeLikeness));
        this.mainExtraLightColor = new Color(SubstanceColorUtilities.getInterpolatedRGB(firstScheme.getExtraLightColor(), secondScheme.getExtraLightColor(), firstSchemeLikeness));
        this.mainUltraLightColor = new Color(SubstanceColorUtilities.getInterpolatedRGB(firstScheme.getUltraLightColor(), secondScheme.getUltraLightColor(), firstSchemeLikeness));
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

    public double getFirstSchemeLikeness() {
        return this.firstSchemeLikeness;
    }

    public SubstanceColorScheme getFirstScheme() {
        return this.firstScheme;
    }

    public SubstanceColorScheme getSecondScheme() {
        return this.secondScheme;
    }
}

