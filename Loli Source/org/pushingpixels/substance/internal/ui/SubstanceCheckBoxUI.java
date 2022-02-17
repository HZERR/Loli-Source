/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Set;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicButtonListener;
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
import org.pushingpixels.substance.internal.ui.SubstanceRadioButtonUI;
import org.pushingpixels.substance.internal.utils.HashMapKey;
import org.pushingpixels.substance.internal.utils.LazyResettableHashMap;
import org.pushingpixels.substance.internal.utils.RolloverButtonListener;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceImageCreator;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;

public class SubstanceCheckBoxUI
extends SubstanceRadioButtonUI {
    protected Set lafWidgets;
    private static final String propertyPrefix = "CheckBox.";
    private static LazyResettableHashMap<Icon> icons = new LazyResettableHashMap("SubstanceCheckBoxUI");

    public void __org__pushingpixels__substance__internal__ui__SubstanceCheckBoxUI__installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceCheckBoxUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceCheckBoxUI__uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceCheckBoxUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceCheckBoxUI__installListeners(AbstractButton abstractButton) {
        super.installListeners(abstractButton);
    }

    @Override
    protected void installListeners(AbstractButton abstractButton) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceCheckBoxUI__installListeners(abstractButton);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    @Override
    protected void installDefaults(AbstractButton abstractButton) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceCheckBoxUI__installDefaults(abstractButton);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceCheckBoxUI__uninstallListeners(AbstractButton abstractButton) {
        super.uninstallListeners(abstractButton);
    }

    @Override
    protected void uninstallListeners(AbstractButton abstractButton) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceCheckBoxUI__uninstallListeners(abstractButton);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceCheckBoxUI__uninstallDefaults(AbstractButton abstractButton) {
        super.uninstallDefaults(abstractButton);
    }

    @Override
    protected void uninstallDefaults(AbstractButton abstractButton) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceCheckBoxUI__uninstallDefaults(abstractButton);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceCheckBoxUI__update(Graphics graphics, JComponent jComponent) {
        super.update(graphics, jComponent);
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceCheckBoxUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstanceCheckBoxUI((JToggleButton)comp);
    }

    public SubstanceCheckBoxUI(JToggleButton button) {
        super(button);
    }

    @Override
    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceCheckBoxUI__installDefaults(AbstractButton b2) {
        super.installDefaults(b2);
        this.button.setRolloverEnabled(true);
        Border border = b2.getBorder();
        if (border == null || border instanceof UIResource) {
            b2.setBorder(SubstanceSizeUtils.getCheckBoxBorder(SubstanceSizeUtils.getComponentFontSize(b2), b2.getComponentOrientation().isLeftToRight()));
        }
    }

    private static Icon getIcon(JToggleButton button, StateTransitionTracker stateTransitionTracker) {
        StateTransitionTracker.ModelStateInfo modelStateInfo = stateTransitionTracker.getModelStateInfo();
        Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo.getStateContributionMap();
        SubstanceFillPainter fillPainter = SubstanceCoreUtilities.getFillPainter(button);
        SubstanceBorderPainter borderPainter = SubstanceCoreUtilities.getBorderPainter(button);
        ComponentState currState = modelStateInfo.getCurrModelState();
        SubstanceColorScheme baseFillColorScheme = SubstanceColorSchemeUtilities.getColorScheme(button, ColorSchemeAssociationKind.FILL, currState);
        SubstanceColorScheme baseMarkColorScheme = SubstanceColorSchemeUtilities.getColorScheme(button, ColorSchemeAssociationKind.MARK, currState);
        SubstanceColorScheme baseBorderColorScheme = SubstanceColorSchemeUtilities.getColorScheme(button, ColorSchemeAssociationKind.BORDER, currState);
        float visibility = stateTransitionTracker.getFacetStrength(ComponentStateFacet.SELECTION);
        boolean isCheckMarkFadingOut = !currState.isFacetActive(ComponentStateFacet.SELECTION);
        int fontSize = SubstanceSizeUtils.getComponentFontSize(button);
        int checkMarkSize = SubstanceSizeUtils.getCheckBoxMarkSize(fontSize);
        HashMapKey keyBase = SubstanceCoreUtilities.getHashKey(fontSize, checkMarkSize, fillPainter.getDisplayName(), borderPainter.getDisplayName(), baseFillColorScheme.getDisplayName(), baseMarkColorScheme.getDisplayName(), baseBorderColorScheme.getDisplayName(), Float.valueOf(visibility), isCheckMarkFadingOut);
        Icon iconBase = icons.get(keyBase);
        if (iconBase == null) {
            iconBase = new ImageIcon(SubstanceImageCreator.getCheckBox(button, fillPainter, borderPainter, checkMarkSize, currState, baseFillColorScheme, baseMarkColorScheme, baseBorderColorScheme, visibility, isCheckMarkFadingOut));
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
                iconLayer = new ImageIcon(SubstanceImageCreator.getCheckBox(button, fillPainter, borderPainter, checkMarkSize, currState, fillColorScheme, markColorScheme, borderColorScheme, visibility, isCheckMarkFadingOut));
                icons.put(keyLayer, iconLayer);
            }
            iconLayer.paintIcon(button, g2d, 0, 0);
        }
        g2d.dispose();
        return new ImageIcon(result);
    }

    @Override
    protected BasicButtonListener createButtonListener(AbstractButton b2) {
        return new RolloverButtonListener(b2, this.stateTransitionTracker);
    }

    @Override
    public Icon getDefaultIcon() {
        return SubstanceCheckBoxUI.getIcon(this.button, this.stateTransitionTracker);
    }

    public static String getMemoryUsage() {
        StringBuffer sb = new StringBuffer();
        sb.append("SubstanceCheckBox: \n");
        sb.append("\t" + icons.size() + " icons");
        return sb.toString();
    }
}

