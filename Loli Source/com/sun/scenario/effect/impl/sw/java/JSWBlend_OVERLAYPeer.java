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

public class JSWBlend_OVERLAYPeer
extends JSWEffectPeer {
    public JSWBlend_OVERLAYPeer(FilterContext filterContext, Renderer renderer, String string) {
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
                int n16;
                int n17;
                float f12 = i3;
                float f13 = f10;
                float f14 = f7;
                if (f13 >= 0.0f && f14 >= 0.0f) {
                    int n18 = (int)(f13 * (float)n4);
                    int n19 = (int)(f14 * (float)n5);
                    boolean bl = n18 >= n4 || n19 >= n5;
                    n17 = bl ? 0 : arrn[n19 * n6 + n18];
                } else {
                    n17 = 0;
                }
                float f15 = (float)(n17 >> 16 & 0xFF) / 255.0f;
                float f16 = (float)(n17 >> 8 & 0xFF) / 255.0f;
                float f17 = (float)(n17 & 0xFF) / 255.0f;
                float f18 = (float)(n17 >>> 24) / 255.0f;
                f13 = f15;
                f14 = f16;
                float f19 = f17;
                float f20 = f18;
                float f21 = f11;
                float f22 = f8;
                if (f21 >= 0.0f && f22 >= 0.0f) {
                    int n20 = (int)(f21 * (float)n9);
                    int n21 = (int)(f22 * (float)n10);
                    boolean bl = n20 >= n9 || n21 >= n10;
                    n16 = bl ? 0 : arrn2[n21 * n11 + n20];
                } else {
                    n16 = 0;
                }
                f15 = (float)(n16 >> 16 & 0xFF) / 255.0f;
                f16 = (float)(n16 >> 8 & 0xFF) / 255.0f;
                f17 = (float)(n16 & 0xFF) / 255.0f;
                f18 = (float)(n16 >>> 24) / 255.0f;
                f21 = f15 * f2;
                f22 = f16 * f2;
                float f23 = f17 * f2;
                float f24 = f18 * f2;
                float f25 = f13;
                float f26 = f14;
                float f27 = f19;
                float f28 = f20;
                float f29 = f21;
                float f30 = f22;
                float f31 = f23;
                float f32 = f24;
                float f33 = f28 + f32 - f28 * f32;
                float f34 = f25 - f28 * 0.5f;
                float f35 = f26 - f28 * 0.5f;
                float f36 = f27 - f28 * 0.5f;
                float f37 = (float)Math.ceil(f34);
                float f38 = (float)Math.ceil(f35);
                float f39 = (float)Math.ceil(f36);
                f34 = f37;
                f35 = f38;
                f36 = f39;
                float f40 = f25 - f34 * f28;
                float f41 = f26 - f35 * f28;
                float f42 = f27 - f36 * f28;
                float f43 = Math.abs(f40);
                float f44 = Math.abs(f41);
                float f45 = Math.abs(f42);
                f40 = f43;
                f41 = f44;
                f42 = f45;
                float f46 = f29 - f34 * f32;
                float f47 = f30 - f35 * f32;
                float f48 = f31 - f36 * f32;
                f43 = Math.abs(f46);
                f44 = Math.abs(f47);
                f45 = Math.abs(f48);
                f46 = f43;
                f47 = f44;
                f48 = f45;
                float f49 = (2.0f * f40 + 1.0f - f28) * f46 + (1.0f - f32) * f40;
                float f50 = (2.0f * f41 + 1.0f - f28) * f47 + (1.0f - f32) * f41;
                float f51 = (2.0f * f42 + 1.0f - f28) * f48 + (1.0f - f32) * f42;
                float f52 = f49 - f34 * f33;
                float f53 = f50 - f35 * f33;
                float f54 = f51 - f36 * f33;
                f43 = Math.abs(f52);
                f44 = Math.abs(f53);
                f45 = Math.abs(f54);
                f49 = f43;
                f50 = f44;
                f51 = f45;
                float f55 = f49;
                float f56 = f50;
                float f57 = f51;
                float f58 = f33;
                float f59 = f55;
                float f60 = f56;
                float f61 = f57;
                float f62 = f58;
                if (f62 < 0.0f) {
                    f62 = 0.0f;
                } else if (f62 > 1.0f) {
                    f62 = 1.0f;
                }
                if (f59 < 0.0f) {
                    f59 = 0.0f;
                } else if (f59 > f62) {
                    f59 = f62;
                }
                if (f60 < 0.0f) {
                    f60 = 0.0f;
                } else if (f60 > f62) {
                    f60 = f62;
                }
                if (f61 < 0.0f) {
                    f61 = 0.0f;
                } else if (f61 > f62) {
                    f61 = f62;
                }
                arrn3[n15 + i3] = (int)(f59 * 255.0f) << 16 | (int)(f60 * 255.0f) << 8 | (int)(f61 * 255.0f) << 0 | (int)(f62 * 255.0f) << 24;
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

