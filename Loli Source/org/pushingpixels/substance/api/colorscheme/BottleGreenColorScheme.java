/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.colorscheme.BaseLightColorScheme;

public class BottleGreenColorScheme
extends BaseLightColorScheme {
    private static final Color mainUltraLightColor = new Color(145, 209, 131);
    private static final Color mainExtraLightColor = new Color(115, 197, 99);
    private static final Color mainLightColor = new Color(63, 181, 59);
    private static final Color mainMidColor = new Color(6, 139, 58);
    private static final Color mainDarkColor = new Color(11, 75, 38);
    private static final Color mainUltraDarkColor = new Color(0, 14, 14);
    private static final Color foregroundColor = Color.black;

    public BottleGreenColorScheme() {
        super("Bottle Green");
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

