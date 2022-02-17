/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.tabbed;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import org.pushingpixels.lafwidget.utils.LafConstants;

public abstract class TabPreviewPainter {
    public void previewTab(JTabbedPane tabPane, int tabIndex, Graphics g2, int x2, int y2, int w2, int h2) {
    }

    public boolean hasPreview(JTabbedPane tabPane, int tabIndex) {
        return false;
    }

    public boolean isSensitiveToEvents(JTabbedPane tabPane, int tabIndex) {
        return false;
    }

    public Rectangle getPreviewDialogScreenBounds(JTabbedPane tabPane) {
        Rectangle tabPaneBounds = tabPane.getBounds();
        Point tabPaneScreenLoc = tabPane.getLocationOnScreen();
        return new Rectangle(tabPaneScreenLoc.x, tabPaneScreenLoc.y, tabPaneBounds.width, tabPaneBounds.height);
    }

    public JFrame getModalOwner(JTabbedPane tabPane) {
        return null;
    }

    public boolean hasOverviewDialog(JTabbedPane tabPane) {
        return false;
    }

    public boolean hasPreviewWindow(JTabbedPane tabPane, int tabIndex) {
        return false;
    }

    public Dimension getPreviewWindowDimension(JTabbedPane tabPane, int tabIndex) {
        return new Dimension(300, 200);
    }

    public int getPreviewWindowExtraDelay(JTabbedPane tabPane, int tabIndex) {
        return 0;
    }

    public boolean toUpdatePeriodically(JTabbedPane tabPane) {
        return false;
    }

    public int getUpdateCycle(JTabbedPane tabPane) {
        return 10000;
    }

    public LafConstants.TabOverviewKind getOverviewKind(JTabbedPane tabPane) {
        return LafConstants.TabOverviewKind.GRID;
    }

    public boolean toDisposeOverviewOnFocusLoss() {
        return true;
    }
}

