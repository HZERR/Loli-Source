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
import javax.swing.JToolBar;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolBarSeparatorUI;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.internal.painter.SeparatorPainterUtils;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;

public class SubstanceToolBarSeparatorUI
extends BasicToolBarSeparatorUI {
    protected Set lafWidgets;

    public void __org__pushingpixels__substance__internal__ui__SubstanceToolBarSeparatorUI__installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceToolBarSeparatorUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceToolBarSeparatorUI__uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceToolBarSeparatorUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceToolBarSeparatorUI__installListeners(JSeparator jSeparator) {
        super.installListeners(jSeparator);
    }

    @Override
    protected void installListeners(JSeparator jSeparator) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceToolBarSeparatorUI__installListeners(jSeparator);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceToolBarSeparatorUI__installDefaults(JSeparator jSeparator) {
        super.installDefaults(jSeparator);
    }

    @Override
    protected void installDefaults(JSeparator jSeparator) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceToolBarSeparatorUI__installDefaults(jSeparator);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceToolBarSeparatorUI__uninstallListeners(JSeparator jSeparator) {
        super.uninstallListeners(jSeparator);
    }

    @Override
    protected void uninstallListeners(JSeparator jSeparator) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceToolBarSeparatorUI__uninstallListeners(jSeparator);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceToolBarSeparatorUI__uninstallDefaults(JSeparator jSeparator) {
        super.uninstallDefaults(jSeparator);
    }

    @Override
    protected void uninstallDefaults(JSeparator jSeparator) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceToolBarSeparatorUI__uninstallDefaults(jSeparator);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceToolBarSeparatorUI__update(Graphics graphics, JComponent jComponent) {
        super.update(graphics, jComponent);
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceToolBarSeparatorUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstanceToolBarSeparatorUI();
    }

    @Override
    public void paint(Graphics g2, JComponent c2) {
        Graphics2D graphics = (Graphics2D)g2.create();
        SeparatorPainterUtils.paintSeparator(c2, graphics, c2.getWidth(), c2.getHeight(), ((JSeparator)c2).getOrientation());
        graphics.dispose();
    }

    @Override
    public Dimension getPreferredSize(JComponent c2) {
        Dimension size = ((JToolBar.Separator)c2).getSeparatorSize();
        if (size != null) {
            size = size.getSize();
        } else {
            size = new Dimension(6, 6);
            if (((JSeparator)c2).getOrientation() == 1) {
                size.height = 0;
            } else {
                size.width = 0;
            }
        }
        return size;
    }

    @Override
    public Dimension getMaximumSize(JComponent c2) {
        Dimension pref = this.getPreferredSize(c2);
        if (((JSeparator)c2).getOrientation() == 1) {
            return new Dimension(pref.width, 32767);
        }
        return new Dimension(32767, pref.height);
    }
}

