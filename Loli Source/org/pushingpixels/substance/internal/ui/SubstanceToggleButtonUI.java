/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonListener;
import javax.swing.plaf.basic.BasicToggleButtonUI;
import javax.swing.text.View;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.lafwidget.animation.AnimationConfigurationManager;
import org.pushingpixels.lafwidget.animation.AnimationFacet;
import org.pushingpixels.lafwidget.animation.effects.GhostPaintingUtils;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.api.shaper.SubstanceButtonShaper;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.animation.TransitionAwareUI;
import org.pushingpixels.substance.internal.utils.ButtonBackgroundDelegate;
import org.pushingpixels.substance.internal.utils.ButtonVisualStateTracker;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.SubstanceTextUtilities;
import org.pushingpixels.substance.internal.utils.border.SubstanceButtonBorder;
import org.pushingpixels.substance.internal.utils.icon.GlowingIcon;

public class SubstanceToggleButtonUI
extends BasicToggleButtonUI
implements TransitionAwareUI {
    protected boolean iconGhostingMarker;
    protected Set lafWidgets;
    private ButtonBackgroundDelegate delegate;
    protected GlowingIcon glowingIcon;
    protected PropertyChangeListener substancePropertyListener;
    protected ButtonVisualStateTracker substanceVisualStateTracker;
    protected JToggleButton toggleButton;
    private Rectangle viewRect = new Rectangle();
    private Rectangle iconRect = new Rectangle();
    private Rectangle textRect = new Rectangle();

    @Override
    protected void paintIcon(Graphics graphics, AbstractButton abstractButton, Rectangle rectangle) {
        abstractButton.putClientProperty("icon.bounds", new Rectangle(rectangle));
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        GhostPaintingUtils.paintGhostIcon(graphics2D, abstractButton, rectangle);
        graphics2D.dispose();
        this.__org__pushingpixels__substance__internal__ui__SubstanceToggleButtonUI__icon__paintIcon(graphics, abstractButton, rectangle);
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceToggleButtonUI__installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceToggleButtonUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceToggleButtonUI__uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceToggleButtonUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    @Override
    protected void installListeners(AbstractButton abstractButton) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceToggleButtonUI__installListeners(abstractButton);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    @Override
    public void installDefaults(AbstractButton abstractButton) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceToggleButtonUI__installDefaults(abstractButton);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    @Override
    protected void uninstallListeners(AbstractButton abstractButton) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceToggleButtonUI__uninstallListeners(abstractButton);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    @Override
    public void uninstallDefaults(AbstractButton abstractButton) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceToggleButtonUI__uninstallDefaults(abstractButton);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceToggleButtonUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstanceToggleButtonUI((JToggleButton)comp);
    }

    public SubstanceToggleButtonUI(JToggleButton toggleButton) {
        this.toggleButton = toggleButton;
        this.delegate = new ButtonBackgroundDelegate();
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceToggleButtonUI__installDefaults(AbstractButton b2) {
        super.installDefaults(b2);
        if (b2.getClientProperty("substancelaf.buttonborderoriginal") == null) {
            b2.putClientProperty("substancelaf.buttonborderoriginal", b2.getBorder());
        }
        if (b2.getClientProperty("substancelaf.buttonborderoriginal") == null) {
            b2.putClientProperty("substancelaf.buttonborderoriginal", b2.getBorder());
        }
        SubstanceButtonShaper shaper = SubstanceCoreUtilities.getButtonShaper(b2);
        if (b2.getClientProperty("substancelaf.buttonbordercomputed") == null) {
            b2.setBorder(shaper.getButtonBorder(b2));
        } else {
            Border currBorder = b2.getBorder();
            if (!(currBorder instanceof SubstanceButtonBorder)) {
                b2.setBorder(shaper.getButtonBorder(b2));
            } else {
                SubstanceButtonBorder sbCurrBorder = (SubstanceButtonBorder)currBorder;
                if (shaper.getClass() != sbCurrBorder.getButtonShaperClass()) {
                    b2.setBorder(shaper.getButtonBorder(b2));
                }
            }
        }
        b2.putClientProperty("substancelaf.buttonopacityoriginal", b2.isOpaque());
        b2.setOpaque(false);
        b2.setRolloverEnabled(true);
        LookAndFeel.installProperty(b2, "iconTextGap", SubstanceSizeUtils.getTextIconGap(SubstanceSizeUtils.getComponentFontSize(b2)));
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceToggleButtonUI__uninstallDefaults(AbstractButton b2) {
        super.uninstallDefaults(b2);
        b2.setBorder((Border)b2.getClientProperty("substancelaf.buttonborderoriginal"));
        b2.setOpaque((Boolean)b2.getClientProperty("substancelaf.buttonopacityoriginal"));
        b2.putClientProperty("substancelaf.buttonopacityoriginal", null);
    }

    @Override
    protected BasicButtonListener createButtonListener(AbstractButton b2) {
        return null;
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceToggleButtonUI__installListeners(AbstractButton b2) {
        super.installListeners(b2);
        this.substanceVisualStateTracker = new ButtonVisualStateTracker();
        this.substanceVisualStateTracker.installListeners(b2, true);
        this.trackGlowingIcon();
        this.substancePropertyListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("icon".equals(evt.getPropertyName())) {
                    SubstanceToggleButtonUI.this.trackGlowingIcon();
                }
            }
        };
        b2.addPropertyChangeListener(this.substancePropertyListener);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceToggleButtonUI__uninstallListeners(AbstractButton b2) {
        this.substanceVisualStateTracker.uninstallListeners(b2);
        this.substanceVisualStateTracker = null;
        b2.removePropertyChangeListener(this.substancePropertyListener);
        this.substancePropertyListener = null;
        super.uninstallListeners(b2);
    }

    @Override
    public void paint(Graphics g2, JComponent c2) {
        AbstractButton b2 = (AbstractButton)c2;
        FontMetrics fm = g2.getFontMetrics();
        Insets i2 = c2.getInsets();
        this.viewRect.x = i2.left;
        this.viewRect.y = i2.top;
        this.viewRect.width = b2.getWidth() - (i2.right + this.viewRect.x);
        this.viewRect.height = b2.getHeight() - (i2.bottom + this.viewRect.y);
        this.textRect.height = 0;
        this.textRect.width = 0;
        this.textRect.y = 0;
        this.textRect.x = 0;
        this.iconRect.height = 0;
        this.iconRect.width = 0;
        this.iconRect.y = 0;
        this.iconRect.x = 0;
        Font f2 = c2.getFont();
        String text = SwingUtilities.layoutCompoundLabel(c2, fm, b2.getText(), b2.getIcon(), b2.getVerticalAlignment(), b2.getHorizontalAlignment(), b2.getVerticalTextPosition(), b2.getHorizontalTextPosition(), this.viewRect, this.iconRect, this.textRect, b2.getText() == null ? 0 : b2.getIconTextGap());
        Graphics2D g2d = (Graphics2D)g2.create();
        View v2 = (View)c2.getClientProperty("html");
        g2d.setFont(f2);
        this.delegate.updateBackground(g2d, b2);
        if (v2 != null) {
            v2.paint(g2d, this.textRect);
        } else {
            this.paintButtonText(g2d, b2, this.textRect, text);
        }
        if (b2.getIcon() != null) {
            this.paintIcon((Graphics)g2d, b2, this.iconRect);
        }
        if (b2.isFocusPainted()) {
            SubstanceCoreUtilities.paintFocus(g2, b2, b2, this, null, this.textRect, 1.0f, SubstanceSizeUtils.getFocusRingPadding(SubstanceSizeUtils.getComponentFontSize(b2)));
        }
    }

    @Override
    public Dimension getPreferredSize(JComponent c2) {
        AbstractButton button = (AbstractButton)c2;
        SubstanceButtonShaper shaper = SubstanceCoreUtilities.getButtonShaper(button);
        Dimension superPref = super.getPreferredSize(button);
        if (superPref == null) {
            return null;
        }
        if (shaper == null) {
            return superPref;
        }
        return shaper.getPreferredSize(button, superPref);
    }

    @Override
    public boolean contains(JComponent c2, int x2, int y2) {
        return ButtonBackgroundDelegate.contains((JToggleButton)c2, x2, y2);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceToggleButtonUI__icon__paintIcon(Graphics g2, AbstractButton b2, Rectangle iconRect) {
        Graphics2D graphics = (Graphics2D)g2.create();
        Icon originalIcon = SubstanceCoreUtilities.getOriginalIcon(b2, b2.getIcon());
        Icon themedIcon = !(b2 instanceof JRadioButton) && !(b2 instanceof JCheckBox) && SubstanceCoreUtilities.useThemedDefaultIcon(b2) ? SubstanceCoreUtilities.getThemedIcon(b2, originalIcon) : originalIcon;
        graphics.setComposite(LafWidgetUtilities.getAlphaComposite((Component)b2, g2));
        float activeAmount = this.substanceVisualStateTracker.getStateTransitionTracker().getActiveStrength();
        if (activeAmount >= 0.0f) {
            if (AnimationConfigurationManager.getInstance().isAnimationAllowed(AnimationFacet.ICON_GLOW, b2) && this.substanceVisualStateTracker.getStateTransitionTracker().getIconGlowTracker().isPlaying()) {
                this.glowingIcon.paintIcon(b2, graphics, iconRect.x, iconRect.y);
            } else {
                themedIcon.paintIcon(b2, graphics, iconRect.x, iconRect.y);
                graphics.setComposite(LafWidgetUtilities.getAlphaComposite(b2, activeAmount, g2));
                originalIcon.paintIcon(b2, graphics, iconRect.x, iconRect.y);
            }
        } else {
            originalIcon.paintIcon(b2, graphics, iconRect.x, iconRect.y);
        }
        graphics.dispose();
    }

    protected void paintButtonText(Graphics g2, AbstractButton button, Rectangle textRect, String text) {
        SubstanceTextUtilities.paintText(g2, button, textRect, text, button.getDisplayedMnemonicIndex());
    }

    protected void trackGlowingIcon() {
        Icon currIcon = this.toggleButton.getIcon();
        if (currIcon instanceof GlowingIcon) {
            return;
        }
        if (currIcon == null) {
            return;
        }
        this.glowingIcon = new GlowingIcon(currIcon, this.substanceVisualStateTracker.getStateTransitionTracker().getIconGlowTracker());
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceToggleButtonUI__update(Graphics g2, JComponent c2) {
        this.paint(g2, c2);
    }

    @Override
    public boolean isInside(MouseEvent me) {
        return this.contains(this.toggleButton, me.getX(), me.getY());
    }

    @Override
    public StateTransitionTracker getTransitionTracker() {
        return this.substanceVisualStateTracker.getStateTransitionTracker();
    }
}

