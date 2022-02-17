/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.tabbed;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.JTabbedPane;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import org.pushingpixels.lafwidget.LafWidgetUtilities2;
import org.pushingpixels.lafwidget.animation.AnimationConfigurationManager;
import org.pushingpixels.lafwidget.tabbed.TabPagerMouseWheelListener;
import org.pushingpixels.lafwidget.tabbed.TabPreviewControl;
import org.pushingpixels.lafwidget.tabbed.TabPreviewPainter;
import org.pushingpixels.lafwidget.tabbed.TabPreviewThread;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.callback.UIThreadTimelineCallbackAdapter;

public class TabPagerManager {
    protected static TabPagerManager instance;
    protected JTabbedPane currTabbedPane;
    protected int currTabIndex;
    protected int nextTabIndex;
    protected int prevTabIndex;
    protected JWindow prevTabWindow;
    protected JWindow currTabWindow = new JWindow();
    protected JWindow nextTabWindow;
    protected boolean isVisible;

    public static synchronized TabPagerManager getPager() {
        if (instance == null) {
            instance = new TabPagerManager();
        }
        return instance;
    }

    private TabPagerManager() {
        this.currTabWindow.getContentPane().setLayout(new BorderLayout());
        this.currTabWindow.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e2) {
                SwingUtilities.invokeLater(new Runnable(){

                    @Override
                    public void run() {
                        TabPreviewPainter tpp = LafWidgetUtilities2.getTabPreviewPainter(TabPagerManager.this.currTabbedPane);
                        if (tpp.isSensitiveToEvents(TabPagerManager.this.currTabbedPane, TabPagerManager.this.currTabIndex)) {
                            TabPagerManager.this.hide();
                            TabPagerManager.this.currTabbedPane.setSelectedIndex(TabPagerManager.this.currTabIndex);
                        }
                    }
                });
            }
        });
        this.currTabWindow.addMouseWheelListener(new TabPagerMouseWheelListener());
        this.prevTabWindow = new JWindow();
        this.prevTabWindow.getContentPane().setLayout(new BorderLayout());
        this.prevTabWindow.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e2) {
                SwingUtilities.invokeLater(new Runnable(){

                    @Override
                    public void run() {
                        TabPreviewPainter tpp = LafWidgetUtilities2.getTabPreviewPainter(TabPagerManager.this.currTabbedPane);
                        if (tpp.isSensitiveToEvents(TabPagerManager.this.currTabbedPane, TabPagerManager.this.prevTabIndex)) {
                            TabPagerManager.this.hide();
                            TabPagerManager.this.currTabbedPane.setSelectedIndex(TabPagerManager.this.prevTabIndex);
                        }
                    }
                });
            }
        });
        this.prevTabWindow.addMouseWheelListener(new TabPagerMouseWheelListener());
        this.nextTabWindow = new JWindow();
        this.nextTabWindow.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e2) {
                SwingUtilities.invokeLater(new Runnable(){

                    @Override
                    public void run() {
                        TabPreviewPainter tpp = LafWidgetUtilities2.getTabPreviewPainter(TabPagerManager.this.currTabbedPane);
                        if (tpp.isSensitiveToEvents(TabPagerManager.this.currTabbedPane, TabPagerManager.this.nextTabIndex)) {
                            TabPagerManager.this.hide();
                            TabPagerManager.this.currTabbedPane.setSelectedIndex(TabPagerManager.this.nextTabIndex);
                        }
                    }
                });
            }
        });
        this.nextTabWindow.addMouseWheelListener(new TabPagerMouseWheelListener());
        this.recomputeBounds();
        this.isVisible = false;
    }

    private void recomputeBounds() {
        Rectangle virtualBounds = new Rectangle();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gds = ge.getScreenDevices();
        for (int i2 = 0; i2 < gds.length; ++i2) {
            GraphicsDevice gd = gds[i2];
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            virtualBounds = virtualBounds.union(gc.getBounds());
        }
        int screenWidth = virtualBounds.width;
        int screenHeight = virtualBounds.height;
        int currWidth = screenWidth / 3;
        int currHeight = screenHeight / 3;
        this.currTabWindow.setSize(currWidth, currHeight);
        this.currTabWindow.setLocation(currWidth + virtualBounds.x, currHeight);
        int smallWidth = 2 * screenWidth / 9;
        int smallHeight = 2 * screenHeight / 9;
        this.prevTabWindow.setSize(smallWidth, smallHeight);
        this.prevTabWindow.setLocation(screenWidth / 18 + virtualBounds.x, 7 * screenHeight / 18);
        this.nextTabWindow.getContentPane().setLayout(new BorderLayout());
        this.nextTabWindow.setSize(smallWidth, smallHeight);
        this.nextTabWindow.setLocation(13 * screenWidth / 18 + virtualBounds.x, 7 * screenHeight / 18);
    }

    private void setTabbedPane(JTabbedPane jtp) {
        if (this.currTabbedPane == jtp) {
            return;
        }
        this.currTabbedPane = jtp;
    }

    public synchronized void page(JTabbedPane tabbedPane, boolean isForward) {
        this.setTabbedPane(tabbedPane);
        if (!this.isVisible) {
            this.recomputeBounds();
            this.currTabWindow.setVisible(true);
            this.prevTabWindow.setVisible(true);
            this.nextTabWindow.setVisible(true);
            this.isVisible = true;
            this.currTabIndex = this.currTabbedPane.getSelectedIndex();
        }
        int delta = isForward ? 1 : -1;
        this.currTabIndex += delta;
        if (this.currTabIndex == this.currTabbedPane.getTabCount()) {
            this.currTabIndex = 0;
        }
        if (this.currTabIndex == -1) {
            this.currTabIndex = this.currTabbedPane.getTabCount() - 1;
        }
        this.nextTabIndex = this.currTabIndex + 1;
        this.prevTabIndex = this.currTabIndex - 1;
        if (this.nextTabIndex == this.currTabbedPane.getTabCount()) {
            this.nextTabIndex = 0;
        }
        if (this.prevTabIndex == -1) {
            this.prevTabIndex = this.currTabbedPane.getTabCount() - 1;
        }
        TabPreviewThread.TabPreviewInfo currTabPreviewInfo = new TabPreviewThread.TabPreviewInfo();
        currTabPreviewInfo.tabPane = this.currTabbedPane;
        currTabPreviewInfo.tabIndexToPreview = this.currTabIndex;
        currTabPreviewInfo.setPreviewWidth(this.currTabWindow.getWidth() - 4);
        currTabPreviewInfo.setPreviewHeight(this.currTabWindow.getHeight() - 20);
        currTabPreviewInfo.previewCallback = new TabPagerPreviewCallback(this.currTabWindow, this.currTabbedPane, this.currTabIndex);
        currTabPreviewInfo.initiator = this;
        TabPreviewPainter previewPainter = LafWidgetUtilities2.getTabPreviewPainter(currTabPreviewInfo.tabPane);
        if (previewPainter != null && previewPainter.hasPreviewWindow(this.currTabbedPane, this.currTabIndex)) {
            TabPreviewThread.getInstance().queueTabPreviewRequest(currTabPreviewInfo);
        }
        TabPreviewThread.TabPreviewInfo prevTabPreviewInfo = new TabPreviewThread.TabPreviewInfo();
        prevTabPreviewInfo.tabPane = this.currTabbedPane;
        prevTabPreviewInfo.tabIndexToPreview = this.prevTabIndex;
        prevTabPreviewInfo.setPreviewWidth(this.prevTabWindow.getWidth() - 4);
        prevTabPreviewInfo.setPreviewHeight(this.prevTabWindow.getHeight() - 20);
        prevTabPreviewInfo.previewCallback = new TabPagerPreviewCallback(this.prevTabWindow, this.currTabbedPane, this.prevTabIndex);
        prevTabPreviewInfo.initiator = this;
        if (previewPainter != null && previewPainter.hasPreviewWindow(this.currTabbedPane, this.prevTabIndex)) {
            TabPreviewThread.getInstance().queueTabPreviewRequest(prevTabPreviewInfo);
        }
        TabPreviewThread.TabPreviewInfo nextTabPreviewInfo = new TabPreviewThread.TabPreviewInfo();
        nextTabPreviewInfo.tabPane = this.currTabbedPane;
        nextTabPreviewInfo.tabIndexToPreview = this.nextTabIndex;
        nextTabPreviewInfo.setPreviewWidth(this.nextTabWindow.getWidth() - 4);
        nextTabPreviewInfo.setPreviewHeight(this.nextTabWindow.getHeight() - 20);
        nextTabPreviewInfo.previewCallback = new TabPagerPreviewCallback(this.nextTabWindow, this.currTabbedPane, this.nextTabIndex);
        nextTabPreviewInfo.initiator = this;
        if (previewPainter != null && previewPainter.hasPreviewWindow(this.currTabbedPane, this.nextTabIndex)) {
            TabPreviewThread.getInstance().queueTabPreviewRequest(nextTabPreviewInfo);
        }
    }

    public void page(boolean isForward) {
        if (this.currTabbedPane == null) {
            return;
        }
        this.page(this.currTabbedPane, isForward);
    }

    public boolean isVisible() {
        return this.isVisible;
    }

    public synchronized int hide() {
        int result = this.isVisible ? this.currTabIndex : -1;
        Point currWindowLocation = this.currTabWindow.getLocation();
        Dimension currWindowSize = this.currTabWindow.getSize();
        Point nextWindowLocation = this.nextTabWindow.getLocation();
        Dimension nextWindowSize = this.nextTabWindow.getSize();
        Point prevWindowLocation = this.prevTabWindow.getLocation();
        Dimension prevWindowSize = this.prevTabWindow.getSize();
        Timeline hideTabPagerTimeline = new Timeline(this.currTabbedPane);
        AnimationConfigurationManager.getInstance().configureTimeline(hideTabPagerTimeline);
        hideTabPagerTimeline.addPropertyToInterpolate(Timeline.property("bounds").on(this.currTabWindow).from(new Rectangle(currWindowLocation, currWindowSize)).to(new Rectangle(currWindowLocation.x + currWindowSize.width / 2, currWindowLocation.y + currWindowSize.height / 2, 0, 0)));
        hideTabPagerTimeline.addPropertyToInterpolate(Timeline.property("bounds").on(this.prevTabWindow).from(new Rectangle(prevWindowLocation, prevWindowSize)).to(new Rectangle(prevWindowLocation.x + prevWindowSize.width / 2, prevWindowLocation.y + prevWindowSize.height / 2, 0, 0)));
        hideTabPagerTimeline.addPropertyToInterpolate(Timeline.property("bounds").on(this.nextTabWindow).from(new Rectangle(nextWindowLocation, nextWindowSize)).to(new Rectangle(nextWindowLocation.x + nextWindowSize.width / 2, nextWindowLocation.y + nextWindowSize.height / 2, 0, 0)));
        hideTabPagerTimeline.addCallback(new UIThreadTimelineCallbackAdapter(){

            @Override
            public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
                if (oldState == Timeline.TimelineState.DONE && newState == Timeline.TimelineState.IDLE) {
                    TabPagerManager.this.currTabWindow.setVisible(false);
                    TabPagerManager.this.currTabWindow.dispose();
                    TabPagerManager.this.prevTabWindow.setVisible(false);
                    TabPagerManager.this.prevTabWindow.dispose();
                    TabPagerManager.this.nextTabWindow.setVisible(false);
                    TabPagerManager.this.nextTabWindow.dispose();
                }
            }
        });
        hideTabPagerTimeline.play();
        this.isVisible = false;
        return result;
    }

    public static void reset() {
    }

    public class TabPagerPreviewCallback
    implements TabPreviewThread.TabPreviewCallback {
        private JWindow previewWindow;
        private TabPreviewControl previewControl;

        public TabPagerPreviewCallback(JWindow previewWindow, JTabbedPane tabPane, int tabIndex) {
            this.previewWindow = previewWindow;
            this.previewControl = new TabPreviewControl(tabPane, tabIndex);
            this.previewWindow.getContentPane().removeAll();
            this.previewWindow.getContentPane().add((Component)this.previewControl, "Center");
            this.previewWindow.getContentPane().doLayout();
            this.previewControl.doLayout();
        }

        @Override
        public void start(JTabbedPane tabPane, int tabCount, TabPreviewThread.TabPreviewInfo tabPreviewInfo) {
        }

        @Override
        public void offer(JTabbedPane tabPane, int tabIndex, BufferedImage componentSnap) {
            if (TabPagerManager.this.currTabbedPane != tabPane) {
                return;
            }
            if (!this.previewWindow.isVisible()) {
                return;
            }
            this.previewControl.setPreviewImage(componentSnap, true);
        }
    }
}

