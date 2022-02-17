/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.imageresize4j.ImageResizeProcessor
 */
package com.gif4j;

import com.gif4j.GifFrame;
import com.gif4j.GifImage;
import com.gif4j.b;
import com.gif4j.h;
import com.gif4j.n;
import com.gif4j.quantizer.ExactQuantizer;
import com.gif4j.quantizer.Quantizer;
import com.imageresize4j.ImageResizeProcessor;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.IndexColorModel;
import java.util.Iterator;

public class GifTransformer {
    private GifTransformer() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static final GifImage scale(GifImage gifImage, double d2, double d3, boolean bl) {
        if (gifImage == null) {
            throw new NullPointerException("gif image is null!");
        }
        if (d2 <= 0.0 && d3 <= 0.0) {
            throw new IllegalArgumentException("xscale and yscale are both less than or equal to 0");
        }
        if (gifImage.getNumberOfFrames() > 1) {
            return GifTransformer.a(gifImage, d2, d3, bl);
        }
        int n2 = gifImage.getScreenWidth();
        int n3 = gifImage.getScreenHeight();
        if (d2 <= 0.0) {
            d2 = d3;
        }
        if (d3 <= 0.0) {
            d3 = d2;
        }
        n2 = (int)Math.rint((double)n2 * d2);
        n3 = (int)Math.rint((double)n3 * d3);
        GifImage gifImage2 = new GifImage(n2, n3, 0);
        gifImage.a(gifImage2);
        gifImage2.c = false;
        Iterator iterator = gifImage.frames();
        while (iterator.hasNext()) {
            GifFrame gifFrame = (GifFrame)iterator.next();
            Point point = gifFrame.b(gifImage.a, gifImage.b);
            GifFrame gifFrame2 = gifFrame.g();
            gifFrame2.u = -1;
            gifFrame2.b = (int)Math.rint((double)point.x * d2);
            gifFrame2.c = (int)Math.rint((double)point.y * d3);
            gifFrame2.d = (int)Math.rint((double)gifFrame.d * d2);
            gifFrame2.e = (int)Math.rint((double)gifFrame.e * d3);
            BufferedImage bufferedImage = gifFrame.getAsBufferedImage();
            int n4 = 1;
            if (bufferedImage.getColorModel().hasAlpha()) {
                n4 = 2;
            }
            BufferedImage bufferedImage2 = new BufferedImage(gifFrame2.d, gifFrame2.e, n4);
            Graphics2D graphics2D = null;
            try {
                graphics2D = bufferedImage2.createGraphics();
                if (bl) {
                    graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                }
                graphics2D.drawImage(bufferedImage, 0, 0, gifFrame2.d, gifFrame2.e, null);
            }
            finally {
                if (graphics2D != null) {
                    graphics2D.dispose();
                }
            }
            bufferedImage2 = bl ? Quantizer.quantize(7, bufferedImage2, 8, gifFrame.q) : Quantizer.quantize(4, bufferedImage2, 8, gifFrame.q);
            gifFrame2.f = true;
            gifFrame2.b(bufferedImage2);
            gifImage2.addGifFrame(gifFrame2);
        }
        if (gifImage.c && gifImage.getNumberOfFrames() > 1) {
            n.a(gifImage2, false);
        }
        return gifImage2;
    }

    public static final GifImage scaleViaIR4J(GifImage gifImage, double d2, double d3, int n2) {
        return GifTransformer.scaleViaIR4J(gifImage, d2, d3, n2, 8);
    }

