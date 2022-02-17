/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.painter.fill;

import java.awt.Color;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.painter.fill.ClassicFillPainter;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;

public class MatteFillPainter
extends ClassicFillPainter {
    public static final MatteFillPainter INSTANCE = new MatteFillPainter();

    @Override
    public String getDisplayName() {
        return "Matte";
    }

    @Override
    public Color getTopFillColor(SubstanceColorScheme fillScheme) {
        return SubstanceColorUtilities.getInterpolatedColor(super.getBottomFillColor(fillScheme), super.getMidFillColorTop(fillScheme), 0.5);
    }

    @Override
    public Color getMidFillColorTop(SubstanceColorScheme fillScheme) {
        return SubstanceColorUtilities.getInterpolatedColor(super.getMidFillColorTop(fillScheme), super.getBottomFillColor(fillScheme), 0.7);
    }

    @Override
    public Color getBottomFillColor(SubstanceColorScheme fillScheme) {
        return super.getMidFillColorTop(fillScheme);
    }
}

