/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.colorscheme.BaseLightColorScheme;

public class BarbyPinkColorScheme
extends BaseLightColorScheme {
    private static final Color mainUltraLightColor = new Color(240, 159, 242);
    private static final Color mainExtraLightColor = new Color(239, 153, 235);
    private static final Color mainLightColor = new Color(238, 139, 230);
    private static final Color mainMidColor = new Color(231, 95, 193);
    private static final Color mainDarkColor = new Color(150, 30, 101);
    private static final Color mainUltraDarkColor = new Color(111, 29, 78);
    private static final Color foregroundColor = Color.black;

    public BarbyPinkColorScheme() {
        super("Barby Pink");
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

