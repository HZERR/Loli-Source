/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.VolatileImage;
import javax.swing.AbstractButton;
import javax.swing.CellRendererPane;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceConstants;
import org.pushingpixels.substance.api.painter.border.FlatBorderPainter;
import org.pushingpixels.substance.api.painter.border.SubstanceBorderPainter;
import org.pushingpixels.substance.api.painter.fill.SubstanceFillPainter;
import org.pushingpixels.substance.api.watermark.SubstanceWatermark;
import org.pushingpixels.substance.internal.colorscheme.ShiftColorScheme;
import org.pushingpixels.substance.internal.painter.SimplisticFillPainter;
import org.pushingpixels.substance.internal.utils.PairwiseButtonBackgroundDelegate;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceOutlineUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.SubstanceSplitPaneDivider;
import org.pushingpixels.substance.internal.utils.filters.ColorSchemeFilter;
import org.pushingpixels.substance.internal.utils.filters.GrayscaleFilter;
import org.pushingpixels.substance.internal.utils.filters.NegatedFilter;
import org.pushingpixels.substance.internal.utils.filters.TranslucentFilter;

public final class SubstanceImageCreator {
    private static final int[] crayonColors = new int[]{0x800000, 0x808000, 32768, 32896, 128, 0x800080, 0x7F7F7F, 0x808080, 0x804000, 0x408000, 32832, 16512, 0x400080, 0x800040, 0x666666, 0x999999, 0xFF0000, 0xFFFF00, 65280, 65535, 255, 0xFF00FF, 0x4C4C4C, 0xB3B3B3, 0xFF8000, 0x80FF00, 65408, 33023, 0x8000FF, 0xFF0080, 0x333333, 0xCCCCCC, 0xFF6666, 0xFFFF66, 0x66FF66, 0x66FFFF, 0x6666FF, 0xFF66FF, 0x191919, 0xE6E6E6, 0xFFCC66, 0xCCFF66, 0x66FFCC, 0x66CCFF, 0xCC66FF, 0xFF6FCF, 0, 0xFFFFFF};

    public static void paintBorder(Component c2, Graphics2D graphics, int x2, int y2, int width, int height, float radius, SubstanceColorScheme borderScheme) {
        SubstanceBorderPainter borderPainter = SubstanceCoreUtilities.getBorderPainter(c2);
        graphics.translate(x2, y2);
        int componentFontSize = SubstanceSizeUtils.getComponentFontSize(c2);
        int borderDelta = (int)Math.floor((double)SubstanceSizeUtils.getBorderStrokeWidth(componentFontSize) / 2.0);
        GeneralPath contour = SubstanceOutlineUtilities.getBaseOutline(width, height, radius, null, borderDelta);
        int borderThickness = (int)SubstanceSizeUtils.getBorderStrokeWidth(componentFontSize);
        boolean skipInnerBorder = c2 instanceof JTextComponent || SwingUtilities.getAncestorOfClass(CellRendererPane.class, c2) != null && SwingUtilities.getAncestorOfClass(JFileChooser.class, c2) != null;
        GeneralPath contourInner = skipInnerBorder ? null : SubstanceOutlineUtilities.getBaseOutline(width, height, radius - (float)borderThickness, null, borderThickness + borderDelta);
        borderPainter.paintBorder(graphics, c2, width, height, contour, contourInner, borderScheme);
        graphics.translate(-x2, -y2);
    }

