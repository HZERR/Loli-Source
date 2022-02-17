/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.painter.border;

import java.awt.Color;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.painter.border.StandardBorderPainter;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;

public class ClassicBorderPainter
extends StandardBorderPainter {
    @Override
    public String getDisplayName() {
        return "Classic";
    }

    @Override
    public Color getTopBorderColor(SubstanceColorScheme borderScheme) {
        return SubstanceColorUtilities.getInterpolatedColor(super.getTopBorderColor(borderScheme), super.getMidBorderColor(borderScheme), 0.0);
    }

    @Override
    public Color getMidBorderColor(SubstanceColorScheme borderScheme) {
        return this.getTopBorderColor(borderScheme);
    }

    @Override
    public Color getBottomBorderColor(SubstanceColorScheme borderScheme) {
        return this.getTopBorderColor(borderScheme);
    }
}

