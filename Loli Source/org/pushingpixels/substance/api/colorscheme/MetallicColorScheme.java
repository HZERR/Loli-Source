/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.colorscheme.BaseLightColorScheme;

public class MetallicColorScheme
extends BaseLightColorScheme {
    private static final Color mainUltraLightColor = new Color(250, 252, 255);
    private static final Color mainExtraLightColor = new Color(240, 245, 250);
    private static final Color mainLightColor = new Color(200, 210, 220);
    private static final Color mainMidColor = new Color(180, 185, 190);
    private static final Color mainDarkColor = new Color(80, 85, 90);
    private static final Color mainUltraDarkColor = new Color(32, 37, 42);
    private static final Color foregroundColor = new Color(15, 20, 25);

    public MetallicColorScheme() {
        super("Metallic");
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

