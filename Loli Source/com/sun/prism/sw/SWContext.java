/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.sw;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.openpisces.Renderer;
import com.sun.pisces.PiscesRenderer;
import com.sun.prism.BasicStroke;
import com.sun.prism.PixelFormat;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.shape.MaskData;
import com.sun.prism.impl.shape.OpenPiscesPrismUtils;
import com.sun.prism.impl.shape.ShapeUtil;
import com.sun.prism.sw.DirectRTPiscesAlphaConsumer;
import com.sun.prism.sw.SWArgbPreTexture;
import com.sun.prism.sw.SWMaskTexture;
import com.sun.prism.sw.SWRTTexture;
import java.lang.ref.SoftReference;

final class SWContext {
    private final ResourceFactory factory;
    private final ShapeRenderer shapeRenderer;
    private SoftReference<SWRTTexture> readBackBufferRef;
    private SoftReference<SWArgbPreTexture> imagePaintTextureRef;

    SWContext(ResourceFactory resourceFactory) {
        this.factory = resourceFactory;
        this.shapeRenderer = PrismSettings.doNativePisces ? new NativeShapeRenderer() : new JavaShapeRenderer();
    }

    void renderShape(PiscesRenderer piscesRenderer, Shape shape, BasicStroke basicStroke, BaseTransform baseTransform, Rectangle rectangle, boolean bl) {
        this.shapeRenderer.renderShape(piscesRenderer, shape, basicStroke, baseTransform, rectangle, bl);
    }

    private SWRTTexture initRBBuffer(int n2, int n3) {
        SWRTTexture sWRTTexture = (SWRTTexture)this.factory.createRTTexture(n2, n3, Texture.WrapMode.CLAMP_NOT_NEEDED);
        this.readBackBufferRef = new SoftReference<SWRTTexture>(sWRTTexture);
        return sWRTTexture;
    }

    private void disposeRBBuffer() {
        if (this.readBackBufferRef != null) {
            this.readBackBufferRef.clear();
            this.readBackBufferRef = null;
        }
    }

    SWRTTexture validateRBBuffer(int n2, int n3) {
        SWRTTexture sWRTTexture;
        if (this.readBackBufferRef == null) {
            sWRTTexture = this.initRBBuffer(n2, n3);
        } else {
            sWRTTexture = this.readBackBufferRef.get();
            if (sWRTTexture == null || sWRTTexture.getPhysicalWidth() < n2 || sWRTTexture.getPhysicalHeight() < n3) {
                this.disposeRBBuffer();
                sWRTTexture = this.initRBBuffer(n2, n3);
            }
            sWRTTexture.setContentWidth(n2);
            sWRTTexture.setContentHeight(n3);
        }
        return sWRTTexture;
    }

    private SWArgbPreTexture initImagePaintTexture(int n2, int n3) {
        SWArgbPreTexture sWArgbPreTexture = (SWArgbPreTexture)this.factory.createTexture(PixelFormat.INT_ARGB_PRE, Texture.Usage.DEFAULT, Texture.WrapMode.REPEAT, n2, n3);
        this.imagePaintTextureRef = new SoftReference<SWArgbPreTexture>(sWArgbPreTexture);
        return sWArgbPreTexture;
    }

    private void disposeImagePaintTexture() {
        if (this.imagePaintTextureRef != null) {
            this.imagePaintTextureRef.clear();
            this.imagePaintTextureRef = null;
        }
    }

    SWArgbPreTexture validateImagePaintTexture(int n2, int n3) {
        SWArgbPreTexture sWArgbPreTexture;
        if (this.imagePaintTextureRef == null) {
            sWArgbPreTexture = this.initImagePaintTexture(n2, n3);
        } else {
            sWArgbPreTexture = this.imagePaintTextureRef.get();
            if (sWArgbPreTexture == null || sWArgbPreTexture.getPhysicalWidth() < n2 || sWArgbPreTexture.getPhysicalHeight() < n3) {
                this.disposeImagePaintTexture();
                sWArgbPreTexture = this.initImagePaintTexture(n2, n3);
            }
            sWArgbPreTexture.setContentWidth(n2);
            sWArgbPreTexture.setContentHeight(n3);
        }
        return sWArgbPreTexture;
    }

