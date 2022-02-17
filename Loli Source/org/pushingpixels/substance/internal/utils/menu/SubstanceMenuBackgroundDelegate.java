/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils.menu;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.Map;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceConstants;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.animation.TransitionAwareUI;
import org.pushingpixels.substance.internal.painter.BackgroundPaintingUtils;
import org.pushingpixels.substance.internal.painter.HighlightPainterUtils;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;

public class SubstanceMenuBackgroundDelegate {
    public static void paintBackground(Graphics g2, Component menuItem, int textOffset) {
        if (!menuItem.isShowing()) {
            return;
        }
        int menuWidth = menuItem.getWidth();
        int menuHeight = menuItem.getHeight();
        Graphics2D graphics = (Graphics2D)g2.create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        BackgroundPaintingUtils.update(graphics, menuItem, false);
        if (textOffset == 0) {
            return;
        }
        if (menuItem.getParent() instanceof JPopupMenu) {
            if (menuItem.getComponentOrientation().isLeftToRight()) {
                SubstanceConstants.MenuGutterFillKind fillKind = SubstanceCoreUtilities.getMenuGutterFillKind();
                if (fillKind != SubstanceConstants.MenuGutterFillKind.NONE) {
                    SubstanceColorScheme scheme = SubstanceColorSchemeUtilities.getColorScheme(menuItem, ComponentState.ENABLED);
                    Color leftColor = fillKind == SubstanceConstants.MenuGutterFillKind.SOFT_FILL || fillKind == SubstanceConstants.MenuGutterFillKind.HARD ? scheme.getUltraLightColor() : scheme.getLightColor();
                    Color rightColor = fillKind == SubstanceConstants.MenuGutterFillKind.SOFT_FILL || fillKind == SubstanceConstants.MenuGutterFillKind.SOFT ? scheme.getUltraLightColor() : scheme.getLightColor();
                    LinearGradientPaint gp = new LinearGradientPaint(0.0f, 0.0f, textOffset, 0.0f, new float[]{0.0f, 1.0f}, new Color[]{leftColor, rightColor}, MultipleGradientPaint.CycleMethod.REPEAT);
                    graphics.setComposite(LafWidgetUtilities.getAlphaComposite(menuItem, 0.7f, g2));
                    graphics.setPaint(gp);
                    graphics.fillRect(0, 0, textOffset - 2, menuHeight);
                }
            } else {
                SubstanceConstants.MenuGutterFillKind fillKind = SubstanceCoreUtilities.getMenuGutterFillKind();
                if (fillKind != SubstanceConstants.MenuGutterFillKind.NONE) {
                    SubstanceColorScheme scheme = SubstanceColorSchemeUtilities.getColorScheme(menuItem, ComponentState.ENABLED);
                    Color leftColor = fillKind == SubstanceConstants.MenuGutterFillKind.HARD_FILL || fillKind == SubstanceConstants.MenuGutterFillKind.HARD ? scheme.getLightColor() : scheme.getUltraLightColor();
                    Color rightColor = fillKind == SubstanceConstants.MenuGutterFillKind.HARD_FILL || fillKind == SubstanceConstants.MenuGutterFillKind.SOFT ? scheme.getLightColor() : scheme.getUltraLightColor();
                    LinearGradientPaint gp = new LinearGradientPaint(textOffset, 0.0f, menuWidth, 0.0f, new float[]{0.0f, 1.0f}, new Color[]{leftColor, rightColor}, MultipleGradientPaint.CycleMethod.REPEAT);
                    graphics.setComposite(LafWidgetUtilities.getAlphaComposite(menuItem, 0.7f, g2));
                    graphics.setPaint(gp);
                    graphics.fillRect(textOffset - 2, 0, menuWidth, menuHeight);
                }
            }
        }
        graphics.dispose();
    }

    public static void paintHighlights(Graphics g2, JMenuItem menuItem, float borderAlpha) {
        Graphics2D graphics = (Graphics2D)g2.create();
        TransitionAwareUI transitionAwareUI = (TransitionAwareUI)((Object)menuItem.getUI());
        StateTransitionTracker stateTransitionTracker = transitionAwareUI.getTransitionTracker();
        StateTransitionTracker.ModelStateInfo modelStateInfo = stateTransitionTracker.getModelStateInfo();
        ComponentState currState = modelStateInfo.getCurrModelStateNoSelection();
        if (currState.isDisabled()) {
            return;
        }
        Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo.getStateNoSelectionContributionMap();
        if (currState == ComponentState.ENABLED && activeStates.size() == 1) {
            return;
        }
        for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> stateEntry : activeStates.entrySet()) {
            ComponentState activeState = stateEntry.getKey();
            float alpha = SubstanceColorSchemeUtilities.getHighlightAlpha(menuItem, activeState) * stateEntry.getValue().getContribution();
            if (alpha == 0.0f) continue;
            SubstanceColorScheme fillScheme = SubstanceColorSchemeUtilities.getColorScheme(menuItem, ColorSchemeAssociationKind.HIGHLIGHT, activeState);
            SubstanceColorScheme borderScheme = SubstanceColorSchemeUtilities.getColorScheme(menuItem, ColorSchemeAssociationKind.HIGHLIGHT_BORDER, activeState);
            graphics.setComposite(LafWidgetUtilities.getAlphaComposite(menuItem, alpha, g2));
            HighlightPainterUtils.paintHighlight(graphics, null, menuItem, new Rectangle(0, 0, menuItem.getWidth(), menuItem.getHeight()), borderAlpha, null, fillScheme, borderScheme);
            graphics.setComposite(LafWidgetUtilities.getAlphaComposite((Component)menuItem, g2));
        }
        graphics.dispose();
    }
}

