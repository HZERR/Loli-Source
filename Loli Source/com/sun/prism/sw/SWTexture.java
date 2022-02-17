/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.sw;

import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.sw.SWArgbPreTexture;
import com.sun.prism.sw.SWMaskTexture;
import com.sun.prism.sw.SWResourceFactory;

abstract class SWTexture
implements Texture {
    boolean allocated = false;
    int physicalWidth;
    int physicalHeight;
    int contentWidth;
    int contentHeight;
    private SWResourceFactory factory;
    private int lastImageSerial;
    private final Texture.WrapMode wrapMode;
    private int lockcount;
    boolean permanent;
    int employcount;

    static Texture create(SWResourceFactory sWResourceFactory, PixelFormat pixelFormat, Texture.WrapMode wrapMode, int n2, int n3) {
        switch (pixelFormat) {
            case BYTE_ALPHA: {
                return new SWMaskTexture(sWResourceFactory, wrapMode, n2, n3);
            }
        }
        return new SWArgbPreTexture(sWResourceFactory, wrapMode, n2, n3);
    }

    SWTexture(SWResourceFactory sWResourceFactory, Texture.WrapMode wrapMode, int n2, int n3) {
        this.factory = sWResourceFactory;
        this.wrapMode = wrapMode;
        this.physicalWidth = n2;
        this.physicalHeight = n3;
        this.contentWidth = n2;
        this.contentHeight = n3;
        this.lock();
    }

    SWTexture(SWTexture sWTexture, Texture.WrapMode wrapMode) {
        this.allocated = sWTexture.allocated;
        this.physicalWidth = sWTexture.physicalWidth;
        this.physicalHeight = sWTexture.physicalHeight;
        this.contentWidth = sWTexture.contentWidth;
        this.contentHeight = sWTexture.contentHeight;
        this.factory = sWTexture.factory;
        this.lastImageSerial = sWTexture.lastImageSerial;
        this.wrapMode = wrapMode;
        this.lock();
    }

    SWResourceFactory getResourceFactory() {
        return this.factory;
    }

    int getOffset() {
        return 0;
    }

    @Override
    public void lock() {
        ++this.lockcount;
    }

    @Override
    public void unlock() {
        this.assertLocked();
        --this.lockcount;
    }

    @Override
    public boolean isLocked() {
        return this.lockcount > 0;
    }

    @Override
    public int getLockCount() {
        return this.lockcount;
    }

    @Override
    public void assertLocked() {
        if (this.lockcount <= 0) {
            throw new IllegalStateException("texture not locked");
        }
    }

    @Override
    public void makePermanent() {
        this.permanent = true;
    }

    @Override
    public void contentsUseful() {
        this.assertLocked();
        ++this.employcount;
    }

    @Override
    public void contentsNotUseful() {
        if (this.employcount <= 0) {
            throw new IllegalStateException("Resource obsoleted too many times");
        }
        --this.employcount;
    }

    @Override
    public boolean isSurfaceLost() {
        return false;
    }

    @Override
    public void dispose() {
    }

    @Override
    public int getPhysicalWidth() {
        return this.physicalWidth;
    }

    @Override
    public int getPhysicalHeight() {
        return this.physicalHeight;
    }

    @Override
    public int getContentX() {
        return 0;
    }

    @Override
    public int getContentY() {
        return 0;
    }

    @Override
    public int getContentWidth() {
        return this.contentWidth;
    }

    @Override
    public void setContentWidth(int n2) {
        if (n2 > this.physicalWidth) {
            throw new IllegalArgumentException("contentWidth cannot exceed physicalWidth");
        }
        this.contentWidth = n2;
    }

    @Override
    public int getContentHeight() {
        return this.contentHeight;
    }

    @Override
    public void setContentHeight(int n2) {
        if (n2 > this.physicalHeight) {
            throw new IllegalArgumentException("contentHeight cannot exceed physicalHeight");
        }
        this.contentHeight = n2;
    }

    @Override
    public int getMaxContentWidth() {
        return this.getPhysicalWidth();
    }

    @Override
    public int getMaxContentHeight() {
        return this.getPhysicalHeight();
    }

    @Override
    public int getLastImageSerial() {
        return this.lastImageSerial;
    }

    @Override
    public void setLastImageSerial(int n2) {
        this.lastImageSerial = n2;
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
        if (PrismSettings.debug) {
            System.out.println("IMG.Bytes per pixel: " + image.getBytesPerPixelUnit());
            System.out.println("IMG.scanline: " + image.getScanlineStride());
        }
        this.update(image.getPixelBuffer(), image.getPixelFormat(), n2, n3, 0, 0, n4, n5, image.getScanlineStride(), bl);
    }

    @Override
    public Texture.WrapMode getWrapMode() {
        return this.wrapMode;
    }

    @Override
    public boolean getUseMipmap() {
        return false;
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
        return this.createSharedLockedTexture(wrapMode);
    }

    @Override
    public boolean getLinearFiltering() {
        return false;
    }

    @Override
    public void setLinearFiltering(boolean bl) {
    }

    void allocate() {
        if (this.allocated) {
            return;
        }
        if (PrismSettings.debug) {
            System.out.println("PCS Texture allocating buffer: " + this + ", " + this.physicalWidth + "x" + this.physicalHeight);
        }
        this.allocateBuffer();
        this.allocated = true;
    }

    abstract void allocateBuffer();

    abstract Texture createSharedLockedTexture(Texture.WrapMode var1);
}

