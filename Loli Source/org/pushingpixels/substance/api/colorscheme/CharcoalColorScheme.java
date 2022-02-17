/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.colorscheme.BaseDarkColorScheme;

public class CharcoalColorScheme
extends BaseDarkColorScheme {
    private static final Color mainUltraLightColor = new Color(110, 21, 27);
    private static final Color mainExtraLightColor = new Color(94, 27, 36);
    private static final Color mainLightColor = new Color(61, 19, 29);
    private static final Color mainMidColor = new Color(50, 20, 22);
    private static final Color mainDarkColor = new Color(35, 15, 10);
    private static final Color mainUltraDarkColor = new Color(13, 8, 4);
    private static final Color foregroundColor = Color.white;

    public CharcoalColorScheme() {
        super("Charcoal");
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

