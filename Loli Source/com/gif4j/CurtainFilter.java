/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j;

import com.gif4j.GifFrame;
import com.gif4j.GifImage;
import com.gif4j.MorphingFilter;

public class CurtainFilter
extends MorphingFilter {
    public static final int MOVE_FROM_LEFT_RIGHT_TO_CENTER = 0;
    public static final int MOVE_FROM_CENTER_TO_LEFT_RIGHT = 1;
    public static final int MOVE_FROM_LEFT_TO_RIGHT = 2;
    public static final int MOVE_FROM_RIGHT_TO_LEFT = 3;
    public static final int MOVE_FROM_TOP_BOTTOM_TO_MIDDLE = 4;
    public static final int MOVE_FROM_MIDDLE_TO_TOP_BOTTOM = 5;
    public static final int MOVE_FROM_TOP_TO_BOTTOM = 6;
    public static final int MOVE_FROM_BOTTOM_TO_TOP = 7;
    private int a;
    private int b;
    private int c;

    public CurtainFilter(int n2) {
        this(n2, 8, 10);
    }

    public CurtainFilter(int n2, int n3) {
        this(n2, n3, 10);
    }

    public CurtainFilter(int n2, int n3, int n4) {
        if (n3 <= 1) {
            throw new IllegalArgumentException("frames number should be greater than 1.");
        }
        this.a = n3;
        if (n4 < 1) {
            throw new IllegalArgumentException("delay beetwen frames (in 1/100 sec) should be greater than 0.");
        }
        if (n2 < 0 || n2 > 7) {
            throw new IllegalArgumentException("unknown moveFromTo parameter.");
        }
        this.b = n4;
        this.c = n2;
    }

    GifFrame[] a(GifImage gifImage, GifFrame gifFrame) {
        int n2;
        GifFrame[] arrgifFrame = new GifFrame[this.a];
        byte[] arrby = new byte[gifFrame.d > gifFrame.e ? gifFrame.d : gifFrame.e];
        for (n2 = 0; n2 < arrby.length; ++n2) {
            arrby[n2] = (byte)gifFrame.r;
        }
        switch (this.c) {
            case 0: {
                n2 = gifFrame.d / (2 * this.a);
                arrgifFrame[0] = gifFrame.f();
                arrgifFrame[0].o = 1;
                arrgifFrame[0].s = this.b;
                for (int i2 = 1; i2 < this.a; ++i2) {
                    int n3 = i2 * n2;
                    int n4 = 0;
                    int n5 = gifFrame.d - 2 * n3;
                    int n6 = gifFrame.e;
                    arrgifFrame[i2] = gifFrame.a(n3, n4, n5, n6, false);
                    arrgifFrame[i2].b = n3 + gifFrame.b;
                    arrgifFrame[i2].c = n4 + gifFrame.c;
                    for (int i3 = 0; i3 < n6; ++i3) {
                        System.arraycopy(arrby, 0, arrgifFrame[i2 - 1].n, i3 * arrgifFrame[i2 - 1].d + n2, n5);
                    }
                    arrgifFrame[i2].o = 1;
                    arrgifFrame[i2].s = this.b;
                }
                arrgifFrame[this.a - 1].o = gifFrame.o;
                arrgifFrame[this.a - 1].s = gifFrame.s;
                return arrgifFrame;
            }
            case 1: {
                int n7;
                n2 = gifFrame.d / (2 * this.a);
                arrgifFrame[0] = gifFrame.f();
                arrgifFrame[0].o = 1;
                arrgifFrame[0].s = this.b;
                for (int i4 = 1; i4 < this.a; ++i4) {
                    n7 = i4 * n2;
                    int n8 = 0;
                    int n9 = gifFrame.d - 2 * n7;
                    int n10 = gifFrame.e;
                    arrgifFrame[i4] = gifFrame.a(n7, n8, n9, n10, false);
                    arrgifFrame[i4].b = n7 + gifFrame.b;
                    arrgifFrame[i4].c = n8 + gifFrame.c;
                    for (int i5 = 0; i5 < n10; ++i5) {
                        System.arraycopy(arrby, 0, arrgifFrame[i4 - 1].n, i5 * arrgifFrame[i4 - 1].d + n2, n9);
                    }
                    arrgifFrame[i4].o = 1;
                    arrgifFrame[i4].s = this.b;
                }
                GifFrame[] arrgifFrame2 = new GifFrame[this.a];
                for (n7 = 0; n7 < this.a; ++n7) {
                    arrgifFrame2[n7] = arrgifFrame[this.a - n7 - 1];
                }
                arrgifFrame2[this.a - 1].o = gifFrame.o;
                arrgifFrame2[this.a - 1].s = gifFrame.s;
                return arrgifFrame2;
            }
            case 2: {
                n2 = gifFrame.d / this.a;
                int n11 = gifFrame.d % this.a;
                int n12 = 0;
                for (int i6 = 0; i6 < this.a; ++i6) {
                    int n13 = n2 + (n11 > 0 ? 1 : 0);
                    arrgifFrame[i6] = gifFrame.a(n12, 0, n13, gifFrame.e, false);
                    arrgifFrame[i6].b = n12 + gifFrame.b;
                    arrgifFrame[i6].c = gifFrame.c;
                    n12 += n13;
                    --n11;
                    arrgifFrame[i6].o = 1;
                    arrgifFrame[i6].s = this.b;
                }
                arrgifFrame[this.a - 1].o = gifFrame.o;
                arrgifFrame[this.a - 1].s = gifFrame.s;
                return arrgifFrame;
            }
            case 3: {
                n2 = gifFrame.d / this.a;
                int n14 = gifFrame.d % this.a;
                int n15 = gifFrame.d;
                for (int i7 = 0; i7 < this.a; ++i7) {
                    int n16 = n2 + (n14 > 0 ? 1 : 0);
                    arrgifFrame[i7] = gifFrame.a(n15 -= n16, 0, n16, gifFrame.e, false);
                    arrgifFrame[i7].b = n15 + gifFrame.b;
                    arrgifFrame[i7].c = gifFrame.c;
                    --n14;
                    arrgifFrame[i7].o = 1;
                    arrgifFrame[i7].s = this.b;
                }
                arrgifFrame[this.a - 1].o = gifFrame.o;
                arrgifFrame[this.a - 1].s = gifFrame.s;
                return arrgifFrame;
            }
            case 4: {
                n2 = gifFrame.e / (2 * this.a);
                arrgifFrame[0] = gifFrame.f();
                arrgifFrame[0].o = 1;
                arrgifFrame[0].s = this.b;
                for (int i8 = 1; i8 < this.a; ++i8) {
                    int n17 = 0;
                    int n18 = i8 * n2;
                    int n19 = gifFrame.d;
                    int n20 = gifFrame.e - 2 * n18;
                    arrgifFrame[i8] = gifFrame.a(n17, n18, n19, n20, false);
                    arrgifFrame[i8].b = n17 + gifFrame.b;
                    arrgifFrame[i8].c = n18 + gifFrame.c;
                    for (int i9 = n2; i9 < n2 + n20; ++i9) {
                        System.arraycopy(arrby, 0, arrgifFrame[i8 - 1].n, i9 * arrgifFrame[i8 - 1].d, n19);
                    }
                    arrgifFrame[i8].o = 1;
                    arrgifFrame[i8].s = this.b;
                }
                arrgifFrame[this.a - 1].o = gifFrame.o;
                arrgifFrame[this.a - 1].s = gifFrame.s;
                return arrgifFrame;
            }
            case 5: {
                int n21;
                n2 = gifFrame.e / (2 * this.a);
                arrgifFrame[0] = gifFrame.f();
                arrgifFrame[0].o = 1;
                arrgifFrame[0].s = this.b;
                for (int i10 = 1; i10 < this.a; ++i10) {
                    n21 = 0;
                    int n22 = i10 * n2;
                    int n23 = gifFrame.d;
                    int n24 = gifFrame.e - 2 * n22;
                    arrgifFrame[i10] = gifFrame.a(n21, n22, n23, n24, false);
                    arrgifFrame[i10].b = n21 + gifFrame.b;
                    arrgifFrame[i10].c = n22 + gifFrame.c;
                    for (int i11 = n2; i11 < n2 + n24; ++i11) {
                        System.arraycopy(arrby, 0, arrgifFrame[i10 - 1].n, i11 * arrgifFrame[i10 - 1].d, n23);
                    }
                    arrgifFrame[i10].o = 1;
                    arrgifFrame[i10].s = this.b;
                }
                GifFrame[] arrgifFrame3 = new GifFrame[this.a];
                for (n21 = 0; n21 < this.a; ++n21) {
                    arrgifFrame3[n21] = arrgifFrame[this.a - n21 - 1];
                }
                arrgifFrame3[this.a - 1].o = gifFrame.o;
                arrgifFrame3[this.a - 1].s = gifFrame.s;
                return arrgifFrame3;
            }
            case 6: {
                n2 = gifFrame.e / this.a;
                int n25 = gifFrame.e % this.a;
                int n26 = 0;
                for (int i12 = 0; i12 < this.a; ++i12) {
                    int n27 = n2 + (n25 > 0 ? 1 : 0);
                    arrgifFrame[i12] = gifFrame.a(0, n26, gifFrame.d, n27, false);
                    arrgifFrame[i12].b = gifFrame.b;
                    arrgifFrame[i12].c = gifFrame.c + n26;
                    n26 += n27;
                    --n25;
                    arrgifFrame[i12].o = 1;
                    arrgifFrame[i12].s = this.b;
                }
                arrgifFrame[this.a - 1].o = gifFrame.o;
                arrgifFrame[this.a - 1].s = gifFrame.s;
                return arrgifFrame;
            }
            case 7: {
                n2 = gifFrame.e / this.a;
                int n28 = gifFrame.e % this.a;
                int n29 = gifFrame.e;
                for (int i13 = 0; i13 < this.a; ++i13) {
                    int n30 = n2 + (n28 > 0 ? 1 : 0);
                    arrgifFrame[i13] = gifFrame.a(0, n29 -= n30, gifFrame.d, n30, false);
                    arrgifFrame[i13].b = gifFrame.b;
                    arrgifFrame[i13].c = gifFrame.c + n29;
                    --n28;
                    arrgifFrame[i13].o = 1;
                    arrgifFrame[i13].s = this.b;
                }
                arrgifFrame[this.a - 1].o = gifFrame.o;
                arrgifFrame[this.a - 1].s = gifFrame.s;
                return arrgifFrame;
            }
        }
        return arrgifFrame;
    }
}

