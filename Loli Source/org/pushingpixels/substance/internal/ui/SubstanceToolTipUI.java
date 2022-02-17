/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JToolTip;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolTipUI;
import javax.swing.text.View;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceTextUtilities;

public class SubstanceToolTipUI
extends BasicToolTipUI {
    protected Set lafWidgets;

    public void __org__pushingpixels__substance__internal__ui__SubstanceToolTipUI__installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceToolTipUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceToolTipUI__uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceToolTipUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceToolTipUI__installListeners(JComponent jComponent) {
        super.installListeners(jComponent);
    }

    @Override
    protected void installListeners(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceToolTipUI__installListeners(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceToolTipUI__installDefaults(JComponent jComponent) {
        super.installDefaults(jComponent);
    }

    @Override
    protected void installDefaults(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceToolTipUI__installDefaults(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceToolTipUI__uninstallListeners(JComponent jComponent) {
        super.uninstallListeners(jComponent);
    }

    @Override
    protected void uninstallListeners(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceToolTipUI__uninstallListeners(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceToolTipUI__uninstallDefaults(JComponent jComponent) {
        super.uninstallDefaults(jComponent);
    }

    @Override
    protected void uninstallDefaults(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceToolTipUI__uninstallDefaults(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceToolTipUI__update(Graphics graphics, JComponent jComponent) {
        super.update(graphics, jComponent);
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceToolTipUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstanceToolTipUI();
    }

    @Override
    public void paint(Graphics g2, JComponent c2) {
        Font font = c2.getFont();
        Dimension size = c2.getSize();
        if (c2.isOpaque()) {
            g2.setColor(c2.getBackground());
            g2.fillRect(0, 0, size.width, size.height);
        }
        g2.setColor(c2.getForeground());
        g2.setFont(font);
        String tipText = ((JToolTip)c2).getTipText();
        if (tipText == null) {
            tipText = "";
        }
        Insets insets = c2.getInsets();
        Rectangle paintTextR = new Rectangle(insets.left + 3, insets.top, size.width - (insets.left + insets.right + 6), size.height - (insets.top + insets.bottom + 2));
        View v2 = (View)c2.getClientProperty("html");
        if (v2 != null) {
            v2.paint(g2, paintTextR);
        } else {
            SubstanceTextUtilities.paintText(g2, c2, paintTextR, tipText, -1, font, c2.getForeground(), null);
        }
    }

    @Override
    public Dimension getPreferredSize(JComponent c2) {
        Font font = c2.getFont();
        Insets insets = c2.getInsets();
        Dimension prefSize = new Dimension(insets.left + insets.right, insets.top + insets.bottom);
        String text = ((JToolTip)c2).getTipText();
        if (text == null || text.equals("")) {
            text = "";
        } else {
            View v2;
            View view = v2 = c2 != null ? (View)c2.getClientProperty("html") : null;
            if (v2 != null) {
                prefSize.width += (int)(v2.getPreferredSpan(0) + 6.0f);
                prefSize.height += (int)(v2.getPreferredSpan(1) + 2.0f);
            } else {
                FontMetrics fm = c2.getFontMetrics(font);
                prefSize.width += fm.stringWidth(text) + 6;
                prefSize.height += fm.getHeight() + 2;
            }
        }
        return prefSize;
    }
}

