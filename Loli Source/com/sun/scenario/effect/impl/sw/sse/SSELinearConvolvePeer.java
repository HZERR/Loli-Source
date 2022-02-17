/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.sw.sse;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.LinearConvolveRenderState;
import com.sun.scenario.effect.impl.sw.sse.SSEEffectPeer;
import java.nio.FloatBuffer;

public class SSELinearConvolvePeer
extends SSEEffectPeer<LinearConvolveRenderState> {
    public SSELinearConvolvePeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    @Override
    public ImageData filter(Effect effect, LinearConvolveRenderState linearConvolveRenderState, BaseTransform baseTransform, Rectangle rectangle, ImageData ... arrimageData) {
        this.setRenderState(linearConvolveRenderState);
        Rectangle rectangle2 = arrimageData[0].getTransformedBounds(null);
        Rectangle rectangle3 = linearConvolveRenderState.getPassResultBounds(rectangle2, null);
        Rectangle rectangle4 = linearConvolveRenderState.getPassResultBounds(rectangle2, rectangle);
        this.setDestBounds(rectangle4);
        int n2 = rectangle4.width;
        int n3 = rectangle4.height;
        HeapImage heapImage = (HeapImage)arrimageData[0].getUntransformedImage();
        int n4 = heapImage.getPhysicalWidth();
        int n5 = heapImage.getPhysicalHeight();
        int n6 = heapImage.getScanlineStride();
        int[] arrn = heapImage.getPixelArray();
        Rectangle rectangle5 = arrimageData[0].getUntransformedBounds();
        BaseTransform baseTransform2 = arrimageData[0].getTransform();
        Rectangle rectangle6 = new Rectangle(0, 0, n4, n5);
        this.setInputBounds(0, rectangle5);
        this.setInputTransform(0, baseTransform2);
        this.setInputNativeBounds(0, rectangle6);
        HeapImage heapImage2 = (HeapImage)((Object)this.getRenderer().getCompatibleImage(n2, n3));
        this.setDestNativeBounds(heapImage2.getPhysicalWidth(), heapImage2.getPhysicalHeight());
        int n7 = heapImage2.getScanlineStride();
        int[] arrn2 = heapImage2.getPixelArray();
        int n8 = linearConvolveRenderState.getPassKernelSize();
        FloatBuffer floatBuffer = linearConvolveRenderState.getPassWeights();
        LinearConvolveRenderState.PassType passType = linearConvolveRenderState.getPassType();
        if (!baseTransform2.isIdentity() || !rectangle4.contains(rectangle3.x, rectangle3.y)) {
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
            int n9 = this.getTextureCoordinates(0, arrf2, rectangle5.x, rectangle5.y, rectangle6.width, rectangle6.height, rectangle4, baseTransform2);
            float f6 = arrf2[0] * (float)n4;
            float f7 = arrf2[1] * (float)n5;
            if (n9 < 8) {
                f5 = (arrf2[2] - arrf2[0]) * (float)n4 / (float)rectangle4.width;
                f4 = 0.0f;
                f3 = 0.0f;
                f2 = (arrf2[3] - arrf2[1]) * (float)n5 / (float)rectangle4.height;
            } else {
                f5 = (arrf2[4] - arrf2[0]) * (float)n4 / (float)rectangle4.width;
                f4 = (arrf2[5] - arrf2[1]) * (float)n5 / (float)rectangle4.height;
                f3 = (arrf2[6] - arrf2[0]) * (float)n4 / (float)rectangle4.width;
                f2 = (arrf2[7] - arrf2[1]) * (float)n5 / (float)rectangle4.height;
            }
            float[] arrf3 = linearConvolveRenderState.getPassVector();
            float f8 = arrf3[0] * (float)n4;
            float f9 = arrf3[1] * (float)n5;
            float f10 = arrf3[2] * (float)n4;
            float f11 = arrf3[3] * (float)n5;
            this.filterVector(arrn2, n2, n3, n7, arrn, n4, n5, n6, arrf, n8, f6, f7, f10, f11, f8, f9, f5, f4, f3, f2);
        }
        return new ImageData(this.getFilterContext(), heapImage2, rectangle4);
    }

    native void filterVector(int[] var1, int var2, int var3, int var4, int[] var5, int var6, int var7, int var8, float[] var9, int var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, float var18, float var19, float var20);

    native void filterHV(int[] var1, int var2, int var3, int var4, int var5, int[] var6, int var7, int var8, int var9, int var10, float[] var11);
}

