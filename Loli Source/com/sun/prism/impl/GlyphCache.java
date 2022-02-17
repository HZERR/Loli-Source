/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl;

import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.Glyph;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.logging.PulseLogger;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.BaseContext;
import com.sun.prism.impl.BufferUtil;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.VertexBuffer;
import com.sun.prism.impl.packrect.RectanglePacker;
import com.sun.prism.impl.shape.MaskData;
import com.sun.prism.paint.Color;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.WeakHashMap;

public class GlyphCache {
    private static final int WIDTH = PrismSettings.glyphCacheWidth;
    private static final int HEIGHT = PrismSettings.glyphCacheHeight;
    private static ByteBuffer emptyMask;
    private final BaseContext context;
    private final FontStrike strike;
    private static final int SEGSHIFT = 5;
    private static final int SEGSIZE = 32;
    HashMap<Integer, GlyphData[]> glyphDataMap = new HashMap();
    private static final int SUBPIXEL_SHIFT = 27;
    private RectanglePacker packer;
    private boolean isLCDCache;
    static WeakHashMap<BaseContext, RectanglePacker> greyPackerMap;
    static WeakHashMap<BaseContext, RectanglePacker> lcdPackerMap;

    public GlyphCache(BaseContext baseContext, FontStrike fontStrike) {
        this.context = baseContext;
        this.strike = fontStrike;
        this.isLCDCache = fontStrike.getAAMode() == 1;
        WeakHashMap<BaseContext, RectanglePacker> weakHashMap = this.isLCDCache ? lcdPackerMap : greyPackerMap;
        this.packer = weakHashMap.get(baseContext);
        if (this.packer == null) {
            ResourceFactory resourceFactory = baseContext.getResourceFactory();
            Texture texture = resourceFactory.createMaskTexture(WIDTH, HEIGHT, Texture.WrapMode.CLAMP_NOT_NEEDED);
            texture.contentsUseful();
            texture.makePermanent();
            if (!this.isLCDCache) {
                resourceFactory.setGlyphTexture(texture);
            }
            texture.setLinearFiltering(false);
            this.packer = new RectanglePacker(texture, WIDTH, HEIGHT);
            weakHashMap.put(baseContext, this.packer);
        }
    }

    public void render(BaseContext baseContext, GlyphList glyphList, float f2, float f3, int n2, int n3, Color color, Color color2, BaseTransform baseTransform, BaseBounds baseBounds) {
        int n4;
        int n5;
        if (this.isLCDCache) {
            n5 = baseContext.getLCDBuffer().getPhysicalWidth();
            n4 = baseContext.getLCDBuffer().getPhysicalHeight();
        } else {
            n5 = 1;
            n4 = 1;
        }
        Texture texture = this.getBackingStore();
        VertexBuffer vertexBuffer = baseContext.getVertexBuffer();
        int n6 = glyphList.getGlyphCount();
        Color color3 = null;
        Point2D point2D = new Point2D();
        for (int i2 = 0; i2 < n6; ++i2) {
            int n7 = glyphList.getGlyphCode(i2);
            if ((n7 & 0xFFFFFF) == 65535) continue;
            point2D.setLocation(f2 + glyphList.getPosX(i2), f3 + glyphList.getPosY(i2));
            int n8 = this.strike.getQuantizedPosition(point2D);
            GlyphData glyphData = this.getCachedGlyph(n7, n8);
            if (glyphData == null) continue;
            if (baseBounds != null) {
                if (f2 + glyphList.getPosX(i2) > baseBounds.getMaxX()) break;
                if (f2 + glyphList.getPosX(i2 + 1) < baseBounds.getMinX()) continue;
            }
            if (color != null && color2 != null) {
                int n9 = glyphList.getCharOffset(i2);
                if (n2 <= n9 && n9 < n3) {
                    if (color != color3) {
                        vertexBuffer.setPerVertexColor(color, 1.0f);
                        color3 = color;
                    }
                } else if (color2 != color3) {
                    vertexBuffer.setPerVertexColor(color2, 1.0f);
                    color3 = color2;
                }
            }
            baseTransform.transform(point2D, point2D);
            this.addDataToQuad(glyphData, vertexBuffer, texture, point2D.x, point2D.y, n5, n4);
        }
    }

