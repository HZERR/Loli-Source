/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.painter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.watermark.SubstanceWatermark;
import org.pushingpixels.substance.internal.painter.DecorationPainterUtils;
import org.pushingpixels.substance.internal.painter.OverlayPainterUtils;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;

public class BackgroundPaintingUtils {
    public static void updateIfOpaque(Graphics g2, Component c2) {
        if (SubstanceCoreUtilities.isOpaque(c2)) {
            BackgroundPaintingUtils.update(g2, c2, false);
        }
    }

    public static void update(Graphics g2, Component c2, boolean force) {
        BackgroundPaintingUtils.update(g2, c2, force, null);
    }

    public static void update(Graphics g2, Component c2, boolean force, DecorationAreaType decorationType) {
        if (!SubstanceLookAndFeel.isCurrentLookAndFeel()) {
            return;
        }
        boolean isInCellRenderer = SwingUtilities.getAncestorOfClass(CellRendererPane.class, c2) != null;
        boolean isPreviewMode = false;
        if (c2 instanceof JComponent) {
            isPreviewMode = Boolean.TRUE.equals(((JComponent)c2).getClientProperty("lafwidgets.internal.previewMode"));
        }
        Graphics2D graphics = (Graphics2D)g2.create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        graphics.setComposite(LafWidgetUtilities.getAlphaComposite(c2, g2));
        if (decorationType == null) {
            decorationType = SubstanceLookAndFeel.getDecorationType(c2);
        }
        SubstanceSkin skin = SubstanceCoreUtilities.getSkin(c2);
        boolean isShowing = c2.isShowing();
        if (isShowing && decorationType != DecorationAreaType.NONE && skin.isRegisteredAsDecorationArea(decorationType)) {
            DecorationPainterUtils.paintDecorationBackground(graphics, c2, decorationType, force);
            OverlayPainterUtils.paintOverlays(graphics, c2, skin, decorationType);
        } else {
            Color backgr = SubstanceColorUtilities.getBackgroundFillColor((c2 instanceof JTextComponent || c2 instanceof JSpinner) && c2.getParent() != null ? c2.getParent() : c2);
            graphics.setColor(backgr);
            graphics.fillRect(0, 0, c2.getWidth(), c2.getHeight());
            if (isShowing) {
                OverlayPainterUtils.paintOverlays(graphics, c2, skin, decorationType);
                SubstanceWatermark watermark = SubstanceCoreUtilities.getSkin(c2).getWatermark();
                if (watermark != null && !isPreviewMode && !isInCellRenderer && SubstanceCoreUtilities.toDrawWatermark(c2)) {
                    watermark.drawWatermarkImage(graphics, c2, 0, 0, c2.getWidth(), c2.getHeight());
                }
            }
        }
        graphics.dispose();
    }

    public static void fillAndWatermark(Graphics g2, JComponent c2, Color fillColor, Rectangle rect) {
        boolean isInCellRenderer;
        if (!SubstanceLookAndFeel.isCurrentLookAndFeel()) {
            return;
        }
        boolean bl = isInCellRenderer = SwingUtilities.getAncestorOfClass(CellRendererPane.class, c2) != null;
        if (!c2.isShowing() && !isInCellRenderer) {
            return;
        }
        Graphics2D graphics = (Graphics2D)g2.create();
        graphics.setComposite(LafWidgetUtilities.getAlphaComposite((Component)c2, g2));
        graphics.setColor(fillColor);
        graphics.fillRect(rect.x, rect.y, rect.width, rect.height);
        graphics.setComposite(LafWidgetUtilities.getAlphaComposite(c2, 1.0f, g2));
        SubstanceWatermark watermark = SubstanceCoreUtilities.getSkin(c2).getWatermark();
        if (watermark != null && !isInCellRenderer && c2.isShowing() && SubstanceCoreUtilities.toDrawWatermark(c2)) {
            watermark.drawWatermarkImage(graphics, c2, rect.x, rect.y, rect.width, rect.height);
        }
        graphics.dispose();
    }
}

