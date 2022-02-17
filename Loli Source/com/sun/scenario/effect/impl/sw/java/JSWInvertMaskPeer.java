/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.InvertMask;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;
import com.sun.scenario.effect.impl.sw.java.JSWEffectPeer;

public class JSWInvertMaskPeer
extends JSWEffectPeer {
    public JSWInvertMaskPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    @Override
    protected final InvertMask getEffect() {
        return (InvertMask)super.getEffect();
    }

    private float[] getOffset() {
        float f2 = this.getEffect().getOffsetX();
        float f3 = this.getEffect().getOffsetY();
        float[] arrf = new float[]{f2, f3};
        try {
            this.getInputTransform(0).inverseDeltaTransform(arrf, 0, arrf, 0, 1);
        }
        catch (Exception exception) {
            // empty catch block
        }
        arrf[0] = arrf[0] / (float)this.getInputNativeBounds((int)0).width;
        arrf[1] = arrf[1] / (float)this.getInputNativeBounds((int)0).height;
        return arrf;
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
        float[] arrf2 = this.getOffset();
        float f2 = arrf2[0];
        float f3 = arrf2[1];
        float f4 = (arrf[2] - arrf[0]) / (float)n7;
        float f5 = (arrf[3] - arrf[1]) / (float)n8;
        float f6 = arrf[1] + f5 * 0.5f;
        for (int i2 = 0; i2 < 0 + n8; ++i2) {
            float f7 = i2;
            int n10 = i2 * n9;
            float f8 = arrf[0] + f4 * 0.5f;
            for (int i3 = 0; i3 < 0 + n7; ++i3) {
                float f9;
                int n11;
                float f10 = i3;
                float f11 = f8 - f2;
                float f12 = f6 - f3;
                if (f11 >= 0.0f && f12 >= 0.0f) {
                    int n12 = (int)(f11 * (float)n4);
                    int n13 = (int)(f12 * (float)n5);
                    boolean bl = n12 >= n4 || n13 >= n5;
                    n11 = bl ? 0 : arrn[n13 * n6 + n12];
                } else {
                    n11 = 0;
                }
                f11 = f9 = (float)(n11 >>> 24) / 255.0f;
                float f13 = f12 = 1.0f - f11;
                float f14 = f12;
                float f15 = f12;
                float f16 = f12;
                if (f16 < 0.0f) {
                    f16 = 0.0f;
                } else if (f16 > 1.0f) {
                    f16 = 1.0f;
                }
                if (f13 < 0.0f) {
                    f13 = 0.0f;
                } else if (f13 > f16) {
                    f13 = f16;
                }
                if (f14 < 0.0f) {
                    f14 = 0.0f;
                } else if (f14 > f16) {
                    f14 = f16;
                }
                if (f15 < 0.0f) {
                    f15 = 0.0f;
                } else if (f15 > f16) {
                    f15 = f16;
                }
                arrn2[n10 + i3] = (int)(f13 * 255.0f) << 16 | (int)(f14 * 255.0f) << 8 | (int)(f15 * 255.0f) << 0 | (int)(f16 * 255.0f) << 24;
                f8 += f4;
            }
            f6 += f5;
        }
        arrimageData[0].releaseTransformedImage(heapImage);
        return new ImageData(this.getFilterContext(), heapImage2, rectangle2);
    }
}

