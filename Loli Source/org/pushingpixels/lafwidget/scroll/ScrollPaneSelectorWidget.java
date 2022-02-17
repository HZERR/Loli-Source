/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.scroll;

import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.ComboPopup;
import org.pushingpixels.lafwidget.LafWidgetAdapter;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.LafWidgetUtilities2;
import org.pushingpixels.lafwidget.preview.PreviewPainter;
import org.pushingpixels.lafwidget.scroll.ScrollPaneSelector;

public class ScrollPaneSelectorWidget
extends LafWidgetAdapter<JScrollPane> {
    protected ScrollPaneSelector scrollPaneSelector;
    protected HierarchyListener hierarchyListener;
    protected PropertyChangeListener propertyChangeListener;

    @Override
    public boolean requiresCustomLafSupport() {
        return false;
    }

    @Override
    public void installUI() {
        if (LafWidgetRepository.getRepository().getLafSupport().toInstallExtraElements(this.jcomp)) {
            PreviewPainter pPainter = LafWidgetUtilities2.getComponentPreviewPainter(this.jcomp);
            if (pPainter == null) {
                return;
            }
            this.scrollPaneSelector = new ScrollPaneSelector();
            this.scrollPaneSelector.installOnScrollPane((JScrollPane)this.jcomp);
        }
    }

    @Override
    public void uninstallUI() {
        if (this.scrollPaneSelector != null) {
            this.scrollPaneSelector.uninstallFromScrollPane();
            this.scrollPaneSelector = null;
        }
    }

    @Override
    public void installListeners() {
        this.hierarchyListener = new HierarchyListener(){

            @Override
            public void hierarchyChanged(HierarchyEvent e2) {
                if (((JScrollPane)ScrollPaneSelectorWidget.this.jcomp).getParent() instanceof ComboPopup && ScrollPaneSelectorWidget.this.scrollPaneSelector != null) {
                    ScrollPaneSelectorWidget.this.scrollPaneSelector.uninstallFromScrollPane();
                    ScrollPaneSelectorWidget.this.scrollPaneSelector = null;
                }
            }
        };
        ((JScrollPane)this.jcomp).addHierarchyListener(this.hierarchyListener);
        this.propertyChangeListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("lafwidgets.componentPreviewPainter".equals(evt.getPropertyName())) {
                    PreviewPainter pPainter = LafWidgetUtilities2.getComponentPreviewPainter(ScrollPaneSelectorWidget.this.jcomp);
                    if (ScrollPaneSelectorWidget.this.scrollPaneSelector != null) {
                        ScrollPaneSelectorWidget.this.scrollPaneSelector.uninstallFromScrollPane();
                        ScrollPaneSelectorWidget.this.scrollPaneSelector = null;
                    }
                    if (pPainter != null && LafWidgetRepository.getRepository().getLafSupport().toInstallExtraElements(ScrollPaneSelectorWidget.this.jcomp)) {
                        ScrollPaneSelectorWidget.this.scrollPaneSelector = new ScrollPaneSelector();
                        ScrollPaneSelectorWidget.this.scrollPaneSelector.installOnScrollPane((JScrollPane)ScrollPaneSelectorWidget.this.jcomp);
                    }
                }
            }
        };
        ((JScrollPane)this.jcomp).addPropertyChangeListener(this.propertyChangeListener);
    }

    @Override
    public void uninstallListeners() {
        ((JScrollPane)this.jcomp).removeHierarchyListener(this.hierarchyListener);
        this.hierarchyListener = null;
        ((JScrollPane)this.jcomp).removePropertyChangeListener(this.propertyChangeListener);
        this.propertyChangeListener = null;
    }
}

