/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSeparatorUI;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.internal.painter.SeparatorPainterUtils;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.menu.MenuUtilities;
import org.pushingpixels.substance.internal.utils.menu.SubstanceMenuBackgroundDelegate;

public class SubstanceSeparatorUI
extends BasicSeparatorUI {
    protected Set lafWidgets;

    public void __org__pushingpixels__substance__internal__ui__SubstanceSeparatorUI__installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceSeparatorUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceSeparatorUI__uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceSeparatorUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceSeparatorUI__installListeners(JSeparator jSeparator) {
        super.installListeners(jSeparator);
    }

    @Override
    protected void installListeners(JSeparator jSeparator) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceSeparatorUI__installListeners(jSeparator);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceSeparatorUI__installDefaults(JSeparator jSeparator) {
        super.installDefaults(jSeparator);
    }

    @Override
    protected void installDefaults(JSeparator jSeparator) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceSeparatorUI__installDefaults(jSeparator);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceSeparatorUI__uninstallListeners(JSeparator jSeparator) {
        super.uninstallListeners(jSeparator);
    }

    @Override
    protected void uninstallListeners(JSeparator jSeparator) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceSeparatorUI__uninstallListeners(jSeparator);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceSeparatorUI__uninstallDefaults(JSeparator jSeparator) {
        super.uninstallDefaults(jSeparator);
    }

    @Override
    protected void uninstallDefaults(JSeparator jSeparator) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceSeparatorUI__uninstallDefaults(jSeparator);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceSeparatorUI__update(Graphics graphics, JComponent jComponent) {
        super.update(graphics, jComponent);
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceSeparatorUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstanceSeparatorUI();
    }

    @Override
    public void paint(Graphics g2, JComponent c2) {
        Container parent = c2.getParent();
        if (!(parent instanceof JPopupMenu)) {
            SeparatorPainterUtils.paintSeparator(c2, g2, c2.getWidth(), c2.getHeight(), ((JSeparator)c2).getOrientation());
            return;
        }
        Graphics2D graphics = (Graphics2D)g2.create();
        int xOffset = MenuUtilities.getTextOffset(c2, parent);
        SubstanceMenuBackgroundDelegate.paintBackground(graphics, c2, xOffset);
        Dimension s2 = c2.getSize();
        int startX = 0;
        int width = s2.width;
        if (parent.getComponentOrientation().isLeftToRight()) {
            startX = xOffset - 2;
            width = s2.width - startX;
        } else {
            startX = 0;
            width = xOffset - 4;
        }
        graphics.translate(startX, 0);
        graphics.setComposite(LafWidgetUtilities.getAlphaComposite(parent));
        SeparatorPainterUtils.paintSeparator(c2, graphics, width, s2.height, ((JSeparator)c2).getOrientation(), true, 2);
        graphics.dispose();
    }

    @Override
    public Dimension getPreferredSize(JComponent c2) {
        float borderStrokeWidth = SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(c2));
        int prefSize = (int)Math.ceil(2.0 * (double)borderStrokeWidth);
        if (((JSeparator)c2).getOrientation() == 1) {
            return new Dimension(prefSize, 0);
        }
        return new Dimension(0, prefSize);
    }
}

