/*
 * Decompiled with CFR 0.150.
 */
package com.objectplanet.image;

import com.objectplanet.image.b;
import java.awt.Image;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public class e
extends b {
    protected a c = new a();
    protected a g = new a();
    protected ByteArrayOutputStream void = new ByteArrayOutputStream();
    protected ByteArrayOutputStream b = new ByteArrayOutputStream();
    protected boolean char;
    protected byte[] else;
    protected int goto;
    protected int f;
    protected int long;
    protected int e;
    protected boolean d;

    e(int n2) {
        super(n2);
        this.int = 3;
    }

    synchronized void a(Image image, DataOutputStream dataOutputStream) throws IOException {
        this.void.reset();
        this.void.write("PLTE".getBytes());
        this.b.reset();
        this.b.write("PLTE".getBytes());
        if (this.e == 2 || this.e == 0) {
            this.c.a();
        }
        this.char = false;
        this.f = (image.getWidth(null) + 1) * image.getHeight(null);
        this.byte = new byte[this.f];
        this.else = new byte[this.f];
        this.goto = 0;
        super.a(image, dataOutputStream);
    }

    protected void a() {
        try {
            if (!this.d) {
                this.if.write(this.byte, 0, this.f);
            } else {
                this.if.write(this.else, 0, this.f);
            }
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
        super.a();
    }

    protected void a(DataOutputStream dataOutputStream) throws IOException {
        this.a(this.try, dataOutputStream);
        if (!this.d) {
            this.a(this.void, dataOutputStream);
        } else {
            this.a(this.b, dataOutputStream);
        }
        this.a(this.do, dataOutputStream);
    }

    protected void if() {
        int n2;
        int n3;
        int n4 = 0xFFFFFF;
        for (n3 = 0; n3 < 3; ++n3) {
            this.b.write(-1);
            this.b.write(-1);
            this.b.write(-1);
            this.g.a(n4, n3);
        }
        for (n2 = 50; n2 <= 255; n2 += 41) {
            for (int i2 = 50; i2 <= 255; i2 += 41) {
                for (int i3 = 50; i3 <= 255; i3 += 41) {
                    if (n2 == i2 && n2 == i3) continue;
                    n4 = n2 << 16 | i2 << 8 | i3;
                    this.b.write((byte)n2);
                    this.b.write((byte)i2);
                    this.b.write((byte)i3);
                    this.g.a(n4, n3);
                    ++n3;
                }
            }
        }
        for (n2 = 0; n2 <= 252; n2 += 6) {
            n4 = n2 << 16 | n2 << 8 | n2;
            this.b.write(n2);
            this.b.write(n2);
            this.b.write(n2);
            this.g.a(n4, n3);
            ++n3;
        }
    }

    public void setPixels(int n2, int n3, int n4, int n5, ColorModel colorModel, int[] arrn, int n6, int n7) {
        if (colorModel == null || arrn == null) {
            return;
        }
        if (!(this.char || this.e != 1 && this.e != 0)) {
            this.if();
            this.char = true;
        }
        int n8 = 0;
        this.long = this.void.size() - 4;
        for (int i2 = 0; i2 < n5; ++i2) {
            this.else[this.goto] = 0;
            this.byte[this.goto] = 0;
            ++this.goto;
            for (int i3 = 0; i3 < n7; ++i3) {
                if (this.e == 1 || this.e == 0) {
                    this.do(arrn[n8]);
                }
                if (this.e == 2 || this.e == 0) {
                    int n9 = this.c.a(arrn[n8]);
                    if (n9 == -1 && this.long < 768) {
                        this.void.write((byte)colorModel.getRed(arrn[n8]));
                        this.void.write((byte)colorModel.getGreen(arrn[n8]));
                        this.void.write((byte)colorModel.getBlue(arrn[n8]));
                        n9 = this.long / 3;
                        this.c.a(arrn[n8], n9);
                        this.long += 3;
                    }
                    this.byte[this.goto] = n9 >= 0 ? (byte)n9 : (byte)0;
                }
                ++this.goto;
                ++n8;
            }
            if (this.e != 1 && (this.e != 0 || this.long < 768)) continue;
            this.d = true;
        }
    }

    void do(int n2) {
        int n3;
        int n4;
        int n5 = n2 >> 16 & 0xFF;
        int n6 = n2 >> 8 & 0xFF;
        int n7 = n2 & 0xFF;
        float f2 = Math.abs(n5 - n6);
        f2 = Math.max(f2, (float)Math.abs(n5 - n7));
        if ((f2 = Math.max(f2, (float)Math.abs(n7 - n6))) < 24.0f || n5 < 50 && n6 < 50 && n7 < 50) {
            n4 = (int)((double)n5 * 0.3 + (double)n6 * 0.59 + (double)n7 * 0.11);
            n4 = (int)Math.round((double)n4 / 6.0) * 6;
            n4 = Math.max(n4, 6);
            n4 = Math.min(n4, 252);
            n3 = n4 << 16 | n4 << 8 | n4;
        } else {
            n4 = (int)Math.round((double)(n5 - 50) / 41.0) * 41 + 50;
            int n8 = (int)Math.round((double)(n6 - 50) / 41.0) * 41 + 50;
            int n9 = (int)Math.round((double)(n7 - 50) / 41.0) * 41 + 50;
            n4 = Math.max(n4, 50);
            n8 = Math.max(n8, 50);
            n9 = Math.max(n9, 50);
            if (n4 == n8 && n8 == n9) {
                n8 = n9 = (n4 = (int)Math.round((double)n4 / 6.0) * 6);
            }
            n3 = n4 << 16 | n8 << 8 | n9;
        }
        n4 = this.g.a(n3);
        this.else[this.goto] = n4 >= 0 ? (byte)n4 : (byte)0;
    }

    public synchronized void if(int n2) {
        this.c.a();
        this.char = false;
        this.e = n2;
    }

    public synchronized int do() {
        return this.e;
    }

    public void setPixels(int n2, int n3, int n4, int n5, ColorModel colorModel, byte[] arrby, int n6, int n7) {
        if (colorModel == null || arrby == null) {
            return;
        }
        if (!this.char) {
            byte[] arrby2 = new byte[256];
            byte[] arrby3 = new byte[256];
            byte[] arrby4 = new byte[256];
            ((IndexColorModel)colorModel).getReds(arrby2);
            ((IndexColorModel)colorModel).getGreens(arrby3);
            ((IndexColorModel)colorModel).getBlues(arrby4);
            for (int i2 = 0; i2 < 256; ++i2) {
                this.void.write(arrby2[i2]);
                this.void.write(arrby3[i2]);
                this.void.write(arrby4[i2]);
            }
            this.char = true;
        }
        try {
            this.if.write(0);
            this.if.write(arrby, 0, arrby.length);
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    class com.objectplanet.image.e$a {
        a[] a = new a[101];

        com.objectplanet.image.e$a() {
        }

        void a(int n2, int n3) {
            int n4 = (n2 & Integer.MAX_VALUE) % this.a.length;
            a a2 = this.a[n4];
            while (a2 != null) {
                if (a2.a == n2) {
                    a2.do = n3;
                    return;
                }
                a2 = a2.if;
            }
            a2 = new a();
            a2.a = n2;
            a2.do = n3;
            a2.if = this.a[n4];
            this.a[n4] = a2;
        }

        int a(int n2) {
            int n3 = (n2 & Integer.MAX_VALUE) % this.a.length;
            a a2 = this.a[n3];
            while (a2 != null) {
                if (a2.a == n2) {
                    return a2.do;
                }
                a2 = a2.if;
            }
            return -1;
        }

        void a() {
            for (int i2 = 0; i2 < this.a.length; ++i2) {
                this.a[i2] = null;
            }
        }

        /*
         * Illegal identifiers - consider using --renameillegalidents true
         */
        class a {
            int a;
            int do;
            a if;

            a() {
            }
        }
    }
}

