/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.painter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LinearGradientPaint;
import java.awt.image.BufferedImage;
import java.util.Collection;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.internal.utils.HashMapKey;
import org.pushingpixels.substance.internal.utils.LazyResettableHashMap;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;

public class SeparatorPainterUtils {
    private static LazyResettableHashMap<BufferedImage> cached = new LazyResettableHashMap("SeparatorPainterUtils");

    public static void paintSeparator(Component c2, Graphics graphics, int width, int height, int orientation) {
        SeparatorPainterUtils.paintSeparator(c2, graphics, width, height, orientation, true, 10);
    }

    public static void paintSeparator(Component c2, Graphics graphics, int width, int height, int orientation, boolean hasShadow, int maxGradLength) {
        SeparatorPainterUtils.paintSeparator(c2, graphics, width, height, orientation, hasShadow, maxGradLength, maxGradLength, false);
    }

    public static void paintSeparator(Component c2, Graphics g2, int width, int height, int orientation, boolean hasShadow, int maxGradLengthStart, int maxGradLengthEnd, boolean toEnforceAlphaColors) {
        SubstanceColorScheme compScheme = SubstanceColorSchemeUtilities.getColorScheme(c2, ColorSchemeAssociationKind.SEPARATOR, ComponentState.ENABLED);
        SeparatorPainterUtils.paintSeparator(c2, g2, compScheme, width, height, orientation, hasShadow, maxGradLengthStart, maxGradLengthEnd, toEnforceAlphaColors);
    }

    public static void paintSeparator(Component c2, Graphics g2, SubstanceColorScheme scheme, int width, int height, int orientation, boolean hasShadow, int maxGradLengthStart, int maxGradLengthEnd, boolean toEnforceAlphaColors) {
        DecorationAreaType decorationAreaType = SubstanceLookAndFeel.getDecorationType(c2);
        SubstanceSkin skin = SubstanceCoreUtilities.getSkin(c2);
        boolean toUseAlphaColors = decorationAreaType == null || decorationAreaType == DecorationAreaType.NONE ? false : skin.isRegisteredAsDecorationArea(decorationAreaType) || skin.getOverlayPainters(decorationAreaType).size() > 0;
        toUseAlphaColors = toUseAlphaColors || toEnforceAlphaColors;
        Color backgrFill = SubstanceColorUtilities.getBackgroundFillColor(c2);
        int fontSize = SubstanceSizeUtils.getComponentFontSize(c2);
        float borderStrokeWidth = SubstanceSizeUtils.getBorderStrokeWidth(fontSize);
        if (orientation == 0 && height == 0) {
            height = (int)Math.ceil(2.0 * (double)borderStrokeWidth);
        }
        if (orientation == 1 && width == 0) {
            width = (int)Math.ceil(2.0 * (double)borderStrokeWidth);
        }
        if (width == 0 || height == 0) {
            return;
        }
        HashMapKey key = SubstanceCoreUtilities.getHashKey(fontSize, scheme.getDisplayName(), width, height, orientation, hasShadow, maxGradLengthStart, maxGradLengthEnd, toUseAlphaColors, backgrFill.getRGB());
        BufferedImage singleLine = cached.get(key);
        if (singleLine == null) {
            singleLine = SubstanceCoreUtilities.getBlankImage(width, height);
            Graphics2D graphics = singleLine.createGraphics();
            Color foreLight = SeparatorPainterUtils.getSeparatorLightColor(scheme);
            Color foreDark = SeparatorPainterUtils.getSeparatorDarkColor(scheme);
            Color back = SeparatorPainterUtils.getSeparatorShadowColor(scheme);
            Color foreLight12 = toUseAlphaColors ? SubstanceColorUtilities.getAlphaColor(foreLight, 32) : SubstanceColorUtilities.getInterpolatedColor(foreLight, backgrFill, 0.12);
            Color foreDark95 = toUseAlphaColors ? SubstanceColorUtilities.getAlphaColor(foreDark, 240) : SubstanceColorUtilities.getInterpolatedColor(foreDark, backgrFill, 0.95);
            Color back12 = toUseAlphaColors ? SubstanceColorUtilities.getAlphaColor(back, 32) : SubstanceColorUtilities.getInterpolatedColor(back, backgrFill, 0.12);
            Color back95 = toUseAlphaColors ? SubstanceColorUtilities.getAlphaColor(back, 240) : SubstanceColorUtilities.getInterpolatedColor(back, backgrFill, 0.95);
            graphics.setStroke(new BasicStroke(borderStrokeWidth, 0, 1));
            if (orientation == 1) {
                int gradStart = Math.min(maxGradLengthStart, height / 2);
                int gradEnd = Math.min(maxGradLengthEnd, height / 2);
                graphics.translate(Math.max(0, width / 2 - 1), 0);
                graphics.setPaint(new GradientPaint(0.0f, 0.0f, foreLight12, 0.0f, gradStart, foreDark95));
                graphics.drawLine(0, 0, 0, gradStart);
                graphics.setColor(foreDark95);
                graphics.drawLine(0, gradStart, 0, height - gradEnd);
                graphics.setPaint(new GradientPaint(0.0f, height - gradEnd, foreDark95, 0.0f, height, foreLight12));
                graphics.drawLine(0, height - gradEnd, 0, height);
                if (hasShadow) {
                    int offset = (int)borderStrokeWidth;
                    graphics.setPaint(new GradientPaint(offset, 0.0f, back12, offset, gradStart, back95));
                    graphics.drawLine(offset, 0, offset, gradStart);
                    graphics.setColor(back95);
                    graphics.drawLine(offset, gradStart, offset, height - gradEnd);
                    graphics.setPaint(new GradientPaint(offset, height - gradEnd, back95, offset, height, back12));
                    graphics.drawLine(offset, height - gradEnd, offset, height);
                }
            } else {
                int gradStart = Math.min(maxGradLengthStart, width / 2);
                int gradEnd = Math.min(maxGradLengthEnd, width / 2);
                graphics.translate(0, Math.max(0, height / 2 - 1));
                graphics.setPaint(new GradientPaint(0.0f, 0.0f, foreLight12, gradStart, 0.0f, foreDark95));
                graphics.drawLine(0, 0, gradStart, 0);
                graphics.setColor(foreDark95);
                graphics.drawLine(gradStart, 0, width - gradEnd, 0);
                graphics.setPaint(new GradientPaint(width - gradEnd, 0.0f, foreDark95, width, 0.0f, foreLight12));
                graphics.drawLine(width - gradEnd, 0, width, 0);
                if (hasShadow) {
                    int offset = (int)borderStrokeWidth;
                    graphics.setPaint(new GradientPaint(0.0f, offset, back12, gradStart, offset, back95));
                    graphics.drawLine(0, offset, gradStart, offset);
                    graphics.setColor(back95);
                    graphics.drawLine(gradStart, offset, width - gradEnd, offset);
                    graphics.setPaint(new GradientPaint(width - gradEnd, offset, back95, width, offset, back12));
                    graphics.drawLine(width - gradEnd, offset, width, offset);
                }
            }
            graphics.dispose();
            cached.put(key, singleLine);
        }
        Graphics2D g2d = (Graphics2D)g2.create();
        g2d.drawImage((Image)singleLine, 0, 0, null);
        g2d.dispose();
    }

