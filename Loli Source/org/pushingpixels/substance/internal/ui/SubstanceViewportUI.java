/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicViewportUI;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.internal.painter.BackgroundPaintingUtils;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;

public class SubstanceViewportUI
extends BasicViewportUI {
    protected Set lafWidgets;

    public void __org__pushingpixels__substance__internal__ui__SubstanceViewportUI__installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceViewportUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceViewportUI__uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceViewportUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    @Override
    protected void installDefaults(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceViewportUI__installDefaults(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceViewportUI__uninstallDefaults(JComponent jComponent) {
        super.uninstallDefaults(jComponent);
    }

    @Override
    protected void uninstallDefaults(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceViewportUI__uninstallDefaults(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceViewportUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstanceViewportUI();
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceViewportUI__installDefaults(JComponent c2) {
        Color backgroundFillColor;
        super.installDefaults(c2);
        Color backgr = c2.getBackground();
        if ((backgr == null || backgr instanceof UIResource) && (backgroundFillColor = SubstanceColorUtilities.getBackgroundFillColor(c2)) != null) {
            c2.setBackground(new ColorUIResource(backgroundFillColor));
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceViewportUI__update(Graphics g2, JComponent c2) {
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

