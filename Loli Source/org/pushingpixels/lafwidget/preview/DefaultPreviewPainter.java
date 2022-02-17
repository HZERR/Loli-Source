/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.preview;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.swing.JViewport;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.lafwidget.preview.PreviewPainter;

public class DefaultPreviewPainter
extends PreviewPainter {
    @Override
    public boolean hasPreview(Container parent, Component component, int componentIndex) {
        return component != null;
    }

    @Override
    public void previewComponent(Container parent, Component component, int componentIndex, Graphics g2, int x2, int y2, int w2, int h2) {
        if (component == null) {
            return;
        }
        int compWidth = component.getWidth();
        int compHeight = component.getHeight();
        if (compWidth > 0 && compHeight > 0) {
            BufferedImage tempCanvas = new BufferedImage(compWidth, compHeight, 2);
            Graphics tempCanvasGraphics = tempCanvas.getGraphics();
            component.paint(tempCanvasGraphics);
            double coef = Math.min((double)w2 / (double)compWidth, (double)h2 / (double)compHeight);
            if (coef < 1.0) {
                int sdWidth = (int)(coef * (double)compWidth);
                int sdHeight = (int)(coef * (double)compHeight);
                int dx = x2 + (w2 - sdWidth) / 2;
                int dy = y2 + (h2 - sdHeight) / 2;
                g2.drawImage(LafWidgetUtilities.createThumbnail(tempCanvas, sdWidth), dx, dy, null);
            } else {
                g2.drawImage(tempCanvas, x2, y2, null);
            }
        }
    }

    @Override
    public boolean hasPreviewWindow(Container parent, Component component, int componentIndex) {
        return true;
    }

    @Override
    public Dimension getPreviewWindowDimension(Container parent, Component component, int componentIndex) {
        Dimension superResult = super.getPreviewWindowDimension(parent, component, componentIndex);
        if (parent instanceof JViewport) {
            Rectangle viewportRect = ((JViewport)parent).getViewRect();
            int width = Math.min(viewportRect.width / 3, superResult.width);
            int height = Math.min(viewportRect.height / 3, superResult.height);
            return new Dimension(width, height);
        }
        return superResult;
    }
}

