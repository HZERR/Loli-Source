/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.tabbed;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JTabbedPane;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.lafwidget.tabbed.TabPreviewPainter;

public class DefaultTabPreviewPainter
extends TabPreviewPainter {
    @Override
    public boolean hasPreview(JTabbedPane tabPane, int tabIndex) {
        return tabPane.getComponentAt(tabIndex) != null;
    }

    @Override
    public boolean isSensitiveToEvents(JTabbedPane tabPane, int tabIndex) {
        return tabPane.isEnabledAt(tabIndex);
    }

    @Override
    public void previewTab(JTabbedPane tabPane, int tabIndex, Graphics g2, int x2, int y2, int w2, int h2) {
        Component tabComponent = tabPane.getComponentAt(tabIndex);
        if (tabComponent == null) {
            return;
        }
        int compWidth = tabComponent.getWidth();
        int compHeight = tabComponent.getHeight();
        if (compWidth > 0 && compHeight > 0) {
            BufferedImage tempCanvas = new BufferedImage(compWidth, compHeight, 2);
            Graphics tempCanvasGraphics = tempCanvas.getGraphics();
            tabComponent.paint(tempCanvasGraphics);
            double coef = Math.min((double)w2 / (double)compWidth, (double)h2 / (double)compHeight);
            Graphics2D g22 = (Graphics2D)g2.create();
            if (!tabPane.isEnabledAt(tabIndex)) {
                g22.setComposite(AlphaComposite.getInstance(3, 0.5f));
            }
            if (coef < 1.0) {
                int sdWidth = (int)(coef * (double)compWidth);
                int sdHeight = (int)(coef * (double)compHeight);
                int dx = (w2 - sdWidth) / 2;
                int dy = (h2 - sdHeight) / 2;
                g22.drawImage((Image)LafWidgetUtilities.createThumbnail(tempCanvas, sdWidth), dx, dy, null);
            } else {
                g22.drawImage((Image)tempCanvas, 0, 0, null);
            }
            g22.dispose();
        }
    }

    @Override
    public boolean hasPreviewWindow(JTabbedPane tabPane, int tabIndex) {
        return true;
    }

    @Override
    public boolean hasOverviewDialog(JTabbedPane tabPane) {
        return true;
    }
}

