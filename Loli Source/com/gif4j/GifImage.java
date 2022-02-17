/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j;

import com.gif4j.GifFrame;
import com.gif4j.ImageUtils;
import com.gif4j.MorphingFilter;
import com.gif4j.Watermark;
import com.gif4j.r;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

public class GifImage {
    public static final int RESIZE_STRATEGY_CROP_TO_FIT_IMAGE_SIZE = 0;
    public static final int RESIZE_STRATEGY_SCALE_TO_FIT_IMAGE_SIZE = 1;
    public static final int RESIZE_STRATEGY_EXTEND_TO_CURRENT = 2;
    int a = 0;
    int b = 0;
    boolean c = false;
    int d;
    int e;
    byte[] f;
    byte[] g;
    byte[] h;
    byte[] i;
    boolean j = false;
    int k = -1;
    byte l;
    int m = 2;
    int n = 200;
    int o = 0;
    int p = 0;
    Color q;
    r r = null;
    String s = "89a";
    Vector t;
    Vector u;

    public GifImage() {
        this(0, 0, 2);
    }

    GifImage(boolean bl) {
        this(0, 0, 2);
        if (bl) {
            this.o = -1;
        }
    }

    public boolean isGlobalColorTable() {
        return this.c;
    }

    public GifImage(int n2) {
        this(0, 0, n2);
    }

    public GifImage(int n2, int n3) {
        this(n2, n3, 1);
    }

    public GifImage(int n2, int n3, int n4) {
        if (n2 <= 0 && n3 > 0 || n2 > 0 && n3 <= 0) {
            throw new IllegalArgumentException("image width and height should be both equal to 0 or greater than 0.");
        }
        this.a(n4);
        this.a = n2;
        this.b = n3;
        this.t = new Vector();
    }

    GifImage a(BufferedImage bufferedImage) {
        this.addGifFrame(new GifFrame(bufferedImage, 4));
        return this;
    }

    public GifImage addGifFrame(GifFrame gifFrame) {
        return this.addGifFrame(gifFrame, null, null);
    }

    public GifImage addGifFrame(GifFrame gifFrame, MorphingFilter morphingFilter) {
        return this.addGifFrame(gifFrame, morphingFilter, null);
    }

    public GifImage addGifFrame(GifFrame gifFrame, Watermark watermark) {
        return this.addGifFrame(gifFrame, null, watermark);
    }

    public GifImage addGifFrame(GifFrame gifFrame, MorphingFilter morphingFilter, Watermark watermark) {
        if (gifFrame == null) {
            throw new NullPointerException("GifFrame is null!");
        }
        if (morphingFilter != null) {
            gifFrame.w = morphingFilter;
        }
        if (gifFrame.b < 0) {
            gifFrame.b = 0;
        }
        if (gifFrame.c < 0) {
            gifFrame.c = 0;
        }
        if (this.a == 0 && this.b == 0) {
            this.a = gifFrame.b + gifFrame.d;
            this.b = gifFrame.c + gifFrame.e;
            this.t.add(gifFrame);
            gifFrame.a(this.o, watermark);
            ++this.o;
        } else if (this.m == 2) {
            if (gifFrame.b + gifFrame.d > this.a) {
                this.a = gifFrame.b + gifFrame.d;
            }
            if (gifFrame.c + gifFrame.e > this.b) {
                this.b = gifFrame.c + gifFrame.e;
            }
            this.t.add(gifFrame);
            gifFrame.a(this.o, watermark);
            ++this.o;
        } else if (!gifFrame.x) {
            gifFrame.a(this.a(gifFrame.t, gifFrame.b, gifFrame.c));
            if (gifFrame.t != null) {
                this.t.add(gifFrame);
                gifFrame.a(this.o, watermark);
                ++this.o;
            }
        } else {
            this.t.add(gifFrame);
            ++this.o;
        }
        return this;
    }

    BufferedImage a(BufferedImage bufferedImage, int n2, int n3) {
        if (bufferedImage == null) {
            return null;
        }
        int n4 = bufferedImage.getWidth();
        int n5 = bufferedImage.getHeight();
        boolean bl = false;
        if (n2 + n4 > this.a) {
            n4 = this.a - n2;
            bl = true;
        }
        if (n3 + n5 > this.b) {
            n5 = this.b - n3;
            bl = true;
        }
        if (n4 > 0 && n5 > 0) {
            if (bl) {
                bufferedImage = this.m == 0 ? bufferedImage.getSubimage(0, 0, n4, n5) : ImageUtils.scale(bufferedImage, n4, n5, true);
            }
            return bufferedImage;
        }
        return null;
    }

    public void setLoopNumber(int n2) {
        if (n2 < 0) {
            throw new IllegalArgumentException("number should be greater than or equal to 0.");
        }
        if (this.r == null) {
            this.r = new r(n2);
        } else {
            this.r.a(n2);
        }
        if (n2 == 1) {
            this.r = null;
        }
    }

    public int getLoopNumber() {
        if (this.r != null) {
            return this.r.a;
        }
        return -1;
    }

    public void setDefaultDelay(int n2) {
        if (n2 <= 0) {
            throw new IllegalArgumentException("delay should be greater than 0.");
        }
        this.n = n2;
    }

    public int getCurrentLogicWidth() {
        return this.a;
    }

    public int getCurrentLogicHeight() {
        return this.b;
    }

    public int getScreenWidth() {
        return this.a;
    }

    public int getScreenHeight() {
        return this.b;
    }

    public String getVersion() {
        return this.s;
    }

