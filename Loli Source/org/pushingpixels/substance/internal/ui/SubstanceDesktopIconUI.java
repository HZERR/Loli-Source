/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicDesktopIconUI;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceInternalFrameTitlePane;

public class SubstanceDesktopIconUI
extends BasicDesktopIconUI {
    protected Set lafWidgets;
    private MouseInputListener substanceLabelMouseInputListener;
    private int width;

    @Override
    protected void installComponents() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceDesktopIconUI__installComponents();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installComponents();
        }
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceDesktopIconUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceDesktopIconUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    @Override
    protected void installListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceDesktopIconUI__installListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    @Override
    protected void installDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceDesktopIconUI__installDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    @Override
    protected void uninstallComponents() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceDesktopIconUI__uninstallComponents();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallComponents();
        }
    }

    @Override
    protected void uninstallListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceDesktopIconUI__uninstallListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceDesktopIconUI__uninstallDefaults() {
        super.uninstallDefaults();
    }

    @Override
    protected void uninstallDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceDesktopIconUI__uninstallDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceDesktopIconUI__update(Graphics graphics, JComponent jComponent) {
        super.update(graphics, jComponent);
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceDesktopIconUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstanceDesktopIconUI();
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceDesktopIconUI__installDefaults() {
        super.installDefaults();
        Font f2 = this.desktopIcon.getFont();
        if (f2 == null || f2 instanceof UIResource) {
            this.desktopIcon.setFont(UIManager.getFont("DesktopIcon.font"));
        }
        this.width = UIManager.getInt("DesktopIcon.width");
        this.desktopIcon.setBackground(SubstanceCoreUtilities.getSkin(this.desktopIcon.getInternalFrame()).getBackgroundColorScheme(DecorationAreaType.SECONDARY_TITLE_PANE).getBackgroundFillColor());
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceDesktopIconUI__installComponents() {
        this.frame = this.desktopIcon.getInternalFrame();
        this.iconPane = new SubstanceInternalFrameTitlePane(this.frame);
        this.iconPane.setOpaque(false);
        this.desktopIcon.setLayout(new BorderLayout());
        this.desktopIcon.add((Component)this.iconPane, "Center");
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceDesktopIconUI__uninstallComponents() {
        ((SubstanceInternalFrameTitlePane)this.iconPane).uninstall();
        this.desktopIcon.setLayout(null);
        this.desktopIcon.remove(this.iconPane);
        this.frame = null;
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceDesktopIconUI__installListeners() {
        super.installListeners();
        this.substanceLabelMouseInputListener = this.createMouseInputListener();
        this.iconPane.addMouseMotionListener(this.substanceLabelMouseInputListener);
        this.iconPane.addMouseListener(this.substanceLabelMouseInputListener);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceDesktopIconUI__uninstallListeners() {
        ((SubstanceInternalFrameTitlePane)this.iconPane).uninstallListeners();
        this.iconPane.removeMouseMotionListener(this.substanceLabelMouseInputListener);
        this.iconPane.removeMouseListener(this.substanceLabelMouseInputListener);
        this.substanceLabelMouseInputListener = null;
        super.uninstallListeners();
    }

    @Override
    public Dimension getPreferredSize(JComponent c2) {
        return this.getMinimumSize(c2);
    }

    @Override
    public Dimension getMinimumSize(JComponent c2) {
        return new Dimension(this.width, this.desktopIcon.getLayout().minimumLayoutSize((Container)this.desktopIcon).height);
    }

    @Override
    public Dimension getMaximumSize(JComponent c2) {
        return this.getMinimumSize(c2);
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceDesktopIconUI__installUI(JComponent c2) {
        super.installUI(c2);
        c2.setOpaque(false);
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceDesktopIconUI__uninstallUI(JComponent c2) {
        SubstanceInternalFrameTitlePane thePane = (SubstanceInternalFrameTitlePane)this.iconPane;
        super.uninstallUI(c2);
        thePane.uninstallListeners();
    }

    public JComponent getComponentForHover() {
        return this.iconPane;
    }

    public void installIfNecessary(JInternalFrame.JDesktopIcon jdi) {
        if (this.desktopIcon == null) {
            this.installUI(jdi);
        }
    }

    public void uninstallIfNecessary(JInternalFrame.JDesktopIcon jdi) {
        if (this.desktopIcon == jdi) {
            this.uninstallUI(jdi);
        }
    }

    void setWindowModified(boolean isWindowModified) {
        ((SubstanceInternalFrameTitlePane)this.iconPane).getCloseButton().putClientProperty("windowModified", isWindowModified);
    }
}

