/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.colorscheme.BaseDarkColorScheme;

public class EbonyColorScheme
extends BaseDarkColorScheme {
    private static final Color mainUltraLightColor = new Color(85, 85, 85);
    private static final Color mainExtraLightColor = new Color(75, 75, 75);
    private static final Color mainLightColor = new Color(60, 60, 60);
    private static final Color mainMidColor = new Color(40, 40, 40);
    private static final Color mainDarkColor = new Color(20, 20, 20);
    private static final Color mainUltraDarkColor = new Color(10, 10, 10);
    private static final Color foregroundColor = Color.white;

    public EbonyColorScheme() {
        super("Ebony");
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

