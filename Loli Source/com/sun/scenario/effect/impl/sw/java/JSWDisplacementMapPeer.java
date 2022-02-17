/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.DisplacementMap;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.FloatMap;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;
import com.sun.scenario.effect.impl.sw.java.JSWEffectPeer;

public class JSWDisplacementMapPeer
extends JSWEffectPeer {
    public JSWDisplacementMapPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    @Override
    protected final DisplacementMap getEffect() {
        return (DisplacementMap)super.getEffect();
    }

    private float[] getSampletx() {
        return new float[]{this.getEffect().getOffsetX(), this.getEffect().getOffsetY(), this.getEffect().getScaleX(), this.getEffect().getScaleY()};
    }

    private float[] getImagetx() {
        float f2 = this.getEffect().getWrap() ? 0.5f : 0.0f;
        return new float[]{f2 / (float)this.getInputNativeBounds((int)0).width, f2 / (float)this.getInputNativeBounds((int)0).height, ((float)this.getInputBounds((int)0).width - 2.0f * f2) / (float)this.getInputNativeBounds((int)0).width, ((float)this.getInputBounds((int)0).height - 2.0f * f2) / (float)this.getInputNativeBounds((int)0).height};
    }

    private float getWrap() {
        return this.getEffect().getWrap() ? 1.0f : 0.0f;
    }

    @Override
    protected Object getSamplerData(int n2) {
        return this.getEffect().getMapData();
    }

    @Override
    public int getTextureCoordinates(int n2, float[] arrf, float f2, float f3, float f4, float f5, Rectangle rectangle, BaseTransform baseTransform) {
        arrf[1] = 0.0f;
        arrf[0] = 0.0f;
        arrf[3] = 1.0f;
        arrf[2] = 1.0f;
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
        FloatMap floatMap = (FloatMap)this.getSamplerData(1);
        boolean bl = false;
        boolean bl2 = false;
        int n7 = floatMap.getWidth();
        int n8 = floatMap.getHeight();
        int n9 = floatMap.getWidth();
        float[] arrf2 = floatMap.getData();
        float[] arrf3 = new float[4];
        float[] arrf4 = new float[4];
        this.getTextureCoordinates(0, arrf4, rectangle4.x, rectangle4.y, n4, n5, rectangle2, baseTransform2);
        float[] arrf5 = new float[]{0.0f, 0.0f, 1.0f, 1.0f};
        int n10 = rectangle2.width;
        int n11 = rectangle2.height;
        HeapImage heapImage2 = (HeapImage)((Object)this.getRenderer().getCompatibleImage(n10, n11));
        this.setDestNativeBounds(heapImage2.getPhysicalWidth(), heapImage2.getPhysicalHeight());
        int n12 = heapImage2.getScanlineStride();
        int[] arrn2 = heapImage2.getPixelArray();
        float[] arrf6 = this.getImagetx();
        float f2 = arrf6[0];
        float f3 = arrf6[1];
        float f4 = arrf6[2];
        float f5 = arrf6[3];
        float f6 = this.getWrap();
        float[] arrf7 = this.getSampletx();
        float f7 = arrf7[0];
        float f8 = arrf7[1];
        float f9 = arrf7[2];
        float f10 = arrf7[3];
        float f11 = (arrf4[2] - arrf4[0]) / (float)n10;
        float f12 = (arrf4[3] - arrf4[1]) / (float)n11;
        float f13 = (arrf5[2] - arrf5[0]) / (float)n10;
        float f14 = (arrf5[3] - arrf5[1]) / (float)n11;
        float f15 = arrf4[1] + f12 * 0.5f;
        float f16 = arrf5[1] + f14 * 0.5f;
        for (int i2 = 0; i2 < 0 + n11; ++i2) {
            float f17 = i2;
            int n13 = i2 * n12;
            float f18 = arrf4[0] + f11 * 0.5f;
            float f19 = arrf5[0] + f13 * 0.5f;
            for (int i3 = 0; i3 < 0 + n10; ++i3) {
                float f20 = i3;
                float f21 = f19;
                float f22 = f16;
                this.fsample(arrf2, f21, f22, n7, n8, n9, arrf3);
                float f23 = arrf3[0];
                float f24 = arrf3[1];
                float f25 = arrf3[2];
                float f26 = arrf3[3];
                f21 = f23;
                f22 = f24;
                float f27 = f25;
                float f28 = f26;
                float f29 = f18 + f9 * (f21 + f7);
                float f30 = f15 + f10 * (f22 + f8);
                float f31 = f29;
                float f32 = f30;
                float f33 = (float)Math.floor(f31);
                float f34 = (float)Math.floor(f32);
                f29 -= f6 * f33;
                f30 -= f6 * f34;
                f29 = f2 + f29 * f4;
                f30 = f3 + f30 * f5;
                f31 = f29;
                f32 = f30;
                this.lsample(arrn, f31, f32, n4, n5, n6, arrf);
                f23 = arrf[0];
                f24 = arrf[1];
                f25 = arrf[2];
                f26 = arrf[3];
                float f35 = f23;
                float f36 = f24;
                float f37 = f25;
                float f38 = f26;
                if (f38 < 0.0f) {
                    f38 = 0.0f;
                } else if (f38 > 1.0f) {
                    f38 = 1.0f;
                }
                if (f35 < 0.0f) {
                    f35 = 0.0f;
                } else if (f35 > f38) {
                    f35 = f38;
                }
                if (f36 < 0.0f) {
                    f36 = 0.0f;
                } else if (f36 > f38) {
                    f36 = f38;
                }
                if (f37 < 0.0f) {
                    f37 = 0.0f;
                } else if (f37 > f38) {
                    f37 = f38;
                }
                arrn2[n13 + i3] = (int)(f35 * 255.0f) << 16 | (int)(f36 * 255.0f) << 8 | (int)(f37 * 255.0f) << 0 | (int)(f38 * 255.0f) << 24;
                f18 += f11;
                f19 += f13;
            }
            f15 += f12;
            f16 += f14;
        }
        return new ImageData(this.getFilterContext(), heapImage2, rectangle2);
    }
}

