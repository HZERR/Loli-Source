/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.PerspectiveTransform;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.AccessHelper;
import com.sun.scenario.effect.impl.state.PerspectiveTransformState;
import com.sun.scenario.effect.impl.state.RenderState;
import com.sun.scenario.effect.impl.sw.java.JSWEffectPeer;

public class JSWPerspectiveTransformPeer
extends JSWEffectPeer {
    public JSWPerspectiveTransformPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    @Override
    protected final PerspectiveTransform getEffect() {
        return (PerspectiveTransform)super.getEffect();
    }

    private float[][] getITX() {
        PerspectiveTransformState perspectiveTransformState = (PerspectiveTransformState)AccessHelper.getState(this.getEffect());
        return perspectiveTransformState.getITX();
    }

    private float[] getTx0() {
        Rectangle rectangle = this.getInputBounds(0);
        Rectangle rectangle2 = this.getInputNativeBounds(0);
        float f2 = (float)rectangle.width / (float)rectangle2.width;
        float[] arrf = this.getITX()[0];
        return new float[]{arrf[0] * f2, arrf[1] * f2, arrf[2] * f2};
    }

    private float[] getTx1() {
        Rectangle rectangle = this.getInputBounds(0);
        Rectangle rectangle2 = this.getInputNativeBounds(0);
        float f2 = (float)rectangle.height / (float)rectangle2.height;
        float[] arrf = this.getITX()[1];
        return new float[]{arrf[0] * f2, arrf[1] * f2, arrf[2] * f2};
    }

    private float[] getTx2() {
        return this.getITX()[2];
    }

    @Override
    public int getTextureCoordinates(int n2, float[] arrf, float f2, float f3, float f4, float f5, Rectangle rectangle, BaseTransform baseTransform) {
        arrf[0] = rectangle.x;
        arrf[1] = rectangle.y;
        arrf[2] = rectangle.x + rectangle.width;
        arrf[3] = rectangle.y + rectangle.height;
        return 4;
    }

    @Override
    public ImageData filter(Effect effect, RenderState renderState, BaseTransform baseTransform, Rectangle rectangle, ImageData ... arrimageData) {
        this.setEffect(effect);
        Rectangle rectangle2 = this.getResultBounds(baseTransform, rectangle, arrimageData);
        this.setDestBounds(rectangle2);
        HeapImage heapImage = (HeapImage)arrimageData[0].getUntransformedImage();
        int n2 = 0;
        int n3 = 0;
        int n4 = heapImage.getPhysicalWidth();
        int n5 = heapImage.getPhysicalHeight();
        int n6 = heapImage.getScanlineStride();
        int[] arrn = heapImage.getPixelArray();
        Rectangle rectangle3 = new Rectangle(n2, n3, n4, n5);
        Rectangle rectangle4 = arrimageData[0].getUntransformedBounds();
        BaseTransform baseTransform2 = arrimageData[0].getTransform();
        this.setInputBounds(0, rectangle4);
        this.setInputNativeBounds(0, rectangle3);
        float[] arrf = new float[4];
        float[] arrf2 = new float[4];
        this.getTextureCoordinates(0, arrf2, rectangle4.x, rectangle4.y, n4, n5, rectangle2, baseTransform2);
        int n7 = rectangle2.width;
        int n8 = rectangle2.height;
        HeapImage heapImage2 = (HeapImage)((Object)this.getRenderer().getCompatibleImage(n7, n8));
        this.setDestNativeBounds(heapImage2.getPhysicalWidth(), heapImage2.getPhysicalHeight());
        int n9 = heapImage2.getScanlineStride();
        int[] arrn2 = heapImage2.getPixelArray();
        float[] arrf3 = this.getTx1();
        float f2 = arrf3[0];
        float f3 = arrf3[1];
        float f4 = arrf3[2];
        float[] arrf4 = this.getTx0();
        float f5 = arrf4[0];
        float f6 = arrf4[1];
        float f7 = arrf4[2];
        float[] arrf5 = this.getTx2();
        float f8 = arrf5[0];
        float f9 = arrf5[1];
        float f10 = arrf5[2];
        float f11 = (arrf2[2] - arrf2[0]) / (float)n7;
        float f12 = (arrf2[3] - arrf2[1]) / (float)n8;
        float f13 = arrf2[1] + f12 * 0.5f;
        for (int i2 = 0; i2 < 0 + n8; ++i2) {
            float f14 = i2;
            int n10 = i2 * n9;
            float f15 = arrf2[0] + f11 * 0.5f;
            for (int i3 = 0; i3 < 0 + n7; ++i3) {
                float f16;
                float f17 = i3;
                float f18 = f15;
                float f19 = f13;
                float f20 = 1.0f;
                float f21 = f18;
                float f22 = f19;
                float f23 = f20;
                float f24 = f8;
                float f25 = f9;
                float f26 = f10;
                float f27 = f16 = f21 * f24 + f22 * f25 + f23 * f26;
                f21 = f18;
                f22 = f19;
                f23 = f20;
                f24 = f5;
                f25 = f6;
                f26 = f7;
                f16 = f21 * f24 + f22 * f25 + f23 * f26;
                float f28 = f16 / f27;
                f21 = f18;
                f22 = f19;
                f23 = f20;
                f24 = f2;
                f25 = f3;
                f26 = f4;
                f16 = f21 * f24 + f22 * f25 + f23 * f26;
                float f29 = f16 / f27;
                f25 = f28;
                f26 = f29;
                this.lsample(arrn, f25, f26, n4, n5, n6, arrf);
                f21 = arrf[0];
                f22 = arrf[1];
                f23 = arrf[2];
                f24 = arrf[3];
                float f30 = f21;
                float f31 = f22;
                float f32 = f23;
                float f33 = f24;
                if (f33 < 0.0f) {
                    f33 = 0.0f;
                } else if (f33 > 1.0f) {
                    f33 = 1.0f;
                }
                if (f30 < 0.0f) {
                    f30 = 0.0f;
                } else if (f30 > f33) {
                    f30 = f33;
                }
                if (f31 < 0.0f) {
                    f31 = 0.0f;
                } else if (f31 > f33) {
                    f31 = f33;
                }
                if (f32 < 0.0f) {
                    f32 = 0.0f;
                } else if (f32 > f33) {
                    f32 = f33;
                }
                arrn2[n10 + i3] = (int)(f30 * 255.0f) << 16 | (int)(f31 * 255.0f) << 8 | (int)(f32 * 255.0f) << 0 | (int)(f33 * 255.0f) << 24;
                f15 += f11;
            }
            f13 += f12;
        }
        return new ImageData(this.getFilterContext(), heapImage2, rectangle2);
    }
}

