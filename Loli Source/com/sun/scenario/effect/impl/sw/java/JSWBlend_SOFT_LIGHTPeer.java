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

public class JSWBlend_SOFT_LIGHTPeer
extends JSWEffectPeer {
    public JSWBlend_SOFT_LIGHTPeer(FilterContext filterContext, Renderer renderer, String string) {
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
                int n16;
                int n17;
                float f14 = i3;
                float f15 = f10;
                float f16 = f7;
                if (f15 >= 0.0f && f16 >= 0.0f) {
                    int n18 = (int)(f15 * (float)n4);
                    int n19 = (int)(f16 * (float)n5);
                    boolean bl = n18 >= n4 || n19 >= n5;
                    n17 = bl ? 0 : arrn[n19 * n6 + n18];
                } else {
                    n17 = 0;
                }
                float f17 = (float)(n17 >> 16 & 0xFF) / 255.0f;
                float f18 = (float)(n17 >> 8 & 0xFF) / 255.0f;
                float f19 = (float)(n17 & 0xFF) / 255.0f;
                float f20 = (float)(n17 >>> 24) / 255.0f;
                f15 = f17;
                f16 = f18;
                float f21 = f19;
                float f22 = f20;
                float f23 = f11;
                float f24 = f8;
                if (f23 >= 0.0f && f24 >= 0.0f) {
                    int n20 = (int)(f23 * (float)n9);
                    int n21 = (int)(f24 * (float)n10);
                    boolean bl = n20 >= n9 || n21 >= n10;
                    n16 = bl ? 0 : arrn2[n21 * n11 + n20];
                } else {
                    n16 = 0;
                }
                f17 = (float)(n16 >> 16 & 0xFF) / 255.0f;
                f18 = (float)(n16 >> 8 & 0xFF) / 255.0f;
                f19 = (float)(n16 & 0xFF) / 255.0f;
                f20 = (float)(n16 >>> 24) / 255.0f;
                f23 = f17 * f2;
                f24 = f18 * f2;
                float f25 = f19 * f2;
                float f26 = f20 * f2;
                float f27 = f15;
                float f28 = f16;
                float f29 = f21;
                float f30 = f22;
                float f31 = f23;
                float f32 = f24;
                float f33 = f25;
                float f34 = f26;
                float f35 = f30 + f34 - f30 * f34;
                float f36 = f27 / f30;
                float f37 = f28 / f30;
                float f38 = f29 / f30;
                float f39 = f31 / f34;
                float f40 = f32 / f34;
                float f41 = f33 / f34;
                float f42 = f36;
                f42 = f13 = (float)Math.sqrt(f42);
                float f43 = f36;
                float f44 = f42;
                float f45 = f43 <= 0.25f ? ((16.0f * f43 - 12.0f) * f43 + 4.0f) * f43 : f44;
                f43 = f12 = f45;
                f44 = f37;
                f42 = f13 = (float)Math.sqrt(f44);
                f44 = f37;
                f45 = f42;
                float f46 = f44 <= 0.25f ? ((16.0f * f44 - 12.0f) * f44 + 4.0f) * f44 : f45;
                f44 = f12 = f46;
                f45 = f38;
                f42 = f13 = (float)Math.sqrt(f45);
                f45 = f38;
                f46 = f42;
                float f47 = f45 <= 0.25f ? ((16.0f * f45 - 12.0f) * f45 + 4.0f) * f45 : f46;
                f45 = f12 = f47;
                float f48 = f30 == 0.0f ? f31 : (f34 == 0.0f ? f27 : (f39 <= 0.5f ? f27 + (1.0f - f30) * f31 - f34 * f27 * (1.0f - 2.0f * f39) * (1.0f - f36) : f27 + (1.0f - f30) * f31 + (2.0f * f31 - f34) * (f30 * f43 - f27)));
                float f49 = f30 == 0.0f ? f32 : (f34 == 0.0f ? f28 : (f40 <= 0.5f ? f28 + (1.0f - f30) * f32 - f34 * f28 * (1.0f - 2.0f * f40) * (1.0f - f37) : f28 + (1.0f - f30) * f32 + (2.0f * f32 - f34) * (f30 * f44 - f28)));
                float f50 = f30 == 0.0f ? f33 : (f34 == 0.0f ? f29 : (f41 <= 0.5f ? f29 + (1.0f - f30) * f33 - f34 * f29 * (1.0f - 2.0f * f41) * (1.0f - f38) : f29 + (1.0f - f30) * f33 + (2.0f * f33 - f34) * (f30 * f45 - f29)));
                float f51 = f48;
                float f52 = f49;
                float f53 = f50;
                float f54 = f35;
                float f55 = f51;
                float f56 = f52;
                float f57 = f53;
                float f58 = f54;
                if (f58 < 0.0f) {
                    f58 = 0.0f;
                } else if (f58 > 1.0f) {
                    f58 = 1.0f;
                }
                if (f55 < 0.0f) {
                    f55 = 0.0f;
                } else if (f55 > f58) {
                    f55 = f58;
                }
                if (f56 < 0.0f) {
                    f56 = 0.0f;
                } else if (f56 > f58) {
                    f56 = f58;
                }
                if (f57 < 0.0f) {
                    f57 = 0.0f;
                } else if (f57 > f58) {
                    f57 = f58;
                }
                arrn3[n15 + i3] = (int)(f55 * 255.0f) << 16 | (int)(f56 * 255.0f) << 8 | (int)(f57 * 255.0f) << 0 | (int)(f58 * 255.0f) << 24;
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

