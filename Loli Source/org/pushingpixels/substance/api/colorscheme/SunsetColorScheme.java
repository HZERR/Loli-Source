/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.colorscheme.BaseLightColorScheme;

public class SunsetColorScheme
extends BaseLightColorScheme {
    private static final Color mainUltraLightColor = new Color(255, 196, 56);
    private static final Color mainExtraLightColor = new Color(255, 162, 45);
    private static final Color mainLightColor = new Color(255, 137, 41);
    private static final Color mainMidColor = new Color(254, 97, 30);
    private static final Color mainDarkColor = new Color(197, 19, 55);
    private static final Color mainUltraDarkColor = new Color(115, 38, 80);
    private static final Color foregroundColor = Color.black;

    public SunsetColorScheme() {
        super("Sunset");
    }

    @Override
    public Color getForegroundColor() {
        return foregroundColor;
    }

    @Override
    public Color getUltraLightColor() {
        return mainUltraLightColor;
    }

    @Override
    public Color getExtraLightColor() {
        return mainExtraLightColor;
    }

    @Override
    public Color getLightColor() {
        return mainLightColor;
    }

    @Override
    public Color getMidColor() {
        return mainMidColor;
    }

    @Override
    public Color getDarkColor() {
        return mainDarkColor;
    }

    @Override
    public Color getUltraDarkColor() {
        return mainUltraDarkColor;
    }
}

