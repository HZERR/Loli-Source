/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.painter;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.painter.decoration.SubstanceDecorationPainter;
import org.pushingpixels.substance.api.watermark.SubstanceWatermark;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;

public class DecorationPainterUtils {
    private static final String DECORATION_AREA_TYPE = "substancelaf.internal.painter.decorationAreaType";

    public static void setDecorationType(JComponent comp, DecorationAreaType type) {
        comp.putClientProperty(DECORATION_AREA_TYPE, type);
    }

    public static void clearDecorationType(JComponent comp) {
        if (comp != null) {
            comp.putClientProperty(DECORATION_AREA_TYPE, null);
        }
    }

    public static DecorationAreaType getDecorationType(Component comp) {
        for (Component c2 = comp; c2 != null; c2 = c2.getParent()) {
            JComponent jc;
            Object prop;
            if (!(c2 instanceof JComponent) || !((prop = (jc = (JComponent)c2).getClientProperty(DECORATION_AREA_TYPE)) instanceof DecorationAreaType)) continue;
            return (DecorationAreaType)prop;
        }
        return DecorationAreaType.NONE;
    }

    public static DecorationAreaType getImmediateDecorationType(Component comp) {
        JComponent jc;
        Object prop;
        Component c2 = comp;
        if (c2 instanceof JComponent && (prop = (jc = (JComponent)c2).getClientProperty(DECORATION_AREA_TYPE)) instanceof DecorationAreaType) {
            return (DecorationAreaType)prop;
        }
        return null;
    }

    public static void paintDecorationBackground(Graphics g2, Component c2, boolean force) {
        DecorationAreaType decorationType = SubstanceLookAndFeel.getDecorationType(c2);
        DecorationPainterUtils.paintDecorationBackground(g2, c2, decorationType, force);
    }

    public static void paintDecorationBackground(Graphics g2, Component c2, DecorationAreaType decorationType, boolean force) {
        boolean isInCellRenderer = SwingUtilities.getAncestorOfClass(CellRendererPane.class, c2) != null;
        boolean isPreviewMode = false;
        if (c2 instanceof JComponent) {
            isPreviewMode = Boolean.TRUE.equals(((JComponent)c2).getClientProperty("lafwidgets.internal.previewMode"));
        }
        if (!(force || isPreviewMode || c2.isShowing() || isInCellRenderer)) {
            return;
        }
        if (c2.getHeight() == 0 || c2.getWidth() == 0) {
            return;
        }
        SubstanceSkin skin = SubstanceCoreUtilities.getSkin(c2);
        SubstanceDecorationPainter painter = skin.getDecorationPainter();
        Graphics2D g2d = (Graphics2D)g2.create();
        painter.paintDecorationArea(g2d, c2, decorationType, c2.getWidth(), c2.getHeight(), skin);
        SubstanceWatermark watermark = SubstanceCoreUtilities.getSkin(c2).getWatermark();
        if (watermark != null && !isPreviewMode && !isInCellRenderer && c2.isShowing() && SubstanceCoreUtilities.toDrawWatermark(c2)) {
            watermark.drawWatermarkImage(g2d, c2, 0, 0, c2.getWidth(), c2.getHeight());
            g2d.setComposite(LafWidgetUtilities.getAlphaComposite(c2, 0.5f, g2));
            painter.paintDecorationArea(g2d, c2, decorationType, c2.getWidth(), c2.getHeight(), skin);
        }
        g2d.dispose();
    }
}

