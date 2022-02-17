/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.painter.decoration;

import java.awt.Component;
import java.awt.Graphics2D;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.painter.decoration.SubstanceDecorationPainter;
import org.pushingpixels.substance.internal.utils.SubstanceImageCreator;

public class Glass3DDecorationPainter
implements SubstanceDecorationPainter {
    public static final String DISPLAY_NAME = "Glass 3D";

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public void paintDecorationArea(Graphics2D graphics, Component comp, DecorationAreaType decorationAreaType, int width, int height, SubstanceSkin skin) {
        SubstanceImageCreator.paintRectangularBackground(comp, graphics, 0, 0, width, height, skin.getBackgroundColorScheme(decorationAreaType), 0.0f, false);
    }
}

