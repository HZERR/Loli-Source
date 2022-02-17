/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.colorscheme.BaseLightColorScheme;

public class TerracottaColorScheme
extends BaseLightColorScheme {
    private static final Color mainUltraLightColor = new Color(250, 203, 125);
    private static final Color mainExtraLightColor = new Color(248, 191, 114);
    private static final Color mainLightColor = new Color(239, 176, 105);
    private static final Color mainMidColor = new Color(227, 147, 88);
    private static final Color mainDarkColor = new Color(195, 113, 63);
    private static final Color mainUltraDarkColor = new Color(163, 87, 64);
    private static final Color foregroundColor = Color.black;

    public TerracottaColorScheme() {
        super("Terracotta");
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

