/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.colorscheme.BaseLightColorScheme;

public class AquaColorScheme
extends BaseLightColorScheme {
    private static final Color mainUltraLightColor = new Color(194, 224, 237);
    private static final Color mainExtraLightColor = new Color(164, 227, 243);
    private static final Color mainLightColor = new Color(112, 206, 239);
    private static final Color mainMidColor = new Color(32, 180, 226);
    private static final Color mainDarkColor = new Color(44, 47, 140);
    private static final Color mainUltraDarkColor = new Color(30, 40, 100);
    private static final Color foregroundColor = Color.black;

    public AquaColorScheme() {
        super("Aqua");
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