    private void addDataToQuad(GlyphData glyphData, VertexBuffer vertexBuffer, Texture texture, float f2, float f3, float f4, float f5) {
        f3 = Math.round(f3);
        Rectangle rectangle = glyphData.getRect();
        if (rectangle == null) {
            return;
        }
        int n2 = glyphData.getBlankBoundary();
        float f6 = rectangle.width - n2 * 2;
        float f7 = rectangle.height - n2 * 2;
        float f8 = (float)glyphData.getOriginX() + f2;
        float f9 = (float)glyphData.getOriginY() + f3;
        float f10 = f9 + f7;
        float f11 = texture.getPhysicalWidth();
        float f12 = texture.getPhysicalHeight();
        float f13 = (float)(rectangle.x + n2) / f11;
        float f14 = (float)(rectangle.y + n2) / f12;
        float f15 = f13 + f6 / f11;
        float f16 = f14 + f7 / f12;
        if (this.isLCDCache) {
            f8 = (float)Math.round(f8 * 3.0f) / 3.0f;
            float f17 = f8 + f6 / 3.0f;
            float f18 = f8 / f4;
            float f19 = f17 / f4;
            float f20 = f9 / f5;
            float f21 = f10 / f5;
            vertexBuffer.addQuad(f8, f9, f17, f10, f13, f14, f15, f16, f18, f20, f19, f21);
        } else {
            f8 = Math.round(f8);
            float f22 = f8 + f6;
            if (this.context.isSuperShaderEnabled()) {
                vertexBuffer.addSuperQuad(f8, f9, f22, f10, f13, f14, f15, f16, true);
            } else {
                vertexBuffer.addQuad(f8, f9, f22, f10, f13, f14, f15, f16);
            }
        }
    }

    public Texture getBackingStore() {
        return this.packer.getBackingStore();
    }

    public void clear() {
        this.glyphDataMap.clear();
    }

    private void clearAll() {
        this.context.flushVertexBuffer();
        this.context.clearGlyphCaches();
        this.packer.clear();
    }

    private GlyphData getCachedGlyph(int n2, int n3) {
        int n4 = n2 >> 5;
        int n5 = n2 % 32;
        GlyphData[] arrglyphData = this.glyphDataMap.get(n4 |= n3 << 27);
        if (arrglyphData != null) {
            if (arrglyphData[n5] != null) {
                return arrglyphData[n5];
            }
        } else {
            arrglyphData = new GlyphData[32];
            this.glyphDataMap.put(n4, arrglyphData);
        }
        GlyphData glyphData = null;
        Glyph glyph = this.strike.getGlyph(n2);
        if (glyph != null) {
            byte[] arrby = glyph.getPixelData(n3);
            if (arrby == null || arrby.length == 0) {
                glyphData = new GlyphData(0, 0, 0, glyph.getPixelXAdvance(), glyph.getPixelYAdvance(), null);
            } else {
                MaskData maskData = MaskData.create(arrby, glyph.getOriginX(), glyph.getOriginY(), glyph.getWidth(), glyph.getHeight());
                int n6 = 1;
                int n7 = maskData.getWidth() + 2 * n6;
                int n8 = maskData.getHeight() + 2 * n6;
                int n9 = maskData.getOriginX();
                int n10 = maskData.getOriginY();
                Rectangle rectangle = new Rectangle(0, 0, n7, n8);
                glyphData = new GlyphData(n9, n10, n6, glyph.getPixelXAdvance(), glyph.getPixelYAdvance(), rectangle);
                if (!this.packer.add(rectangle)) {
                    if (PulseLogger.PULSE_LOGGING_ENABLED) {
                        PulseLogger.incrementCounter("Font Glyph Cache Cleared");
                    }
                    this.clearAll();
                    this.packer.add(rectangle);
                }
                boolean bl = true;
                Texture texture = this.getBackingStore();
                int n11 = rectangle.width;
                int n12 = rectangle.height;
                int n13 = texture.getPixelFormat().getBytesPerPixelUnit();
                int n14 = n11 * n13;
                int n15 = n14 * n12;
                if (emptyMask == null || n15 > emptyMask.capacity()) {
                    emptyMask = BufferUtil.newByteBuffer(n15);
                }
                try {
                    texture.update(emptyMask, texture.getPixelFormat(), rectangle.x, rectangle.y, 0, 0, n11, n12, n14, bl);
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                    return null;
                }
                maskData.uploadToTexture(texture, n6 + rectangle.x, n6 + rectangle.y, bl);
            }
            arrglyphData[n5] = glyphData;
        }
        return glyphData;
    }

    static {
        greyPackerMap = new WeakHashMap();
        lcdPackerMap = new WeakHashMap();
    }

    static class GlyphData {
        private final int originX;
        private final int originY;
        private final int blankBoundary;
        private final float xAdvance;
        private final float yAdvance;
        private final Rectangle rect;

        GlyphData(int n2, int n3, int n4, float f2, float f3, Rectangle rectangle) {
            this.originX = n2;
            this.originY = n3;
            this.blankBoundary = n4;
            this.xAdvance = f2;
            this.yAdvance = f3;
            this.rect = rectangle;
        }

        int getOriginX() {
            return this.originX;
        }

        int getOriginY() {
            return this.originY;
        }

        int getBlankBoundary() {
            return this.blankBoundary;
        }

        float getXAdvance() {
            return this.xAdvance;
        }

        float getYAdvance() {
            return this.yAdvance;
        }

        Rectangle getRect() {
            return this.rect;
        }
    }
}

