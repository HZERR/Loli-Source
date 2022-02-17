/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.painter.fill;

import java.awt.Color;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.painter.fill.StandardFillPainter;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;

public class SubduedFillPainter
extends StandardFillPainter {
    @Override
    public String getDisplayName() {
        return "Subdued";
    }

    @Override
    public Color getTopFillColor(SubstanceColorScheme fillScheme) {
        return SubstanceColorUtilities.getInterpolatedColor(super.getTopFillColor(fillScheme), this.getMidFillColorTop(fillScheme), 0.3);
    }
}