    public static Color getSeparatorShadowColor(SubstanceColorScheme scheme) {
        return scheme.isDark() ? scheme.getDarkColor() : scheme.getUltraLightColor();
    }

    public static Color getSeparatorDarkColor(SubstanceColorScheme scheme) {
        return scheme.isDark() ? scheme.getExtraLightColor() : SubstanceColorUtilities.getInterpolatedColor(scheme.getMidColor(), scheme.getDarkColor(), 0.4);
    }

    public static Color getSeparatorLightColor(SubstanceColorScheme scheme) {
        return scheme.isDark() ? scheme.getLightColor() : SubstanceColorUtilities.getInterpolatedColor(scheme.getLightColor(), scheme.getDarkColor(), 0.8);
    }

    public static void paintVerticalLines(Graphics g2, Component c2, SubstanceColorScheme scheme, int y2, Collection<Integer> x2, int height, float fadeStartFraction) {
        int componentFontSize = SubstanceSizeUtils.getComponentFontSize(c2);
        Color backgrFill = SubstanceColorUtilities.getBackgroundFillColor(c2);
        HashMapKey key = SubstanceCoreUtilities.getHashKey(componentFontSize, scheme.getDisplayName(), 0, height, 1, true, 0.0, Float.valueOf(fadeStartFraction), backgrFill.getRGB());
        float borderStrokeWidth = SubstanceSizeUtils.getBorderStrokeWidth(componentFontSize);
        int offset = (int)borderStrokeWidth;
        BufferedImage singleLine = cached.get(key);
        if (singleLine == null) {
            singleLine = SubstanceCoreUtilities.getBlankImage(Math.max(2, (int)Math.ceil(2.0 * (double)borderStrokeWidth)), height);
            Graphics2D graphics = singleLine.createGraphics();
            Color foreLight = SeparatorPainterUtils.getSeparatorLightColor(scheme);
            Color foreDark = SeparatorPainterUtils.getSeparatorDarkColor(scheme);
            Color back = SeparatorPainterUtils.getSeparatorShadowColor(scheme);
            graphics.setStroke(new BasicStroke(borderStrokeWidth, 0, 1));
            Color foreLight12 = SubstanceColorUtilities.getInterpolatedColor(foreLight, backgrFill, 0.12);
            Color foreDark95 = SubstanceColorUtilities.getInterpolatedColor(foreDark, backgrFill, 0.95);
            Color back12 = SubstanceColorUtilities.getInterpolatedColor(back, backgrFill, 0.12);
            Color back95 = SubstanceColorUtilities.getInterpolatedColor(back, backgrFill, 0.95);
            LinearGradientPaint forePaint = new LinearGradientPaint(0.0f, 0.0f, 0.0f, height, new float[]{0.0f, fadeStartFraction, 1.0f}, new Color[]{foreDark95, foreDark95, foreLight12});
            graphics.setPaint(forePaint);
            graphics.translate(borderStrokeWidth / 2.0f, 0.0);
            graphics.drawLine(0, 0, 0, height);
            LinearGradientPaint backPaint = new LinearGradientPaint(0.0f, 0.0f, 0.0f, height, new float[]{0.0f, fadeStartFraction, 1.0f}, new Color[]{back95, back95, back12});
            graphics.setPaint(backPaint);
            graphics.drawLine(offset, 0, offset, height);
            graphics.dispose();
            cached.put(key, singleLine);
        }
        Graphics2D g2d = (Graphics2D)g2.create();
        for (int lineX : x2) {
            g2d.drawImage((Image)singleLine, lineX - offset / 2, y2, null);
        }
        g2d.dispose();
    }

