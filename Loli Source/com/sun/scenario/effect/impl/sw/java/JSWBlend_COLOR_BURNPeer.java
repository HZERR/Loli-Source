/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Blend;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;
import com.sun.scenario.effect.impl.sw.java.JSWEffectPeer;

public class JSWBlend_COLOR_BURNPeer
extends JSWEffectPeer {
    public JSWBlend_COLOR_BURNPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    @Override
    protected final Blend getEffect() {
        return (Blend)super.getEffect();
    }

    private float getOpacity() {
        return this.getEffect().getOpacity();
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
        HeapImage heapImage2 = (HeapImage)arrimageData[1].getTransformedImage(rectangle2);
        int n7 = 0;
        int n8 = 0;
        int n9 = heapImage2.getPhysicalWidth();
        int n10 = heapImage2.getPhysicalHeight();
        int n11 = heapImage2.getScanlineStride();
        int[] arrn2 = heapImage2.getPixelArray();
        Rectangle rectangle5 = new Rectangle(n7, n8, n9, n10);
        Rectangle rectangle6 = arrimageData[1].getTransformedBounds(rectangle2);
        BaseTransform baseTransform3 = BaseTransform.IDENTITY_TRANSFORM;
        this.setInputBounds(1, rectangle6);
        this.setInputNativeBounds(1, rectangle5);
        float[] arrf = new float[4];
        this.getTextureCoordinates(0, arrf, rectangle4.x, rectangle4.y, n4, n5, rectangle2, baseTransform2);
        float[] arrf2 = new float[4];
        this.getTextureCoordinates(1, arrf2, rectangle6.x, rectangle6.y, n9, n10, rectangle2, baseTransform3);
        int n12 = rectangle2.width;
        int n13 = rectangle2.height;
        HeapImage heapImage3 = (HeapImage)((Object)this.getRenderer().getCompatibleImage(n12, n13));
        this.setDestNativeBounds(heapImage3.getPhysicalWidth(), heapImage3.getPhysicalHeight());
        int n14 = heapImage3.getScanlineStride();
        int[] arrn3 = heapImage3.getPixelArray();
        float f2 = this.getOpacity();
        float f3 = (arrf[2] - arrf[0]) / (float)n12;
        float f4 = (arrf[3] - arrf[1]) / (float)n13;
        float f5 = (arrf2[2] - arrf2[0]) / (float)n12;
        float f6 = (arrf2[3] - arrf2[1]) / (float)n13;
        float f7 = arrf[1] + f4 * 0.5f;
        float f8 = arrf2[1] + f6 * 0.5f;
        for (int i2 = 0; i2 < 0 + n13; ++i2) {
            float f9 = i2;
            int n15 = i2 * n14;
            float f10 = arrf[0] + f3 * 0.5f;
            float f11 = arrf2[0] + f5 * 0.5f;
            for (int i3 = 0; i3 < 0 + n12; ++i3) {
                float f12;
                float f13;
                float f14;
                int n16;
                int n17;
                float f15 = i3;
                float f16 = f10;
                float f17 = f7;
                if (f16 >= 0.0f && f17 >= 0.0f) {
                    int n18 = (int)(f16 * (float)n4);
                    int n19 = (int)(f17 * (float)n5);
                    boolean bl = n18 >= n4 || n19 >= n5;
                    n17 = bl ? 0 : arrn[n19 * n6 + n18];
                } else {
                    n17 = 0;
                }
                float f18 = (float)(n17 >> 16 & 0xFF) / 255.0f;
                float f19 = (float)(n17 >> 8 & 0xFF) / 255.0f;
                float f20 = (float)(n17 & 0xFF) / 255.0f;
                float f21 = (float)(n17 >>> 24) / 255.0f;
                f16 = f18;
                f17 = f19;
                float f22 = f20;
                float f23 = f21;
                float f24 = f11;
                float f25 = f8;
                if (f24 >= 0.0f && f25 >= 0.0f) {
                    int n20 = (int)(f24 * (float)n9);
                    int n21 = (int)(f25 * (float)n10);
                    boolean bl = n20 >= n9 || n21 >= n10;
                    n16 = bl ? 0 : arrn2[n21 * n11 + n20];
                } else {
                    n16 = 0;
                }
                f18 = (float)(n16 >> 16 & 0xFF) / 255.0f;
                f19 = (float)(n16 >> 8 & 0xFF) / 255.0f;
                f20 = (float)(n16 & 0xFF) / 255.0f;
                f21 = (float)(n16 >>> 24) / 255.0f;
                f24 = f18 * f2;
                f25 = f19 * f2;
                float f26 = f20 * f2;
                float f27 = f21 * f2;
                float f28 = f16;
                float f29 = f17;
                float f30 = f22;
                float f31 = f23;
                float f32 = f24;
                float f33 = f25;
                float f34 = f26;
                float f35 = f27;
                float f36 = f31 + f35 - f31 * f35;
                float f37 = (1.0f - f35) * f28 + (1.0f - f31) * f32;
                float f38 = (1.0f - f35) * f29 + (1.0f - f31) * f33;
                float f39 = (1.0f - f35) * f30 + (1.0f - f31) * f34;
                float f40 = f31 * f35;
                float f41 = f35 * f35;
                f14 = f31 == f28 ? f40 : (f32 == 0.0f ? 0.0f : ((f14 = f41 * (f31 - f28) / f32) >= f40 ? 0.0f : f40 - f14));
                f13 = f31 == f29 ? f40 : (f33 == 0.0f ? 0.0f : ((f13 = f41 * (f31 - f29) / f33) >= f40 ? 0.0f : f40 - f13));
                f12 = f31 == f30 ? f40 : (f34 == 0.0f ? 0.0f : ((f12 = f41 * (f31 - f30) / f34) >= f40 ? 0.0f : f40 - f12));
                float f42 = f37 += f14;
                float f43 = f38 += f13;
                float f44 = f39 += f12;
                float f45 = f36;
                float f46 = f42;
                float f47 = f43;
                float f48 = f44;
                float f49 = f45;
                if (f49 < 0.0f) {
                    f49 = 0.0f;
                } else if (f49 > 1.0f) {
                    f49 = 1.0f;
                }
                if (f46 < 0.0f) {
                    f46 = 0.0f;
                } else if (f46 > f49) {
                    f46 = f49;
                }
                if (f47 < 0.0f) {
                    f47 = 0.0f;
                } else if (f47 > f49) {
                    f47 = f49;
                }
                if (f48 < 0.0f) {
                    f48 = 0.0f;
                } else if (f48 > f49) {
                    f48 = f49;
                }
                arrn3[n15 + i3] = (int)(f46 * 255.0f) << 16 | (int)(f47 * 255.0f) << 8 | (int)(f48 * 255.0f) << 0 | (int)(f49 * 255.0f) << 24;
                f10 += f3;
                f11 += f5;
            }
            f7 += f4;
            f8 += f6;
        }
        arrimageData[0].releaseTransformedImage(heapImage);
        arrimageData[1].releaseTransformedImage(heapImage2);
        return new ImageData(this.getFilterContext(), heapImage3, rectangle2);
    }
}

