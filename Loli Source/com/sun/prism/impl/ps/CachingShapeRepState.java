/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.ps;

import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.BasicStroke;
import com.sun.prism.Graphics;
import com.sun.prism.Texture;
import com.sun.prism.impl.Disposer;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.VertexBuffer;
import com.sun.prism.impl.ps.BaseShaderContext;
import com.sun.prism.impl.ps.BaseShaderGraphics;
import com.sun.prism.impl.shape.MaskData;
import com.sun.prism.impl.shape.ShapeUtil;
import com.sun.prism.paint.Paint;
import com.sun.prism.ps.Shader;
import java.util.Arrays;
import java.util.Comparator;

class CachingShapeRepState {
    private static final BaseTransform IDENT = BaseTransform.IDENTITY_TRANSFORM;
    private static final MaskCache maskCache = new MaskCache();
    private int renderCount;
    private Boolean tryCache;
    private BaseTransform lastXform;
    private final MaskTexData texData;
    private float[] bbox;
    private final Object disposerReferent = new Object();
    private final Disposer.Record disposerRecord;

    private static boolean equalsIgnoreTranslation(BaseTransform baseTransform, BaseTransform baseTransform2) {
        if (baseTransform == baseTransform2) {
            return true;
        }
        return baseTransform.getMxx() == baseTransform2.getMxx() && baseTransform.getMxy() == baseTransform2.getMxy() && baseTransform.getMyx() == baseTransform2.getMyx() && baseTransform.getMyy() == baseTransform2.getMyy();
    }

    CachingShapeRepState() {
        this.texData = new MaskTexData();
        this.disposerRecord = new CSRDisposerRecord(this.texData);
        Disposer.addRecord(this.disposerReferent, this.disposerRecord);
    }

    void fillNoCache(Graphics graphics, Shape shape) {
        graphics.fill(shape);
    }

    void drawNoCache(Graphics graphics, Shape shape) {
        graphics.draw(shape);
    }

    void invalidate() {
        this.renderCount = 0;
        this.tryCache = null;
        this.lastXform = null;
        this.bbox = null;
    }

    private void invalidateMaskTexData() {
        this.tryCache = null;
        this.lastXform = null;
        maskCache.unref(this.texData);
    }

