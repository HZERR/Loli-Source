/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.robot;

import java.nio.Buffer;
import java.nio.IntBuffer;

public class FXRobotImage {
    private final IntBuffer pixelBuffer;
    private final int width;
    private final int height;
    private final int scanlineStride;

    public static FXRobotImage create(Buffer buffer, int n2, int n3, int n4) {
        return new FXRobotImage(buffer, n2, n3, n4);
    }

    private FXRobotImage(Buffer buffer, int n2, int n3, int n4) {
        if (buffer == null) {
            throw new IllegalArgumentException("Pixel buffer must be non-null");
        }
        if (n2 <= 0 || n3 <= 0) {
            throw new IllegalArgumentException("Image dimensions must be > 0");
        }
        this.pixelBuffer = (IntBuffer)buffer;
        this.width = n2;
        this.height = n3;
        this.scanlineStride = n4;
    }

    public Buffer getPixelBuffer() {
        return this.pixelBuffer;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getScanlineStride() {
        return this.scanlineStride;
    }

    public int getPixelStride() {
        return 4;
    }

    public int getArgbPre(int n2, int n3) {
        if (n2 < 0 || n2 >= this.width || n3 < 0 || n3 >= this.height) {
            throw new IllegalArgumentException("x,y must be >0, <width, height");
        }
        return this.pixelBuffer.get(n2 + n3 * this.scanlineStride / 4);
    }

    public int getArgb(int n2, int n3) {
        if (n2 < 0 || n2 >= this.width || n3 < 0 || n3 >= this.height) {
            throw new IllegalArgumentException("x,y must be >0, <width, height");
        }
        int n4 = this.pixelBuffer.get(n2 + n3 * this.scanlineStride / 4);
        if (n4 >> 24 == -1) {
            return n4;
        }
        int n5 = n4 >>> 24;
        int n6 = n4 >> 16 & 0xFF;
        int n7 = n4 >> 8 & 0xFF;
        int n8 = n4 & 0xFF;
        int n9 = n5 + (n5 >> 7);
        n6 = n6 * n9 >> 8;
        n7 = n7 * n9 >> 8;
        n8 = n8 * n9 >> 8;
        return n5 << 24 | n6 << 16 | n7 << 8 | n8;
    }

    public String toString() {
        return super.toString() + " [format=INT_ARGB_PRE width=" + this.width + " height=" + this.height + " scanlineStride=" + this.scanlineStride + " pixelStride=" + this.getPixelStride() + " pixelBuffer=" + this.pixelBuffer + "]";
    }
}