    void dispose() {
        this.disposeRBBuffer();
        this.disposeImagePaintTexture();
        this.shapeRenderer.dispose();
    }

    class JavaShapeRenderer
    implements ShapeRenderer {
        private final DirectRTPiscesAlphaConsumer alphaConsumer = new DirectRTPiscesAlphaConsumer();

        JavaShapeRenderer() {
        }

        @Override
        public void renderShape(PiscesRenderer piscesRenderer, Shape shape, BasicStroke basicStroke, BaseTransform baseTransform, Rectangle rectangle, boolean bl) {
            if (basicStroke != null && basicStroke.getType() != 0) {
                shape = basicStroke.createStrokedShape(shape);
                basicStroke = null;
            }
            Renderer renderer = OpenPiscesPrismUtils.setupRenderer(shape, basicStroke, baseTransform, rectangle, bl);
            this.alphaConsumer.initConsumer(renderer, piscesRenderer);
            renderer.produceAlphas(this.alphaConsumer);
        }

        @Override
        public void dispose() {
        }
    }

    class NativeShapeRenderer
    implements ShapeRenderer {
        private SoftReference<SWMaskTexture> maskTextureRef;

        NativeShapeRenderer() {
        }

        @Override
        public void renderShape(PiscesRenderer piscesRenderer, Shape shape, BasicStroke basicStroke, BaseTransform baseTransform, Rectangle rectangle, boolean bl) {
            MaskData maskData = ShapeUtil.rasterizeShape(shape, basicStroke, rectangle.toRectBounds(), baseTransform, true, bl);
            SWMaskTexture sWMaskTexture = this.validateMaskTexture(maskData.getWidth(), maskData.getHeight());
            maskData.uploadToTexture(sWMaskTexture, 0, 0, false);
            piscesRenderer.fillAlphaMask(sWMaskTexture.getDataNoClone(), maskData.getOriginX(), maskData.getOriginY(), maskData.getWidth(), maskData.getHeight(), 0, sWMaskTexture.getPhysicalWidth());
        }

        private SWMaskTexture initMaskTexture(int n2, int n3) {
            SWMaskTexture sWMaskTexture = (SWMaskTexture)SWContext.this.factory.createMaskTexture(n2, n3, Texture.WrapMode.CLAMP_NOT_NEEDED);
            this.maskTextureRef = new SoftReference<SWMaskTexture>(sWMaskTexture);
            return sWMaskTexture;
        }

        private void disposeMaskTexture() {
            if (this.maskTextureRef != null) {
                this.maskTextureRef.clear();
                this.maskTextureRef = null;
            }
        }

        private SWMaskTexture validateMaskTexture(int n2, int n3) {
            SWMaskTexture sWMaskTexture;
            if (this.maskTextureRef == null) {
                sWMaskTexture = this.initMaskTexture(n2, n3);
            } else {
                sWMaskTexture = this.maskTextureRef.get();
                if (sWMaskTexture == null || sWMaskTexture.getPhysicalWidth() < n2 || sWMaskTexture.getPhysicalHeight() < n3) {
                    this.disposeMaskTexture();
                    sWMaskTexture = this.initMaskTexture(n2, n3);
                }
            }
            return sWMaskTexture;
        }

        @Override
        public void dispose() {
            this.disposeMaskTexture();
        }
    }

    static interface ShapeRenderer {
        public void renderShape(PiscesRenderer var1, Shape var2, BasicStroke var3, BaseTransform var4, Rectangle var5, boolean var6);

        public void dispose();
    }
}

