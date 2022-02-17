/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.painter.fill;

import java.awt.Color;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.painter.fill.StandardFillPainter;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;

public class GlassFillPainter
extends StandardFillPainter {
    @Override
    public String getDisplayName() {
        return "Glass";
    }

    @Override
    public Color getTopFillColor(SubstanceColorScheme fillScheme) {
        return SubstanceColorUtilities.getInterpolatedColor(super.getBottomFillColor(fillScheme), super.getMidFillColorTop(fillScheme), 0.6);
    }

    @Override
    public Color getMidFillColorTop(SubstanceColorScheme fillScheme) {
        return SubstanceColorUtilities.getInterpolatedColor(this.getTopFillColor(fillScheme), super.getMidFillColorTop(fillScheme), 0.8);
    }

    @Override
    public Color getMidFillColorBottom(SubstanceColorScheme fillScheme) {
        return super.getMidFillColorTop(fillScheme);
    }

    @Override
    public Color getBottomFillColor(SubstanceColorScheme fillScheme) {
        return SubstanceColorUtilities.getInterpolatedColor(this.getMidFillColorBottom(fillScheme), super.getBottomFillColor(fillScheme), 0.7);
    }
}

