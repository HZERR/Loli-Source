/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl;

import com.sun.glass.ui.Screen;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.impl.ByteGray;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.prism.PixelFormat;
import com.sun.prism.RTTexture;
import com.sun.prism.RenderTarget;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.BaseGraphics;
import com.sun.prism.impl.GlyphCache;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.VertexBuffer;
import com.sun.prism.impl.paint.PaintUtil;
import com.sun.prism.impl.shape.MaskData;
import com.sun.prism.paint.Gradient;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class BaseContext {
    private final Screen screen;
    private final ResourceFactory factory;
    private final VertexBuffer vertexBuffer;
    private static final int MIN_MASK_DIM = 1024;
    private Texture maskTex;
    private ByteBuffer maskBuffer;
    private ByteBuffer clearBuffer;
    private int curMaskRow;
    private int nextMaskRow;
    private int curMaskCol;
    private int highMaskCol;
    private Texture paintTex;
    private int[] paintPixels;
    private ByteBuffer paintBuffer;
    private Texture rectTex;
    private int rectTexMax;
    private Texture wrapRectTex;
    private Texture ovalTex;
    private final Map<FontStrike, GlyphCache> greyGlyphCaches = new HashMap<FontStrike, GlyphCache>();
    private final Map<FontStrike, GlyphCache> lcdGlyphCaches = new HashMap<FontStrike, GlyphCache>();

    protected BaseContext(Screen screen, ResourceFactory resourceFactory, int n2) {
        this.screen = screen;
        this.factory = resourceFactory;
        this.vertexBuffer = new VertexBuffer(this, n2);
    }

    protected void setDeviceParametersFor2D() {
    }

    protected void setDeviceParametersFor3D() {
    }

    public Screen getAssociatedScreen() {
        return this.screen;
    }

    public ResourceFactory getResourceFactory() {
        return this.factory;
    }

    public VertexBuffer getVertexBuffer() {
        return this.vertexBuffer;
    }

    public void flushVertexBuffer() {
        this.vertexBuffer.flush();
    }

    protected final void flushMask() {
        if (this.curMaskRow > 0 || this.curMaskCol > 0) {
            this.maskTex.lock();
            this.maskTex.update(this.maskBuffer, this.maskTex.getPixelFormat(), 0, 0, 0, 0, this.highMaskCol, this.nextMaskRow, this.maskTex.getPhysicalWidth(), true);
            this.maskTex.unlock();
            this.highMaskCol = 0;
            this.nextMaskRow = 0;
            this.curMaskCol = 0;
            this.curMaskRow = 0;
        }
    }

    public void drawQuads(float[] arrf, byte[] arrby, int n2) {
        this.flushMask();
        this.renderQuads(arrf, arrby, n2);
    }

    protected abstract void renderQuads(float[] var1, byte[] var2, int var3);

    public void setRenderTarget(BaseGraphics baseGraphics) {
        if (baseGraphics != null) {
            this.setRenderTarget(baseGraphics.getRenderTarget(), baseGraphics.getCameraNoClone(), baseGraphics.isDepthTest() && baseGraphics.isDepthBuffer(), baseGraphics.isState3D());
        } else {
            this.releaseRenderTarget();
        }
    }

    protected void releaseRenderTarget() {
    }

    protected abstract void setRenderTarget(RenderTarget var1, NGCamera var2, boolean var3, boolean var4);

    public abstract void validateClearOp(BaseGraphics var1);

    public abstract void validatePaintOp(BaseGraphics var1, BaseTransform var2, Texture var3, float var4, float var5, float var6, float var7);

    public abstract void validateTextureOp(BaseGraphics var1, BaseTransform var2, Texture var3, PixelFormat var4);

    public void clearGlyphCaches() {
        this.clearCaches(this.greyGlyphCaches);
        this.clearCaches(this.lcdGlyphCaches);
    }

    private void clearCaches(Map<FontStrike, GlyphCache> map) {
        Iterator<Object> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            iterator.next().clearDesc();
        }
        for (GlyphCache glyphCache : map.values()) {
            if (glyphCache == null) continue;
            glyphCache.clear();
        }
        map.clear();
    }

    public abstract RTTexture getLCDBuffer();

    public GlyphCache getGlyphCache(FontStrike fontStrike) {
        Map<FontStrike, GlyphCache> map = fontStrike.getAAMode() == 1 ? this.lcdGlyphCaches : this.greyGlyphCaches;
        return this.getGlyphCache(fontStrike, map);
    }

    public boolean isSuperShaderEnabled() {
        return false;
    }

    private GlyphCache getGlyphCache(FontStrike fontStrike, Map<FontStrike, GlyphCache> map) {
        GlyphCache glyphCache = map.get(fontStrike);
        if (glyphCache == null) {
            glyphCache = new GlyphCache(this, fontStrike);
            map.put(fontStrike, glyphCache);
        }
        return glyphCache;
    }

    public Texture validateMaskTexture(MaskData maskData, boolean bl) {
        int n2 = bl ? 1 : 0;
        int n3 = maskData.getWidth() + n2 + n2;
        int n4 = maskData.getHeight() + n2 + n2;
        int n5 = 0;
        int n6 = 0;
        if (this.maskTex != null) {
            this.maskTex.lock();
            if (this.maskTex.isSurfaceLost()) {
                this.maskTex = null;
            } else {
                n5 = this.maskTex.getContentWidth();
                n6 = this.maskTex.getContentHeight();
            }
        }
        if (this.maskTex == null || n5 < n3 || n6 < n4) {
            if (this.maskTex != null) {
                this.flushVertexBuffer();
                this.maskTex.dispose();
                this.maskTex = null;
            }
            this.maskBuffer = null;
            int n7 = Math.max(1024, Math.max(n3, n5));
            int n8 = Math.max(1024, Math.max(n4, n6));
            this.maskTex = this.getResourceFactory().createMaskTexture(n7, n8, Texture.WrapMode.CLAMP_NOT_NEEDED);
            this.maskBuffer = ByteBuffer.allocate(n7 * n8);
            if (this.clearBuffer == null || this.clearBuffer.capacity() < n7) {
                this.clearBuffer = null;
                this.clearBuffer = ByteBuffer.allocate(n7);
            }
            this.highMaskCol = 0;
            this.nextMaskRow = 0;
            this.curMaskCol = 0;
            this.curMaskRow = 0;
        }
        return this.maskTex;
    }

    public void updateMaskTexture(MaskData maskData, RectBounds rectBounds, boolean bl) {
        this.maskTex.assertLocked();
        int n2 = maskData.getWidth();
        int n3 = maskData.getHeight();
        int n4 = this.maskTex.getContentWidth();
        int n5 = this.maskTex.getContentHeight();
        int n6 = bl ? 1 : 0;
        int n7 = n2 + n6 + n6;
        int n8 = n3 + n6 + n6;
        if (this.curMaskCol + n7 > n4) {
            this.curMaskCol = 0;
            this.curMaskRow = this.nextMaskRow;
        }
        if (this.curMaskRow + n8 > n5) {
            this.flushVertexBuffer();
        }
        int n9 = this.curMaskRow * n4 + this.curMaskCol;
        ByteToBytePixelConverter byteToBytePixelConverter = ByteGray.ToByteGrayConverter();
        if (bl) {
            int n10 = n9;
            byteToBytePixelConverter.convert(this.clearBuffer, 0, 0, this.maskBuffer, n10, n4, n2 + 1, 1);
            n10 = n9 + n2 + 1;
            byteToBytePixelConverter.convert(this.clearBuffer, 0, 0, this.maskBuffer, n10, n4, 1, n3 + 1);
            n10 = n9 + n4;
            byteToBytePixelConverter.convert(this.clearBuffer, 0, 0, this.maskBuffer, n10, n4, 1, n3 + 1);
            n10 = n9 + (n3 + 1) * n4 + 1;
            byteToBytePixelConverter.convert(this.clearBuffer, 0, 0, this.maskBuffer, n10, n4, n2 + 1, 1);
            n9 += n4 + 1;
        }
        byteToBytePixelConverter.convert(maskData.getMaskBuffer(), 0, n2, this.maskBuffer, n9, n4, n2, n3);
        float f2 = this.maskTex.getPhysicalWidth();
        float f3 = this.maskTex.getPhysicalHeight();
        rectBounds.setMinX((float)(this.curMaskCol + n6) / f2);
        rectBounds.setMinY((float)(this.curMaskRow + n6) / f3);
        rectBounds.setMaxX((float)(this.curMaskCol + n6 + n2) / f2);
        rectBounds.setMaxY((float)(this.curMaskRow + n6 + n3) / f3);
        this.curMaskCol += n7;
        if (this.highMaskCol < this.curMaskCol) {
            this.highMaskCol = this.curMaskCol;
        }
        if (this.nextMaskRow < this.curMaskRow + n8) {
            this.nextMaskRow = this.curMaskRow + n8;
        }
    }

    public int getRectTextureMaxSize() {
        if (this.rectTex == null) {
            this.createRectTexture();
        }
        return this.rectTexMax;
    }

    public Texture getRectTexture() {
        if (this.rectTex == null) {
            this.createRectTexture();
        }
        this.rectTex.lock();
        return this.rectTex;
    }

    private void createRectTexture() {
        int n2;
        int n3 = PrismSettings.primTextureSize;
        if (n3 < 0) {
            n3 = this.getResourceFactory().getMaximumTextureSize();
        }
        int n4 = 3;
        int n5 = 2;
        while (n4 + n5 + 1 <= n3) {
            this.rectTexMax = n5++;
            n4 += n5;
        }
        byte[] arrby = new byte[n4 * n4];
        int n6 = 1;
        for (int i2 = 1; i2 <= this.rectTexMax; ++i2) {
            int n7 = 1;
            for (n2 = 1; n2 <= this.rectTexMax; ++n2) {
                int n8 = n6 * n4 + n7;
                for (int i3 = 0; i3 < i2; ++i3) {
                    for (int i4 = 0; i4 < n2; ++i4) {
                        arrby[n8 + i4] = -1;
                    }
                    n8 += n4;
                }
                n7 += n2 + 1;
            }
            n6 += i2 + 1;
        }
        if (PrismSettings.verbose) {
            System.out.println("max rectangle texture cell size = " + this.rectTexMax);
        }
        Texture texture = this.getResourceFactory().createMaskTexture(n4, n4, Texture.WrapMode.CLAMP_NOT_NEEDED);
        texture.contentsUseful();
        texture.makePermanent();
        PixelFormat pixelFormat = texture.getPixelFormat();
        n2 = n4 * pixelFormat.getBytesPerPixelUnit();
        texture.update(ByteBuffer.wrap(arrby), pixelFormat, 0, 0, 0, 0, n4, n4, n2, false);
        this.rectTex = texture;
    }

    public Texture getWrapRectTexture() {
        if (this.wrapRectTex == null) {
            int n2;
            Texture texture = this.getResourceFactory().createMaskTexture(2, 2, Texture.WrapMode.CLAMP_TO_EDGE);
            texture.contentsUseful();
            texture.makePermanent();
            int n3 = texture.getPhysicalWidth();
            int n4 = texture.getPhysicalHeight();
            if (PrismSettings.verbose) {
                System.out.println("wrap rectangle texture = " + n3 + " x " + n4);
            }
            byte[] arrby = new byte[n3 * n4];
            int n5 = n3;
            for (int i2 = 1; i2 < n4; ++i2) {
                for (n2 = 1; n2 < n4; ++n2) {
                    arrby[n5 + n2] = -1;
                }
                n5 += n3;
            }
            PixelFormat pixelFormat = texture.getPixelFormat();
            n2 = n3 * pixelFormat.getBytesPerPixelUnit();
            texture.update(ByteBuffer.wrap(arrby), pixelFormat, 0, 0, 0, 0, n3, n4, n2, false);
            this.wrapRectTex = texture;
        }
        this.wrapRectTex.lock();
        return this.wrapRectTex;
    }

    public Texture getOvalTexture() {
        if (this.ovalTex == null) {
            int n2;
            int n3 = this.getRectTextureMaxSize();
            int n4 = n3 * (n3 + 1) / 2;
            byte[] arrby = new byte[(n4 += n3 + 1) * n4];
            int n5 = 1;
            for (int i2 = 1; i2 <= n3; ++i2) {
                int n6 = 1;
                for (n2 = 1; n2 <= n3; ++n2) {
                    int n7 = n5 * n4 + n6;
                    for (int i3 = 0; i3 < i2; ++i3) {
                        int n8;
                        int n9;
                        if (i3 * 2 >= i2) {
                            int n10 = i2 - 1 - i3;
                            n9 = n7 + (n10 - i3) * n4;
                            for (n8 = 0; n8 < n2; ++n8) {
                                arrby[n7 + n8] = arrby[n9 + n8];
                            }
                        } else {
                            float f2 = (float)i3 + 0.0625f;
                            for (n9 = 0; n9 < 8; ++n9) {
                                float f3 = f2 / (float)i2 - 0.5f;
                                f3 = (float)Math.sqrt(0.25f - f3 * f3);
                                int n11 = Math.round((float)n2 * 4.0f * (1.0f - f3 * 2.0f));
                                int n12 = n11 >> 3;
                                int n13 = n11 & 7;
                                int n14 = n7 + n12;
                                arrby[n14] = (byte)(arrby[n14] + (8 - n13));
                                int n15 = n7 + n12 + 1;
                                arrby[n15] = (byte)(arrby[n15] + n13);
                                f2 += 0.125f;
                            }
                            n9 = 0;
                            for (n8 = 0; n8 < n2; ++n8) {
                                arrby[n7 + n8] = n8 * 2 >= n2 ? arrby[n7 + n2 - 1 - n8] : (byte)(((n9 += arrby[n7 + n8]) * 255 + 32) / 64);
                            }
                            arrby[n7 + n2] = 0;
                        }
                        n7 += n4;
                    }
                    n6 += n2 + 1;
                }
                n5 += i2 + 1;
            }
            Texture texture = this.getResourceFactory().createMaskTexture(n4, n4, Texture.WrapMode.CLAMP_NOT_NEEDED);
            texture.contentsUseful();
            texture.makePermanent();
            PixelFormat pixelFormat = texture.getPixelFormat();
            n2 = n4 * pixelFormat.getBytesPerPixelUnit();
            texture.update(ByteBuffer.wrap(arrby), pixelFormat, 0, 0, 0, 0, n4, n4, n2, false);
            this.ovalTex = texture;
        }
        this.ovalTex.lock();
        return this.ovalTex;
    }

    public Texture getGradientTexture(Gradient gradient, BaseTransform baseTransform, int n2, int n3, MaskData maskData, float f2, float f3, float f4, float f5) {
        int n4;
        int n5 = n2 * n3;
        int n6 = n5 * 4;
        if (this.paintBuffer == null || this.paintBuffer.capacity() < n6) {
            this.paintPixels = new int[n5];
            this.paintBuffer = ByteBuffer.wrap(new byte[n6]);
        }
        if (this.paintTex != null) {
            this.paintTex.lock();
            if (this.paintTex.isSurfaceLost()) {
                this.paintTex = null;
            }
        }
        if (this.paintTex == null || this.paintTex.getContentWidth() < n2 || this.paintTex.getContentHeight() < n3) {
            int n7 = n2;
            n4 = n3;
            if (this.paintTex != null) {
                n7 = Math.max(n2, this.paintTex.getContentWidth());
                n4 = Math.max(n3, this.paintTex.getContentHeight());
                this.paintTex.dispose();
            }
            this.paintTex = this.getResourceFactory().createTexture(PixelFormat.BYTE_BGRA_PRE, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_NOT_NEEDED, n7, n4);
        }
        PaintUtil.fillImageWithGradient(this.paintPixels, gradient, baseTransform, 0, 0, n2, n3, f2, f3, f4, f5);
        byte[] arrby = this.paintBuffer.array();
        if (maskData != null) {
            byte[] arrby2 = maskData.getMaskBuffer().array();
            int n8 = 0;
            for (int i2 = 0; i2 < n5; ++i2) {
                int n9 = this.paintPixels[i2];
                int n10 = arrby2[i2] & 0xFF;
                arrby[n8++] = (byte)((n9 & 0xFF) * n10 / 255);
                arrby[n8++] = (byte)((n9 >> 8 & 0xFF) * n10 / 255);
                arrby[n8++] = (byte)((n9 >> 16 & 0xFF) * n10 / 255);
                arrby[n8++] = (byte)((n9 >>> 24) * n10 / 255);
            }
        } else {
            n4 = 0;
            for (int i3 = 0; i3 < n5; ++i3) {
                int n11 = this.paintPixels[i3];
                arrby[n4++] = (byte)(n11 & 0xFF);
                arrby[n4++] = (byte)(n11 >> 8 & 0xFF);
                arrby[n4++] = (byte)(n11 >> 16 & 0xFF);
                arrby[n4++] = (byte)(n11 >>> 24);
            }
        }
        this.paintTex.update(this.paintBuffer, PixelFormat.BYTE_BGRA_PRE, 0, 0, 0, 0, n2, n3, n2 * 4, false);
        return this.paintTex;
    }
}

