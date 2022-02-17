/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.mac;

import com.sun.glass.ui.Pixels;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

final class MacPixels
extends Pixels {
    private static final int nativeFormat = MacPixels._initIDs();

    private static native int _initIDs();

    static int getNativeFormat_impl() {
        return nativeFormat;
    }

    protected MacPixels(int n2, int n3, ByteBuffer byteBuffer) {
        super(n2, n3, byteBuffer);
    }

    protected MacPixels(int n2, int n3, IntBuffer intBuffer) {
        super(n2, n3, intBuffer);
    }

    protected MacPixels(int n2, int n3, IntBuffer intBuffer, float f2) {
        super(n2, n3, intBuffer, f2);
    }

    @Override
    protected void _fillDirectByteBuffer(ByteBuffer byteBuffer) {
        if (this.bytes != null) {
            this.bytes.rewind();
            if (this.bytes.isDirect()) {
                this._copyPixels(byteBuffer, this.bytes, this.getWidth() * this.getHeight());
            } else {
                byteBuffer.put(this.bytes);
            }
            this.bytes.rewind();
        } else {
            this.ints.rewind();
            if (this.ints.isDirect()) {
                this._copyPixels(byteBuffer, this.ints, this.getWidth() * this.getHeight());
            } else {
                for (int i2 = 0; i2 < this.ints.capacity(); ++i2) {
                    int n2 = this.ints.get();
                    byteBuffer.put((byte)(n2 >> 0 & 0xFF));
                    byteBuffer.put((byte)(n2 >> 8 & 0xFF));
                    byteBuffer.put((byte)(n2 >> 16 & 0xFF));
                    byteBuffer.put((byte)(n2 >> 24 & 0xFF));
                }
            }
            this.ints.rewind();
        }
    }

    protected native void _copyPixels(Buffer var1, Buffer var2, int var3);

    @Override
    protected native void _attachInt(long var1, int var3, int var4, IntBuffer var5, int[] var6, int var7);

    @Override
    protected native void _attachByte(long var1, int var3, int var4, ByteBuffer var5, byte[] var6, int var7);

    public String toString() {
        return "MacPixels [" + this.getWidth() + "x" + this.getHeight() + "]: " + super.toString();
    }
}

