/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.painter.overlay;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.SwingUtilities;
import org.pushingpixels.substance.api.ColorSchemeSingleColorQuery;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.painter.SubstancePainterUtils;
import org.pushingpixels.substance.api.painter.overlay.SubstanceOverlayPainter;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;

public final class TopBezelOverlayPainter
implements SubstanceOverlayPainter {
    ColorSchemeSingleColorQuery colorSchemeQueryTop;
    ColorSchemeSingleColorQuery colorSchemeQueryBottom;

    public TopBezelOverlayPainter(ColorSchemeSingleColorQuery colorSchemeQueryTop, ColorSchemeSingleColorQuery colorSchemeQueryBottom) {
        this.colorSchemeQueryTop = colorSchemeQueryTop;
        this.colorSchemeQueryBottom = colorSchemeQueryBottom;
    }

    @Override
    public void paintOverlay(Graphics2D graphics, Component comp, DecorationAreaType decorationAreaType, int width, int height, SubstanceSkin skin) {
        Component topMostWithSameDecorationAreaType = SubstancePainterUtils.getTopMostParentWithDecorationAreaType(comp, decorationAreaType);
        Point inTopMost = SwingUtilities.convertPoint(comp, new Point(0, 0), topMostWithSameDecorationAreaType);
        int dy = inTopMost.y;
        int fontSize = SubstanceSizeUtils.getComponentFontSize(comp);
        float borderStrokeWidth = SubstanceSizeUtils.getBorderStrokeWidth(fontSize);
        graphics.setStroke(new BasicStroke(borderStrokeWidth));
        SubstanceColorScheme colorScheme = skin.getBackgroundColorScheme(decorationAreaType);
        graphics.setColor(this.colorSchemeQueryTop.query(colorScheme));
        graphics.drawLine(0, (int)borderStrokeWidth - dy - 1, width, (int)borderStrokeWidth - dy - 1);
        graphics.setColor(this.colorSchemeQueryBottom.query(colorScheme));
        graphics.drawLine(0, (int)(2.0f * borderStrokeWidth) - dy - 1, width, (int)(2.0f * borderStrokeWidth) - dy - 1);
    }

    @Override
    public String getDisplayName() {
        return "Top Line Shadow";
    }
}

