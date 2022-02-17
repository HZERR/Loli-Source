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
import org.pushingpixels.substance.api.ColorSchemeTransform;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.painter.border.FractionBasedBorderPainter;
import org.pushingpixels.substance.api.painter.border.SubstanceBorderPainter;
import org.pushingpixels.substance.internal.utils.HashMapKey;
import org.pushingpixels.substance.internal.utils.LazyResettableHashMap;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceInternalArrowButton;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;

public class DelegateFractionBasedBorderPainter
implements SubstanceBorderPainter {
    protected String displayName;
    protected FractionBasedBorderPainter delegate;
    protected int[] masks;
    protected ColorSchemeTransform transform;
    protected static final LazyResettableHashMap<SubstanceColorScheme> transformMap = new LazyResettableHashMap("DelegateBorderPainter");

    public DelegateFractionBasedBorderPainter(String displayName, FractionBasedBorderPainter delegate, int[] masks, ColorSchemeTransform transform) {
        this.displayName = displayName;
        this.delegate = delegate;
        this.masks = new int[masks.length];
        System.arraycopy(masks, 0, this.masks, 0, masks.length);
        this.transform = transform;
    }

    @Override
    public boolean isPaintingInnerContour() {
        return false;
    }

    @Override
    public void paintBorder(Graphics g2, Component c2, int width, int height, Shape contour, Shape innerContour, SubstanceColorScheme borderScheme) {
        Graphics2D graphics = (Graphics2D)g2.create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        SubstanceColorScheme scheme = this.getShiftScheme(borderScheme);
        float[] fractions = this.delegate.getFractions();
        ColorSchemeSingleColorQuery[] colorQueries = this.delegate.getColorQueries();
        Color[] fillColors = new Color[fractions.length];
        for (int i2 = 0; i2 < fractions.length; ++i2) {
            ColorSchemeSingleColorQuery colorQuery = colorQueries[i2];
            Color color = colorQuery.query(scheme);
            fillColors[i2] = color = new Color(this.masks[i2] & color.getRGB(), true);
        }
        float strokeWidth = SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(c2));
        boolean isSpecialButton = c2 instanceof SubstanceInternalArrowButton;
        int joinKind = isSpecialButton ? 0 : 1;
        int capKind = isSpecialButton ? 2 : 0;
        graphics.setStroke(new BasicStroke(strokeWidth, capKind, joinKind));
        LinearGradientPaint gradient = new LinearGradientPaint(0.0f, 0.0f, 0.0f, height, fractions, fillColors, MultipleGradientPaint.CycleMethod.REPEAT);
        graphics.setPaint(gradient);
        graphics.draw(contour);
        graphics.dispose();
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    private SubstanceColorScheme getShiftScheme(SubstanceColorScheme orig) {
        HashMapKey key = SubstanceCoreUtilities.getHashKey(orig.getDisplayName(), this.getDisplayName(), this.transform);
        SubstanceColorScheme result = transformMap.get(key);
        if (result == null) {
            result = this.transform.transform(orig);
            transformMap.put(key, result);
        }
        return result;
    }
}

