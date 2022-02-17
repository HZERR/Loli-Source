/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.colorscheme.BaseDarkColorScheme;

public class DarkGrayColorScheme
extends BaseDarkColorScheme {
    private static final Color mainUltraDarkColor = new Color(5, 5, 5);
    private static final Color mainDarkColor = new Color(15, 15, 15);
    private static final Color mainMidColor = new Color(30, 30, 30);
    private static final Color mainLightColor = new Color(45, 45, 45);
    private static final Color mainExtraLightColor = new Color(75, 75, 75);
    private static final Color mainUltraLightColor = new Color(155, 155, 155);
    private static final Color foregroundColor = Color.white;

    public DarkGrayColorScheme() {
        super("Dark Gray");
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

