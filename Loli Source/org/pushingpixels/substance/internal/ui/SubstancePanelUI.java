/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicPanelUI;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.animation.effects.GhostPaintingUtils;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.internal.painter.BackgroundPaintingUtils;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;

public class SubstancePanelUI
extends BasicPanelUI {
    protected boolean containerGhostingMarker;
    protected Set lafWidgets;

    @Override
    protected void update(Graphics graphics, JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstancePanelUI__container__update(graphics, jComponent);
        GhostPaintingUtils.paintGhostImages(jComponent, graphics);
    }

    public void __org__pushingpixels__substance__internal__ui__SubstancePanelUI__installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstancePanelUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstancePanelUI__uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstancePanelUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    @Override
    protected void installDefaults(JPanel jPanel) {
        this.__org__pushingpixels__substance__internal__ui__SubstancePanelUI__installDefaults(jPanel);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstancePanelUI__uninstallDefaults(JPanel jPanel) {
        super.uninstallDefaults(jPanel);
    }

    @Override
    protected void uninstallDefaults(JPanel jPanel) {
        this.__org__pushingpixels__substance__internal__ui__SubstancePanelUI__uninstallDefaults(jPanel);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstancePanelUI__container__update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstancePanelUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstancePanelUI();
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstancePanelUI__installDefaults(JPanel p2) {
        Color backgroundFillColor;
        super.installDefaults(p2);
        Color backgr = p2.getBackground();
        if ((backgr == null || backgr instanceof UIResource) && (backgroundFillColor = SubstanceColorUtilities.getBackgroundFillColor(p2)) != null) {
            p2.setBackground(new ColorUIResource(backgroundFillColor));
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstancePanelUI__update(Graphics g2, JComponent c2) {
        if (!SubstanceLookAndFeel.isCurrentLookAndFeel()) {
            return;
        }
        if (this.toPaintBackground(c2)) {
            BackgroundPaintingUtils.update(g2, c2, false);
        }
        super.paint(g2, c2);
    }

    protected boolean toPaintBackground(JComponent c2) {
        return SubstanceCoreUtilities.isOpaque(c2);
    }
}

