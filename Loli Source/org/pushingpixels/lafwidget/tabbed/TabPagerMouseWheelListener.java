/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.tabbed;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JTabbedPane;
import org.pushingpixels.lafwidget.LafWidgetUtilities2;
import org.pushingpixels.lafwidget.tabbed.TabPagerManager;
import org.pushingpixels.lafwidget.tabbed.TabPreviewPainter;

public class TabPagerMouseWheelListener
implements MouseWheelListener {
    protected JTabbedPane tabbedPane;

    public TabPagerMouseWheelListener() {
        this(null);
    }

    public TabPagerMouseWheelListener(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e2) {
        TabPreviewPainter tpp = LafWidgetUtilities2.getTabPreviewPainter(this.tabbedPane);
        if (tpp == null) {
            return;
        }
        if ((e2.getModifiers() & 2) != 0 && e2.getScrollType() == 0) {
            int amount = e2.getWheelRotation();
            TabPagerManager te = TabPagerManager.getPager();
            if (te.isVisible()) {
                if (amount > 0) {
                    if (this.tabbedPane != null) {
                        te.page(this.tabbedPane, true);
                    } else {
                        te.page(true);
                    }
                } else if (this.tabbedPane != null) {
                    te.page(this.tabbedPane, false);
                } else {
                    te.page(false);
                }
            }
        }
    }
}

