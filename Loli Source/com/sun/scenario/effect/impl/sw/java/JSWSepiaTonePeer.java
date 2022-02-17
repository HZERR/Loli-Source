/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.SepiaTone;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;
import com.sun.scenario.effect.impl.sw.java.JSWEffectPeer;

public class JSWSepiaTonePeer
extends JSWEffectPeer {
    public JSWSepiaTonePeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    @Override
    protected final SepiaTone getEffect() {
        return (SepiaTone)super.getEffect();
    }

    private float getLevel() {
        return this.getEffect().getLevel();
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
        float f2 = this.getLevel();
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
                float f10 = 0.3f;
                float f11 = 0.59f;
                float f12 = 0.11f;
                float f13 = 1.6f;
                float f14 = 1.2f;
                float f15 = 0.9f;
                float f16 = f7;
                float f17 = f5;
                if (f16 >= 0.0f && f17 >= 0.0f) {
                    int n12 = (int)(f16 * (float)n4);
                    int n13 = (int)(f17 * (float)n5);
                    boolean bl = n12 >= n4 || n13 >= n5;
                    n11 = bl ? 0 : arrn[n13 * n6 + n12];
                } else {
                    n11 = 0;
                }
                float f18 = (float)(n11 >> 16 & 0xFF) / 255.0f;
                float f19 = (float)(n11 >> 8 & 0xFF) / 255.0f;
                float f20 = (float)(n11 & 0xFF) / 255.0f;
                float f21 = (float)(n11 >>> 24) / 255.0f;
                f16 = f18;
                f17 = f19;
                float f22 = f20;
                float f23 = f21;
                float f24 = f16;
                float f25 = f17;
                float f26 = f22;
                float f27 = f10;
                float f28 = f11;
                float f29 = f12;
                f25 = f24 = (f8 = f24 * f27 + f25 * f28 + f26 * f29);
                f26 = f24;
                f27 = f24;
                f28 = f25 * f13;
                f29 = f26 * f14;
                float f30 = f27 * f15;
                float f31 = f28;
                float f32 = f29;
                float f33 = f30;
                float f34 = f16;
                float f35 = f17;
                float f36 = f22;
                float f37 = 1.0f - f2;
                float f38 = f31 * (1.0f - f37) + f34 * f37;
                float f39 = f32 * (1.0f - f37) + f35 * f37;
                float f40 = f33 * (1.0f - f37) + f36 * f37;
                float f41 = f38;
                float f42 = f39;
                float f43 = f40;
                float f44 = f23;
                if (f44 < 0.0f) {
                    f44 = 0.0f;
                } else if (f44 > 1.0f) {
                    f44 = 1.0f;
                }
                if (f41 < 0.0f) {
                    f41 = 0.0f;
                } else if (f41 > f44) {
                    f41 = f44;
                }
                if (f42 < 0.0f) {
                    f42 = 0.0f;
                } else if (f42 > f44) {
                    f42 = f44;
                }
                if (f43 < 0.0f) {
                    f43 = 0.0f;
                } else if (f43 > f44) {
                    f43 = f44;
                }
                arrn2[n10 + i3] = (int)(f41 * 255.0f) << 16 | (int)(f42 * 255.0f) << 8 | (int)(f43 * 255.0f) << 0 | (int)(f44 * 255.0f) << 24;
                f7 += f3;
            }
            f5 += f4;
        }
        arrimageData[0].releaseTransformedImage(heapImage);
        return new ImageData(this.getFilterContext(), heapImage2, rectangle2);
    }
}

