/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget;

import javax.swing.JComponent;
import org.pushingpixels.lafwidget.LafWidget;

public abstract class LafWidgetAdapter<T extends JComponent>
implements LafWidget<T> {
    protected T jcomp;

    @Override
    public void setComponent(T jcomp) {
        this.jcomp = jcomp;
    }

    @Override
    public void installUI() {
    }

    @Override
    public void installComponents() {
    }

    @Override
    public void installDefaults() {
    }

    @Override
    public void installListeners() {
    }

    @Override
    public void uninstallUI() {
    }

    @Override
    public void uninstallComponents() {
    }

    @Override
    public void uninstallDefaults() {
    }

    @Override
    public void uninstallListeners() {
    }
}

