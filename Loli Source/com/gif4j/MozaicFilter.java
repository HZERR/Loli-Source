/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j;

import com.gif4j.GifFrame;
import com.gif4j.GifImage;
import com.gif4j.MorphingFilter;
import java.util.Random;

public class MozaicFilter
extends MorphingFilter {
    private int a;
    private int b;
    private int c;

    public MozaicFilter() {
        this(4, 4, 10);
    }

    public MozaicFilter(int n2) {
        this(n2, 4, 10);
    }

    public MozaicFilter(int n2, int n3) {
        this(n2, n3, 10);
    }

    public MozaicFilter(int n2, int n3, int n4) {
        if (n2 <= 1) {
            throw new IllegalArgumentException("mozaic box size should be greater than 1");
        }
        if (n3 <= 1) {
            throw new IllegalArgumentException("number of frames should be greater than 1");
        }
        if (n4 < 1) {
            throw new IllegalArgumentException("delay between frames (in 1/100 sec) should be greater than or equal to 1");
        }
        this.a = n2;
        this.b = n3;
        this.c = n4;
    }

    GifFrame[] a(GifImage gifImage, GifFrame gifFrame) {
        int n2;
        GifFrame[] arrgifFrame = new GifFrame[this.b];
        arrgifFrame[0] = gifFrame.a(true);
        arrgifFrame[0].s = this.c;
        arrgifFrame[0].o = 1;
        for (n2 = 1; n2 < this.b; ++n2) {
            arrgifFrame[n2] = arrgifFrame[0].f();
            arrgifFrame[n2].s = this.c;
            arrgifFrame[n2].o = 1;
        }
        arrgifFrame[this.b - 1].s = gifFrame.s;
        arrgifFrame[this.b - 1].o = gifFrame.o;
        n2 = gifFrame.d / this.a * this.a;
        int n3 = gifFrame.e / this.a * this.a;
        if (n2 != gifFrame.d) {
            n2 += this.a;
        }
        if (n3 != gifFrame.e) {
            n3 += this.a;
        }
        Random random = new Random();
        for (int i2 = 0; i2 < n3; i2 += this.a) {
            for (int i3 = 0; i3 < n2; i3 += this.a) {
                int n4 = random.nextInt(this.b);
                int n5 = i3 + this.a <= gifFrame.d ? this.a : gifFrame.d - i3;
                int n6 = i2 + this.a <= gifFrame.e ? this.a : gifFrame.e - i2;
                for (int i4 = 0; i4 < n6; ++i4) {
                    for (int i5 = 0; i5 < n5; ++i5) {
                        int n7 = (i2 + i4) * gifFrame.d + i3 + i5;
                        arrgifFrame[n4].n[n7] = gifFrame.n[n7];
                    }
                }
            }
        }
        return arrgifFrame;
    }
}

