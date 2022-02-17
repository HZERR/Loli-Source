/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.LookAndFeel;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuUI;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.animation.effects.GhostPaintingUtils;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.animation.TransitionAwareUI;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.icon.MenuArrowIcon;
import org.pushingpixels.substance.internal.utils.menu.MenuUtilities;
import org.pushingpixels.substance.internal.utils.menu.SubstanceMenu;

public class SubstanceMenuUI
extends BasicMenuUI
implements SubstanceMenu,
TransitionAwareUI {
    protected boolean containerGhostingMarker;
    protected Set lafWidgets;
    protected MouseListener substanceMouseListener;
    protected StateTransitionTracker stateTransitionTracker;
    protected MenuUtilities.MenuPropertyListener substanceMenuPropertyListener;
    protected PropertyChangeListener substancePropertyListener;
    protected FocusListener substanceFocusListener;

    @Override
    protected void update(Graphics graphics, JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceMenuUI__container__update(graphics, jComponent);
        GhostPaintingUtils.paintGhostImages(jComponent, graphics);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceMenuUI__installComponents(JMenuItem jMenuItem) {
        super.installComponents(jMenuItem);
    }

    @Override
    protected void installComponents(JMenuItem jMenuItem) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceMenuUI__installComponents(jMenuItem);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installComponents();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceMenuUI__installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceMenuUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceMenuUI__uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceMenuUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    @Override
    protected void installListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceMenuUI__installListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    @Override
    protected void installDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceMenuUI__installDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceMenuUI__uninstallComponents(JMenuItem jMenuItem) {
        super.uninstallComponents(jMenuItem);
    }

    @Override
    protected void uninstallComponents(JMenuItem jMenuItem) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceMenuUI__uninstallComponents(jMenuItem);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallComponents();
        }
    }

    @Override
    protected void uninstallListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceMenuUI__uninstallListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceMenuUI__uninstallDefaults() {
        super.uninstallDefaults();
    }

    @Override
    protected void uninstallDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceMenuUI__uninstallDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceMenuUI__update(Graphics graphics, JComponent jComponent) {
        super.update(graphics, jComponent);
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceMenuUI__container__update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceMenuUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstanceMenuUI((JMenu)comp);
    }

    public SubstanceMenuUI(JMenu menuItem) {
        this.stateTransitionTracker = new StateTransitionTracker(menuItem, menuItem.getModel());
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceMenuUI__installDefaults() {
        super.installDefaults();
        this.menuItem.setRolloverEnabled(true);
        this.arrowIcon = new MenuArrowIcon((JMenu)this.menuItem);
        this.defaultTextIconGap = SubstanceSizeUtils.getTextIconGap(SubstanceSizeUtils.getComponentFontSize(this.menuItem));
        this.menuItem.putClientProperty("substancelaf.componentFlat", Boolean.TRUE);
        LookAndFeel.installProperty(this.menuItem, "opaque", Boolean.FALSE);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceMenuUI__installListeners() {
        super.installListeners();
        this.substanceMenuPropertyListener = new MenuUtilities.MenuPropertyListener(this.menuItem);
        this.substanceMenuPropertyListener.install();
        this.stateTransitionTracker.registerModelListeners();
        this.substanceMouseListener = new MouseAdapter(){

            private boolean toRepaint() {
                MenuElement[] selectedMenuPath;
                for (MenuElement elem : selectedMenuPath = MenuSelectionManager.defaultManager().getSelectedPath()) {
                    if (elem != SubstanceMenuUI.this.menuItem) continue;
                    return true;
                }
                return selectedMenuPath.length == 0;
            }

            @Override
            public void mouseEntered(MouseEvent e2) {
                if (this.toRepaint()) {
                    SubstanceMenuUI.this.stateTransitionTracker.turnOffModelChangeTracking();
                    SubstanceMenuUI.this.menuItem.getModel().setRollover(true);
                    SubstanceMenuUI.this.stateTransitionTracker.onModelStateChanged();
                    Rectangle bounds = SubstanceMenuUI.this.menuItem.getBounds();
                    SubstanceMenuUI.this.menuItem.getParent().repaint(bounds.x, bounds.y, bounds.width, bounds.height);
                }
            }

            @Override
            public void mouseExited(MouseEvent e2) {
                if (this.toRepaint()) {
                    SubstanceMenuUI.this.stateTransitionTracker.turnOffModelChangeTracking();
                    SubstanceMenuUI.this.menuItem.getModel().setRollover(false);
                    SubstanceMenuUI.this.stateTransitionTracker.onModelStateChanged();
                    Rectangle bounds = SubstanceMenuUI.this.menuItem.getBounds();
                    SubstanceMenuUI.this.menuItem.getParent().repaint(bounds.x, bounds.y, bounds.width, bounds.height);
                }
            }
        };
        this.menuItem.addMouseListener(this.substanceMouseListener);
        this.substanceFocusListener = new FocusAdapter(){

            private boolean toRepaint() {
                MenuElement[] selectedMenuPath;
                for (MenuElement elem : selectedMenuPath = MenuSelectionManager.defaultManager().getSelectedPath()) {
                    if (elem != SubstanceMenuUI.this.menuItem) continue;
                    return true;
                }
                return selectedMenuPath.length == 0;
            }

            @Override
            public void focusLost(FocusEvent e2) {
                if (this.toRepaint()) {
                    SubstanceMenuUI.this.stateTransitionTracker.turnOffModelChangeTracking();
                    SubstanceMenuUI.this.menuItem.getModel().setRollover(false);
                    SubstanceMenuUI.this.stateTransitionTracker.onModelStateChanged();
                    Rectangle bounds = SubstanceMenuUI.this.menuItem.getBounds();
                    SubstanceMenuUI.this.menuItem.getParent().repaint(bounds.x, bounds.y, bounds.width, bounds.height);
                }
            }
        };
        this.menuItem.addFocusListener(this.substanceFocusListener);
        this.substancePropertyListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("model".equals(evt.getPropertyName())) {
                    SubstanceMenuUI.this.stateTransitionTracker.setModel((ButtonModel)evt.getNewValue());
                }
                if ("font".equals(evt.getPropertyName())) {
                    SwingUtilities.invokeLater(new Runnable(){

                        @Override
                        public void run() {
                            if (SubstanceMenuUI.this.menuItem != null) {
                                SubstanceMenuUI.this.menuItem.updateUI();
                            }
                        }
                    });
                }
            }
        };
        this.menuItem.addPropertyChangeListener(this.substancePropertyListener);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceMenuUI__uninstallListeners() {
        super.uninstallListeners();
        this.substanceMenuPropertyListener.uninstall();
        this.substanceMenuPropertyListener = null;
        this.menuItem.removeMouseListener(this.substanceMouseListener);
        this.substanceMouseListener = null;
        this.menuItem.removeFocusListener(this.substanceFocusListener);
        this.substanceFocusListener = null;
        this.menuItem.removePropertyChangeListener(this.substancePropertyListener);
        this.substancePropertyListener = null;
        this.stateTransitionTracker.unregisterModelListeners();
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
        return null;
    }

    @Override
    public int getDefaultTextIconGap() {
        return this.defaultTextIconGap;
    }

    @Override
    protected Dimension getPreferredMenuItemSize(JComponent c2, Icon checkIcon, Icon arrowIcon, int defaultTextIconGap) {
        Dimension superDim = super.getPreferredMenuItemSize(c2, checkIcon, arrowIcon, defaultTextIconGap);
        if (MenuUtilities.getPopupLayoutMetrics(this.menuItem, false) != null) {
            return new Dimension(MenuUtilities.getPreferredWidth(this.menuItem), superDim.height);
        }
        return superDim;
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

