/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.colorscheme.BaseLightColorScheme;

public class LimeGreenColorScheme
extends BaseLightColorScheme {
    private static final Color mainUltraLightColor = new Color(205, 255, 85);
    private static final Color mainExtraLightColor = new Color(172, 255, 54);
    private static final Color mainLightColor = new Color(169, 248, 57);
    private static final Color mainMidColor = new Color(117, 232, 39);
    private static final Color mainDarkColor = new Color(18, 86, 0);
    private static final Color mainUltraDarkColor = new Color(8, 62, 0);
    private static final Color foregroundColor = Color.black;

    public LimeGreenColorScheme() {
        super("Lime Green");
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

