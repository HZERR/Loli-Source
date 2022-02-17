/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils.border;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import javax.swing.border.Border;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;

public class SubstanceEtchedBorder
implements Border {
    public Color getHighlightColor(Component c2) {
        SubstanceColorScheme colorScheme = SubstanceColorSchemeUtilities.getColorScheme(c2, ColorSchemeAssociationKind.SEPARATOR, ComponentState.ENABLED);
        boolean isDark = colorScheme.isDark();
        Color foreDark = isDark ? colorScheme.getExtraLightColor() : SubstanceColorUtilities.getInterpolatedColor(colorScheme.getMidColor(), colorScheme.getDarkColor(), 0.4);
        return SubstanceColorUtilities.getAlphaColor(foreDark, 196);
    }

    public Color getShadowColor(Component c2) {
        SubstanceColorScheme colorScheme = SubstanceColorSchemeUtilities.getColorScheme(c2, ColorSchemeAssociationKind.SEPARATOR, ComponentState.ENABLED);
        Color back = colorScheme.isDark() ? colorScheme.getDarkColor() : colorScheme.getUltraLightColor();
        return SubstanceColorUtilities.getAlphaColor(back, 196);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    @Override
    public void paintBorder(Component c2, Graphics g2, int x2, int y2, int width, int height) {
        int w2 = width;
        int h2 = height;
        Graphics2D g2d = (Graphics2D)g2.create();
        float strokeWidth = SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(c2));
        g2d.setStroke(new BasicStroke(strokeWidth, 0, 1));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.translate(x2, y2);
        g2d.setColor(this.getShadowColor(c2));
        int delta = (int)Math.floor((double)strokeWidth / 2.0);
        g2d.draw(new Rectangle2D.Float(delta, delta, (float)(w2 - delta) - 2.0f * strokeWidth, (float)(h2 - delta) - 2.0f * strokeWidth));
        g2d.setColor(this.getHighlightColor(c2));
        g2d.draw(new Line2D.Float(strokeWidth, (float)h2 - 3.0f * strokeWidth, strokeWidth, strokeWidth));
        g2d.draw(new Line2D.Float((float)delta + strokeWidth, (float)delta + strokeWidth, (float)(w2 - delta) - 3.0f * strokeWidth, (float)delta + strokeWidth));
        g2d.draw(new Line2D.Float(delta, (float)(h2 - delta) - strokeWidth, (float)(w2 - delta) - strokeWidth, (float)(h2 - delta) - strokeWidth));
        g2d.draw(new Line2D.Float((float)(w2 - delta) - strokeWidth, (float)(h2 - delta) - strokeWidth, (float)(w2 - delta) - strokeWidth, delta));
        g2d.dispose();
        RenderingUtils.installDesktopHints((Graphics2D)g2, c2);
    }

    @Override
    public Insets getBorderInsets(Component c2) {
        float borderStrokeWidth = SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(c2));
        int prefSize = (int)Math.ceil(2.0 * (double)borderStrokeWidth);
        return new Insets(prefSize, prefSize, prefSize, prefSize);
    }
}

