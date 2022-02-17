/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Set;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceConstants;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.painter.border.SubstanceBorderPainter;
import org.pushingpixels.substance.api.painter.fill.SubstanceFillPainter;
import org.pushingpixels.substance.api.shaper.RectangularButtonShaper;
import org.pushingpixels.substance.api.shaper.StandardButtonShaper;
import org.pushingpixels.substance.api.shaper.SubstanceButtonShaper;
import org.pushingpixels.substance.internal.animation.ModificationAwareUI;
import org.pushingpixels.substance.internal.animation.RootPaneDefaultButtonTracker;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.animation.TransitionAwareUI;
import org.pushingpixels.substance.internal.utils.HashMapKey;
import org.pushingpixels.substance.internal.utils.LazyResettableHashMap;
import org.pushingpixels.substance.internal.utils.PairwiseButtonBackgroundDelegate;
import org.pushingpixels.substance.internal.utils.Sideable;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.trident.Timeline;

public class ButtonBackgroundDelegate {
    private static LazyResettableHashMap<BufferedImage> regularBackgrounds = new LazyResettableHashMap("ButtonBackgroundDelegate");

    public static BufferedImage getFullAlphaBackground(AbstractButton button, ButtonModel model, SubstanceButtonShaper shaper, SubstanceFillPainter fillPainter, SubstanceBorderPainter borderPainter, int width, int height) {
        ModificationAwareUI modificationAwareUI;
        Timeline modificationTimeline;
        JButton jb;
        TransitionAwareUI transitionAwareUI = (TransitionAwareUI)((Object)button.getUI());
        StateTransitionTracker.ModelStateInfo modelStateInfo = transitionAwareUI.getTransitionTracker().getModelStateInfo();
        ComponentState currState = modelStateInfo.getCurrModelState();
        float cyclePos = 0.0f;
        if (button instanceof JButton && RootPaneDefaultButtonTracker.isPulsating(jb = (JButton)button) && currState != ComponentState.PRESSED_SELECTED && currState != ComponentState.PRESSED_UNSELECTED) {
            cyclePos = RootPaneDefaultButtonTracker.getTimelinePosition(jb);
        }
        Set<SubstanceConstants.Side> straightSides = SubstanceCoreUtilities.getSides(button, "substancelaf.buttonside");
        boolean isRoundButton = StandardButtonShaper.isRoundButton(button);
        float radius = 0.0f;
        if (shaper instanceof RectangularButtonShaper) {
            radius = ((RectangularButtonShaper)((Object)shaper)).getCornerRadius(button, null);
        }
        Set<SubstanceConstants.Side> openSides = SubstanceCoreUtilities.getSides(button, "substancelaf.buttonopenSide");
        boolean isContentAreaFilled = button.isContentAreaFilled();
        boolean isBorderPainted = button.isBorderPainted();
        SubstanceColorScheme baseBorderScheme = SubstanceColorSchemeUtilities.getColorScheme(button, ColorSchemeAssociationKind.BORDER, currState);
        if (button.getUI() instanceof ModificationAwareUI && (modificationTimeline = (modificationAwareUI = (ModificationAwareUI)((Object)button.getUI())).getModificationTimeline()) != null && modificationTimeline.getState() != Timeline.TimelineState.IDLE) {
            HashMapKey key2;
            BufferedImage layer2;
            SubstanceColorScheme colorScheme2 = SubstanceColorSchemeUtilities.YELLOW;
            SubstanceColorScheme colorScheme = SubstanceColorSchemeUtilities.ORANGE;
            cyclePos = modificationTimeline.getTimelinePosition();
            HashMapKey key1 = SubstanceCoreUtilities.getHashKey(width, height, colorScheme.getDisplayName(), baseBorderScheme.getDisplayName(), shaper.getDisplayName(), fillPainter.getDisplayName(), borderPainter.getDisplayName(), straightSides, openSides, button.getClass().getName(), isRoundButton, Float.valueOf(radius), isContentAreaFilled, isBorderPainted, SubstanceSizeUtils.getComponentFontSize(button));
            BufferedImage layer1 = regularBackgrounds.get(key1);
            if (layer1 == null) {
                layer1 = ButtonBackgroundDelegate.createBackgroundImage(button, shaper, fillPainter, borderPainter, width, height, colorScheme, baseBorderScheme, openSides, isContentAreaFilled, isBorderPainted);
                regularBackgrounds.put(key1, layer1);
            }
            if ((layer2 = regularBackgrounds.get(key2 = SubstanceCoreUtilities.getHashKey(width, height, colorScheme2.getDisplayName(), baseBorderScheme.getDisplayName(), shaper.getDisplayName(), fillPainter.getDisplayName(), borderPainter.getDisplayName(), straightSides, openSides, button.getClass().getName(), isRoundButton, Float.valueOf(radius), isContentAreaFilled, isBorderPainted, SubstanceSizeUtils.getComponentFontSize(button)))) == null) {
                layer2 = ButtonBackgroundDelegate.createBackgroundImage(button, shaper, fillPainter, borderPainter, width, height, colorScheme2, baseBorderScheme, openSides, isContentAreaFilled, isBorderPainted);
                regularBackgrounds.put(key2, layer2);
            }
            BufferedImage result = SubstanceCoreUtilities.getBlankImage(width, height);
            Graphics2D g2d = result.createGraphics();
            if (cyclePos < 1.0f) {
                g2d.drawImage((Image)layer1, 0, 0, null);
            }
            if (cyclePos > 0.0f) {
                g2d.setComposite(AlphaComposite.SrcOver.derive(cyclePos));
                g2d.drawImage((Image)layer2, 0, 0, null);
            }
            g2d.dispose();
            return result;
        }
        Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo.getStateContributionMap();
        SubstanceColorScheme baseFillScheme = SubstanceColorSchemeUtilities.getColorScheme(button, currState);
        HashMapKey keyBase = SubstanceCoreUtilities.getHashKey(width, height, baseFillScheme.getDisplayName(), baseBorderScheme.getDisplayName(), shaper.getDisplayName(), fillPainter.getDisplayName(), borderPainter.getDisplayName(), straightSides, openSides, button.getClass().getName(), isRoundButton, (int)(1000.0f * radius), isContentAreaFilled, isBorderPainted, SubstanceSizeUtils.getComponentFontSize(button));
        BufferedImage layerBase = regularBackgrounds.get(keyBase);
        if (layerBase == null) {
            layerBase = ButtonBackgroundDelegate.createBackgroundImage(button, shaper, fillPainter, borderPainter, width, height, baseFillScheme, baseBorderScheme, openSides, isContentAreaFilled, isBorderPainted);
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
            SubstanceColorScheme fillScheme = SubstanceColorSchemeUtilities.getColorScheme(button, activeState);
            SubstanceColorScheme borderScheme = SubstanceColorSchemeUtilities.getColorScheme(button, ColorSchemeAssociationKind.BORDER, activeState);
            HashMapKey key = SubstanceCoreUtilities.getHashKey(width, height, fillScheme.getDisplayName(), borderScheme.getDisplayName(), shaper.getDisplayName(), fillPainter.getDisplayName(), borderPainter.getDisplayName(), straightSides, openSides, button.getClass().getName(), isRoundButton, (int)(1000.0f * radius), isContentAreaFilled, isBorderPainted, SubstanceSizeUtils.getComponentFontSize(button));
            BufferedImage layer = regularBackgrounds.get(key);
            if (layer == null) {
                layer = ButtonBackgroundDelegate.createBackgroundImage(button, shaper, fillPainter, borderPainter, width, height, fillScheme, borderScheme, openSides, isContentAreaFilled, isBorderPainted);
                regularBackgrounds.put(key, layer);
            }
            g2d.drawImage((Image)layer, 0, 0, null);
        }
        g2d.dispose();
        return result;
    }

