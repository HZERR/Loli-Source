/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.util;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.FilteredImageSource;
import java.awt.image.PixelGrabber;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import javax.swing.ImageIcon;

public class Images {
    private static GraphiteFilter graphiteFilter = new GraphiteFilter();
    private static Properties canGraphite;

    private Images() {
    }

    public static Image createImage(URL resource) {
        Image image = Toolkit.getDefaultToolkit().createImage(resource);
        return image;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private static boolean canGraphite(URL resource) {
        String file;
        int i2;
        if (canGraphite == null) {
            Class<Images> class_ = Images.class;
            // MONITORENTER : org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.util.Images.class
            if (canGraphite == null) {
                Properties p2 = new Properties();
                try {
                    p2.load(Images.class.getResourceAsStream("graphiteable.properties"));
                }
                catch (IOException e2) {
                    System.err.println("Failed to load graphiteable.properties: " + e2);
                }
                canGraphite = p2;
            }
            // MONITOREXIT : class_
        }
        if ((i2 = (file = resource.getFile()).lastIndexOf(File.separatorChar)) == -1) return canGraphite.containsKey(file);
        file = file.substring(i2 + 1);
        return canGraphite.containsKey(file);
    }

    private static Image toGraphite(Image image) {
        return Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), graphiteFilter));
    }

    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage)image;
        }
        image = new ImageIcon(image).getImage();
        BufferedImage bimage = null;
        if (System.getProperty("java.version").startsWith("1.4.1_")) {
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), 2);
        } else {
            boolean hasAlpha;
            try {
                hasAlpha = Images.hasAlpha(image);
            }
            catch (IllegalAccessError e2) {
                hasAlpha = true;
            }
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            try {
                int transparency = 1;
                if (hasAlpha) {
                    transparency = 3;
                }
                GraphicsDevice gs = ge.getDefaultScreenDevice();
                GraphicsConfiguration gc = gs.getDefaultConfiguration();
                bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
            }
            catch (Exception e3) {
                // empty catch block
            }
            if (bimage == null) {
                int type = 1;
                if (hasAlpha) {
                    type = 2;
                }
                bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
            }
        }
        Graphics2D g2 = bimage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return bimage;
    }

    public static boolean hasAlpha(Image image) {
        if (image instanceof BufferedImage) {
            BufferedImage bimage = (BufferedImage)image;
            return bimage.getColorModel().hasAlpha();
        }
        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        }
        catch (InterruptedException e2) {
            // empty catch block
        }
        ColorModel cm = pg.getColorModel();
        return cm.hasAlpha();
    }

    public static BufferedImage[] split(Image image, int count, boolean isHorizontal) {
        BufferedImage src = Images.toBufferedImage(image);
        if (count == 1) {
            return new BufferedImage[]{src};
        }
        BufferedImage[] parts = new BufferedImage[count];
        for (int i2 = 0; i2 < count; ++i2) {
            parts[i2] = isHorizontal ? src.getSubimage(src.getWidth() / count * i2, 0, src.getWidth() / count, src.getHeight()) : src.getSubimage(0, src.getHeight() / count * i2, src.getWidth(), src.getHeight() / count);
        }
        return parts;
    }

    public static class GraphiteFilter
    extends RGBImageFilter {
        private static final float saturationAdjust = 0.179f;
        private static float hueAdjust = 0.0052f;
        private static float brightnessAdjust = 0.09f;
        private float[] hsb = new float[3];

        @Override
        public int filterRGB(int x2, int y2, int rgb) {
            int alpha = rgb & 0xFF000000;
            int red = rgb >> 16 & 0xFF;
            int green = rgb >> 8 & 0xFF;
            int blue = rgb & 0xFF;
            float RW = 0.273393f;
            float RG = 0.273393f;
            float RB = 0.273393f;
            float a2 = RW + 0.179f;
            float b2 = RW;
            float c2 = RW;
            float d2 = RG;
            float e2 = RG + 0.179f;
            float f2 = RG;
            float g2 = RB;
            float h2 = RB;
            float i2 = RB + 0.179f;
            int outputRed = (int)(a2 * (float)red + d2 * (float)green + g2 * (float)blue);
            int outputGreen = (int)(b2 * (float)red + e2 * (float)green + h2 * (float)blue);
            int outputBlue = (int)(c2 * (float)red + f2 * (float)green + i2 * (float)blue);
            return alpha | outputRed << 16 | outputGreen << 8 | outputBlue;
        }
    }
}

