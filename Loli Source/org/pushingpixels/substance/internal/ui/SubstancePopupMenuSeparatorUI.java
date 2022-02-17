/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPopupMenuSeparatorUI;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.internal.painter.SeparatorPainterUtils;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.menu.MenuUtilities;
import org.pushingpixels.substance.internal.utils.menu.SubstanceMenuBackgroundDelegate;

public class SubstancePopupMenuSeparatorUI
extends BasicPopupMenuSeparatorUI {
    protected Set lafWidgets;

    public void __org__pushingpixels__substance__internal__ui__SubstancePopupMenuSeparatorUI__installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstancePopupMenuSeparatorUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstancePopupMenuSeparatorUI__uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstancePopupMenuSeparatorUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstancePopupMenuSeparatorUI__installListeners(JSeparator jSeparator) {
        super.installListeners(jSeparator);
    }

    @Override
    protected void installListeners(JSeparator jSeparator) {
        this.__org__pushingpixels__substance__internal__ui__SubstancePopupMenuSeparatorUI__installListeners(jSeparator);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstancePopupMenuSeparatorUI__installDefaults(JSeparator jSeparator) {
        super.installDefaults(jSeparator);
    }

    @Override
    protected void installDefaults(JSeparator jSeparator) {
        this.__org__pushingpixels__substance__internal__ui__SubstancePopupMenuSeparatorUI__installDefaults(jSeparator);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstancePopupMenuSeparatorUI__uninstallListeners(JSeparator jSeparator) {
        super.uninstallListeners(jSeparator);
    }

    @Override
    protected void uninstallListeners(JSeparator jSeparator) {
        this.__org__pushingpixels__substance__internal__ui__SubstancePopupMenuSeparatorUI__uninstallListeners(jSeparator);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstancePopupMenuSeparatorUI__uninstallDefaults(JSeparator jSeparator) {
        super.uninstallDefaults(jSeparator);
    }

    @Override
    protected void uninstallDefaults(JSeparator jSeparator) {
        this.__org__pushingpixels__substance__internal__ui__SubstancePopupMenuSeparatorUI__uninstallDefaults(jSeparator);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstancePopupMenuSeparatorUI__update(Graphics graphics, JComponent jComponent) {
        super.update(graphics, jComponent);
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstancePopupMenuSeparatorUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstancePopupMenuSeparatorUI();
    }

    @Override
    public void paint(Graphics g2, JComponent c2) {
        Graphics2D graphics = (Graphics2D)g2.create();
        JSeparator sep = (JSeparator)c2;
        int xOffset = MenuUtilities.getTextOffset(sep, sep.getParent());
        SubstanceMenuBackgroundDelegate.paintBackground(graphics, c2, xOffset);
        Dimension s2 = c2.getSize();
        int startX = 0;
        int width = s2.width;
        if (c2.getComponentOrientation().isLeftToRight()) {
            startX = xOffset - 2;
            width = s2.width - startX;
        } else {
            startX = 0;
            width = xOffset > 0 ? xOffset - 4 : s2.width;
        }
        graphics.translate(startX, 0);
        graphics.setComposite(LafWidgetUtilities.getAlphaComposite(sep));
        SeparatorPainterUtils.paintSeparator(sep, graphics, width, s2.height, sep.getOrientation(), true, 2);
        graphics.dispose();
    }

    @Override
    public Dimension getPreferredSize(JComponent c2) {
        float borderStrokeWidth = SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(c2));
        int prefSize = (int)Math.ceil(2.0 * (double)borderStrokeWidth);
        return new Dimension(0, prefSize);
    }
}

