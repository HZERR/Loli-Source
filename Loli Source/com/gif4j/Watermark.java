/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j;

import com.gif4j.GifFrame;
import com.gif4j.GifImage;
import com.gif4j.b;
import com.gif4j.n;
import com.gif4j.quantizer.Quantizer;
import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Iterator;

public class Watermark {
    public static final int LAYOUT_TOP_LEFT = 0;
    public static final int LAYOUT_TOP_CENTER = 1;
    public static final int LAYOUT_TOP_RIGHT = 2;
    public static final int LAYOUT_MIDDLE_LEFT = 3;
    public static final int LAYOUT_MIDDLE_CENTER = 4;
    public static final int LAYOUT_MIDDLE_RIGHT = 5;
    public static final int LAYOUT_BOTTOM_LEFT = 6;
    public static final int LAYOUT_BOTTOM_CENTER = 7;
    public static final int LAYOUT_BOTTOM_RIGHT = 8;
    public static final int LAYOUT_COVER_CONSECUTIVELY = 9;
    private BufferedImage a = null;
    private float b = 0.5f;
    private int c;
    private int d;
    private int e;
    private int f;
    private int g = -1;

    public Watermark(BufferedImage bufferedImage, Point point) {
        this(bufferedImage, point, 0.5f);
    }

    public Watermark(BufferedImage bufferedImage, Point point, float f2) {
        if (bufferedImage == null) {
            throw new NullPointerException("watermarkImage == null!");
        }
        this.a = bufferedImage;
        this.c = point.x;
        this.d = point.y;
        if (f2 >= 0.0f && f2 <= 1.0f) {
            this.b = f2;
        }
        this.e = bufferedImage.getWidth();
        this.f = bufferedImage.getHeight();
    }

    public Watermark(BufferedImage bufferedImage, int n2) {
        this(bufferedImage, n2, 0.5f);
    }

    public Watermark(BufferedImage bufferedImage, int n2, float f2) {
        if (bufferedImage == null) {
            throw new NullPointerException("watermarkImage == null!");
        }
        this.a = bufferedImage;
        this.g = n2;
        if (n2 < 0 || n2 > 9) {
            throw new IllegalArgumentException("Unknown layout constraint.");
        }
        if (f2 >= 0.0f && f2 <= 1.0f) {
            this.b = f2;
        }
        this.e = bufferedImage.getWidth();
        this.f = bufferedImage.getHeight();
    }

    public void setPosition(Point point) {
        this.c = point.x;
        if (this.c < 0) {
            this.c = 0;
        }
        this.d = point.y;
        if (this.d < 0) {
            this.d = 0;
        }
        this.g = -1;
    }

    public void setLayoutConstraint(int n2) {
        if (n2 < 0 || n2 > 9) {
            throw new IllegalArgumentException("unknown layout constraint.");
        }
        this.g = n2;
    }

