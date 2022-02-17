/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.painter;

import java.awt.Component;
import java.awt.Point;
import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.internal.painter.DecorationPainterUtils;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;

public class SubstancePainterUtils {
    public static Point getOffsetInRootPaneCoords(Component comp) {
        JRootPane rootPane = SwingUtilities.getRootPane(comp);
        int dx = 0;
        int dy = 0;
        JComponent titlePane = null;
        if (rootPane != null && (titlePane = SubstanceCoreUtilities.getTitlePane(rootPane)) != null) {
            if (comp.isShowing() && titlePane.isShowing()) {
                dx += comp.getLocationOnScreen().x - titlePane.getLocationOnScreen().x;
                dy += comp.getLocationOnScreen().y - titlePane.getLocationOnScreen().y;
            } else {
                Component c2;
                dx = 0;
                dy = 0;
                for (c2 = comp; c2 != rootPane; c2 = c2.getParent()) {
                    dx += c2.getX();
                    dy += c2.getY();
                }
                c2 = titlePane;
                if (c2 != null && c2.getParent() != null) {
                    while (c2 != rootPane) {
                        dx -= c2.getX();
                        dy -= c2.getY();
                        c2 = c2.getParent();
                    }
                }
            }
        }
        return new Point(dx, dy);
    }

    public static Component getTopMostParentWithDecorationAreaType(Component comp, DecorationAreaType type) {
        Component c2;
        Component topMostWithSameDecorationAreaType = c2 = comp;
        while (c2 != null) {
            if (DecorationPainterUtils.getImmediateDecorationType(c2) == type) {
                topMostWithSameDecorationAreaType = c2;
            }
            c2 = c2.getParent();
        }
        return topMostWithSameDecorationAreaType;
    }
}

