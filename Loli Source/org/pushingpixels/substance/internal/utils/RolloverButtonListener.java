/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils;

import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.security.AccessControlException;
import javax.swing.AbstractButton;
import javax.swing.plaf.basic.BasicButtonListener;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.utils.SubstanceInternalFrameTitlePane;

public class RolloverButtonListener
extends BasicButtonListener {
    private boolean isMouseInside;
    private AbstractButton button;
    private StateTransitionTracker stateTransitionTracker;

    public RolloverButtonListener(AbstractButton b2, StateTransitionTracker stateTransitionTracker) {
        super(b2);
        this.button = b2;
        this.isMouseInside = false;
        this.stateTransitionTracker = stateTransitionTracker;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void mouseEntered(MouseEvent e2) {
        this.stateTransitionTracker.turnOffModelChangeTracking();
        try {
            boolean isMouseDrag;
            super.mouseEntered(e2);
            this.isMouseInside = true;
            boolean bl = isMouseDrag = (e2.getModifiersEx() & 0x400) != 0;
            if (!isMouseDrag) {
                this.button.getModel().setRollover(true);
            }
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
            super.mouseExited(e2);
            this.isMouseInside = false;
            this.button.getModel().setRollover(false);
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
            super.mouseReleased(e2);
            for (ActionListener al : this.button.getActionListeners()) {
                if (!(al instanceof SubstanceInternalFrameTitlePane.ClickListener)) continue;
                return;
            }
            this.button.getModel().setRollover(this.isMouseInside);
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
            super.mouseMoved(e2);
            for (ActionListener al : this.button.getActionListeners()) {
                if (!(al instanceof SubstanceInternalFrameTitlePane.ClickListener)) continue;
                return;
            }
            this.button.getModel().setRollover(this.isMouseInside);
        }
        finally {
            this.stateTransitionTracker.onModelStateChanged();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void focusGained(FocusEvent e2) {
        PointerInfo pi;
        block8: {
            block7: {
                this.stateTransitionTracker.turnOffModelChangeTracking();
                super.focusGained(e2);
                if (this.button.isShowing()) break block7;
                this.stateTransitionTracker.onModelStateChanged();
                return;
            }
            pi = MouseInfo.getPointerInfo();
            if (pi != null) break block8;
            this.stateTransitionTracker.onModelStateChanged();
            return;
        }
        try {
            try {
                int px = pi.getLocation().x - this.button.getLocationOnScreen().x;
                int py = pi.getLocation().y - this.button.getLocationOnScreen().y;
                this.button.getModel().setRollover(this.button.contains(px, py));
            }
            catch (AccessControlException accessControlException) {
                // empty catch block
            }
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
            super.focusLost(e2);
            this.button.getModel().setRollover(false);
        }
        finally {
            this.stateTransitionTracker.onModelStateChanged();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void mouseClicked(MouseEvent e2) {
        this.stateTransitionTracker.turnOffModelChangeTracking();
        try {
            super.mouseClicked(e2);
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
            super.mouseDragged(e2);
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
            super.mousePressed(e2);
        }
        finally {
            this.stateTransitionTracker.onModelStateChanged();
        }
    }
}

