/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism;

import com.sun.prism.Image;
import com.sun.prism.MediaFrame;
import com.sun.prism.PixelFormat;
import com.sun.prism.Texture;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public final class MultiTexture
implements Texture {
    private int width;
    private int height;
    private PixelFormat format;
    private Texture.WrapMode wrapMode;
    private boolean linearFiltering = true;
    private final ArrayList<Texture> textures;
    private int lastImageSerial;

    public MultiTexture(PixelFormat pixelFormat, Texture.WrapMode wrapMode, int n2, int n3) {
        this.width = n2;
        this.height = n3;
        this.format = pixelFormat;
        this.wrapMode = wrapMode;
        this.textures = new ArrayList(4);
    }

    private MultiTexture(MultiTexture multiTexture, Texture.WrapMode wrapMode) {
        this(multiTexture.format, wrapMode, multiTexture.width, multiTexture.height);
        for (int i2 = 0; i2 < multiTexture.textureCount(); ++i2) {
            Texture texture = multiTexture.getTexture(i2);
            this.setTexture(texture.getSharedTexture(wrapMode), i2);
        }
        this.linearFiltering = multiTexture.linearFiltering;
        this.lastImageSerial = multiTexture.lastImageSerial;
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
        MultiTexture multiTexture = new MultiTexture(this, wrapMode);
        multiTexture.lock();
        return multiTexture;
    }

    public int textureCount() {
        return this.textures.size();
    }

    public void setTexture(Texture texture, int n2) {
        if (!texture.getWrapMode().isCompatibleWith(this.wrapMode)) {
            throw new IllegalArgumentException("texture wrap mode must match multi-texture mode");
        }
        if (this.textures.size() < n2 + 1) {
            for (int i2 = this.textures.size(); i2 < n2; ++i2) {
                this.textures.add(null);
            }
            this.textures.add(texture);
        } else {
            this.textures.set(n2, texture);
        }
        texture.setLinearFiltering(this.linearFiltering);
    }

    public Texture getTexture(int n2) {
        return this.textures.get(n2);
    }

    public Texture[] getTextures() {
        return this.textures.toArray(new Texture[this.textures.size()]);
    }

    public void removeTexture(Texture texture) {
        this.textures.remove(texture);
    }

    public void removeTexture(int n2) {
        this.textures.remove(n2);
    }

    @Override
    public PixelFormat getPixelFormat() {
        return this.format;
    }

    @Override
    public int getPhysicalWidth() {
        return this.width;
    }

    @Override
    public int getPhysicalHeight() {
        return this.height;
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
        return this.width;
    }

    @Override
    public int getContentHeight() {
        return this.height;
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
        throw new UnsupportedOperationException("Update from Image not supported");
    }

    @Override
    public void update(Image image, int n2, int n3) {
        throw new UnsupportedOperationException("Update from Image not supported");
    }

    @Override
    public void update(Image image, int n2, int n3, int n4, int n5) {
        throw new UnsupportedOperationException("Update from Image not supported");
    }

    @Override
    public void update(Image image, int n2, int n3, int n4, int n5, boolean bl) {
        throw new UnsupportedOperationException("Update from Image not supported");
    }

    @Override
    public void update(Buffer buffer, PixelFormat pixelFormat, int n2, int n3, int n4, int n5, int n6, int n7, int n8, boolean bl) {
        throw new UnsupportedOperationException("Update from generic Buffer not supported");
    }

    @Override
    public void update(MediaFrame mediaFrame, boolean bl) {
        if (mediaFrame.getPixelFormat() == PixelFormat.MULTI_YCbCr_420) {
            int n2 = mediaFrame.getEncodedWidth();
            int n3 = mediaFrame.getEncodedHeight();
            for (int i2 = 0; i2 < mediaFrame.planeCount(); ++i2) {
                Texture texture = this.textures.get(i2);
                if (null == texture) continue;
                int n4 = n2;
                int n5 = n3;
                if (i2 == 2 || i2 == 1) {
                    n4 /= 2;
                    n5 /= 2;
                }
                ByteBuffer byteBuffer = mediaFrame.getBufferForPlane(i2);
                texture.update(byteBuffer, PixelFormat.BYTE_ALPHA, 0, 0, 0, 0, n4, n5, mediaFrame.strideForPlane(i2), bl);
            }
        } else {
            throw new IllegalArgumentException("Invalid pixel format in MediaFrame");
        }
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
    public boolean getLinearFiltering() {
        return this.linearFiltering;
    }

    @Override
    public void setLinearFiltering(boolean bl) {
        this.linearFiltering = bl;
        for (Texture texture : this.textures) {
            texture.setLinearFiltering(bl);
        }
    }

    @Override
    public void lock() {
        for (Texture texture : this.textures) {
            texture.lock();
        }
    }

    @Override
    public void unlock() {
        for (Texture texture : this.textures) {
            texture.unlock();
        }
    }

    @Override
    public boolean isLocked() {
        for (Texture texture : this.textures) {
            if (!texture.isLocked()) continue;
            return true;
        }
        return false;
    }

    @Override
    public int getLockCount() {
        int n2 = 0;
        for (Texture texture : this.textures) {
            n2 = Math.max(n2, texture.getLockCount());
        }
        return n2;
    }

    @Override
    public void assertLocked() {
        for (Texture texture : this.textures) {
            texture.assertLocked();
        }
    }

    @Override
    public void makePermanent() {
        for (Texture texture : this.textures) {
            texture.makePermanent();
        }
    }

    @Override
    public void contentsUseful() {
        for (Texture texture : this.textures) {
            texture.contentsUseful();
        }
    }

    @Override
    public void contentsNotUseful() {
        for (Texture texture : this.textures) {
            texture.contentsNotUseful();
        }
    }

    @Override
    public boolean isSurfaceLost() {
        for (Texture texture : this.textures) {
            if (!texture.isSurfaceLost()) continue;
            return true;
        }
        return false;
    }

    @Override
    public void dispose() {
        for (Texture texture : this.textures) {
            texture.dispose();
        }
        this.textures.clear();
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
    public void setContentWidth(int n2) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void setContentHeight(int n2) {
        throw new UnsupportedOperationException("Not supported.");
    }
}

