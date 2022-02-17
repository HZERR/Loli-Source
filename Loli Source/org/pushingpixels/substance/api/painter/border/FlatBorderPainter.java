/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.painter.border;

import java.awt.Color;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.painter.border.StandardBorderPainter;

public class FlatBorderPainter
extends StandardBorderPainter {
    @Override
    public String getDisplayName() {
        return "Flat";
    }

    @Override
    public Color getMidBorderColor(SubstanceColorScheme borderScheme) {
        return super.getTopBorderColor(borderScheme);
    }

    @Override
    public Color getBottomBorderColor(SubstanceColorScheme borderScheme) {
        return super.getTopBorderColor(borderScheme);
    }
}

