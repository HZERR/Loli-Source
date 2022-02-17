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

public final class BottomShadowOverlayPainter
implements SubstanceOverlayPainter {
    private static BottomShadowOverlayPainter INSTANCE;

    public static synchronized BottomShadowOverlayPainter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BottomShadowOverlayPainter();
        }
        return INSTANCE;
    }

    private BottomShadowOverlayPainter() {
    }

    @Override
    public void paintOverlay(Graphics2D graphics, Component comp, DecorationAreaType decorationAreaType, int width, int height, SubstanceSkin skin) {
        Color shadowColor = SubstanceColorUtilities.getBackgroundFillColor(comp).darker();
        Component topMostWithSameDecorationAreaType = SubstancePainterUtils.getTopMostParentWithDecorationAreaType(comp, decorationAreaType);
        int topHeight = topMostWithSameDecorationAreaType.getHeight();
        Point inTopMost = SwingUtilities.convertPoint(comp, new Point(0, 0), topMostWithSameDecorationAreaType);
        int dy = inTopMost.y;
        Graphics2D fillGraphics = (Graphics2D)graphics.create();
        fillGraphics.translate(0, -dy);
        int shadowHeight = 4;
        GradientPaint fillPaint = new GradientPaint(0.0f, topHeight - shadowHeight, SubstanceColorUtilities.getAlphaColor(shadowColor, 0), 0.0f, topHeight, SubstanceColorUtilities.getAlphaColor(shadowColor, 128));
        fillGraphics.setPaint(fillPaint);
        fillGraphics.fillRect(0, topHeight - shadowHeight, width, shadowHeight);
        fillGraphics.dispose();
    }

    @Override
    public String getDisplayName() {
        return "Bottom Shadow";
    }
}