    private static BufferedImage createBackgroundImage(AbstractButton button, SubstanceButtonShaper shaper, SubstanceFillPainter fillPainter, SubstanceBorderPainter borderPainter, int width, int height, SubstanceColorScheme colorScheme, SubstanceColorScheme borderScheme, Set<SubstanceConstants.Side> openSides, boolean isContentAreaFilled, boolean isBorderPainted) {
        int openDelta = (int)Math.ceil(3.0 * (double)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(button)));
        int deltaLeft = openSides != null && openSides.contains((Object)SubstanceConstants.Side.LEFT) ? openDelta : 0;
        int deltaRight = openSides != null && openSides.contains((Object)SubstanceConstants.Side.RIGHT) ? openDelta : 0;
        int deltaTop = openSides != null && openSides.contains((Object)SubstanceConstants.Side.TOP) ? openDelta : 0;
        int deltaBottom = openSides != null && openSides.contains((Object)SubstanceConstants.Side.BOTTOM) ? openDelta : 0;
        int borderDelta = (int)Math.floor((double)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(button)) / 2.0);
        Shape contour = shaper.getButtonOutline(button, new Insets(borderDelta, borderDelta, borderDelta, borderDelta), width + deltaLeft + deltaRight, height + deltaTop + deltaBottom, false);
        BufferedImage newBackground = SubstanceCoreUtilities.getBlankImage(width, height);
        Graphics2D finalGraphics = (Graphics2D)newBackground.getGraphics();
        finalGraphics.translate(-deltaLeft, -deltaTop);
        if (isContentAreaFilled) {
            fillPainter.paintContourBackground(finalGraphics, button, width + deltaLeft + deltaRight, height + deltaTop + deltaBottom, contour, false, colorScheme, true);
        }
        if (isBorderPainted) {
            int borderThickness = (int)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(button));
            Shape contourInner = borderPainter.isPaintingInnerContour() ? shaper.getButtonOutline(button, new Insets(borderDelta + borderThickness, borderDelta + borderThickness, borderDelta + borderThickness, borderDelta + borderThickness), width + deltaLeft + deltaRight, height + deltaTop + deltaBottom, true) : null;
            borderPainter.paintBorder(finalGraphics, button, width + deltaLeft + deltaRight, height + deltaTop + deltaBottom, contour, contourInner, borderScheme);
        }
        return newBackground;
    }

    public void updateBackground(Graphics g2, AbstractButton button) {
        if (!SubstanceLookAndFeel.isCurrentLookAndFeel()) {
            return;
        }
        if (SubstanceCoreUtilities.isButtonNeverPainted(button)) {
            return;
        }
        int width = button.getWidth();
        int height = button.getHeight();
        int y2 = 0;
        if (SubstanceCoreUtilities.isScrollButton(button) || SubstanceCoreUtilities.isSpinnerButton(button)) {
            Sideable sideable = (Sideable)((Object)button);
            PairwiseButtonBackgroundDelegate.updatePairwiseBackground(g2, button, width, height, sideable.getSide(), false);
            return;
        }
        SubstanceFillPainter fillPainter = SubstanceCoreUtilities.getFillPainter(button);
        SubstanceButtonShaper shaper = SubstanceCoreUtilities.getButtonShaper(button);
        SubstanceBorderPainter borderPainter = SubstanceCoreUtilities.getBorderPainter(button);
        BufferedImage bgImage = ButtonBackgroundDelegate.getFullAlphaBackground(button, button.getModel(), shaper, fillPainter, borderPainter, width, height);
        TransitionAwareUI transitionAwareUI = (TransitionAwareUI)((Object)button.getUI());
        StateTransitionTracker stateTransitionTracker = transitionAwareUI.getTransitionTracker();
        StateTransitionTracker.ModelStateInfo modelStateInfo = stateTransitionTracker.getModelStateInfo();
        Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo.getStateContributionMap();
        boolean isFlat = SubstanceCoreUtilities.hasFlatAppearance(button);
        boolean isSpecial = isFlat || !button.isEnabled();
        float extraAlpha = 1.0f;
        if (isSpecial) {
            if (isFlat) {
                extraAlpha = 0.0f;
                for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : activeStates.entrySet()) {
                    ComponentState activeState = activeEntry.getKey();
                    if (activeState.isDisabled() || activeState == ComponentState.ENABLED) continue;
                    extraAlpha += activeEntry.getValue().getContribution();
                }
            } else if (!button.isEnabled()) {
                extraAlpha = SubstanceColorSchemeUtilities.getAlpha(button, modelStateInfo.getCurrModelState());
            }
        }
        if (extraAlpha > 0.0f) {
            Graphics2D graphics = (Graphics2D)g2.create();
            graphics.setComposite(LafWidgetUtilities.getAlphaComposite(button, extraAlpha, g2));
            graphics.drawImage((Image)bgImage, 0, y2, null);
            graphics.dispose();
        }
    }

    public static boolean isRoundButton(AbstractButton button) {
        return !SubstanceCoreUtilities.isComboBoxButton(button) && !SubstanceCoreUtilities.isScrollButton(button) && SubstanceCoreUtilities.hasText(button);
    }

    public static boolean contains(AbstractButton button, int x2, int y2) {
        if (!SubstanceLookAndFeel.isCurrentLookAndFeel()) {
            return false;
        }
        SubstanceButtonShaper shaper = SubstanceCoreUtilities.getButtonShaper(button);
        if (shaper == null) {
            return false;
        }
        Shape contour = shaper.getButtonOutline(button, null, button.getWidth(), button.getHeight(), false);
        return contour.contains(x2, y2);
    }

    static String getMemoryUsage() {
        StringBuffer sb = new StringBuffer();
        sb.append("SubstanceBackgroundDelegate: \n");
        sb.append("\t" + regularBackgrounds.size() + " regular");
        return sb.toString();
    }
}

