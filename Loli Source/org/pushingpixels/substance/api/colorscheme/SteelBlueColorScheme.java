/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.colorscheme.BaseLightColorScheme;

public class SteelBlueColorScheme
extends BaseLightColorScheme {
    private static final Color mainUltraLightColor = new Color(149, 193, 219);
    private static final Color mainExtraLightColor = new Color(130, 181, 212);
    private static final Color mainLightColor = new Color(118, 165, 195);
    private static final Color mainMidColor = new Color(108, 149, 178);
    private static final Color mainDarkColor = new Color(38, 79, 111);
    private static final Color mainUltraDarkColor = new Color(47, 75, 99);
    private static final Color foregroundColor = Color.black;

    public SteelBlueColorScheme() {
        super("Steel Blue");
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

