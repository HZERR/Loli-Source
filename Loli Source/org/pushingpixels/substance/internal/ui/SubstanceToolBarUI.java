/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolBarUI;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.animation.effects.GhostPaintingUtils;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.internal.painter.BackgroundPaintingUtils;
import org.pushingpixels.substance.internal.painter.DecorationPainterUtils;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;

public class SubstanceToolBarUI
extends BasicToolBarUI {
    protected boolean containerGhostingMarker;
    protected Set lafWidgets;

    @Override
    protected void update(Graphics graphics, JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceToolBarUI__container__update(graphics, jComponent);
        GhostPaintingUtils.paintGhostImages(jComponent, graphics);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceToolBarUI__installComponents() {
        super.installComponents();
    }

    @Override
    protected void installComponents() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceToolBarUI__installComponents();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installComponents();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceToolBarUI__installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceToolBarUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceToolBarUI__uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceToolBarUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceToolBarUI__installListeners() {
        super.installListeners();
    }

    @Override
    protected void installListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceToolBarUI__installListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    @Override
    protected void installDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceToolBarUI__installDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceToolBarUI__uninstallComponents() {
        super.uninstallComponents();
    }

    @Override
    protected void uninstallComponents() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceToolBarUI__uninstallComponents();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallComponents();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceToolBarUI__uninstallListeners() {
        super.uninstallListeners();
    }

    @Override
    protected void uninstallListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceToolBarUI__uninstallListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    @Override
    protected void uninstallDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceToolBarUI__uninstallDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceToolBarUI__container__update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceToolBarUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstanceToolBarUI();
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceToolBarUI__installDefaults() {
        super.installDefaults();
        SubstanceLookAndFeel.setDecorationType(this.toolBar, DecorationAreaType.TOOLBAR);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceToolBarUI__uninstallDefaults() {
        DecorationPainterUtils.clearDecorationType(this.toolBar);
        super.uninstallDefaults();
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceToolBarUI__update(Graphics g2, JComponent c2) {
        boolean isOpaque = SubstanceCoreUtilities.isOpaque(c2);
        if (isOpaque) {
            BackgroundPaintingUtils.update(g2, c2, false);
        } else {
            super.update(g2, c2);
        }
    }

    @Override
    protected void setBorderToRollover(Component c2) {
    }

    @Override
    protected void setBorderToNonRollover(Component c2) {
    }

    @Override
    protected void setBorderToNormal(Component c2) {
    }
}

