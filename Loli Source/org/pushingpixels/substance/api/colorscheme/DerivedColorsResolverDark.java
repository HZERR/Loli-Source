/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.SchemeDerivedColorsResolver;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.colorscheme.DerivedColorsResolverLight;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;

class DerivedColorsResolverDark
implements SchemeDerivedColorsResolver {
    static final DerivedColorsResolverDark INSTANCE = new DerivedColorsResolverDark();

    DerivedColorsResolverDark() {
    }

    @Override
    public boolean isDark() {
        return true;
    }

    @Override
    public SchemeDerivedColorsResolver invert() {
        return DerivedColorsResolverLight.INSTANCE;
    }

    @Override
    public Color getWatermarkStampColor(SubstanceColorScheme colorScheme) {
        return SubstanceColorUtilities.getAlphaColor(colorScheme.getUltraLightColor(), 30);
    }

    @Override
    public Color getWatermarkDarkColor(SubstanceColorScheme colorScheme) {
        return colorScheme.getLightColor();
    }

    @Override
    public Color getWatermarkLightColor(SubstanceColorScheme colorScheme) {
        return colorScheme.getUltraLightColor();
    }

    @Override
    public Color getLineColor(SubstanceColorScheme colorScheme) {
        return colorScheme.getMidColor();
    }

    @Override
    public Color getSelectionForegroundColor(SubstanceColorScheme colorScheme) {
        return colorScheme.getUltraDarkColor().darker();
    }

    @Override
    public Color getSelectionBackgroundColor(SubstanceColorScheme colorScheme) {
        return colorScheme.getUltraLightColor().brighter();
    }

    @Override
    public Color getBackgroundFillColor(SubstanceColorScheme colorScheme) {
        return colorScheme.getDarkColor().brighter();
    }

    @Override
    public Color getFocusRingColor(SubstanceColorScheme colorScheme) {
        return colorScheme.getUltraDarkColor();
    }

    @Override
    public Color getTextBackgroundFillColor(SubstanceColorScheme colorScheme) {
        return SubstanceColorUtilities.getInterpolatedColor(colorScheme.getMidColor(), colorScheme.getLightColor(), 0.4);
    }
}

