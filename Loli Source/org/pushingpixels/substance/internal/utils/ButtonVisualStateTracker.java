/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.utils.RolloverButtonListener;
import org.pushingpixels.substance.internal.utils.scroll.SubstanceScrollButton;
import org.pushingpixels.trident.swing.SwingRepaintCallback;

public class ButtonVisualStateTracker {
    private RolloverButtonListener substanceButtonListener;
    protected PropertyChangeListener substancePropertyListener;
    protected StateTransitionTracker stateTransitionTracker;

    public void installListeners(final AbstractButton b2, boolean toInstallRolloverListener) {
        this.stateTransitionTracker = new StateTransitionTracker(b2, b2.getModel());
        if (b2 instanceof SubstanceScrollButton) {
            this.stateTransitionTracker.setRepaintCallback(new StateTransitionTracker.RepaintCallback(){

                @Override
                public SwingRepaintCallback getRepaintCallback() {
                    JScrollBar scrollBar = (JScrollBar)SwingUtilities.getAncestorOfClass(JScrollBar.class, b2);
                    if (scrollBar != null) {
                        return new SwingRepaintCallback(scrollBar);
                    }
                    return new SwingRepaintCallback(b2);
                }
            });
        }
        this.stateTransitionTracker.registerModelListeners();
        this.stateTransitionTracker.registerFocusListeners();
        if (toInstallRolloverListener) {
            this.substanceButtonListener = new RolloverButtonListener(b2, this.stateTransitionTracker);
            b2.addMouseListener(this.substanceButtonListener);
            b2.addMouseMotionListener(this.substanceButtonListener);
            b2.addFocusListener(this.substanceButtonListener);
            b2.addPropertyChangeListener(this.substanceButtonListener);
            b2.addChangeListener(this.substanceButtonListener);
        }
        this.substancePropertyListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("model".equals(evt.getPropertyName())) {
                    ButtonVisualStateTracker.this.stateTransitionTracker.setModel((ButtonModel)evt.getNewValue());
                }
            }
        };
        b2.addPropertyChangeListener(this.substancePropertyListener);
    }

    public void uninstallListeners(AbstractButton b2) {
        if (this.substanceButtonListener != null) {
            b2.removeMouseListener(this.substanceButtonListener);
            b2.removeMouseMotionListener(this.substanceButtonListener);
            b2.removeFocusListener(this.substanceButtonListener);
            b2.removePropertyChangeListener(this.substanceButtonListener);
            b2.removeChangeListener(this.substanceButtonListener);
            this.substanceButtonListener = null;
        }
        b2.removePropertyChangeListener(this.substancePropertyListener);
        this.substancePropertyListener = null;
        this.stateTransitionTracker.unregisterModelListeners();
        this.stateTransitionTracker.unregisterFocusListeners();
    }

    public StateTransitionTracker getStateTransitionTracker() {
        return this.stateTransitionTracker;
    }
}

