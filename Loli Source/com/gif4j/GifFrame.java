/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j;

import com.gif4j.ImageUtils;
import com.gif4j.MorphingFilter;
import com.gif4j.Watermark;
import com.gif4j.quantizer.Quantizer;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.MemoryImageSource;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class GifFrame {
    public static final int DISPOSAL_METHOD_NOT_SPECIFIED = 0;
    public static final int DISPOSAL_METHOD_DO_NOT_DISPOSE = 1;
    public static final int DISPOSAL_METHOD_RESTORE_TO_BACKGROUND_COLOR = 2;
    public static final int DISPOSAL_METHOD_RESTORE_TO_PREVIOUS = 3;
    public static final int LAYOUT_TOP_LEFT = 0;
    public static final int LAYOUT_TOP_CENTER = 1;
    public static final int LAYOUT_TOP_RIGHT = 2;
    public static final int LAYOUT_MIDDLE_LEFT = 3;
    public static final int LAYOUT_MIDDLE_CENTER = 4;
    public static final int LAYOUT_MIDDLE_RIGHT = 5;
    public static final int LAYOUT_BOTTOM_LEFT = 6;
    public static final int LAYOUT_BOTTOM_CENTER = 7;
    public static final int LAYOUT_BOTTOM_RIGHT = 8;
    static final byte[] a;
    int b = 0;
    int c = 0;
    int d;
    int e;
    boolean f = true;
    boolean g = false;
    boolean h = false;
    int i;
    int j;
    byte[] k;
    byte[] l;
    byte[] m;
    byte[] n;
    int o;
    boolean p = false;
    boolean q = false;
    int r = -1;
    int s = -1;
    BufferedImage t = null;
    int u = -1;
    int v = 0;
    MorphingFilter w = null;
    boolean x = false;

    GifFrame() {
    }

    public boolean isLocalColorTable() {
        return this.f;
    }

    public GifFrame(BufferedImage bufferedImage) {
        this(bufferedImage, new Point(0, 0), 1, -1);
    }

    public GifFrame(BufferedImage bufferedImage, int n2) {
        this(bufferedImage, n2, 1, -1);
    }

    public GifFrame(BufferedImage bufferedImage, int n2, int n3) {
        this(bufferedImage, n2, n3, -1);
    }

    public GifFrame(BufferedImage bufferedImage, int n2, int n3, int n4) {
        this(bufferedImage, new Point(0, 0), n3, n4);
        this.setLayoutConstraint(n2);
    }

    public GifFrame(BufferedImage bufferedImage, Point point) {
        this(bufferedImage, point, 1, -1);
    }

    public GifFrame(BufferedImage bufferedImage, Point point, int n2) {
        this(bufferedImage, point, n2, -1);
    }

    public GifFrame(BufferedImage bufferedImage, Point point, int n2, int n3) {
        if (bufferedImage == null) {
            throw new NullPointerException("image is null!");
        }
        this.b = point.x;
        this.c = point.y;
        if (this.b < 0) {
            this.b = 0;
        }
        if (this.c < 0) {
            this.c = 0;
        }
        if (n3 <= 0 && n3 != -1) {
            throw new IllegalArgumentException("delay time (in 1/100 sec) should be greater than 0.");
        }
        this.a(bufferedImage);
        this.setDisposalMethod(n2);
        this.setDelay(n3);
    }

    public void setDisposalMethod(int n2) {
        if (n2 < 0 || n2 > 3) {
            throw new IllegalArgumentException("unknown disposal method.");
        }
        this.o = n2;
    }

    public int getDisposalMethod() {
        return this.o;
    }

    public int getX() {
        return this.b;
    }

    public int getY() {
        return this.c;
    }

    public int getWidth() {
        return this.d;
    }

    public int getHeight() {
        return this.e;
    }

    public void setLayoutConstraint(int n2) {
        if (n2 < 0 || n2 > 8) {
            throw new IllegalArgumentException("unknown layout constraint.");
        }
        this.u = n2;
    }

    public void setDelay(int n2) {
        if (n2 < 0) {
            n2 = -1;
        }
        this.s = n2;
    }

    public int getDelay() {
        if (this.s < 0) {
            return 0;
        }
        return this.s;
    }

    public boolean isInterlaced() {
        return this.h;
    }

    public void setInterlaced(boolean bl) {
        this.h = bl;
    }

    public IndexColorModel getColorModel() {
        byte[] arrby = new byte[this.k.length];
        System.arraycopy(this.k, 0, arrby, 0, this.k.length);
        byte[] arrby2 = new byte[this.l.length];
        System.arraycopy(this.l, 0, arrby2, 0, this.l.length);
        byte[] arrby3 = new byte[this.m.length];
        System.arraycopy(this.m, 0, arrby3, 0, this.m.length);
        return new IndexColorModel(this.i, Math.min(this.j, this.k.length), arrby, arrby2, arrby3, this.r);
    }

    public Image getAsImage() {
        byte[] arrby = new byte[this.n.length];
        System.arraycopy(this.n, 0, arrby, 0, this.n.length);
        return Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(this.d, this.e, (ColorModel)this.getColorModel(), arrby, 0, this.d));
    }

    public BufferedImage getAsBufferedImage() {
        DataBufferByte dataBufferByte = new DataBufferByte(this.n.length);
        byte[] arrby = dataBufferByte.getData();
        System.arraycopy(this.n, 0, arrby, 0, this.n.length);
        WritableRaster writableRaster = Raster.createInterleavedRaster(dataBufferByte, this.d, this.e, this.d, 1, new int[]{0}, null);
        IndexColorModel indexColorModel = this.getColorModel();
        return new BufferedImage(indexColorModel, writableRaster, indexColorModel.isAlphaPremultiplied(), null);
    }

    void a(BufferedImage bufferedImage) {
        if (bufferedImage == null) {
            return;
        }
        this.t = bufferedImage;
        this.d = bufferedImage.getWidth();
        this.e = bufferedImage.getHeight();
    }

    void a(int n2, Watermark watermark) {
        this.v = n2;
        if (this.x) {
            return;
        }
        if (watermark != null) {
            this.t = watermark.apply(this.t);
        }
        if (this.t.getType() != 13) {
            this.t = this.t.getType() == 10 ? ImageUtils.a(this.t, n2 != -1) : (this.t.getType() == 12 ? ImageUtils.b(this.t, n2 != -1) : Quantizer.quantize(this.t, 8, n2 != -1));
        } else if (!(this.t.getRaster().getDataBuffer() instanceof DataBufferByte) || this.w != null && ((IndexColorModel)this.t.getColorModel()).getTransparentPixel() == -1) {
            this.t = Quantizer.quantize(this.t, 8, n2 != -1);
        }
        this.b(this.t);
        this.x = true;
    }

    void b(BufferedImage bufferedImage) {
        IndexColorModel indexColorModel = (IndexColorModel)bufferedImage.getColorModel();
        this.k = new byte[indexColorModel.getMapSize()];
        indexColorModel.getReds(this.k);
        this.m = new byte[indexColorModel.getMapSize()];
        indexColorModel.getBlues(this.m);
        this.l = new byte[indexColorModel.getMapSize()];
        indexColorModel.getGreens(this.l);
        byte[] arrby = ((DataBufferByte)bufferedImage.getRaster().getDataBuffer()).getData();
        this.n = new byte[arrby.length];
        System.arraycopy(arrby, 0, this.n, 0, arrby.length);
        this.i = a[this.k.length];
        this.j = 1 << this.i;
        this.q = indexColorModel.hasAlpha();
        this.r = indexColorModel.getTransparentPixel();
    }

    void a(int n2, int n3) {
        if (this.u != -1) {
            switch (this.u) {
                case 0: {
                    this.b = 0;
                    this.c = 0;
                    break;
                }
                case 1: {
                    this.b = (n2 - this.d) / 2;
                    this.c = 0;
                    break;
                }
                case 2: {
                    this.b = n2 - this.d;
                    this.c = 0;
                    break;
                }
                case 3: {
                    this.b = 0;
                    this.c = (n3 - this.e) / 2;
                    break;
                }
                case 4: {
                    this.b = (n2 - this.d) / 2;
                    this.c = (n3 - this.e) / 2;
                    break;
                }
                case 5: {
                    this.b = n2 - this.d;
                    this.c = (n3 - this.e) / 2;
                    break;
                }
                case 6: {
                    this.b = 0;
                    this.c = n3 - this.e;
                    break;
                }
                case 7: {
                    this.b = (n2 - this.d) / 2;
                    this.c = n3 - this.e;
                    break;
                }
                case 8: {
                    this.b = n2 - this.d;
                    this.c = n3 - this.e;
                }
            }
            this.u = -1;
        }
    }

    Point b(int n2, int n3) {
        Point point = new Point(this.b, this.c);
        if (this.u != -1) {
            switch (this.u) {
                case 0: {
                    point.x = 0;
                    point.y = 0;
                    break;
                }
                case 1: {
                    point.x = (n2 - this.d) / 2;
                    point.y = 0;
                    break;
                }
                case 2: {
                    point.x = n2 - this.d;
                    point.y = 0;
                    break;
                }
                case 3: {
                    point.x = 0;
                    point.y = (n3 - this.e) / 2;
                    break;
                }
                case 4: {
                    point.x = (n2 - this.d) / 2;
                    point.y = (n3 - this.e) / 2;
                    break;
                }
                case 5: {
                    point.x = n2 - this.d;
                    point.y = (n3 - this.e) / 2;
                    break;
                }
                case 6: {
                    point.x = 0;
                    point.y = n3 - this.e;
                    break;
                }
                case 7: {
                    point.x = (n2 - this.d) / 2;
                    point.y = n3 - this.e;
                    break;
                }
                case 8: {
                    point.x = n2 - this.d;
                    point.y = n3 - this.e;
                }
            }
        }
        return point;
    }

    void a() {
        this.n = null;
        this.l = null;
        this.m = null;
        this.k = null;
        this.t = null;
    }

    byte[] b() {
        byte[] arrby = new byte[this.j * 3];
        int n2 = 0;
        for (int i2 = 0; i2 < this.k.length; ++i2) {
            arrby[n2] = this.k[i2];
            arrby[++n2] = this.l[i2];
            arrby[++n2] = this.m[i2];
            ++n2;
        }
        return arrby;
    }

    byte[] c() {
        byte[] arrby = new byte[]{33, -7, 4, (byte)(this.o << 2 | (this.p ? 2 : 0) | (this.q ? 1 : 0)), (byte)(this.s & 0xFF), (byte)(this.s >> 8 & 0xFF), (byte)this.r, 0};
        return arrby;
    }

    byte[] d() {
        byte[] arrby = new byte[]{44, (byte)(this.b & 0xFF), (byte)(this.b >> 8 & 0xFF), (byte)(this.c & 0xFF), (byte)(this.c >> 8 & 0xFF), (byte)(this.d & 0xFF), (byte)(this.d >> 8 & 0xFF), (byte)(this.e & 0xFF), (byte)(this.e >> 8 & 0xFF), (byte)((this.f ? 128 : 0) | (this.h ? 64 : 0) | this.i - 1)};
        return arrby;
    }

    byte[] e() {
        if (this.h) {
            byte[] arrby = new byte[this.n.length];
            int n2 = 0;
            int n3 = 0;
            while (n3 < this.e) {
                System.arraycopy(this.n, n3 * this.d, arrby, n2 * this.d, this.d);
                n3 += 8;
                ++n2;
            }
            n3 = 4;
            while (n3 < this.e) {
                System.arraycopy(this.n, n3 * this.d, arrby, n2 * this.d, this.d);
                n3 += 8;
                ++n2;
            }
            n3 = 2;
            while (n3 < this.e) {
                System.arraycopy(this.n, n3 * this.d, arrby, n2 * this.d, this.d);
                n3 += 4;
                ++n2;
            }
            n3 = 1;
            while (n3 < this.e) {
                System.arraycopy(this.n, n3 * this.d, arrby, n2 * this.d, this.d);
                n3 += 2;
                ++n2;
            }
            return arrby;
        }
        return this.n;
    }

    GifFrame f() {
        return this.a(false);
    }

    GifFrame a(boolean bl) {
        GifFrame gifFrame = new GifFrame();
        gifFrame.n = new byte[this.n.length];
        if (bl) {
            for (int i2 = 0; i2 < this.n.length; ++i2) {
                gifFrame.n[i2] = (byte)this.r;
            }
        } else {
            System.arraycopy(this.n, 0, gifFrame.n, 0, this.n.length);
        }
        if (this.f) {
            gifFrame.f = true;
            gifFrame.k = new byte[this.k.length];
            gifFrame.l = new byte[this.l.length];
            gifFrame.m = new byte[this.m.length];
            System.arraycopy(this.k, 0, gifFrame.k, 0, this.k.length);
            System.arraycopy(this.l, 0, gifFrame.l, 0, this.l.length);
            System.arraycopy(this.m, 0, gifFrame.m, 0, this.m.length);
        } else {
            gifFrame.f = false;
            if (this.k != null) {
                gifFrame.k = new byte[this.k.length];
                System.arraycopy(this.k, 0, gifFrame.k, 0, this.k.length);
            }
            if (this.l != null) {
                gifFrame.l = new byte[this.l.length];
                System.arraycopy(this.l, 0, gifFrame.l, 0, this.l.length);
            }
            if (this.m != null) {
                gifFrame.m = new byte[this.m.length];
                System.arraycopy(this.m, 0, gifFrame.m, 0, this.m.length);
            }
        }
        gifFrame.h = this.h;
        gifFrame.s = this.s;
        gifFrame.o = this.o;
        gifFrame.u = this.u;
        gifFrame.i = this.i;
        gifFrame.j = this.j;
        gifFrame.g = this.g;
        gifFrame.p = this.p;
        gifFrame.q = this.q;
        gifFrame.r = this.r;
        gifFrame.b = this.b;
        gifFrame.c = this.c;
        gifFrame.d = this.d;
        gifFrame.e = this.e;
        gifFrame.w = this.w;
        gifFrame.v = this.v;
        gifFrame.x = true;
        return gifFrame;
    }

    GifFrame g() {
        GifFrame gifFrame = new GifFrame();
        gifFrame.h = this.h;
        gifFrame.s = this.s;
        gifFrame.o = this.o;
        gifFrame.u = this.u;
        gifFrame.p = this.p;
        gifFrame.b = this.b;
        gifFrame.c = this.c;
        gifFrame.d = this.d;
        gifFrame.e = this.e;
        gifFrame.x = true;
        gifFrame.w = this.w;
        gifFrame.v = this.v;
        return gifFrame;
    }

    GifFrame a(int n2, int n3, int n4, int n5, boolean bl) {
        GifFrame gifFrame = new GifFrame();
        gifFrame.n = new byte[n4 * n5];
        if (bl) {
            for (int i2 = 0; i2 < gifFrame.n.length; ++i2) {
                gifFrame.n[i2] = (byte)this.r;
            }
        } else {
            for (int i3 = 0; i3 < n5; ++i3) {
                System.arraycopy(this.n, (n3 + i3) * this.d + n2, gifFrame.n, i3 * n4, n4);
            }
        }
        if (this.f) {
            gifFrame.k = new byte[this.k.length];
            gifFrame.l = new byte[this.l.length];
            gifFrame.m = new byte[this.m.length];
            System.arraycopy(this.k, 0, gifFrame.k, 0, this.k.length);
            System.arraycopy(this.l, 0, gifFrame.l, 0, this.l.length);
            System.arraycopy(this.m, 0, gifFrame.m, 0, this.m.length);
        } else {
            gifFrame.f = false;
        }
        gifFrame.h = this.h;
        gifFrame.s = this.s;
        gifFrame.o = this.o;
        gifFrame.u = this.u;
        gifFrame.i = this.i;
        gifFrame.j = this.j;
        gifFrame.g = this.g;
        gifFrame.p = this.p;
        gifFrame.q = this.q;
        gifFrame.r = this.r;
        gifFrame.b = 0;
        gifFrame.c = 0;
        gifFrame.d = n4;
        gifFrame.e = n5;
        gifFrame.x = true;
        return gifFrame;
    }

    static {
        int n2;
        a = new byte[257];
        GifFrame.a[2] = 1;
        GifFrame.a[1] = 1;
        GifFrame.a[4] = 2;
        GifFrame.a[3] = 2;
        GifFrame.a[8] = 3;
        GifFrame.a[7] = 3;
        GifFrame.a[6] = 3;
        GifFrame.a[5] = 3;
        for (n2 = 9; n2 <= 16; ++n2) {
            GifFrame.a[n2] = 4;
        }
        for (n2 = 17; n2 <= 32; ++n2) {
            GifFrame.a[n2] = 5;
        }
        for (n2 = 33; n2 <= 64; ++n2) {
            GifFrame.a[n2] = 6;
        }
        for (n2 = 65; n2 <= 128; ++n2) {
            GifFrame.a[n2] = 7;
        }
        for (n2 = 129; n2 <= 256; ++n2) {
            GifFrame.a[n2] = 8;
        }
    }
}