    public static void paintTextComponentBorder(JComponent c2, Graphics graphics, int x2, int y2, int width, int height, float radius, SubstanceColorScheme borderScheme) {
        int componentFontSize = SubstanceSizeUtils.getComponentFontSize(c2);
        int borderDelta = (int)Math.floor((double)SubstanceSizeUtils.getBorderStrokeWidth(componentFontSize) / 2.0);
        GeneralPath contour = SubstanceOutlineUtilities.getBaseOutline(width, height, radius, SubstanceCoreUtilities.getSides(c2, "substancelaf.buttonside"), borderDelta);
        int borderThickness = (int)SubstanceSizeUtils.getBorderStrokeWidth(componentFontSize);
        GeneralPath contourInner = SubstanceOutlineUtilities.getBaseOutline(width, height, radius - (float)borderThickness, null, borderThickness + borderDelta);
        Graphics2D g2d = (Graphics2D)graphics.create();
        g2d.translate(x2, y2);
        ComponentState stateForOuterBorder = c2.isEnabled() ? ComponentState.ENABLED : ComponentState.DISABLED_UNSELECTED;
        Color lightColor = SubstanceColorUtilities.getDefaultBackgroundColor(c2, stateForOuterBorder);
        if (stateForOuterBorder.isDisabled()) {
            float alpha = SubstanceColorSchemeUtilities.getAlpha(c2, stateForOuterBorder);
            lightColor = SubstanceColorUtilities.getAlphaColor(lightColor, (int)(255.0f * alpha));
        }
        Color outerColor = SubstanceColorUtilities.getOuterTextComponentBorderColor(lightColor);
        float[] hsb = Color.RGBtoHSB(lightColor.getRed(), lightColor.getGreen(), lightColor.getBlue(), null);
        double bottomInnerBlend = 0.85;
        double topInnerBlend = 0.8;
        if (hsb[2] < 0.3f) {
            bottomInnerBlend = 0.6;
            topInnerBlend = 0.95;
        } else if (hsb[2] < 0.5f) {
            bottomInnerBlend = 0.8;
        } else if (hsb[2] < 0.75f) {
            bottomInnerBlend = 0.7;
        }
        Color darkColor = borderScheme.getDarkColor();
        Color topInnerColor = SubstanceColorUtilities.getInterpolatedColor(darkColor, lightColor, topInnerBlend);
        Color bottomInnerColor = SubstanceColorUtilities.getInterpolatedColor(lightColor, darkColor, bottomInnerBlend);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        float strokeWidth = SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(c2));
        g2d.setStroke(new BasicStroke(strokeWidth, 0, 1));
        g2d.setPaint(new GradientPaint(0.0f, 0.0f, outerColor, 0.0f, height, outerColor));
        g2d.draw(contour);
        g2d.setPaint(new GradientPaint(0.0f, 0.0f, topInnerColor, 0.0f, height, bottomInnerColor));
        g2d.draw(contourInner);
        g2d.dispose();
    }

    private static BufferedImage getCheckMark(int dimension, boolean isEnabled, SubstanceColorScheme scheme, float checkMarkVisibility) {
        BufferedImage result = SubstanceCoreUtilities.getBlankImage(dimension, dimension);
        Graphics2D graphics = (Graphics2D)result.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GeneralPath path = new GeneralPath();
        path.moveTo(0.25f * (float)dimension, 0.5f * (float)dimension);
        path.quadTo(0.37f * (float)dimension, 0.6f * (float)dimension, 0.47f * (float)dimension, 0.8f * (float)dimension);
        path.quadTo(0.55f * (float)dimension, 0.5f * (float)dimension, 0.85f * (float)dimension, 0.0f);
        float xClipStart = 0.15f * (float)dimension;
        float xClipEnd = 0.95f * (float)dimension;
        float xClipRealEnd = xClipStart + (xClipEnd - xClipStart) * checkMarkVisibility;
        graphics.setClip(0, 0, (int)Math.ceil(xClipRealEnd), dimension);
        graphics.setColor(SubstanceColorUtilities.getMarkColor(scheme, isEnabled));
        BasicStroke stroke = new BasicStroke(0.15f * (float)dimension, 1, 1);
        graphics.setStroke(stroke);
        graphics.draw(path);
        return result;
    }

    public static Icon getArrowIcon(int fontSize, int direction, SubstanceColorScheme colorScheme) {
        float width = SubstanceSizeUtils.getArrowIconWidth(fontSize);
        float height = SubstanceSizeUtils.getArrowIconHeight(fontSize);
        if (direction == 0) {
            height *= 2.0f;
        }
        float strokeWidth = SubstanceSizeUtils.getArrowStrokeWidth(fontSize);
        return new ImageIcon(SubstanceImageCreator.getArrow(width, height, strokeWidth, direction, colorScheme));
    }

    public static Icon getArrowIcon(float width, float height, float strokeWidth, int direction, SubstanceColorScheme scheme) {
        return new ImageIcon(SubstanceImageCreator.getArrow(width, height, strokeWidth, direction, scheme));
    }

    public static BufferedImage getArrow(float width, float height, float strokeWidth, int direction, SubstanceColorScheme scheme) {
        BufferedImage arrowImage = SubstanceCoreUtilities.getBlankImage((int)width, (int)height);
        Graphics2D graphics = (Graphics2D)arrowImage.getGraphics();
        graphics.translate(1, 1);
        height -= 2.0f;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color arrowColor = SubstanceColorUtilities.getMarkColor(scheme, true);
        graphics.setColor(arrowColor);
        int cap = (width -= 2.0f) < 15.0f ? 0 : 1;
        BasicStroke stroke = new BasicStroke(strokeWidth, cap, 0);
        graphics.setStroke(stroke);
        int cushion = (int)strokeWidth / 2;
        if (direction == 0) {
            BufferedImage top = SubstanceImageCreator.getArrow(width, height / 2.0f, strokeWidth, 1, scheme);
            BufferedImage bottom = SubstanceImageCreator.getArrow(width, height / 2.0f, strokeWidth, 5, scheme);
            graphics.drawImage((Image)top, 0, 1, null);
            graphics.drawImage((Image)bottom, 0, (int)height / 2 - 1, null);
            return arrowImage;
        }
        GeneralPath gp = new GeneralPath();
        gp.moveTo(cushion, strokeWidth);
        gp.lineTo(0.5f * (width - 1.0f), height - 1.0f - (float)cushion);
        gp.lineTo(width - 1.0f - (float)cushion, strokeWidth);
        graphics.draw(gp);
        int quadrantCounterClockwise = 0;
        switch (direction) {
            case 1: {
                quadrantCounterClockwise = 2;
                break;
            }
            case 7: {
                quadrantCounterClockwise = 1;
                break;
            }
            case 5: {
                quadrantCounterClockwise = 0;
                break;
            }
            case 3: {
                quadrantCounterClockwise = 3;
            }
        }
        BufferedImage rotatedImage = SubstanceImageCreator.getRotated(arrowImage, quadrantCounterClockwise);
        return rotatedImage;
    }

    public static Icon getDoubleArrowIconDelta(int fontSize, float deltaWidth, float deltaHeight, float deltaStrokeWidth, int direction, SubstanceColorScheme colorScheme) {
        float arrowWidth = SubstanceSizeUtils.getArrowIconWidth(fontSize) + deltaWidth;
        float arrowHeight = SubstanceSizeUtils.getArrowIconHeight(fontSize) + deltaHeight;
        float arrowStrokeWidth = SubstanceSizeUtils.getDoubleArrowStrokeWidth(fontSize) + deltaStrokeWidth;
        return SubstanceImageCreator.getDoubleArrowIcon(fontSize, arrowWidth, arrowHeight, arrowStrokeWidth, direction, colorScheme);
    }

    public static Icon getDoubleArrowIcon(int fontSize, float width, float height, float strokeWidth, int direction, SubstanceColorScheme colorScheme) {
        boolean toggle;
        int delta = 3 + 2 * SubstanceSizeUtils.getExtraPadding(fontSize) / 3;
        boolean bl = toggle = direction == 7 || direction == 3;
        if (toggle) {
            float temp = width;
            width = height;
            height = temp;
        }
        BufferedImage downArrowImage = SubstanceCoreUtilities.getBlankImage((int)width, (int)height);
        BufferedImage singleArrow = SubstanceImageCreator.getArrow(width, Math.max(1.0f, height - (float)delta), strokeWidth, 5, colorScheme);
        Graphics2D graphics = (Graphics2D)downArrowImage.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int arrowHeight = singleArrow.getHeight();
        int arrowWidth = singleArrow.getWidth();
        int offset = toggle ? (int)(height - (float)arrowHeight - (float)delta) / 2 : (int)(width - (float)arrowWidth - (float)delta) / 2;
        graphics.drawImage((Image)singleArrow, 0, offset, null);
        graphics.drawImage((Image)singleArrow, 0, offset + delta, null);
        int quadrantCounterClockwise = 0;
        switch (direction) {
            case 1: {
                quadrantCounterClockwise = 2;
                break;
            }
            case 7: {
                quadrantCounterClockwise = 1;
                break;
            }
            case 5: {
                quadrantCounterClockwise = 0;
                break;
            }
            case 3: {
                quadrantCounterClockwise = 3;
            }
        }
        BufferedImage arrowImage = SubstanceImageCreator.getRotated(downArrowImage, quadrantCounterClockwise);
        return new ImageIcon(arrowImage);
    }

    public static BufferedImage getRotated(BufferedImage bi, int quadrantClockwise) {
        int width = bi.getWidth();
        int height = bi.getHeight();
        if ((quadrantClockwise %= 4) == 1 || quadrantClockwise == 3) {
            width = bi.getHeight();
            height = bi.getWidth();
        }
        BufferedImage biRot = SubstanceCoreUtilities.getBlankImage(width, height);
        AffineTransform at = null;
        switch (quadrantClockwise) {
            case 1: {
                at = AffineTransform.getTranslateInstance(width, 0.0);
                at.rotate(1.5707963267948966);
                break;
            }
            case 2: {
                at = AffineTransform.getTranslateInstance(width, height);
                at.rotate(Math.PI);
                break;
            }
            case 3: {
                at = AffineTransform.getTranslateInstance(0.0, height);
                at.rotate(-1.5707963267948966);
            }
        }
        Graphics2D rotg = biRot.createGraphics();
        if (at != null) {
            rotg.setTransform(at);
        }
        rotg.drawImage((Image)bi, 0, 0, null);
        rotg.dispose();
        return biRot;
    }

    public static VolatileImage getRotated(VolatileImage bi, int quadrantClockwise) {
        int width = bi.getWidth();
        int height = bi.getHeight();
        if ((quadrantClockwise %= 4) == 1 || quadrantClockwise == 3) {
            width = bi.getHeight();
            height = bi.getWidth();
        }
        VolatileImage biRot = SubstanceCoreUtilities.getBlankVolatileImage(width, height);
        AffineTransform at = null;
        switch (quadrantClockwise) {
            case 1: {
                at = AffineTransform.getTranslateInstance(width, 0.0);
                at.rotate(1.5707963267948966);
                break;
            }
            case 2: {
                at = AffineTransform.getTranslateInstance(width, height);
                at.rotate(Math.PI);
                break;
            }
            case 3: {
                at = AffineTransform.getTranslateInstance(0.0, height);
                at.rotate(-1.5707963267948966);
            }
        }
        Graphics2D rotg = biRot.createGraphics();
        if (at != null) {
            rotg.setTransform(at);
        }
        rotg.drawImage(bi, 0, 0, null);
        rotg.dispose();
        return biRot;
    }

    public static Icon toGreyscale(Icon icon) {
        if (icon == null) {
            return null;
        }
        int width = icon.getIconWidth();
        int height = icon.getIconHeight();
        BufferedImage result = SubstanceCoreUtilities.getBlankImage(width, height);
        icon.paintIcon(null, result.getGraphics(), 0, 0);
        return new ImageIcon(new GrayscaleFilter().filter(result, null));
    }

    public static Icon makeTransparent(Component c2, Icon icon, double alpha) {
        if (icon == null) {
            return null;
        }
        int width = icon.getIconWidth();
        int height = icon.getIconHeight();
        BufferedImage result = SubstanceCoreUtilities.getBlankImage(width, height);
        icon.paintIcon(c2, result.getGraphics(), 0, 0);
        return new ImageIcon(new TranslucentFilter(alpha).filter(result, null));
    }

    public static BufferedImage getRadioButton(JComponent component, SubstanceFillPainter fillPainter, SubstanceBorderPainter borderPainter, int dimension, ComponentState componentState, int offsetX, SubstanceColorScheme fillColorScheme, SubstanceColorScheme markColorScheme, SubstanceColorScheme borderColorScheme, float checkMarkVisibility) {
        if (!componentState.isActive()) {
            fillPainter = SimplisticSoftBorderReverseFillPainter.INSTANCE;
        }
        float borderThickness = SubstanceSizeUtils.getBorderStrokeWidth(dimension);
        int delta = (int)((double)borderThickness - 0.6);
        Ellipse2D.Float contourBorder = new Ellipse2D.Float(delta, delta, dimension - 2 * delta - 1, dimension - 2 * delta - 1);
        BufferedImage offBackground = SubstanceCoreUtilities.getBlankImage(dimension + offsetX, dimension);
        Graphics2D graphics = (Graphics2D)offBackground.getGraphics();
        float alpha = SubstanceColorSchemeUtilities.getAlpha(component, componentState);
        graphics.setComposite(AlphaComposite.getInstance(3, alpha));
        graphics.translate(offsetX, 0);
        fillPainter.paintContourBackground(graphics, component, dimension, dimension, contourBorder, false, fillColorScheme, true);
        Ellipse2D.Float contourInner = new Ellipse2D.Float((float)delta + borderThickness, (float)delta + borderThickness, (float)(dimension - 2 * delta) - 2.0f * borderThickness, (float)(dimension - 2 * delta) - 2.0f * borderThickness);
        borderPainter.paintBorder(graphics, component, dimension, dimension, contourBorder, contourInner, borderColorScheme);
        graphics.setComposite(AlphaComposite.SrcOver);
        float rc = (float)dimension / 2.0f;
        float radius = (float)dimension / 4.5f;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if ((double)checkMarkVisibility > 0.0) {
            Ellipse2D.Double markOval = new Ellipse2D.Double(rc - radius, rc - radius, 2.0f * radius, 2.0f * radius);
            graphics.setComposite(AlphaComposite.getInstance(3, alpha * checkMarkVisibility));
            SubstanceImageCreator.drawRadioMark(graphics, SubstanceColorUtilities.getMarkColor(markColorScheme, !componentState.isDisabled()), markOval);
        } else {
            graphics.setPaint(new GradientPaint(rc + radius, rc - radius, fillColorScheme.getDarkColor(), rc - radius, rc + radius, fillColorScheme.getLightColor()));
            Ellipse2D.Double markOval = new Ellipse2D.Double(rc - radius, rc - radius, 2.0f * radius, 2.0f * radius);
            graphics.setComposite(AlphaComposite.getInstance(3, alpha * 0.3f));
            graphics.fill(markOval);
        }
        graphics.translate(-offsetX, 0);
        return offBackground;
    }

    private static void drawRadioMark(Graphics2D graphics, Color color, Shape markOval) {
        graphics.setColor(color);
        graphics.fill(markOval);
    }

    public static BufferedImage getCheckBox(AbstractButton button, SubstanceFillPainter fillPainter, SubstanceBorderPainter borderPainter, int dimension, ComponentState componentState, SubstanceColorScheme fillColorScheme, SubstanceColorScheme markColorScheme, SubstanceColorScheme borderColorScheme, float checkMarkVisibility, boolean isCheckMarkFadingOut) {
        int offset;
        int delta = offset = SubstanceSizeUtils.getAdjustedSize(SubstanceSizeUtils.getComponentFontSize(button), 3, 9, 1, false);
        float cornerRadius = SubstanceSizeUtils.getClassicButtonCornerRadius(SubstanceSizeUtils.getComponentFontSize(button));
        if (dimension <= 10) {
            offset = 2;
            cornerRadius = 2.0f;
        }
        int contourDim = dimension - delta;
        int borderDelta = (int)Math.floor((double)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(button)) / 2.0);
        GeneralPath contour = SubstanceOutlineUtilities.getBaseOutline(contourDim, contourDim, cornerRadius, null, borderDelta);
        if (!componentState.isActive()) {
            fillPainter = SimplisticSoftBorderReverseFillPainter.INSTANCE;
        }
        BufferedImage offBackground = SubstanceCoreUtilities.getBlankImage(dimension, dimension);
        Graphics2D graphics = (Graphics2D)offBackground.getGraphics();
        float alpha = SubstanceColorSchemeUtilities.getAlpha(button, componentState);
        graphics.setComposite(AlphaComposite.getInstance(3, alpha));
        graphics.translate(delta - 1, delta - 1);
        fillPainter.paintContourBackground(graphics, button, contourDim, contourDim, contour, false, fillColorScheme, true);
        int borderThickness = (int)SubstanceSizeUtils.getBorderStrokeWidth(dimension);
        GeneralPath contourInner = SubstanceOutlineUtilities.getBaseOutline(contourDim, contourDim, cornerRadius - (float)borderThickness, null, borderThickness + borderDelta);
        borderPainter.paintBorder(graphics, button, contourDim, contourDim, contour, contourInner, borderColorScheme);
        graphics.translate(-delta, 1 - delta);
        if ((double)checkMarkVisibility > 0.0) {
            if (isCheckMarkFadingOut) {
                graphics.setComposite(AlphaComposite.getInstance(3, alpha * checkMarkVisibility));
                checkMarkVisibility = 1.0f;
            }
            BufferedImage checkMark = SubstanceImageCreator.getCheckMark(dimension - 2 * offset / 3, !componentState.isDisabled(), markColorScheme, checkMarkVisibility);
            graphics.drawImage((Image)checkMark, 1 + 2 * offset / 3, dimension < 14 ? 0 : -1, null);
        }
        return offBackground;
    }

    public static void paintCompositeRoundedBackground(JComponent component, Graphics g2, int width, int height, int cornerRadius, AbstractButton decrButton, AbstractButton incrButton, boolean flipSides) {
        Graphics2D graphics;
        int delta = 3;
        if (decrButton != null) {
            graphics = (Graphics2D)g2.create();
            if (!flipSides) {
                graphics.clip(new Rectangle(-delta, 0, width / 2, height));
                graphics.translate(-delta, 0);
            } else {
                graphics.clip(new Rectangle(width / 2, 0, width / 2 + 1, height));
            }
            PairwiseButtonBackgroundDelegate.updatePairwiseBackground(graphics, decrButton, width + 2 * delta, height, flipSides ? SubstanceConstants.Side.RIGHT : SubstanceConstants.Side.LEFT, true);
            graphics.dispose();
        }
        if (incrButton != null) {
            graphics = (Graphics2D)g2.create();
            if (!flipSides) {
                graphics.clip(new Rectangle(width / 2, 0, width / 2 + 1, height));
            } else {
                graphics.clip(new Rectangle(-delta, 0, width / 2, height));
                graphics.translate(-delta, 0);
            }
            PairwiseButtonBackgroundDelegate.updatePairwiseBackground(graphics, incrButton, width + 2 * delta, height, flipSides ? SubstanceConstants.Side.LEFT : SubstanceConstants.Side.RIGHT, true);
            graphics.dispose();
        }
    }

    private static BufferedImage overlayEcho(BufferedImage image, float echoAlpha, int offsetX, int offsetY) {
        int width = image.getWidth();
        int height = image.getHeight();
        offsetY = 0;
        offsetX = 0;
        BufferedImage negated = SubstanceImageCreator.getNegated(image);
        BufferedImage result = SubstanceCoreUtilities.getBlankImage(width, height);
        Graphics2D graphics = (Graphics2D)result.getGraphics().create();
        graphics.setComposite(AlphaComposite.getInstance(3, 0.2f * echoAlpha * echoAlpha * echoAlpha));
        graphics.drawImage((Image)negated, offsetX - 1, offsetY - 1, null);
        graphics.drawImage((Image)negated, offsetX + 1, offsetY - 1, null);
        graphics.drawImage((Image)negated, offsetX - 1, offsetY + 1, null);
        graphics.drawImage((Image)negated, offsetX + 1, offsetY + 1, null);
        graphics.setComposite(AlphaComposite.getInstance(3, 0.7f * echoAlpha * echoAlpha * echoAlpha));
        graphics.drawImage((Image)negated, offsetX, offsetY - 1, null);
        graphics.drawImage((Image)negated, offsetX, offsetY + 1, null);
        graphics.drawImage((Image)negated, offsetX - 1, offsetY, null);
        graphics.drawImage((Image)negated, offsetX + 1, offsetY, null);
        graphics.setComposite(AlphaComposite.getInstance(3, 1.0f));
        graphics.drawImage((Image)image, 0, 0, null);
        graphics.dispose();
        return result;
    }

    public static Icon getMinimizeIcon(SubstanceColorScheme scheme, SubstanceColorScheme backgroundScheme) {
        int iSize = SubstanceSizeUtils.getTitlePaneIconSize();
        return SubstanceImageCreator.getMinimizeIcon(iSize, scheme, backgroundScheme);
    }

    public static Icon getMinimizeIcon(int iSize, SubstanceColorScheme scheme, SubstanceColorScheme backgroundScheme) {
        BufferedImage image = SubstanceCoreUtilities.getBlankImage(iSize, iSize);
        Graphics2D graphics = (Graphics2D)image.getGraphics();
        int start = iSize / 4 - 2;
        int end = 3 * iSize / 4;
        int size = end - start - 3;
        Color color = SubstanceColorUtilities.getMarkColor(scheme, true);
        graphics.setColor(color);
        graphics.fillRect(start + 2, end - 2, size, 3);
        int fgStrength = SubstanceColorUtilities.getColorBrightness(color.getRGB());
        int fgNegativeStrength = SubstanceColorUtilities.getColorBrightness(SubstanceColorUtilities.getNegativeColor(color.getRGB()));
        int bgStrength = SubstanceColorUtilities.getColorBrightness(backgroundScheme.getLightColor().getRGB());
        boolean noEcho = fgStrength > fgNegativeStrength && fgStrength < bgStrength;
        return new ImageIcon(SubstanceImageCreator.overlayEcho(image, noEcho ? 0.0f : SubstanceColorUtilities.getColorStrength(color), 1, 1));
    }

    public static Icon getRestoreIcon(SubstanceColorScheme scheme, SubstanceColorScheme backgroundScheme) {
        int iSize = SubstanceSizeUtils.getTitlePaneIconSize();
        BufferedImage image = SubstanceCoreUtilities.getBlankImage(iSize, iSize);
        Graphics2D graphics = (Graphics2D)image.getGraphics();
        int start = iSize / 4 - 2;
        int end = 3 * iSize / 4 - 1;
        int size = end - start - 3;
        Color color = SubstanceColorUtilities.getMarkColor(scheme, true);
        graphics.setColor(color);
        graphics.drawRect(start, end - size + 1, size, size);
        graphics.drawLine(start, end - size + 2, start + size, end - size + 2);
        graphics.fillRect(end - size, start + 1, size + 1, 2);
        graphics.drawLine(end, start + 1, end, start + size + 1);
        graphics.drawLine(start + size + 2, start + size + 1, end, start + size + 1);
        int fgStrength = SubstanceColorUtilities.getColorBrightness(color.getRGB());
        int fgNegativeStrength = SubstanceColorUtilities.getColorBrightness(SubstanceColorUtilities.getNegativeColor(color.getRGB()));
        int bgStrength = SubstanceColorUtilities.getColorBrightness(backgroundScheme.getLightColor().getRGB());
        boolean noEcho = fgStrength > fgNegativeStrength && fgStrength < bgStrength;
        return new ImageIcon(SubstanceImageCreator.overlayEcho(image, noEcho ? 0.0f : SubstanceColorUtilities.getColorStrength(color), 1, 1));
    }

    public static Icon getMaximizeIcon(SubstanceColorScheme scheme, SubstanceColorScheme backgroundScheme) {
        int iSize = SubstanceSizeUtils.getTitlePaneIconSize();
        return SubstanceImageCreator.getMaximizeIcon(iSize, scheme, backgroundScheme);
    }

    public static Icon getMaximizeIcon(int iSize, SubstanceColorScheme scheme, SubstanceColorScheme backgroundScheme) {
        BufferedImage image = SubstanceCoreUtilities.getBlankImage(iSize, iSize);
        Graphics2D graphics = (Graphics2D)image.getGraphics();
        int start = iSize / 4 - 1;
        int end = iSize - start - 1;
        Color color = SubstanceColorUtilities.getMarkColor(scheme, true);
        graphics.setColor(color);
        graphics.drawRect(start, start, end - start, end - start);
        graphics.drawLine(start, start + 1, end, start + 1);
        int fgStrength = SubstanceColorUtilities.getColorBrightness(color.getRGB());
        int fgNegativeStrength = SubstanceColorUtilities.getColorBrightness(SubstanceColorUtilities.getNegativeColor(color.getRGB()));
        int bgStrength = SubstanceColorUtilities.getColorBrightness(backgroundScheme.getLightColor().getRGB());
        boolean noEcho = fgStrength > fgNegativeStrength && fgStrength < bgStrength;
        return new ImageIcon(SubstanceImageCreator.overlayEcho(image, noEcho ? 0.0f : SubstanceColorUtilities.getColorStrength(color), 1, 1));
    }

    public static Icon getCloseIcon(SubstanceColorScheme scheme, SubstanceColorScheme backgroundScheme) {
        return SubstanceImageCreator.getCloseIcon(SubstanceSizeUtils.getTitlePaneIconSize(), scheme, backgroundScheme);
    }

    public static Icon getCloseIcon(int iSize, SubstanceColorScheme colorScheme, SubstanceColorScheme backgroundScheme) {
        BufferedImage image = SubstanceCoreUtilities.getBlankImage(iSize, iSize);
        Graphics2D graphics = (Graphics2D)image.getGraphics();
        if (iSize < 15) {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        } else {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }
        int start = iSize / 4;
        int end = 3 * iSize / 4;
        BasicStroke stroke = new BasicStroke(SubstanceSizeUtils.getFocusStrokeWidth(iSize), 1, 1);
        graphics.setStroke(stroke);
        Color color = SubstanceColorUtilities.getMarkColor(colorScheme, true);
        graphics.setColor(color);
        graphics.drawLine(start, start, end, end);
        graphics.drawLine(start, end, end, start);
        int fgStrength = SubstanceColorUtilities.getColorBrightness(color.getRGB());
        int fgNegativeStrength = SubstanceColorUtilities.getColorBrightness(SubstanceColorUtilities.getNegativeColor(color.getRGB()));
        int bgStrength = SubstanceColorUtilities.getColorBrightness(backgroundScheme.getLightColor().getRGB());
        boolean noEcho = fgStrength > fgNegativeStrength && fgStrength < bgStrength;
        return new ImageIcon(SubstanceImageCreator.overlayEcho(image, noEcho ? 0.0f : SubstanceColorUtilities.getColorStrength(color), 1, 1));
    }

    public static void paintRectangularBackground(Component c2, Graphics g2, int startX, int startY, int width, int height, SubstanceColorScheme colorScheme, float borderAlpha, boolean isVertical) {
        LinearGradientPaint paint;
        Graphics2D graphics = (Graphics2D)g2.create();
        graphics.translate(startX, startY);
        if (!isVertical) {
            paint = new LinearGradientPaint(0.0f, 0.0f, 0.0f, height, new float[]{0.0f, 0.4f, 0.5f, 1.0f}, new Color[]{colorScheme.getUltraLightColor(), colorScheme.getLightColor(), colorScheme.getMidColor(), colorScheme.getUltraLightColor()}, MultipleGradientPaint.CycleMethod.REPEAT);
            graphics.setPaint(paint);
            graphics.fillRect(0, 0, width, height);
        } else {
            paint = new LinearGradientPaint(0.0f, 0.0f, width, 0.0f, new float[]{0.0f, 0.4f, 0.5f, 1.0f}, new Color[]{colorScheme.getUltraLightColor(), colorScheme.getLightColor(), colorScheme.getMidColor(), colorScheme.getUltraLightColor()}, MultipleGradientPaint.CycleMethod.REPEAT);
            graphics.setPaint(paint);
            graphics.fillRect(0, 0, width, height);
        }
        if (borderAlpha > 0.0f) {
            Graphics2D g2d = (Graphics2D)graphics.create();
            g2d.setComposite(LafWidgetUtilities.getAlphaComposite(null, borderAlpha, graphics));
            SubstanceImageCreator.paintSimpleBorderAliased(c2, g2d, width, height, colorScheme);
            g2d.dispose();
        }
        graphics.dispose();
    }

    public static void paintSimpleBorder(Component c2, Graphics2D g2d, int width, int height, SubstanceColorScheme borderColorScheme) {
        int componentFontSize = SubstanceSizeUtils.getComponentFontSize(c2);
        float borderThickness = (float)Math.floor(SubstanceSizeUtils.getBorderStrokeWidth(componentFontSize));
        g2d.setColor(SubstanceColorUtilities.getMidBorderColor(borderColorScheme));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        int joinKind = 1;
        int capKind = 0;
        g2d.setStroke(new BasicStroke(borderThickness, capKind, joinKind));
        g2d.draw(new Rectangle2D.Float((borderThickness - 1.0f) / 2.0f, (borderThickness - 1.0f) / 2.0f, (float)(width - 1) - (borderThickness - 1.5f), (float)(height - 1) - (borderThickness - 1.5f)));
    }

    public static void paintSimpleBorderAliased(Component c2, Graphics2D g2d, int width, int height, SubstanceColorScheme colorScheme) {
        int componentFontSize = SubstanceSizeUtils.getComponentFontSize(c2);
        float borderThickness = (float)Math.floor(SubstanceSizeUtils.getBorderStrokeWidth(componentFontSize));
        g2d.setColor(SubstanceColorUtilities.getMidBorderColor(colorScheme));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        int joinKind = 0;
        int capKind = 2;
        g2d.setStroke(new BasicStroke(borderThickness, capKind, joinKind));
        g2d.draw(new Rectangle2D.Float(borderThickness / 2.0f, borderThickness / 2.0f, (float)width - borderThickness, (float)height - borderThickness));
    }

    public static void paintRectangularStripedBackground(Component c2, Graphics g2, int startX, int startY, int width, int height, SubstanceColorScheme colorScheme, BufferedImage stripeImage, int stripeOffset, float borderAlpha, boolean isVertical) {
        LinearGradientPaint paint;
        Graphics2D graphics = (Graphics2D)g2.create(startX, startY, width, height);
        if (!isVertical) {
            paint = new LinearGradientPaint(0.0f, 0.0f, 0.0f, height, new float[]{0.0f, 0.2f, 0.5f, 0.8f, 1.0f}, new Color[]{colorScheme.getDarkColor(), colorScheme.getLightColor(), colorScheme.getMidColor(), colorScheme.getLightColor(), colorScheme.getDarkColor()}, MultipleGradientPaint.CycleMethod.REPEAT);
            graphics.setPaint(paint);
            graphics.fillRect(0, 0, width, height);
            if (stripeImage != null) {
                int stripeSize = stripeImage.getHeight();
                int stripeCount = width / stripeSize;
                stripeOffset %= 2 * stripeSize;
                for (int stripe = -2; stripe <= stripeCount; stripe += 2) {
                    int stripePos = stripe * stripeSize + stripeOffset;
                    graphics.drawImage((Image)stripeImage, stripePos, 0, null);
                }
            }
        } else {
            paint = new LinearGradientPaint(0.0f, 0.0f, width, 0.0f, new float[]{0.0f, 0.2f, 0.5f, 0.8f, 1.0f}, new Color[]{colorScheme.getDarkColor(), colorScheme.getLightColor(), colorScheme.getMidColor(), colorScheme.getLightColor(), colorScheme.getDarkColor()}, MultipleGradientPaint.CycleMethod.REPEAT);
            graphics.setPaint(paint);
            graphics.fillRect(0, 0, width, height);
            if (stripeImage != null) {
                int stripeSize = stripeImage.getWidth();
                int stripeCount = height / stripeSize;
                stripeOffset %= 2 * stripeSize;
                for (int stripe = -2; stripe <= stripeCount; stripe += 2) {
                    int stripePos = stripe * stripeSize + stripeOffset;
                    graphics.drawImage((Image)stripeImage, 0, stripePos, null);
                }
            }
        }
        if (borderAlpha > 0.0f) {
            Graphics2D g2d = (Graphics2D)graphics.create();
            g2d.setComposite(LafWidgetUtilities.getAlphaComposite(null, borderAlpha, graphics));
            SubstanceImageCreator.paintSimpleBorderAliased(c2, g2d, width, height, colorScheme);
            g2d.dispose();
        }
        graphics.dispose();
    }

    public static BufferedImage getStripe(int baseSize, Color color) {
        int width = (int)(1.8 * (double)baseSize);
        int height = baseSize;
        BufferedImage result = SubstanceCoreUtilities.getBlankImage(width, height);
        Graphics2D graphics = (Graphics2D)result.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Polygon polygon = new Polygon();
        polygon.addPoint(0, 0);
        polygon.addPoint(width - 1 - baseSize, 0);
        polygon.addPoint(width - 1, height - 1);
        polygon.addPoint(baseSize, height - 1);
        graphics.setColor(color);
        graphics.fillPolygon(polygon);
        graphics.drawPolygon(polygon);
        float[] BLUR = new float[]{0.1f, 0.1f, 0.1f, 0.1f, 0.3f, 0.1f, 0.1f, 0.1f, 0.1f};
        ConvolveOp vBlurOp = new ConvolveOp(new Kernel(3, 3, BLUR));
        BufferedImage blurred = vBlurOp.filter(result, null);
        return blurred;
    }

    public static BufferedImage getDragImage(Component c2, SubstanceColorScheme colorScheme, int width, int height, int maxNumberOfStripes) {
        BufferedImage result = SubstanceCoreUtilities.getBlankImage(width, height);
        Graphics2D graphics = (Graphics2D)result.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        boolean isDark = colorScheme.isDark();
        Color back1 = isDark ? colorScheme.getLightColor() : SubstanceColorUtilities.getInterpolatedColor(colorScheme.getLightColor(), colorScheme.getDarkColor(), 0.8);
        Color back2 = isDark ? colorScheme.getExtraLightColor() : SubstanceColorUtilities.getInterpolatedColor(colorScheme.getMidColor(), colorScheme.getDarkColor(), 0.4);
        Color fore = isDark ? colorScheme.getDarkColor() : colorScheme.getUltraLightColor();
        int componentFontSize = SubstanceSizeUtils.getComponentFontSize(c2);
        int bumpDotDiameter = SubstanceSizeUtils.getDragBumpDiameter(componentFontSize);
        int bumpCellSize = (int)(1.5 * (double)bumpDotDiameter + 1.0);
        int bumpRows = Math.max(1, height / bumpCellSize - 1);
        int bumpColumns = Math.max(1, (width - 2) / bumpCellSize);
        if (maxNumberOfStripes > 0) {
            if (height > width) {
                bumpColumns = Math.min(bumpColumns, maxNumberOfStripes);
            } else {
                bumpRows = Math.min(bumpRows, maxNumberOfStripes);
            }
        }
        int bumpRowOffset = (height - bumpCellSize * bumpRows) / 2;
        int bumpColOffset = 1 + (width - bumpCellSize * bumpColumns) / 2;
        for (int col = 0; col < bumpColumns; ++col) {
            int cx = bumpColOffset + col * bumpCellSize;
            boolean isEvenCol = col % 2 == 0;
            int offsetY = isEvenCol ? 0 : bumpDotDiameter;
            for (int row = 0; row < bumpRows; ++row) {
                int cy = offsetY + bumpRowOffset + row * bumpCellSize;
                graphics.setColor(fore);
                graphics.fillOval(cx + 1, cy + 1, bumpDotDiameter, bumpDotDiameter);
                graphics.setPaint(new GradientPaint(cx, cy, back1, cx + bumpDotDiameter - 1, cy + bumpDotDiameter - 1, back2));
                graphics.fillOval(cx, cy, bumpDotDiameter, bumpDotDiameter);
            }
        }
        return result;
    }

    public static void paintSplitDividerBumpImage(Graphics g2, SubstanceSplitPaneDivider divider, int x2, int y2, int width, int height, boolean isHorizontal, SubstanceColorScheme colorScheme) {
        Graphics2D graphics = (Graphics2D)g2.create();
        graphics.translate(x2, y2);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int componentFontSize = SubstanceSizeUtils.getComponentFontSize(divider);
        int bumpDotDiameter = SubstanceSizeUtils.getBigDragBumpDiameter(componentFontSize);
        int bumpCellSize = (int)(1.5 * (double)bumpDotDiameter + 1.0);
        int bumpRows = isHorizontal ? 1 : Math.max(1, height / bumpCellSize - 1);
        int bumpColumns = isHorizontal ? Math.max(1, (width - 2) / bumpCellSize) : 1;
        int bumpRowOffset = (height - bumpCellSize * bumpRows) / 2;
        int bumpColOffset = 1 + (width - bumpCellSize * bumpColumns) / 2;
        BufferedImage singleDot = SubstanceCoreUtilities.getBlankImage(bumpDotDiameter, bumpDotDiameter);
        Graphics2D dotGraphics = (Graphics2D)singleDot.getGraphics();
        dotGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color markColor = SubstanceColorUtilities.getMarkColor(colorScheme, divider.isEnabled());
        dotGraphics.setColor(markColor);
        dotGraphics.fillOval(0, 0, bumpDotDiameter, bumpDotDiameter);
        dotGraphics.setComposite(AlphaComposite.getInstance(3, 0.4f));
        SubstanceBorderPainter borderPainter = SubstanceCoreUtilities.getBorderPainter(divider);
        borderPainter.paintBorder(dotGraphics, divider, width, height, new Ellipse2D.Float(0.0f, 0.0f, bumpDotDiameter - 1, bumpDotDiameter - 1), null, colorScheme);
        graphics.setComposite(LafWidgetUtilities.getAlphaComposite(divider, 0.8f, g2));
        for (int col = 0; col < bumpColumns; ++col) {
            int cx = bumpColOffset + col * bumpCellSize;
            for (int row = 0; row < bumpRows; ++row) {
                int cy = bumpRowOffset + row * bumpCellSize + (bumpCellSize - bumpDotDiameter) / 2;
                graphics.drawImage((Image)singleDot, cx, cy, null);
            }
        }
        graphics.dispose();
    }

    public static BufferedImage getResizeGripImage(Component c2, SubstanceColorScheme colorScheme, int dimension, boolean isCrowded) {
        BufferedImage result = SubstanceCoreUtilities.getBlankImage(dimension, dimension);
        Graphics2D graphics = (Graphics2D)result.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        boolean isDark = colorScheme.isDark();
        Color back1 = isDark ? colorScheme.getLightColor() : SubstanceColorUtilities.getInterpolatedColor(colorScheme.getLightColor(), colorScheme.getDarkColor(), 0.8);
        Color back2 = isDark ? colorScheme.getExtraLightColor() : SubstanceColorUtilities.getInterpolatedColor(colorScheme.getMidColor(), colorScheme.getDarkColor(), 0.4);
        Color fore = isDark ? colorScheme.getDarkColor() : colorScheme.getUltraLightColor();
        int bumpDotDiameter = SubstanceSizeUtils.getDragBumpDiameter(SubstanceSizeUtils.getComponentFontSize(c2));
        int bumpCellSize = (int)(1.5 * (double)bumpDotDiameter + 1.0);
        if (isCrowded) {
            --bumpCellSize;
        }
        int bumpLines = dimension / bumpCellSize;
        int bumpOffset = (dimension - bumpCellSize * bumpLines) / 2;
        for (int col = 0; col < bumpLines; ++col) {
            int cx = bumpOffset + col * bumpCellSize;
            for (int row = bumpLines - col - 1; row < bumpLines; ++row) {
                int cy = bumpOffset + row * bumpCellSize;
                graphics.setColor(fore);
                graphics.fillOval(cx + 1, cy + 1, bumpDotDiameter, bumpDotDiameter);
                graphics.setPaint(new GradientPaint(cx, cy, back1, cx + bumpDotDiameter - 1, cy + bumpDotDiameter - 1, back2));
                graphics.fillOval(cx, cy, bumpDotDiameter, bumpDotDiameter);
            }
        }
        return result;
    }

    public static BufferedImage getTreeIcon(JTree tree, SubstanceColorScheme fillScheme, SubstanceColorScheme borderScheme, boolean isCollapsed) {
        Color signColor;
        int fontSize = SubstanceSizeUtils.getComponentFontSize(tree);
        int dim = SubstanceSizeUtils.getTreeIconSize(fontSize);
        BufferedImage result = SubstanceCoreUtilities.getBlankImage(dim + 2, dim);
        Graphics2D graphics = (Graphics2D)result.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        Graphics2D g2 = (Graphics2D)graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        SimplisticSoftBorderReverseFillPainter gradPainter = new SimplisticSoftBorderReverseFillPainter();
        FlatBorderPainter fbp = new FlatBorderPainter();
        int borderDelta = (int)Math.floor((double)SubstanceSizeUtils.getBorderStrokeWidth(fontSize) / 2.0);
        GeneralPath contour = SubstanceOutlineUtilities.getBaseOutline(dim - 1, dim - 1, SubstanceSizeUtils.getClassicButtonCornerRadius(dim), null, borderDelta);
        g2.translate(0, 1);
        boolean isDark = fillScheme.isDark();
        fillScheme = new ShiftColorScheme(fillScheme, fillScheme.getExtraLightColor(), 0.7);
        gradPainter.paintContourBackground(g2, tree, dim - 1, dim - 1, contour, false, fillScheme, false);
        borderScheme = new ShiftColorScheme(borderScheme, isDark ? borderScheme.getUltraLightColor() : borderScheme.getLightColor(), 0.5);
        fbp.paintBorder(g2, tree, dim - 1, dim - 1, contour, null, borderScheme);
        g2.translate(-1, -1);
        Color color = signColor = isDark ? borderScheme.getUltraLightColor().brighter().brighter() : borderScheme.getUltraDarkColor();
        if (tree != null && !tree.isEnabled()) {
            signColor = borderScheme.getForegroundColor();
        }
        g2.setColor(signColor);
        int mid = dim / 2;
        int length = 5 * dim / 12;
        g2.drawLine(mid - length / 2, dim / 2, mid + length / 2, dim / 2);
        if (isCollapsed) {
            g2.drawLine(dim / 2, mid - length / 2, dim / 2, mid + length / 2);
        }
        g2.dispose();
        return result;
    }

    public static BufferedImage getSingleCrayon(Color mainColor, int width, int height) {
        BufferedImage image = SubstanceCoreUtilities.getBlankImage(width, height);
        int baseTop = (int)(0.2 * (double)height);
        Graphics2D graphics = (Graphics2D)image.getGraphics().create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int r2 = mainColor.getRed();
        int g2 = mainColor.getGreen();
        int b2 = mainColor.getBlue();
        double lc = 0.8;
        int lr = (int)((double)r2 + (double)(255 - r2) * lc);
        int lg = (int)((double)g2 + (double)(255 - g2) * lc);
        int lb = (int)((double)b2 + (double)(255 - b2) * lc);
        double dc = 0.05;
        int dr = (int)((1.0 - dc) * (double)r2);
        int dg = (int)((1.0 - dc) * (double)g2);
        int db = (int)((1.0 - dc) * (double)b2);
        Color lightColor = new Color(lr, lg, lb);
        Color darkColor = new Color(dr, dg, db);
        LinearGradientPaint paint = new LinearGradientPaint(0.0f, 0.0f, width, 0.0f, new float[]{0.0f, 0.3f, 0.5f, 0.9f, 1.0f}, new Color[]{lightColor, darkColor, darkColor, lightColor, lightColor}, MultipleGradientPaint.CycleMethod.REPEAT);
        graphics.setPaint(paint);
        graphics.fillRect(0, baseTop, width, height);
        int dbwr = lr;
        int dbwg = lg;
        int dbwb = lb;
        int lbwr = 128 + dr / 4;
        int lbwg = 128 + dg / 4;
        int lbwb = 128 + db / 4;
        Color lightStripeColor = new Color(lbwr, lbwg, lbwb);
        Color darkStripeColor = new Color(dbwr, dbwg, dbwb);
        int stripeTop = (int)(0.35 * (double)height);
        int stripeHeight = (int)(0.04 * (double)height);
        LinearGradientPaint stripePaint = new LinearGradientPaint(0.0f, 0.0f, width, 0.0f, new float[]{0.0f, 0.3f, 0.5f, 0.9f, 1.0f}, new Color[]{lightStripeColor, darkStripeColor, darkStripeColor, lightStripeColor, lightStripeColor}, MultipleGradientPaint.CycleMethod.REPEAT);
        graphics.setPaint(stripePaint);
        graphics.fillRect(0, stripeTop, width, stripeHeight);
        graphics.setColor(lightStripeColor);
        graphics.drawRect(0, stripeTop, width - 1, stripeHeight);
        GeneralPath capPath = new GeneralPath();
        capPath.moveTo(0.5f * (float)width - 3.0f, 4.0f);
        capPath.quadTo(0.5f * (float)width, 0.0f, 0.5f * (float)width + 3.0f, 4.0f);
        capPath.lineTo(width - 3, baseTop);
        capPath.lineTo(2.0f, baseTop);
        capPath.lineTo(0.5f * (float)width - 3.0f, 4.0f);
        graphics.setClip(capPath);
        graphics.setPaint(new GradientPaint(0.0f, baseTop / 2, lightColor, (int)(0.6 * (double)width), baseTop, mainColor));
        graphics.fillRect(0, 0, width / 2, baseTop);
        graphics.setPaint(new GradientPaint(width, baseTop / 2, lightColor, (int)(0.4 * (double)width), baseTop, mainColor));
        graphics.fillRect(width / 2, 0, width / 2, baseTop);
        graphics.setStroke(new BasicStroke(1.3f, 1, 1));
        graphics.setClip(null);
        graphics.setColor(new Color(64 + dr / 2, 64 + dg / 2, 64 + db / 2, 200));
        graphics.drawRect(0, baseTop, width - 1, height - baseTop - 1);
        graphics.draw(capPath);
        graphics.dispose();
        return image;
    }

    private static int crayonX(int i2) {
        return i2 % 8 * 22 + 4 + i2 / 8 % 2 * 11;
    }

    private static int crayonY(int i2) {
        return i2 / 8 * 20 + 23;
    }

    public static Image getCrayonsImage() {
        int iw = 195;
        int ih = 208;
        BufferedImage image = SubstanceCoreUtilities.getBlankImage(iw, ih);
        Graphics2D graphics = (Graphics2D)((Image)image).getGraphics().create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(new Color(240, 240, 240));
        graphics.fillRect(0, 0, iw, ih);
        for (int i2 = 0; i2 < crayonColors.length; ++i2) {
            Color crayonColor = new Color(0xFF000000 | crayonColors[i2]);
            BufferedImage crayonImage = SubstanceImageCreator.getSingleCrayon(crayonColor, 22, 120);
            graphics.drawImage((Image)crayonImage, SubstanceImageCreator.crayonX(i2), SubstanceImageCreator.crayonY(i2), null);
        }
        graphics.setColor(new Color(190, 190, 190));
        graphics.drawRoundRect(0, 1, iw - 1, ih - 2, 4, 4);
        graphics.dispose();
        return image;
    }

    public static Icon getHexaMarker(int value, SubstanceColorScheme colorScheme) {
        BufferedImage result = SubstanceCoreUtilities.getBlankImage(9, 9);
        value %= 16;
        Color offColor = null;
        Color onColor = null;
        if (colorScheme == null) {
            return new ImageIcon(result);
        }
        boolean isDark = colorScheme.isDark();
        offColor = isDark ? colorScheme.getMidColor() : colorScheme.getMidColor().darker();
        onColor = isDark ? SubstanceColorUtilities.getInterpolatedColor(colorScheme.getUltraLightColor(), Color.white, 0.2) : colorScheme.getUltraDarkColor().darker();
        boolean bit1 = (value & 1) != 0;
        boolean bit2 = (value & 2) != 0;
        boolean bit3 = (value & 4) != 0;
        boolean bit4 = (value & 8) != 0;
        Graphics2D graphics = (Graphics2D)result.getGraphics().create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(bit1 ? onColor : offColor);
        graphics.fillOval(5, 5, 4, 4);
        graphics.setColor(bit2 ? onColor : offColor);
        graphics.fillOval(5, 0, 4, 4);
        graphics.setColor(bit3 ? onColor : offColor);
        graphics.fillOval(0, 5, 4, 4);
        graphics.setColor(bit4 ? onColor : offColor);
        graphics.fillOval(0, 0, 4, 4);
        graphics.dispose();
        return new ImageIcon(result);
    }

    public static Icon getSearchIcon(int dimension, SubstanceColorScheme colorScheme, boolean leftToRight) {
        BufferedImage result = SubstanceCoreUtilities.getBlankImage(dimension, dimension);
        Graphics2D graphics = (Graphics2D)result.getGraphics().create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color color = SubstanceColorUtilities.getMarkColor(colorScheme, true);
        graphics.setColor(color);
        graphics.setStroke(new BasicStroke(1.5f));
        if (leftToRight) {
            int xc = (int)(0.6 * (double)dimension);
            int yc = (int)(0.45 * (double)dimension);
            int r2 = (int)(0.3 * (double)dimension);
            graphics.drawOval(xc - r2, yc - r2, 2 * r2, 2 * r2);
            graphics.setStroke(new BasicStroke(3.0f));
            GeneralPath handle = new GeneralPath();
            handle.moveTo((float)((double)xc - (double)r2 / Math.sqrt(2.0)), (float)((double)yc + (double)r2 / Math.sqrt(2.0)));
            handle.lineTo(1.8f, (float)dimension - 2.2f);
            graphics.draw(handle);
        } else {
            int xc = (int)(0.4 * (double)dimension);
            int yc = (int)(0.45 * (double)dimension);
            int r3 = (int)(0.3 * (double)dimension);
            graphics.drawOval(xc - r3, yc - r3, 2 * r3, 2 * r3);
            graphics.setStroke(new BasicStroke(3.0f));
            GeneralPath handle = new GeneralPath();
            handle.moveTo((float)((double)xc + (double)r3 / Math.sqrt(2.0)), (float)((double)yc + (double)r3 / Math.sqrt(2.0)));
            handle.lineTo((float)dimension - 2.5f, (float)dimension - 2.2f);
            graphics.draw(handle);
        }
        graphics.dispose();
        return new ImageIcon(result);
    }

    public static Icon getWatermarkIcon(SubstanceWatermark watermark) {
        int iSize = SubstanceSizeUtils.getTitlePaneIconSize();
        BufferedImage result = SubstanceCoreUtilities.getBlankImage(iSize, iSize);
        Graphics2D graphics = (Graphics2D)result.getGraphics().create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        watermark.previewWatermark(graphics, SubstanceColorSchemeUtilities.METALLIC_SKIN, 0, 0, iSize, iSize);
        graphics.dispose();
        return new ImageIcon(result);
    }

    public static Icon getSmallLockIcon(SubstanceColorScheme scheme, Component c2) {
        int componentFontSize = SubstanceSizeUtils.getComponentFontSize(c2);
        int extraPadding = SubstanceSizeUtils.getExtraPadding(componentFontSize);
        int width = 6 + 2 * extraPadding;
        int height = 9 + 2 * extraPadding;
        BufferedImage result = SubstanceCoreUtilities.getBlankImage(width, height);
        Color fore = scheme.getForegroundColor();
        Color fill = new Color(208, 208, 48);
        Graphics2D graphics = (Graphics2D)result.getGraphics().create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        float borderStrokeWidth = SubstanceSizeUtils.getBorderStrokeWidth(componentFontSize) / 1.2f;
        float extraInsets = borderStrokeWidth / 2.0f;
        graphics.setStroke(new BasicStroke(borderStrokeWidth, 2, 1));
        float lockPadTop = (float)height / 3.0f;
        float lockPadBottom = (float)height - extraInsets;
        float lockPadLeft = extraInsets;
        float lockPadRight = (float)width - extraInsets;
        graphics.setColor(fill);
        graphics.fill(new Rectangle2D.Float(lockPadLeft, lockPadTop, lockPadRight - lockPadLeft, lockPadBottom - lockPadTop));
        graphics.setColor(fore);
        graphics.draw(new Rectangle2D.Float(lockPadLeft, lockPadTop, lockPadRight - lockPadLeft, lockPadBottom - lockPadTop));
        graphics.setColor(fore);
        float lockHandleLeft = (float)width / 4.0f;
        float lockHandleRight = (float)width - (float)width / 4.0f;
        GeneralPath handle = new GeneralPath();
        handle.moveTo(lockHandleLeft, lockPadTop);
        handle.lineTo(lockHandleLeft, extraInsets);
        handle.lineTo(lockHandleRight, extraInsets);
        handle.lineTo(lockHandleRight, lockPadTop);
        graphics.draw(handle);
        graphics.setColor(fore);
        float lockKeyholeTop = lockPadTop + 2.0f * borderStrokeWidth;
        float lockKeyholeBottom = lockPadBottom - 2.0f * borderStrokeWidth + 1.0f;
        float lockKeyholeLeft = lockHandleLeft + 1.0f;
        float lockKeyholeRight = lockHandleRight;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        graphics.setStroke(new BasicStroke(1.0f));
        graphics.fill(new Rectangle2D.Float(lockKeyholeLeft, lockKeyholeTop, lockKeyholeRight - lockKeyholeLeft, lockKeyholeBottom - lockKeyholeTop));
        graphics.dispose();
        return new ImageIcon(result);
    }

    public static BufferedImage getNegated(BufferedImage bi) {
        return new NegatedFilter().filter(bi, null);
    }

    public static BufferedImage getColorSchemeImage(Component comp, Icon original, SubstanceColorScheme colorScheme, float originalBrightnessFactor) {
        int w2 = original.getIconWidth();
        int h2 = original.getIconHeight();
        BufferedImage origImage = SubstanceCoreUtilities.getBlankImage(w2, h2);
        original.paintIcon(comp, origImage.getGraphics(), 0, 0);
        return SubstanceImageCreator.getColorSchemeImage(origImage, colorScheme, originalBrightnessFactor);
    }

    public static BufferedImage getColorSchemeImage(BufferedImage original, SubstanceColorScheme colorScheme, float originalBrightnessFactor) {
        return ColorSchemeFilter.getColorSchemeFilter(colorScheme, originalBrightnessFactor).filter(original, null);
    }

    public static class SimplisticSoftBorderReverseFillPainter
    extends SimplisticFillPainter {
        public static final SubstanceFillPainter INSTANCE = new SimplisticSoftBorderReverseFillPainter();

        private SimplisticSoftBorderReverseFillPainter() {
        }

        @Override
        public String getDisplayName() {
            return "Simplistic Soft Border Reverse";
        }

        @Override
        public Color getTopFillColor(SubstanceColorScheme fillScheme) {
            return super.getBottomFillColor(fillScheme);
        }

        @Override
        public Color getBottomFillColor(SubstanceColorScheme fillScheme) {
            return super.getTopFillColor(fillScheme);
        }
    }
}

