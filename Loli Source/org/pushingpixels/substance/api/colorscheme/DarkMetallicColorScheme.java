/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.colorscheme.BaseDarkColorScheme;

public class DarkMetallicColorScheme
extends BaseDarkColorScheme {
    private static final Color mainUltraDarkColor = new Color(5, 3, 0);
    private static final Color mainDarkColor = new Color(15, 10, 5);
    private static final Color mainMidColor = new Color(55, 45, 35);
    private static final Color mainLightColor = new Color(75, 70, 65);
    private static final Color mainExtraLightColor = new Color(90, 85, 80);
    private static final Color mainUltraLightColor = new Color(100, 90, 85);
    private static final Color foregroundColor = Color.white;

    public DarkMetallicColorScheme() {
        super("Dark Metallic");
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

