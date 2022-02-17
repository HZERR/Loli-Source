/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j;

import com.gif4j.GifFrame;
import com.gif4j.GifImage;
import com.gif4j.f;
import com.gif4j.i;
import com.gif4j.j;
import com.gif4j.r;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class GifDecoder {
    private GifDecoder() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static final GifImage decode(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("Input File is null!");
        }
        if (!file.canRead()) {
            throw new IOException("Can't read Input File!");
        }
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            GifImage gifImage = GifDecoder.decode(fileInputStream);
            return gifImage;
        }
        finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                }
                catch (Exception exception) {}
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static final GifImage decode(URL uRL) throws IOException {
        if (uRL == null) {
            throw new IllegalArgumentException("Input URL is null!");
        }
        InputStream inputStream = null;
        try {
            inputStream = uRL.openStream();
        }
        catch (IOException iOException) {
            IOException iOException2 = new IOException("Can't get input stream from the URL!");
            iOException2.initCause(iOException);
            throw iOException2;
        }
        try {
            GifImage gifImage = GifDecoder.decode(inputStream);
            return gifImage;
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (Exception exception) {}
            }
        }
    }

    public static final GifImage decode(InputStream inputStream) throws IOException {
        int n2;
        Object object;
        if (inputStream == null) {
            throw new NullPointerException("Input stream is null!");
        }
        f f2 = new f(inputStream, 8192);
        byte[] arrby = new byte[3];
        byte[] arrby2 = new byte[3];
        byte[] arrby3 = new byte[7];
        f2.read(arrby);
        String string = new String(arrby);
        if (!string.equals("GIF")) {
            throw new IOException("The specified input stream is not recognised as a GIF image");
        }
        f2.read(arrby2);
        GifImage gifImage = new GifImage();
        gifImage.s = new String(arrby2);
        f2.read(arrby3);
        gifImage.a = arrby3[0] & 0xFF | (arrby3[1] & 0xFF) << 8;
        gifImage.b = arrby3[2] & 0xFF | (arrby3[3] & 0xFF) << 8;
        byte by = arrby3[4];
        gifImage.k = arrby3[5] & 0xFF;
        gifImage.l = arrby3[6];
        gifImage.p = (by >> 4 & 7) + 1;
        gifImage.d = (by & 7) + 1;
        gifImage.e = 1 << gifImage.d;
        if ((by & 0x80) != 0) {
            gifImage.c = true;
            gifImage.j = (by & 8) != 0;
            object = new byte[3 * gifImage.e];
            if (f2.read((byte[])object) != ((byte[])object).length) {
                throw new IOException("Invalid input stream: more data is expected!");
            }
            gifImage.f = new byte[gifImage.e];
            gifImage.h = new byte[gifImage.e];
            gifImage.g = new byte[gifImage.e];
            for (n2 = 0; n2 < gifImage.e; ++n2) {
                gifImage.f[n2] = object[3 * n2];
                gifImage.g[n2] = object[3 * n2 + 1];
                gifImage.h[n2] = object[3 * n2 + 2];
            }
        } else {
            gifImage.c = false;
            gifImage.j = false;
            gifImage.k = -1;
            gifImage.d = gifImage.p;
            gifImage.e = 1 << gifImage.d;
        }
        object = GifDecoder.a(f2, gifImage);
        n2 = f2.read();
        while (n2 == 44) {
            int n3;
            byte[] arrby4 = new byte[9];
            f2.read(arrby4);
            GifFrame gifFrame = new GifFrame();
            gifFrame.b = arrby4[0] & 0xFF | (arrby4[1] & 0xFF) << 8;
            gifFrame.c = arrby4[2] & 0xFF | (arrby4[3] & 0xFF) << 8;
            gifFrame.d = arrby4[4] & 0xFF | (arrby4[5] & 0xFF) << 8;
            gifFrame.e = arrby4[6] & 0xFF | (arrby4[7] & 0xFF) << 8;
            byte by2 = arrby4[8];
            boolean bl = gifFrame.h = (by2 & 0x40) != 0;
            if ((by2 & 0x80) != 0) {
                gifFrame.f = true;
                gifFrame.g = (by2 & 0x20) != 0;
                gifFrame.i = (by2 & 7) + 1;
                gifFrame.j = 1 << gifFrame.i;
                byte[] arrby5 = new byte[3 * gifFrame.j];
                if (f2.read(arrby5) != arrby5.length) {
                    throw new IOException("Invalid input stream: more data is expected!");
                }
                gifFrame.k = new byte[gifFrame.j];
                gifFrame.m = new byte[gifFrame.j];
                gifFrame.l = new byte[gifFrame.j];
                for (int i2 = 0; i2 < gifFrame.j; ++i2) {
                    gifFrame.k[i2] = arrby5[3 * i2];
                    gifFrame.l[i2] = arrby5[3 * i2 + 1];
                    gifFrame.m[i2] = arrby5[3 * i2 + 2];
                }
                if (object != null) {
                    gifFrame.q = object.b;
                    gifFrame.r = object.c;
                    gifFrame.p = object.a;
                    gifFrame.o = object.e;
                    gifFrame.s = object.d;
                } else {
                    gifFrame.q = false;
                    gifFrame.r = -1;
                    gifFrame.p = false;
                    gifFrame.o = 0;
                    gifFrame.s = 0;
                }
            } else {
                gifFrame.f = false;
                gifFrame.i = gifImage.d;
                gifFrame.j = 1 << gifFrame.i;
                if (gifImage.c) {
                    gifFrame.k = new byte[gifFrame.j];
                    gifFrame.m = new byte[gifFrame.j];
                    gifFrame.l = new byte[gifFrame.j];
                    System.arraycopy(gifImage.f, 0, gifFrame.k, 0, gifFrame.j);
                    System.arraycopy(gifImage.g, 0, gifFrame.l, 0, gifFrame.j);
                    System.arraycopy(gifImage.h, 0, gifFrame.m, 0, gifFrame.j);
                } else {
                    gifFrame.f = true;
                    gifFrame.k = new byte[gifFrame.j];
                    gifFrame.m = new byte[gifFrame.j];
                    gifFrame.l = new byte[gifFrame.j];
                    for (n3 = 0; n3 < gifFrame.j; ++n3) {
                        byte by3;
                        gifFrame.k[n3] = by3 = (byte)(256.0 / (double)gifFrame.j * (double)n3);
                        gifFrame.l[n3] = by3;
                        gifFrame.m[n3] = by3;
                    }
                }
                if (object != null) {
                    gifFrame.q = object.b;
                    gifFrame.r = object.c;
                    gifFrame.p = object.a;
                    gifFrame.o = object.e;
                    gifFrame.s = object.d;
                } else {
                    if (gifImage.c && gifImage.getLastFrame() != null) {
                        gifFrame.q = gifImage.getLastFrame().q;
                        gifFrame.r = gifImage.getLastFrame().r;
                    } else {
                        gifFrame.q = false;
                        gifFrame.r = -1;
                    }
                    gifFrame.p = false;
                    gifFrame.o = 0;
                    gifFrame.s = 0;
                }
            }
            n3 = f2.read();
            if (n3 < 0) {
                throw new IOException("Invalid input stream: more data is expected!");
            }
            gifFrame.n = new byte[gifFrame.d * gifFrame.e];
            i i3 = new i();
            i3.a(f2, gifFrame, n3);
            gifFrame.x = true;
            gifImage.addGifFrame(gifFrame);
            n2 = f2.read();
            if (n2 > 0) {
                f2.a(new byte[]{(byte)n2});
            }
            object = GifDecoder.a(f2, gifImage);
            n2 = f2.read();
        }
        return gifImage;
    }

    private static final j a(f f2, GifImage gifImage) throws IOException {
        j j2 = null;
        int n2 = f2.read();
        while (n2 != 44 && n2 != 59 && n2 >= 0) {
            if (n2 == 33) {
                Object object;
                Object object2;
                int n3 = f2.read();
                if (n3 == 254) {
                    byte[] arrby = new byte[]{};
                    object2 = new byte[255];
                    int n4 = f2.read();
                    while (n4 > 0 && f2.read((byte[])object2, 0, n4) != -1) {
                        object = arrby;
                        arrby = new byte[((Object)object).length + n4];
                        System.arraycopy(object, 0, arrby, 0, ((Object)object).length);
                        System.arraycopy(object2, 0, arrby, ((Object)object).length, n4);
                        n4 = f2.read();
                    }
                    object = new String(arrby, "US-ASCII");
                    if (!((String)object).startsWith("gif4j")) {
                        gifImage.addComment((String)object);
                    }
                } else if (n3 == 1) {
                    f2.read();
                    byte[] arrby = new byte[255];
                    int n5 = f2.read();
                    while (n5 > 0 && f2.read(arrby, 0, n5) != -1) {
                        n5 = f2.read();
                    }
                } else if (n3 == 249) {
                    f2.read();
                    byte[] arrby = new byte[4];
                    f2.read(arrby);
                    byte by = arrby[0];
                    j j3 = new j();
                    j3.a = (by & 2) != 0;
                    j3.e = by >> 2 & 7;
                    j3.d = arrby[1] & 0xFF | (arrby[2] & 0xFF) << 8;
                    if ((by & 1) != 0) {
                        j3.b = true;
                        j3.c = arrby[3] & 0xFF;
                    } else {
                        j3.b = false;
                        j3.c = -1;
                    }
                    f2.read();
                    j2 = j3;
                } else if (n3 == 255) {
                    f2.read();
                    byte[] arrby = new byte[8];
                    f2.read(arrby);
                    object2 = new String(arrby);
                    byte[] arrby2 = new byte[3];
                    f2.read(arrby2);
                    object = new String(arrby2);
                    byte[] arrby3 = new byte[]{};
                    byte[] arrby4 = new byte[255];
                    int n6 = f2.read();
                    while (n6 > 0 && f2.read(arrby4, 0, n6) != -1) {
                        byte[] arrby5 = arrby3;
                        arrby3 = new byte[arrby5.length + n6];
                        System.arraycopy(arrby5, 0, arrby3, 0, arrby5.length);
                        System.arraycopy(arrby4, 0, arrby3, arrby5.length, n6);
                        n6 = f2.read();
                    }
                    if (((String)object2).equals("NETSCAPE") && ((String)object).equals("2.0") && arrby3[0] == 1) {
                        gifImage.r = new r(arrby3[1] & 0xFF | (arrby3[2] & 0xFF) << 8);
                    }
                } else {
                    int n7 = f2.read();
                    if (n7 < 0) {
                        throw new IOException("Invalid input stream: more data is expected!");
                    }
                    f2.skip(n7);
                }
            }
            n2 = f2.read();
        }
        if (n2 == 44 || n2 == 59) {
            f2.a(new byte[]{(byte)n2});
        }
        return j2;
    }
}

