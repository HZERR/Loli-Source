/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.colorscheme.BaseDarkColorScheme;

public class UltramarineColorScheme
extends BaseDarkColorScheme {
    private static final Color mainUltraLightColor = new Color(46, 22, 124);
    private static final Color mainExtraLightColor = new Color(33, 19, 113);
    private static final Color mainLightColor = new Color(31, 17, 104);
    private static final Color mainMidColor = new Color(47, 6, 53);
    private static final Color mainDarkColor = new Color(11, 1, 38);
    private static final Color mainUltraDarkColor = new Color(2, 1, 23);
    private static final Color foregroundColor = Color.white;

    public UltramarineColorScheme() {
        super("Ultramarine");
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

