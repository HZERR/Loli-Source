/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.tabbed;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JTabbedPane;
import org.pushingpixels.lafwidget.LafWidgetAdapter;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.LafWidgetSupport;
import org.pushingpixels.lafwidget.tabbed.TabPreviewThread;
import org.pushingpixels.lafwidget.tabbed.TabPreviewWindow;

public class TabHoverPreviewWidget
extends LafWidgetAdapter<JTabbedPane> {
    protected MouseRolloverHandler baseRolloverHandler;

    @Override
    public void installListeners() {
        this.baseRolloverHandler = new MouseRolloverHandler();
        ((JTabbedPane)this.jcomp).addMouseMotionListener(this.baseRolloverHandler);
        ((JTabbedPane)this.jcomp).addMouseListener(this.baseRolloverHandler);
    }

    @Override
    public void uninstallListeners() {
        if (this.baseRolloverHandler != null) {
            ((JTabbedPane)this.jcomp).removeMouseMotionListener(this.baseRolloverHandler);
            ((JTabbedPane)this.jcomp).removeMouseListener(this.baseRolloverHandler);
            this.baseRolloverHandler = null;
        }
    }

    @Override
    public void uninstallUI() {
        if (TabPreviewThread.instanceRunning()) {
            TabPreviewThread.getInstance().cancelTabPreviewRequests(this.jcomp);
        }
        TabPreviewWindow.cancelPreviewRequest();
        super.uninstallUI();
    }

    @Override
    public boolean requiresCustomLafSupport() {
        return false;
    }

    protected class MouseRolloverHandler
    implements MouseListener,
    MouseMotionListener {
        int prevRolledOver = -1;

        protected MouseRolloverHandler() {
        }

        @Override
        public void mouseClicked(MouseEvent e2) {
        }

        @Override
        public void mouseDragged(MouseEvent e2) {
        }

        @Override
        public void mouseEntered(MouseEvent e2) {
        }

        @Override
        public void mousePressed(MouseEvent e2) {
            TabPreviewWindow.cancelPreviewRequest();
        }

        @Override
        public void mouseReleased(MouseEvent e2) {
        }

        @Override
        public void mouseMoved(MouseEvent e2) {
            if (e2.getSource() != TabHoverPreviewWidget.this.jcomp) {
                return;
            }
            LafWidgetSupport lafSupport = LafWidgetRepository.getRepository().getLafSupport();
            int currRolledOver = 0;
            try {
                currRolledOver = lafSupport.getRolloverTabIndex((JTabbedPane)TabHoverPreviewWidget.this.jcomp);
            }
            catch (UnsupportedOperationException uoe) {
                return;
            }
            if (currRolledOver >= ((JTabbedPane)TabHoverPreviewWidget.this.jcomp).getTabCount()) {
                return;
            }
            if (currRolledOver != ((JTabbedPane)TabHoverPreviewWidget.this.jcomp).getSelectedIndex()) {
                if (currRolledOver == this.prevRolledOver) {
                    if (currRolledOver >= 0 && currRolledOver < ((JTabbedPane)TabHoverPreviewWidget.this.jcomp).getTabCount() && ((JTabbedPane)TabHoverPreviewWidget.this.jcomp).isEnabledAt(currRolledOver)) {
                        TabPreviewWindow.getInstance().postPreviewRequest((JTabbedPane)TabHoverPreviewWidget.this.jcomp, currRolledOver);
                    }
                } else {
                    if (this.prevRolledOver >= 0 && this.prevRolledOver < ((JTabbedPane)TabHoverPreviewWidget.this.jcomp).getTabCount() && ((JTabbedPane)TabHoverPreviewWidget.this.jcomp).isEnabledAt(this.prevRolledOver)) {
                        TabPreviewWindow.cancelPreviewRequest();
                    }
                    if (currRolledOver >= 0 && currRolledOver < ((JTabbedPane)TabHoverPreviewWidget.this.jcomp).getTabCount() && ((JTabbedPane)TabHoverPreviewWidget.this.jcomp).isEnabledAt(currRolledOver)) {
                        TabPreviewWindow.getInstance().postPreviewRequest((JTabbedPane)TabHoverPreviewWidget.this.jcomp, currRolledOver);
                    }
                }
            } else {
                TabPreviewWindow.cancelPreviewRequest();
            }
            this.prevRolledOver = currRolledOver;
        }

        @Override
        public void mouseExited(MouseEvent e2) {
            if (this.prevRolledOver >= 0 && this.prevRolledOver < ((JTabbedPane)TabHoverPreviewWidget.this.jcomp).getTabCount() && ((JTabbedPane)TabHoverPreviewWidget.this.jcomp).isEnabledAt(this.prevRolledOver)) {
                TabPreviewWindow.cancelPreviewRequest();
            }
            this.prevRolledOver = -1;
        }
    }
}

