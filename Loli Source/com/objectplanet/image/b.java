/*
 * Decompiled with CFR 0.150.
 */
package com.objectplanet.image;

import java.awt.Image;
import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageProducer;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
abstract class b
implements ImageConsumer {
    private ImageProducer case;
    protected ByteArrayOutputStream try;
    protected ByteArrayOutputStream do;
    protected DeflaterOutputStream if;
    protected Deflater new;
    private CRC32 for;
    protected int int;
    protected byte[] byte;
    private boolean a;

    b(int n2) {
        if (n2 < -1 || n2 > 9) {
            throw new IllegalArgumentException("Invalid compression: " + n2);
        }
        this.try = new ByteArrayOutputStream();
        this.do = new ByteArrayOutputStream();
        this.new = new Deflater(n2);
        this.for = new CRC32();
    }

    void a(int n2) {
        this.new.setLevel(n2);
    }

    synchronized void a(Image image, DataOutputStream dataOutputStream) throws IOException {
        if (image == null || dataOutputStream == null) {
            return;
        }
        this.do.reset();
        this.do.write("IDAT".getBytes());
        this.new.reset();
        this.if = new DeflaterOutputStream((OutputStream)this.do, this.new);
        this.a = true;
        this.case = image.getSource();
        this.case.startProduction(this);
        while (this.a) {
            try {
                this.wait();
            }
            catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
        this.a();
        this.a(dataOutputStream);
    }

    protected void a() {
        try {
            this.if.finish();
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public void setDimensions(int n2, int n3) {
        this.try.reset();
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(this.try);
            dataOutputStream.write("IHDR".getBytes());
            dataOutputStream.writeInt(n2);
            dataOutputStream.writeInt(n3);
            dataOutputStream.write(8);
            dataOutputStream.write(this.int);
            dataOutputStream.write(0);
            dataOutputStream.write(0);
            dataOutputStream.write(0);
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    public void setColorModel(ColorModel colorModel) {
    }

    public abstract void setPixels(int var1, int var2, int var3, int var4, ColorModel var5, int[] var6, int var7, int var8);

    public void setPixels(int n2, int n3, int n4, int n5, ColorModel colorModel, byte[] arrby, int n6, int n7) {
        if (colorModel == null || arrby == null) {
            return;
        }
        int[] arrn = new int[arrby.length];
        for (int i2 = 0; i2 < arrby.length; ++i2) {
            arrn[i2] = arrby[i2] & 0xFF;
        }
        this.setPixels(n2, n3, n4, n5, colorModel, arrn, n6, n7);
    }

    public synchronized void imageComplete(int n2) {
        this.case.removeConsumer(this);
        this.a = false;
        this.notifyAll();
    }

    protected void a(DataOutputStream dataOutputStream) throws IOException {
        this.a(this.try, dataOutputStream);
        this.a(this.do, dataOutputStream);
    }

    protected void a(ByteArrayOutputStream byteArrayOutputStream, DataOutputStream dataOutputStream) throws IOException {
        if (byteArrayOutputStream != null && dataOutputStream != null) {
            dataOutputStream.writeInt(byteArrayOutputStream.size() - 4);
            byte[] arrby = byteArrayOutputStream.toByteArray();
            dataOutputStream.write(arrby);
            this.for.reset();
            this.for.update(arrby);
            dataOutputStream.writeInt((int)this.for.getValue());
        }
    }

    public void setProperties(Hashtable hashtable) {
    }

    public void setHints(int n2) {
    }
}

