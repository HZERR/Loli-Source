/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import com.sun.javafx.PlatformUtil;
import com.sun.prism.Image;
import com.sun.prism.MediaFrame;
import com.sun.prism.MultiTexture;
import com.sun.prism.PixelFormat;
import com.sun.prism.Texture;
import com.sun.prism.es2.ES2Context;
import com.sun.prism.es2.ES2Pipeline;
import com.sun.prism.es2.ES2TextureData;
import com.sun.prism.es2.ES2TextureResource;
import com.sun.prism.es2.ES2VramPool;
import com.sun.prism.es2.GLContext;
import com.sun.prism.impl.BaseTexture;
import com.sun.prism.impl.BufferUtil;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

class ES2Texture<T extends ES2TextureData>
extends BaseTexture<ES2TextureResource<T>> {
    final ES2Context context;

    ES2Texture(ES2Context eS2Context, ES2TextureResource<T> eS2TextureResource, PixelFormat pixelFormat, Texture.WrapMode wrapMode, int n2, int n3, int n4, int n5, int n6, int n7, boolean bl) {
        super(eS2TextureResource, pixelFormat, wrapMode, n2, n3, n4, n5, n6, n7, bl);
        this.context = eS2Context;
    }

    ES2Texture(ES2Context eS2Context, ES2TextureResource<T> eS2TextureResource, PixelFormat pixelFormat, Texture.WrapMode wrapMode, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9, boolean bl) {
        super(eS2TextureResource, pixelFormat, wrapMode, n2, n3, n4, n5, n6, n7, n8, n9, bl);
        this.context = eS2Context;
    }

    private ES2Texture(ES2Texture eS2Texture, Texture.WrapMode wrapMode) {
        super(eS2Texture, wrapMode, false);
        this.context = eS2Texture.context;
    }

    @Override
    protected Texture createSharedTexture(Texture.WrapMode wrapMode) {
        return new ES2Texture<T>(this, wrapMode);
    }

    static int nextPowerOfTwo(int n2, int n3) {
        int n4;
        if (n2 > n3) {
            return 0;
        }
        for (n4 = 1; n4 < n2; n4 *= 2) {
        }
        return n4;
    }

    static ES2Texture create(ES2Context eS2Context, PixelFormat pixelFormat, Texture.WrapMode wrapMode, int n2, int n3, boolean bl) {
        long l2;
        ES2VramPool eS2VramPool;
        int n4;
        int n5;
        int n6;
        int n7;
        if (!eS2Context.getResourceFactory().isFormatSupported(pixelFormat)) {
            throw new UnsupportedOperationException("Pixel format " + (Object)((Object)pixelFormat) + " not supported on this device");
        }
        if (pixelFormat == PixelFormat.MULTI_YCbCr_420) {
            throw new IllegalArgumentException("Format requires multitexturing: " + (Object)((Object)pixelFormat));
        }
        GLContext gLContext = eS2Context.getGLContext();
        switch (wrapMode) {
            case CLAMP_TO_ZERO: {
                if (gLContext.canClampToZero()) break;
                wrapMode = wrapMode.simulatedVersion();
                break;
            }
            case CLAMP_TO_EDGE: 
            case REPEAT: {
                if (gLContext.canCreateNonPowTwoTextures() || (n2 & n2 - 1) == 0 && (n3 & n3 - 1) == 0) break;
                wrapMode = wrapMode.simulatedVersion();
                break;
            }
            case CLAMP_NOT_NEEDED: {
                break;
            }
            case CLAMP_TO_EDGE_SIMULATED: 
            case CLAMP_TO_ZERO_SIMULATED: 
            case REPEAT_SIMULATED: {
                throw new IllegalArgumentException("Cannot request simulated wrap mode: " + (Object)((Object)wrapMode));
            }
        }
        int n8 = gLContext.getMaxTextureSize();
        int n9 = n2;
        int n10 = n3;
        switch (wrapMode) {
            case CLAMP_TO_ZERO_SIMULATED: {
                n7 = 1;
                n6 = 1;
                n5 = n9 + 2;
                n4 = n10 + 2;
                break;
            }
            case CLAMP_TO_EDGE_SIMULATED: 
            case REPEAT_SIMULATED: {
                n7 = 0;
                n6 = 0;
                n5 = n9;
                n4 = n10;
                if ((n2 & n2 - 1) != 0) {
                    ++n5;
                }
                if ((n3 & n3 - 1) == 0) break;
                ++n4;
                break;
            }
            default: {
                n7 = 0;
                n6 = 0;
                n5 = n9;
                n4 = n10;
            }
        }
        if (n5 > n8 || n4 > n8) {
            throw new RuntimeException("Requested texture dimensions (" + n2 + "x" + n3 + ") " + "require dimensions (" + n5 + "x" + n4 + ") " + "that exceed maximum texture size (" + n8 + ")");
        }
        if (!gLContext.canCreateNonPowTwoTextures()) {
            n5 = ES2Texture.nextPowerOfTwo(n5, n8);
            n4 = ES2Texture.nextPowerOfTwo(n4, n8);
        }
        if (!(eS2VramPool = ES2VramPool.instance).prepareForAllocation(l2 = eS2VramPool.estimateTextureSize(n5, n4, pixelFormat))) {
            return null;
        }
        int n11 = gLContext.getBoundTexture();
        ES2TextureData eS2TextureData = new ES2TextureData(eS2Context, gLContext.genAndBindTexture(), n5, n4, l2);
        ES2TextureResource<ES2TextureData> eS2TextureResource = new ES2TextureResource<ES2TextureData>(eS2TextureData);
        boolean bl2 = ES2Texture.uploadPixels(gLContext, 50, null, pixelFormat, n5, n4, n7, n6, 0, 0, n9, n10, 0, true, bl);
        gLContext.texParamsMinMax(53, bl);
        gLContext.setBoundTexture(n11);
        if (!bl2) {
            return null;
        }
        return new ES2Texture<ES2TextureData>(eS2Context, eS2TextureResource, pixelFormat, wrapMode, n5, n4, n7, n6, n9, n10, bl);
    }

    public static Texture create(ES2Context eS2Context, MediaFrame mediaFrame) {
        long l2;
        ES2VramPool eS2VramPool;
        mediaFrame.holdFrame();
        PixelFormat pixelFormat = mediaFrame.getPixelFormat();
        if (mediaFrame.getPixelFormat() == PixelFormat.MULTI_YCbCr_420) {
            int n2 = mediaFrame.getEncodedWidth();
            int n3 = mediaFrame.getEncodedHeight();
            int n4 = mediaFrame.planeCount();
            MultiTexture multiTexture = new MultiTexture(pixelFormat, Texture.WrapMode.CLAMP_TO_EDGE, mediaFrame.getWidth(), mediaFrame.getHeight());
            for (int i2 = 0; i2 < n4; ++i2) {
                ES2Texture eS2Texture;
                int n5 = n2;
                int n6 = n3;
                if (i2 == 2 || i2 == 1) {
                    n5 /= 2;
                    n6 /= 2;
                }
                if ((eS2Texture = ES2Texture.create(eS2Context, PixelFormat.BYTE_ALPHA, Texture.WrapMode.CLAMP_TO_EDGE, n5, n6, false)) == null) continue;
                multiTexture.setTexture(eS2Texture, i2);
            }
            mediaFrame.releaseFrame();
            return multiTexture;
        }
        GLContext gLContext = eS2Context.getGLContext();
        int n7 = gLContext.getMaxTextureSize();
        int n8 = mediaFrame.getEncodedHeight();
        int n9 = mediaFrame.getEncodedWidth();
        int n10 = n8;
        pixelFormat = mediaFrame.getPixelFormat();
        if (!gLContext.canCreateNonPowTwoTextures()) {
            n9 = ES2Texture.nextPowerOfTwo(n9, n7);
            n10 = ES2Texture.nextPowerOfTwo(n10, n7);
        }
        if (!(eS2VramPool = ES2VramPool.instance).prepareForAllocation(l2 = eS2VramPool.estimateTextureSize(n9, n10, pixelFormat))) {
            return null;
        }
        int n11 = gLContext.getBoundTexture();
        ES2TextureData eS2TextureData = new ES2TextureData(eS2Context, gLContext.genAndBindTexture(), n9, n10, l2);
        ES2TextureResource<ES2TextureData> eS2TextureResource = new ES2TextureResource<ES2TextureData>(eS2TextureData);
        boolean bl = ES2Texture.uploadPixels(eS2Context.getGLContext(), 50, mediaFrame, n9, n10, true);
        gLContext.texParamsMinMax(53, false);
        gLContext.setBoundTexture(n11);
        ES2Texture<ES2TextureData> eS2Texture = null;
        if (bl) {
            eS2Texture = new ES2Texture<ES2TextureData>(eS2Context, eS2TextureResource, pixelFormat, Texture.WrapMode.CLAMP_TO_EDGE, n9, n10, 0, 0, mediaFrame.getWidth(), mediaFrame.getHeight(), false);
        }
        mediaFrame.releaseFrame();
        return eS2Texture;
    }

    private static boolean uploadPixels(GLContext gLContext, int n2, Buffer buffer, PixelFormat pixelFormat, int n3, int n4, int n5, int n6, int n7, int n8, int n9, int n10, int n11, boolean bl, boolean bl2) {
        int n12;
        int n13;
        int n14;
        int n15;
        int n16;
        int n17;
        int n18 = 1;
        boolean bl3 = ES2Pipeline.glFactory.isGL2();
        switch (pixelFormat) {
            case BYTE_BGRA_PRE: 
            case INT_ARGB_PRE: {
                n18 = 4;
                n17 = 40;
                n16 = 41;
                if (!bl3) {
                    if (!PlatformUtil.isIOS()) {
                        if (ES2Pipeline.glFactory.isGLExtensionSupported("GL_EXT_texture_format_BGRA8888")) {
                            n16 = 41;
                            n17 = 41;
                        } else {
                            n16 = 40;
                        }
                    }
                    n15 = 21;
                    break;
                }
                n15 = 22;
                break;
            }
            case BYTE_RGB: {
                n17 = bl3 ? 40 : 42;
                n16 = 42;
                n15 = 21;
                break;
            }
            case BYTE_GRAY: {
                n17 = 43;
                n16 = 43;
                n15 = 21;
                break;
            }
            case BYTE_ALPHA: {
                n17 = 44;
                n16 = 44;
                n15 = 21;
                break;
            }
            case FLOAT_XYZW: {
                n18 = 4;
                n17 = bl3 ? 45 : 40;
                n16 = 40;
                n15 = 20;
                break;
            }
            case BYTE_APPLE_422: {
                n18 = 2;
                n17 = 42;
                n16 = 46;
                n15 = 24;
                break;
            }
            default: {
                throw new InternalError("Image format not supported: " + (Object)((Object)pixelFormat));
            }
        }
        if (!bl3 && n17 != n16 && !PlatformUtil.isIOS()) {
            throw new InternalError("On ES 2.0 device, internalFormat must match pixelFormat");
        }
        boolean bl4 = true;
        if (bl) {
            gLContext.pixelStorei(60, 1);
            if (pixelFormat == PixelFormat.FLOAT_XYZW && n17 == 40) {
                bl4 = gLContext.texImage2D(n2, 0, 40, n3, n4, 0, n16, n15, null, bl2);
            } else {
                if (bl3) {
                    n14 = 44;
                    n13 = 21;
                    n12 = 1;
                } else {
                    n14 = n16;
                    n13 = n15;
                    n12 = pixelFormat.getBytesPerPixelUnit();
                }
                ByteBuffer byteBuffer = null;
                if (n9 != n3 || n10 != n4) {
                    int n19 = n3 * n4 * n12;
                    byteBuffer = BufferUtil.newByteBuffer(n19);
                }
                if (bl3) {
                    gLContext.pixelStorei(61, 0);
                    gLContext.pixelStorei(62, 0);
                    gLContext.pixelStorei(63, 0);
                    gLContext.pixelStorei(60, n18);
                }
                bl4 = gLContext.texImage2D(n2, 0, n17, n3, n4, 0, n14, n13, byteBuffer, bl2);
            }
        }
        if (buffer != null) {
            n14 = n11 / pixelFormat.getBytesPerPixelUnit();
            if (!(bl3 || n7 == 0 && n8 == 0 && n9 == n14)) {
                buffer = Image.createPackedBuffer(buffer, pixelFormat, n7, n8, n9, n10, n11);
                n8 = 0;
                n7 = 0;
                n11 = n9;
                n14 = n11 / pixelFormat.getBytesPerPixelUnit();
            }
            gLContext.pixelStorei(60, n18);
            if (bl3) {
                if (n9 == n14) {
                    gLContext.pixelStorei(61, 0);
                } else {
                    gLContext.pixelStorei(61, n14);
                }
            }
            n13 = buffer.position();
            n12 = ES2Texture.getBufferElementSizeLog(buffer);
            int n20 = pixelFormat.getBytesPerPixelUnit() >> n12;
            buffer.position(n7 * n20 + n8 * (n11 >> n12));
            gLContext.texSubImage2D(n2, 0, n5, n6, n9, n10, n16, n15, buffer);
            buffer.position(n13);
        }
        return bl4;
    }

    private static boolean uploadPixels(GLContext gLContext, int n2, MediaFrame mediaFrame, int n3, int n4, boolean bl) {
        int n5;
        int n6;
        int n7;
        int n8;
        mediaFrame.holdFrame();
        int n9 = 1;
        int n10 = mediaFrame.getEncodedWidth();
        int n11 = n8 = mediaFrame.getEncodedHeight();
        ByteBuffer byteBuffer = mediaFrame.getBufferForPlane(0);
        switch (mediaFrame.getPixelFormat()) {
            case INT_ARGB_PRE: {
                n9 = 4;
                n7 = 40;
                n6 = 41;
                if (byteBuffer.order() == ByteOrder.LITTLE_ENDIAN) {
                    n5 = 22;
                    break;
                }
                n5 = 23;
                break;
            }
            case BYTE_APPLE_422: {
                n9 = 2;
                n7 = 42;
                n6 = 46;
                n5 = 24;
                break;
            }
            default: {
                mediaFrame.releaseFrame();
                throw new InternalError("Invalid video image format " + (Object)((Object)mediaFrame.getPixelFormat()));
            }
        }
        boolean bl2 = true;
        if (bl) {
            gLContext.pixelStorei(60, 1);
            ByteBuffer byteBuffer2 = null;
            if (n10 != n3 || n11 != n4) {
                int n12 = n3 * n4;
                byteBuffer2 = BufferUtil.newByteBuffer(n12);
            }
            bl2 = gLContext.texImage2D(n2, 0, n7, n3, n4, 0, 44, 21, byteBuffer2, false);
        }
        if (byteBuffer != null) {
            gLContext.pixelStorei(60, n9);
            gLContext.pixelStorei(61, mediaFrame.strideForPlane(0) / n9);
            gLContext.texSubImage2D(n2, 0, 0, 0, n10, mediaFrame.getHeight(), n6, n5, byteBuffer);
        }
        mediaFrame.releaseFrame();
        return bl2;
    }

    public static int getBufferElementSizeLog(Buffer buffer) {
        if (buffer instanceof ByteBuffer) {
            return 0;
        }
        if (buffer instanceof IntBuffer || buffer instanceof FloatBuffer) {
            return 2;
        }
        throw new InternalError("Unsupported Buffer type: " + buffer.getClass());
    }

    void updateWrapState() {
        Texture.WrapMode wrapMode = this.getWrapMode();
        ES2TextureData eS2TextureData = (ES2TextureData)((ES2TextureResource)this.resource).getResource();
        if (eS2TextureData.getWrapMode() != wrapMode) {
            int n2;
            GLContext gLContext = this.context.getGLContext();
            int n3 = gLContext.getBoundTexture();
            if (n3 != (n2 = eS2TextureData.getTexID())) {
                gLContext.setBoundTexture(n2);
            }
            gLContext.updateWrapState(n2, wrapMode);
            if (n3 != n2) {
                gLContext.setBoundTexture(n3);
            }
            eS2TextureData.setWrapMode(wrapMode);
        }
    }

    void updateFilterState() {
        boolean bl = this.getLinearFiltering();
        ES2TextureData eS2TextureData = (ES2TextureData)((ES2TextureResource)this.resource).getResource();
        if (eS2TextureData.isFiltered() != bl) {
            int n2;
            GLContext gLContext = this.context.getGLContext();
            int n3 = gLContext.getBoundTexture();
            if (n3 != (n2 = eS2TextureData.getTexID())) {
                gLContext.setBoundTexture(n2);
            }
            gLContext.updateFilterState(n2, bl);
            if (n3 != n2) {
                gLContext.setBoundTexture(n3);
            }
            eS2TextureData.setFiltered(bl);
        }
    }

    public int getNativeSourceHandle() {
        return ((ES2TextureData)((ES2TextureResource)this.resource).getResource()).getTexID();
    }

    @Override
    public void update(Buffer buffer, PixelFormat pixelFormat, int n2, int n3, int n4, int n5, int n6, int n7, int n8, boolean bl) {
        int n9;
        this.checkUpdateParams(buffer, pixelFormat, n2, n3, n4, n5, n6, n7, n8);
        if (!bl) {
            this.context.flushVertexBuffer();
        }
        if ((n9 = this.getNativeSourceHandle()) != 0) {
            int n10;
            GLContext gLContext = this.context.getGLContext();
            int n11 = gLContext.getActiveTextureUnit();
            int n12 = gLContext.getBoundTexture();
            boolean bl2 = false;
            for (n10 = 0; n10 < 2; ++n10) {
                if (gLContext.getBoundTexture(n10) != n9) continue;
                bl2 = true;
                if (n11 == n10) break;
                gLContext.setActiveTextureUnit(n10);
                break;
            }
            if (!bl2) {
                gLContext.setBoundTexture(n9);
            }
            n10 = this.getContentX();
            int n13 = this.getContentY();
            int n14 = this.getContentWidth();
            int n15 = this.getContentHeight();
            int n16 = this.getPhysicalWidth();
            int n17 = this.getPhysicalHeight();
            boolean bl3 = this.getUseMipmap();
            ES2Texture.uploadPixels(gLContext, 50, buffer, pixelFormat, n16, n17, n10 + n2, n13 + n3, n4, n5, n6, n7, n8, false, bl3);
            switch (this.getWrapMode()) {
                case CLAMP_TO_EDGE: {
                    break;
                }
                case CLAMP_TO_EDGE_SIMULATED: {
                    boolean bl4;
                    boolean bl5 = n14 < n16 && n2 + n6 == n14;
                    boolean bl6 = bl4 = n15 < n17 && n3 + n7 == n15;
                    if (bl5) {
                        ES2Texture.uploadPixels(gLContext, 50, buffer, pixelFormat, n16, n17, n10 + n14, n13 + n3, n4 + n6 - 1, n5, 1, n7, n8, false, bl3);
                    }
                    if (!bl4) break;
                    ES2Texture.uploadPixels(gLContext, 50, buffer, pixelFormat, n16, n17, n10 + n2, n13 + n15, n4, n5 + n7 - 1, n6, 1, n8, false, bl3);
                    if (!bl5) break;
                    ES2Texture.uploadPixels(gLContext, 50, buffer, pixelFormat, n16, n17, n10 + n14, n13 + n15, n4 + n6 - 1, n5 + n7 - 1, 1, 1, n8, false, bl3);
                    break;
                }
                case REPEAT: {
                    break;
                }
                case REPEAT_SIMULATED: {
                    boolean bl7;
                    boolean bl8 = n14 < n16 && n2 == 0;
                    boolean bl9 = bl7 = n15 < n17 && n3 == 0;
                    if (bl8) {
                        ES2Texture.uploadPixels(gLContext, 50, buffer, pixelFormat, n16, n17, n10 + n14, n13 + n3, n4, n5, 1, n7, n8, false, bl3);
                    }
                    if (!bl7) break;
                    ES2Texture.uploadPixels(gLContext, 50, buffer, pixelFormat, n16, n17, n10 + n2, n13 + n15, n4, n5, n6, 1, n8, false, bl3);
                    if (!bl8) break;
                    ES2Texture.uploadPixels(gLContext, 50, buffer, pixelFormat, n16, n17, n10 + n14, n13 + n15, n4, n5, 1, 1, n8, false, bl3);
                    break;
                }
            }
            if (n11 != gLContext.getActiveTextureUnit()) {
                gLContext.setActiveTextureUnit(n11);
            }
            if (n12 != gLContext.getBoundTexture()) {
                gLContext.setBoundTexture(n12);
            }
        }
    }

    @Override
    public void update(MediaFrame mediaFrame, boolean bl) {
        int n2;
        if (!bl) {
            this.context.flushVertexBuffer();
        }
        if ((n2 = this.getNativeSourceHandle()) != 0) {
            GLContext gLContext = this.context.getGLContext();
            int n3 = gLContext.getActiveTextureUnit();
            int n4 = gLContext.getBoundTexture();
            boolean bl2 = false;
            for (int i2 = 0; i2 < 2; ++i2) {
                if (gLContext.getBoundTexture(i2) != n2) continue;
                bl2 = true;
                if (n3 == i2) break;
                gLContext.setActiveTextureUnit(i2);
                break;
            }
            if (!bl2) {
                gLContext.setBoundTexture(n2);
            }
            ES2Texture.uploadPixels(gLContext, 50, mediaFrame, this.getPhysicalWidth(), this.getPhysicalHeight(), false);
            if (n3 != gLContext.getActiveTextureUnit()) {
                gLContext.setActiveTextureUnit(n3);
            }
            if (n4 != gLContext.getBoundTexture()) {
                gLContext.setBoundTexture(n4);
            }
        }
    }
}

