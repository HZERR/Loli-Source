/*
 * Decompiled with CFR 0.150.
 */
package com.objectplanet.image;

import com.objectplanet.image.a;
import com.objectplanet.image.b;
import com.objectplanet.image.c;
import com.objectplanet.image.d;
import com.objectplanet.image.e;
import com.objectplanet.image.f;
import com.objectplanet.image.g;
import java.awt.Image;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public class PngEncoder {
    public static final int COLOR_GRAYSCALE = 0;
    public static final int COLOR_TRUECOLOR = 2;
    public static final int COLOR_INDEXED = 3;
    public static final int COLOR_GRAYSCALE_ALPHA = 4;
    public static final int COLOR_INDEXED_ALPHA = 5;
    public static final int COLOR_TRUECOLOR_ALPHA = 6;
    public static final int INDEXED_COLORS_AUTO = 0;
    public static final int INDEXED_COLORS_CONVERT = 1;
    public static final int INDEXED_COLORS_ORIGINAL = 2;
    public static final int DEFAULT_COMPRESSION = -1;
    public static final int BEST_SPEED = 1;
    public static final int BEST_COMPRESSION = 9;
    private static final byte[] if = new byte[]{-119, 80, 78, 71, 13, 10, 26, 10};
    private static final byte[] a = new byte[]{0, 0, 0, 0, 73, 69, 78, 68, -82, 66, 96, -126};
    private int do;
    private int int;
    private b for;

    public PngEncoder() {
        this(2);
    }

    public PngEncoder(int n2) {
        this(n2, -1);
    }

    public PngEncoder(int n2, int n3) {
        this.int = n3;
        this.a(n2, n3);
    }

    private void a(int n2, int n3) {
        this.do = n2;
        switch (n2) {
            case 3: {
                this.for = new e(n3);
                break;
            }
            case 5: {
                this.for = new a(n3);
                break;
            }
            case 2: {
                this.for = new c(n3);
                break;
            }
            case 6: {
                this.for = new g(n3);
                break;
            }
            case 0: {
                this.for = new d(n3);
                break;
            }
            case 4: {
                this.for = new f(n3);
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid colorType, must be COLOR_INDEXED, COLOR_TRUECOLOR, COLOR_GRAYSCALE, COLOR_INDEXED_ALPHA, COLOR_TRUECOLOR_ALPHA or COLOR_GRAYSCALE_ALPHA");
            }
        }
    }

    public synchronized void setColorType(int n2) {
        this.a(n2, this.int);
    }

    public synchronized int getColorType() {
        return this.do;
    }

    public synchronized void setIndexedColorMode(int n2) {
        if (this.for instanceof e) {
            ((e)this.for).if(n2);
        }
    }

    public synchronized int getIndexedColorMode() {
        if (this.for instanceof e) {
            return ((e)this.for).do();
        }
        return -1;
    }

    public synchronized void setCompression(int n2) {
        this.int = n2;
        this.for.a(n2);
    }

    public static String getVersion() {
        return "2.0.2";
    }

    public static void main(String[] arrstring) {
        System.out.println("Java PngEncoder " + PngEncoder.getVersion());
        System.out.println("Copyright 2003-2006, ObjectPlanet, Inc.");
    }

    public synchronized void encode(Image image, OutputStream outputStream) throws IOException {
        if (image == null || image.getSource() == null || outputStream == null) {
            return;
        }
        outputStream.write(if);
        this.for.a(image, new DataOutputStream(outputStream));
        outputStream.write(a);
    }
}

