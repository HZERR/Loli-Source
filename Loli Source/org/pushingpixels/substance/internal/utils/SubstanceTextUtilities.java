/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.plaf.TextUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.text.JTextComponent;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.lafwidget.text.LockBorder;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.ComponentStateFacet;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.watermark.SubstanceWatermark;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.animation.TransitionAwareUI;
import org.pushingpixels.substance.internal.painter.BackgroundPaintingUtils;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceOutlineUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.border.SubstanceTextComponentBorder;

public class SubstanceTextUtilities {
    public static final String ENFORCE_FG_COLOR = "substancelaf.internal.textUtilities.enforceFgColor";

    public static void paintTextWithDropShadow(JComponent c2, Graphics g2, Color foregroundColor, String text, int width, int height, int xOffset, int yOffset) {
        Graphics2D graphics = (Graphics2D)g2.create();
        RenderingUtils.installDesktopHints(graphics, c2);
        BufferedImage blurred = SubstanceCoreUtilities.getBlankImage(width, height);
        Graphics2D gBlurred = (Graphics2D)blurred.getGraphics();
        gBlurred.setFont(graphics.getFont());
        gBlurred.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        float luminFactor = SubstanceColorUtilities.getColorStrength(foregroundColor);
        gBlurred.setColor(SubstanceColorUtilities.getNegativeColor(foregroundColor));
        ConvolveOp convolve = new ConvolveOp(new Kernel(3, 3, new float[]{0.02f, 0.05f, 0.02f, 0.05f, 0.02f, 0.05f, 0.02f, 0.05f, 0.02f}), 1, null);
        gBlurred.drawString(text, xOffset, yOffset - 1);
        blurred = convolve.filter(blurred, null);
        graphics.setComposite(LafWidgetUtilities.getAlphaComposite(c2, luminFactor, g2));
        graphics.drawImage((Image)blurred, 0, 0, null);
        graphics.setComposite(LafWidgetUtilities.getAlphaComposite((Component)c2, g2));
        FontMetrics fm = graphics.getFontMetrics();
        SubstanceTextUtilities.paintText(graphics, c2, new Rectangle(xOffset, yOffset - fm.getAscent(), width - xOffset, fm.getHeight()), text, -1, graphics.getFont(), foregroundColor, graphics.getClipBounds());
        graphics.dispose();
    }

    private static void paintText(Graphics g2, JComponent comp, Rectangle textRect, String text, int mnemonicIndex, Font font, Color color, Rectangle clip, AffineTransform transform) {
        if (text == null || text.length() == 0) {
            return;
        }
        Graphics2D g2d = (Graphics2D)g2.create();
        g2d.setFont(font);
        g2d.setColor(color);
        if (clip != null) {
            g2d.clip(clip);
        }
        if (transform != null) {
            g2d.transform(transform);
        }
        BasicGraphicsUtils.drawStringUnderlineCharAt(g2d, text, mnemonicIndex, textRect.x, textRect.y + g2d.getFontMetrics().getAscent());
        g2d.dispose();
    }

    public static void paintText(Graphics g2, JComponent comp, Rectangle textRect, String text, int mnemonicIndex, Font font, Color color, Rectangle clip) {
        SubstanceTextUtilities.paintText(g2, comp, textRect, text, mnemonicIndex, font, color, clip, null);
    }

    public static void paintVerticalText(Graphics g2, JComponent comp, Rectangle textRect, String text, int mnemonicIndex, Font font, Color color, Rectangle clip, boolean isFromBottomToTop) {
        AffineTransform at;
        if (text == null || text.length() == 0) {
            return;
        }
        if (!isFromBottomToTop) {
            at = AffineTransform.getTranslateInstance(textRect.x + textRect.width, textRect.y);
            at.rotate(1.5707963267948966);
        } else {
            at = AffineTransform.getTranslateInstance(textRect.x, textRect.y + textRect.height);
            at.rotate(-1.5707963267948966);
        }
        Rectangle newRect = new Rectangle(0, 0, textRect.width, textRect.height);
        SubstanceTextUtilities.paintText(g2, comp, newRect, text, mnemonicIndex, font, color, clip, at);
    }

    public static void paintText(Graphics g2, AbstractButton button, Rectangle textRect, String text, int mnemonicIndex) {
        SubstanceTextUtilities.paintText(g2, button, button.getModel(), textRect, text, mnemonicIndex);
    }

