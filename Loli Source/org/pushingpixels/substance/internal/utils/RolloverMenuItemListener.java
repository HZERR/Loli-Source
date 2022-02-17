/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils;

import java.awt.event.MouseEvent;
import javax.swing.ButtonModel;
import javax.swing.JMenuItem;
import javax.swing.event.MouseInputListener;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;

public class RolloverMenuItemListener
implements MouseInputListener {
    private boolean isMouseInside;
    private JMenuItem item;
    private StateTransitionTracker stateTransitionTracker;

    public RolloverMenuItemListener(JMenuItem item, StateTransitionTracker stateTransitionTracker) {
        this.item = item;
        this.stateTransitionTracker = stateTransitionTracker;
        this.isMouseInside = false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void mouseEntered(MouseEvent e2) {
        this.stateTransitionTracker.turnOffModelChangeTracking();
        try {
            this.isMouseInside = true;
            this.item.getModel().setRollover(true);
        }
        finally {
            this.stateTransitionTracker.onModelStateChanged();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void mouseExited(MouseEvent e2) {
        this.stateTransitionTracker.turnOffModelChangeTracking();
        try {
            this.isMouseInside = false;
            this.item.getModel().setRollover(false);
        }
        finally {
            this.stateTransitionTracker.onModelStateChanged();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void mouseReleased(MouseEvent e2) {
        this.stateTransitionTracker.turnOffModelChangeTracking();
        try {
            ButtonModel model = this.item.getModel();
            model.setRollover(false);
        }
        finally {
            this.stateTransitionTracker.onModelStateChanged();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e2) {
    }

    @Override
    public void mousePressed(MouseEvent e2) {
    }

    @Override
    public void mouseDragged(MouseEvent e2) {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void mouseMoved(MouseEvent e2) {
        this.stateTransitionTracker.turnOffModelChangeTracking();
        try {
            this.item.getModel().setRollover(this.isMouseInside);
        }
        finally {
            this.stateTransitionTracker.onModelStateChanged();
        }
    }
}

