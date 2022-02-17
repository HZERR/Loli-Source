/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.tabbed;

import java.awt.Insets;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.pushingpixels.lafwidget.LafWidgetAdapter;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.LafWidgetSupport;
import org.pushingpixels.lafwidget.LafWidgetUtilities2;
import org.pushingpixels.lafwidget.tabbed.TabOverviewButton;
import org.pushingpixels.lafwidget.tabbed.TabPreviewPainter;

public class TabOverviewDialogWidget
extends LafWidgetAdapter<JTabbedPane> {
    protected TabOverviewButton overviewButton;
    protected PropertyChangeListener propertyListener;
    protected ContainerListener containerListener;

    @Override
    public void installComponents() {
        this.overviewButton = new TabOverviewButton((JTabbedPane)this.jcomp);
    }

    @Override
    public void installDefaults() {
        TabPreviewPainter previewPainter = LafWidgetUtilities2.getTabPreviewPainter((JTabbedPane)this.jcomp);
        if (previewPainter != null && previewPainter.hasOverviewDialog((JTabbedPane)this.jcomp)) {
            LafWidgetSupport lafSupport = LafWidgetRepository.getRepository().getLafSupport();
            Insets currTabAreaInsets = lafSupport.getTabAreaInsets((JTabbedPane)this.jcomp);
            if (currTabAreaInsets == null) {
                currTabAreaInsets = UIManager.getInsets("TabbedPane.tabAreaInsets");
            }
            Insets tabAreaInsets = new Insets(currTabAreaInsets.top, LafWidgetRepository.getRepository().getLafSupport().getLookupButtonSize() + 2 + currTabAreaInsets.left, currTabAreaInsets.bottom, currTabAreaInsets.right);
            lafSupport.setTabAreaInsets((JTabbedPane)this.jcomp, tabAreaInsets);
            ((JTabbedPane)this.jcomp).add(this.overviewButton);
            this.overviewButton.setVisible(true);
            ((JTabbedPane)this.jcomp).setComponentZOrder(this.overviewButton, 0);
            this.overviewButton.updateLocation((JTabbedPane)this.jcomp, tabAreaInsets);
        }
    }

    @Override
    public void uninstallComponents() {
        if (this.overviewButton.getParent() == this.jcomp) {
            ((JTabbedPane)this.jcomp).remove(this.overviewButton);
        }
    }

    @Override
    public void installListeners() {
        this.propertyListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Insets currTabAreaInsets;
                LafWidgetSupport lafSupport = LafWidgetRepository.getRepository().getLafSupport();
                Insets lafInsets = lafSupport.getTabAreaInsets((JTabbedPane)TabOverviewDialogWidget.this.jcomp);
                Insets insets = currTabAreaInsets = lafInsets == null ? UIManager.getInsets("TabbedPane.tabAreaInsets") : lafInsets;
                if ("lafwidgets.tabbedpanePreviewPainter".equals(evt.getPropertyName())) {
                    TabPreviewPainter previewPainter = LafWidgetUtilities2.getTabPreviewPainter((JTabbedPane)TabOverviewDialogWidget.this.jcomp);
                    if (previewPainter != null && previewPainter.hasOverviewDialog((JTabbedPane)TabOverviewDialogWidget.this.jcomp)) {
                        Insets tabAreaInsets = new Insets(currTabAreaInsets.top, LafWidgetRepository.getRepository().getLafSupport().getLookupButtonSize() + 2 + currTabAreaInsets.left, currTabAreaInsets.bottom, currTabAreaInsets.right);
                        lafSupport.setTabAreaInsets((JTabbedPane)TabOverviewDialogWidget.this.jcomp, tabAreaInsets);
                        ((JTabbedPane)TabOverviewDialogWidget.this.jcomp).add(TabOverviewDialogWidget.this.overviewButton);
                        TabOverviewDialogWidget.this.overviewButton.setVisible(true);
                        TabOverviewDialogWidget.this.overviewButton.updateLocation((JTabbedPane)TabOverviewDialogWidget.this.jcomp, tabAreaInsets);
                    } else {
                        ((JTabbedPane)TabOverviewDialogWidget.this.jcomp).remove(TabOverviewDialogWidget.this.overviewButton);
                    }
                }
                if ("tabPlacement".equals(evt.getPropertyName()) || "componentOrientation".equals(evt.getPropertyName()) || "tabAreaInsets".equals(evt.getPropertyName())) {
                    SwingUtilities.invokeLater(new Runnable(){

                        @Override
                        public void run() {
                            if (TabOverviewDialogWidget.this.overviewButton.getParent() == TabOverviewDialogWidget.this.jcomp) {
                                TabOverviewDialogWidget.this.overviewButton.updateLocation((JTabbedPane)TabOverviewDialogWidget.this.jcomp, currTabAreaInsets);
                            }
                        }
                    });
                }
            }
        };
        ((JTabbedPane)this.jcomp).addPropertyChangeListener(this.propertyListener);
        this.containerListener = new ContainerAdapter(){

            @Override
            public void componentAdded(ContainerEvent e2) {
                this.syncOverviewButtonVisibility();
            }

            @Override
            public void componentRemoved(ContainerEvent e2) {
                this.syncOverviewButtonVisibility();
            }

            private void syncOverviewButtonVisibility() {
                if (TabOverviewDialogWidget.this.overviewButton.getParent() != TabOverviewDialogWidget.this.jcomp) {
                    return;
                }
                TabOverviewDialogWidget.this.overviewButton.setVisible(((JTabbedPane)TabOverviewDialogWidget.this.jcomp).getTabCount() > 0);
            }
        };
        ((JTabbedPane)this.jcomp).addContainerListener(this.containerListener);
    }

    @Override
    public void uninstallListeners() {
        ((JTabbedPane)this.jcomp).removePropertyChangeListener(this.propertyListener);
        this.propertyListener = null;
        ((JTabbedPane)this.jcomp).removeContainerListener(this.containerListener);
        this.containerListener = null;
    }

    @Override
    public boolean requiresCustomLafSupport() {
        return false;
    }
}

