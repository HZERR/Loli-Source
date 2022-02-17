/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.LinearConvolveRenderState;
import com.sun.scenario.effect.impl.sw.java.JSWEffectPeer;
import java.nio.FloatBuffer;

public class JSWLinearConvolvePeer
extends JSWEffectPeer<LinearConvolveRenderState> {
    private static final float cmin = 1.0f;
    private static final float cmax = 254.9375f;

    public JSWLinearConvolvePeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    private Rectangle getResultBounds(LinearConvolveRenderState linearConvolveRenderState, Rectangle rectangle, ImageData ... arrimageData) {
        Rectangle rectangle2 = arrimageData[0].getTransformedBounds(null);
        rectangle2 = linearConvolveRenderState.getPassResultBounds(rectangle2, rectangle);
        return rectangle2;
    }

    @Override
    public ImageData filter(Effect effect, LinearConvolveRenderState linearConvolveRenderState, BaseTransform baseTransform, Rectangle rectangle, ImageData ... arrimageData) {
        this.setRenderState(linearConvolveRenderState);
        Rectangle rectangle2 = this.getResultBounds(linearConvolveRenderState, null, arrimageData);
        Rectangle rectangle3 = new Rectangle(rectangle2);
        rectangle3.intersectWith(rectangle);
        this.setDestBounds(rectangle3);
        int n2 = rectangle3.width;
        int n3 = rectangle3.height;
        HeapImage heapImage = (HeapImage)arrimageData[0].getUntransformedImage();
        int n4 = heapImage.getPhysicalWidth();
        int n5 = heapImage.getPhysicalHeight();
        int n6 = heapImage.getScanlineStride();
        int[] arrn = heapImage.getPixelArray();
        Rectangle rectangle4 = arrimageData[0].getUntransformedBounds();
        BaseTransform baseTransform2 = arrimageData[0].getTransform();
        Rectangle rectangle5 = new Rectangle(0, 0, n4, n5);
        this.setInputBounds(0, rectangle4);
        this.setInputTransform(0, baseTransform2);
        this.setInputNativeBounds(0, rectangle5);
        HeapImage heapImage2 = (HeapImage)((Object)this.getRenderer().getCompatibleImage(n2, n3));
        this.setDestNativeBounds(heapImage2.getPhysicalWidth(), heapImage2.getPhysicalHeight());
        int n7 = heapImage2.getScanlineStride();
        int[] arrn2 = heapImage2.getPixelArray();
        int n8 = linearConvolveRenderState.getPassKernelSize();
        FloatBuffer floatBuffer = linearConvolveRenderState.getPassWeights();
        LinearConvolveRenderState.PassType passType = linearConvolveRenderState.getPassType();
        if (!baseTransform2.isIdentity() || !rectangle3.contains(rectangle2.x, rectangle2.y)) {
            passType = LinearConvolveRenderState.PassType.GENERAL_VECTOR;
        }
        if (n8 >= 0) {
            passType = LinearConvolveRenderState.PassType.GENERAL_VECTOR;
        }
        if (passType == LinearConvolveRenderState.PassType.HORIZONTAL_CENTERED) {
            float[] arrf = new float[n8 * 2];
            floatBuffer.get(arrf, 0, n8);
            floatBuffer.rewind();
            floatBuffer.get(arrf, n8, n8);
            this.filterHV(arrn2, n2, n3, 1, n7, arrn, n4, n5, 1, n6, arrf);
        } else if (passType == LinearConvolveRenderState.PassType.VERTICAL_CENTERED) {
            float[] arrf = new float[n8 * 2];
            floatBuffer.get(arrf, 0, n8);
            floatBuffer.rewind();
            floatBuffer.get(arrf, n8, n8);
            this.filterHV(arrn2, n3, n2, n7, 1, arrn, n5, n4, n6, 1, arrf);
        } else {
            float f2;
            float f3;
            float f4;
            float f5;
            float[] arrf = new float[n8];
            floatBuffer.get(arrf, 0, n8);
            float[] arrf2 = new float[8];
            int n9 = this.getTextureCoordinates(0, arrf2, rectangle4.x, rectangle4.y, rectangle5.width, rectangle5.height, rectangle3, baseTransform2);
            float f6 = arrf2[0] * (float)n4;
            float f7 = arrf2[1] * (float)n5;
            if (n9 < 8) {
                f5 = (arrf2[2] - arrf2[0]) * (float)n4 / (float)rectangle3.width;
                f4 = 0.0f;
                f3 = 0.0f;
                f2 = (arrf2[3] - arrf2[1]) * (float)n5 / (float)rectangle3.height;
            } else {
                f5 = (arrf2[4] - arrf2[0]) * (float)n4 / (float)rectangle3.width;
                f4 = (arrf2[5] - arrf2[1]) * (float)n5 / (float)rectangle3.height;
                f3 = (arrf2[6] - arrf2[0]) * (float)n4 / (float)rectangle3.width;
                f2 = (arrf2[7] - arrf2[1]) * (float)n5 / (float)rectangle3.height;
            }
            float[] arrf3 = linearConvolveRenderState.getPassVector();
            float f8 = arrf3[0] * (float)n4;
            float f9 = arrf3[1] * (float)n5;
            float f10 = arrf3[2] * (float)n4;
            float f11 = arrf3[3] * (float)n5;
            this.filterVector(arrn2, n2, n3, n7, arrn, n4, n5, n6, arrf, n8, f6, f7, f10, f11, f8, f9, f5, f4, f3, f2);
        }
        return new ImageData(this.getFilterContext(), heapImage2, rectangle3);
    }

    protected void filterVector(int[] arrn, int n2, int n3, int n4, int[] arrn2, int n5, int n6, int n7, float[] arrf, int n8, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11) {
        int n9 = 0;
        float[] arrf2 = new float[4];
        f2 += (f10 + f8) * 0.5f;
        f3 += (f11 + f9) * 0.5f;
        for (int i2 = 0; i2 < n3; ++i2) {
            float f12 = f2;
            float f13 = f3;
            for (int i3 = 0; i3 < n2; ++i3) {
                arrf2[3] = 0.0f;
                arrf2[2] = 0.0f;
                arrf2[1] = 0.0f;
                arrf2[0] = 0.0f;
                float f14 = f12 + f4;
                float f15 = f13 + f5;
                for (int i4 = 0; i4 < n8; ++i4) {
                    this.laccumsample(arrn2, f14, f15, n5, n6, n7, arrf[i4], arrf2);
                    f14 += f6;
                    f15 += f7;
                }
                arrn[n9 + i3] = ((arrf2[3] < 1.0f ? 0 : (arrf2[3] > 254.9375f ? 255 : (int)arrf2[3])) << 24) + ((arrf2[0] < 1.0f ? 0 : (arrf2[0] > 254.9375f ? 255 : (int)arrf2[0])) << 16) + ((arrf2[1] < 1.0f ? 0 : (arrf2[1] > 254.9375f ? 255 : (int)arrf2[1])) << 8) + (arrf2[2] < 1.0f ? 0 : (arrf2[2] > 254.9375f ? 255 : (int)arrf2[2]));
                f12 += f8;
                f13 += f9;
            }
            f2 += f10;
            f3 += f11;
            n9 += n4;
        }
    }

    protected void filterHV(int[] arrn, int n2, int n3, int n4, int n5, int[] arrn2, int n6, int n7, int n8, int n9, float[] arrf) {
        int n10 = arrf.length / 2;
        float[] arrf2 = new float[n10 * 4];
        int n11 = 0;
        int n12 = 0;
        for (int i2 = 0; i2 < n3; ++i2) {
            int n13;
            int n14 = n11;
            int n15 = n12;
            for (n13 = 0; n13 < arrf2.length; ++n13) {
                arrf2[n13] = 0.0f;
            }
            n13 = n10;
            for (int i3 = 0; i3 < n2; ++i3) {
                int n16 = (n10 - n13) * 4;
                int n17 = i3 < n6 ? arrn2[n15] : 0;
                arrf2[n16 + 0] = n17 >>> 24;
                arrf2[n16 + 1] = n17 >> 16 & 0xFF;
                arrf2[n16 + 2] = n17 >> 8 & 0xFF;
                arrf2[n16 + 3] = n17 & 0xFF;
                if (--n13 <= 0) {
                    n13 += n10;
                }
                float f2 = 0.0f;
                float f3 = 0.0f;
                float f4 = 0.0f;
                float f5 = 0.0f;
                for (n16 = 0; n16 < arrf2.length; n16 += 4) {
                    float f6 = arrf[n13 + (n16 >> 2)];
                    f2 += arrf2[n16 + 0] * f6;
                    f3 += arrf2[n16 + 1] * f6;
                    f4 += arrf2[n16 + 2] * f6;
                    f5 += arrf2[n16 + 3] * f6;
                }
                arrn[n14] = ((f2 < 1.0f ? 0 : (f2 > 254.9375f ? 255 : (int)f2)) << 24) + ((f3 < 1.0f ? 0 : (f3 > 254.9375f ? 255 : (int)f3)) << 16) + ((f4 < 1.0f ? 0 : (f4 > 254.9375f ? 255 : (int)f4)) << 8) + (f5 < 1.0f ? 0 : (f5 > 254.9375f ? 255 : (int)f5));
                n14 += n4;
                n15 += n8;
            }
            n11 += n5;
            n12 += n9;
        }
    }
}

