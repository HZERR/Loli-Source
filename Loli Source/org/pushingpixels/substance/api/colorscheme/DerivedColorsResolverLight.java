/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.SchemeDerivedColorsResolver;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.colorscheme.DerivedColorsResolverDark;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;

public class DerivedColorsResolverLight
implements SchemeDerivedColorsResolver {
    static final DerivedColorsResolverLight INSTANCE = new DerivedColorsResolverLight();

    @Override
    public boolean isDark() {
        return false;
    }

    @Override
    public SchemeDerivedColorsResolver invert() {
        return DerivedColorsResolverDark.INSTANCE;
    }

    @Override
    public Color getWatermarkStampColor(SubstanceColorScheme colorScheme) {
        return SubstanceColorUtilities.getAlphaColor(colorScheme.getMidColor(), 50);
    }

    @Override
    public Color getWatermarkLightColor(SubstanceColorScheme colorScheme) {
        return colorScheme.getLightColor();
    }

    @Override
    public Color getWatermarkDarkColor(SubstanceColorScheme colorScheme) {
        return SubstanceColorUtilities.getAlphaColor(colorScheme.getDarkColor(), 15);
    }

    @Override
    public Color getLineColor(SubstanceColorScheme colorScheme) {
        return SubstanceColorUtilities.getInterpolatedColor(colorScheme.getMidColor(), colorScheme.getDarkColor(), 0.7);
    }

    @Override
    public Color getSelectionForegroundColor(SubstanceColorScheme colorScheme) {
        return colorScheme.getUltraDarkColor().darker().darker();
    }

    @Override
    public Color getSelectionBackgroundColor(SubstanceColorScheme colorScheme) {
        return colorScheme.getExtraLightColor();
    }

    @Override
    public Color getBackgroundFillColor(SubstanceColorScheme colorScheme) {
        return colorScheme.getExtraLightColor();
    }

    @Override
    public Color getFocusRingColor(SubstanceColorScheme colorScheme) {
        return colorScheme.getDarkColor();
    }

    @Override
    public Color getTextBackgroundFillColor(SubstanceColorScheme colorScheme) {
        return SubstanceColorUtilities.getInterpolatedColor(colorScheme.getUltraLightColor(), colorScheme.getExtraLightColor(), 0.8);
    }
}

