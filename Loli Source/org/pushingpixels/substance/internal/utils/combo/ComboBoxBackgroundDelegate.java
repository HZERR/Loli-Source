/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils.combo;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.util.Map;
import javax.swing.ButtonModel;
import javax.swing.JComboBox;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.painter.border.SubstanceBorderPainter;
import org.pushingpixels.substance.api.painter.fill.SubstanceFillPainter;
import org.pushingpixels.substance.api.shaper.ClassicButtonShaper;
import org.pushingpixels.substance.api.shaper.SubstanceButtonShaper;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.animation.TransitionAwareUI;
import org.pushingpixels.substance.internal.utils.HashMapKey;
import org.pushingpixels.substance.internal.utils.LazyResettableHashMap;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceOutlineUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;

public class ComboBoxBackgroundDelegate {
    private static LazyResettableHashMap<BufferedImage> regularBackgrounds = new LazyResettableHashMap("ComboBoxBackgroundDelegate");

    public static BufferedImage getFullAlphaBackground(JComboBox combo, ButtonModel model, SubstanceFillPainter fillPainter, SubstanceBorderPainter borderPainter, int width, int height) {
        TransitionAwareUI transitionAwareUI = (TransitionAwareUI)((Object)combo.getUI());
        StateTransitionTracker.ModelStateInfo modelStateInfo = transitionAwareUI.getTransitionTracker().getModelStateInfo();
        ComponentState currState = modelStateInfo.getCurrModelState();
        Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo.getStateContributionMap();
        ClassicButtonShaper shaper = ClassicButtonShaper.INSTANCE;
        int comboFontSize = SubstanceSizeUtils.getComponentFontSize(combo);
        float radius = SubstanceSizeUtils.getClassicButtonCornerRadius(comboFontSize);
        SubstanceColorScheme baseFillScheme = SubstanceColorSchemeUtilities.getColorScheme(combo, currState);
        SubstanceColorScheme baseBorderScheme = SubstanceColorSchemeUtilities.getColorScheme(combo, ColorSchemeAssociationKind.BORDER, currState);
        HashMapKey keyBase = SubstanceCoreUtilities.getHashKey(width, height, baseFillScheme.getDisplayName(), baseBorderScheme.getDisplayName(), fillPainter.getDisplayName(), borderPainter.getDisplayName(), combo.getClass().getName(), Float.valueOf(radius), comboFontSize);
        BufferedImage layerBase = regularBackgrounds.get(keyBase);
        if (layerBase == null) {
            layerBase = ComboBoxBackgroundDelegate.createBackgroundImage(combo, shaper, fillPainter, borderPainter, width, height, baseFillScheme, baseBorderScheme, radius);
            regularBackgrounds.put(keyBase, layerBase);
        }
        if (currState.isDisabled() || activeStates.size() == 1) {
            return layerBase;
        }
        BufferedImage result = SubstanceCoreUtilities.getBlankImage(width, height);
        Graphics2D g2d = result.createGraphics();
        g2d.drawImage((Image)layerBase, 0, 0, null);
        for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : activeStates.entrySet()) {
            float stateContribution;
            ComponentState activeState = activeEntry.getKey();
            if (activeState == currState || !((stateContribution = activeEntry.getValue().getContribution()) > 0.0f)) continue;
            g2d.setComposite(AlphaComposite.SrcOver.derive(stateContribution));
            SubstanceColorScheme fillScheme = SubstanceColorSchemeUtilities.getColorScheme(combo, activeState);
            SubstanceColorScheme borderScheme = SubstanceColorSchemeUtilities.getColorScheme(combo, ColorSchemeAssociationKind.BORDER, activeState);
            HashMapKey key = SubstanceCoreUtilities.getHashKey(width, height, fillScheme.getDisplayName(), borderScheme.getDisplayName(), fillPainter.getDisplayName(), borderPainter.getDisplayName(), combo.getClass().getName(), Float.valueOf(radius), comboFontSize);
            BufferedImage layer = regularBackgrounds.get(key);
            if (layer == null) {
                layer = ComboBoxBackgroundDelegate.createBackgroundImage(combo, shaper, fillPainter, borderPainter, width, height, fillScheme, borderScheme, radius);
                regularBackgrounds.put(key, layer);
            }
            g2d.drawImage((Image)layer, 0, 0, null);
        }
        g2d.dispose();
        return result;
    }

    private static BufferedImage createBackgroundImage(JComboBox combo, SubstanceButtonShaper shaper, SubstanceFillPainter fillPainter, SubstanceBorderPainter borderPainter, int width, int height, SubstanceColorScheme fillScheme, SubstanceColorScheme borderScheme, float radius) {
        int comboFontSize = SubstanceSizeUtils.getComponentFontSize(combo);
        int borderDelta = (int)Math.floor((double)SubstanceSizeUtils.getBorderStrokeWidth(comboFontSize) / 2.0);
        GeneralPath contour = SubstanceOutlineUtilities.getBaseOutline(width, height, radius, null, borderDelta);
        BufferedImage newBackground = SubstanceCoreUtilities.getBlankImage(width, height);
        Graphics2D finalGraphics = (Graphics2D)newBackground.getGraphics();
        fillPainter.paintContourBackground(finalGraphics, combo, width, height, contour, false, fillScheme, true);
        int borderThickness = (int)SubstanceSizeUtils.getBorderStrokeWidth(comboFontSize);
        GeneralPath contourInner = borderPainter.isPaintingInnerContour() ? SubstanceOutlineUtilities.getBaseOutline(width, height, radius - (float)borderThickness, null, borderDelta + borderThickness) : null;
        borderPainter.paintBorder(finalGraphics, combo, width, height, contour, contourInner, borderScheme);
        return newBackground;
    }

    public void updateBackground(Graphics g2, JComboBox combo, ButtonModel comboModel) {
        if (!SubstanceLookAndFeel.isCurrentLookAndFeel()) {
            return;
        }
        int width = combo.getWidth();
        int height = combo.getHeight();
        int y2 = 0;
        SubstanceFillPainter fillPainter = SubstanceCoreUtilities.getFillPainter(combo);
        SubstanceBorderPainter borderPainter = SubstanceCoreUtilities.getBorderPainter(combo);
        BufferedImage bgImage = ComboBoxBackgroundDelegate.getFullAlphaBackground(combo, comboModel, fillPainter, borderPainter, width, height);
        TransitionAwareUI transitionAwareUI = (TransitionAwareUI)((Object)combo.getUI());
        StateTransitionTracker stateTransitionTracker = transitionAwareUI.getTransitionTracker();
        StateTransitionTracker.ModelStateInfo modelStateInfo = stateTransitionTracker.getModelStateInfo();
        Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo.getStateContributionMap();
        boolean isFlat = SubstanceCoreUtilities.hasFlatAppearance(combo, false);
        boolean isSpecial = isFlat || !combo.isEnabled();
        float extraAlpha = 1.0f;
        if (isSpecial) {
            if (isFlat) {
                extraAlpha = 0.0f;
                for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : activeStates.entrySet()) {
                    ComponentState activeState = activeEntry.getKey();
                    if (activeState.isDisabled() || activeState == ComponentState.ENABLED) continue;
                    extraAlpha += activeEntry.getValue().getContribution();
                }
            } else if (!combo.isEnabled()) {
                extraAlpha = SubstanceColorSchemeUtilities.getAlpha(combo, modelStateInfo.getCurrModelState());
            }
        }
        if (extraAlpha > 0.0f) {
            Graphics2D graphics = (Graphics2D)g2.create();
            graphics.setComposite(LafWidgetUtilities.getAlphaComposite(combo, extraAlpha, g2));
            graphics.drawImage((Image)bgImage, 0, y2, null);
            graphics.dispose();
        }
    }

    static String getMemoryUsage() {
        StringBuffer sb = new StringBuffer();
        sb.append("SubstanceBackgroundDelegate: \n");
        sb.append("\t" + regularBackgrounds.size() + " regular");
        return sb.toString();
    }
}

