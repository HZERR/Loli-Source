/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.colorscheme.BaseDarkColorScheme;

public class JadeForestColorScheme
extends BaseDarkColorScheme {
    private static final Color mainUltraLightColor = new Color(40, 124, 22);
    private static final Color mainExtraLightColor = new Color(45, 113, 19);
    private static final Color mainLightColor = new Color(39, 104, 17);
    private static final Color mainMidColor = new Color(6, 53, 27);
    private static final Color mainDarkColor = new Color(7, 38, 1);
    private static final Color mainUltraDarkColor = new Color(10, 23, 1);
    private static final Color foregroundColor = Color.white;

    public JadeForestColorScheme() {
        super("Jade Forest");
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

