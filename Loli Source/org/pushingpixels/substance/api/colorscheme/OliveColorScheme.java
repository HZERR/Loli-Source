/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.colorscheme.BaseLightColorScheme;

public class OliveColorScheme
extends BaseLightColorScheme {
    private static final Color mainUltraLightColor = new Color(205, 212, 182);
    private static final Color mainExtraLightColor = new Color(189, 192, 165);
    private static final Color mainLightColor = new Color(175, 183, 142);
    private static final Color mainMidColor = new Color(165, 174, 129);
    private static final Color mainDarkColor = new Color(135, 142, 102);
    private static final Color mainUltraDarkColor = new Color(104, 111, 67);
    private static final Color foregroundColor = Color.black;

    public OliveColorScheme() {
        super("Olive");
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

