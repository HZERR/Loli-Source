/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.preview;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.util.WeakHashMap;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.lafwidget.preview.DefaultPreviewPainter;

public class InternalFramePreviewPainter
extends DefaultPreviewPainter {
    private static WeakHashMap snapshots = new WeakHashMap();

    public static void refreshSnaphost(JInternalFrame frame) {
        if (!frame.isShowing()) {
            return;
        }
        int frameWidth = frame.getWidth();
        int frameHeight = frame.getHeight();
        int dx = 0;
        int dy = 0;
        Border internalFrameBorder = UIManager.getBorder("InternalFrame.border");
        Insets borderInsets = internalFrameBorder.getBorderInsets(frame);
        dx += borderInsets.left;
        dy += borderInsets.top;
        frameWidth -= borderInsets.left + borderInsets.right;
        frameHeight -= borderInsets.top + borderInsets.bottom;
        BasicInternalFrameUI frameUI = (BasicInternalFrameUI)frame.getUI();
        JComponent frameTitlePane = frameUI.getNorthPane();
        if (frameTitlePane != null) {
            dy += frameTitlePane.getHeight();
            frameHeight -= frameTitlePane.getHeight();
        }
        if (frameWidth > 0 && frameHeight > 0) {
            int maxWidth;
            BufferedImage tempCanvas = new BufferedImage(frameWidth, frameHeight, 2);
            Graphics tempCanvasGraphics = tempCanvas.getGraphics();
            tempCanvasGraphics.translate(-dx, -dy);
            frame.paint(tempCanvasGraphics);
            int maxHeight = maxWidth = UIManager.getInt("DesktopIcon.width");
            double coef = Math.min((double)maxWidth / (double)frameWidth, (double)maxHeight / (double)frameHeight);
            if (coef < 1.0) {
                int sdWidth = (int)(coef * (double)frameWidth);
                BufferedImage scaledDown = LafWidgetUtilities.createThumbnail(tempCanvas, sdWidth);
                snapshots.put(frame, scaledDown);
            } else {
                snapshots.put(frame, tempCanvas);
            }
        }
    }

    @Override
    public void previewComponent(Container parent, Component component, int componentIndex, Graphics g2, int x2, int y2, int w2, int h2) {
        BufferedImage preview = (BufferedImage)snapshots.get(component);
        if (preview != null) {
            g2.drawImage(preview, x2, y2, null);
        }
    }

    @Override
    public Dimension getPreviewWindowDimension(Container parent, Component component, int componentIndex) {
        return new Dimension(UIManager.getInt("DesktopIcon.width"), UIManager.getInt("DesktopIcon.width"));
    }
}

