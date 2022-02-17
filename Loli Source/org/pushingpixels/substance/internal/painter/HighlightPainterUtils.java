/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.painter;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.EnumSet;
import java.util.Set;
import javax.swing.CellRendererPane;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceConstants;
import org.pushingpixels.substance.api.painter.border.SubstanceBorderPainter;
import org.pushingpixels.substance.api.painter.highlight.SubstanceHighlightPainter;
import org.pushingpixels.substance.internal.utils.HashMapKey;
import org.pushingpixels.substance.internal.utils.LazyResettableHashMap;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;

public class HighlightPainterUtils {
    protected static final LazyResettableHashMap<BufferedImage> smallCache = new LazyResettableHashMap("SubstanceHighlightUtils");

    public static void paintHighlight(Graphics g2, CellRendererPane rendererPane, Component c2, Rectangle rect, float borderAlpha, Set<SubstanceConstants.Side> openSides, SubstanceColorScheme fillScheme, SubstanceColorScheme borderScheme) {
        if (rect.width <= 0 || rect.height <= 0) {
            return;
        }
        Component compForQuerying = rendererPane != null ? rendererPane : c2;
        SubstanceHighlightPainter highlightPainter = SubstanceCoreUtilities.getSkin(compForQuerying).getHighlightPainter();
        SubstanceBorderPainter highlightBorderPainter = SubstanceCoreUtilities.getHighlightBorderPainter(compForQuerying);
        Graphics2D g2d = (Graphics2D)g2.create(rect.x, rect.y, rect.width, rect.height);
        if (openSides == null) {
            openSides = EnumSet.noneOf(SubstanceConstants.Side.class);
        }
        if (rect.width * rect.height < 100000) {
            String openKey = "";
            for (SubstanceConstants.Side oSide : openSides) {
                openKey = openKey + oSide.name() + "-";
            }
            HashMapKey key = SubstanceCoreUtilities.getHashKey(highlightPainter.getDisplayName(), highlightBorderPainter.getDisplayName(), rect.width, rect.height, fillScheme.getDisplayName(), borderScheme.getDisplayName(), Float.valueOf(borderAlpha), openKey);
            BufferedImage result = smallCache.get(key);
            if (result == null) {
                result = HighlightPainterUtils.createHighlighterImage(c2, rect, borderAlpha, openSides, fillScheme, borderScheme, highlightPainter, highlightBorderPainter);
                smallCache.put(key, result);
            }
            g2d.drawImage((Image)result, 0, 0, null);
        }
    }

    private static BufferedImage createHighlighterImage(Component c2, Rectangle rect, float borderAlpha, Set<SubstanceConstants.Side> openSides, SubstanceColorScheme currScheme, SubstanceColorScheme currBorderScheme, SubstanceHighlightPainter highlightPainter, SubstanceBorderPainter highlightBorderPainter) {
        BufferedImage result = SubstanceCoreUtilities.getBlankImage(rect.width, rect.height);
        Graphics2D resGraphics = result.createGraphics();
        highlightPainter.paintHighlight(resGraphics, c2, rect.width, rect.height, currScheme);
        HighlightPainterUtils.paintHighlightBorder(resGraphics, c2, rect.width, rect.height, borderAlpha, openSides, highlightBorderPainter, currBorderScheme);
        resGraphics.dispose();
        return result;
    }

    private static void paintHighlightBorder(Graphics2D graphics, Component comp, int width, int height, float borderAlpha, Set<SubstanceConstants.Side> openSides, SubstanceBorderPainter highlightBorderPainter, SubstanceColorScheme borderColorScheme) {
        if (borderAlpha <= 0.0f) {
            return;
        }
        int openDelta = 3 + (int)Math.ceil(3.0 * (double)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(comp)));
        int deltaLeft = openSides.contains((Object)SubstanceConstants.Side.LEFT) ? openDelta : 0;
        int deltaRight = openSides.contains((Object)SubstanceConstants.Side.RIGHT) ? openDelta : 0;
        int deltaTop = openSides.contains((Object)SubstanceConstants.Side.TOP) ? openDelta : 0;
        int deltaBottom = openSides.contains((Object)SubstanceConstants.Side.BOTTOM) ? openDelta : 0;
        int borderDelta = (int)Math.floor((double)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(comp)) / 2.0);
        Rectangle contour = new Rectangle(borderDelta, borderDelta, width + deltaLeft + deltaRight - 2 * borderDelta - 1, height + deltaTop + deltaBottom - 2 * borderDelta - 1);
        Graphics2D g2d = (Graphics2D)graphics.create();
        g2d.translate(-deltaLeft, -deltaTop);
        g2d.setComposite(LafWidgetUtilities.getAlphaComposite(null, borderAlpha, graphics));
        int borderThickness = (int)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(comp));
        Rectangle contourInner = new Rectangle(borderDelta + borderThickness, borderDelta + borderThickness, width + deltaLeft + deltaRight - 2 * borderDelta - 2 * borderThickness - 1, height + deltaTop + deltaBottom - 2 * borderDelta - 2 * borderThickness - 1);
        highlightBorderPainter.paintBorder(g2d, comp, width + deltaLeft + deltaRight, height + deltaTop + deltaBottom, contour, contourInner, borderColorScheme);
        g2d.dispose();
    }

    public static String getMemoryUsage() {
        StringBuffer sb = new StringBuffer();
        sb.append("SubstanceHighlightUtils: \n");
        sb.append("\t" + smallCache.size() + " smalls");
        return sb.toString();
    }
}