    void render(Graphics graphics, Shape shape, RectBounds rectBounds, BasicStroke basicStroke) {
        BaseTransform baseTransform = graphics.getTransformNoClone();
        if (this.lastXform == null || !CachingShapeRepState.equalsIgnoreTranslation(baseTransform, this.lastXform)) {
            this.invalidateMaskTexData();
            if (this.lastXform != null) {
                this.renderCount = 0;
            }
        }
        if (this.texData.cacheEntry != null) {
            this.texData.maskTex.lock();
            if (this.texData.maskTex.isSurfaceLost()) {
                this.texData.maskTex.unlock();
                this.invalidateMaskTexData();
            }
        }
        RectBounds rectBounds2 = null;
        boolean bl = false;
        if (this.tryCache == null) {
            if (baseTransform.isIdentity()) {
                rectBounds2 = rectBounds;
            } else {
                rectBounds2 = new RectBounds();
                bl = true;
                rectBounds2 = (RectBounds)baseTransform.transform(rectBounds, rectBounds2);
            }
            this.tryCache = !rectBounds2.isEmpty() && maskCache.hasRoom(rectBounds2);
        }
        ++this.renderCount;
        if (this.tryCache == Boolean.FALSE || this.renderCount <= 1 || !(graphics instanceof BaseShaderGraphics) || ((BaseShaderGraphics)graphics).isComplexPaint()) {
            if (basicStroke == null) {
                this.fillNoCache(graphics, shape);
            } else {
                this.drawNoCache(graphics, shape);
            }
            return;
        }
        BaseShaderGraphics baseShaderGraphics = (BaseShaderGraphics)graphics;
        BaseShaderContext baseShaderContext = baseShaderGraphics.getContext();
        if (this.lastXform == null || !this.lastXform.equals(baseTransform)) {
            if (rectBounds2 == null) {
                if (baseTransform.isIdentity()) {
                    rectBounds2 = rectBounds;
                } else {
                    rectBounds2 = new RectBounds();
                    bl = true;
                    rectBounds2 = (RectBounds)baseTransform.transform(rectBounds, rectBounds2);
                }
            }
            if (this.texData.cacheEntry != null) {
                this.texData.adjustOrigin(baseTransform);
            } else {
                maskCache.get(baseShaderContext, this.texData, shape, basicStroke, baseTransform, rectBounds2, bl, graphics.isAntialiasedShape());
            }
            if (this.lastXform == null) {
                this.lastXform = baseTransform.copy();
            } else {
                this.lastXform.setTransform(baseTransform);
            }
        }
        Paint paint = baseShaderGraphics.getPaint();
        float f2 = 0.0f;
        float f3 = 0.0f;
        float f4 = 0.0f;
        float f5 = 0.0f;
        if (paint.isProportional()) {
            if (this.bbox == null) {
                this.bbox = new float[]{Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY};
                Shape.accumulate(this.bbox, shape, BaseTransform.IDENTITY_TRANSFORM);
            }
            f2 = this.bbox[0];
            f3 = this.bbox[1];
            f4 = this.bbox[2] - f2;
            f5 = this.bbox[3] - f3;
        }
        int n2 = this.texData.maskW;
        int n3 = this.texData.maskH;
        Texture texture = this.texData.maskTex;
        float f6 = texture.getPhysicalWidth();
        float f7 = texture.getPhysicalHeight();
        float f8 = this.texData.maskX;
        float f9 = this.texData.maskY;
        float f10 = f8 + (float)n2;
        float f11 = f9 + (float)n3;
        float f12 = (float)texture.getContentX() / f6;
        float f13 = (float)texture.getContentY() / f7;
        float f14 = f12 + (float)n2 / f6;
        float f15 = f13 + (float)n3 / f7;
        if (PrismSettings.primTextureSize != 0) {
            Shader shader = baseShaderContext.validatePaintOp(baseShaderGraphics, IDENT, BaseShaderContext.MaskType.ALPHA_TEXTURE, this.texData.maskTex, f2, f3, f4, f5);
            VertexBuffer vertexBuffer = baseShaderContext.getVertexBuffer();
            vertexBuffer.addQuad(f8, f9, f10, f11, f12, f13, f14, f15, baseShaderGraphics.getPaintTextureTx(baseTransform, shader, f2, f3, f4, f5));
        } else {
            baseShaderContext.validatePaintOp(baseShaderGraphics, IDENT, this.texData.maskTex, f2, f3, f4, f5);
            VertexBuffer vertexBuffer = baseShaderContext.getVertexBuffer();
            vertexBuffer.addQuad(f8, f9, f10, f11, f12, f13, f14, f15);
        }
        texture.unlock();
    }

    void dispose() {
        this.invalidate();
    }

    private static class CSRDisposerRecord
    implements Disposer.Record {
        private MaskTexData texData;

        private CSRDisposerRecord(MaskTexData maskTexData) {
            this.texData = maskTexData;
        }

        @Override
        public void dispose() {
            if (this.texData != null) {
                maskCache.unref(this.texData);
                this.texData = null;
            }
        }
    }

    private static class MaskCache {
        private static final int MAX_MASK_DIM = 512;
        private static final int MAX_SIZE_IN_PIXELS = 0x400000;
        private static Comparator<CacheEntry> comparator = (cacheEntry, cacheEntry2) -> {
            int n2 = Float.compare(cacheEntry.xformBounds.getWidth(), cacheEntry2.xformBounds.getWidth());
            if (n2 != 0) {
                return n2;
            }
            return Float.compare(cacheEntry.xformBounds.getHeight(), cacheEntry2.xformBounds.getHeight());
        };
        private CacheEntry[] entries = new CacheEntry[8];
        private int entriesSize = 0;
        private int totalPixels;
        private CacheEntry tmpKey = new CacheEntry();

        private MaskCache() {
            this.tmpKey.xformBounds = new RectBounds();
        }

        private void ensureSize(int n2) {
            if (this.entries.length < n2) {
                CacheEntry[] arrcacheEntry = new CacheEntry[n2 * 3 / 2];
                System.arraycopy(this.entries, 0, arrcacheEntry, 0, this.entries.length);
                this.entries = arrcacheEntry;
            }
        }

        private void addEntry(CacheEntry cacheEntry) {
            this.ensureSize(this.entriesSize + 1);
            int n2 = Arrays.binarySearch(this.entries, 0, this.entriesSize, cacheEntry, comparator);
            if (n2 < 0) {
                n2 ^= 0xFFFFFFFF;
            }
            System.arraycopy(this.entries, n2, this.entries, n2 + 1, this.entriesSize - n2);
            this.entries[n2] = cacheEntry;
            ++this.entriesSize;
        }

