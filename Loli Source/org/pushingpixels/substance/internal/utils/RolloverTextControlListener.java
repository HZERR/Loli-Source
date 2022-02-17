/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.animation.TransitionAwareUI;

public class RolloverTextControlListener
implements MouseListener,
MouseMotionListener,
FocusListener {
    private boolean isMouseInside;
    private ButtonModel model;
    private TransitionAwareUI trackableUI;
    private StateTransitionTracker stateTransitionTracker;
    private JComponent component;

    public RolloverTextControlListener(JComponent component, TransitionAwareUI trackableUI, ButtonModel model) {
        this.component = component;
        this.trackableUI = trackableUI;
        this.model = model;
        this.isMouseInside = false;
        this.stateTransitionTracker = this.trackableUI.getTransitionTracker();
    }

    public void registerListeners() {
        this.component.addMouseListener(this);
        this.component.addMouseMotionListener(this);
        this.component.addFocusListener(this);
    }

    public void unregisterListeners() {
        this.component.removeMouseListener(this);
        this.component.removeMouseMotionListener(this);
        this.component.removeFocusListener(this);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void focusGained(FocusEvent e2) {
        this.stateTransitionTracker.turnOffModelChangeTracking();
        try {
            this.model.setSelected(true);
        }
        finally {
            this.stateTransitionTracker.onModelStateChanged();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void focusLost(FocusEvent e2) {
        this.stateTransitionTracker.turnOffModelChangeTracking();
        try {
            this.model.setSelected(false);
        }
        finally {
            this.stateTransitionTracker.onModelStateChanged();
        }
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

