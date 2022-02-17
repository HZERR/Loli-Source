/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j;

import com.gif4j.GifImage;
import com.gif4j.Watermark;
import com.gif4j.q;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.DirectColorModel;
import java.awt.image.ImagingOpException;
import java.awt.image.IndexColorModel;
import java.awt.image.PixelGrabber;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import sun.awt.image.BytePackedRaster;

public class ImageUtils {
    private static final Component a = new q();
    private static final MediaTracker b = new MediaTracker(a);
    private static int c = 0;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static final void a(Image image) throws InterruptedException {
        int n2;
        Component component = a;
        synchronized (component) {
            n2 = c++;
        }
        b.addImage(image, n2);
        try {
            b.waitForID(n2, 0L);
            if (b.isErrorID(n2)) {
                throw new InterruptedException("Can't load image");
            }
        }
        catch (InterruptedException interruptedException) {
            throw interruptedException;
        }
        finally {
            b.removeImage(image, n2);
        }
    }

    static final boolean b(Image image) {
        if (image instanceof BufferedImage) {
            BufferedImage bufferedImage = (BufferedImage)image;
            return bufferedImage.getColorModel().hasAlpha();
        }
        PixelGrabber pixelGrabber = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pixelGrabber.grabPixels();
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
        ColorModel colorModel = pixelGrabber.getColorModel();
        if (colorModel instanceof IndexColorModel) {
            return ((IndexColorModel)colorModel).getTransparentPixel() != -1;
        }
        if (colorModel instanceof DirectColorModel) {
            return ((DirectColorModel)colorModel).getAlphaMask() != 0;
        }
        return false;
    }

    static final BufferedImage a(BufferedImage bufferedImage, boolean bl) {
        if (bufferedImage.getType() != 10) {
            return null;
        }
        byte[] arrby = new byte[256];
        for (int i2 = 0; i2 < 256; ++i2) {
            arrby[i2] = (byte)i2;
        }
        if (bl) {
            IndexColorModel indexColorModel = new IndexColorModel(8, 256, arrby, arrby, arrby, 256);
            WritableRaster writableRaster = indexColorModel.createCompatibleWritableRaster(bufferedImage.getWidth(), bufferedImage.getHeight());
            byte[] arrby2 = ((DataBufferByte)writableRaster.getDataBuffer()).getData();
            byte[] arrby3 = ((DataBufferByte)bufferedImage.getRaster().getDataBuffer()).getData();
            for (int i3 = 0; i3 < arrby3.length; ++i3) {
                if (arrby3[i3] != -1) continue;
                arrby3[i3] = -2;
            }
            System.arraycopy(arrby3, 0, arrby2, 0, arrby3.length);
            return new BufferedImage(indexColorModel, writableRaster, false, null);
        }
        IndexColorModel indexColorModel = new IndexColorModel(8, 256, arrby, arrby, arrby);
        WritableRaster writableRaster = indexColorModel.createCompatibleWritableRaster(bufferedImage.getWidth(), bufferedImage.getHeight());
        byte[] arrby4 = ((DataBufferByte)writableRaster.getDataBuffer()).getData();
        byte[] arrby5 = ((DataBufferByte)bufferedImage.getRaster().getDataBuffer()).getData();
        System.arraycopy(arrby5, 0, arrby4, 0, arrby5.length);
        return new BufferedImage(indexColorModel, writableRaster, false, null);
    }