        private void removeEntry(CacheEntry cacheEntry) {
            int n2 = Arrays.binarySearch(this.entries, 0, this.entriesSize, cacheEntry, comparator);
            if (n2 < 0) {
                throw new IllegalStateException("Trying to remove a cached item that's not in the cache");
            }
            if (this.entries[n2] != cacheEntry) {
                this.tmpKey.xformBounds.deriveWithNewBounds(0.0f, 0.0f, 0.0f, cacheEntry.xformBounds.getWidth(), Math.nextAfter(cacheEntry.xformBounds.getHeight(), Double.NEGATIVE_INFINITY), 0.0f);
                n2 = Arrays.binarySearch(this.entries, 0, this.entriesSize, this.tmpKey, comparator);
                if (n2 < 0) {
                    n2 ^= 0xFFFFFFFF;
                }
                this.tmpKey.xformBounds.deriveWithNewBounds(0.0f, 0.0f, 0.0f, cacheEntry.xformBounds.getWidth(), Math.nextAfter(cacheEntry.xformBounds.getHeight(), Double.POSITIVE_INFINITY), 0.0f);
                int n3 = Arrays.binarySearch(this.entries, 0, this.entriesSize, this.tmpKey, comparator);
                if (n3 < 0) {
                    n3 ^= 0xFFFFFFFF;
                }
                while (this.entries[n2] != cacheEntry && n2 < n3) {
                    ++n2;
                }
                if (n2 >= n3) {
                    throw new IllegalStateException("Trying to remove a cached item that's not in the cache");
                }
            }
            System.arraycopy(this.entries, n2 + 1, this.entries, n2, this.entriesSize - n2 - 1);
            --this.entriesSize;
        }

        boolean hasRoom(RectBounds rectBounds) {
            int n2 = (int)(rectBounds.getWidth() + 0.5f);
            int n3 = (int)(rectBounds.getHeight() + 0.5f);
            int n4 = n2 * n3;
            return n2 <= 512 && n3 <= 512 && this.totalPixels + n4 <= 0x400000;
        }

        boolean entryMatches(CacheEntry cacheEntry, Shape shape, BasicStroke basicStroke, BaseTransform baseTransform, boolean bl) {
            return cacheEntry.antialiasedShape == bl && CachingShapeRepState.equalsIgnoreTranslation(baseTransform, cacheEntry.xform) && cacheEntry.shape.equals(shape) && (basicStroke == null ? cacheEntry.stroke == null : basicStroke.equals(cacheEntry.stroke));
        }

