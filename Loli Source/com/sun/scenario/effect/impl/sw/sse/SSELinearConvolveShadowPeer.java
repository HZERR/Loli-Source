/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.sw.sse;

import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.LinearConvolveRenderState;
import com.sun.scenario.effect.impl.sw.sse.SSELinearConvolvePeer;

public class SSELinearConvolveShadowPeer
extends SSELinearConvolvePeer {
    public SSELinearConvolveShadowPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    private float[] getShadowColor() {
        return ((LinearConvolveRenderState)this.getRenderState()).getPassShadowColorComponents();
    }

    private static native void filterVector(int[] var0, int var1, int var2, int var3, int[] var4, int var5, int var6, int var7, float[] var8, int var9, float var10, float var11, float var12, float var13, float var14, float var15, float[] var16, float var17, float var18, float var19, float var20);

    @Override
    void filterVector(int[] arrn, int n2, int n3, int n4, int[] arrn2, int n5, int n6, int n7, float[] arrf, int n8, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11) {
        SSELinearConvolveShadowPeer.filterVector(arrn, n2, n3, n4, arrn2, n5, n6, n7, arrf, n8, f2, f3, f4, f5, f6, f7, this.getShadowColor(), f8, f9, f10, f11);
    }

    private static native void filterHV(int[] var0, int var1, int var2, int var3, int var4, int[] var5, int var6, int var7, int var8, int var9, float[] var10, float[] var11);

    @Override
    void filterHV(int[] arrn, int n2, int n3, int n4, int n5, int[] arrn2, int n6, int n7, int n8, int n9, float[] arrf) {
        SSELinearConvolveShadowPeer.filterHV(arrn, n2, n3, n4, n5, arrn2, n6, n7, n8, n9, arrf, this.getShadowColor());
    }
}

