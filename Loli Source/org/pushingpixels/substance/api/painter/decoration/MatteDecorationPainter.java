/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.painter.decoration;

import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.painter.SubstancePainterUtils;
import org.pushingpixels.substance.api.painter.decoration.SubstanceDecorationPainter;

public class MatteDecorationPainter
implements SubstanceDecorationPainter {
    public static final String DISPLAY_NAME = "Matte";

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public void paintDecorationArea(Graphics2D graphics, Component comp, DecorationAreaType decorationAreaType, int width, int height, SubstanceSkin skin) {
        if (decorationAreaType == DecorationAreaType.PRIMARY_TITLE_PANE || decorationAreaType == DecorationAreaType.SECONDARY_TITLE_PANE) {
            this.paintTitleBackground(graphics, comp, width, height, skin.getBackgroundColorScheme(decorationAreaType));
        } else {
            this.paintExtraBackground(graphics, comp, width, height, skin.getBackgroundColorScheme(decorationAreaType));
        }
    }

    private void paintTitleBackground(Graphics2D graphics, Component comp, int width, int height, SubstanceColorScheme scheme) {
        Graphics2D temp = (Graphics2D)graphics.create();
        this.fill(temp, comp, scheme, 0, 0, 0, width, height);
        temp.dispose();
    }

    private void paintExtraBackground(Graphics2D graphics, Component comp, int width, int height, SubstanceColorScheme scheme) {
        Point offset = SubstancePainterUtils.getOffsetInRootPaneCoords(comp);
        Graphics2D temp = (Graphics2D)graphics.create();
        this.fill(temp, comp, scheme, offset.y, 0, 0, width, height);
        temp.dispose();
    }

    protected void fill(Graphics2D graphics, Component comp, SubstanceColorScheme scheme, int offsetY, int x2, int y2, int width, int height) {
        int flexPoint = 50;
        int startY = y2 + offsetY;
        if (startY < 0) {
            startY = 0;
        }
        int endY = startY + height;
        int currStart = 0;
        if (flexPoint >= startY) {
            graphics.setPaint(new GradientPaint(x2, currStart - offsetY, scheme.getLightColor(), x2, flexPoint - offsetY, scheme.getMidColor()));
            graphics.fillRect(x2, currStart - offsetY, width, flexPoint);
        }
        if ((currStart += flexPoint) > endY) {
            return;
        }
        graphics.setColor(scheme.getMidColor());
        graphics.fillRect(x2, currStart - offsetY, width, endY - currStart + offsetY);
    }
}

