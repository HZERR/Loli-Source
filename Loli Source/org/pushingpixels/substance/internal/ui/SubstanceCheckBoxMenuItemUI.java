/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicCheckBoxMenuItemUI;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.animation.TransitionAwareUI;
import org.pushingpixels.substance.internal.utils.RolloverMenuItemListener;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.icon.CheckBoxMenuItemIcon;
import org.pushingpixels.substance.internal.utils.menu.MenuUtilities;
import org.pushingpixels.substance.internal.utils.menu.SubstanceMenu;

public class SubstanceCheckBoxMenuItemUI
extends BasicCheckBoxMenuItemUI
implements SubstanceMenu,
TransitionAwareUI {
    protected Set lafWidgets;
    protected RolloverMenuItemListener substanceRolloverListener;
    protected StateTransitionTracker stateTransitionTracker;
    protected MenuUtilities.MenuPropertyListener substanceMenuPropertyListener;
    protected PropertyChangeListener substancePropertyListener;

    protected void __org__pushingpixels__substance__internal__ui__SubstanceCheckBoxMenuItemUI__installComponents(JMenuItem jMenuItem) {
        super.installComponents(jMenuItem);
    }

    @Override
    protected void installComponents(JMenuItem jMenuItem) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceCheckBoxMenuItemUI__installComponents(jMenuItem);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installComponents();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceCheckBoxMenuItemUI__installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceCheckBoxMenuItemUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceCheckBoxMenuItemUI__uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceCheckBoxMenuItemUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    @Override
    protected void installListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceCheckBoxMenuItemUI__installListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    @Override
    protected void installDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceCheckBoxMenuItemUI__installDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceCheckBoxMenuItemUI__uninstallComponents(JMenuItem jMenuItem) {
        super.uninstallComponents(jMenuItem);
    }

    @Override
    protected void uninstallComponents(JMenuItem jMenuItem) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceCheckBoxMenuItemUI__uninstallComponents(jMenuItem);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallComponents();
        }
    }

    @Override
    protected void uninstallListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceCheckBoxMenuItemUI__uninstallListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceCheckBoxMenuItemUI__uninstallDefaults() {
        super.uninstallDefaults();
    }

    @Override
    protected void uninstallDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceCheckBoxMenuItemUI__uninstallDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceCheckBoxMenuItemUI__update(Graphics graphics, JComponent jComponent) {
        super.update(graphics, jComponent);
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceCheckBoxMenuItemUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        JCheckBoxMenuItem item = (JCheckBoxMenuItem)comp;
        item.setRolloverEnabled(true);
        return new SubstanceCheckBoxMenuItemUI((JCheckBoxMenuItem)comp);
    }

    public SubstanceCheckBoxMenuItemUI(JCheckBoxMenuItem menuItem) {
        this.stateTransitionTracker = new StateTransitionTracker(menuItem, menuItem.getModel());
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceCheckBoxMenuItemUI__installListeners() {
        super.installListeners();
        this.substanceMenuPropertyListener = new MenuUtilities.MenuPropertyListener(this.menuItem);
        this.substanceMenuPropertyListener.install();
        this.substanceRolloverListener = new RolloverMenuItemListener(this.menuItem, this.stateTransitionTracker);
        this.menuItem.addMouseListener(this.substanceRolloverListener);
        this.stateTransitionTracker.registerModelListeners();
        this.substancePropertyListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("model".equals(evt.getPropertyName())) {
                    SubstanceCheckBoxMenuItemUI.this.stateTransitionTracker.setModel((ButtonModel)evt.getNewValue());
                }
                if ("font".equals(evt.getPropertyName())) {
                    SwingUtilities.invokeLater(new Runnable(){

                        @Override
                        public void run() {
                            if (SubstanceCheckBoxMenuItemUI.this.menuItem != null) {
                                SubstanceCheckBoxMenuItemUI.this.menuItem.updateUI();
                            }
                        }
                    });
                }
            }
        };
        this.menuItem.addPropertyChangeListener(this.substancePropertyListener);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceCheckBoxMenuItemUI__uninstallListeners() {
        super.uninstallListeners();
        this.substanceMenuPropertyListener.uninstall();
        this.substanceMenuPropertyListener = null;
        this.menuItem.removeMouseListener(this.substanceRolloverListener);
        this.substanceRolloverListener = null;
        this.menuItem.removePropertyChangeListener(this.substancePropertyListener);
        this.substancePropertyListener = null;
        this.stateTransitionTracker.unregisterModelListeners();
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceCheckBoxMenuItemUI__installDefaults() {
        super.installDefaults();
        if (this.checkIcon == null || this.checkIcon instanceof UIResource) {
            this.checkIcon = new CheckBoxMenuItemIcon(this.menuItem, 1 + SubstanceSizeUtils.getMenuCheckMarkSize(SubstanceSizeUtils.getComponentFontSize(this.menuItem)));
        }
        this.defaultTextIconGap = SubstanceSizeUtils.getTextIconGap(SubstanceSizeUtils.getComponentFontSize(this.menuItem));
    }

    @Override
    public JMenuItem getAssociatedMenuItem() {
        return this.menuItem;
    }

    @Override
    public Font getAcceleratorFont() {
        return this.acceleratorFont;
    }

    @Override
    public Icon getArrowIcon() {
        return this.arrowIcon;
    }

    @Override
    public Icon getCheckIcon() {
        return this.checkIcon;
    }

    @Override
    public int getDefaultTextIconGap() {
        return this.defaultTextIconGap;
    }

    @Override
    protected Dimension getPreferredMenuItemSize(JComponent c2, Icon checkIcon, Icon arrowIcon, int defaultTextIconGap) {
        Dimension superDim = super.getPreferredMenuItemSize(c2, checkIcon, arrowIcon, defaultTextIconGap);
        return new Dimension(MenuUtilities.getPreferredWidth(this.menuItem), superDim.height);
    }

    @Override
    protected void paintMenuItem(Graphics g2, JComponent c2, Icon checkIcon, Icon arrowIcon, Color background, Color foreground, int defaultTextIconGap) {
        MenuUtilities.paintMenuItem(g2, this.menuItem, checkIcon, arrowIcon, defaultTextIconGap);
    }

    @Override
    public StateTransitionTracker getTransitionTracker() {
        return this.stateTransitionTracker;
    }

    @Override
    public boolean isInside(MouseEvent me) {
        return true;
    }
}

