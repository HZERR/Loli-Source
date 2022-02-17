/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.painter.decoration;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.Map;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.painter.SubstancePainterUtils;
import org.pushingpixels.substance.api.painter.decoration.SubstanceDecorationPainter;
import org.pushingpixels.substance.internal.utils.SubstanceImageCreator;

public abstract class ImageWrapperDecorationPainter
implements SubstanceDecorationPainter {
    protected Image originalTile = null;
    protected SubstanceDecorationPainter baseDecorationPainter;
    protected LinkedHashMap<String, Image> colorizedTileMap = new LinkedHashMap<String, Image>(){

        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Image> eldest) {
            return this.size() > 10;
        }
    };
    protected float textureAlpha = 0.3f;

    @Override
    public void paintDecorationArea(Graphics2D graphics, Component comp, DecorationAreaType decorationAreaType, int width, int height, SubstanceSkin skin) {
        if (decorationAreaType == DecorationAreaType.PRIMARY_TITLE_PANE || decorationAreaType == DecorationAreaType.SECONDARY_TITLE_PANE) {
            this.paintTitleBackground(graphics, comp, decorationAreaType, width, height, skin);
        } else {
            this.paintExtraBackground(graphics, comp, decorationAreaType, width, height, skin);
        }
    }

    private void paintTitleBackground(Graphics2D graphics, Component comp, DecorationAreaType decorationAreaType, int width, int height, SubstanceSkin skin) {
        SubstanceColorScheme tileScheme = skin.getBackgroundColorScheme(decorationAreaType);
        if (this.baseDecorationPainter == null) {
            graphics.setColor(tileScheme.getMidColor());
            graphics.fillRect(0, 0, width, height);
        } else {
            this.baseDecorationPainter.paintDecorationArea(graphics, comp, decorationAreaType, width, height, skin);
        }
        Graphics2D temp = (Graphics2D)graphics.create();
        this.tileArea(temp, comp, tileScheme, 0, 0, 0, 0, width, height);
        temp.dispose();
    }

    private void paintExtraBackground(Graphics2D graphics, Component comp, DecorationAreaType decorationAreaType, int width, int height, SubstanceSkin skin) {
        Point offset = SubstancePainterUtils.getOffsetInRootPaneCoords(comp);
        SubstanceColorScheme tileScheme = skin.getBackgroundColorScheme(decorationAreaType);
        if (this.baseDecorationPainter != null) {
            this.baseDecorationPainter.paintDecorationArea(graphics, comp, decorationAreaType, width, height, skin);
        } else {
            graphics.setColor(tileScheme.getMidColor());
            graphics.fillRect(0, 0, width, height);
        }
        Graphics2D temp = (Graphics2D)graphics.create();
        this.tileArea(temp, comp, tileScheme, offset.x, offset.y, 0, 0, width, height);
        temp.dispose();
    }

    protected void tileArea(Graphics2D g2, Component comp, SubstanceColorScheme tileScheme, int offsetTextureX, int offsetTextureY, int x2, int y2, int width, int height) {
        Graphics2D graphics = (Graphics2D)g2.create();
        graphics.setComposite(LafWidgetUtilities.getAlphaComposite(comp, this.textureAlpha, g2));
        Image colorizedTile = this.getColorizedTile(tileScheme);
        int tileWidth = colorizedTile.getWidth(null);
        int tileHeight = colorizedTile.getHeight(null);
        int currTileTop = -(offsetTextureY %= tileHeight);
        do {
            int currTileLeft = -(offsetTextureX %= tileWidth);
            do {
                graphics.drawImage(colorizedTile, currTileLeft, currTileTop, null);
            } while ((currTileLeft += tileWidth) < width);
        } while ((currTileTop += tileHeight) < height);
        graphics.dispose();
    }

    public void setBaseDecorationPainter(SubstanceDecorationPainter baseDecorationPainter) {
        this.baseDecorationPainter = baseDecorationPainter;
    }

    public void setTextureAlpha(float textureAlpha) {
        this.textureAlpha = textureAlpha;
    }

    protected Image getColorizedTile(SubstanceColorScheme scheme) {
        Image result = this.colorizedTileMap.get(scheme.getDisplayName());
        if (result == null) {
            BufferedImage tileBi = new BufferedImage(this.originalTile.getWidth(null), this.originalTile.getHeight(null), 2);
            tileBi.getGraphics().drawImage(this.originalTile, 0, 0, null);
            result = SubstanceImageCreator.getColorSchemeImage(tileBi, scheme, 0.0f);
            this.colorizedTileMap.put(scheme.getDisplayName(), result);
        }
        return result;
    }
}

