/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.sw.java;

import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.LinearConvolveRenderState;
import com.sun.scenario.effect.impl.sw.java.JSWLinearConvolvePeer;

public class JSWLinearConvolveShadowPeer
extends JSWLinearConvolvePeer {
    public JSWLinearConvolveShadowPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    private float[] getShadowColor() {
        return ((LinearConvolveRenderState)this.getRenderState()).getPassShadowColorComponents();
    }

    @Override
    protected void filterVector(int[] arrn, int n2, int n3, int n4, int[] arrn2, int n5, int n6, int n7, float[] arrf, int n8, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11) {
        float[] arrf2 = this.getShadowColor();
        int n9 = 0;
        f2 += (f10 + f8) * 0.5f;
        f3 += (f11 + f9) * 0.5f;
        for (int i2 = 0; i2 < n3; ++i2) {
            float f12 = f2;
            float f13 = f3;
            for (int i3 = 0; i3 < n2; ++i3) {
                float f14 = 0.0f;
                float f15 = f12 + f4;
                float f16 = f13 + f5;
                for (int i4 = 0; i4 < n8; ++i4) {
                    if (f15 >= 0.0f && f16 >= 0.0f) {
                        int n10 = (int)f15;
                        int n11 = (int)f16;
                        if (n10 < n5 && n11 < n6) {
                            int n12 = arrn2[n11 * n7 + n10];
                            f14 += (float)(n12 >>> 24) * arrf[i4];
                        }
                    }
                    f15 += f6;
                    f16 += f7;
                }
                f14 = f14 < 0.0f ? 0.0f : (f14 > 255.0f ? 255.0f : f14);
                arrn[n9 + i3] = (int)(arrf2[0] * f14) << 16 | (int)(arrf2[1] * f14) << 8 | (int)(arrf2[2] * f14) | (int)(arrf2[3] * f14) << 24;
                f12 += f8;
                f13 += f9;
            }
            f2 += f10;
            f3 += f11;
            n9 += n4;
        }
    }

    @Override
    protected void filterHV(int[] arrn, int n2, int n3, int n4, int n5, int[] arrn2, int n6, int n7, int n8, int n9, float[] arrf) {
        int n10;
        float[] arrf2 = this.getShadowColor();
        int n11 = arrf.length / 2;
        float[] arrf3 = new float[n11];
        int n12 = 0;
        int n13 = 0;
        int[] arrn3 = new int[256];
        for (n10 = 0; n10 < arrn3.length; ++n10) {
            arrn3[n10] = (int)(arrf2[0] * (float)n10) << 16 | (int)(arrf2[1] * (float)n10) << 8 | (int)(arrf2[2] * (float)n10) | (int)(arrf2[3] * (float)n10) << 24;
        }
        for (n10 = 0; n10 < n3; ++n10) {
            int n14;
            int n15 = n12;
            int n16 = n13;
            for (n14 = 0; n14 < arrf3.length; ++n14) {
                arrf3[n14] = 0.0f;
            }
            n14 = n11;
            for (int i2 = 0; i2 < n2; ++i2) {
                arrf3[n11 - n14] = (i2 < n6 ? arrn2[n16] : 0) >>> 24;
                if (--n14 <= 0) {
                    n14 += n11;
                }
                float f2 = -0.5f;
                for (int i3 = 0; i3 < arrf3.length; ++i3) {
                    f2 += arrf3[i3] * arrf[n14 + i3];
                }
                arrn[n15] = f2 < 0.0f ? 0 : (f2 >= 254.0f ? arrn3[255] : arrn3[(int)f2 + 1]);
                n15 += n4;
                n16 += n8;
            }
            n12 += n5;
            n13 += n9;
        }
    }
}

