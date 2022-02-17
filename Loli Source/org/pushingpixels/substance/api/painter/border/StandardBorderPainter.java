/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.painter.border;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.painter.border.SubstanceBorderPainter;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceInternalArrowButton;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;

public class StandardBorderPainter
implements SubstanceBorderPainter {
    @Override
    public String getDisplayName() {
        return "Standard";
    }

    @Override
    public boolean isPaintingInnerContour() {
        return false;
    }

    @Override
    public void paintBorder(Graphics g2, Component c2, int width, int height, Shape contour, Shape innerContour, SubstanceColorScheme borderScheme) {
        if (contour == null) {
            return;
        }
        Graphics2D graphics = (Graphics2D)g2.create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        Color topBorderColor = this.getTopBorderColor(borderScheme);
        Color midBorderColor = this.getMidBorderColor(borderScheme);
        Color bottomBorderColor = this.getBottomBorderColor(borderScheme);
        if (topBorderColor != null && midBorderColor != null && bottomBorderColor != null) {
            float strokeWidth = SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(c2));
            boolean isSpecialButton = c2 instanceof SubstanceInternalArrowButton;
            int joinKind = isSpecialButton ? 0 : 1;
            int capKind = isSpecialButton ? 2 : 0;
            graphics.setStroke(new BasicStroke(strokeWidth, capKind, joinKind));
            LinearGradientPaint gradient = new LinearGradientPaint(0.0f, 0.0f, 0.0f, height, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{topBorderColor, midBorderColor, bottomBorderColor}, MultipleGradientPaint.CycleMethod.REPEAT);
            graphics.setPaint(gradient);
            graphics.draw(contour);
        }
        graphics.dispose();
    }

    public Color getTopBorderColor(SubstanceColorScheme borderScheme) {
        return SubstanceColorUtilities.getTopBorderColor(borderScheme);
    }

    public Color getMidBorderColor(SubstanceColorScheme borderScheme) {
        return SubstanceColorUtilities.getMidBorderColor(borderScheme);
    }

    public Color getBottomBorderColor(SubstanceColorScheme borderScheme) {
        return SubstanceColorUtilities.getBottomBorderColor(borderScheme);
    }
}