    public static void paintText(Graphics g2, AbstractButton button, ButtonModel model, Rectangle textRect, String text, int mnemonicIndex) {
        TransitionAwareUI transitionAwareUI = (TransitionAwareUI)((Object)button.getUI());
        StateTransitionTracker stateTransitionTracker = transitionAwareUI.getTransitionTracker();
        float buttonAlpha = SubstanceColorSchemeUtilities.getAlpha(button, ComponentState.getState(button));
        if (button instanceof JMenuItem) {
            SubstanceTextUtilities.paintMenuItemText(g2, (JMenuItem)button, textRect, text, mnemonicIndex, stateTransitionTracker.getModelStateInfo(), buttonAlpha);
        } else {
            SubstanceTextUtilities.paintText(g2, (JComponent)button, textRect, text, mnemonicIndex, stateTransitionTracker.getModelStateInfo(), buttonAlpha);
        }
    }

    public static void paintText(Graphics g2, JComponent component, Rectangle textRect, String text, int mnemonicIndex, ComponentState state, float textAlpha) {
        Color fgColor = SubstanceTextUtilities.getForegroundColor(component, text, state, textAlpha);
        SubstanceTextUtilities.paintText(g2, component, textRect, text, mnemonicIndex, component.getFont(), fgColor, null);
    }

    public static void paintText(Graphics g2, JComponent component, Rectangle textRect, String text, int mnemonicIndex, StateTransitionTracker.ModelStateInfo modelStateInfo, float textAlpha) {
        Color fgColor = SubstanceTextUtilities.getForegroundColor(component, text, modelStateInfo, textAlpha);
        SubstanceTextUtilities.paintText(g2, component, textRect, text, mnemonicIndex, component.getFont(), fgColor, null);
    }

    public static void paintMenuItemText(Graphics g2, JMenuItem menuItem, Rectangle textRect, String text, int mnemonicIndex, StateTransitionTracker.ModelStateInfo modelStateInfo, float textAlpha) {
        Color fgColor = SubstanceTextUtilities.getMenuComponentForegroundColor(menuItem, text, modelStateInfo, textAlpha);
        SubstanceTextUtilities.paintText(g2, menuItem, textRect, text, mnemonicIndex, menuItem.getFont(), fgColor, null);
    }

    public static Color getForegroundColor(JComponent component, String text, ComponentState state, float textAlpha) {
        Color bgFillColor;
        Color fgColor;
        if (text == null || text.length() == 0) {
            return null;
        }
        boolean toEnforceFgColor = SwingUtilities.getAncestorOfClass(CellRendererPane.class, component) != null || Boolean.TRUE.equals(component.getClientProperty(ENFORCE_FG_COLOR));
        Color color = fgColor = toEnforceFgColor ? component.getForeground() : SubstanceColorSchemeUtilities.getColorScheme(component, state).getForegroundColor();
        if (textAlpha < 1.0f && (bgFillColor = SubstanceColorUtilities.getBackgroundFillColor(component)) != null) {
            fgColor = SubstanceColorUtilities.getInterpolatedColor(fgColor, bgFillColor, textAlpha);
        }
        return fgColor;
    }

    public static Color getForegroundColor(JComponent component, String text, StateTransitionTracker.ModelStateInfo modelStateInfo, float textAlpha) {
        if (text == null || text.length() == 0) {
            return null;
        }
        boolean toEnforceFgColor = SwingUtilities.getAncestorOfClass(CellRendererPane.class, component) != null || Boolean.TRUE.equals(component.getClientProperty(ENFORCE_FG_COLOR));
        Color fgColor = toEnforceFgColor ? component.getForeground() : SubstanceColorUtilities.getForegroundColor(component, modelStateInfo);
        if (textAlpha < 1.0f) {
            Color bgFillColor = SubstanceColorUtilities.getBackgroundFillColor(component);
            fgColor = SubstanceColorUtilities.getInterpolatedColor(fgColor, bgFillColor, textAlpha);
        }
        return fgColor;
    }

    public static Color getMenuComponentForegroundColor(Component menuComponent, String text, StateTransitionTracker.ModelStateInfo modelStateInfo, float textAlpha) {
        if (text == null || text.length() == 0) {
            return null;
        }
        Color fgColor = SubstanceColorUtilities.getMenuComponentForegroundColor(menuComponent, modelStateInfo);
        if (textAlpha < 1.0f) {
            Color bgFillColor = SubstanceColorUtilities.getBackgroundFillColor(menuComponent);
            fgColor = SubstanceColorUtilities.getInterpolatedColor(fgColor, bgFillColor, textAlpha);
        }
        return fgColor;
    }

