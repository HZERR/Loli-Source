/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.prism.ps;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Texture;
import com.sun.prism.ps.Shader;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.FloatMap;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.PrTexture;
import com.sun.scenario.effect.impl.prism.ps.PPSDrawable;
import com.sun.scenario.effect.impl.prism.ps.PPSEffectPeer;
import com.sun.scenario.effect.impl.prism.ps.PPSRenderer;

public abstract class PPSTwoSamplerPeer
extends PPSEffectPeer {
    private Shader shader;

    protected PPSTwoSamplerPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    @Override
    public void dispose() {
        if (this.shader != null) {
            this.shader.dispose();
        }
    }

    @Override
    ImageData filterImpl(ImageData ... arrimageData) {
        int n2;
        Object object;
        Rectangle rectangle;
        PrTexture prTexture;
        Object object2;
        Rectangle rectangle2 = this.getDestBounds();
        int n3 = rectangle2.width;
        int n4 = rectangle2.height;
        PPSRenderer pPSRenderer = this.getRenderer();
        PPSDrawable pPSDrawable = pPSRenderer.getCompatibleImage(n3, n4);
        if (pPSDrawable == null) {
            pPSRenderer.markLost();
            return new ImageData(this.getFilterContext(), pPSDrawable, rectangle2);
        }
        this.setDestNativeBounds(pPSDrawable.getPhysicalWidth(), pPSDrawable.getPhysicalHeight());
        Filterable filterable = arrimageData[0].getUntransformedImage();
        PrTexture prTexture2 = (PrTexture)((Object)filterable);
        Rectangle rectangle3 = arrimageData[0].getUntransformedBounds();
        BaseTransform baseTransform = arrimageData[0].getTransform();
        this.setInputBounds(0, rectangle3);
        this.setInputTransform(0, baseTransform);
        this.setInputNativeBounds(0, prTexture2.getNativeBounds());
        float[] arrf = new float[8];
        if (arrimageData.length > 1) {
            object2 = arrimageData[1].getUntransformedImage();
            prTexture = (PrTexture)object2;
            if (prTexture == null) {
                pPSRenderer.markLost();
                return new ImageData(this.getFilterContext(), pPSDrawable, rectangle2);
            }
            rectangle = arrimageData[1].getUntransformedBounds();
            object = arrimageData[1].getTransform();
            this.setInputBounds(1, rectangle);
            this.setInputTransform(1, (BaseTransform)object);
            this.setInputNativeBounds(1, prTexture.getNativeBounds());
            n2 = this.getTextureCoordinates(1, arrf, rectangle.x, rectangle.y, object2.getPhysicalWidth(), object2.getPhysicalHeight(), rectangle2, (BaseTransform)object);
        } else {
            object2 = (FloatMap)this.getSamplerData(1);
            prTexture = (PrTexture)((FloatMap)object2).getAccelData(this.getFilterContext());
            if (prTexture == null) {
                pPSRenderer.markLost();
                return new ImageData(this.getFilterContext(), pPSDrawable, rectangle2);
            }
            rectangle = new Rectangle(((FloatMap)object2).getWidth(), ((FloatMap)object2).getHeight());
            object = prTexture.getNativeBounds();
            this.setInputBounds(1, rectangle);
            this.setInputNativeBounds(1, (Rectangle)object);
            arrf[1] = 0.0f;
            arrf[0] = 0.0f;
            arrf[2] = (float)rectangle.width / (float)((Rectangle)object).width;
            arrf[3] = (float)rectangle.height / (float)((Rectangle)object).height;
            n2 = 4;
        }
        object2 = new float[8];
        int n5 = this.getTextureCoordinates(0, (float[])object2, rectangle3.x, rectangle3.y, filterable.getPhysicalWidth(), filterable.getPhysicalHeight(), rectangle2, baseTransform);
        object = pPSDrawable.createGraphics();
        if (object == null) {
            pPSRenderer.markLost();
            return new ImageData(this.getFilterContext(), pPSDrawable, rectangle2);
        }
        if (this.shader == null) {
            this.shader = this.createShader();
        }
        if (this.shader == null || !this.shader.isValid()) {
            pPSRenderer.markLost();
            return new ImageData(this.getFilterContext(), pPSDrawable, rectangle2);
        }
        object.setExternalShader(this.shader);
        this.updateShader(this.shader);
        float f2 = 0.0f;
        float f3 = 0.0f;
        float f4 = n3;
        float f5 = n4;
        Object t2 = prTexture2.getTextureObject();
        if (t2 == null) {
            pPSRenderer.markLost();
            return new ImageData(this.getFilterContext(), pPSDrawable, rectangle2);
        }
        Object t3 = prTexture.getTextureObject();
        if (t3 == null) {
            pPSRenderer.markLost();
            return new ImageData(this.getFilterContext(), pPSDrawable, rectangle2);
        }
        float f6 = (float)t2.getContentX() / (float)t2.getPhysicalWidth();
        float f7 = (float)t2.getContentY() / (float)t2.getPhysicalHeight();
        float f8 = f6 + object2[0];
        float f9 = f7 + object2[1];
        float f10 = f6 + object2[2];
        float f11 = f7 + object2[3];
        float f12 = (float)t3.getContentX() / (float)t3.getPhysicalWidth();
        float f13 = (float)t3.getContentY() / (float)t3.getPhysicalHeight();
        float f14 = f12 + arrf[0];
        float f15 = f13 + arrf[1];
        float f16 = f12 + arrf[2];
        float f17 = f13 + arrf[3];
        if (n5 < 8 && n2 < 8) {
            object.drawTextureRaw2((Texture)t2, (Texture)t3, f2, f3, f4, f5, f8, f9, f10, f11, f14, f15, f16, f17);
        } else {
            float f18;
            float f19;
            float f20;
            float f21;
            float f22;
            float f23;
            float f24;
            float f25;
            if (n5 < 8) {
                f25 = f10;
                f24 = f9;
                f23 = f8;
                f22 = f11;
            } else {
                f25 = f6 + object2[4];
                f24 = f7 + object2[5];
                f23 = f6 + object2[6];
                f22 = f7 + object2[7];
            }
            if (n2 < 8) {
                f21 = f16;
                f20 = f15;
                f19 = f14;
                f18 = f17;
            } else {
                f21 = f12 + arrf[4];
                f20 = f13 + arrf[5];
                f19 = f12 + arrf[6];
                f18 = f13 + arrf[7];
            }
            object.drawMappedTextureRaw2((Texture)t2, (Texture)t3, f2, f3, f4, f5, f8, f9, f25, f24, f23, f22, f10, f11, f14, f15, f21, f20, f19, f18, f16, f17);
        }
        object.setExternalShader(null);
        if (arrimageData.length <= 1) {
            prTexture.unlock();
        }
        return new ImageData(this.getFilterContext(), pPSDrawable, rectangle2);
    }
}