        void get(BaseShaderContext baseShaderContext, MaskTexData maskTexData, Shape shape, BasicStroke basicStroke, BaseTransform baseTransform, RectBounds rectBounds, boolean bl, boolean bl2) {
            Object object;
            if (maskTexData == null) {
                throw new InternalError("MaskTexData must be non-null");
            }
            if (maskTexData.cacheEntry != null) {
                throw new InternalError("CacheEntry should already be null");
            }
            this.tmpKey.xformBounds.deriveWithNewBounds(0.0f, 0.0f, 0.0f, rectBounds.getWidth(), Math.nextAfter(rectBounds.getHeight(), Double.NEGATIVE_INFINITY), 0.0f);
            int n2 = Arrays.binarySearch(this.entries, 0, this.entriesSize, this.tmpKey, comparator);
            if (n2 < 0) {
                n2 ^= 0xFFFFFFFF;
            }
            this.tmpKey.xformBounds.deriveWithNewBounds(0.0f, 0.0f, 0.0f, rectBounds.getWidth(), Math.nextAfter(rectBounds.getHeight(), Double.POSITIVE_INFINITY), 0.0f);
            int n3 = Arrays.binarySearch(this.entries, 0, this.entriesSize, this.tmpKey, comparator);
            if (n3 < 0) {
                n3 ^= 0xFFFFFFFF;
            }
            while (n2 < n3) {
                object = this.entries[n2];
                if (this.entryMatches((CacheEntry)object, shape, basicStroke, baseTransform, bl2)) {
                    ((CacheEntry)object).texData.maskTex.lock();
                    if (((CacheEntry)object).texData.maskTex.isSurfaceLost()) {
                        ((CacheEntry)object).texData.maskTex.unlock();
                    } else {
                        ++((CacheEntry)object).refCount;
                        ((CacheEntry)object).texData.copyInto(maskTexData);
                        maskTexData.cacheEntry = (CacheEntry)object;
                        maskTexData.adjustOrigin(baseTransform);
                        return;
                    }
                }
                ++n2;
            }
            object = ShapeUtil.rasterizeShape(shape, basicStroke, rectBounds, baseTransform, true, bl2);
            int n4 = ((MaskData)object).getWidth();
            int n5 = ((MaskData)object).getHeight();
            maskTexData.maskX = ((MaskData)object).getOriginX();
            maskTexData.maskY = ((MaskData)object).getOriginY();
            maskTexData.maskW = n4;
            maskTexData.maskH = n5;
            maskTexData.maskTex = baseShaderContext.getResourceFactory().createMaskTexture(n4, n5, Texture.WrapMode.CLAMP_TO_ZERO);
            ((MaskData)object).uploadToTexture(maskTexData.maskTex, 0, 0, false);
            maskTexData.maskTex.contentsUseful();
            CacheEntry cacheEntry = new CacheEntry();
            cacheEntry.shape = shape.copy();
            if (basicStroke != null) {
                cacheEntry.stroke = basicStroke.copy();
            }
            cacheEntry.xform = baseTransform.copy();
            cacheEntry.xformBounds = bl ? rectBounds : (RectBounds)rectBounds.copy();
            cacheEntry.texData = maskTexData.copy();
            cacheEntry.antialiasedShape = bl2;
            cacheEntry.refCount = 1;
            maskTexData.cacheEntry = cacheEntry;
            this.addEntry(cacheEntry);
            this.totalPixels += n4 * n5;
        }

        void unref(MaskTexData maskTexData) {
            if (maskTexData == null) {
                throw new InternalError("MaskTexData must be non-null");
            }
            CacheEntry cacheEntry = maskTexData.cacheEntry;
            if (cacheEntry == null) {
                return;
            }
            maskTexData.cacheEntry = null;
            maskTexData.maskTex = null;
            --cacheEntry.refCount;
            if (cacheEntry.refCount <= 0) {
                this.removeEntry(cacheEntry);
                cacheEntry.shape = null;
                cacheEntry.stroke = null;
                cacheEntry.xform = null;
                cacheEntry.xformBounds = null;
                cacheEntry.texData.maskTex.dispose();
                cacheEntry.antialiasedShape = false;
                cacheEntry.texData = null;
                this.totalPixels -= maskTexData.maskW * maskTexData.maskH;
            }
        }
    }

    private static class CacheEntry {
        Shape shape;
        BasicStroke stroke;
        BaseTransform xform;
        RectBounds xformBounds;
        MaskTexData texData;
        boolean antialiasedShape;
        int refCount;

        private CacheEntry() {
        }
    }

    private static class MaskTexData {
        private CacheEntry cacheEntry;
        private Texture maskTex;
        private float maskX;
        private float maskY;
        private int maskW;
        private int maskH;

        private MaskTexData() {
        }

        void adjustOrigin(BaseTransform baseTransform) {
            float f2 = (float)(baseTransform.getMxt() - this.cacheEntry.xform.getMxt());
            float f3 = (float)(baseTransform.getMyt() - this.cacheEntry.xform.getMyt());
            this.maskX = this.cacheEntry.texData.maskX + f2;
            this.maskY = this.cacheEntry.texData.maskY + f3;
        }

        MaskTexData copy() {
            MaskTexData maskTexData = new MaskTexData();
            maskTexData.cacheEntry = this.cacheEntry;
            maskTexData.maskTex = this.maskTex;
            maskTexData.maskX = this.maskX;
            maskTexData.maskY = this.maskY;
            maskTexData.maskW = this.maskW;
            maskTexData.maskH = this.maskH;
            return maskTexData;
        }

        void copyInto(MaskTexData maskTexData) {
            if (maskTexData == null) {
                throw new InternalError("MaskTexData must be non-null");
            }
            maskTexData.cacheEntry = this.cacheEntry;
            maskTexData.maskTex = this.maskTex;
            maskTexData.maskX = this.maskX;
            maskTexData.maskY = this.maskY;
            maskTexData.maskW = this.maskW;
            maskTexData.maskH = this.maskH;
        }
    }
}

