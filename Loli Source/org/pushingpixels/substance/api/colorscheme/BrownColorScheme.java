/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.colorscheme.BaseLightColorScheme;

public class BrownColorScheme
extends BaseLightColorScheme {
    private static final Color mainUltraLightColor = new Color(240, 230, 170);
    private static final Color mainExtraLightColor = new Color(230, 219, 142);
    private static final Color mainLightColor = new Color(217, 179, 89);
    private static final Color mainMidColor = new Color(190, 137, 27);
    private static final Color mainDarkColor = new Color(162, 90, 26);
    private static final Color mainUltraDarkColor = new Color(94, 71, 57);
    private static final Color foregroundColor = Color.black;

    public BrownColorScheme() {
        super("Brown");
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

