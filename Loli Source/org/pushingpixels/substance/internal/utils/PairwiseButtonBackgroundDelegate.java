/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import javax.swing.AbstractButton;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceConstants;
import org.pushingpixels.substance.api.painter.border.SubstanceBorderPainter;
import org.pushingpixels.substance.api.painter.fill.MatteFillPainter;
import org.pushingpixels.substance.api.painter.fill.SubstanceFillPainter;
import org.pushingpixels.substance.api.shaper.RectangularButtonShaper;
import org.pushingpixels.substance.api.shaper.SubstanceButtonShaper;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.animation.TransitionAwareUI;
import org.pushingpixels.substance.internal.utils.HashMapKey;
import org.pushingpixels.substance.internal.utils.LazyResettableHashMap;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceImageCreator;
import org.pushingpixels.substance.internal.utils.SubstanceOutlineUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;

public class PairwiseButtonBackgroundDelegate {
    private static LazyResettableHashMap<BufferedImage> pairwiseBackgrounds = new LazyResettableHashMap("PairwiseButtonBackgroundDelegate");

    public static void updatePairwiseBackground(Graphics g2, AbstractButton button, int width, int height, SubstanceConstants.Side side, boolean toIgnoreOpenSides) {
        if (SubstanceCoreUtilities.isButtonNeverPainted(button)) {
            return;
        }
        SubstanceButtonShaper shaper = SubstanceCoreUtilities.getButtonShaper(button);
        TransitionAwareUI transitionAwareUI = (TransitionAwareUI)((Object)button.getUI());
        StateTransitionTracker stateTransitionTracker = transitionAwareUI.getTransitionTracker();
        StateTransitionTracker.ModelStateInfo modelStateInfo = stateTransitionTracker.getModelStateInfo();
        ComponentState currState = modelStateInfo.getCurrModelState();
        SubstanceColorScheme baseFillScheme = SubstanceColorSchemeUtilities.getColorScheme(button, currState);
        SubstanceColorScheme baseBorderScheme = SubstanceColorSchemeUtilities.getColorScheme(button, ColorSchemeAssociationKind.BORDER, currState);
        SubstanceFillPainter fillPainter = SubstanceCoreUtilities.isSpinnerButton(button) ? MatteFillPainter.INSTANCE : SubstanceImageCreator.SimplisticSoftBorderReverseFillPainter.INSTANCE;
        BufferedImage baseLayer = PairwiseButtonBackgroundDelegate.getPairwiseFullAlphaBackground(button, fillPainter, shaper, width, height, side, baseFillScheme, baseBorderScheme, toIgnoreOpenSides);
        BufferedImage fullOpacity = null;
        Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo.getStateContributionMap();
        if (currState.isDisabled() || activeStates.size() == 1) {
            fullOpacity = baseLayer;
        } else {
            fullOpacity = SubstanceCoreUtilities.getBlankImage(baseLayer.getWidth(), baseLayer.getHeight());
            Graphics2D g2fullOpacity = fullOpacity.createGraphics();
            g2fullOpacity.drawImage((Image)baseLayer, 0, 0, null);
            for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : activeStates.entrySet()) {
                float contribution;
                ComponentState activeState = activeEntry.getKey();
                if (activeState == currState || (contribution = activeEntry.getValue().getContribution()) == 0.0f) continue;
                SubstanceColorScheme fillScheme = SubstanceColorSchemeUtilities.getColorScheme(button, activeState);
                SubstanceColorScheme borderScheme = SubstanceColorSchemeUtilities.getColorScheme(button, ColorSchemeAssociationKind.BORDER, activeState);
                BufferedImage layer = PairwiseButtonBackgroundDelegate.getPairwiseFullAlphaBackground(button, fillPainter, shaper, width, height, side, fillScheme, borderScheme, toIgnoreOpenSides);
                g2fullOpacity.setComposite(AlphaComposite.SrcOver.derive(contribution));
                g2fullOpacity.drawImage((Image)layer, 0, 0, null);
            }
            g2fullOpacity.dispose();
        }
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
                extraAlpha = SubstanceColorSchemeUtilities.getAlpha(button, currState);
            }
        }
        if (extraAlpha > 0.0f) {
            Graphics2D graphics = (Graphics2D)g2.create();
            graphics.setComposite(LafWidgetUtilities.getAlphaComposite(button, extraAlpha, g2));
            graphics.drawImage((Image)fullOpacity, 0, 0, null);
            graphics.dispose();
        }
    }

    private static BufferedImage getPairwiseFullAlphaBackground(AbstractButton button, SubstanceFillPainter fillPainter, SubstanceButtonShaper shaper, int width, int height, SubstanceConstants.Side side, SubstanceColorScheme colorScheme, SubstanceColorScheme borderScheme, boolean toIgnoreOpenSides) {
        HashMapKey key;
        BufferedImage finalBackground;
        if (SubstanceCoreUtilities.isButtonNeverPainted(button)) {
            return null;
        }
        Set<SubstanceConstants.Side> openSides = toIgnoreOpenSides ? EnumSet.noneOf(SubstanceConstants.Side.class) : SubstanceCoreUtilities.getSides(button, "substancelaf.buttonopenSide");
        boolean isBorderPainted = button.isBorderPainted();
        boolean isContentAreaFilled = button.isContentAreaFilled();
        float radius = 0.0f;
        if (SubstanceCoreUtilities.isSpinnerButton(button) && shaper instanceof RectangularButtonShaper) {
            radius = ((RectangularButtonShaper)((Object)shaper)).getCornerRadius(button, null);
        }
        if ((finalBackground = pairwiseBackgrounds.get(key = SubstanceCoreUtilities.getHashKey(new Object[]{width, height, side, openSides, colorScheme.getDisplayName(), borderScheme.getDisplayName(), button.getClass().getName(), fillPainter.getDisplayName(), shaper.getDisplayName(), isBorderPainted, isContentAreaFilled, Float.valueOf(radius)}))) == null) {
            int deltaLeft = openSides != null && openSides.contains((Object)SubstanceConstants.Side.LEFT) ? 3 : 0;
            int deltaRight = openSides != null && openSides.contains((Object)SubstanceConstants.Side.RIGHT) ? 3 : 0;
            int deltaTop = openSides != null && openSides.contains((Object)SubstanceConstants.Side.TOP) ? 3 : 0;
            int deltaBottom = openSides != null && openSides.contains((Object)SubstanceConstants.Side.BOTTOM) ? 3 : 0;
            GeneralPath contour = null;
            SubstanceBorderPainter borderPainter = SubstanceCoreUtilities.getBorderPainter(button);
            int borderDelta = (int)Math.floor((double)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(button)) / 2.0);
            finalBackground = SubstanceCoreUtilities.getBlankImage(width, height);
            Graphics2D finalGraphics = (Graphics2D)finalBackground.getGraphics();
            finalGraphics.translate(-deltaLeft, -deltaTop);
            if (side != null) {
                switch (side) {
                    case TOP: 
                    case BOTTOM: {
                        contour = SubstanceOutlineUtilities.getBaseOutline(height + deltaTop + deltaBottom, width + deltaLeft + deltaRight, radius, null, borderDelta);
                        int translateY = height;
                        if (SubstanceCoreUtilities.isScrollButton(button)) {
                            translateY += 1 + (side == SubstanceConstants.Side.BOTTOM ? 1 : -2);
                        }
                        AffineTransform at = AffineTransform.getTranslateInstance(0.0, translateY);
                        at.rotate(-1.5707963267948966);
                        finalGraphics.setTransform(at);
                        if (isContentAreaFilled) {
                            fillPainter.paintContourBackground(finalGraphics, button, height + deltaTop + deltaBottom, width + deltaLeft + deltaRight, contour, false, colorScheme, true);
                        }
                        if (!isBorderPainted) break;
                        borderPainter.paintBorder(finalGraphics, button, height + deltaTop + deltaBottom, width + deltaLeft + deltaRight, contour, null, borderScheme);
                        break;
                    }
                    case RIGHT: 
                    case LEFT: {
                        contour = SubstanceOutlineUtilities.getBaseOutline(width + deltaLeft + deltaRight, height + deltaTop + deltaBottom, radius, null, borderDelta);
                        if (isContentAreaFilled) {
                            fillPainter.paintContourBackground(finalGraphics, button, width + deltaLeft + deltaRight, height + deltaTop + deltaBottom, contour, false, colorScheme, true);
                        }
                        if (!isBorderPainted) break;
                        borderPainter.paintBorder(finalGraphics, button, width + deltaLeft + deltaRight, height + deltaTop + deltaBottom, contour, null, borderScheme);
                    }
                }
            } else {
                contour = SubstanceOutlineUtilities.getBaseOutline(width + deltaLeft + deltaRight, height + deltaTop + deltaBottom, radius, null, borderDelta);
                fillPainter.paintContourBackground(finalGraphics, button, width + deltaLeft + deltaRight, height + deltaTop + deltaBottom, contour, false, colorScheme, true);
                if (isBorderPainted) {
                    borderPainter.paintBorder(finalGraphics, button, width + deltaLeft + deltaRight, height + deltaTop + deltaBottom, contour, null, borderScheme);
                }
            }
            pairwiseBackgrounds.put(key, finalBackground);
        }
        return finalBackground;
    }
}

