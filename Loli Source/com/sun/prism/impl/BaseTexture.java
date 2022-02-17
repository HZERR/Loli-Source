/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl;

import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import com.sun.prism.Texture;
import com.sun.prism.impl.ManagedResource;
import java.nio.Buffer;

public abstract class BaseTexture<T extends ManagedResource>
implements Texture {
    protected final T resource;
    private final PixelFormat format;
    private final int physicalWidth;
    private final int physicalHeight;
    private final int contentX;
    private final int contentY;
    protected int contentWidth;
    protected int contentHeight;
    private final int maxContentWidth;
    private final int maxContentHeight;
    private final Texture.WrapMode wrapMode;
    private final boolean useMipmap;
    private boolean linearFiltering = true;
    private int lastImageSerial;

    protected BaseTexture(BaseTexture<T> baseTexture, Texture.WrapMode wrapMode, boolean bl) {
        this.resource = baseTexture.resource;
        this.format = baseTexture.format;
        this.wrapMode = wrapMode;
        this.physicalWidth = baseTexture.physicalWidth;
        this.physicalHeight = baseTexture.physicalHeight;
        this.contentX = baseTexture.contentX;
        this.contentY = baseTexture.contentY;
        this.contentWidth = baseTexture.contentWidth;
        this.contentHeight = baseTexture.contentHeight;
        this.maxContentWidth = baseTexture.maxContentWidth;
        this.maxContentHeight = baseTexture.maxContentHeight;
        this.useMipmap = bl;
    }

    protected BaseTexture(T t2, PixelFormat pixelFormat, Texture.WrapMode wrapMode, int n2, int n3) {
        this(t2, pixelFormat, wrapMode, n2, n3, 0, 0, n2, n3, false);
    }

    protected BaseTexture(T t2, PixelFormat pixelFormat, Texture.WrapMode wrapMode, int n2, int n3, int n4, int n5, int n6, int n7, boolean bl) {
        this.resource = t2;
        this.format = pixelFormat;
        this.wrapMode = wrapMode;
        this.physicalWidth = n2;
        this.physicalHeight = n3;
        this.contentX = n4;
        this.contentY = n5;
        this.contentWidth = n6;
        this.contentHeight = n7;
        this.maxContentWidth = n2;
        this.maxContentHeight = n3;
        this.useMipmap = bl;
    }

    protected BaseTexture(T t2, PixelFormat pixelFormat, Texture.WrapMode wrapMode, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9, boolean bl) {
        this.resource = t2;
        this.format = pixelFormat;
        this.wrapMode = wrapMode;
        this.physicalWidth = n2;
        this.physicalHeight = n3;
        this.contentX = n4;
        this.contentY = n5;
        this.contentWidth = n6;
        this.contentHeight = n7;
        this.maxContentWidth = n8;
        this.maxContentHeight = n9;
        this.useMipmap = bl;
    }

    @Override
    public final PixelFormat getPixelFormat() {
        return this.format;
    }

    @Override
    public final int getPhysicalWidth() {
        return this.physicalWidth;
    }

    @Override
    public final int getPhysicalHeight() {
        return this.physicalHeight;
    }

    @Override
    public final int getContentX() {
        return this.contentX;
    }

    @Override
    public final int getContentY() {
        return this.contentY;
    }

    @Override
    public final int getContentWidth() {
        return this.contentWidth;
    }

    @Override
    public final int getContentHeight() {
        return this.contentHeight;
    }

    @Override
    public int getMaxContentWidth() {
        return this.maxContentWidth;
    }

    @Override
    public int getMaxContentHeight() {
        return this.maxContentHeight;
    }

    @Override
    public void setContentWidth(int n2) {
        if (n2 > this.maxContentWidth) {
            throw new IllegalArgumentException("ContentWidth must be less than or equal to maxContentWidth");
        }
        this.contentWidth = n2;
    }

    @Override
    public void setContentHeight(int n2) {
        if (n2 > this.maxContentHeight) {
            throw new IllegalArgumentException("ContentWidth must be less than or equal to maxContentHeight");
        }
        this.contentHeight = n2;
    }

    @Override
    public final Texture.WrapMode getWrapMode() {
        return this.wrapMode;
    }

    @Override
    public boolean getUseMipmap() {
        return this.useMipmap;
    }

    @Override
    public Texture getSharedTexture(Texture.WrapMode wrapMode) {
        this.assertLocked();
        if (this.wrapMode == wrapMode) {
            this.lock();
            return this;
        }
        switch (wrapMode) {
            case REPEAT: {
                if (this.wrapMode == Texture.WrapMode.CLAMP_TO_EDGE) break;
                return null;
            }
            case CLAMP_TO_EDGE: {
                if (this.wrapMode == Texture.WrapMode.REPEAT) break;
                return null;
            }
            default: {
                return null;
            }
        }
        Texture texture = this.createSharedTexture(wrapMode);
        texture.lock();
        return texture;
    }

    protected abstract Texture createSharedTexture(Texture.WrapMode var1);

    @Override
    public final boolean getLinearFiltering() {
        return this.linearFiltering;
    }

    @Override
    public void setLinearFiltering(boolean bl) {
        this.linearFiltering = bl;
    }

    @Override
    public final int getLastImageSerial() {
        return this.lastImageSerial;
    }

    @Override
    public final void setLastImageSerial(int n2) {
        this.lastImageSerial = n2;
    }

    @Override
    public final void lock() {
        ((ManagedResource)this.resource).lock();
    }

    @Override
    public final boolean isLocked() {
        return ((ManagedResource)this.resource).isLocked();
    }

    @Override
    public final int getLockCount() {
        return ((ManagedResource)this.resource).getLockCount();
    }

    @Override
    public final void assertLocked() {
        ((ManagedResource)this.resource).assertLocked();
    }

    @Override
    public final void unlock() {
        ((ManagedResource)this.resource).unlock();
    }

    @Override
    public final void makePermanent() {
        ((ManagedResource)this.resource).makePermanent();
    }

    @Override
    public final void contentsUseful() {
        ((ManagedResource)this.resource).contentsUseful();
    }

    @Override
    public final void contentsNotUseful() {
        ((ManagedResource)this.resource).contentsNotUseful();
    }

    @Override
    public final boolean isSurfaceLost() {
        return !((ManagedResource)this.resource).isValid();
    }

    @Override
    public final void dispose() {
        ((ManagedResource)this.resource).dispose();
    }

    @Override
    public void update(Image image) {
        this.update(image, 0, 0);
    }

    @Override
    public void update(Image image, int n2, int n3) {
        this.update(image, n2, n3, image.getWidth(), image.getHeight());
    }

    @Override
    public void update(Image image, int n2, int n3, int n4, int n5) {
        this.update(image, n2, n3, n4, n5, false);
    }

    @Override
    public void update(Image image, int n2, int n3, int n4, int n5, boolean bl) {
        Buffer buffer = image.getPixelBuffer();
        int n6 = buffer.position();
        this.update(buffer, image.getPixelFormat(), n2, n3, image.getMinX(), image.getMinY(), n4, n5, image.getScanlineStride(), bl);
        buffer.position(n6);
    }

    protected void checkUpdateParams(Buffer buffer, PixelFormat pixelFormat, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        if (this.format == PixelFormat.MULTI_YCbCr_420) {
            throw new IllegalArgumentException("MULTI_YCbCr_420 requires multitexturing");
        }
        if (buffer == null) {
            throw new IllegalArgumentException("Pixel buffer must be non-null");
        }
        if (pixelFormat != this.format) {
            throw new IllegalArgumentException("Image format (" + (Object)((Object)pixelFormat) + ") " + "must match texture format (" + (Object)((Object)this.format) + ")");
        }
        if (n2 < 0 || n3 < 0) {
            throw new IllegalArgumentException("dstx (" + n2 + ") and dsty (" + n3 + ") must be >= 0");
        }
        if (n4 < 0 || n5 < 0) {
            throw new IllegalArgumentException("srcx (" + n4 + ") and srcy (" + n5 + ") must be >= 0");
        }
        if (n6 <= 0 || n7 <= 0) {
            throw new IllegalArgumentException("srcw (" + n6 + ") and srch (" + n7 + ") must be > 0");
        }
        int n9 = pixelFormat.getBytesPerPixelUnit();
        if (n8 % n9 != 0) {
            throw new IllegalArgumentException("srcscan (" + n8 + ") " + "must be a multiple of the pixel stride (" + n9 + ")");
        }
        if (n6 > n8 / n9) {
            throw new IllegalArgumentException("srcw (" + n6 + ") " + "must be <= srcscan/bytesPerPixel (" + n8 / n9 + ")");
        }
        if (n2 + n6 > this.contentWidth || n3 + n7 > this.contentHeight) {
            throw new IllegalArgumentException("Destination region (x=" + n2 + ", y=" + n3 + ", w=" + n6 + ", h=" + n7 + ") " + "must fit within texture content bounds " + "(contentWidth=" + this.contentWidth + ", contentHeight=" + this.contentHeight + ")");
        }
        int n10 = n4 * n9 + n5 * n8 + (n7 - 1) * n8 + n6 * n9;
        int n11 = n10 / this.format.getDataType().getSizeInBytes();
        if (n11 > buffer.remaining()) {
            throw new IllegalArgumentException("Upload requires " + n11 + " elements, but only " + buffer.remaining() + " elements remain in the buffer");
        }
    }

    public String toString() {
        return super.toString() + " [format=" + (Object)((Object)this.format) + " physicalWidth=" + this.physicalWidth + " physicalHeight=" + this.physicalHeight + " contentX=" + this.contentX + " contentY=" + this.contentY + " contentWidth=" + this.contentWidth + " contentHeight=" + this.contentHeight + " wrapMode=" + (Object)((Object)this.wrapMode) + " linearFiltering=" + this.linearFiltering + "]";
    }
}