    static final BufferedImage b(BufferedImage bufferedImage, boolean bl) {
        int n2;
        if (bufferedImage.getType() != 12) {
            return null;
        }
        int n3 = bufferedImage.getWidth();
        int n4 = bufferedImage.getHeight();
        IndexColorModel indexColorModel = (IndexColorModel)bufferedImage.getColorModel();
        int n5 = indexColorModel.getMapSize();
        IndexColorModel indexColorModel2 = null;
        if (bl) {
            n2 = 0;
            if (n5 <= 2) {
                n2 = 2;
            } else if (n5 <= 4) {
                n2 = 3;
            } else if (n5 <= 16) {
                n2 = 5;
            }
            byte[] arrby = new byte[n5 * 2];
            indexColorModel.getReds(arrby);
            byte[] arrby2 = new byte[n5 * 2];
            indexColorModel.getGreens(arrby2);
            byte[] arrby3 = new byte[n5 * 2];
            indexColorModel.getBlues(arrby3);
            indexColorModel2 = new IndexColorModel(n2, n5 * 2, arrby, arrby2, arrby3, n5 * 2);
        } else {
            n2 = 0;
            if (n5 <= 2) {
                n2 = 1;
            } else if (n5 <= 4) {
                n2 = 2;
            } else if (n5 <= 16) {
                n2 = 4;
            }
            byte[] arrby = new byte[n5];
            indexColorModel.getReds(arrby);
            byte[] arrby4 = new byte[n5];
            indexColorModel.getGreens(arrby4);
            byte[] arrby5 = new byte[n5];
            indexColorModel.getBlues(arrby5);
            indexColorModel2 = new IndexColorModel(n2, n5, arrby, arrby4, arrby5);
        }
        byte[] arrby = ((BytePackedRaster)bufferedImage.getRaster()).getByteData(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null);
        DataBufferByte dataBufferByte = new DataBufferByte(arrby, arrby.length);
        return new BufferedImage(indexColorModel2, Raster.createInterleavedRaster(dataBufferByte, n3, n4, n3, 1, new int[]{0}, new Point(0, 0)), false, null);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static final BufferedImage toBufferedImage(Image image) {
        if (image == null) {
            throw new NullPointerException("image is null!");
        }
        if (image instanceof BufferedImage) {
            return (BufferedImage)image;
        }
        try {
            ImageUtils.a(image);
        }
        catch (InterruptedException interruptedException) {
            return null;
        }
        int n2 = 1;
        if (ImageUtils.b(image)) {
            n2 = 2;
        }
        try {
            ImageUtils.a(image);
        }
        catch (InterruptedException interruptedException) {
            return null;
        }
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), n2);
        Graphics2D graphics2D = null;
        try {
            graphics2D = bufferedImage.createGraphics();
            graphics2D.drawImage(image, 0, 0, null);
        }
        finally {
            if (graphics2D != null) {
                graphics2D.dispose();
            }
        }
        return bufferedImage;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static final BufferedImage scale(BufferedImage bufferedImage, int n2, int n3, boolean bl) {
        if (bufferedImage == null) {
            throw new NullPointerException("source image == null!");
        }
        int n4 = bufferedImage.getWidth();
        int n5 = bufferedImage.getHeight();
        if (n4 <= n2 && n5 <= n3) {
            return bufferedImage;
        }
        if (n2 <= 0 && n3 <= 0) {
            return bufferedImage;
        }
        double d2 = (double)n2 / (double)n4;
        double d3 = (double)n3 / (double)n5;
        if (n2 == 0) {
            d2 = d3;
        }
        if (n3 == 0) {
            d3 = d2;
        }
        AffineTransform affineTransform = null;
        if (bl) {
            if (d2 < d3) {
                n4 = (int)Math.rint((double)n4 * d2 - 0.01);
                n5 = (int)Math.rint((double)n5 * d2 - 0.01);
                affineTransform = AffineTransform.getScaleInstance(d2, d2);
            } else {
                n4 = (int)Math.rint((double)n4 * d3 - 0.01);
                n5 = (int)Math.rint((double)n5 * d3 - 0.01);
                affineTransform = AffineTransform.getScaleInstance(d3, d3);
            }
        } else {
            n4 = (int)Math.rint((double)n4 * d2 - 0.01);
            n5 = (int)Math.rint((double)n5 * d3 - 0.01);
            affineTransform = AffineTransform.getScaleInstance(d2, d3);
        }
        AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, 2);
        BufferedImage bufferedImage2 = null;
        boolean bl2 = false;
        try {
            bufferedImage2 = new BufferedImage(n4, n5, bufferedImage.getType());
            affineTransformOp.filter(bufferedImage, bufferedImage2);
            bl2 = true;
        }
        catch (ImagingOpException imagingOpException) {
        }
        catch (IllegalArgumentException illegalArgumentException) {
            // empty catch block
        }
        if (!bl2) {
            int n6 = 1;
            if (bufferedImage.getColorModel().hasAlpha()) {
                n6 = 2;
            }
            bufferedImage2 = new BufferedImage(n4, n5, n6);
            Graphics2D graphics2D = null;
            try {
                graphics2D = bufferedImage2.createGraphics();
                graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                graphics2D.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
                graphics2D.drawImage(bufferedImage, 0, 0, n4, n5, null);
            }
            finally {
                if (graphics2D != null) {
                    graphics2D.dispose();
                }
            }
        }
        return bufferedImage2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static BufferedImage borderWithPaint(BufferedImage bufferedImage, int n2, int n3, Paint paint) {
        if (bufferedImage == null) {
            throw new NullPointerException("image is null!");
        }
        if (bufferedImage.getWidth() >= n2 && bufferedImage.getHeight() >= n3) {
            return bufferedImage;
        }
        if (bufferedImage.getWidth() > n2) {
            n2 = bufferedImage.getWidth();
        }
        if (bufferedImage.getHeight() > n3) {
            n3 = bufferedImage.getHeight();
        }
        int n4 = 1;
        if (bufferedImage.getColorModel().hasAlpha()) {
            n4 = 2;
        }
        BufferedImage bufferedImage2 = new BufferedImage(n2, n3, n4);
        Graphics2D graphics2D = null;
        try {
            graphics2D = bufferedImage2.createGraphics();
            graphics2D.setPaint(paint);
            graphics2D.fillRect(0, 0, n2, n3);
            graphics2D.drawImage(bufferedImage, null, (n2 - bufferedImage.getWidth()) / 2, (n3 - bufferedImage.getHeight()) / 2);
        }
        finally {
            if (graphics2D != null) {
                graphics2D.dispose();
            }
        }
        return bufferedImage2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static final BufferedImage addInsets(BufferedImage bufferedImage, Insets insets, Paint paint) {
        if (bufferedImage == null) {
            throw new NullPointerException("image is null!");
        }
        int n2 = 1;
        if (bufferedImage.getColorModel().hasAlpha()) {
            n2 = 2;
        }
        BufferedImage bufferedImage2 = new BufferedImage(bufferedImage.getWidth() + insets.left + insets.right, bufferedImage.getHeight() + insets.top + insets.bottom, n2);
        Graphics2D graphics2D = null;
        try {
            graphics2D = bufferedImage2.createGraphics();
            graphics2D.setPaint(paint);
            graphics2D.fillRect(0, 0, bufferedImage2.getWidth(), bufferedImage2.getHeight());
            graphics2D.drawImage(bufferedImage, null, insets.left, insets.top);
        }
        finally {
            if (graphics2D != null) {
                graphics2D.dispose();
            }
        }
        return bufferedImage2;
    }

    static final GifImage a(GifImage gifImage) {
        Watermark watermark = new Watermark(ImageUtils.toBufferedImage(Toolkit.getDefaultToolkit().createImage(new byte[]{71, 73, 70, 56, 57, 97, 46, 0, 9, 0, -128, 0, 0, 0, 0, 0, -1, -1, -1, 33, -7, 4, 0, 0, 0, 0, 0, 44, 0, 0, 0, 0, 46, 0, 9, 0, 0, 2, 64, -116, -113, -87, -53, -115, 0, 65, -112, 6, 30, 122, 95, 116, 53, 47, 60, -127, -45, -61, -111, 99, 34, 109, -93, 8, -74, -98, 117, 82, -101, -36, -99, 94, 5, -81, 104, 76, -46, -95, 90, 11, 118, 48, -77, 91, 74, 104, 19, -118, 108, -76, 86, 49, 25, -53, -31, 114, -50, 99, -23, -118, 93, 20, 0, 0, 59})), 0, 0.6f);
        return watermark.apply(gifImage, false);
    }
}

