/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.desktop;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import org.pushingpixels.lafwidget.LafWidgetAdapter;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.LafWidgetSupport;
import org.pushingpixels.lafwidget.LafWidgetUtilities;

public class DesktopIconHoverPreviewWidget
extends LafWidgetAdapter<JInternalFrame.JDesktopIcon> {
    protected JComponent compToHover;
    protected PropertyChangeListener internalFramePropertyListener;
    private BufferedImage snapshot;
    private JWindow previewWindow;
    private boolean isInDrag;
    protected TitleMouseHandler titleMouseHandler;

    @Override
    public void installComponents() {
        this.previewWindow = new JWindow();
        this.previewWindow.getContentPane().setLayout(new BorderLayout());
    }

    @Override
    public void installListeners() {
        this.internalFramePropertyListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("ancestor".equals(evt.getPropertyName())) {
                    DesktopIconHoverPreviewWidget.this.updateSnapshot(((JInternalFrame.JDesktopIcon)DesktopIconHoverPreviewWidget.this.jcomp).getInternalFrame());
                }
            }
        };
        ((JInternalFrame.JDesktopIcon)this.jcomp).getInternalFrame().addPropertyChangeListener(this.internalFramePropertyListener);
        this.titleMouseHandler = new TitleMouseHandler();
        LafWidgetSupport lafSupport = LafWidgetRepository.getRepository().getLafSupport();
        this.compToHover = lafSupport.getComponentForHover((JInternalFrame.JDesktopIcon)this.jcomp);
        if (this.compToHover != null) {
            this.compToHover.addMouseMotionListener(this.titleMouseHandler);
            this.compToHover.addMouseListener(this.titleMouseHandler);
        }
    }

    @Override
    public void uninstallListeners() {
        ((JInternalFrame.JDesktopIcon)this.jcomp).getInternalFrame().removePropertyChangeListener(this.internalFramePropertyListener);
        this.internalFramePropertyListener = null;
        if (this.compToHover != null) {
            this.compToHover.removeMouseMotionListener(this.titleMouseHandler);
            this.compToHover.removeMouseListener(this.titleMouseHandler);
        }
        this.titleMouseHandler = null;
    }

    private void syncPreviewWindow(boolean toShow) {
        if (toShow) {
            int x2 = ((JInternalFrame.JDesktopIcon)this.jcomp).getLocationOnScreen().x;
            int y2 = ((JInternalFrame.JDesktopIcon)this.jcomp).getLocationOnScreen().y;
            this.previewWindow.setLocation(x2, y2 - this.previewWindow.getHeight());
        }
    }

    private void updateSnapshot(JInternalFrame frame) {
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
            HashMap<Component, Boolean> dbSnapshot = new HashMap<Component, Boolean>();
            LafWidgetUtilities.makePreviewable(frame, dbSnapshot);
            frame.paint(tempCanvasGraphics);
            LafWidgetUtilities.restorePreviewable(frame, dbSnapshot);
            int maxHeight = maxWidth = UIManager.getInt("DesktopIcon.width");
            double coef = Math.min((double)maxWidth / (double)frameWidth, (double)maxHeight / (double)frameHeight);
            if (coef < 1.0) {
                BufferedImage scaledDown;
                int sdWidth = (int)(coef * (double)frameWidth);
                this.snapshot = scaledDown = LafWidgetUtilities.createThumbnail(tempCanvas, sdWidth);
            } else {
                this.snapshot = tempCanvas;
            }
        }
    }

    public synchronized BufferedImage getSnapshot(JInternalFrame frame) {
        return this.snapshot;
    }

    @Override
    public boolean requiresCustomLafSupport() {
        return false;
    }

    protected class TitleMouseHandler
    extends MouseInputAdapter {
        protected TitleMouseHandler() {
        }

        @Override
        public void mouseEntered(MouseEvent e2) {
            if (DesktopIconHoverPreviewWidget.this.isInDrag) {
                return;
            }
            BufferedImage previewImage = DesktopIconHoverPreviewWidget.this.snapshot;
            if (previewImage != null) {
                DesktopIconHoverPreviewWidget.this.previewWindow.getContentPane().removeAll();
                JLabel previewLabel = new JLabel(new ImageIcon(previewImage));
                DesktopIconHoverPreviewWidget.this.previewWindow.getContentPane().add((Component)previewLabel, "Center");
                DesktopIconHoverPreviewWidget.this.previewWindow.setSize(previewImage.getWidth(), previewImage.getHeight());
                DesktopIconHoverPreviewWidget.this.syncPreviewWindow(true);
                DesktopIconHoverPreviewWidget.this.previewWindow.setVisible(true);
            }
        }

        @Override
        public void mouseExited(MouseEvent e2) {
            DesktopIconHoverPreviewWidget.this.isInDrag = false;
            DesktopIconHoverPreviewWidget.this.previewWindow.dispose();
        }

        @Override
        public void mousePressed(MouseEvent e2) {
            DesktopIconHoverPreviewWidget.this.previewWindow.dispose();
        }

        @Override
        public void mouseReleased(MouseEvent e2) {
            DesktopIconHoverPreviewWidget.this.isInDrag = false;
            DesktopIconHoverPreviewWidget.this.syncPreviewWindow(true);
            DesktopIconHoverPreviewWidget.this.previewWindow.setVisible(true);
        }

        @Override
        public void mouseDragged(MouseEvent e2) {
            DesktopIconHoverPreviewWidget.this.isInDrag = true;
            if (DesktopIconHoverPreviewWidget.this.previewWindow.isVisible()) {
                DesktopIconHoverPreviewWidget.this.syncPreviewWindow(false);
                DesktopIconHoverPreviewWidget.this.previewWindow.dispose();
            }
        }
    }
}

