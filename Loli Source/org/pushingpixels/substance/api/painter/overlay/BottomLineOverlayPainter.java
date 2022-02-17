/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.painter.overlay;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Graphics2D;
import org.pushingpixels.substance.api.ColorSchemeSingleColorQuery;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.painter.SubstancePainterUtils;
import org.pushingpixels.substance.api.painter.overlay.SubstanceOverlayPainter;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;

public final class BottomLineOverlayPainter
implements SubstanceOverlayPainter {
    ColorSchemeSingleColorQuery colorSchemeQuery;

    public BottomLineOverlayPainter(ColorSchemeSingleColorQuery colorSchemeQuery) {
        this.colorSchemeQuery = colorSchemeQuery;
    }

    @Override
    public void paintOverlay(Graphics2D graphics, Component comp, DecorationAreaType decorationAreaType, int width, int height, SubstanceSkin skin) {
        Component topMostWithSameDecorationAreaType = SubstancePainterUtils.getTopMostParentWithDecorationAreaType(comp, decorationAreaType);
        int fontSize = SubstanceSizeUtils.getComponentFontSize(comp);
        float borderStrokeWidth = SubstanceSizeUtils.getBorderStrokeWidth(fontSize);
        graphics.setStroke(new BasicStroke(borderStrokeWidth));
        SubstanceColorScheme colorScheme = skin.getBackgroundColorScheme(decorationAreaType);
        graphics.setColor(this.colorSchemeQuery.query(colorScheme));
        graphics.drawLine(0, topMostWithSameDecorationAreaType.getHeight() - (int)borderStrokeWidth, width, topMostWithSameDecorationAreaType.getHeight() - (int)borderStrokeWidth);
    }

    @Override
    public String getDisplayName() {
        return "Bottom Line";
    }
}

