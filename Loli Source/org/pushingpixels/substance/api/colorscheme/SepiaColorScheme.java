/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.colorscheme.BaseLightColorScheme;

public class SepiaColorScheme
extends BaseLightColorScheme {
    private static final Color mainUltraLightColor = new Color(220, 182, 150);
    private static final Color mainExtraLightColor = new Color(205, 168, 135);
    private static final Color mainLightColor = new Color(195, 153, 128);
    private static final Color mainMidColor = new Color(187, 151, 102);
    private static final Color mainDarkColor = new Color(157, 102, 72);
    private static final Color mainUltraDarkColor = new Color(154, 106, 84);
    private static final Color foregroundColor = Color.black;

    public SepiaColorScheme() {
        super("Sepia");
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

