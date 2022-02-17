/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.painter.overlay;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.SwingUtilities;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.painter.SubstancePainterUtils;
import org.pushingpixels.substance.api.painter.overlay.SubstanceOverlayPainter;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;

public final class TopShadowOverlayPainter
implements SubstanceOverlayPainter {
    private static TopShadowOverlayPainter INSTANCE;

    public static synchronized TopShadowOverlayPainter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TopShadowOverlayPainter();
        }
        return INSTANCE;
    }

    private TopShadowOverlayPainter() {
    }

    @Override
    public void paintOverlay(Graphics2D graphics, Component comp, DecorationAreaType decorationAreaType, int width, int height, SubstanceSkin skin) {
        Color shadowColor = SubstanceColorUtilities.getBackgroundFillColor(comp).darker();
        Component topMostWithSameDecorationAreaType = SubstancePainterUtils.getTopMostParentWithDecorationAreaType(comp, decorationAreaType);
        Point inTopMost = SwingUtilities.convertPoint(comp, new Point(0, 0), topMostWithSameDecorationAreaType);
        int dy = inTopMost.y;
        Graphics2D g2d = (Graphics2D)graphics.create();
        g2d.translate(0, -dy);
        g2d.setPaint(new GradientPaint(0.0f, 0.0f, SubstanceColorUtilities.getAlphaColor(shadowColor, 160), 0.0f, 4.0f, SubstanceColorUtilities.getAlphaColor(shadowColor, 16)));
        g2d.fillRect(0, 0, comp.getWidth(), 4);
        g2d.dispose();
    }

    @Override
    public String getDisplayName() {
        return "Top Shadow";
    }
}

