/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.tree.dnd;

import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

class AutoScrollingTreeDropTarget
extends DropTarget {
    private JViewport viewport;
    private int scrollUnits;
    private JTree tree;
    private Point lastDragCursorLocn = new Point(0, 0);
    private static final int AUTOSCROLL_MARGIN = 16;

    AutoScrollingTreeDropTarget(JTree aTree, DropTargetListener listener) {
        super(aTree, 3, listener);
        this.viewport = (JViewport)SwingUtilities.getAncestorOfClass(JViewport.class, aTree);
        this.scrollUnits = Math.max(aTree.getRowHeight(), 16);
        this.tree = aTree;
    }

    @Override
    protected void updateAutoscroll(Point dragCursorLocn) {
        if (this.lastDragCursorLocn.equals(dragCursorLocn)) {
            return;
        }
        this.lastDragCursorLocn.setLocation(dragCursorLocn);
        this.doAutoscroll(dragCursorLocn);
    }

    @Override
    protected void initializeAutoscrolling(Point p2) {
        this.doAutoscroll(p2);
    }

    @Override
    protected void clearAutoscroll() {
    }

    protected void doAutoscroll(Point aPoint) {
        if (this.viewport == null) {
            return;
        }
        Point treePosition = this.viewport.getViewPosition();
        int vH = this.viewport.getExtentSize().height;
        int vW = this.viewport.getExtentSize().width;
        Point nextPoint = null;
        if (aPoint.y - treePosition.y < 16) {
            nextPoint = new Point(treePosition.x, Math.max(treePosition.y - this.scrollUnits, 0));
        } else if (treePosition.y + vH - aPoint.y < 16) {
            nextPoint = new Point(treePosition.x, Math.min(aPoint.y + 16, this.tree.getHeight() - vH));
        } else if (aPoint.x - treePosition.x < 16) {
            nextPoint = new Point(Math.max(treePosition.x - 16, 0), treePosition.y);
        } else if (treePosition.x + vW - aPoint.x < 16) {
            nextPoint = new Point(Math.min(treePosition.x + 16, this.tree.getWidth() - vW), treePosition.y);
        }
        if (nextPoint != null) {
            this.viewport.setViewPosition(nextPoint);
        }
    }
}

