/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j;

import com.gif4j.GifFrame;
import com.gif4j.GifImage;
import com.gif4j.ImageUtils;
import com.gif4j.a;
import com.gif4j.c;
import java.awt.image.BufferedImage;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

public class GifEncoder {
    private static final byte[] a = new byte[6];

    private GifEncoder() {
    }

    public static final void encode(GifImage gifImage, OutputStream outputStream) throws IOException {
        GifEncoder.encode(gifImage, outputStream, false);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static final void encode(GifImage gifImage, OutputStream outputStream, boolean bl) throws IOException {
        if (gifImage == null) {
            throw new NullPointerException("gif image is null!");
        }
        if (outputStream == null) {
            throw new NullPointerException("output stream is null!");
        }
        DataOutputStream dataOutputStream = null;
        try {
            dataOutputStream = new DataOutputStream(outputStream);
            GifEncoder.a(gifImage, dataOutputStream, bl);
        }
        finally {
            if (dataOutputStream != null) {
                try {
                    dataOutputStream.flush();
                }
                catch (IOException iOException) {}
            }
        }
    }

    public static final void encode(GifImage gifImage, File file) throws IOException {
        GifEncoder.encode(gifImage, file, false);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static final void encode(GifImage gifImage, File file, boolean bl) throws IOException {
        if (gifImage == null) {
            throw new NullPointerException("gif image is null!");
        }
        if (file == null) {
            throw new NullPointerException("output is null!");
        }
        FilterOutputStream filterOutputStream = null;
        try {
            file.delete();
            filterOutputStream = new DataOutputStream(new FileOutputStream(file));
            GifEncoder.a(gifImage, (DataOutput)((Object)filterOutputStream), bl);
        }
        finally {
            if (filterOutputStream != null) {
                filterOutputStream.close();
            }
        }
    }

    private static final void a(GifImage gifImage, DataOutput dataOutput, boolean bl) throws IOException {
        if (dataOutput == null) {
            throw new NullPointerException("output us null!");
        }
        GifImage gifImage2 = ImageUtils.a(gifImage);
        Vector vector = gifImage2.a(bl);
        if (vector == null) {
            return;
        }
        GifEncoder.a(gifImage2, dataOutput);
        GifEncoder.a(vector, dataOutput);
        GifEncoder.b(gifImage2, dataOutput);
        dataOutput.write(59);
        gifImage.b();
        gifImage2.b();
    }

    public static final void encode(BufferedImage bufferedImage, OutputStream outputStream) throws IOException {
        if (bufferedImage == null) {
            throw new NullPointerException("image == null!");
        }
        GifEncoder.encode(new GifImage(true).a(bufferedImage), outputStream);
    }

    public static final void encode(BufferedImage bufferedImage, DataOutput dataOutput) throws IOException {
        if (bufferedImage == null) {
            throw new NullPointerException("image == null!");
        }
        GifEncoder.a(new GifImage(true).a(bufferedImage), dataOutput, false);
    }

    public static final void encode(BufferedImage bufferedImage, File file) throws IOException {
        if (bufferedImage == null) {
            throw new NullPointerException("image == null!");
        }
        GifEncoder.encode(new GifImage(true).a(bufferedImage), file, false);
    }

    private static final void a(GifImage gifImage, DataOutput dataOutput) throws IOException {
        dataOutput.write(a);
        GifEncoder.a(gifImage.a, dataOutput);
        GifEncoder.a(gifImage.b, dataOutput);
        byte by = (byte)((gifImage.c ? 128 : 0) | 0x70 | (gifImage.c ? gifImage.d - 1 : 0));
        dataOutput.write(by);
        dataOutput.write(gifImage.k);
        dataOutput.write(gifImage.l);
        if (gifImage.c) {
            dataOutput.write(gifImage.a());
        }
        if (gifImage.r != null) {
            gifImage.r.a(dataOutput);
        }
    }

    private static final void b(GifImage gifImage, DataOutput dataOutput) throws IOException {
        int n2 = gifImage.getNumberOfComments();
        if (n2 > 0) {
            c c2 = null;
            for (int i2 = 0; i2 < n2; ++i2) {
                c2 = new c(gifImage.getComment(i2));
                dataOutput.write(c2.a());
            }
        }
    }

    private static final void a(Vector vector, DataOutput dataOutput) throws IOException {
        a a2 = new a(dataOutput);
        int n2 = vector.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            GifFrame gifFrame = (GifFrame)vector.elementAt(i2);
            dataOutput.write(gifFrame.c());
            dataOutput.write(gifFrame.d());
            if (gifFrame.f) {
                dataOutput.write(gifFrame.b());
            }
            int n3 = gifFrame.i <= 1 ? 2 : gifFrame.i;
            dataOutput.write((byte)n3);
            byte[] arrby = gifFrame.e();
            if (arrby.length > 0) {
                a2.a(gifFrame.e(), n3 + 1);
            }
            dataOutput.write(0);
            gifFrame.a();
        }
        com.gif4j.a.a(a2);
    }

    private static final void a(int n2, DataOutput dataOutput) throws IOException {
        dataOutput.write((byte)(n2 & 0xFF));
        dataOutput.write((byte)(n2 >> 8 & 0xFF));
    }

    static {
        GifEncoder.a[0] = 71;
        GifEncoder.a[1] = 73;
        GifEncoder.a[2] = 70;
        GifEncoder.a[3] = 56;
        GifEncoder.a[4] = 57;
        GifEncoder.a[5] = 97;
    }
}

