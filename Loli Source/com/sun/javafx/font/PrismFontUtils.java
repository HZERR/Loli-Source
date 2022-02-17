/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.Metrics;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.geom.transform.BaseTransform;

public class PrismFontUtils {
    private PrismFontUtils() {
    }

    static Metrics getFontMetrics(PGFont pGFont) {
        FontStrike fontStrike = pGFont.getStrike(BaseTransform.IDENTITY_TRANSFORM, 0);
        return fontStrike.getMetrics();
    }

    static double computeStringWidth(PGFont pGFont, String string) {
        if (string == null || string.equals("")) {
            return 0.0;
        }
        FontStrike fontStrike = pGFont.getStrike(BaseTransform.IDENTITY_TRANSFORM, 0);
        double d2 = 0.0;
        for (int i2 = 0; i2 < string.length(); ++i2) {
            d2 += (double)fontStrike.getCharAdvance(string.charAt(i2));
        }
        return d2;
    }
}

