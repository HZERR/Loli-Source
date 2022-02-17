/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.colorscheme.BaseLightColorScheme;

public class SunGlareColorScheme
extends BaseLightColorScheme {
    private static final Color mainUltraLightColor = new Color(255, 255, 209);
    private static final Color mainExtraLightColor = new Color(248, 249, 160);
    private static final Color mainLightColor = new Color(255, 255, 80);
    private static final Color mainMidColor = new Color(252, 226, 55);
    private static final Color mainDarkColor = new Color(106, 29, 0);
    private static final Color mainUltraDarkColor = new Color(67, 18, 0);
    private static final Color foregroundColor = Color.black;

    public SunGlareColorScheme() {
        super("Sun Glare");
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

