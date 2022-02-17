/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.animation.effects;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractButton;
import org.pushingpixels.lafwidget.LafWidgetAdapter;
import org.pushingpixels.lafwidget.animation.effects.GhostingListener;

public class GhostAnimationWidget
extends LafWidgetAdapter<AbstractButton> {
    private GhostingListener ghostModelChangeListener;
    protected PropertyChangeListener ghostPropertyListener;

    @Override
    public boolean requiresCustomLafSupport() {
        return false;
    }

    @Override
    public void installDefaults() {
        ((AbstractButton)this.jcomp).setRolloverEnabled(true);
    }

    @Override
    public void installListeners() {
        this.ghostPropertyListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("model".equals(evt.getPropertyName())) {
                    if (GhostAnimationWidget.this.ghostModelChangeListener != null) {
                        GhostAnimationWidget.this.ghostModelChangeListener.unregisterListeners();
                    }
                    GhostAnimationWidget.this.ghostModelChangeListener = new GhostingListener(GhostAnimationWidget.this.jcomp, ((AbstractButton)GhostAnimationWidget.this.jcomp).getModel());
                    GhostAnimationWidget.this.ghostModelChangeListener.registerListeners();
                }
            }
        };
        ((AbstractButton)this.jcomp).addPropertyChangeListener(this.ghostPropertyListener);
        this.ghostModelChangeListener = new GhostingListener(this.jcomp, ((AbstractButton)this.jcomp).getModel());
        this.ghostModelChangeListener.registerListeners();
    }

    @Override
    public void uninstallListeners() {
        ((AbstractButton)this.jcomp).removePropertyChangeListener(this.ghostPropertyListener);
        this.ghostPropertyListener = null;
        this.ghostModelChangeListener.unregisterListeners();
        this.ghostModelChangeListener = null;
    }
}

