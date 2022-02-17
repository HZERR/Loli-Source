/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.painter.highlight;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.painter.fill.GlassFillPainter;
import org.pushingpixels.substance.api.painter.highlight.SubstanceHighlightPainter;

public class GlassHighlightPainter
implements SubstanceHighlightPainter {
    public static final String DISPLAY_NAME = "Glass";
    protected GlassFillPainter painter = new GlassFillPainter();

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public void paintHighlight(Graphics2D graphics, Component comp, int width, int height, SubstanceColorScheme colorScheme) {
        Graphics2D g2d = (Graphics2D)graphics.create();
        g2d.translate(-3, -3);
        this.painter.paintContourBackground(g2d, comp, width + 6, height + 6, new Rectangle(width + 6, height + 6), false, colorScheme, false);
        g2d.dispose();
    }
}

