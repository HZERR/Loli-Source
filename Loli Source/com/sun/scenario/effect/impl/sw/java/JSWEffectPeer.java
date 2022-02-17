/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.sw.java;

import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;

public abstract class JSWEffectPeer<T extends RenderState>
extends EffectPeer<T> {
    protected static final int FVALS_A = 3;
    protected static final int FVALS_R = 0;
    protected static final int FVALS_G = 1;
    protected static final int FVALS_B = 2;

    protected JSWEffectPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    protected final void laccum(int n2, float f2, float[] arrf) {
        arrf[0] = arrf[0] + (float)(n2 >> 16 & 0xFF) * (f2 /= 255.0f);
        arrf[1] = arrf[1] + (float)(n2 >> 8 & 0xFF) * f2;
        arrf[2] = arrf[2] + (float)(n2 & 0xFF) * f2;
        arrf[3] = arrf[3] + (float)(n2 >>> 24) * f2;
    }

    protected final void lsample(int[] arrn, float f2, float f3, int n2, int n3, int n4, float[] arrf) {
        arrf[0] = 0.0f;
        arrf[1] = 0.0f;
        arrf[2] = 0.0f;
        arrf[3] = 0.0f;
        f2 = f2 * (float)n2 + 0.5f;
        f3 = f3 * (float)n3 + 0.5f;
        int n5 = (int)f2;
        int n6 = (int)f3;
        if (f2 > 0.0f && f3 > 0.0f && n5 <= n2 && n6 <= n3) {
            int n7 = n6 * n4 + n5;
            float f4 = (f2 -= (float)n5) * (f3 -= (float)n6);
            if (n6 < n3) {
                if (n5 < n2) {
                    this.laccum(arrn[n7], f4, arrf);
                }
                if (n5 > 0) {
                    this.laccum(arrn[n7 - 1], f3 - f4, arrf);
                }
            }
            if (n6 > 0) {
                if (n5 < n2) {
                    this.laccum(arrn[n7 - n4], f2 - f4, arrf);
                }
                if (n5 > 0) {
                    this.laccum(arrn[n7 - n4 - 1], 1.0f - f2 - f3 + f4, arrf);
                }
            }
        }
    }

    protected final void laccumsample(int[] arrn, float f2, float f3, int n2, int n3, int n4, float f4, float[] arrf) {
        f4 *= 255.0f;
        int n5 = (int)(f2 += 0.5f);
        int n6 = (int)(f3 += 0.5f);
        if (f2 > 0.0f && f3 > 0.0f && n5 <= n2 && n6 <= n3) {
            int n7 = n6 * n4 + n5;
            float f5 = (f2 -= (float)n5) * (f3 -= (float)n6);
            if (n6 < n3) {
                if (n5 < n2) {
                    this.laccum(arrn[n7], f5 * f4, arrf);
                }
                if (n5 > 0) {
                    this.laccum(arrn[n7 - 1], (f3 - f5) * f4, arrf);
                }
            }
            if (n6 > 0) {
                if (n5 < n2) {
                    this.laccum(arrn[n7 - n4], (f2 - f5) * f4, arrf);
                }
                if (n5 > 0) {
                    this.laccum(arrn[n7 - n4 - 1], (1.0f - f2 - f3 + f5) * f4, arrf);
                }
            }
        }
    }

    protected final void faccum(float[] arrf, int n2, float f2, float[] arrf2) {
        arrf2[0] = arrf2[0] + arrf[n2] * f2;
        arrf2[1] = arrf2[1] + arrf[n2 + 1] * f2;
        arrf2[2] = arrf2[2] + arrf[n2 + 2] * f2;
        arrf2[3] = arrf2[3] + arrf[n2 + 3] * f2;
    }

    protected final void fsample(float[] arrf, float f2, float f3, int n2, int n3, int n4, float[] arrf2) {
        arrf2[0] = 0.0f;
        arrf2[1] = 0.0f;
        arrf2[2] = 0.0f;
        arrf2[3] = 0.0f;
        f2 = f2 * (float)n2 + 0.5f;
        f3 = f3 * (float)n3 + 0.5f;
        int n5 = (int)f2;
        int n6 = (int)f3;
        if (f2 > 0.0f && f3 > 0.0f && n5 <= n2 && n6 <= n3) {
            int n7 = 4 * (n6 * n4 + n5);
            float f4 = (f2 -= (float)n5) * (f3 -= (float)n6);
            if (n6 < n3) {
                if (n5 < n2) {
                    this.faccum(arrf, n7, f4, arrf2);
                }
                if (n5 > 0) {
                    this.faccum(arrf, n7 - 4, f3 - f4, arrf2);
                }
            }
            if (n6 > 0) {
                if (n5 < n2) {
                    this.faccum(arrf, n7 - n4 * 4, f2 - f4, arrf2);
                }
                if (n5 > 0) {
                    this.faccum(arrf, n7 - n4 * 4 - 4, 1.0f - f2 - f3 + f4, arrf2);
                }
            }
        }
    }
}

