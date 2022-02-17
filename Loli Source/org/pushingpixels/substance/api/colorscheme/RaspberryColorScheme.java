/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.colorscheme.BaseLightColorScheme;

public class RaspberryColorScheme
extends BaseLightColorScheme {
    private static final Color mainUltraLightColor = new Color(254, 166, 189);
    private static final Color mainExtraLightColor = new Color(255, 152, 177);
    private static final Color mainLightColor = new Color(251, 110, 144);
    private static final Color mainMidColor = new Color(225, 52, 98);
    private static final Color mainDarkColor = new Color(84, 28, 41);
    private static final Color mainUltraDarkColor = new Color(40, 0, 9);
    private static final Color foregroundColor = Color.black;

    public RaspberryColorScheme() {
        super("Raspberry");
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

