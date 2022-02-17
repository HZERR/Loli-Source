/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.tabbed;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JWindow;
import javax.swing.Timer;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.LafWidgetSupport;
import org.pushingpixels.lafwidget.LafWidgetUtilities2;
import org.pushingpixels.lafwidget.animation.AnimationConfigurationManager;
import org.pushingpixels.lafwidget.tabbed.TabPreviewPainter;
import org.pushingpixels.lafwidget.tabbed.TabPreviewThread;
import org.pushingpixels.trident.Timeline;

public class TabPreviewWindow
extends JWindow
implements ActionListener {
    protected static TabPreviewWindow instance;
    protected static TabPreviewThread.TabPreviewInfo currTabPreviewInfo;
    protected static Timer currTabPreviewTimer;

    public static synchronized TabPreviewWindow getInstance() {
        if (instance == null) {
            instance = new TabPreviewWindow();
            instance.setLayout(new BorderLayout());
        }
        return instance;
    }

    public synchronized void postPreviewRequest(JTabbedPane tabPane, int tabIndex) {
        TabPreviewPainter previewPainter = LafWidgetUtilities2.getTabPreviewPainter(tabPane);
        if (previewPainter == null || !previewPainter.hasPreviewWindow(tabPane, tabIndex)) {
            return;
        }
        if (currTabPreviewInfo != null && TabPreviewWindow.currTabPreviewInfo.tabPane == tabPane && TabPreviewWindow.currTabPreviewInfo.tabIndexToPreview == tabIndex) {
            return;
        }
        if (currTabPreviewTimer != null && currTabPreviewTimer.isRunning()) {
            currTabPreviewTimer.stop();
        }
        Dimension previewDim = previewPainter.getPreviewWindowDimension(tabPane, tabIndex);
        int pWidth = previewDim.width;
        int pHeight = previewDim.height;
        Component tabComponent = tabPane.getComponentAt(tabIndex);
        if (tabComponent != null) {
            int height;
            double pRatio = (double)previewDim.width / (double)previewDim.height;
            int width = tabComponent.getWidth();
            double ratio = (double)width / (double)(height = tabComponent.getHeight());
            if (pRatio > ratio) {
                pWidth = (int)((double)pHeight * ratio);
            } else {
                pHeight = (int)((double)pWidth / ratio);
            }
        }
        currTabPreviewInfo = new TabPreviewThread.TabPreviewInfo();
        TabPreviewWindow.currTabPreviewInfo.tabPane = tabPane;
        TabPreviewWindow.currTabPreviewInfo.tabIndexToPreview = tabIndex;
        currTabPreviewInfo.setPreviewWidth(pWidth);
        currTabPreviewInfo.setPreviewHeight(pHeight);
        TabPreviewWindow.currTabPreviewInfo.initiator = tabPane;
        TabPreviewWindow.currTabPreviewInfo.previewCallback = new TabPreviewThread.TabPreviewCallback(){

            @Override
            public void start(JTabbedPane tabPane, int tabCount, TabPreviewThread.TabPreviewInfo tabPreviewInfo) {
            }

            @Override
            public void offer(JTabbedPane tabPane, int tabIndex, BufferedImage componentSnap) {
                if (currTabPreviewInfo == null) {
                    return;
                }
                if (tabPane != TabPreviewWindow.currTabPreviewInfo.tabPane || tabIndex != TabPreviewWindow.currTabPreviewInfo.tabIndexToPreview) {
                    return;
                }
                Rectangle previewScreenRectangle = TabPreviewWindow.this.getPreviewWindowScreenRect(tabPane, tabIndex, currTabPreviewInfo.getPreviewWidth(), currTabPreviewInfo.getPreviewHeight());
                TabPreviewWindow.this.getContentPane().removeAll();
                final PreviewLabel previewLabel = new PreviewLabel((Icon)new ImageIcon(componentSnap));
                TabPreviewWindow.this.addComponentListener(new ComponentAdapter(){

                    @Override
                    public void componentShown(ComponentEvent e2) {
                        previewLabel.setVisible(true);
                        Timeline timeline = new Timeline(previewLabel);
                        AnimationConfigurationManager.getInstance().configureTimeline(timeline);
                        timeline.addPropertyToInterpolate("alpha", Float.valueOf(0.0f), Float.valueOf(1.0f));
                        timeline.play();
                    }
                });
                TabPreviewWindow.this.getContentPane().add((Component)previewLabel, "Center");
                TabPreviewWindow.this.setSize(previewScreenRectangle.width, previewScreenRectangle.height);
                TabPreviewWindow.this.setLocation(previewScreenRectangle.x, previewScreenRectangle.y);
                previewLabel.setVisible(false);
                TabPreviewWindow.this.setVisible(true);
            }
        };
        int extraDelay = previewPainter.getPreviewWindowExtraDelay(tabPane, tabIndex);
        if (extraDelay < 0) {
            throw new IllegalArgumentException("Extra delay for tab preview must be non-negative");
        }
        currTabPreviewTimer = new Timer(2000 + extraDelay, this);
        currTabPreviewTimer.setRepeats(false);
        currTabPreviewTimer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e2) {
        if (currTabPreviewInfo == null) {
            return;
        }
        TabPreviewPainter previewPainter = LafWidgetUtilities2.getTabPreviewPainter(TabPreviewWindow.currTabPreviewInfo.tabPane);
        if (previewPainter == null || !previewPainter.hasPreviewWindow(TabPreviewWindow.currTabPreviewInfo.tabPane, TabPreviewWindow.currTabPreviewInfo.tabIndexToPreview)) {
            return;
        }
        TabPreviewThread.getInstance().queueTabPreviewRequest(currTabPreviewInfo);
    }

    protected Rectangle getPreviewWindowScreenRect(JTabbedPane tabPane, int tabIndex, int pWidth, int pHeight) {
        LafWidgetSupport lafSupport = LafWidgetRepository.getRepository().getLafSupport();
        Rectangle relative = lafSupport.getTabRectangle(tabPane, tabIndex);
        if (relative == null) {
            return null;
        }
        Rectangle result = new Rectangle(pWidth, pHeight);
        boolean ltr = tabPane.getComponentOrientation().isLeftToRight();
        if (ltr) {
            if (tabPane.getTabPlacement() != 3) {
                result.setLocation(relative.x, relative.y + relative.height);
            } else {
                result.setLocation(relative.x, relative.y - pHeight);
            }
        } else if (tabPane.getTabPlacement() != 3) {
            result.setLocation(relative.x + relative.width - pWidth, relative.y + relative.height);
        } else {
            result.setLocation(relative.x + relative.width - pWidth, relative.y - pHeight);
        }
        int dx = tabPane.getLocationOnScreen().x;
        int dy = tabPane.getLocationOnScreen().y;
        result.x += dx;
        result.y += dy;
        Rectangle virtualBounds = new Rectangle();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gds = ge.getScreenDevices();
        for (int i2 = 0; i2 < gds.length; ++i2) {
            GraphicsDevice gd = gds[i2];
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            virtualBounds = virtualBounds.union(gc.getBounds());
        }
        if (result.x + result.width > virtualBounds.width - 1) {
            result.x -= result.x + result.width - virtualBounds.width + 1;
        }
        if (result.y + result.height > virtualBounds.height - 1) {
            result.y -= result.y + result.height - virtualBounds.height + 1;
        }
        if (result.x < virtualBounds.x) {
            result.x = virtualBounds.x + 1;
        }
        if (result.y < virtualBounds.y) {
            result.y = virtualBounds.y + 1;
        }
        return result;
    }

    public static synchronized void cancelPreviewRequest() {
        currTabPreviewInfo = null;
        if (currTabPreviewTimer != null && currTabPreviewTimer.isRunning()) {
            currTabPreviewTimer.stop();
            currTabPreviewTimer = null;
        }
        if (instance != null) {
            instance.dispose();
        }
    }

    public static final class PreviewLabel
    extends JLabel {
        float alpha = 0.0f;

        private PreviewLabel(Icon image) {
            super(image);
        }

        public void setAlpha(float alpha) {
            this.alpha = alpha;
            this.repaint();
        }

        @Override
        protected void paintComponent(Graphics g2) {
            Graphics2D g22 = (Graphics2D)g2.create();
            g22.setComposite(AlphaComposite.SrcOver.derive(this.alpha));
            super.paintComponent(g22);
            g22.dispose();
        }
    }
}

