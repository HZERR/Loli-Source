/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.painter.highlight;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import org.pushingpixels.substance.api.ColorSchemeSingleColorQuery;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.painter.FractionBasedPainter;
import org.pushingpixels.substance.api.painter.highlight.SubstanceHighlightPainter;

public class FractionBasedHighlightPainter
extends FractionBasedPainter
implements SubstanceHighlightPainter {
    public FractionBasedHighlightPainter(String displayName, float[] fractions, ColorSchemeSingleColorQuery[] colorQueries) {
        super(displayName, fractions, colorQueries);
    }

    @Override
    public void paintHighlight(Graphics2D graphics, Component comp, int width, int height, SubstanceColorScheme colorScheme) {
        Graphics2D g2d = (Graphics2D)graphics.create();
        Color[] fillColors = new Color[this.fractions.length];
        for (int i2 = 0; i2 < this.fractions.length; ++i2) {
            fillColors[i2] = this.colorQueries[i2].query(colorScheme);
        }
        LinearGradientPaint gradient = new LinearGradientPaint(0.0f, 0.0f, 0.0f, height, this.fractions, fillColors, MultipleGradientPaint.CycleMethod.REPEAT);
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();
    }
}

