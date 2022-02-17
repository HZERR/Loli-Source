/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Brightpass;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;
import com.sun.scenario.effect.impl.sw.java.JSWEffectPeer;

public class JSWBrightpassPeer
extends JSWEffectPeer {
    public JSWBrightpassPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    @Override
    protected final Brightpass getEffect() {
        return (Brightpass)super.getEffect();
    }

    private float getThreshold() {
        return this.getEffect().getThreshold();
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
        float f2 = this.getThreshold();
        float f3 = (arrf[2] - arrf[0]) / (float)n7;
        float f4 = (arrf[3] - arrf[1]) / (float)n8;
        float f5 = arrf[1] + f4 * 0.5f;
        for (int i2 = 0; i2 < 0 + n8; ++i2) {
            float f6 = i2;
            int n10 = i2 * n9;
            float f7 = arrf[0] + f3 * 0.5f;
            for (int i3 = 0; i3 < 0 + n7; ++i3) {
                float f8;
                int n11;
                float f9 = i3;
                float f10 = 0.2125f;
                float f11 = 0.7154f;
                float f12 = 0.0721f;
                float f13 = f7;
                float f14 = f5;
                if (f13 >= 0.0f && f14 >= 0.0f) {
                    int n12 = (int)(f13 * (float)n4);
                    int n13 = (int)(f14 * (float)n5);
                    boolean bl = n12 >= n4 || n13 >= n5;
                    n11 = bl ? 0 : arrn[n13 * n6 + n12];
                } else {
                    n11 = 0;
                }
                float f15 = (float)(n11 >> 16 & 0xFF) / 255.0f;
                float f16 = (float)(n11 >> 8 & 0xFF) / 255.0f;
                float f17 = (float)(n11 & 0xFF) / 255.0f;
                float f18 = (float)(n11 >>> 24) / 255.0f;
                f13 = f15;
                f14 = f16;
                float f19 = f17;
                float f20 = f18;
                float f21 = f10;
                float f22 = f11;
                float f23 = f12;
                float f24 = f13;
                float f25 = f14;
                float f26 = f19;
                f21 = f8 = f21 * f24 + f22 * f25 + f23 * f26;
                f23 = 0.0f;
                f24 = f21 - f20 * f2;
                f24 = f21 = (f22 = f23 > f24 ? f23 : f24);
                f23 = Math.signum(f24);
                float f27 = f13 * f23;
                float f28 = f14 * f23;
                float f29 = f19 * f23;
                float f30 = f20 * f23;
                if (f30 < 0.0f) {
                    f30 = 0.0f;
                } else if (f30 > 1.0f) {
                    f30 = 1.0f;
                }
                if (f27 < 0.0f) {
                    f27 = 0.0f;
                } else if (f27 > f30) {
                    f27 = f30;
                }
                if (f28 < 0.0f) {
                    f28 = 0.0f;
                } else if (f28 > f30) {
                    f28 = f30;
                }
                if (f29 < 0.0f) {
                    f29 = 0.0f;
                } else if (f29 > f30) {
                    f29 = f30;
                }
                arrn2[n10 + i3] = (int)(f27 * 255.0f) << 16 | (int)(f28 * 255.0f) << 8 | (int)(f29 * 255.0f) << 0 | (int)(f30 * 255.0f) << 24;
                f7 += f3;
            }
            f5 += f4;
        }
        arrimageData[0].releaseTransformedImage(heapImage);
        return new ImageData(this.getFilterContext(), heapImage2, rectangle2);
    }
}

