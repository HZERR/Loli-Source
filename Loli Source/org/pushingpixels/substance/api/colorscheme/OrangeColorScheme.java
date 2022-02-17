/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.colorscheme.BaseLightColorScheme;

public class OrangeColorScheme
extends BaseLightColorScheme {
    public static final Color mainUltraLightColor = new Color(255, 250, 235);
    public static final Color mainExtraLightColor = new Color(255, 220, 180);
    public static final Color mainLightColor = new Color(245, 200, 128);
    public static final Color mainMidColor = new Color(240, 170, 50);
    public static final Color mainDarkColor = new Color(229, 151, 0);
    public static final Color mainUltraDarkColor = new Color(180, 100, 0);
    private static final Color foregroundColor = Color.black;

    public OrangeColorScheme() {
        super("Orange");
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

