/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.painter.decoration;

import java.awt.Component;
import java.awt.Graphics2D;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.painter.decoration.SubstanceDecorationPainter;

public class FlatDecorationPainter
implements SubstanceDecorationPainter {
    public static final String DISPLAY_NAME = "Flat";

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public void paintDecorationArea(Graphics2D graphics, Component comp, DecorationAreaType decorationAreaType, int width, int height, SubstanceSkin skin) {
        graphics.setColor(skin.getBackgroundColorScheme(DecorationAreaType.PRIMARY_TITLE_PANE).getBackgroundFillColor());
        graphics.fillRect(0, 0, width, height);
    }
}

