/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j;

import com.gif4j.GifFrame;
import com.gif4j.GifImage;
import com.gif4j.MorphingFilter;

public class CellFilter
extends MorphingFilter {
    private int a;
    private int b;

    public CellFilter() {
        this(8, 10);
    }

    public CellFilter(int n2) {
        this(n2, 10);
    }

    public CellFilter(int n2, int n3) {
        if (n2 <= 1) {
            throw new IllegalArgumentException("cell side size (in pixels) should be greater than 1.");
        }
        this.a = n2;
        if (n3 < 1) {
            throw new IllegalArgumentException("delay between frames (in 1/100 sec) should be greater than 0.");
        }
        this.b = n3;
    }

    GifFrame[] a(GifImage gifImage, GifFrame gifFrame) {
        GifFrame[] arrgifFrame = new GifFrame[4];
        arrgifFrame[0] = gifFrame.a(true);
        arrgifFrame[0].s = this.b;
        arrgifFrame[0].o = 1;
        arrgifFrame[1] = arrgifFrame[0].f();
        arrgifFrame[2] = arrgifFrame[0].f();
        arrgifFrame[3] = arrgifFrame[0].f();
        arrgifFrame[3].s = gifFrame.s;
        arrgifFrame[3].o = gifFrame.o;
        for (int i2 = 0; i2 < gifFrame.e; ++i2) {
            for (int i3 = 0; i3 < gifFrame.d; ++i3) {
                int n2 = i3 / this.a % 2;
                int n3 = i2 / this.a % 2;
                int n4 = i2 * gifFrame.d + i3;
                arrgifFrame[n3 << 1 | n2].n[n4] = gifFrame.n[n4];
            }
        }
        return arrgifFrame;
    }
}