    public static final GifImage scaleViaIR4J(GifImage gifImage, double d2, double d3, int n2, int n3) {
        if (gifImage == null) {
            throw new NullPointerException("gif image is null!");
        }
        if (d2 <= 0.0 && d3 <= 0.0) {
            throw new IllegalArgumentException("xscale and yscale are both less than or equal to 0");
        }
        if (gifImage.getNumberOfFrames() > 1) {
            return GifTransformer.a(gifImage, d2, d3, n2, n3);
        }
        boolean bl = false;
        try {
            Class<?> class_ = Class.forName("com.imageresize4j.ImageResizeProcessor");
            if (class_ != null) {
                bl = true;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (!bl) {
            throw new RuntimeException("com.imageresize4j.ImageResizeProcessor not found in classpath!");
        }
        int n4 = gifImage.getScreenWidth();
        int n5 = gifImage.getScreenHeight();
        if (d2 <= 0.0) {
            d2 = d3;
        }
        if (d3 <= 0.0) {
            d3 = d2;
        }
        n4 = (int)Math.rint((double)n4 * d2);
        n5 = (int)Math.rint((double)n5 * d3);
        GifImage gifImage2 = new GifImage(n4, n5, 0);
        gifImage.a(gifImage2);
        gifImage2.c = false;
        Iterator iterator = gifImage.frames();
        while (iterator.hasNext()) {
            GifFrame gifFrame = (GifFrame)iterator.next();
            Point point = gifFrame.b(gifImage.a, gifImage.b);
            GifFrame gifFrame2 = gifFrame.g();
            gifFrame2.u = -1;
            gifFrame2.b = (int)Math.rint((double)point.x * d2);
            gifFrame2.c = (int)Math.rint((double)point.y * d3);
            gifFrame2.d = (int)Math.rint((double)gifFrame.d * d2);
            gifFrame2.e = (int)Math.rint((double)gifFrame.e * d3);
            BufferedImage bufferedImage = gifFrame.getAsBufferedImage();
            ImageResizeProcessor imageResizeProcessor = new ImageResizeProcessor(n2);
            BufferedImage bufferedImage2 = imageResizeProcessor.resize(bufferedImage, gifFrame2.d, gifFrame2.e);
            bufferedImage2 = Quantizer.quantize(7, bufferedImage2, n3, gifFrame.q);
            gifFrame2.f = true;
            gifFrame2.b(bufferedImage2);
            gifImage2.addGifFrame(gifFrame2);
        }
        if (gifImage.c && gifImage.getNumberOfFrames() > 1) {
            n.a(gifImage2, false);
        }
        return gifImage2;
    }

    private static final GifImage a(GifImage gifImage, double d2, double d3, boolean bl) {
        Object object;
        int n2 = gifImage.getScreenWidth();
        int n3 = gifImage.getScreenHeight();
        if (d2 <= 0.0) {
            d2 = d3;
        }
        if (d3 <= 0.0) {
            d3 = d2;
        }
        n2 = (int)Math.rint((double)n2 * d2);
        n3 = (int)Math.rint((double)n3 * d3);
        GifImage gifImage2 = new GifImage(n2, n3, 2);
        gifImage.a(gifImage2);
        gifImage2.c = false;
        BufferedImage bufferedImage = new BufferedImage(n2, n3, 2);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        int[] arrn = ((DataBufferInt)bufferedImage.getRaster().getDataBuffer()).getData();
        graphics2D.setBackground(b.c);
        graphics2D.clearRect(0, 0, n2, n3);
        BufferedImage bufferedImage2 = new BufferedImage(n2, n3, 2);
        Graphics2D graphics2D2 = bufferedImage2.createGraphics();
        graphics2D2.setBackground(b.c);
        graphics2D2.clearRect(0, 0, n2, n3);
        if (bl) {
            graphics2D2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        } else {
            graphics2D2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        }
        int[] arrn2 = ((DataBufferInt)bufferedImage2.getRaster().getDataBuffer()).getData();
        b b2 = new b(gifImage);
        Iterator iterator = gifImage.frames();
        boolean bl2 = false;
        try {
            object = Class.forName("com.imageresize4j.ImageResizeProcessor");
            if (object != null) {
                bl2 = true;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        while (iterator.hasNext()) {
            int n4;
            object = (GifFrame)iterator.next();
            GifFrame gifFrame = ((GifFrame)object).g();
            gifFrame.a(gifImage2.a, gifImage2.b);
            BufferedImage bufferedImage3 = b2.a();
            if (bl && bl2) {
                ImageResizeProcessor imageResizeProcessor = new ImageResizeProcessor(21);
                bufferedImage3 = imageResizeProcessor.resize(bufferedImage3, n2, n3);
                graphics2D2.drawImage((Image)bufferedImage3, 0, 0, null);
            } else {
                graphics2D2.drawImage(bufferedImage3, 0, 0, n2, n3, null);
            }
            int n5 = n2 - 1;
            int n6 = n3 - 1;
            int n7 = 0;
            int n8 = 0;
            for (n4 = 0; n4 < n3; ++n4) {
                for (int i2 = 0; i2 < n2; ++i2) {
                    int n9 = n4 * n2 + i2;
                    if (arrn2[n9] != arrn[n9]) {
                        if (n5 > i2) {
                            n5 = i2;
                        }
                        if (n6 > n4) {
                            n6 = n4;
                        }
                        if (n7 < i2) {
                            n7 = i2;
                        }
                        if (n8 < n4) {
                            n8 = n4;
                        }
                        arrn[n9] = arrn2[n9];
                        continue;
                    }
                    arrn[n9] = 0;
                }
            }
            if (n5 > n7) {
                n4 = n5;
                n5 = n7;
                n7 = n4;
            }
            if (n6 > n8) {
                n4 = n6;
                n6 = n8;
                n8 = n4;
            }
            gifFrame.d = n7 - n5 + 1;
            gifFrame.e = n8 - n6 + 1;
            gifFrame.b = n5;
            gifFrame.c = n6;
            BufferedImage bufferedImage4 = null;
            if (gifFrame.d > 0 && gifFrame.e > 0) {
                bufferedImage4 = gifFrame.d == n2 && gifFrame.e == n3 ? Quantizer.quantize(bl ? 7 : 4, bufferedImage, 8, ((GifFrame)object).q) : Quantizer.quantize(bl ? 7 : 4, bufferedImage.getSubimage(gifFrame.b, gifFrame.c, gifFrame.d, gifFrame.e), 8, ((GifFrame)object).q);
            } else {
                gifFrame.c = 0;
                gifFrame.b = 0;
                gifFrame.e = 1;
                gifFrame.d = 1;
                byte[] arrby = new byte[]{-1};
                IndexColorModel indexColorModel = new IndexColorModel(1, 1, arrby, arrby, arrby, 1);
                bufferedImage4 = new BufferedImage(indexColorModel, indexColorModel.createCompatibleWritableRaster(1, 1), indexColorModel.isAlphaPremultiplied(), null);
            }
            if (gifFrame.o == 1 || gifFrame.o == 0) {
                System.arraycopy(arrn2, 0, arrn, 0, arrn.length);
            } else {
                graphics2D.setBackground(b.c);
                graphics2D.clearRect(0, 0, n2, n3);
            }
            gifFrame.f = true;
            gifFrame.b(bufferedImage4);
            gifImage2.addGifFrame(gifFrame);
            graphics2D2.setBackground(b.c);
            graphics2D2.clearRect(0, 0, n2, n3);
        }
        if (graphics2D2 != null) {
            graphics2D2.dispose();
        }
        if (graphics2D != null) {
            graphics2D.dispose();
        }
        b2.b();
        if (gifImage.c) {
            n.a(gifImage2, false);
        }
        graphics2D2 = null;
        bufferedImage2 = null;
        arrn2 = null;
        graphics2D = null;
        bufferedImage = null;
        arrn = null;
        gifImage = null;
        return gifImage2;
    }

    private static final GifImage a(GifImage gifImage, double d2, double d3, int n2, int n3) {
        ImageResizeProcessor imageResizeProcessor;
        int n4 = gifImage.getScreenWidth();
        int n5 = gifImage.getScreenHeight();
        if (d2 <= 0.0) {
            d2 = d3;
        }
        if (d3 <= 0.0) {
            d3 = d2;
        }
        n4 = (int)Math.rint((double)n4 * d2);
        n5 = (int)Math.rint((double)n5 * d3);
        GifImage gifImage2 = new GifImage(n4, n5, 2);
        gifImage.a(gifImage2);
        gifImage2.c = false;
        BufferedImage bufferedImage = new BufferedImage(n4, n5, 2);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        int[] arrn = ((DataBufferInt)bufferedImage.getRaster().getDataBuffer()).getData();
        graphics2D.setBackground(b.c);
        graphics2D.clearRect(0, 0, n4, n5);
        BufferedImage bufferedImage2 = new BufferedImage(n4, n5, 2);
        Graphics2D graphics2D2 = bufferedImage2.createGraphics();
        graphics2D2.setBackground(b.c);
        graphics2D2.clearRect(0, 0, n4, n5);
        int[] arrn2 = ((DataBufferInt)bufferedImage2.getRaster().getDataBuffer()).getData();
        b b2 = new b(gifImage);
        Iterator iterator = gifImage.frames();
        boolean bl = false;
        try {
            imageResizeProcessor = Class.forName("com.imageresize4j.ImageResizeProcessor");
            if (imageResizeProcessor != null) {
                bl = true;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (!bl) {
            throw new RuntimeException("com.imageresize4j.ImageResizeProcessor isn't found in classpath!");
        }
        imageResizeProcessor = new ImageResizeProcessor(n2);
        while (iterator.hasNext()) {
            int n6;
            GifFrame gifFrame = (GifFrame)iterator.next();
            GifFrame gifFrame2 = gifFrame.g();
            gifFrame2.a(gifImage2.a, gifImage2.b);
            BufferedImage bufferedImage3 = b2.a();
            bufferedImage3 = imageResizeProcessor.resize(bufferedImage3, n4, n5);
            graphics2D2.drawImage((Image)bufferedImage3, 0, 0, null);
            int n7 = n4 - 1;
            int n8 = n5 - 1;
            int n9 = 0;
            int n10 = 0;
            for (n6 = 0; n6 < n5; ++n6) {
                for (int i2 = 0; i2 < n4; ++i2) {
                    int n11 = n6 * n4 + i2;
                    if (arrn2[n11] != arrn[n11]) {
                        if (n7 > i2) {
                            n7 = i2;
                        }
                        if (n8 > n6) {
                            n8 = n6;
                        }
                        if (n9 < i2) {
                            n9 = i2;
                        }
                        if (n10 < n6) {
                            n10 = n6;
                        }
                        arrn[n11] = arrn2[n11];
                        continue;
                    }
                    arrn[n11] = 0;
                }
            }
            if (n7 > n9) {
                n6 = n7;
                n7 = n9;
                n9 = n6;
            }
            if (n8 > n10) {
                n6 = n8;
                n8 = n10;
                n10 = n6;
            }
            gifFrame2.d = n9 - n7 + 1;
            gifFrame2.e = n10 - n8 + 1;
            gifFrame2.b = n7;
            gifFrame2.c = n8;
            BufferedImage bufferedImage4 = null;
            if (gifFrame2.d > 0 && gifFrame2.e > 0) {
                bufferedImage4 = gifFrame2.d == n4 && gifFrame2.e == n5 ? Quantizer.quantize(7, bufferedImage, n3, gifFrame.q) : Quantizer.quantize(7, bufferedImage.getSubimage(gifFrame2.b, gifFrame2.c, gifFrame2.d, gifFrame2.e), n3, gifFrame.q);
            } else {
                gifFrame2.c = 0;
                gifFrame2.b = 0;
                gifFrame2.e = 1;
                gifFrame2.d = 1;
                byte[] arrby = new byte[]{-1};
                IndexColorModel indexColorModel = new IndexColorModel(1, 1, arrby, arrby, arrby, 1);
                bufferedImage4 = new BufferedImage(indexColorModel, indexColorModel.createCompatibleWritableRaster(1, 1), indexColorModel.isAlphaPremultiplied(), null);
            }
            if (gifFrame2.o == 1 || gifFrame2.o == 0) {
                System.arraycopy(arrn2, 0, arrn, 0, arrn.length);
            } else {
                graphics2D.setBackground(b.c);
                graphics2D.clearRect(0, 0, n4, n5);
            }
            gifFrame2.f = true;
            gifFrame2.b(bufferedImage4);
            gifImage2.addGifFrame(gifFrame2);
            graphics2D2.setBackground(b.c);
            graphics2D2.clearRect(0, 0, n4, n5);
        }
        if (graphics2D2 != null) {
            graphics2D2.dispose();
        }
        if (graphics2D != null) {
            graphics2D.dispose();
        }
        b2.b();
        if (gifImage.c) {
            n.a(gifImage2, false);
        }
        graphics2D2 = null;
        bufferedImage2 = null;
        arrn2 = null;
        graphics2D = null;
        bufferedImage = null;
        arrn = null;
        gifImage = null;
        return gifImage2;
    }

    public static final GifImage exactRequantize(GifImage gifImage, int n2, int n3) {
        if (gifImage == null) {
            throw new NullPointerException("source image is null");
        }
        if (n3 < 4 || n3 > 65536) {
            throw new IllegalArgumentException("number of colors must be between 4 and 65536.");
        }
        GifImage gifImage2 = new GifImage(gifImage.getScreenWidth(), gifImage.getScreenHeight(), 0);
        gifImage.a(gifImage2);
        gifImage2.c = false;
        Iterator iterator = gifImage.frames();
        while (iterator.hasNext()) {
            GifFrame gifFrame = (GifFrame)iterator.next();
            GifFrame gifFrame2 = gifFrame.g();
            BufferedImage bufferedImage = gifFrame.getAsBufferedImage();
            bufferedImage = ExactQuantizer.quantize(n2, bufferedImage, n3, gifFrame.q);
            gifFrame2.f = true;
            gifFrame2.b(bufferedImage);
            gifImage2.addGifFrame(gifFrame2);
        }
        if (gifImage.c && gifImage.getNumberOfFrames() > 1) {
            n.a(gifImage2, false);
        }
        return gifImage2;
    }

    public static final GifImage globalRequantize(GifImage gifImage, int n2) {
        if (gifImage == null) {
            throw new NullPointerException("source image is null");
        }
        if (n2 < 4 || n2 > 255) {
            throw new IllegalArgumentException("number of colors must be between 4 and 255.");
        }
        GifImage gifImage2 = new GifImage(gifImage.getScreenWidth(), gifImage.getScreenHeight(), 0);
        gifImage.a(gifImage2);
        Iterator iterator = gifImage.frames();
        while (iterator.hasNext()) {
            GifFrame gifFrame = (GifFrame)iterator.next();
            GifFrame gifFrame2 = gifFrame.f();
            gifImage2.addGifFrame(gifFrame2);
        }
        h.a(gifImage2, false, n2, false);
        return gifImage2;
    }

    public static final GifImage requantize(GifImage gifImage, int n2, int n3) {
        if (gifImage == null) {
            throw new NullPointerException("source image is null");
        }
        if (n3 < 2 || n3 > 8) {
            throw new IllegalArgumentException("color bit depth must be between 2 and 8.");
        }
        GifImage gifImage2 = new GifImage(gifImage.getScreenWidth(), gifImage.getScreenHeight(), 0);
        gifImage.a(gifImage2);
        gifImage2.c = false;
        Iterator iterator = gifImage.frames();
        while (iterator.hasNext()) {
            GifFrame gifFrame = (GifFrame)iterator.next();
            GifFrame gifFrame2 = gifFrame.g();
            BufferedImage bufferedImage = gifFrame.getAsBufferedImage();
            bufferedImage = Quantizer.quantize(n2, bufferedImage, n3, gifFrame.q);
            gifFrame2.f = true;
            gifFrame2.b(bufferedImage);
            gifImage2.addGifFrame(gifFrame2);
        }
        if (gifImage.c && gifImage.getNumberOfFrames() > 1) {
            n.a(gifImage2, false);
        }
        return gifImage2;
    }

    public static final GifImage optimize(GifImage gifImage) {
        int n2 = gifImage.getScreenWidth();
        int n3 = gifImage.getScreenHeight();
        GifImage gifImage2 = new GifImage(n2, n3, 2);
        gifImage.a(gifImage2);
        gifImage2.c = false;
        BufferedImage bufferedImage = new BufferedImage(n2, n3, 2);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        int[] arrn = ((DataBufferInt)bufferedImage.getRaster().getDataBuffer()).getData();
        graphics2D.setBackground(b.c);
        graphics2D.clearRect(0, 0, n2, n3);
        BufferedImage bufferedImage2 = new BufferedImage(n2, n3, 2);
        Graphics2D graphics2D2 = bufferedImage2.createGraphics();
        graphics2D2.setBackground(b.c);
        graphics2D2.clearRect(0, 0, n2, n3);
        int[] arrn2 = ((DataBufferInt)bufferedImage2.getRaster().getDataBuffer()).getData();
        b b2 = new b(gifImage);
        Iterator iterator = gifImage.frames();
        while (iterator.hasNext()) {
            GifFrame gifFrame = (GifFrame)iterator.next();
            GifFrame gifFrame2 = gifFrame.g();
            gifFrame2.a(gifImage2.a, gifImage2.b);
            BufferedImage bufferedImage3 = b2.a();
            graphics2D2.drawImage((Image)bufferedImage3, 0, 0, null);
            int n4 = n2 - 1;
            int n5 = n3 - 1;
            int n6 = 0;
            int n7 = 0;
            for (int i2 = 0; i2 < n3; ++i2) {
                for (int i3 = 0; i3 < n2; ++i3) {
                    int n8 = i2 * n2 + i3;
                    if (arrn2[n8] != arrn[n8]) {
                        if (n4 > i3) {
                            n4 = i3;
                        }
                        if (n5 > i2) {
                            n5 = i2;
                        }
                        if (n6 < i3) {
                            n6 = i3;
                        }
                        if (n7 < i2) {
                            n7 = i2;
                        }
                        arrn[n8] = arrn2[n8];
                        continue;
                    }
                    arrn[n8] = 0;
                }
            }
            if (n4 > n6) {
                n4 = n6;
            }
            if (n5 > n7) {
                n5 = n7;
            }
            gifFrame2.d = n6 - n4 + 1;
            gifFrame2.e = n7 - n5 + 1;
            gifFrame2.b = n4;
            gifFrame2.c = n5;
            BufferedImage bufferedImage4 = null;
            bufferedImage4 = gifFrame2.d == n2 && gifFrame2.e == n3 ? Quantizer.quantize(7, bufferedImage, 8, gifFrame.q) : Quantizer.quantize(7, bufferedImage.getSubimage(gifFrame2.b, gifFrame2.c, gifFrame2.d, gifFrame2.e), 8, gifFrame.q);
            if (gifFrame2.o == 1 || gifFrame2.o == 0) {
                System.arraycopy(arrn2, 0, arrn, 0, arrn.length);
            } else {
                graphics2D.setBackground(b.c);
                graphics2D.clearRect(0, 0, n2, n3);
            }
            gifFrame2.f = true;
            gifFrame2.b(bufferedImage4);
            gifImage2.addGifFrame(gifFrame2);
            graphics2D2.setBackground(b.c);
            graphics2D2.clearRect(0, 0, n2, n3);
        }
        if (graphics2D2 != null) {
            graphics2D2.dispose();
        }
        if (graphics2D != null) {
            graphics2D.dispose();
        }
        b2.b();
        if (gifImage.c) {
            n.a(gifImage2, false);
        }
        graphics2D2 = null;
        bufferedImage2 = null;
        arrn2 = null;
        graphics2D = null;
        bufferedImage = null;
        arrn = null;
        gifImage = null;
        return gifImage2;
    }

    public static final GifImage resize(GifImage gifImage, int n2, int n3, boolean bl) {
        if (gifImage == null) {
            throw new NullPointerException("gif image is null!");
        }
        if (n2 <= 0 && n3 <= 0) {
            throw new IllegalArgumentException("width and height are both less than or equal to 0");
        }
        int n4 = gifImage.getScreenWidth();
        int n5 = gifImage.getScreenHeight();
        double d2 = (double)n2 / (double)n4;
        double d3 = (double)n3 / (double)n5;
        return GifTransformer.scale(gifImage, d2, d3, bl);
    }

    public static final GifImage resizeViaIR4J(GifImage gifImage, int n2, int n3, int n4) {
        return GifTransformer.resizeViaIR4J(gifImage, n2, n3, n4, 8);
    }

    public static final GifImage resizeViaIR4J(GifImage gifImage, int n2, int n3, int n4, int n5) {
        if (gifImage == null) {
            throw new NullPointerException("gif image is null!");
        }
        if (n2 <= 0 && n3 <= 0) {
            throw new IllegalArgumentException("width and height are both less than or equal to 0");
        }
        int n6 = gifImage.getScreenWidth();
        int n7 = gifImage.getScreenHeight();
        double d2 = (double)n2 / (double)n6;
        double d3 = (double)n3 / (double)n7;
        return GifTransformer.scaleViaIR4J(gifImage, d2, d3, n4, n5);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static final GifImage rotate(GifImage gifImage, double d2, boolean bl) {
        if (gifImage == null) {
            throw new NullPointerException("gif image is null!");
        }
        if (gifImage.getNumberOfFrames() > 1) {
            return GifTransformer.a(gifImage, d2, bl);
        }
        int n2 = gifImage.getScreenWidth();
        int n3 = gifImage.getScreenHeight();
        AffineTransform affineTransform = AffineTransform.getRotateInstance(d2, 0.0, 0.0);
        AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, bl ? 2 : 1);
        Rectangle2D rectangle2D = affineTransform.createTransformedShape(new Rectangle2D.Double(0.0, 0.0, n2, n3)).getBounds2D();
        n2 = (int)Math.rint(rectangle2D.getWidth());
        n3 = (int)Math.rint(rectangle2D.getHeight());
        GifImage gifImage2 = new GifImage(n2, n3, 2);
        gifImage.a(gifImage2);
        gifImage2.c = false;
        Iterator iterator = gifImage.frames();
        while (iterator.hasNext()) {
            GifFrame gifFrame = (GifFrame)iterator.next();
            GifFrame gifFrame2 = gifFrame.g();
            gifFrame2.a(gifImage2.a, gifImage2.b);
            Rectangle2D rectangle2D2 = affineTransform.createTransformedShape(new Rectangle2D.Double(0.0, 0.0, gifFrame2.d, gifFrame2.e)).getBounds2D();
            Rectangle2D rectangle2D3 = affineTransform.createTransformedShape(new Rectangle2D.Double(gifFrame2.b, gifFrame2.c, gifFrame2.d, gifFrame2.e)).getBounds2D();
            gifFrame2.d = (int)Math.rint(rectangle2D2.getWidth() + 0.1);
            gifFrame2.e = (int)Math.rint(rectangle2D2.getHeight() + 0.1);
            gifFrame2.b = (int)Math.rint(rectangle2D3.getX() + Math.abs(rectangle2D.getX()) + 0.1);
            gifFrame2.c = (int)Math.rint(rectangle2D3.getY() + Math.abs(rectangle2D.getY()) + 0.1);
            BufferedImage bufferedImage = gifFrame.getAsBufferedImage();
            BufferedImage bufferedImage2 = new BufferedImage(gifFrame2.d, gifFrame2.e, 2);
            Graphics2D graphics2D = null;
            try {
                graphics2D = bufferedImage2.createGraphics();
                graphics2D.drawImage(bufferedImage, affineTransformOp, (int)Math.abs(Math.rint(rectangle2D2.getMinX())), (int)Math.abs(Math.rint(rectangle2D2.getMinY())));
            }
            finally {
                if (graphics2D != null) {
                    graphics2D.dispose();
                }
            }
            bufferedImage2 = Quantizer.quantize(bl ? 2 : 1, bufferedImage2, 8, gifFrame.q);
            gifFrame2.f = true;
            gifFrame2.b(bufferedImage2);
            gifImage2.addGifFrame(gifFrame2);
        }
        if (gifImage.c && gifImage.getNumberOfFrames() > 1) {
            n.a(gifImage2, false);
        }
        return gifImage2;
    }

    private static final GifImage a(GifImage gifImage, double d2, boolean bl) {
        int n2 = gifImage.getScreenWidth();
        int n3 = gifImage.getScreenHeight();
        AffineTransform affineTransform = AffineTransform.getRotateInstance(d2, 0.0, 0.0);
        AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, bl ? 2 : 1);
        Rectangle2D rectangle2D = affineTransform.createTransformedShape(new Rectangle2D.Double(0.0, 0.0, n2, n3)).getBounds2D();
        n2 = (int)Math.rint(rectangle2D.getWidth());
        n3 = (int)Math.rint(rectangle2D.getHeight());
        GifImage gifImage2 = new GifImage(n2, n3, 2);
        gifImage.a(gifImage2);
        gifImage2.c = false;
        BufferedImage bufferedImage = new BufferedImage(n2, n3, 2);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        int[] arrn = ((DataBufferInt)bufferedImage.getRaster().getDataBuffer()).getData();
        graphics2D.setBackground(b.c);
        graphics2D.clearRect(0, 0, n2, n3);
        BufferedImage bufferedImage2 = new BufferedImage(n2, n3, 2);
        Graphics2D graphics2D2 = bufferedImage2.createGraphics();
        int[] arrn2 = ((DataBufferInt)bufferedImage2.getRaster().getDataBuffer()).getData();
        graphics2D2.setBackground(b.c);
        graphics2D2.clearRect(0, 0, n2, n3);
        b b2 = new b(gifImage);
        Iterator iterator = gifImage.frames();
        while (iterator.hasNext()) {
            int n4;
            GifFrame gifFrame = (GifFrame)iterator.next();
            GifFrame gifFrame2 = gifFrame.g();
            gifFrame2.a(gifImage2.a, gifImage2.b);
            BufferedImage bufferedImage3 = b2.a();
            graphics2D2.drawImage(bufferedImage3, affineTransformOp, (int)Math.abs(Math.rint(rectangle2D.getMinX())), (int)Math.abs(Math.rint(rectangle2D.getMinY())));
            int n5 = n2 - 1;
            int n6 = n3 - 1;
            int n7 = 0;
            int n8 = 0;
            for (n4 = 0; n4 < n3; ++n4) {
                for (int i2 = 0; i2 < n2; ++i2) {
                    int n9 = n4 * n2 + i2;
                    if (arrn2[n9] != arrn[n9]) {
                        if (n5 > i2) {
                            n5 = i2;
                        }
                        if (n6 > n4) {
                            n6 = n4;
                        }
                        if (n7 < i2) {
                            n7 = i2;
                        }
                        if (n8 < n4) {
                            n8 = n4;
                        }
                        arrn[n9] = arrn2[n9];
                        continue;
                    }
                    arrn[n9] = 0;
                }
            }
            if (n5 > n7) {
                n4 = n5;
                n5 = n7;
                n7 = n4;
            }
            if (n6 > n8) {
                n4 = n6;
                n6 = n8;
                n8 = n4;
            }
            gifFrame2.d = n7 - n5 + 1;
            gifFrame2.e = n8 - n6 + 1;
            gifFrame2.b = n5;
            gifFrame2.c = n6;
            BufferedImage bufferedImage4 = null;
            if (gifFrame2.d > 0 && gifFrame2.e > 0) {
                bufferedImage4 = gifFrame2.d == n2 && gifFrame2.e == n3 ? Quantizer.quantize(bl ? 7 : 4, bufferedImage, 8, gifFrame.q) : Quantizer.quantize(bl ? 7 : 4, bufferedImage.getSubimage(gifFrame2.b, gifFrame2.c, gifFrame2.d, gifFrame2.e), 8, gifFrame.q);
            } else {
                gifFrame2.c = 0;
                gifFrame2.b = 0;
                gifFrame2.e = 1;
                gifFrame2.d = 1;
                byte[] arrby = new byte[]{-1};
                IndexColorModel indexColorModel = new IndexColorModel(1, 1, arrby, arrby, arrby, 1);
                bufferedImage4 = new BufferedImage(indexColorModel, indexColorModel.createCompatibleWritableRaster(1, 1), indexColorModel.isAlphaPremultiplied(), null);
            }
            if (gifFrame2.o == 1 || gifFrame2.o == 0) {
                System.arraycopy(arrn2, 0, arrn, 0, arrn.length);
            } else {
                graphics2D.setBackground(b.c);
                graphics2D.clearRect(0, 0, n2, n3);
            }
            gifFrame2.f = true;
            gifFrame2.b(bufferedImage4);
            gifImage2.addGifFrame(gifFrame2);
            graphics2D2.setBackground(b.c);
            graphics2D2.clearRect(0, 0, n2, n3);
        }
        if (gifImage.c) {
            n.a(gifImage2, false);
        }
        if (graphics2D2 != null) {
            graphics2D2.dispose();
        }
        if (graphics2D != null) {
            graphics2D.dispose();
        }
        b2.b();
        graphics2D2 = null;
        bufferedImage2 = null;
        arrn2 = null;
        graphics2D = null;
        bufferedImage = null;
        arrn = null;
        gifImage = null;
        return gifImage2;
    }

    public static final GifImage rotate90Left(GifImage gifImage) {
        if (gifImage == null) {
            throw new NullPointerException("gif image is null!");
        }
        int n2 = gifImage.getScreenWidth();
        int n3 = gifImage.getScreenHeight();
        GifImage gifImage2 = new GifImage(n3, n2, 2);
        gifImage.a(gifImage2);
        Iterator iterator = gifImage.frames();
        while (iterator.hasNext()) {
            GifFrame gifFrame = (GifFrame)iterator.next();
            GifFrame gifFrame2 = gifFrame.f();
            gifFrame2.u = -1;
            Point point = gifFrame.b(n2, n3);
            gifFrame2.b = point.y;
            gifFrame2.c = n2 - point.x - gifFrame.d;
            gifFrame2.d = gifFrame.e;
            gifFrame2.e = gifFrame.d;
            for (int i2 = 0; i2 < gifFrame.e; ++i2) {
                for (int i3 = 0; i3 < gifFrame.d; ++i3) {
                    gifFrame2.n[(gifFrame.d - i3 - 1) * gifFrame.e + i2] = gifFrame.n[i2 * gifFrame.d + i3];
                }
            }
            gifImage2.addGifFrame(gifFrame2);
        }
        return gifImage2;
    }

    public static final GifImage rotate90Right(GifImage gifImage) {
        if (gifImage == null) {
            throw new NullPointerException("gif image is null!");
        }
        int n2 = gifImage.getScreenWidth();
        int n3 = gifImage.getScreenHeight();
        GifImage gifImage2 = new GifImage(n3, n2, 2);
        gifImage.a(gifImage2);
        Iterator iterator = gifImage.frames();
        while (iterator.hasNext()) {
            GifFrame gifFrame = (GifFrame)iterator.next();
            GifFrame gifFrame2 = gifFrame.f();
            gifFrame2.u = -1;
            Point point = gifFrame.b(n2, n3);
            gifFrame2.b = n3 - point.y - gifFrame.e;
            gifFrame2.c = point.x;
            gifFrame2.d = gifFrame.e;
            gifFrame2.e = gifFrame.d;
            for (int i2 = 0; i2 < gifFrame.e; ++i2) {
                for (int i3 = 0; i3 < gifFrame.d; ++i3) {
                    gifFrame2.n[i3 * gifFrame.e + (gifFrame.e - i2 - 1)] = gifFrame.n[i2 * gifFrame.d + i3];
                }
            }
            gifImage2.addGifFrame(gifFrame2);
        }
        return gifImage2;
    }

    public static final GifImage rotate180(GifImage gifImage) {
        if (gifImage == null) {
            throw new NullPointerException("gif image is null!");
        }
        int n2 = gifImage.getScreenWidth();
        int n3 = gifImage.getScreenHeight();
        GifImage gifImage2 = new GifImage(n2, n3, 2);
        gifImage.a(gifImage2);
        Iterator iterator = gifImage.frames();
        while (iterator.hasNext()) {
            GifFrame gifFrame = (GifFrame)iterator.next();
            GifFrame gifFrame2 = gifFrame.f();
            gifFrame2.a(gifImage2.a, gifImage2.b);
            int n4 = gifFrame2.c;
            int n5 = gifFrame2.b;
            gifFrame2.b = n2 - n5 - gifFrame.d;
            gifFrame2.c = n3 - n4 - gifFrame.e;
            for (int i2 = 0; i2 < gifFrame.e; ++i2) {
                for (int i3 = 0; i3 < gifFrame.d; ++i3) {
                    gifFrame2.n[(gifFrame.e - i2 - 1) * gifFrame.d + (gifFrame.d - i3 - 1)] = gifFrame.n[i2 * gifFrame.d + i3];
                }
            }
            gifImage2.addGifFrame(gifFrame2);
        }
        return gifImage2;
    }

    public static final GifImage flipHorizontal(GifImage gifImage) {
        if (gifImage == null) {
            throw new NullPointerException("gif image is null!");
        }
        int n2 = gifImage.getScreenWidth();
        int n3 = gifImage.getScreenHeight();
        GifImage gifImage2 = new GifImage(n2, n3, 2);
        gifImage.a(gifImage2);
        Iterator iterator = gifImage.frames();
        while (iterator.hasNext()) {
            GifFrame gifFrame = (GifFrame)iterator.next();
            GifFrame gifFrame2 = gifFrame.f();
            gifFrame2.a(gifImage2.a, gifImage2.b);
            int n4 = gifFrame2.c;
            int n5 = gifFrame2.b;
            gifFrame2.b = n2 - n5 - gifFrame.d;
            gifFrame2.c = n4;
            for (int i2 = 0; i2 < gifFrame.e; ++i2) {
                for (int i3 = 0; i3 < gifFrame.d; ++i3) {
                    gifFrame2.n[i2 * gifFrame.d + (gifFrame.d - i3 - 1)] = gifFrame.n[i2 * gifFrame.d + i3];
                }
            }
            gifImage2.addGifFrame(gifFrame2);
        }
        return gifImage2;
    }

    public static final GifImage flipVertical(GifImage gifImage) {
        if (gifImage == null) {
            throw new NullPointerException("gif image is null!");
        }
        int n2 = gifImage.getScreenWidth();
        int n3 = gifImage.getScreenHeight();
        GifImage gifImage2 = new GifImage(n2, n3, 2);
        gifImage.a(gifImage2);
        Iterator iterator = gifImage.frames();
        while (iterator.hasNext()) {
            int n4;
            GifFrame gifFrame = (GifFrame)iterator.next();
            GifFrame gifFrame2 = gifFrame.f();
            gifFrame2.a(gifImage2.a, gifImage2.b);
            int n5 = gifFrame2.c;
            gifFrame2.b = n4 = gifFrame2.b;
            gifFrame2.c = n3 - n5 - gifFrame.e;
            for (int i2 = 0; i2 < gifFrame.e; ++i2) {
                for (int i3 = 0; i3 < gifFrame.d; ++i3) {
                    gifFrame2.n[(gifFrame.e - i2 - 1) * gifFrame.d + i3] = gifFrame.n[i2 * gifFrame.d + i3];
                }
            }
            gifImage2.addGifFrame(gifFrame2);
        }
        return gifImage2;
    }

    public static final GifImage crop(GifImage gifImage, Rectangle rectangle) {
        if (gifImage == null) {
            throw new NullPointerException("gif image is null!");
        }
        int n2 = gifImage.getScreenWidth();
        int n3 = gifImage.getScreenHeight();
        if (rectangle.x < 0) {
            rectangle.x = 0;
        }
        if (rectangle.y < 0) {
            rectangle.y = 0;
        }
        if (rectangle.width + rectangle.x > n2) {
            rectangle.width = n2 - rectangle.x;
        }
        if (rectangle.height + rectangle.y > n3) {
            rectangle.height = n3 - rectangle.y;
        }
        GifImage gifImage2 = new GifImage(rectangle.width, rectangle.height, 2);
        gifImage.a(gifImage2);
        Iterator iterator = gifImage.frames();
        while (iterator.hasNext()) {
            int n4;
            int n5;
            int n6;
            GifFrame gifFrame = (GifFrame)iterator.next();
            GifFrame gifFrame2 = gifFrame.f();
            gifFrame2.u = -1;
            Rectangle rectangle2 = new Rectangle(gifFrame.b, gifFrame.c, gifFrame.d, gifFrame.e);
            if (!(rectangle.intersects(rectangle2) || rectangle.contains(rectangle2) || rectangle2.contains(rectangle))) {
                gifFrame2.c = 0;
                gifFrame2.b = 0;
                gifFrame2.e = 1;
                gifFrame2.d = 1;
                gifFrame2.n = new byte[1];
                gifFrame2.m = new byte[1];
                gifFrame2.l = gifFrame2.m;
                gifFrame2.k = gifFrame2.m;
                gifFrame2.i = GifFrame.a[gifFrame2.k.length];
                gifFrame2.j = 1 << gifFrame2.i;
                gifFrame2.q = true;
                gifFrame2.r = 0;
                gifImage2.addGifFrame(gifFrame2);
                continue;
            }
            int n7 = rectangle.x - gifFrame.b;
            if (n7 < 0) {
                n7 = 0;
            }
            if ((n6 = rectangle.y - gifFrame.c) < 0) {
                n6 = 0;
            }
            if ((n5 = gifFrame.b + gifFrame.d - (rectangle.x + rectangle.width)) < 0) {
                n5 = 0;
            }
            if ((n4 = gifFrame.c + gifFrame.e - (rectangle.y + rectangle.height)) < 0) {
                n4 = 0;
            }
            if (n7 + n5 > gifFrame.d) {
                n5 = gifFrame.d - n7;
            }
            if (n6 + n4 > gifFrame.e) {
                n4 = gifFrame.e - n6;
            }
            int n8 = gifFrame.d - n7 - n5;
            int n9 = gifFrame.e - n6 - n4;
            byte[] arrby = new byte[n8 * n9];
            for (int i2 = n6; i2 < gifFrame.e - n4; ++i2) {
                for (int i3 = n7; i3 < gifFrame.d - n5; ++i3) {
                    arrby[(i2 - n6) * n8 + i3 - n7] = gifFrame.n[i2 * gifFrame.d + i3];
                }
            }
            gifFrame2.b = gifFrame.b - rectangle.x;
            if (gifFrame2.b < 0) {
                gifFrame2.b = 0;
            }
            gifFrame2.c = gifFrame.c - rectangle.y;
            if (gifFrame2.c < 0) {
                gifFrame2.c = 0;
            }
            gifFrame2.d = n8;
            gifFrame2.e = n9;
            gifFrame2.n = arrby;
            gifImage2.addGifFrame(gifFrame2);
        }
        return gifImage2;
    }
}

