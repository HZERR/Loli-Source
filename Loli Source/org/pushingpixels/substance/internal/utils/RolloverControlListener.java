/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.ButtonModel;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.animation.TransitionAwareUI;

public class RolloverControlListener
implements MouseListener,
MouseMotionListener {
    private boolean isMouseInside;
    private ButtonModel model;
    private TransitionAwareUI trackableUI;
    private StateTransitionTracker stateTransitionTracker;

    public RolloverControlListener(TransitionAwareUI trackableUI, ButtonModel model) {
        this.trackableUI = trackableUI;
        this.model = model;
        this.isMouseInside = false;
        this.stateTransitionTracker = trackableUI.getTransitionTracker();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void mouseEntered(MouseEvent e2) {
        this.stateTransitionTracker.turnOffModelChangeTracking();
        try {
            boolean isInside;
            Component component = (Component)e2.getSource();
            if (!component.isEnabled()) {
                return;
            }
            this.isMouseInside = isInside = this.trackableUI.isInside(e2);
            this.model.setRollover(isInside);
            this.model.setEnabled(component.isEnabled());
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
            Component component = (Component)e2.getSource();
            if (!component.isEnabled()) {
                return;
            }
            this.isMouseInside = false;
            this.model.setRollover(false);
            this.model.setEnabled(component.isEnabled());
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
            boolean isInside;
            Component component = (Component)e2.getSource();
            if (!component.isEnabled()) {
                return;
            }
            this.isMouseInside = isInside = this.trackableUI.isInside(e2);
            this.model.setRollover(this.isMouseInside);
            this.model.setPressed(false);
            this.model.setArmed(false);
            this.model.setSelected(false);
            this.model.setEnabled(component.isEnabled());
        }
        finally {
            this.stateTransitionTracker.onModelStateChanged();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void mousePressed(MouseEvent e2) {
        this.stateTransitionTracker.turnOffModelChangeTracking();
        try {
            boolean isInside;
            Component component = (Component)e2.getSource();
            if (!component.isEnabled()) {
                return;
            }
            this.isMouseInside = isInside = this.trackableUI.isInside(e2);
            this.model.setRollover(this.isMouseInside);
            if (this.isMouseInside) {
                this.model.setPressed(true);
                this.model.setArmed(true);
                this.model.setSelected(true);
            }
            this.model.setEnabled(component.isEnabled());
        }
        finally {
            this.stateTransitionTracker.onModelStateChanged();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void mouseDragged(MouseEvent e2) {
        this.stateTransitionTracker.turnOffModelChangeTracking();
        try {
            boolean isInside;
            Component component = (Component)e2.getSource();
            if (!component.isEnabled()) {
                return;
            }
            this.isMouseInside = isInside = this.trackableUI.isInside(e2);
            this.model.setEnabled(component.isEnabled());
        }
        finally {
            this.stateTransitionTracker.onModelStateChanged();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void mouseMoved(MouseEvent e2) {
        this.stateTransitionTracker.turnOffModelChangeTracking();
        try {
            boolean isInside;
            Component component = (Component)e2.getSource();
            if (!component.isEnabled()) {
                return;
            }
            this.isMouseInside = isInside = this.trackableUI.isInside(e2);
            this.model.setRollover(isInside);
            this.model.setEnabled(component.isEnabled());
        }
        finally {
            this.stateTransitionTracker.onModelStateChanged();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e2) {
    }
}

