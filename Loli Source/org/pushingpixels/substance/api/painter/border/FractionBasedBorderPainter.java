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
import org.pushingpixels.substance.api.ColorSchemeSingleColorQuery;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.painter.FractionBasedPainter;
import org.pushingpixels.substance.api.painter.border.SubstanceBorderPainter;
import org.pushingpixels.substance.internal.utils.SubstanceInternalArrowButton;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;

public class FractionBasedBorderPainter
extends FractionBasedPainter
implements SubstanceBorderPainter {
    public FractionBasedBorderPainter(String displayName, float[] fractions, ColorSchemeSingleColorQuery[] colorQueries) {
        super(displayName, fractions, colorQueries);
    }

    @Override
    public void paintBorder(Graphics g2, Component c2, int width, int height, Shape contour, Shape innerContour, SubstanceColorScheme borderScheme) {
        if (contour == null) {
            return;
        }
        Graphics2D graphics = (Graphics2D)g2.create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        Color[] drawColors = new Color[this.fractions.length];
        for (int i2 = 0; i2 < this.fractions.length; ++i2) {
            ColorSchemeSingleColorQuery colorQuery = this.colorQueries[i2];
            drawColors[i2] = colorQuery.query(borderScheme);
        }
        float strokeWidth = SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(c2));
        boolean isSpecialButton = c2 instanceof SubstanceInternalArrowButton;
        int joinKind = isSpecialButton ? 0 : 1;
        int capKind = isSpecialButton ? 2 : 0;
        graphics.setStroke(new BasicStroke(strokeWidth, capKind, joinKind));
        LinearGradientPaint gradient = new LinearGradientPaint(0.0f, 0.0f, 0.0f, height, this.fractions, drawColors, MultipleGradientPaint.CycleMethod.REPEAT);
        graphics.setPaint(gradient);
        graphics.draw(contour);
        graphics.dispose();
    }

    @Override
    public boolean isPaintingInnerContour() {
        return false;
    }
}