    public void setTransparency(float f2) {
        if (f2 >= 0.0f && f2 <= 1.0f) {
            this.b = f2;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public BufferedImage apply(BufferedImage bufferedImage) {
        if (bufferedImage == null) {
            return null;
        }
        int n2 = bufferedImage.getWidth();
        int n3 = bufferedImage.getHeight();
        if (this.g != -1) {
            this.c = 0;
            this.d = 0;
        }
        if (this.c > n2 || this.d > n3) {
            return bufferedImage;
        }
        boolean bl = false;
        if (this.d + this.f > n3) {
            this.f = n3 - this.d;
            bl = true;
        }
        if (this.e + this.c > n2) {
            this.e = n2 - this.c;
            bl = true;
        }
        if (bl) {
            this.a = this.a.getSubimage(0, 0, this.e, this.f);
        }
        int n4 = 1;
        if (bufferedImage.getColorModel().hasAlpha()) {
            n4 = 2;
        }
        BufferedImage bufferedImage2 = new BufferedImage(n2, n3, n4);
        if (this.g != 9) {
            switch (this.g) {
                case 0: {
                    this.c = 0;
                    this.d = 0;
                    break;
                }
                case 1: {
                    this.c = (n2 - this.e) / 2;
                    this.d = 0;
                    break;
                }
                case 2: {
                    this.c = n2 - this.e;
                    this.d = 0;
                    break;
                }
                case 3: {
                    this.c = 0;
                    this.d = (n3 - this.f) / 2;
                    break;
                }
                case 4: {
                    this.c = (n2 - this.e) / 2;
                    this.d = (n3 - this.f) / 2;
                    break;
                }
                case 5: {
                    this.c = n2 - this.e;
                    this.d = (n3 - this.f) / 2;
                    break;
                }
                case 6: {
                    this.c = 0;
                    this.d = n3 - this.f;
                    break;
                }
                case 7: {
                    this.c = (n2 - this.e) / 2;
                    this.d = n3 - this.f;
                    break;
                }
                case 8: {
                    this.c = n2 - this.e;
                    this.d = n3 - this.f;
                }
            }
            Graphics2D graphics2D = null;
            try {
                graphics2D = bufferedImage2.createGraphics();
                graphics2D.drawImage((Image)bufferedImage, 0, 0, null);
                AlphaComposite alphaComposite = AlphaComposite.getInstance(3, this.b);
                graphics2D.setComposite(alphaComposite);
                graphics2D.drawImage((Image)this.a, this.c, this.d, null);
            }
            finally {
                if (graphics2D != null) {
                    graphics2D.dispose();
                }
            }
        }
        Graphics2D graphics2D = null;
        try {
            graphics2D = bufferedImage2.createGraphics();
            graphics2D.drawImage((Image)bufferedImage, 0, 0, null);
            AlphaComposite alphaComposite = AlphaComposite.getInstance(3, this.b);
            graphics2D.setComposite(alphaComposite);
            for (int i2 = 0; i2 < n3; i2 += this.f) {
                for (int i3 = 0; i3 < n2; i3 += this.e) {
                    graphics2D.drawImage((Image)this.a, i3, i2, null);
                }
            }
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
    public GifImage apply(GifImage gifImage, boolean bl) {
        if (gifImage == null) {
            return null;
        }
        int n2 = gifImage.getScreenWidth();
        int n3 = gifImage.getScreenHeight();
        if (this.g != -1) {
            this.c = 0;
            this.d = 0;
        }
        if (this.c > n2 || this.d > n3) {
            return gifImage;
        }
        boolean bl2 = false;
        if (this.d + this.f > n3) {
            this.f = n3 - this.d;
            bl2 = true;
        }
        if (this.e + this.c > n2) {
            this.e = n2 - this.c;
            bl2 = true;
        }
        if (bl2) {
            this.a = this.a.getSubimage(0, 0, this.e, this.f);
        }
        if (this.g != 9) {
            switch (this.g) {
                case 0: {
                    this.c = 0;
                    this.d = 0;
                    break;
                }
                case 1: {
                    this.c = (n2 - this.e) / 2;
                    this.d = 0;
                    break;
                }
                case 2: {
                    this.c = n2 - this.e;
                    this.d = 0;
                    break;
                }
                case 3: {
                    this.c = 0;
                    this.d = (n3 - this.f) / 2;
                    break;
                }
                case 4: {
                    this.c = (n2 - this.e) / 2;
                    this.d = (n3 - this.f) / 2;
                    break;
                }
                case 5: {
                    this.c = n2 - this.e;
                    this.d = (n3 - this.f) / 2;
                    break;
                }
                case 6: {
                    this.c = 0;
                    this.d = n3 - this.f;
                    break;
                }
                case 7: {
                    this.c = (n2 - this.e) / 2;
                    this.d = n3 - this.f;
                    break;
                }
                case 8: {
                    this.c = n2 - this.e;
                    this.d = n3 - this.f;
                }
            }
        } else {
            this.c = 0;
            this.d = 0;
            BufferedImage bufferedImage = new BufferedImage(n2, n3, 2);
            Graphics2D graphics2D = null;
            try {
                graphics2D = bufferedImage.createGraphics();
                for (int i2 = 0; i2 < n3; i2 += this.f) {
                    for (int i3 = 0; i3 < n2; i3 += this.e) {
                        graphics2D.drawImage((Image)this.a, i3, i2, null);
                    }
                }
            }
            finally {
                if (graphics2D != null) {
                    graphics2D.dispose();
                }
            }
            this.a = bufferedImage;
            this.e = n2;
            this.f = n3;
        }
        int n4 = gifImage.getScreenWidth();
        int n5 = gifImage.getScreenHeight();
        GifImage gifImage2 = new GifImage(n4, n5, 2);
        gifImage.a(gifImage2);
        gifImage2.c = false;
        Iterator iterator = gifImage.frames();
        Rectangle2D.Float float_ = new Rectangle2D.Float(this.c, this.d, this.e, this.f);
        int[] arrn = new int[n4 * n5];
        while (iterator.hasNext()) {
            GifFrame gifFrame = (GifFrame)iterator.next();
            Point point = gifFrame.b(n4, n5);
            Rectangle2D.Float float_2 = new Rectangle2D.Float(point.x, point.y, gifFrame.d, gifFrame.e);
            if (float_2.intersects(float_) || float_2.contains(float_) || float_.contains(float_2)) {
                Object object;
                GifFrame gifFrame2 = gifFrame.g();
                gifFrame2.a(gifImage2.a, gifImage2.b);
                BufferedImage bufferedImage = gifFrame.getAsBufferedImage();
                BufferedImage bufferedImage2 = new BufferedImage(gifFrame2.d, gifFrame2.e, 2);
                Graphics2D graphics2D = null;
                try {
                    graphics2D = bufferedImage2.createGraphics();
                    graphics2D.drawImage(bufferedImage, null, 0, 0);
                    object = AlphaComposite.getInstance(3, this.b);
                    graphics2D.setComposite((Composite)object);
                    graphics2D.drawImage((Image)this.a, this.c - point.x, this.d - point.y, null);
                }
                finally {
                    if (graphics2D != null) {
                        graphics2D.dispose();
                    }
                }
                object = ((DataBufferInt)bufferedImage2.getRaster().getDataBuffer()).getData();
                int n6 = this.c < point.x ? point.x : this.c;
                int n7 = this.d < point.y ? point.y : this.d;
                int n8 = this.c + this.e > point.x + gifFrame2.d ? point.x + gifFrame2.d : this.c + this.e;
                int n9 = this.d + this.f > point.y + gifFrame2.e ? point.y + gifFrame2.e : this.d + this.f;
                for (int i4 = n7; i4 < n9; ++i4) {
                    for (int i5 = n6; i5 < n8; ++i5) {
                        int n10;
                        int n11 = (i4 - point.y) * gifFrame2.d + i5 - point.x;
                        Object object2 = object[n11];
                        int n12 = object2 >> 24 & 0xFF;
                        if (n12 == 0) continue;
                        if (n12 == 255) {
                            n10 = (i4 - this.d) * this.e + i5 - this.c;
                            arrn[n10] = (int)object2;
                            continue;
                        }
                        n10 = (i4 - this.d) * this.e + i5 - this.c;
                        if (arrn[n10] != 0) {
                            object[n11] = arrn[n10];
                            continue;
                        }
                        double d2 = (double)n12 / 255.0;
                        int n13 = (int)((double)(object2 >> 16 & 0xFF) * d2 + 255.0 - (double)n12);
                        int n14 = (int)((double)(object2 >> 8 & 0xFF) * d2 + 255.0 - (double)n12);
                        int n15 = (int)((double)(object2 & 0xFF) * d2 + 255.0 - (double)n12);
                        object[n11] = 0xFF000000 | n13 << 16 | n14 << 8 | n15;
                    }
                }
                bufferedImage2 = bl ? Quantizer.quantize(7, bufferedImage2, 8, gifFrame.q) : Quantizer.quantize(4, bufferedImage2, 8, gifFrame.q);
                gifFrame2.f = true;
                gifFrame2.b(bufferedImage2);
                gifImage2.addGifFrame(gifFrame2);
                continue;
            }
            gifImage2.addGifFrame(gifFrame.f());
        }
        if (gifImage.c) {
            n.a(gifImage2, false);
        }
        return gifImage2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final GifImage applyFull(GifImage gifImage, boolean bl) {
        if (gifImage == null) {
            return null;
        }
        int n2 = gifImage.getScreenWidth();
        int n3 = gifImage.getScreenHeight();
        if (this.g != -1) {
            this.c = 0;
            this.d = 0;
        }
        if (this.c > n2 || this.d > n3) {
            return gifImage;
        }
        boolean bl2 = false;
        if (this.d + this.f > n3) {
            this.f = n3 - this.d;
            bl2 = true;
        }
        if (this.e + this.c > n2) {
            this.e = n2 - this.c;
            bl2 = true;
        }
        if (bl2) {
            this.a = this.a.getSubimage(0, 0, this.e, this.f);
        }
        if (this.g != 9) {
            switch (this.g) {
                case 0: {
                    this.c = 0;
                    this.d = 0;
                    break;
                }
                case 1: {
                    this.c = (n2 - this.e) / 2;
                    this.d = 0;
                    break;
                }
                case 2: {
                    this.c = n2 - this.e;
                    this.d = 0;
                    break;
                }
                case 3: {
                    this.c = 0;
                    this.d = (n3 - this.f) / 2;
                    break;
                }
                case 4: {
                    this.c = (n2 - this.e) / 2;
                    this.d = (n3 - this.f) / 2;
                    break;
                }
                case 5: {
                    this.c = n2 - this.e;
                    this.d = (n3 - this.f) / 2;
                    break;
                }
                case 6: {
                    this.c = 0;
                    this.d = n3 - this.f;
                    break;
                }
                case 7: {
                    this.c = (n2 - this.e) / 2;
                    this.d = n3 - this.f;
                    break;
                }
                case 8: {
                    this.c = n2 - this.e;
                    this.d = n3 - this.f;
                }
            }
        } else {
            this.c = 0;
            this.d = 0;
            BufferedImage bufferedImage = new BufferedImage(n2, n3, 2);
            Graphics2D graphics2D = null;
            try {
                graphics2D = bufferedImage.createGraphics();
                for (int i2 = 0; i2 < n3; i2 += this.f) {
                    for (int i3 = 0; i3 < n2; i3 += this.e) {
                        graphics2D.drawImage((Image)this.a, i3, i2, null);
                    }
                }
            }
            finally {
                if (graphics2D != null) {
                    graphics2D.dispose();
                }
            }
            this.a = bufferedImage;
            this.e = n2;
            this.f = n3;
        }
        int n4 = gifImage.getScreenWidth();
        int n5 = gifImage.getScreenHeight();
        GifImage gifImage2 = new GifImage(n4, n5, 2);
        gifImage.a(gifImage2);
        gifImage2.c = false;
        BufferedImage bufferedImage = new BufferedImage(n4, n5, 2);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        int[] arrn = ((DataBufferInt)bufferedImage.getRaster().getDataBuffer()).getData();
        graphics2D.setBackground(com.gif4j.b.c);
        graphics2D.clearRect(0, 0, n4, n5);
        BufferedImage bufferedImage2 = new BufferedImage(n4, n5, 2);
        Graphics2D graphics2D2 = bufferedImage2.createGraphics();
        graphics2D2.setBackground(com.gif4j.b.c);
        graphics2D2.clearRect(0, 0, n4, n5);
        int[] arrn2 = ((DataBufferInt)bufferedImage2.getRaster().getDataBuffer()).getData();
        b b2 = new b(gifImage);
        Iterator iterator = gifImage.frames();
        while (iterator.hasNext()) {
            int n6;
            GifFrame gifFrame = (GifFrame)iterator.next();
            GifFrame gifFrame2 = gifFrame.g();
            BufferedImage bufferedImage3 = b2.a();
            graphics2D2.drawImage(bufferedImage3, null, 0, 0);
            Composite composite = graphics2D2.getComposite();
            AlphaComposite alphaComposite = AlphaComposite.getInstance(3, this.b);
            graphics2D2.setComposite(alphaComposite);
            graphics2D2.drawImage((Image)this.a, this.c, this.d, null);
            graphics2D2.setComposite(composite);
            int n7 = n4 - 1;
            int n8 = n5 - 1;
            int n9 = 0;
            int n10 = 0;
            for (n6 = 0; n6 < n5; ++n6) {
                for (int i4 = 0; i4 < n4; ++i4) {
                    int n11 = n6 * n4 + i4;
                    if (arrn2[n11] != arrn[n11]) {
                        if (n7 > i4) {
                            n7 = i4;
                        }
                        if (n8 > n6) {
                            n8 = n6;
                        }
                        if (n9 < i4) {
                            n9 = i4;
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
            Watermark.a(arrn);
            BufferedImage bufferedImage4 = null;
            bufferedImage4 = gifFrame2.d == n4 && gifFrame2.e == n5 ? Quantizer.quantize(bl ? 7 : 4, bufferedImage, 8, gifFrame.q) : Quantizer.quantize(bl ? 7 : 4, bufferedImage.getSubimage(gifFrame2.b, gifFrame2.c, gifFrame2.d, gifFrame2.e), 8, gifFrame.q);
            if (gifFrame2.o == 1 || gifFrame2.o == 0) {
                System.arraycopy(arrn2, 0, arrn, 0, arrn.length);
            } else {
                graphics2D.setBackground(com.gif4j.b.c);
                graphics2D.clearRect(0, 0, n4, n5);
            }
            gifFrame2.f = true;
            gifFrame2.b(bufferedImage4);
            gifImage2.addGifFrame(gifFrame2);
            graphics2D2.setBackground(com.gif4j.b.c);
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

    private static final int[] a(int[] arrn) {
        for (int i2 = 0; i2 < arrn.length; ++i2) {
            int n2 = arrn[i2];
            int n3 = n2 >> 24 & 0xFF;
            if (n3 == 0) continue;
            double d2 = (double)n3 / 255.0;
            int n4 = (int)((double)(n2 >> 16 & 0xFF) * d2 + 255.0 - (double)n3);
            int n5 = (int)((double)(n2 >> 8 & 0xFF) * d2 + 255.0 - (double)n3);
            int n6 = (int)((double)(n2 & 0xFF) * d2 + 255.0 - (double)n3);
            arrn[i2] = 0xFF000000 | n4 << 16 | n5 << 8 | n6;
        }
        return arrn;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final GifImage applyFrame(GifImage gifImage, int n2, boolean bl) {
        if (gifImage == null) {
            return null;
        }
        if (n2 < 0 || n2 >= gifImage.getNumberOfFrames()) {
            throw new IllegalArgumentException("Number of frame is invalid!");
        }
        int n3 = gifImage.getScreenWidth();
        int n4 = gifImage.getScreenHeight();
        if (this.g != -1) {
            this.c = 0;
            this.d = 0;
        }
        if (this.c > n3 || this.d > n4) {
            return gifImage;
        }
        boolean bl2 = false;
        if (this.d + this.f > n4) {
            this.f = n4 - this.d;
            bl2 = true;
        }
        if (this.e + this.c > n3) {
            this.e = n3 - this.c;
            bl2 = true;
        }
        if (bl2) {
            this.a = this.a.getSubimage(0, 0, this.e, this.f);
        }
        if (this.g != 9) {
            switch (this.g) {
                case 0: {
                    this.c = 0;
                    this.d = 0;
                    break;
                }
                case 1: {
                    this.c = (n3 - this.e) / 2;
                    this.d = 0;
                    break;
                }
                case 2: {
                    this.c = n3 - this.e;
                    this.d = 0;
                    break;
                }
                case 3: {
                    this.c = 0;
                    this.d = (n4 - this.f) / 2;
                    break;
                }
                case 4: {
                    this.c = (n3 - this.e) / 2;
                    this.d = (n4 - this.f) / 2;
                    break;
                }
                case 5: {
                    this.c = n3 - this.e;
                    this.d = (n4 - this.f) / 2;
                    break;
                }
                case 6: {
                    this.c = 0;
                    this.d = n4 - this.f;
                    break;
                }
                case 7: {
                    this.c = (n3 - this.e) / 2;
                    this.d = n4 - this.f;
                    break;
                }
                case 8: {
                    this.c = n3 - this.e;
                    this.d = n4 - this.f;
                }
            }
        } else {
            this.c = 0;
            this.d = 0;
            BufferedImage bufferedImage = new BufferedImage(n3, n4, 2);
            Graphics2D graphics2D = null;
            try {
                graphics2D = bufferedImage.createGraphics();
                for (int i2 = 0; i2 < n4; i2 += this.f) {
                    for (int i3 = 0; i3 < n3; i3 += this.e) {
                        graphics2D.drawImage((Image)this.a, i3, i2, null);
                    }
                }
            }
            finally {
                if (graphics2D != null) {
                    graphics2D.dispose();
                }
            }
            this.a = bufferedImage;
            this.e = n3;
            this.f = n4;
        }
        int n5 = gifImage.getScreenWidth();
        int n6 = gifImage.getScreenHeight();
        GifImage gifImage2 = new GifImage(n5, n6, 2);
        gifImage.a(gifImage2);
        gifImage2.c = false;
        BufferedImage bufferedImage = new BufferedImage(n5, n6, 2);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        int[] arrn = ((DataBufferInt)bufferedImage.getRaster().getDataBuffer()).getData();
        graphics2D.setBackground(com.gif4j.b.c);
        graphics2D.clearRect(0, 0, n5, n6);
        BufferedImage bufferedImage2 = new BufferedImage(n5, n6, 2);
        Graphics2D graphics2D2 = bufferedImage2.createGraphics();
        graphics2D2.setBackground(com.gif4j.b.c);
        graphics2D2.clearRect(0, 0, n5, n6);
        int[] arrn2 = ((DataBufferInt)bufferedImage2.getRaster().getDataBuffer()).getData();
        b b2 = new b(gifImage);
        Iterator iterator = gifImage.frames();
        int n7 = 0;
        while (iterator.hasNext()) {
            int n8;
            GifFrame gifFrame = (GifFrame)iterator.next();
            GifFrame gifFrame2 = gifFrame.g();
            BufferedImage bufferedImage3 = b2.a();
            graphics2D2.drawImage(bufferedImage3, null, 0, 0);
            if (n7 == n2) {
                Composite composite = graphics2D2.getComposite();
                AlphaComposite alphaComposite = AlphaComposite.getInstance(3, this.b);
                graphics2D2.setComposite(alphaComposite);
                graphics2D2.drawImage((Image)this.a, this.c, this.d, null);
                graphics2D2.setComposite(composite);
            }
            int n9 = n5 - 1;
            int n10 = n6 - 1;
            int n11 = 0;
            int n12 = 0;
            for (n8 = 0; n8 < n6; ++n8) {
                for (int i4 = 0; i4 < n5; ++i4) {
                    int n13 = n8 * n5 + i4;
                    if (arrn2[n13] != arrn[n13]) {
                        if (n9 > i4) {
                            n9 = i4;
                        }
                        if (n10 > n8) {
                            n10 = n8;
                        }
                        if (n11 < i4) {
                            n11 = i4;
                        }
                        if (n12 < n8) {
                            n12 = n8;
                        }
                        arrn[n13] = arrn2[n13];
                        continue;
                    }
                    arrn[n13] = 0;
                }
            }
            if (n9 > n11) {
                n8 = n9;
                n9 = n11;
                n11 = n8;
            }
            if (n10 > n12) {
                n8 = n10;
                n10 = n12;
                n12 = n8;
            }
            gifFrame2.d = n11 - n9 + 1;
            gifFrame2.e = n12 - n10 + 1;
            gifFrame2.b = n9;
            gifFrame2.c = n10;
            BufferedImage bufferedImage4 = null;
            bufferedImage4 = gifFrame2.d == n5 && gifFrame2.e == n6 ? Quantizer.quantize(bl ? 7 : 4, bufferedImage, 8, gifFrame.q) : Quantizer.quantize(bl ? 7 : 4, bufferedImage.getSubimage(gifFrame2.b, gifFrame2.c, gifFrame2.d, gifFrame2.e), 8, gifFrame.q);
            if (gifFrame2.o == 1 || gifFrame2.o == 0) {
                System.arraycopy(arrn2, 0, arrn, 0, arrn.length);
            } else {
                graphics2D.setBackground(com.gif4j.b.c);
                graphics2D.clearRect(0, 0, n5, n6);
            }
            gifFrame2.f = true;
            gifFrame2.b(bufferedImage4);
            gifImage2.addGifFrame(gifFrame2);
            graphics2D2.setBackground(com.gif4j.b.c);
            graphics2D2.clearRect(0, 0, n5, n6);
            ++n7;
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
}

