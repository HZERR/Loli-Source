/*
 * Decompiled with CFR 0.150.
 */
package com.objectplanet.image;

import com.objectplanet.image.e;
import java.awt.Image;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class a
extends e {
    protected ByteArrayOutputStream j = new ByteArrayOutputStream();
    protected ByteArrayOutputStream h = new ByteArrayOutputStream();
    private boolean i;

    a(int n2) {
        super(n2);
        this.int = 5;
    }

    synchronized void a(Image image, DataOutputStream dataOutputStream) throws IOException {
        this.j.reset();
        this.j.write("tRNS".getBytes());
        this.h.reset();
        this.h.write("tRNS".getBytes());
        this.i = false;
        super.a(image, dataOutputStream);
    }

    protected void a(DataOutputStream dataOutputStream) throws IOException {
        this.a(this.try, dataOutputStream);
        if (!this.d) {
            this.a(this.void, dataOutputStream);
            this.a(this.j, dataOutputStream);
        } else {
            this.a(this.b, dataOutputStream);
            this.a(this.h, dataOutputStream);
        }
        this.a(this.do, dataOutputStream);
    }

    public void setDimensions(int n2, int n3) {
        this.try.reset();
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(this.try);
            dataOutputStream.write("IHDR".getBytes());
            dataOutputStream.writeInt(n2);
            dataOutputStream.writeInt(n3);
            dataOutputStream.write(8);
            dataOutputStream.write(3);
            dataOutputStream.write(0);
            dataOutputStream.write(0);
            dataOutputStream.write(0);
        }
        catch (IOException iOException) {
            // empty catch block
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
        if (!this.i) {
            this.h.write(0);
            this.i = true;
        }
        int n8 = 0;
        this.long = this.void.size() - 4;
        for (int i2 = 0; i2 < n5; ++i2) {
            this.else[this.goto] = 0;
            this.byte[this.goto] = 0;
            ++this.goto;
            for (int i3 = 0; i3 < n7; ++i3) {
                if (this.e == 1 || this.e == 0) {
                    if (colorModel.getAlpha(arrn[n8]) == 0) {
                        this.else[i3 + 1] = 0;
                    } else {
                        this.do(arrn[n8]);
                    }
                }
                if (this.e == 2 || this.e == 0) {
                    int n9 = this.c.a(arrn[n8]);
                    if (n9 == -1 && this.long < 768) {
                        this.void.write((byte)colorModel.getRed(arrn[n8]));
                        this.void.write((byte)colorModel.getGreen(arrn[n8]));
                        this.void.write((byte)colorModel.getBlue(arrn[n8]));
                        this.j.write((byte)colorModel.getAlpha(arrn[n8]));
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

    public void setPixels(int n2, int n3, int n4, int n5, ColorModel colorModel, byte[] arrby, int n6, int n7) {
        if (colorModel == null || arrby == null) {
            return;
        }
        if (!this.char) {
            byte[] arrby2 = new byte[256];
            byte[] arrby3 = new byte[256];
            byte[] arrby4 = new byte[256];
            byte[] arrby5 = new byte[256];
            ((IndexColorModel)colorModel).getAlphas(arrby2);
            ((IndexColorModel)colorModel).getReds(arrby3);
            ((IndexColorModel)colorModel).getGreens(arrby4);
            ((IndexColorModel)colorModel).getBlues(arrby5);
            for (int i2 = 0; i2 < 256; ++i2) {
                this.void.write(arrby3[i2]);
                this.void.write(arrby4[i2]);
                this.void.write(arrby5[i2]);
                this.j.write(arrby2[i2]);
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
}

