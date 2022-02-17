/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.tabbed;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.lafwidget.LafWidgetUtilities2;
import org.pushingpixels.lafwidget.tabbed.TabPreviewPainter;
import org.pushingpixels.lafwidget.utils.DeltaQueue;
import org.pushingpixels.lafwidget.utils.TrackableThread;

public class TabPreviewThread
extends TrackableThread {
    private boolean stopRequested;
    protected DeltaQueue previewQueue;
    private static TabPreviewThread tabPreviewThread;

    private TabPreviewThread() {
        this.setName("Laf-Widget tab preview");
        this.stopRequested = false;
        this.previewQueue = new DeltaQueue();
    }

    @Override
    public void run() {
        while (!this.stopRequested) {
            try {
                int delay = 500;
                List<DeltaQueue.Deltable> expired = this.dequeueTabPreviewRequest(delay);
                for (DeltaQueue.Deltable dExpired : expired) {
                    final TabPreviewInfo nextPreviewInfo = (TabPreviewInfo)dExpired;
                    final JTabbedPane jtp = nextPreviewInfo.tabPane;
                    if (jtp == null) continue;
                    final TabPreviewPainter previewPainter = LafWidgetUtilities2.getTabPreviewPainter(jtp);
                    int tabCount = jtp.getTabCount();
                    if (nextPreviewInfo.toPreviewAllTabs) {
                        SwingUtilities.invokeLater(new Runnable(){

                            @Override
                            public void run() {
                                nextPreviewInfo.previewCallback.start(jtp, jtp.getTabCount(), nextPreviewInfo);
                            }
                        });
                        int i2 = 0;
                        while (i2 < tabCount) {
                            final int index = i2++;
                            SwingUtilities.invokeLater(new Runnable(){

                                @Override
                                public void run() {
                                    TabPreviewThread.this.getSingleTabPreviewImage(jtp, previewPainter, nextPreviewInfo, index);
                                }
                            });
                        }
                    } else {
                        SwingUtilities.invokeLater(new Runnable(){

                            @Override
                            public void run() {
                                TabPreviewThread.this.getSingleTabPreviewImage(jtp, previewPainter, nextPreviewInfo, nextPreviewInfo.tabIndexToPreview);
                            }
                        });
                    }
                    if (!previewPainter.toUpdatePeriodically(jtp)) continue;
                    TabPreviewInfo cyclePreviewInfo = new TabPreviewInfo();
                    cyclePreviewInfo.tabPane = nextPreviewInfo.tabPane;
                    cyclePreviewInfo.tabIndexToPreview = nextPreviewInfo.tabIndexToPreview;
                    cyclePreviewInfo.toPreviewAllTabs = nextPreviewInfo.toPreviewAllTabs;
                    cyclePreviewInfo.previewCallback = nextPreviewInfo.previewCallback;
                    cyclePreviewInfo.setPreviewWidth(nextPreviewInfo.getPreviewWidth());
                    cyclePreviewInfo.setPreviewHeight(nextPreviewInfo.getPreviewHeight());
                    cyclePreviewInfo.initiator = nextPreviewInfo.initiator;
                    cyclePreviewInfo.setDelta(previewPainter.getUpdateCycle(cyclePreviewInfo.tabPane));
                    this.queueTabPreviewRequest(cyclePreviewInfo);
                }
                Thread.sleep(delay);
            }
            catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    protected void getSingleTabPreviewImage(final JTabbedPane tabPane, TabPreviewPainter previewPainter, final TabPreviewInfo previewInfo, final int tabIndex) {
        int pWidth = previewInfo.getPreviewWidth();
        int pHeight = previewInfo.getPreviewHeight();
        final BufferedImage previewImage = new BufferedImage(pWidth, pHeight, 2);
        Graphics2D gr = previewImage.createGraphics();
        Component comp = tabPane.getComponentAt(tabIndex);
        if (previewPainter.hasPreview(tabPane, tabIndex)) {
            HashMap<Component, Boolean> dbSnapshot = new HashMap<Component, Boolean>();
            LafWidgetUtilities.makePreviewable(comp, dbSnapshot);
            previewPainter.previewTab(tabPane, tabIndex, gr, 0, 0, pWidth, pHeight);
            LafWidgetUtilities.restorePreviewable(comp, dbSnapshot);
        } else {
            gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            gr.setColor(Color.red);
            gr.setStroke(new BasicStroke(Math.max(5.0f, (float)Math.min(pWidth, pHeight) / 10.0f)));
            gr.drawLine(0, 0, pWidth, pHeight);
            gr.drawLine(0, pHeight, pWidth, 0);
        }
        gr.dispose();
        if (previewInfo.previewCallback != null) {
            SwingUtilities.invokeLater(new Runnable(){

                @Override
                public void run() {
                    previewInfo.previewCallback.offer(tabPane, tabIndex, previewImage);
                }
            });
        }
    }

    public void queueTabPreviewRequest(TabPreviewInfo previewInfo) {
        this.previewQueue.queue(previewInfo);
    }

    public void cancelTabPreviewRequests(final Object initiator) {
        DeltaQueue.DeltaMatcher matcher = new DeltaQueue.DeltaMatcher(){

            @Override
            public boolean matches(DeltaQueue.Deltable deltable) {
                TabPreviewInfo currInfo = (TabPreviewInfo)deltable;
                return currInfo.initiator == initiator;
            }
        };
        this.previewQueue.removeMatching(matcher);
    }

    private List<DeltaQueue.Deltable> dequeueTabPreviewRequest(int delay) {
        return this.previewQueue.dequeue(delay);
    }

    @Override
    protected void requestStop() {
        this.stopRequested = true;
        tabPreviewThread = null;
    }

    public static synchronized TabPreviewThread getInstance() {
        if (tabPreviewThread == null) {
            tabPreviewThread = new TabPreviewThread();
            tabPreviewThread.start();
        }
        return tabPreviewThread;
    }

    public static synchronized boolean instanceRunning() {
        return tabPreviewThread != null;
    }

    public static interface TabPreviewCallback {
        public void start(JTabbedPane var1, int var2, TabPreviewInfo var3);

        public void offer(JTabbedPane var1, int var2, BufferedImage var3);
    }

    public static class TabPreviewInfo
    extends DeltaQueue.Deltable {
        public JTabbedPane tabPane;
        public TabPreviewCallback previewCallback;
        private int previewWidth;
        private int previewHeight;
        public boolean toPreviewAllTabs;
        public int tabIndexToPreview;
        public Object initiator;

        public void setPreviewWidth(int previewWidth) {
            this.previewWidth = previewWidth;
        }

        public int getPreviewWidth() {
            return this.previewWidth;
        }

        public void setPreviewHeight(int previewHeight) {
            this.previewHeight = previewHeight;
        }

        public int getPreviewHeight() {
            return this.previewHeight;
        }
    }
}

