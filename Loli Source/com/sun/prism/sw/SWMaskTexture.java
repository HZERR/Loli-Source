/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.sw;

import com.sun.prism.MediaFrame;
import com.sun.prism.PixelFormat;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.sw.SWResourceFactory;
import com.sun.prism.sw.SWTexture;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public class SWMaskTexture
extends SWTexture {
    private byte[] data;

    SWMaskTexture(SWResourceFactory sWResourceFactory, Texture.WrapMode wrapMode, int n2, int n3) {
        super(sWResourceFactory, wrapMode, n2, n3);
    }

    SWMaskTexture(SWMaskTexture sWMaskTexture, Texture.WrapMode wrapMode) {
        super(sWMaskTexture, wrapMode);
        this.data = sWMaskTexture.data;
    }

    byte[] getDataNoClone() {
        return this.data;
    }

    @Override
    public PixelFormat getPixelFormat() {
        return PixelFormat.BYTE_ALPHA;
    }

    @Override
    public void update(Buffer buffer, PixelFormat pixelFormat, int n2, int n3, int n4, int n5, int n6, int n7, int n8, boolean bl) {
        if (PrismSettings.debug) {
            System.out.println("MASK TEXTURE, Pixel format: " + (Object)((Object)pixelFormat) + ", buffer: " + buffer);
            System.out.println("dstx:" + n2 + " dsty:" + n3);
            System.out.println("srcx:" + n4 + " srcy:" + n5 + " srcw:" + n6 + " srch:" + n7 + " srcscan: " + n8);
        }
        if (pixelFormat != PixelFormat.BYTE_ALPHA) {
            throw new IllegalArgumentException("SWMaskTexture supports BYTE_ALPHA format only.");
        }
        this.checkAllocation(n6, n7);
        this.physicalWidth = n6;
        this.physicalHeight = n7;
        this.allocate();
        ByteBuffer byteBuffer = (ByteBuffer)buffer;
        for (int i2 = 0; i2 < n7; ++i2) {
            byteBuffer.position((n5 + i2) * n8 + n4);
            byteBuffer.get(this.data, i2 * this.physicalWidth, n6);
        }
    }

    @Override
    public void update(MediaFrame mediaFrame, boolean bl) {
        throw new UnsupportedOperationException("update6:unimp");
    }

    void checkAllocation(int n2, int n3) {
        int n4;
        if (this.allocated && (n4 = n2 * n3) > this.data.length) {
            throw new IllegalArgumentException("SRCW * SRCH exceeds buffer length");
        }
    }

    @Override
    void allocateBuffer() {
        this.data = new byte[this.physicalWidth * this.physicalHeight];
    }

    @Override
    Texture createSharedLockedTexture(Texture.WrapMode wrapMode) {
        return new SWMaskTexture(this, wrapMode);
    }
}

