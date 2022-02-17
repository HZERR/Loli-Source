/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j;

import com.gif4j.GifFrame;
import com.gif4j.GifImage;
import com.gif4j.MorphingFilter;

public class TunnelFilter
extends MorphingFilter {
    private int a;
    private int b;
    private boolean c = false;

    public TunnelFilter(boolean bl) {
        this(bl, 8, 10);
    }

    public TunnelFilter(boolean bl, int n2) {
        this(bl, n2, 10);
    }

    public TunnelFilter(boolean bl, int n2, int n3) {
        this.c = bl;
        if (n2 <= 1) {
            throw new IllegalArgumentException("frames number should be greater than 1.");
        }
        this.a = n2;
        if (n3 < 1) {
            throw new IllegalArgumentException("delay between frames (in 1/100 sec) should be greater than 0.");
        }
        this.b = n3;
    }

    GifFrame[] a(GifImage gifImage, GifFrame gifFrame) {
        int n2;
        int n3 = gifFrame.d / (2 * this.a);
        int n4 = gifFrame.e / (2 * this.a);
        GifFrame[] arrgifFrame = new GifFrame[this.a];
        byte[] arrby = new byte[gifFrame.d];
        for (n2 = 0; n2 < arrby.length; ++n2) {
            arrby[n2] = (byte)gifFrame.r;
        }
        arrgifFrame[0] = gifFrame.f();
        arrgifFrame[0].o = 1;
        arrgifFrame[0].s = this.b;
        for (n2 = 1; n2 < this.a; ++n2) {
            int n5 = n2 * n3;
            int n6 = n2 * n4;
            int n7 = gifFrame.d - 2 * n5;
            int n8 = gifFrame.e - 2 * n6;
            arrgifFrame[n2] = gifFrame.a(n5, n6, n7, n8, false);
            arrgifFrame[n2].b = n5 + gifFrame.b;
            arrgifFrame[n2].c = n6 + gifFrame.c;
            for (int i2 = n4; i2 < n4 + n8; ++i2) {
                System.arraycopy(arrby, 0, arrgifFrame[n2 - 1].n, i2 * arrgifFrame[n2 - 1].d + n3, n7);
            }
            arrgifFrame[n2].o = 1;
            arrgifFrame[n2].s = this.b;
        }
        if (this.c) {
            for (n2 = 0; n2 < this.a / 2; ++n2) {
                GifFrame gifFrame2 = arrgifFrame[n2];
                arrgifFrame[n2] = arrgifFrame[this.a - n2 - 1];
                arrgifFrame[this.a - n2 - 1] = gifFrame2;
            }
        }
        arrgifFrame[this.a - 1].o = gifFrame.o;
        arrgifFrame[this.a - 1].s = gifFrame.s;
        return arrgifFrame;
    }
}

