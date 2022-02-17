/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.painter;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.painter.overlay.SubstanceOverlayPainter;

public class OverlayPainterUtils {
    public static void paintOverlays(Graphics g2, Component c2, SubstanceSkin skin, DecorationAreaType decorationAreaType) {
        List<SubstanceOverlayPainter> overlayPainters = skin.getOverlayPainters(decorationAreaType);
        if (overlayPainters.size() == 0) {
            return;
        }
        for (SubstanceOverlayPainter overlayPainter : overlayPainters) {
            Graphics2D g2d = (Graphics2D)g2.create();
            overlayPainter.paintOverlay(g2d, c2, decorationAreaType, c2.getWidth(), c2.getHeight(), skin);
            g2d.dispose();
        }
    }
}