    public static void paintHorizontalLines(Graphics g2, Component c2, SubstanceColorScheme scheme, int x2, Collection<Integer> y2, int width, float fadeStartFraction, boolean isLtr) {
        int componentFontSize = SubstanceSizeUtils.getComponentFontSize(c2);
        Color backgrFill = SubstanceColorUtilities.getBackgroundFillColor(c2);
        HashMapKey key = SubstanceCoreUtilities.getHashKey(componentFontSize, scheme.getDisplayName(), width, 0, 1, true, 0.0, Float.valueOf(fadeStartFraction), isLtr, backgrFill.getRGB());
        float borderStrokeWidth = SubstanceSizeUtils.getBorderStrokeWidth(componentFontSize);
        int offset = (int)borderStrokeWidth;
        BufferedImage singleLine = cached.get(key);
        if (singleLine == null) {
            singleLine = SubstanceCoreUtilities.getBlankImage(width, Math.max(2, (int)Math.ceil(2.0 * (double)borderStrokeWidth)));
            Graphics2D graphics = singleLine.createGraphics();
            Color foreLight = SeparatorPainterUtils.getSeparatorLightColor(scheme);
            Color foreDark = SeparatorPainterUtils.getSeparatorDarkColor(scheme);
            Color back = SeparatorPainterUtils.getSeparatorShadowColor(scheme);
            graphics.setStroke(new BasicStroke(borderStrokeWidth, 0, 1));
            Color foreLight12 = SubstanceColorUtilities.getInterpolatedColor(foreLight, backgrFill, 0.12);
            Color foreDark95 = SubstanceColorUtilities.getInterpolatedColor(foreDark, backgrFill, 0.95);
            Color back12 = SubstanceColorUtilities.getInterpolatedColor(back, backgrFill, 0.12);
            Color back95 = SubstanceColorUtilities.getInterpolatedColor(back, backgrFill, 0.95);
            LinearGradientPaint forePaint = new LinearGradientPaint(0.0f, 0.0f, width, 0.0f, new float[]{0.0f, fadeStartFraction, 1.0f}, new Color[]{isLtr ? foreDark95 : foreLight12, foreDark95, isLtr ? foreLight12 : foreDark95});
            graphics.setPaint(forePaint);
            graphics.drawLine(0, 0, width, 0);
            LinearGradientPaint backPaint = new LinearGradientPaint(0.0f, 9.0f, width, 0.0f, new float[]{0.0f, fadeStartFraction, 1.0f}, new Color[]{isLtr ? back95 : back12, back95, isLtr ? back12 : back95});
            graphics.setPaint(backPaint);
            graphics.drawLine(0, offset, width, offset);
            graphics.dispose();
            cached.put(key, singleLine);
        }
        Graphics2D g2d = (Graphics2D)g2.create();
        for (int lineY : y2) {
            g2d.drawImage((Image)singleLine, x2, lineY - offset / 2, null);
        }
        g2d.dispose();
    }
}

