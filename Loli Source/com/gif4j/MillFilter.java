/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j;

import com.gif4j.GifFrame;
import com.gif4j.GifImage;
import com.gif4j.MorphingFilter;

public class MillFilter
extends MorphingFilter {
    private int a;
    private int b;

    public MillFilter() {
        this(8, 10);
    }

    public MillFilter(int n2) {
        this(n2, 10);
    }

    public MillFilter(int n2, int n3) {
        if (n2 <= 1) {
            throw new IllegalArgumentException("frames number should be greater than 1.");
        }
        this.b = n2;
        if (n3 < 1) {
            throw new IllegalArgumentException("delay between frames (in 1/100 sec) should be greater than 0.");
        }
        this.a = n3;
    }

    GifFrame[] a(GifImage gifImage, GifFrame gifFrame) {
        int n2;
        GifFrame[] arrgifFrame = new GifFrame[this.b];
        for (n2 = 0; n2 < this.b; ++n2) {
            arrgifFrame[n2] = gifFrame.a(true);
            arrgifFrame[n2].o = 1;
            arrgifFrame[n2].s = this.a;
        }
        arrgifFrame[this.b - 1].o = gifFrame.o;
        arrgifFrame[this.b - 1].s = gifFrame.s;
        n2 = gifFrame.d / 2 + gifFrame.d % 2;
        int n3 = gifFrame.e / 2 + gifFrame.e % 2;
        int n4 = gifFrame.d / this.b + (gifFrame.d % this.b != 0 ? 1 : 0);
        int n5 = gifFrame.e / this.b + (gifFrame.e % this.b != 0 ? 1 : 0);
        double d2 = (double)gifFrame.d / (double)gifFrame.e;
        int n6 = 0;
        for (int i2 = gifFrame.e - 1; n6 <= n3 || i2 >= n3; ++n6, --i2) {
            int n7 = 0;
            for (int i3 = gifFrame.d - 1; n7 <= n2 || i3 >= n2; ++n7, --i3) {
                double d3;
                int n8 = 0;
                if (n6 != n3 && (n8 = (d3 = (double)(n2 - n7) / (double)(n3 - n6)) < d2 ? this.b - (int)(d3 * (double)n3 / (double)n4) - 1 : (int)(1.0 / d3 * (double)n2 / (double)n5)) >= this.b) {
                    System.out.println("");
                }
                int n9 = n6 * gifFrame.d + n7;
                arrgifFrame[n8].n[n9] = gifFrame.n[n9];
                n9 = i2 * gifFrame.d + n7;
                arrgifFrame[this.b - n8 - 1].n[n9] = gifFrame.n[n9];
                n9 = n6 * gifFrame.d + i3;
                arrgifFrame[this.b - n8 - 1].n[n9] = gifFrame.n[n9];
                n9 = i2 * gifFrame.d + i3;
                arrgifFrame[n8].n[n9] = gifFrame.n[n9];
            }
        }
        return arrgifFrame;
    }
}

