/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.painter.border;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Shape;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.painter.border.SubstanceBorderPainter;

public class CompositeBorderPainter
implements SubstanceBorderPainter {
    private String displayName;
    private SubstanceBorderPainter inner;
    private SubstanceBorderPainter outer;

    public CompositeBorderPainter(String displayName, SubstanceBorderPainter outer, SubstanceBorderPainter inner) {
        this.displayName = displayName;
        this.outer = outer;
        this.inner = inner;
    }

    @Override
    public boolean isPaintingInnerContour() {
        return true;
    }

    @Override
    public void paintBorder(Graphics g2, Component c2, int width, int height, Shape contour, Shape innerContour, SubstanceColorScheme borderScheme) {
        if (innerContour != null) {
            this.inner.paintBorder(g2, c2, width, height, innerContour, null, borderScheme);
        }
        if (contour != null) {
            this.outer.paintBorder(g2, c2, width, height, contour, null, borderScheme);
        }
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }
}

