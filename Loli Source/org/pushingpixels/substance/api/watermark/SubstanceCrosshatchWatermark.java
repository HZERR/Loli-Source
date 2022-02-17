/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.watermark;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.watermark.SubstanceWatermark;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;

public class SubstanceCrosshatchWatermark
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
        Color stampColorDark = null;
        Color stampColorAll = null;
        Color stampColorLight = null;
        SubstanceColorScheme scheme = skin.getWatermarkColorScheme();
        if (isPreview) {
            stampColorDark = scheme.isDark() ? Color.white : Color.black;
            stampColorAll = Color.lightGray;
            stampColorLight = scheme.isDark() ? Color.black : Color.white;
        } else {
            stampColorDark = scheme.getWatermarkDarkColor();
            stampColorAll = scheme.getWatermarkStampColor();
            stampColorLight = scheme.getWatermarkLightColor();
        }
        graphics.setColor(stampColorAll);
        graphics.fillRect(0, 0, width, height);
        BufferedImage tile = SubstanceCoreUtilities.getBlankImage(4, 4);
        tile.setRGB(0, 0, stampColorDark.getRGB());
        tile.setRGB(2, 2, stampColorDark.getRGB());
        tile.setRGB(0, 1, stampColorLight.getRGB());
        tile.setRGB(2, 3, stampColorLight.getRGB());
        Graphics2D g2d = (Graphics2D)graphics.create();
        g2d.setComposite(AlphaComposite.getInstance(3, 0.4f));
        for (int row = y2; row < y2 + height; row += 4) {
            for (int col = x2; col < x2 + width; col += 4) {
                g2d.drawImage((Image)tile, col, row, null);
            }
        }
        g2d.dispose();
        return true;
    }

    @Override
    public String getDisplayName() {
        return SubstanceCrosshatchWatermark.getName();
    }

    public static String getName() {
        return "Crosshatch";
    }

    @Override
    public void dispose() {
        watermarkImage = null;
    }
}

