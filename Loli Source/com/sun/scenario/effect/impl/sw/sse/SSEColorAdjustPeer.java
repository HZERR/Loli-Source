/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.sw.sse;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.ColorAdjust;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;
import com.sun.scenario.effect.impl.sw.sse.SSEEffectPeer;

public class SSEColorAdjustPeer
extends SSEEffectPeer {
    public SSEColorAdjustPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    @Override
    protected final ColorAdjust getEffect() {
        return (ColorAdjust)super.getEffect();
    }

    private float getHue() {
        return this.getEffect().getHue() / 2.0f;
    }

    private float getSaturation() {
        return this.getEffect().getSaturation() + 1.0f;
    }

    private float getBrightness() {
        return this.getEffect().getBrightness() + 1.0f;
    }

    private float getContrast() {
        float f2 = this.getEffect().getContrast();
        if (f2 > 0.0f) {
            f2 *= 3.0f;
        }
        return f2 + 1.0f;
    }

    @Override
    public ImageData filter(Effect effect, RenderState renderState, BaseTransform baseTransform, Rectangle rectangle, ImageData ... arrimageData) {
        this.setEffect(effect);
        Rectangle rectangle2 = this.getResultBounds(baseTransform, rectangle, arrimageData);
        this.setDestBounds(rectangle2);
        HeapImage heapImage = (HeapImage)arrimageData[0].getTransformedImage(rectangle2);
        int n2 = 0;
        int n3 = 0;
        int n4 = heapImage.getPhysicalWidth();
        int n5 = heapImage.getPhysicalHeight();
        int n6 = heapImage.getScanlineStride();
        int[] arrn = heapImage.getPixelArray();
        Rectangle rectangle3 = new Rectangle(n2, n3, n4, n5);
        Rectangle rectangle4 = arrimageData[0].getTransformedBounds(rectangle2);
        BaseTransform baseTransform2 = BaseTransform.IDENTITY_TRANSFORM;
        this.setInputBounds(0, rectangle4);
        this.setInputNativeBounds(0, rectangle3);
        float[] arrf = new float[4];
        this.getTextureCoordinates(0, arrf, rectangle4.x, rectangle4.y, n4, n5, rectangle2, baseTransform2);
        int n7 = rectangle2.width;
        int n8 = rectangle2.height;
        HeapImage heapImage2 = (HeapImage)((Object)this.getRenderer().getCompatibleImage(n7, n8));
        this.setDestNativeBounds(heapImage2.getPhysicalWidth(), heapImage2.getPhysicalHeight());
        int n9 = heapImage2.getScanlineStride();
        int[] arrn2 = heapImage2.getPixelArray();
        float f2 = this.getBrightness();
        float f3 = this.getContrast();
        float f4 = this.getHue();
        float f5 = this.getSaturation();
        SSEColorAdjustPeer.filter(arrn2, 0, 0, n7, n8, n9, arrn, arrf[0], arrf[1], arrf[2], arrf[3], n4, n5, n6, f2, f3, f4, f5);
        arrimageData[0].releaseTransformedImage(heapImage);
        return new ImageData(this.getFilterContext(), heapImage2, rectangle2);
    }

    private static native void filter(int[] var0, int var1, int var2, int var3, int var4, int var5, int[] var6, float var7, float var8, float var9, float var10, int var11, int var12, int var13, float var14, float var15, float var16, float var17);
}
