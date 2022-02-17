/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j;

import com.gif4j.GifFrame;
import com.gif4j.GifImage;
import com.gif4j.MorphingFilter;

public class SnakeFilter
extends MorphingFilter {
    public static final int LAY_METHOD_SPIRAL_FROM_CENTER = 0;
    public static final int LAY_METHOD_SPIRAL_FROM_CORNER = 1;
    public static final int LAY_METHOD_ZIG_ZAG = 2;
    public static final int LAY_METHOD_LEFT_RIGHT_DIRECT = 3;
    public static final int LAY_METHOD_LEFT_RIGHT_INVERSE = 4;
    int a;
    int b;
    int c;

    public SnakeFilter(int n2) {
        this(n2, 5, 6);
    }

    public SnakeFilter(int n2, int n3) {
        this(n2, n3, 10);
    }

    public SnakeFilter(int n2, int n3, int n4) {
        if (n2 < 0 || n2 > 4) {
            throw new IllegalArgumentException("Unknown layMethod");
        }
        if (n3 < 2) {
            throw new IllegalArgumentException("devide factor must be greater than 1");
        }
        if (n4 < 0) {
            throw new IllegalArgumentException("delay between frames must be greater than 1");
        }
        this.a = n2;
        this.b = n3;
        this.c = n4;
    }

    GifFrame[] a(GifImage gifImage, GifFrame gifFrame) {
        int n2;
        int n3;
        int n4;
        int n5 = this.b * this.b;
        GifFrame[] arrgifFrame = new GifFrame[n5];
        int n6 = gifFrame.d / this.b;
        int n7 = gifFrame.d % this.b;
        int n8 = gifFrame.e / this.b;
        int n9 = gifFrame.e % this.b;
        int n10 = 0;
        for (int i2 = 0; i2 < this.b; ++i2) {
            n10 += i2 == 0 ? 0 : arrgifFrame[i2 * this.b - 1].e;
            n4 = 0;
            for (n3 = 0; n3 < this.b; ++n3) {
                n2 = i2 * this.b + n3;
                GifFrame gifFrame2 = new GifFrame();
                gifFrame2.d = n6 + (n3 < n7 ? 1 : 0);
                gifFrame2.e = n8 + (i2 < n9 ? 1 : 0);
                gifFrame2.n = new byte[gifFrame2.d * gifFrame2.e];
                gifFrame2.c = n10;
                gifFrame2.b = n4;
                n4 += gifFrame2.d;
                gifFrame2.s = this.c;
                gifFrame2.o = 1;
                gifFrame2.x = true;
                gifFrame2.i = gifFrame.i;
                gifFrame2.j = gifFrame.j;
                if (gifFrame.f) {
                    gifFrame2.k = new byte[gifFrame.k.length];
                    gifFrame2.l = new byte[gifFrame.l.length];
                    gifFrame2.m = new byte[gifFrame.m.length];
                    System.arraycopy(gifFrame.k, 0, gifFrame2.k, 0, gifFrame.k.length);
                    System.arraycopy(gifFrame.l, 0, gifFrame2.l, 0, gifFrame.l.length);
                    System.arraycopy(gifFrame.m, 0, gifFrame2.m, 0, gifFrame.m.length);
                } else {
                    gifFrame2.f = false;
                }
                gifFrame2.q = gifFrame.q;
                gifFrame2.r = gifFrame.r;
                int n11 = gifFrame2.c + gifFrame2.e;
                int n12 = gifFrame2.b + gifFrame2.d;
                int n13 = 0;
                for (int i3 = gifFrame2.c; i3 < n11; ++i3) {
                    int n14 = 0;
                    for (int i4 = gifFrame2.b; i4 < n12; ++i4) {
                        gifFrame2.n[n13 * gifFrame2.d + n14] = gifFrame.n[i3 * gifFrame.d + i4];
                        ++n14;
                    }
                    ++n13;
                }
                gifFrame2.c += gifFrame.c;
                gifFrame2.b += gifFrame.b;
                arrgifFrame[n2] = gifFrame2;
            }
        }
        GifFrame[] arrgifFrame2 = new GifFrame[n5];
        int[] arrn = new int[n5];
        n3 = 0;
        switch (this.a) {
            case 0: {
                for (n4 = 1; n4 <= (this.b + 1) / 2; ++n4) {
                    for (n2 = n4; n2 <= this.b - n4 + 1; ++n2) {
                        arrn[n3++] = (n2 - 1) * this.b + n4 - 1;
                    }
                    for (n2 = n4 + 1; n2 <= this.b - n4 + 1; ++n2) {
                        arrn[n3++] = (this.b - n4) * this.b + n2 - 1;
                    }
                    for (n2 = this.b - n4; n2 >= n4; --n2) {
                        arrn[n3++] = (n2 - 1) * this.b + this.b - n4;
                    }
                    for (n2 = this.b - n4; n2 >= n4 + 1; --n2) {
                        arrn[n3++] = (n4 - 1) * this.b + n2 - 1;
                    }
                }
                for (n3 = 0; n3 < arrgifFrame2.length; ++n3) {
                    arrgifFrame2[n3] = arrgifFrame[arrn[n5 - n3 - 1]];
                }
                arrgifFrame2[n5 - 1].s = gifFrame.s;
                arrgifFrame2[n5 - 1].o = gifFrame.o;
                return arrgifFrame2;
            }
            case 1: {
                for (n4 = 1; n4 <= (this.b + 1) / 2; ++n4) {
                    for (n2 = n4; n2 <= this.b - n4 + 1; ++n2) {
                        arrn[n3++] = (n2 - 1) * this.b + n4 - 1;
                    }
                    for (n2 = n4 + 1; n2 <= this.b - n4 + 1; ++n2) {
                        arrn[n3++] = (this.b - n4) * this.b + n2 - 1;
                    }
                    for (n2 = this.b - n4; n2 >= n4; --n2) {
                        arrn[n3++] = (n2 - 1) * this.b + this.b - n4;
                    }
                    for (n2 = this.b - n4; n2 >= n4 + 1; --n2) {
                        arrn[n3++] = (n4 - 1) * this.b + n2 - 1;
                    }
                }
                for (n3 = 0; n3 < arrgifFrame2.length; ++n3) {
                    arrgifFrame2[n3] = arrgifFrame[arrn[n3]];
                }
                arrgifFrame2[n5 - 1].s = gifFrame.s;
                arrgifFrame2[n5 - 1].o = gifFrame.o;
                return arrgifFrame2;
            }
            case 2: {
                for (n4 = 0; n4 < this.b; ++n4) {
                    if (n4 % 2 == 1) {
                        for (n2 = 0; n2 <= n4; ++n2) {
                            arrn[n3++] = n4 + n2 * (this.b - 1);
                        }
                        continue;
                    }
                    for (n2 = n4; n2 >= 0; --n2) {
                        arrn[n3++] = n4 + n2 * (this.b - 1);
                    }
                }
                for (n4 = 2; n4 <= this.b; ++n4) {
                    if (n4 % 2 != this.b % 2) {
                        for (n2 = 0; n2 <= this.b - n4; ++n2) {
                            arrn[n3++] = n4 * this.b - 1 + n2 * (this.b - 1);
                        }
                        continue;
                    }
                    for (n2 = this.b - n4; n2 >= 0; --n2) {
                        arrn[n3++] = n4 * this.b - 1 + n2 * (this.b - 1);
                    }
                }
                for (n3 = 0; n3 < arrgifFrame2.length; ++n3) {
                    arrgifFrame2[n3] = arrgifFrame[arrn[n3]];
                }
                arrgifFrame2[n5 - 1].s = gifFrame.s;
                arrgifFrame2[n5 - 1].o = gifFrame.o;
                return arrgifFrame2;
            }
            case 4: {
                for (n4 = 0; n4 < this.b; ++n4) {
                    if (n4 % 2 == 0) {
                        for (n2 = 0; n2 < this.b; ++n2) {
                            arrn[n3++] = n4 * this.b + n2;
                        }
                        continue;
                    }
                    for (n2 = this.b - 1; n2 >= 0; --n2) {
                        arrn[n3++] = n4 * this.b + n2;
                    }
                }
                for (n3 = 0; n3 < arrgifFrame2.length; ++n3) {
                    arrgifFrame2[n3] = arrgifFrame[arrn[n3]];
                }
                arrgifFrame2[n5 - 1].s = gifFrame.s;
                arrgifFrame2[n5 - 1].o = gifFrame.o;
                return arrgifFrame2;
            }
            case 3: {
                arrgifFrame[n5 - 1].s = gifFrame.s;
                arrgifFrame[n5 - 1].o = gifFrame.o;
            }
        }
        return arrgifFrame;
    }
}

