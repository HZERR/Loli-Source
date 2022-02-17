/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSplitPaneDivider;

public class SubstanceSplitPaneUI
extends BasicSplitPaneUI {
    protected Set lafWidgets;
    protected PropertyChangeListener substancePropertyListener;

    public void __org__pushingpixels__substance__internal__ui__SubstanceSplitPaneUI__installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceSplitPaneUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceSplitPaneUI__uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceSplitPaneUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    @Override
    protected void installListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceSplitPaneUI__installListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceSplitPaneUI__installDefaults() {
        super.installDefaults();
    }

    @Override
    protected void installDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceSplitPaneUI__installDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    @Override
    protected void uninstallListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceSplitPaneUI__uninstallListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceSplitPaneUI__uninstallDefaults() {
        super.uninstallDefaults();
    }

    @Override
    protected void uninstallDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceSplitPaneUI__uninstallDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceSplitPaneUI__update(Graphics graphics, JComponent jComponent) {
        super.update(graphics, jComponent);
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceSplitPaneUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstanceSplitPaneUI();
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceSplitPaneUI__installListeners() {
        super.installListeners();
        this.substancePropertyListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("orientation".equals(evt.getPropertyName())) {
                    SubstanceSplitPaneDivider substanceDivider = (SubstanceSplitPaneDivider)SubstanceSplitPaneUI.this.divider;
                    substanceDivider.updateOneTouchButtons((Integer)evt.getNewValue());
                }
            }
        };
        this.splitPane.addPropertyChangeListener(this.substancePropertyListener);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceSplitPaneUI__uninstallListeners() {
        this.splitPane.removePropertyChangeListener(this.substancePropertyListener);
        this.substancePropertyListener = null;
        super.uninstallListeners();
    }

    @Override
    public BasicSplitPaneDivider createDefaultDivider() {
        return new SubstanceSplitPaneDivider(this);
    }
}

