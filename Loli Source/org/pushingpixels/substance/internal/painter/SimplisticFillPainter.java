/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.painter;

import java.awt.Color;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.painter.fill.StandardFillPainter;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;

public class SimplisticFillPainter
extends StandardFillPainter {
    public static final SimplisticFillPainter INSTANCE = new SimplisticFillPainter();

    @Override
    public String getDisplayName() {
        return "Simplistic";
    }

    @Override
    public Color getTopFillColor(SubstanceColorScheme fillScheme) {
        return super.getMidFillColorTop(fillScheme);
    }

    @Override
    public Color getMidFillColorTop(SubstanceColorScheme fillScheme) {
        return SubstanceColorUtilities.getInterpolatedColor(super.getMidFillColorTop(fillScheme), super.getBottomFillColor(fillScheme), 0.5);
    }

    @Override
    public Color getTopShineColor(SubstanceColorScheme fillScheme) {
        return null;
    }

    @Override
    public Color getBottomShineColor(SubstanceColorScheme fillScheme) {
        return null;
    }
}

