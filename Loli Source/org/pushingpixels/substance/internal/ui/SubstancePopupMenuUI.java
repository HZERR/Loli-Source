/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.menu.MenuUtilities;

public class SubstancePopupMenuUI
extends BasicPopupMenuUI {
    protected Set lafWidgets;
    protected ContainerListener substanceContainerListener;
    protected PopupMenuListener substancePopupMenuListener;

    public void __org__pushingpixels__substance__internal__ui__SubstancePopupMenuUI__installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstancePopupMenuUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstancePopupMenuUI__uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstancePopupMenuUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    @Override
    protected void installListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstancePopupMenuUI__installListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstancePopupMenuUI__installDefaults() {
        super.installDefaults();
    }

    @Override
    public void installDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstancePopupMenuUI__installDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    @Override
    protected void uninstallListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstancePopupMenuUI__uninstallListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstancePopupMenuUI__uninstallDefaults() {
        super.uninstallDefaults();
    }

    @Override
    protected void uninstallDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstancePopupMenuUI__uninstallDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstancePopupMenuUI__update(Graphics graphics, JComponent jComponent) {
        super.update(graphics, jComponent);
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstancePopupMenuUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstancePopupMenuUI();
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstancePopupMenuUI__installListeners() {
        super.installListeners();
        this.substanceContainerListener = new ContainerListener(){

            @Override
            public void componentAdded(ContainerEvent e2) {
                MenuUtilities.cleanPopupLayoutMetrics(SubstancePopupMenuUI.this.popupMenu);
            }

            @Override
            public void componentRemoved(ContainerEvent e2) {
                MenuUtilities.cleanPopupLayoutMetrics(SubstancePopupMenuUI.this.popupMenu);
            }
        };
        this.popupMenu.addContainerListener(this.substanceContainerListener);
        this.substancePopupMenuListener = new PopupMenuListener(){

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e2) {
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e2) {
                for (int i2 = 0; i2 < SubstancePopupMenuUI.this.popupMenu.getComponentCount(); ++i2) {
                    JMenuItem menuItem;
                    Component comp = SubstancePopupMenuUI.this.popupMenu.getComponent(i2);
                    if (!(comp instanceof JMenuItem) || !(menuItem = (JMenuItem)comp).isEnabled() || !menuItem.getModel().isArmed()) continue;
                    menuItem.getModel().setArmed(false);
                }
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e2) {
                for (int i2 = 0; i2 < SubstancePopupMenuUI.this.popupMenu.getComponentCount(); ++i2) {
                    JMenuItem menuItem;
                    Component comp = SubstancePopupMenuUI.this.popupMenu.getComponent(i2);
                    if (!(comp instanceof JMenuItem) || !(menuItem = (JMenuItem)comp).isEnabled() || !menuItem.getModel().isArmed()) continue;
                    menuItem.getModel().setArmed(false);
                }
            }
        };
        this.popupMenu.addPopupMenuListener(this.substancePopupMenuListener);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstancePopupMenuUI__uninstallListeners() {
        this.popupMenu.removeContainerListener(this.substanceContainerListener);
        this.substanceContainerListener = null;
        this.popupMenu.removePopupMenuListener(this.substancePopupMenuListener);
        this.substancePopupMenuListener = null;
        super.uninstallListeners();
    }
}

