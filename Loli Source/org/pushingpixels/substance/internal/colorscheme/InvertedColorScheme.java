/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.colorscheme.BaseColorScheme;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;

public class InvertedColorScheme
extends BaseColorScheme {
    private Color mainUltraLightColor;
    private Color mainExtraLightColor;
    private Color mainLightColor;
    private Color mainMidColor;
    private Color mainDarkColor;
    private Color mainUltraDarkColor;
    private Color foregroundColor;
    private SubstanceColorScheme origScheme;

    public InvertedColorScheme(SubstanceColorScheme origScheme) {
        super("Inverted " + origScheme.getDisplayName(), InvertedColorScheme.getResolver(origScheme).invert());
        this.origScheme = origScheme;
        this.foregroundColor = SubstanceColorUtilities.invertColor(origScheme.getForegroundColor());
        this.mainUltraDarkColor = SubstanceColorUtilities.invertColor(origScheme.getUltraLightColor());
        this.mainDarkColor = SubstanceColorUtilities.invertColor(origScheme.getExtraLightColor());
        this.mainMidColor = SubstanceColorUtilities.invertColor(origScheme.getLightColor());
        this.mainLightColor = SubstanceColorUtilities.invertColor(origScheme.getMidColor());
        this.mainExtraLightColor = SubstanceColorUtilities.invertColor(origScheme.getDarkColor());
        this.mainUltraLightColor = SubstanceColorUtilities.invertColor(origScheme.getUltraDarkColor());
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
}

