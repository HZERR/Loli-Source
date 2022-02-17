/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.colorscheme.BaseLightColorScheme;

public class PurpleColorScheme
extends BaseLightColorScheme {
    private static final Color mainUltraLightColor = new Color(240, 220, 245);
    private static final Color mainExtraLightColor = new Color(218, 209, 233);
    private static final Color mainLightColor = new Color(203, 175, 237);
    private static final Color mainMidColor = new Color(201, 135, 226);
    private static final Color mainDarkColor = new Color(140, 72, 170);
    private static final Color mainUltraDarkColor = new Color(94, 39, 114);
    private static final Color foregroundColor = Color.black;

    public PurpleColorScheme() {
        super("Purple");
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

