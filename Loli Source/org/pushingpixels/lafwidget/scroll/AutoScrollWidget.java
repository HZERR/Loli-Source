/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.scroll;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JScrollPane;
import org.pushingpixels.lafwidget.LafWidgetAdapter;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.lafwidget.scroll.AutoScrollActivator;

public class AutoScrollWidget
extends LafWidgetAdapter<JScrollPane> {
    protected PropertyChangeListener propertyChangeListener;

    @Override
    public boolean requiresCustomLafSupport() {
        return false;
    }

    @Override
    public void installUI() {
        if (LafWidgetUtilities.hasAutoScroll((JScrollPane)this.jcomp)) {
            AutoScrollActivator.setAutoScrollEnabled((JScrollPane)this.jcomp, true);
        }
    }

    @Override
    public void uninstallUI() {
        AutoScrollActivator.setAutoScrollEnabled((JScrollPane)this.jcomp, false);
    }

    @Override
    public void installListeners() {
        this.propertyChangeListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("lafwidget.scroll.auto".equals(evt.getPropertyName())) {
                    AutoScrollActivator.setAutoScrollEnabled((JScrollPane)AutoScrollWidget.this.jcomp, LafWidgetUtilities.hasAutoScroll((JScrollPane)AutoScrollWidget.this.jcomp));
                }
            }
        };
        ((JScrollPane)this.jcomp).addPropertyChangeListener(this.propertyChangeListener);
    }

    @Override
    public void uninstallListeners() {
        ((JScrollPane)this.jcomp).removePropertyChangeListener(this.propertyChangeListener);
        this.propertyChangeListener = null;
    }
}

