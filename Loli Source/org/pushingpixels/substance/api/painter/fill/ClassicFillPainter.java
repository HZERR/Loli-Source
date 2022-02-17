/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.painter.fill;

import java.awt.Color;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.painter.fill.StandardFillPainter;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;

public class ClassicFillPainter
extends StandardFillPainter {
    public static final ClassicFillPainter INSTANCE = new ClassicFillPainter();

    @Override
    public String getDisplayName() {
        return "Classic";
    }

    @Override
    public Color getTopFillColor(SubstanceColorScheme fillScheme) {
        return SubstanceColorUtilities.getInterpolatedColor(super.getBottomFillColor(fillScheme), super.getMidFillColorTop(fillScheme), 0.5);
    }

    @Override
    public Color getMidFillColorTop(SubstanceColorScheme fillScheme) {
        return SubstanceColorUtilities.getInterpolatedColor(super.getMidFillColorTop(fillScheme), super.getBottomFillColor(fillScheme), 0.7);
    }
}

