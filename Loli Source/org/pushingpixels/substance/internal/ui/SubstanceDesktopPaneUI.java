/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicDesktopPaneUI;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.lafwidget.animation.effects.GhostPaintingUtils;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.internal.painter.BackgroundPaintingUtils;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.border.SubstanceBorder;

public class SubstanceDesktopPaneUI
extends BasicDesktopPaneUI {
    protected boolean containerGhostingMarker;
    protected Set lafWidgets;

    @Override
    protected void update(Graphics graphics, JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceDesktopPaneUI__container__update(graphics, jComponent);
        GhostPaintingUtils.paintGhostImages(jComponent, graphics);
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceDesktopPaneUI__installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceDesktopPaneUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceDesktopPaneUI__uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceDesktopPaneUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceDesktopPaneUI__installListeners() {
        super.installListeners();
    }

    @Override
    protected void installListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceDesktopPaneUI__installListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    @Override
    protected void installDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceDesktopPaneUI__installDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceDesktopPaneUI__uninstallListeners() {
        super.uninstallListeners();
    }

    @Override
    protected void uninstallListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceDesktopPaneUI__uninstallListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceDesktopPaneUI__uninstallDefaults() {
        super.uninstallDefaults();
    }

    @Override
    protected void uninstallDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceDesktopPaneUI__uninstallDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceDesktopPaneUI__container__update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceDesktopPaneUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstanceDesktopPaneUI();
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceDesktopPaneUI__installDefaults() {
        super.installDefaults();
        Border curr = this.desktop.getBorder();
        if (curr == null || curr instanceof UIResource) {
            this.desktop.setBorder(new SubstanceBorder());
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceDesktopPaneUI__update(Graphics g2, JComponent c2) {
        if (!c2.isShowing()) {
            return;
        }
        Graphics2D graphics = (Graphics2D)g2.create();
        graphics.setComposite(LafWidgetUtilities.getAlphaComposite((Component)c2, g2));
        if (SubstanceCoreUtilities.isOpaque(c2)) {
            Color back = c2.getBackground();
            if (back instanceof UIResource) {
                graphics.setColor(UIManager.getColor("Panel.background"));
                graphics.fillRect(0, 0, c2.getWidth(), c2.getHeight());
            }
            BackgroundPaintingUtils.updateIfOpaque(graphics, c2);
            super.paint(graphics, c2);
        } else {
            super.paint(graphics, c2);
        }
        graphics.dispose();
    }
}

