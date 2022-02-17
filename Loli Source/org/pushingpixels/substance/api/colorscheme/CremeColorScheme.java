/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.colorscheme.BaseLightColorScheme;

public class CremeColorScheme
extends BaseLightColorScheme {
    private static final Color mainUltraLightColor = new Color(254, 254, 252);
    private static final Color mainExtraLightColor = new Color(238, 243, 230);
    private static final Color mainLightColor = new Color(235, 234, 225);
    private static final Color mainMidColor = new Color(227, 228, 219);
    private static final Color mainDarkColor = new Color(179, 182, 176);
    private static final Color mainUltraDarkColor = new Color(178, 168, 153);
    private static final Color foregroundColor = Color.black;

    public CremeColorScheme() {
        super("Creme");
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

