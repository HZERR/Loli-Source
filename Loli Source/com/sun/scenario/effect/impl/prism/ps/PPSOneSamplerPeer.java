/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.prism.ps;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Texture;
import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderGraphics;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.PrTexture;
import com.sun.scenario.effect.impl.prism.ps.PPSDrawable;
import com.sun.scenario.effect.impl.prism.ps.PPSEffectPeer;
import com.sun.scenario.effect.impl.prism.ps.PPSRenderer;
import com.sun.scenario.effect.impl.state.RenderState;

public abstract class PPSOneSamplerPeer<T extends RenderState>
extends PPSEffectPeer<T> {
    private Shader shader;

    protected PPSOneSamplerPeer(FilterContext filterContext, Renderer renderer, String string) {
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
        Filterable filterable = arrimageData[0].getUntransformedImage();
        PrTexture prTexture = (PrTexture)((Object)filterable);
        Rectangle rectangle = arrimageData[0].getUntransformedBounds();
        Rectangle rectangle2 = this.getDestBounds();
        int n2 = rectangle2.width;
        int n3 = rectangle2.height;
        PPSRenderer pPSRenderer = this.getRenderer();
        PPSDrawable pPSDrawable = pPSRenderer.getCompatibleImage(n2, n3);
        if (pPSDrawable == null) {
            pPSRenderer.markLost();
            return new ImageData(this.getFilterContext(), pPSDrawable, rectangle2);
        }
        this.setDestNativeBounds(pPSDrawable.getPhysicalWidth(), pPSDrawable.getPhysicalHeight());
        BaseTransform baseTransform = arrimageData[0].getTransform();
        this.setInputBounds(0, rectangle);
        this.setInputTransform(0, baseTransform);
        this.setInputNativeBounds(0, prTexture.getNativeBounds());
        float[] arrf = new float[8];
        int n4 = this.getTextureCoordinates(0, arrf, rectangle.x, rectangle.y, filterable.getPhysicalWidth(), filterable.getPhysicalHeight(), rectangle2, baseTransform);
        ShaderGraphics shaderGraphics = pPSDrawable.createGraphics();
        if (shaderGraphics == null) {
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
        shaderGraphics.setExternalShader(this.shader);
        this.updateShader(this.shader);
        float f2 = 0.0f;
        float f3 = 0.0f;
        float f4 = n2;
        float f5 = n3;
        Object t2 = prTexture.getTextureObject();
        if (t2 == null) {
            pPSRenderer.markLost();
            return new ImageData(this.getFilterContext(), pPSDrawable, rectangle2);
        }
        float f6 = (float)t2.getContentX() / (float)t2.getPhysicalWidth();
        float f7 = (float)t2.getContentY() / (float)t2.getPhysicalHeight();
        float f8 = f6 + arrf[0];
        float f9 = f7 + arrf[1];
        float f10 = f6 + arrf[2];
        float f11 = f7 + arrf[3];
        if (n4 < 8) {
            shaderGraphics.drawTextureRaw((Texture)t2, f2, f3, f4, f5, f8, f9, f10, f11);
        } else {
            float f12 = f6 + arrf[4];
            float f13 = f7 + arrf[5];
            float f14 = f6 + arrf[6];
            float f15 = f7 + arrf[7];
            shaderGraphics.drawMappedTextureRaw((Texture)t2, f2, f3, f4, f5, f8, f9, f12, f13, f14, f15, f10, f11);
        }
        shaderGraphics.setExternalShader(null);
        return new ImageData(this.getFilterContext(), pPSDrawable, rectangle2);
    }
}

