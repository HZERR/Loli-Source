/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.AlphaComposite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.Set;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicButtonListener;
import javax.swing.plaf.basic.BasicRadioButtonUI;
import javax.swing.text.View;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.ComponentStateFacet;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.painter.border.SubstanceBorderPainter;
import org.pushingpixels.substance.api.painter.fill.SubstanceFillPainter;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.animation.TransitionAwareUI;
import org.pushingpixels.substance.internal.painter.BackgroundPaintingUtils;
import org.pushingpixels.substance.internal.utils.HashMapKey;
import org.pushingpixels.substance.internal.utils.LazyResettableHashMap;
import org.pushingpixels.substance.internal.utils.RolloverButtonListener;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceImageCreator;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.SubstanceTextUtilities;

public class SubstanceRadioButtonUI
extends BasicRadioButtonUI
implements TransitionAwareUI {
    protected Set lafWidgets;
    protected PropertyChangeListener substancePropertyListener;
    protected JToggleButton button;
    private static LazyResettableHashMap<Icon> icons = new LazyResettableHashMap("SubstanceRadioButtonUI");
    protected StateTransitionTracker stateTransitionTracker;
    private Rectangle viewRect = new Rectangle();
    private Rectangle iconRect = new Rectangle();
    private Rectangle textRect = new Rectangle();

    public void __org__pushingpixels__substance__internal__ui__SubstanceRadioButtonUI__installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceRadioButtonUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceRadioButtonUI__uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceRadioButtonUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    @Override
    protected void installListeners(AbstractButton abstractButton) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceRadioButtonUI__installListeners(abstractButton);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    @Override
    protected void installDefaults(AbstractButton abstractButton) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceRadioButtonUI__installDefaults(abstractButton);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    @Override
    protected void uninstallListeners(AbstractButton abstractButton) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceRadioButtonUI__uninstallListeners(abstractButton);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceRadioButtonUI__uninstallDefaults(AbstractButton abstractButton) {
        super.uninstallDefaults(abstractButton);
    }

    @Override
    protected void uninstallDefaults(AbstractButton abstractButton) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceRadioButtonUI__uninstallDefaults(abstractButton);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceRadioButtonUI__update(Graphics graphics, JComponent jComponent) {
        super.update(graphics, jComponent);
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceRadioButtonUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceRadioButtonUI__installListeners(final AbstractButton b2) {
        super.installListeners(b2);
        this.stateTransitionTracker.registerModelListeners();
        this.stateTransitionTracker.registerFocusListeners();
        this.substancePropertyListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("model".equals(evt.getPropertyName())) {
                    SubstanceRadioButtonUI.this.stateTransitionTracker.setModel((ButtonModel)evt.getNewValue());
                }
                if ("font".equals(evt.getPropertyName())) {
                    SwingUtilities.invokeLater(new Runnable(){

                        @Override
                        public void run() {
                            b2.updateUI();
                        }
                    });
                }
            }
        };
        b2.addPropertyChangeListener(this.substancePropertyListener);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceRadioButtonUI__installDefaults(AbstractButton b2) {
        super.installDefaults(b2);
        Border border = b2.getBorder();
        if (border == null || border instanceof UIResource) {
            b2.setBorder(SubstanceSizeUtils.getRadioButtonBorder(SubstanceSizeUtils.getComponentFontSize(b2), b2.getComponentOrientation().isLeftToRight()));
        }
        this.button.setRolloverEnabled(true);
        LookAndFeel.installProperty(b2, "iconTextGap", SubstanceSizeUtils.getTextIconGap(SubstanceSizeUtils.getComponentFontSize(b2)));
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceRadioButtonUI__uninstallListeners(AbstractButton b2) {
        b2.removePropertyChangeListener(this.substancePropertyListener);
        this.substancePropertyListener = null;
        this.stateTransitionTracker.unregisterModelListeners();
        this.stateTransitionTracker.unregisterFocusListeners();
        super.uninstallListeners(b2);
    }

    private static Icon getIcon(JToggleButton button, StateTransitionTracker stateTransitionTracker) {
        StateTransitionTracker.ModelStateInfo modelStateInfo = stateTransitionTracker.getModelStateInfo();
        Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo.getStateContributionMap();
        int fontSize = SubstanceSizeUtils.getComponentFontSize(button);
        int checkMarkSize = SubstanceSizeUtils.getRadioButtonMarkSize(fontSize);
        SubstanceFillPainter fillPainter = SubstanceCoreUtilities.getFillPainter(button);
        SubstanceBorderPainter borderPainter = SubstanceCoreUtilities.getBorderPainter(button);
        ComponentState currState = modelStateInfo.getCurrModelState();
        SubstanceColorScheme baseFillColorScheme = SubstanceColorSchemeUtilities.getColorScheme(button, ColorSchemeAssociationKind.FILL, currState);
        SubstanceColorScheme baseMarkColorScheme = SubstanceColorSchemeUtilities.getColorScheme(button, ColorSchemeAssociationKind.MARK, currState);
        SubstanceColorScheme baseBorderColorScheme = SubstanceColorSchemeUtilities.getColorScheme(button, ColorSchemeAssociationKind.BORDER, currState);
        float visibility = stateTransitionTracker.getFacetStrength(ComponentStateFacet.SELECTION);
        HashMapKey keyBase = SubstanceCoreUtilities.getHashKey(fontSize, checkMarkSize, fillPainter.getDisplayName(), borderPainter.getDisplayName(), baseFillColorScheme.getDisplayName(), baseMarkColorScheme.getDisplayName(), baseBorderColorScheme.getDisplayName(), Float.valueOf(visibility));
        Icon iconBase = icons.get(keyBase);
        if (iconBase == null) {
            iconBase = new ImageIcon(SubstanceImageCreator.getRadioButton(button, fillPainter, borderPainter, checkMarkSize, currState, 0, baseFillColorScheme, baseMarkColorScheme, baseBorderColorScheme, visibility));
            icons.put(keyBase, iconBase);
        }
        if (currState.isDisabled() || activeStates.size() == 1) {
            return iconBase;
        }
        BufferedImage result = SubstanceCoreUtilities.getBlankImage(iconBase.getIconWidth(), iconBase.getIconHeight());
        Graphics2D g2d = result.createGraphics();
        iconBase.paintIcon(button, g2d, 0, 0);
        for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : activeStates.entrySet()) {
            float stateContribution;
            ComponentState activeState = activeEntry.getKey();
            if (activeState == currState || !((stateContribution = activeEntry.getValue().getContribution()) > 0.0f)) continue;
            g2d.setComposite(AlphaComposite.SrcOver.derive(stateContribution));
            SubstanceColorScheme fillColorScheme = SubstanceColorSchemeUtilities.getColorScheme(button, ColorSchemeAssociationKind.FILL, activeState);
            SubstanceColorScheme markColorScheme = SubstanceColorSchemeUtilities.getColorScheme(button, ColorSchemeAssociationKind.MARK, activeState);
            SubstanceColorScheme borderColorScheme = SubstanceColorSchemeUtilities.getColorScheme(button, ColorSchemeAssociationKind.BORDER, activeState);
            HashMapKey keyLayer = SubstanceCoreUtilities.getHashKey(fontSize, checkMarkSize, fillPainter.getDisplayName(), borderPainter.getDisplayName(), fillColorScheme.getDisplayName(), markColorScheme.getDisplayName(), borderColorScheme.getDisplayName(), Float.valueOf(visibility));
            Icon iconLayer = icons.get(keyLayer);
            if (iconLayer == null) {
                iconLayer = new ImageIcon(SubstanceImageCreator.getRadioButton(button, fillPainter, borderPainter, checkMarkSize, currState, 0, fillColorScheme, markColorScheme, borderColorScheme, visibility));
                icons.put(keyLayer, iconLayer);
            }
            iconLayer.paintIcon(button, g2d, 0, 0);
        }
        g2d.dispose();
        return new ImageIcon(result);
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstanceRadioButtonUI((JToggleButton)comp);
    }

    public SubstanceRadioButtonUI(JToggleButton button) {
        this.button = button;
        button.setRolloverEnabled(true);
        this.stateTransitionTracker = new StateTransitionTracker(this.button, this.button.getModel());
    }

    @Override
    protected BasicButtonListener createButtonListener(AbstractButton b2) {
        return new RolloverButtonListener(b2, this.stateTransitionTracker);
    }

    @Override
    public Icon getDefaultIcon() {
        return SubstanceRadioButtonUI.getIcon(this.button, this.stateTransitionTracker);
    }

    @Override
    public void paint(Graphics g2, JComponent c2) {
        AbstractButton b2 = (AbstractButton)c2;
        if (SubstanceCoreUtilities.isOpaque(c2)) {
            BackgroundPaintingUtils.update(g2, c2, false);
        }
        FontMetrics fm = g2.getFontMetrics();
        Insets i2 = b2.getInsets();
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
        Font f2 = b2.getFont();
        g2.setFont(f2);
        Icon icon = SubstanceCoreUtilities.getOriginalIcon(b2, this.getDefaultIcon());
        String text = SwingUtilities.layoutCompoundLabel(c2, fm, b2.getText(), icon, b2.getVerticalAlignment(), b2.getHorizontalAlignment(), b2.getVerticalTextPosition(), b2.getHorizontalTextPosition(), this.viewRect, this.iconRect, this.textRect, b2.getText() == null ? 0 : b2.getIconTextGap());
        Graphics2D g2d = (Graphics2D)g2.create();
        if (text != null && !text.equals("")) {
            View v2 = (View)b2.getClientProperty("html");
            if (v2 != null) {
                v2.paint(g2d, this.textRect);
            } else {
                this.paintButtonText(g2d, b2, this.textRect, text);
            }
        }
        if (icon != null) {
            icon.paintIcon(c2, g2d, this.iconRect.x, this.iconRect.y);
        }
        if (b2.isFocusPainted()) {
            int focusRingPadding = SubstanceSizeUtils.getFocusRingPadding(SubstanceSizeUtils.getComponentFontSize(this.button)) / 2;
            SubstanceCoreUtilities.paintFocus(g2d, this.button, this.button, this, null, this.textRect, 1.0f, focusRingPadding);
        }
        g2d.dispose();
    }

    public static String getMemoryUsage() {
        StringBuffer sb = new StringBuffer();
        sb.append("SubstanceRadioButtonUI: \n");
        sb.append("\t" + icons.size() + " icons");
        return sb.toString();
    }

    protected void paintButtonText(Graphics g2, AbstractButton button, Rectangle textRect, String text) {
        SubstanceTextUtilities.paintText(g2, button, textRect, text, button.getDisplayedMnemonicIndex());
    }

    @Override
    public boolean isInside(MouseEvent me) {
        return true;
    }

    @Override
    public StateTransitionTracker getTransitionTracker() {
        return this.stateTransitionTracker;
    }
}

