/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.watermark;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.watermark.SubstanceWatermark;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;

public class SubstanceNullWatermark
implements SubstanceWatermark {
    @Override
    public void drawWatermarkImage(Graphics graphics, Component c2, int x2, int y2, int width, int height) {
        SubstanceSkin skin = SubstanceCoreUtilities.getSkin(c2);
        Graphics2D g2d = (Graphics2D)graphics.create();
        g2d.setComposite(LafWidgetUtilities.getAlphaComposite(c2, 0.2f, graphics));
        g2d.setColor(skin.getWatermarkColorScheme().getWatermarkLightColor());
        g2d.fillRect(x2, y2, width, height);
        g2d.dispose();
    }

    @Override
    public void previewWatermark(Graphics g2, SubstanceSkin skin, int x2, int y2, int width, int height) {
    }

    @Override
    public boolean updateWatermarkImage(SubstanceSkin skin) {
        return true;
    }

    @Override
    public String getDisplayName() {
        return "Null";
    }

    @Override
    public void dispose() {
    }
}

