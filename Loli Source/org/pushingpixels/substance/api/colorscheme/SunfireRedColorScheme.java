/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.colorscheme.BaseLightColorScheme;

public class SunfireRedColorScheme
extends BaseLightColorScheme {
    private static final Color mainUltraLightColor = new Color(225, 139, 166);
    private static final Color mainExtraLightColor = new Color(218, 110, 130);
    private static final Color mainLightColor = new Color(215, 42, 23);
    private static final Color mainMidColor = new Color(224, 20, 10);
    private static final Color mainDarkColor = new Color(170, 28, 23);
    private static final Color mainUltraDarkColor = new Color(129, 23, 15);
    private static final Color foregroundColor = Color.black;

    public SunfireRedColorScheme() {
        super("Sunfire Red");
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

