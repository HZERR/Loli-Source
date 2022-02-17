/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.watermark;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.watermark.SubstanceWatermark;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;

public class SubstanceStripeWatermark
implements SubstanceWatermark {
    private static Image watermarkImage = null;

    @Override
    public void drawWatermarkImage(Graphics graphics, Component c2, int x2, int y2, int width, int height) {
        if (!c2.isShowing()) {
            return;
        }
        int dx = c2.getLocationOnScreen().x;
        int dy = c2.getLocationOnScreen().y;
        graphics.drawImage(watermarkImage, x2, y2, x2 + width, y2 + height, x2 + dx, y2 + dy, x2 + dx + width, y2 + dy + height, null);
    }

    @Override
    public boolean updateWatermarkImage(SubstanceSkin skin) {
        GraphicsDevice[] gds;
        Rectangle virtualBounds = new Rectangle();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        for (GraphicsDevice gd : gds = ge.getScreenDevices()) {
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            virtualBounds = virtualBounds.union(gc.getBounds());
        }
        int screenWidth = virtualBounds.width;
        int screenHeight = virtualBounds.height;
        watermarkImage = SubstanceCoreUtilities.getBlankImage(screenWidth, screenHeight);
        Graphics2D graphics = (Graphics2D)watermarkImage.getGraphics().create();
        boolean status = this.drawWatermarkImage(skin, graphics, 0, 0, screenWidth, screenHeight, false);
        graphics.dispose();
        return status;
    }

    @Override
    public void previewWatermark(Graphics g2, SubstanceSkin skin, int x2, int y2, int width, int height) {
        this.drawWatermarkImage(skin, (Graphics2D)g2, x2, y2, width, height, true);
    }

    private boolean drawWatermarkImage(SubstanceSkin skin, Graphics2D graphics, int x2, int y2, int width, int height, boolean isPreview) {
        int row;
        Color stampColor = null;
        SubstanceColorScheme scheme = skin.getWatermarkColorScheme();
        stampColor = isPreview ? (scheme.isDark() ? Color.lightGray : Color.darkGray) : scheme.getWatermarkStampColor();
        graphics.setColor(stampColor);
        for (row = y2; row < y2 + height; row += 2) {
            graphics.drawLine(x2, row, x2 + width, row);
        }
        if (isPreview) {
            graphics.setColor(Color.gray);
            for (row = y2 + 1; row < y2 + height; row += 2) {
                graphics.drawLine(x2, row, x2 + width, row);
            }
        }
        return true;
    }

    @Override
    public String getDisplayName() {
        return "Stripes";
    }

    @Override
    public void dispose() {
        watermarkImage = null;
    }
}

