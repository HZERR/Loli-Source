/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j;

import com.gif4j.GifFrame;
import com.gif4j.GifImage;
import com.gif4j.MorphingFilter;

public class RadarFilter
extends MorphingFilter {
    private int a;
    private int b;

    public RadarFilter() {
        this(2, 10);
    }

    public RadarFilter(int n2) {
        this(n2, 10);
    }

    public RadarFilter(int n2, int n3) {
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
        int n3;
        int n4;
        int n5;
        int n6;
        double d2;
        int n7;
        int n8;
        if (this.b % 2 == 1) {
            ++this.b;
        }
        GifFrame[] arrgifFrame = new GifFrame[this.b * 4];
        int n9 = gifFrame.d / 2 + gifFrame.d % 2;
        int n10 = gifFrame.e / 2 + gifFrame.e % 2;
        for (n8 = 0; n8 < this.b; ++n8) {
            arrgifFrame[n8] = gifFrame.a(0, 0, n9, n10, true);
            arrgifFrame[n8].b = gifFrame.b;
            arrgifFrame[n8].c = gifFrame.c;
            arrgifFrame[n8].o = 1;
            arrgifFrame[n8].s = this.a;
        }
        for (n8 = this.b; n8 < 2 * this.b; ++n8) {
            arrgifFrame[n8] = gifFrame.a(gifFrame.d / 2, 0, n9, n10, true);
            arrgifFrame[n8].b = gifFrame.d / 2 + gifFrame.b;
            arrgifFrame[n8].c = gifFrame.c;
            arrgifFrame[n8].o = 1;
            arrgifFrame[n8].s = this.a;
        }
        for (n8 = 2 * this.b; n8 < 3 * this.b; ++n8) {
            arrgifFrame[n8] = gifFrame.a(gifFrame.d / 2, gifFrame.e / 2, n9, n10, true);
            arrgifFrame[n8].b = gifFrame.d / 2 + gifFrame.b;
            arrgifFrame[n8].c = gifFrame.e / 2 + gifFrame.c;
            arrgifFrame[n8].o = 1;
            arrgifFrame[n8].s = this.a;
        }
        for (n8 = 3 * this.b; n8 < 4 * this.b; ++n8) {
            arrgifFrame[n8] = gifFrame.a(0, gifFrame.e / 2, n9, n10, true);
            arrgifFrame[n8].b = gifFrame.b;
            arrgifFrame[n8].c = gifFrame.e / 2 + gifFrame.c;
            arrgifFrame[n8].o = 1;
            arrgifFrame[n8].s = this.a;
        }
        double d3 = (double)n9 / (double)n10;
        for (n7 = 0; n7 < n9; ++n7) {
            d2 = (double)(n9 - n7) / d3;
            for (n6 = 0; n6 < this.b / 2; ++n6) {
                n5 = n10 - (int)(d2 / (double)(this.b / 2) * (double)n6);
                for (n4 = n10 - (int)(d2 / (double)(this.b / 2) * (double)(n6 + 1)); n4 < n5; ++n4) {
                    arrgifFrame[n6].n[n4 * n9 + n7] = gifFrame.n[n4 * gifFrame.d + n7];
                    n3 = gifFrame.d - n7 - 1;
                    arrgifFrame[2 * this.b - n6 - 1].n[n4 * n9 + n3 - gifFrame.d / 2] = gifFrame.n[n4 * gifFrame.d + n3];
                    n2 = gifFrame.e - n4 - 1;
                    arrgifFrame[4 * this.b - n6 - 1].n[(n2 - gifFrame.e / 2) * n9 + n7] = gifFrame.n[n2 * gifFrame.d + n7];
                    arrgifFrame[2 * this.b + n6].n[(n2 - gifFrame.e / 2) * n9 + n3 - gifFrame.d / 2] = gifFrame.n[n2 * gifFrame.d + n3];
                }
            }
        }
        for (n7 = 0; n7 < n10; ++n7) {
            d2 = (double)(n10 - n7) * d3;
            for (n6 = 0; n6 < this.b / 2; ++n6) {
                n5 = (int)(d2 / (double)(this.b / 2) * (double)(n6 + 1) + (double)n9 - d2);
                if (n6 + 1 == this.b / 2) {
                    n5 = n9;
                }
                for (n4 = (int)(d2 / (double)(this.b / 2) * (double)n6 + (double)n9 - d2); n4 < n5; ++n4) {
                    arrgifFrame[n6 + this.b / 2].n[n7 * n9 + n4] = gifFrame.n[n7 * gifFrame.d + n4];
                    n3 = gifFrame.d - n4 - 1;
                    arrgifFrame[2 * this.b - n6 - this.b / 2 - 1].n[n7 * n9 + n3 - gifFrame.d / 2] = gifFrame.n[n7 * gifFrame.d + n3];
                    n2 = gifFrame.e - n7 - 1;
                    arrgifFrame[4 * this.b - n6 - this.b / 2 - 1].n[(n2 - gifFrame.e / 2) * n9 + n4] = gifFrame.n[n2 * gifFrame.d + n4];
                    arrgifFrame[n6 + this.b / 2 + 2 * this.b].n[(n2 - gifFrame.e / 2) * n9 + n3 - gifFrame.d / 2] = gifFrame.n[n2 * gifFrame.d + n3];
                }
            }
        }
        GifFrame[] arrgifFrame2 = new GifFrame[4 * this.b];
        for (int i2 = 0; i2 < 4 * this.b; ++i2) {
            arrgifFrame2[i2] = arrgifFrame[(i2 + this.b) % (4 * this.b)];
        }
        arrgifFrame2[4 * this.b - 1].s = gifFrame.s;
        return arrgifFrame2;
    }
}

