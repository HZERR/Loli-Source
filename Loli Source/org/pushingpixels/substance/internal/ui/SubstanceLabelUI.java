/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.text.View;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.internal.painter.BackgroundPaintingUtils;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceTextUtilities;

public class SubstanceLabelUI
extends BasicLabelUI {
    protected Set lafWidgets;
    protected PropertyChangeListener substancePropertyChangeListener;
    private Rectangle paintIconR = new Rectangle();
    private Rectangle paintTextR = new Rectangle();
    private Rectangle paintViewR = new Rectangle();
    private Insets paintViewInsets = new Insets(0, 0, 0, 0);

    protected void __org__pushingpixels__substance__internal__ui__SubstanceLabelUI__installComponents(JLabel jLabel) {
        super.installComponents(jLabel);
    }

    @Override
    protected void installComponents(JLabel jLabel) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceLabelUI__installComponents(jLabel);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installComponents();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceLabelUI__installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceLabelUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceLabelUI__uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceLabelUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    @Override
    protected void installListeners(JLabel jLabel) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceLabelUI__installListeners(jLabel);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceLabelUI__installDefaults(JLabel jLabel) {
        super.installDefaults(jLabel);
    }

    @Override
    protected void installDefaults(JLabel jLabel) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceLabelUI__installDefaults(jLabel);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceLabelUI__uninstallComponents(JLabel jLabel) {
        super.uninstallComponents(jLabel);
    }

    @Override
    protected void uninstallComponents(JLabel jLabel) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceLabelUI__uninstallComponents(jLabel);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallComponents();
        }
    }

    @Override
    protected void uninstallListeners(JLabel jLabel) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceLabelUI__uninstallListeners(jLabel);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceLabelUI__uninstallDefaults(JLabel jLabel) {
        super.uninstallDefaults(jLabel);
    }

    @Override
    protected void uninstallDefaults(JLabel jLabel) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceLabelUI__uninstallDefaults(jLabel);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceLabelUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstanceLabelUI();
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceLabelUI__installListeners(final JLabel c2) {
        super.installListeners(c2);
        this.substancePropertyChangeListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("opaque".equals(evt.getPropertyName()) && !Boolean.TRUE.equals(c2.getClientProperty("substancelaf.lockopacity"))) {
                    c2.putClientProperty("substancelaf.buttonopacityoriginal", evt.getNewValue());
                }
            }
        };
        c2.addPropertyChangeListener(this.substancePropertyChangeListener);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceLabelUI__uninstallListeners(JLabel c2) {
        c2.removePropertyChangeListener(this.substancePropertyChangeListener);
        this.substancePropertyChangeListener = null;
        super.uninstallListeners(c2);
    }

    @Override
    public void paint(Graphics g2, JComponent c2) {
        JLabel label = (JLabel)c2;
        String text = label.getText();
        Icon icon = null;
        if (label.isEnabled()) {
            icon = label.getIcon();
            if (icon != null && SubstanceCoreUtilities.useThemedDefaultIcon(label)) {
                icon = SubstanceCoreUtilities.getThemedIcon(label, icon);
            }
        } else {
            icon = label.getDisabledIcon();
        }
        if (icon == null && text == null) {
            return;
        }
        Insets insets = label.getInsets(this.paintViewInsets);
        this.paintViewR.x = insets.left;
        this.paintViewR.y = insets.top;
        this.paintViewR.width = c2.getWidth() - (insets.left + insets.right);
        this.paintViewR.height = c2.getHeight() - (insets.top + insets.bottom);
        this.paintIconR.height = 0;
        this.paintIconR.width = 0;
        this.paintIconR.y = 0;
        this.paintIconR.x = 0;
        this.paintTextR.height = 0;
        this.paintTextR.width = 0;
        this.paintTextR.y = 0;
        this.paintTextR.x = 0;
        String clippedText = SwingUtilities.layoutCompoundLabel(label, g2.getFontMetrics(), text, icon, label.getVerticalAlignment(), label.getHorizontalAlignment(), label.getVerticalTextPosition(), label.getHorizontalTextPosition(), this.paintViewR, this.paintIconR, this.paintTextR, label.getIconTextGap());
        Graphics2D g2d = (Graphics2D)g2.create();
        BackgroundPaintingUtils.updateIfOpaque(g2d, c2);
        if (icon != null) {
            icon.paintIcon(c2, g2d, this.paintIconR.x, this.paintIconR.y);
        }
        ComponentState labelState = label.isEnabled() ? ComponentState.ENABLED : ComponentState.DISABLED_UNSELECTED;
        float labelAlpha = SubstanceColorSchemeUtilities.getAlpha(label, labelState);
        if (text != null) {
            View v2 = (View)c2.getClientProperty("html");
            if (v2 != null) {
                v2.paint(g2d, this.paintTextR);
            } else {
                SubstanceTextUtilities.paintText(g2, (JComponent)label, this.paintTextR, clippedText, label.getDisplayedMnemonicIndex(), labelState, labelAlpha);
            }
        }
        g2d.dispose();
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceLabelUI__update(Graphics g2, JComponent c2) {
        if (!SubstanceLookAndFeel.isCurrentLookAndFeel()) {
            return;
        }
        this.paint(g2, c2);
    }
}

