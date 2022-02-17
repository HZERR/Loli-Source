/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.colorscheme.BaseLightColorScheme;

public class LightGrayColorScheme
extends BaseLightColorScheme {
    private static final Color mainUltraLightColor = new Color(250, 251, 252);
    private static final Color mainExtraLightColor = new Color(240, 242, 244);
    private static final Color mainLightColor = new Color(225, 228, 231);
    private static final Color mainMidColor = new Color(210, 214, 218);
    private static final Color mainDarkColor = new Color(180, 185, 190);
    private static final Color mainUltraDarkColor = new Color(100, 106, 112);
    private static final Color foregroundColor = new Color(120, 125, 130);

    public LightGrayColorScheme() {
        super("Light Gray");
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

