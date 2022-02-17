/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api;

import java.awt.Color;
import org.pushingpixels.substance.api.SubstanceColorScheme;

public interface SchemeDerivedColorsResolver {
    public boolean isDark();

    public SchemeDerivedColorsResolver invert();

    public Color getWatermarkStampColor(SubstanceColorScheme var1);

    public Color getWatermarkLightColor(SubstanceColorScheme var1);

    public Color getWatermarkDarkColor(SubstanceColorScheme var1);

    public Color getLineColor(SubstanceColorScheme var1);

    public Color getSelectionBackgroundColor(SubstanceColorScheme var1);

    public Color getSelectionForegroundColor(SubstanceColorScheme var1);

    public Color getBackgroundFillColor(SubstanceColorScheme var1);

    public Color getTextBackgroundFillColor(SubstanceColorScheme var1);

    public Color getFocusRingColor(SubstanceColorScheme var1);
}

