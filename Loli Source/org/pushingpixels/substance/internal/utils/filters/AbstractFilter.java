/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils.filters;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public abstract class AbstractFilter
implements BufferedImageOp {
    @Override
    public abstract BufferedImage filter(BufferedImage var1, BufferedImage var2);

    @Override
    public Rectangle2D getBounds2D(BufferedImage src) {
        return new Rectangle(0, 0, src.getWidth(), src.getHeight());
    }

    @Override
    public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) {
        if (destCM == null) {
            destCM = src.getColorModel();
        }
        return new BufferedImage(destCM, destCM.createCompatibleWritableRaster(src.getWidth(), src.getHeight()), destCM.isAlphaPremultiplied(), null);
    }

    @Override
    public Point2D getPoint2D(Point2D srcPt, Point2D dstPt) {
        return (Point2D)srcPt.clone();
    }

    @Override
    public RenderingHints getRenderingHints() {
        return null;
    }

    protected int[] getPixels(BufferedImage img, int x2, int y2, int w2, int h2, int[] pixels) {
        if (w2 == 0 || h2 == 0) {
            return new int[0];
        }
        if (pixels == null) {
            pixels = new int[w2 * h2];
        } else if (pixels.length < w2 * h2) {
            throw new IllegalArgumentException("pixels array must have a length >= w*h");
        }
        int imageType = img.getType();
        if (imageType == 2 || imageType == 1) {
            WritableRaster raster = img.getRaster();
            return (int[])raster.getDataElements(x2, y2, w2, h2, pixels);
        }
        return img.getRGB(x2, y2, w2, h2, pixels, 0, w2);
    }

    protected void setPixels(BufferedImage img, int x2, int y2, int w2, int h2, int[] pixels) {
        if (pixels == null || w2 == 0 || h2 == 0) {
            return;
        }
        if (pixels.length < w2 * h2) {
            throw new IllegalArgumentException("pixels array must have a length >= w*h");
        }
        int imageType = img.getType();
        if (imageType == 2 || imageType == 1) {
            WritableRaster raster = img.getRaster();
            raster.setDataElements(x2, y2, w2, h2, pixels);
        } else {
            img.setRGB(x2, y2, w2, h2, pixels, 0, w2);
        }
    }
}

