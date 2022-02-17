/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.sw;

import com.sun.javafx.image.PixelConverter;
import com.sun.javafx.image.PixelGetter;
import com.sun.javafx.image.PixelUtils;
import com.sun.javafx.image.impl.ByteBgraPre;
import com.sun.javafx.image.impl.ByteGray;
import com.sun.javafx.image.impl.ByteRgb;
import com.sun.javafx.image.impl.IntArgbPre;
import com.sun.prism.MediaFrame;
import com.sun.prism.PixelFormat;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.sw.SWResourceFactory;
import com.sun.prism.sw.SWTexture;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

class SWArgbPreTexture
extends SWTexture {
    private int[] data;
    private int offset;
    private boolean hasAlpha = true;

    SWArgbPreTexture(SWResourceFactory sWResourceFactory, Texture.WrapMode wrapMode, int n2, int n3) {
        super(sWResourceFactory, wrapMode, n2, n3);
        this.offset = 0;
    }

    SWArgbPreTexture(SWArgbPreTexture sWArgbPreTexture, Texture.WrapMode wrapMode) {
        super(sWArgbPreTexture, wrapMode);
        this.data = sWArgbPreTexture.data;
        this.offset = sWArgbPreTexture.offset;
        this.hasAlpha = sWArgbPreTexture.hasAlpha;
    }

    int[] getDataNoClone() {
        return this.data;
    }

    @Override
    int getOffset() {
        return this.offset;
    }

    boolean hasAlpha() {
        return this.hasAlpha;
    }

    @Override
    public PixelFormat getPixelFormat() {
        return PixelFormat.INT_ARGB_PRE;
    }

    @Override
    public void update(Buffer buffer, PixelFormat pixelFormat, int n2, int n3, int n4, int n5, int n6, int n7, int n8, boolean bl) {
        PixelGetter<ByteBuffer> pixelGetter;
        if (PrismSettings.debug) {
            System.out.println("ARGB_PRE TEXTURE, Pixel format: " + (Object)((Object)pixelFormat) + ", buffer: " + buffer);
            System.out.println("dstx:" + n2 + " dsty:" + n3);
            System.out.println("srcx:" + n4 + " srcy:" + n5 + " srcw:" + n6 + " srch:" + n7 + " srcscan: " + n8);
        }
        this.checkDimensions(n2 + n6, n3 + n7);
        this.allocate();
        switch (pixelFormat) {
            case BYTE_RGB: {
                pixelGetter = ByteRgb.getter;
                this.hasAlpha = false;
                break;
            }
            case INT_ARGB_PRE: {
                pixelGetter = IntArgbPre.getter;
                n8 >>= 2;
                this.hasAlpha = true;
                break;
            }
            case BYTE_BGRA_PRE: {
                pixelGetter = ByteBgraPre.getter;
                this.hasAlpha = true;
                break;
            }
            case BYTE_GRAY: {
                pixelGetter = ByteGray.getter;
                this.hasAlpha = false;
                break;
            }
            default: {
                throw new UnsupportedOperationException("!!! UNSUPPORTED PIXEL FORMAT: " + (Object)((Object)pixelFormat));
            }
        }
        PixelConverter<ByteBuffer, IntBuffer> pixelConverter = PixelUtils.getConverter(pixelGetter, IntArgbPre.setter);
        buffer.position(0);
        pixelConverter.convert((ByteBuffer)buffer, n5 * n8 + n4, n8, IntBuffer.wrap(this.data), n3 * this.physicalWidth + n2, this.physicalWidth, n6, n7);
    }

    @Override
    public void update(MediaFrame mediaFrame, boolean bl) {
        if (PrismSettings.debug) {
            System.out.println("Media Pixel format: " + (Object)((Object)mediaFrame.getPixelFormat()));
        }
        mediaFrame.holdFrame();
        if (mediaFrame.getPixelFormat() != PixelFormat.INT_ARGB_PRE) {
            MediaFrame mediaFrame2 = mediaFrame.convertToFormat(PixelFormat.INT_ARGB_PRE);
            mediaFrame.releaseFrame();
            mediaFrame = mediaFrame2;
        }
        int n2 = mediaFrame.strideForPlane(0) / 4;
        IntBuffer intBuffer = mediaFrame.getBufferForPlane(0).asIntBuffer();
        if (intBuffer.hasArray()) {
            this.allocated = false;
            this.offset = 0;
            this.physicalWidth = n2;
            this.data = intBuffer.array();
        } else {
            this.allocate();
            for (int i2 = 0; i2 < this.contentHeight; ++i2) {
                intBuffer.position(this.offset + i2 * n2);
                intBuffer.get(this.data, i2 * this.physicalWidth, this.contentWidth);
            }
        }
        mediaFrame.releaseFrame();
    }

    void checkDimensions(int n2, int n3) {
        if (n2 < 0) {
            throw new IllegalArgumentException("srcw must be >=0");
        }
        if (n3 < 0) {
            throw new IllegalArgumentException("srch must be >=0");
        }
        if (n2 > this.physicalWidth) {
            throw new IllegalArgumentException("srcw exceeds WIDTH");
        }
        if (n3 > this.physicalHeight) {
            throw new IllegalArgumentException("srch exceeds HEIGHT");
        }
    }

    void applyCompositeAlpha(float f2) {
        if (this.allocated) {
            this.hasAlpha = this.hasAlpha || f2 < 1.0f;
            for (int i2 = 0; i2 < this.data.length; ++i2) {
                int n2 = (int)((float)(this.data[i2] >> 24) * f2 + 0.5f) & 0xFF;
                this.data[i2] = n2 << 24 | this.data[i2] & 0xFFFFFF;
            }
        } else {
            throw new IllegalStateException("Cannot apply composite alpha to texture with non-allocated data");
        }
    }

    @Override
    void allocateBuffer() {
        this.data = new int[this.physicalWidth * this.physicalHeight];
    }

    @Override
    Texture createSharedLockedTexture(Texture.WrapMode wrapMode) {
        return new SWArgbPreTexture(this, wrapMode);
    }
}