    public static void paintTextCompBackground(Graphics g2, JComponent comp) {
        Color backgroundFillColor = SubstanceTextUtilities.getTextBackgroundFillColor(comp);
        boolean toPaintWatermark = SubstanceLookAndFeel.getCurrentSkin(comp).getWatermark() != null && (SubstanceCoreUtilities.toDrawWatermark(comp) || !comp.isOpaque());
        SubstanceTextUtilities.paintTextCompBackground(g2, comp, backgroundFillColor, toPaintWatermark);
    }

    public static Color getTextBackgroundFillColor(JComponent comp) {
        TextUI ui;
        Color backgroundFillColor = SubstanceColorUtilities.getBackgroundFillColor(comp);
        JTextComponent componentForTransitions = SubstanceCoreUtilities.getTextComponentForTransitions(comp);
        if (componentForTransitions != null && (ui = componentForTransitions.getUI()) instanceof TransitionAwareUI) {
            TransitionAwareUI trackable = (TransitionAwareUI)((Object)ui);
            StateTransitionTracker stateTransitionTracker = trackable.getTransitionTracker();
            Color outerTextComponentBorderColor = SubstanceColorUtilities.getOuterTextComponentBorderColor(backgroundFillColor);
            outerTextComponentBorderColor = SubstanceColorUtilities.getInterpolatedColor(outerTextComponentBorderColor, backgroundFillColor, 0.6);
            float selectionStrength = stateTransitionTracker.getFacetStrength(ComponentStateFacet.SELECTION);
            float rolloverStrength = stateTransitionTracker.getFacetStrength(ComponentStateFacet.ROLLOVER);
            backgroundFillColor = SubstanceColorUtilities.getInterpolatedColor(outerTextComponentBorderColor, backgroundFillColor, Math.max(selectionStrength, rolloverStrength));
        }
        return backgroundFillColor;
    }

    private static void paintTextCompBackground(Graphics g2, JComponent comp, Color backgr, boolean toOverlayWatermark) {
        boolean isSubstanceBorder;
        Graphics2D g2d = (Graphics2D)g2.create();
        int componentFontSize = SubstanceSizeUtils.getComponentFontSize(comp);
        int borderDelta = (int)Math.floor(SubstanceSizeUtils.getBorderStrokeWidth(componentFontSize));
        Border compBorder = comp.getBorder();
        if (compBorder instanceof LockBorder) {
            compBorder = ((LockBorder)compBorder).getOriginalBorder();
        }
        if (!(isSubstanceBorder = compBorder instanceof SubstanceTextComponentBorder)) {
            Border border = compBorder;
            while (border instanceof CompoundBorder) {
                Border outer = ((CompoundBorder)border).getOutsideBorder();
                if (outer instanceof SubstanceTextComponentBorder) {
                    isSubstanceBorder = true;
                    break;
                }
                Border inner = ((CompoundBorder)border).getInsideBorder();
                if (inner instanceof SubstanceTextComponentBorder) {
                    isSubstanceBorder = true;
                    break;
                }
                border = inner;
            }
        }
        Shape contour = isSubstanceBorder ? SubstanceOutlineUtilities.getBaseOutline(comp.getWidth(), comp.getHeight(), Math.max(0.0f, 2.0f * SubstanceSizeUtils.getClassicButtonCornerRadius(componentFontSize) - (float)borderDelta), null, borderDelta) : new Rectangle(0, 0, comp.getWidth(), comp.getHeight());
        BackgroundPaintingUtils.update(g2, comp, false);
        SubstanceWatermark watermark = SubstanceCoreUtilities.getSkin(comp).getWatermark();
        if (watermark != null) {
            watermark.drawWatermarkImage(g2d, comp, 0, 0, comp.getWidth(), comp.getHeight());
        }
        g2d.setColor(backgr);
        g2d.fill(contour);
        if (toOverlayWatermark && watermark != null) {
            g2d.clip(contour);
            watermark.drawWatermarkImage(g2d, comp, 0, 0, comp.getWidth(), comp.getHeight());
        }
        g2d.dispose();
    }
}

