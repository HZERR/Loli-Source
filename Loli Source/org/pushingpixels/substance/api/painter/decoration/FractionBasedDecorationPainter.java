/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.painter.decoration;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
import javax.swing.SwingUtilities;
import org.pushingpixels.substance.api.ColorSchemeSingleColorQuery;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.painter.FractionBasedPainter;
import org.pushingpixels.substance.api.painter.SubstancePainterUtils;
import org.pushingpixels.substance.api.painter.decoration.SubstanceDecorationPainter;

public class FractionBasedDecorationPainter
extends FractionBasedPainter
implements SubstanceDecorationPainter {
    private Set<DecorationAreaType> decoratedAreas = new HashSet<DecorationAreaType>();

    public FractionBasedDecorationPainter(String displayName, float[] fractions, ColorSchemeSingleColorQuery[] colorQueries) {
        this(displayName, fractions, colorQueries, DecorationAreaType.PRIMARY_TITLE_PANE, DecorationAreaType.SECONDARY_TITLE_PANE);
    }

    public FractionBasedDecorationPainter(String displayName, float[] fractions, ColorSchemeSingleColorQuery[] colorQueries, DecorationAreaType ... decorationAreas) {
        super(displayName, fractions, colorQueries);
        if (decorationAreas != null) {
            for (DecorationAreaType decorationArea : decorationAreas) {
                this.decoratedAreas.add(decorationArea);
            }
        }
    }

    @Override
    public void paintDecorationArea(Graphics2D graphics, Component comp, DecorationAreaType decorationAreaType, int width, int height, SubstanceSkin skin) {
        if (this.decoratedAreas.contains(decorationAreaType)) {
            this.paintDecoratedBackground(graphics, comp, decorationAreaType, width, height, skin.getBackgroundColorScheme(decorationAreaType));
        } else {
            this.paintSolidBackground(graphics, comp, width, height, skin.getBackgroundColorScheme(decorationAreaType));
        }
    }

    private void paintDecoratedBackground(Graphics2D graphics, Component comp, DecorationAreaType decorationAreaType, int width, int height, SubstanceColorScheme scheme) {
        Graphics2D g2d = (Graphics2D)graphics.create();
        Color[] fillColors = new Color[this.fractions.length];
        for (int i2 = 0; i2 < this.fractions.length; ++i2) {
            ColorSchemeSingleColorQuery colorQuery = this.colorQueries[i2];
            fillColors[i2] = colorQuery.query(scheme);
        }
        Component topMostWithSameDecorationAreaType = SubstancePainterUtils.getTopMostParentWithDecorationAreaType(comp, decorationAreaType);
        Point inTopMost = SwingUtilities.convertPoint(comp, new Point(0, 0), topMostWithSameDecorationAreaType);
        int dy = inTopMost.y;
        LinearGradientPaint gradient = new LinearGradientPaint(0.0f, 0.0f, 0.0f, topMostWithSameDecorationAreaType.getHeight(), this.fractions, fillColors, MultipleGradientPaint.CycleMethod.REPEAT);
        g2d.setPaint(gradient);
        g2d.translate(0, -dy);
        g2d.fillRect(0, 0, width, topMostWithSameDecorationAreaType.getHeight());
        g2d.dispose();
    }

    private void paintSolidBackground(Graphics2D graphics, Component comp, int width, int height, SubstanceColorScheme scheme) {
        graphics.setColor(scheme.getMidColor());
        graphics.fillRect(0, 0, width, height);
    }
}

