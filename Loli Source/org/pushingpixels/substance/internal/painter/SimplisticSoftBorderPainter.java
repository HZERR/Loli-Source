/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.painter;

import java.awt.Color;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.painter.border.StandardBorderPainter;

public class SimplisticSoftBorderPainter
extends StandardBorderPainter {
    @Override
    public String getDisplayName() {
        return "Simplistic Soft Border";
    }

    @Override
    public Color getTopBorderColor(SubstanceColorScheme borderScheme) {
        return super.getMidBorderColor(borderScheme);
    }

    @Override
    public Color getMidBorderColor(SubstanceColorScheme borderScheme) {
        return super.getBottomBorderColor(borderScheme);
    }

    @Override
    public Color getBottomBorderColor(SubstanceColorScheme borderScheme) {
        return super.getBottomBorderColor(borderScheme);
    }
}

