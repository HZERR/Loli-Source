/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.painter.fill;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.Shape;
import org.pushingpixels.substance.api.ColorSchemeSingleColorQuery;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.painter.FractionBasedPainter;
import org.pushingpixels.substance.api.painter.fill.SubstanceFillPainter;

public class FractionBasedFillPainter
extends FractionBasedPainter
implements SubstanceFillPainter {
    public FractionBasedFillPainter(String displayName, float[] fractions, ColorSchemeSingleColorQuery[] colorQueries) {
        super(displayName, fractions, colorQueries);
    }

    @Override
    public void paintContourBackground(Graphics g2, Component comp, int width, int height, Shape contour, boolean isFocused, SubstanceColorScheme fillScheme, boolean hasShine) {
        Graphics2D graphics = (Graphics2D)g2.create();
        Color[] fillColors = new Color[this.fractions.length];
        for (int i2 = 0; i2 < this.fractions.length; ++i2) {
            ColorSchemeSingleColorQuery colorQuery = this.colorQueries[i2];
            fillColors[i2] = colorQuery.query(fillScheme);
        }
        LinearGradientPaint gradient = new LinearGradientPaint(0.0f, 0.0f, 0.0f, height, this.fractions, fillColors, MultipleGradientPaint.CycleMethod.REPEAT);
        graphics.setPaint(gradient);
        graphics.fill(contour);
        graphics.dispose();
    }
}

