/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.colorscheme.BaseLightColorScheme;

public class DesertSandColorScheme
extends BaseLightColorScheme {
    private static final Color mainUltraLightColor = new Color(204, 226, 135);
    private static final Color mainExtraLightColor = new Color(187, 204, 170);
    private static final Color mainLightColor = new Color(182, 200, 119);
    private static final Color mainMidColor = new Color(147, 157, 105);
    private static final Color mainDarkColor = new Color(113, 120, 81);
    private static final Color mainUltraDarkColor = new Color(80, 96, 48);
    private static final Color foregroundColor = Color.black;

    public DesertSandColorScheme() {
        super("Desert Sand");
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

