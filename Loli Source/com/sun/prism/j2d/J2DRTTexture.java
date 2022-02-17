/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.j2d;

import com.sun.glass.ui.Screen;
import com.sun.javafx.image.impl.IntArgbPre;
import com.sun.prism.Graphics;
import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import com.sun.prism.RTTexture;
import com.sun.prism.Texture;
import com.sun.prism.j2d.J2DPresentable;
import com.sun.prism.j2d.J2DResourceFactory;
import com.sun.prism.j2d.J2DTexture;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

class J2DRTTexture
extends J2DTexture
implements RTTexture {
    protected J2DResourceFactory factory;
    private boolean opaque;

    J2DRTTexture(int n2, int n3, J2DResourceFactory j2DResourceFactory) {
        super(new BufferedImage(n2, n3, 3), PixelFormat.INT_ARGB_PRE, IntArgbPre.setter, Texture.WrapMode.CLAMP_TO_ZERO);
        this.factory = j2DResourceFactory;
        this.opaque = false;
    }

    @Override
    public int[] getPixels() {
        BufferedImage bufferedImage = this.getBufferedImage();
        DataBuffer dataBuffer = bufferedImage.getRaster().getDataBuffer();
        if (dataBuffer instanceof DataBufferInt) {
            return ((DataBufferInt)dataBuffer).getData();
        }
        return null;
    }

    @Override
    public boolean readPixels(Buffer buffer, int n2, int n3, int n4, int n5) {
        if (n2 != this.getContentX() || n3 != this.getContentY() || n4 != this.getContentWidth() || n5 != this.getContentHeight()) {
            throw new IllegalArgumentException("reading subtexture not yet supported!");
        }
        return this.readPixels(buffer);
    }

    @Override
    public boolean readPixels(Buffer buffer) {
        int n2 = this.getContentWidth();
        int n3 = this.getContentHeight();
        int[] arrn = this.getPixels();
        buffer.clear();
        for (int i2 = 0; i2 < n2 * n3; ++i2) {
            int n4 = arrn[i2];
            if (buffer instanceof IntBuffer) {
                ((IntBuffer)buffer).put(n4);
                continue;
            }
            if (!(buffer instanceof ByteBuffer)) continue;
            byte by = (byte)(n4 >> 24);
            byte by2 = (byte)(n4 >> 16);
            byte by3 = (byte)(n4 >> 8);
            byte by4 = (byte)n4;
            ((ByteBuffer)buffer).put(by4);
            ((ByteBuffer)buffer).put(by3);
            ((ByteBuffer)buffer).put(by2);
            ((ByteBuffer)buffer).put(by);
        }
        buffer.rewind();
        return true;
    }

    @Override
    public Graphics createGraphics() {
        BufferedImage bufferedImage = this.getBufferedImage();
        J2DPresentable j2DPresentable = J2DPresentable.create(bufferedImage, this.factory);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        return this.factory.createJ2DPrismGraphics(j2DPresentable, graphics2D);
    }

    Graphics2D createAWTGraphics2D() {
        return this.getBufferedImage().createGraphics();
    }

    @Override
    public Screen getAssociatedScreen() {
        return this.factory.getScreen();
    }

    @Override
    public void update(Image image) {
        throw new UnsupportedOperationException("update() not supported for RTTextures");
    }

    @Override
    public void update(Image image, int n2, int n3) {
        throw new UnsupportedOperationException("update() not supported for RTTextures");
    }

    @Override
    public void update(Image image, int n2, int n3, int n4, int n5) {
        throw new UnsupportedOperationException("update() not supported for RTTextures");
    }

    @Override
    public void update(Image image, int n2, int n3, int n4, int n5, boolean bl) {
        throw new UnsupportedOperationException("update() not supported for RTTextures");
    }

    @Override
    public void update(Buffer buffer, PixelFormat pixelFormat, int n2, int n3, int n4, int n5, int n6, int n7, int n8, boolean bl) {
        throw new UnsupportedOperationException("update() not supported for RTTextures");
    }

    @Override
    public boolean isOpaque() {
        return this.opaque;
    }

    @Override
    public void setOpaque(boolean bl) {
        this.opaque = bl;
    }

    @Override
    public boolean isVolatile() {
        return false;
    }

    @Override
    public boolean isMSAA() {
        return false;
    }
}

