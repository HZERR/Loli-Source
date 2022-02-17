/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import com.sun.glass.ui.Screen;
import com.sun.prism.Graphics;
import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import com.sun.prism.RTTexture;
import com.sun.prism.ReadbackRenderTarget;
import com.sun.prism.Texture;
import com.sun.prism.es2.ES2Context;
import com.sun.prism.es2.ES2Graphics;
import com.sun.prism.es2.ES2RTTextureData;
import com.sun.prism.es2.ES2RenderTarget;
import com.sun.prism.es2.ES2Texture;
import com.sun.prism.es2.ES2TextureResource;
import com.sun.prism.es2.ES2VramPool;
import com.sun.prism.es2.GLContext;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.PrismTrace;
import java.nio.Buffer;

class ES2RTTexture
extends ES2Texture<ES2RTTextureData>
implements ES2RenderTarget,
RTTexture,
ReadbackRenderTarget {
    private boolean opaque;

    private ES2RTTexture(ES2Context eS2Context, ES2TextureResource<ES2RTTextureData> eS2TextureResource, Texture.WrapMode wrapMode, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9) {
        super(eS2Context, eS2TextureResource, PixelFormat.BYTE_BGRA_PRE, wrapMode, n2, n3, n4, n5, n6, n7, n8, n9, false);
        PrismTrace.rttCreated((long)((ES2RTTextureData)eS2TextureResource.getResource()).getFboID(), n2, n3, PixelFormat.BYTE_BGRA_PRE.getBytesPerPixelUnit());
        this.opaque = false;
    }

    void attachDepthBuffer(ES2Context eS2Context) {
        ES2RTTextureData eS2RTTextureData = (ES2RTTextureData)((ES2TextureResource)this.resource).getResource();
        int n2 = eS2RTTextureData.getDepthBufferID();
        if (n2 != 0) {
            return;
        }
        int n3 = this.isMSAA() ? eS2Context.getGLContext().getSampleSize() : 0;
        n2 = eS2Context.getGLContext().createDepthBuffer(this.getPhysicalWidth(), this.getPhysicalHeight(), n3);
        eS2RTTextureData.setDepthBufferID(n2);
    }

    private void createAndAttachMSAABuffer(ES2Context eS2Context) {
        ES2RTTextureData eS2RTTextureData = (ES2RTTextureData)((ES2TextureResource)this.resource).getResource();
        int n2 = eS2RTTextureData.getMSAARenderBufferID();
        if (n2 != 0) {
            return;
        }
        GLContext gLContext = eS2Context.getGLContext();
        n2 = gLContext.createRenderBuffer(this.getPhysicalWidth(), this.getPhysicalHeight(), gLContext.getSampleSize());
        eS2RTTextureData.setMSAARenderBufferID(n2);
    }

    static int getCompatibleDimension(ES2Context eS2Context, int n2, Texture.WrapMode wrapMode) {
        boolean bl;
        GLContext gLContext = eS2Context.getGLContext();
        switch (wrapMode) {
            case CLAMP_NOT_NEEDED: {
                bl = false;
                break;
            }
            case CLAMP_TO_ZERO: {
                bl = !gLContext.canClampToZero();
                break;
            }
            default: {
                throw new IllegalArgumentException("wrap mode not supported for RT textures: " + (Object)((Object)wrapMode));
            }
            case CLAMP_TO_EDGE_SIMULATED: 
            case CLAMP_TO_ZERO_SIMULATED: 
            case REPEAT_SIMULATED: {
                throw new IllegalArgumentException("Cannot request simulated wrap mode: " + (Object)((Object)wrapMode));
            }
        }
        int n3 = bl ? n2 + 2 : n2;
        int n4 = gLContext.getMaxTextureSize();
        int n5 = gLContext.canCreateNonPowTwoTextures() ? (n3 <= n4 ? n3 : 0) : ES2RTTexture.nextPowerOfTwo(n3, n4);
        if (n5 == 0) {
            throw new RuntimeException("Requested texture dimension (" + n2 + ") " + "requires dimension (" + n5 + ") " + "that exceeds maximum texture size (" + n4 + ")");
        }
        n5 = Math.max(n5, PrismSettings.minRTTSize);
        return bl ? n5 - 2 : n5;
    }

    static ES2RTTexture create(ES2Context eS2Context, int n2, int n3, Texture.WrapMode wrapMode, boolean bl) {
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        int n9;
        int n10;
        int n11;
        int n12;
        int n13;
        boolean bl2;
        GLContext gLContext = eS2Context.getGLContext();
        switch (wrapMode) {
            case CLAMP_NOT_NEEDED: {
                bl2 = false;
                break;
            }
            case CLAMP_TO_ZERO: {
                bl2 = !gLContext.canClampToZero();
                break;
            }
            default: {
                throw new IllegalArgumentException("wrap mode not supported for RT textures: " + (Object)((Object)wrapMode));
            }
            case CLAMP_TO_EDGE_SIMULATED: 
            case CLAMP_TO_ZERO_SIMULATED: 
            case REPEAT_SIMULATED: {
                throw new IllegalArgumentException("Cannot request simulated wrap mode: " + (Object)((Object)wrapMode));
            }
        }
        if (bl2) {
            n13 = 1;
            n12 = 1;
            n11 = n2 + 2;
            n10 = n3 + 2;
            wrapMode = wrapMode.simulatedVersion();
        } else {
            n13 = 0;
            n12 = 0;
            n11 = n2;
            n10 = n3;
        }
        int n14 = gLContext.getMaxTextureSize();
        if (gLContext.canCreateNonPowTwoTextures()) {
            n9 = n11 <= n14 ? n11 : 0;
            n8 = n10 <= n14 ? n10 : 0;
        } else {
            n9 = ES2RTTexture.nextPowerOfTwo(n11, n14);
            n8 = ES2RTTexture.nextPowerOfTwo(n10, n14);
        }
        if (n9 == 0 || n8 == 0) {
            throw new RuntimeException("Requested texture dimensions (" + n2 + "x" + n3 + ") " + "require dimensions (" + n9 + "x" + n8 + ") " + "that exceed maximum texture size (" + n14 + ")");
        }
        ES2VramPool eS2VramPool = ES2VramPool.instance;
        int n15 = PrismSettings.minRTTSize;
        long l2 = eS2VramPool.estimateRTTextureSize(n9 = Math.max(n9, n15), n8 = Math.max(n8, n15), false);
        if (!eS2VramPool.prepareForAllocation(l2)) {
            return null;
        }
        if (bl2) {
            n7 = n9 - 2;
            n6 = n8 - 2;
            n5 = n2;
            n4 = n3;
        } else {
            n7 = n9;
            n6 = n8;
            n5 = n2;
            n4 = n3;
        }
        gLContext.setActiveTextureUnit(0);
        int n16 = gLContext.getBoundFBO();
        int n17 = gLContext.getBoundTexture();
        int n18 = 0;
        if (!bl) {
            n18 = gLContext.createTexture(n9, n8);
        }
        int n19 = 0;
        if ((n18 != 0 || bl) && (n19 = gLContext.createFBO(n18)) == 0) {
            gLContext.deleteTexture(n18);
            n18 = 0;
        }
        ES2RTTextureData eS2RTTextureData = new ES2RTTextureData(eS2Context, n18, n19, n9, n8, l2);
        ES2TextureResource<ES2RTTextureData> eS2TextureResource = new ES2TextureResource<ES2RTTextureData>(eS2RTTextureData);
        ES2RTTexture eS2RTTexture = new ES2RTTexture(eS2Context, eS2TextureResource, wrapMode, n9, n8, n13, n12, n5, n4, n7, n6);
        if (bl) {
            eS2RTTexture.createAndAttachMSAABuffer(eS2Context);
        }
        gLContext.bindFBO(n16);
        gLContext.setBoundTexture(n17);
        return eS2RTTexture;
    }

    @Override
    public Texture getBackBuffer() {
        return this;
    }

    @Override
    public Graphics createGraphics() {
        return ES2Graphics.create(this.context, this);
    }

    @Override
    public int[] getPixels() {
        return null;
    }

    @Override
    public boolean readPixels(Buffer buffer, int n2, int n3, int n4, int n5) {
        boolean bl;
        this.context.flushVertexBuffer();
        GLContext gLContext = this.context.getGLContext();
        int n6 = gLContext.getBoundFBO();
        int n7 = this.getFboID();
        boolean bl2 = bl = n6 != n7;
        if (bl) {
            gLContext.bindFBO(n7);
        }
        boolean bl3 = gLContext.readPixels(buffer, n2, n3, n4, n5);
        if (bl) {
            gLContext.bindFBO(n6);
        }
        return bl3;
    }

    @Override
    public boolean readPixels(Buffer buffer) {
        return this.readPixels(buffer, this.getContentX(), this.getContentY(), this.getContentWidth(), this.getContentHeight());
    }

    @Override
    public int getFboID() {
        return ((ES2RTTextureData)((ES2TextureResource)this.resource).getResource()).getFboID();
    }

    @Override
    public Screen getAssociatedScreen() {
        return this.context.getAssociatedScreen();
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
        return ((ES2RTTextureData)((ES2TextureResource)this.resource).getResource()).getMSAARenderBufferID() != 0;
    }
}