    public void addComment(String string) {
        if (string == null) {
            throw new NullPointerException("Comment is null!");
        }
        if (string.length() > 0) {
            if (this.u == null) {
                this.u = new Vector();
            }
            this.u.addElement(string);
        }
    }

    public String getComment(int n2) {
        if (this.u == null || n2 < 0 || n2 >= this.u.size()) {
            throw new IllegalArgumentException("Invalid comment's index: " + n2);
        }
        return (String)this.u.elementAt(n2);
    }

    public void removeComment(int n2) {
        if (this.u == null || n2 < 0 || n2 >= this.u.size()) {
            throw new IllegalArgumentException("Invalid comment's index: " + n2);
        }
        this.u.removeElementAt(n2);
    }

    public int getNumberOfComments() {
        if (this.u == null) {
            return 0;
        }
        return this.u.size();
    }

    public Iterator comments() {
        if (this.u != null) {
            return this.u.iterator();
        }
        return null;
    }

    public Iterator frames() {
        if (this.t != null) {
            return this.t.iterator();
        }
        return null;
    }

    public int getNumberOfFrames() {
        if (this.t == null) {
            return 0;
        }
        return this.t.size();
    }

    public GifFrame getFrame(int n2) {
        if (this.t == null || n2 < 0 || n2 >= this.t.size()) {
            throw new IllegalArgumentException("Invalid frame's index: " + n2);
        }
        return (GifFrame)this.t.elementAt(n2);
    }

    public GifFrame getLastFrame() {
        if (this.t == null || this.t.size() == 0) {
            return null;
        }
        return (GifFrame)this.t.elementAt(this.t.size() - 1);
    }

    public Color getTransparentColor() {
        return this.q;
    }

    public void setTransparentColor(Color color) {
        this.q = color;
    }

    void a(int n2) {
        if (this.m >= 0 && this.m <= 2) {
            this.m = n2;
        }
    }

    Vector a(boolean bl) {
        GifFrame gifFrame;
        if (this.t.size() == 0) {
            return null;
        }
        Vector<GifFrame> vector = new Vector<GifFrame>();
        for (int i2 = 0; i2 < this.t.size(); ++i2) {
            gifFrame = (GifFrame)this.t.get(i2);
            gifFrame.a(this.a, this.b);
            if (gifFrame.s == -1) {
                gifFrame.s = this.n;
            }
            if (this.o == 0 && !this.c) {
                this.c = true;
                this.d = gifFrame.i;
                this.e = gifFrame.j;
                this.f = gifFrame.k;
                this.g = gifFrame.l;
                this.h = gifFrame.m;
                gifFrame.f = false;
            }
            if (gifFrame.w != null) {
                GifFrame[] arrgifFrame = gifFrame.w.a(this, gifFrame);
                vector.addAll(Arrays.asList(arrgifFrame));
                continue;
            }
            vector.add(gifFrame);
        }
        if (this.q != null) {
            if (this.c) {
                if (this.k != -1) {
                    try {
                        this.f[this.k] = (byte)this.q.getRed();
                        this.g[this.k] = (byte)this.q.getGreen();
                        this.h[this.k] = (byte)this.q.getBlue();
                    }
                    catch (Exception exception) {}
                }
            } else {
                Iterator iterator = vector.iterator();
                while (iterator.hasNext()) {
                    gifFrame = (GifFrame)iterator.next();
                    if (gifFrame.r == -1 || !gifFrame.q) continue;
                    try {
                        gifFrame.k[gifFrame.r] = (byte)this.q.getRed();
                        gifFrame.l[gifFrame.r] = (byte)this.q.getGreen();
                        gifFrame.m[gifFrame.r] = (byte)this.q.getBlue();
                    }
                    catch (Exception exception) {}
                }
            }
        }
        if (this.r == null) {
            this.r = new r();
        }
        return vector;
    }

    byte[] a() {
        byte[] arrby = new byte[this.e * 3];
        int n2 = 0;
        for (int i2 = 0; i2 < this.f.length; ++i2) {
            arrby[n2] = this.f[i2];
            arrby[++n2] = this.g[i2];
            arrby[++n2] = this.h[i2];
            ++n2;
        }
        return arrby;
    }

    void b() {
        this.a = 0;
        this.b = 0;
        this.c = false;
        this.d = 0;
        this.e = 0;
        this.i = null;
        this.h = null;
        this.g = null;
        this.f = null;
        this.j = false;
        this.k = 0;
        this.l = 0;
        this.m = 2;
        this.n = 200;
        this.o = 0;
        this.q = null;
        if (this.t != null) {
            this.t.removeAllElements();
        }
    }

    void a(GifImage gifImage) {
        gifImage.k = this.k;
        gifImage.p = this.p;
        gifImage.n = this.n;
        gifImage.m = this.m;
        gifImage.l = this.l;
        gifImage.c = this.c;
        gifImage.c = this.c;
        gifImage.d = this.d;
        gifImage.e = this.e;
        if (this.f != null) {
            gifImage.f = new byte[this.f.length];
            System.arraycopy(this.f, 0, gifImage.f, 0, this.f.length);
        }
        if (this.g != null) {
            gifImage.g = new byte[this.g.length];
            System.arraycopy(this.g, 0, gifImage.g, 0, this.g.length);
        }
        if (this.h != null) {
            gifImage.h = new byte[this.h.length];
            System.arraycopy(this.h, 0, gifImage.h, 0, this.h.length);
        }
        if (this.u != null) {
            gifImage.u = (Vector)this.u.clone();
        }
        if (this.r != null) {
            gifImage.r = new r(this.r.a);
        }
        gifImage.q = this.q;
    }
}

