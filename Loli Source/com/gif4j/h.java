/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j;

import com.gif4j.GifFrame;
import com.gif4j.GifImage;
import com.gif4j.d;
import com.gif4j.e;
import com.gif4j.k;
import com.gif4j.o;
import com.gif4j.s;
import com.gif4j.t;

class h {
    private static final float[] a;
    private static d b;
    private static final int[] c;

    h() {
    }

    static final int a(int n2) {
        int n3 = 0;
        int n4 = n2;
        while ((n2 >>>= 1) > 0) {
            ++n3;
        }
        if (1 << n3 < n4) {
            ++n3;
        }
        return n3;
    }

    public static void a(GifImage gifImage, boolean bl, int n2, boolean bl2) {
        int n3;
        int n4;
        int n5 = n2 - 1;
        int n6 = h.a(n2);
        int n7 = 1 << n6;
        s[] arrs = (s[])b.a();
        if (gifImage.c) {
            for (int i2 = 0; i2 < gifImage.t.size(); ++i2) {
                GifFrame gifFrame = (GifFrame)gifImage.t.get(i2);
                if (gifFrame.k == null) {
                    gifFrame.k = gifImage.f;
                }
                if (gifFrame.l == null) {
                    gifFrame.l = gifImage.g;
                }
                if (gifFrame.m != null) continue;
                gifFrame.m = gifImage.h;
            }
        }
        byte[] arrby = h.a(arrs, gifImage);
        int n8 = 0;
        for (n4 = 0; n4 < 35937 && n8 <= n5; ++n4) {
            if (arrs[n4].d == 0) continue;
            ++n8;
        }
        if (n8 <= n5) {
            n2 = c[n8];
            n5 = n2 - 1;
            gifImage.f = new byte[n2];
            gifImage.g = new byte[n2];
            gifImage.h = new byte[n2];
            gifImage.f[n5] = arrby[0];
            gifImage.g[n5] = arrby[1];
            gifImage.h[n5] = arrby[2];
            gifImage.c = true;
            gifImage.e = n2;
            gifImage.d = GifFrame.a[n2];
            n8 = 0;
            for (n4 = 0; n4 < 35937 && n8 < n5; ++n4) {
                if (arrs[n4].d == 0) continue;
                gifImage.f[n8] = (byte)(arrs[n4].a / arrs[n4].d);
                gifImage.g[n8] = (byte)(arrs[n4].b / arrs[n4].d);
                gifImage.h[n8] = (byte)(arrs[n4].c / arrs[n4].d);
                arrs[n4].f = (byte)n8;
                ++n8;
            }
            for (n4 = 0; n4 < gifImage.t.size(); ++n4) {
                GifFrame gifFrame = (GifFrame)gifImage.t.get(n4);
                byte[] arrby2 = gifFrame.n;
                for (int i3 = 0; i3 < arrby2.length; ++i3) {
                    int n9 = arrby2[i3] & 0xFF;
                    if (n9 != gifFrame.r) {
                        int n10 = gifFrame.n[i3] & 0xFF;
                        int n11 = gifFrame.k[n10] & 0xFF;
                        int n12 = gifFrame.l[n10] & 0xFF;
                        int n13 = gifFrame.m[n10] & 0xFF;
                        int n14 = ((n11 >> 3) + 1) * 1089 + ((n12 >> 3) + 1) * 33 + (n13 >> 3) + 1;
                        gifFrame.n[i3] = arrs[n14].f;
                        continue;
                    }
                    arrby2[i3] = (byte)n5;
                }
                gifFrame.q = true;
                gifFrame.r = n5;
                gifFrame.f = false;
                gifFrame.i = gifImage.d;
                gifFrame.j = gifImage.e;
                if (bl) {
                    gifFrame.m = null;
                    gifFrame.l = null;
                    gifFrame.k = null;
                    continue;
                }
                gifFrame.k = new byte[gifImage.f.length];
                gifFrame.m = new byte[gifImage.h.length];
                gifFrame.l = new byte[gifImage.g.length];
                System.arraycopy(gifImage.f, 0, gifFrame.k, 0, gifImage.f.length);
                System.arraycopy(gifImage.g, 0, gifFrame.l, 0, gifImage.g.length);
                System.arraycopy(gifImage.h, 0, gifFrame.m, 0, gifImage.h.length);
            }
            b.a(arrs);
            return;
        }
        o[] arro = new o[n5];
        int n15 = h.a(arrs, arro, n5);
        gifImage.f = new byte[n2];
        gifImage.g = new byte[n2];
        gifImage.h = new byte[n2];
        gifImage.f[n5] = arrby[0];
        gifImage.g[n5] = arrby[1];
        gifImage.h[n5] = arrby[2];
        byte[] arrby3 = new byte[35938];
        arrby3[35937] = (byte)n5;
        for (n3 = 0; n3 < n15; ++n3) {
            h.a(arro[n3], n3, arrby3);
            long l2 = h.d(arro[n3], arrs);
            if (l2 != 0L) {
                gifImage.f[n3] = (byte)(h.a(arro[n3], arrs) / l2 & 0xFFL);
                gifImage.g[n3] = (byte)(h.b(arro[n3], arrs) / l2 & 0xFFL);
                gifImage.h[n3] = (byte)(h.c(arro[n3], arrs) / l2 & 0xFFL);
                continue;
            }
            gifImage.h[n3] = 0;
            gifImage.g[n3] = 0;
            gifImage.f[n3] = 0;
        }
        b.a(arrs);
        gifImage.c = true;
        gifImage.e = n7;
        gifImage.d = n6;
        for (n3 = 0; n3 < gifImage.t.size(); ++n3) {
            Object object;
            GifFrame gifFrame = (GifFrame)gifImage.t.get(n3);
            if (bl2) {
                object = new k(gifImage.f, gifImage.g, gifImage.h, arrby3, n5, gifFrame);
                ((k)object).b();
            } else {
                object = gifFrame.n;
                e e2 = new e(gifImage.f, gifImage.g, gifImage.h, n5);
                for (int i4 = 0; i4 < ((Object)object).length; ++i4) {
                    int n16 = object[i4] & 0xFF;
                    if (n16 == gifFrame.r) {
                        object[i4] = (byte)n5;
                        continue;
                    }
                    int n17 = gifFrame.k[n16] & 0xFF;
                    int n18 = gifFrame.l[n16] & 0xFF;
                    int n19 = gifFrame.m[n16] & 0xFF;
                    object[i4] = e2.a(n17, n18, n19);
                }
            }
            gifFrame.q = true;
            gifFrame.r = n5;
            gifFrame.f = false;
            gifFrame.i = gifImage.d;
            gifFrame.j = gifImage.e;
            if (bl) {
                gifFrame.m = null;
                gifFrame.l = null;
                gifFrame.k = null;
                continue;
            }
            gifFrame.k = new byte[gifImage.f.length];
            gifFrame.m = new byte[gifImage.h.length];
            gifFrame.l = new byte[gifImage.g.length];
            System.arraycopy(gifImage.f, 0, gifFrame.k, 0, gifImage.f.length);
            System.arraycopy(gifImage.g, 0, gifFrame.l, 0, gifImage.g.length);
            System.arraycopy(gifImage.h, 0, gifFrame.m, 0, gifImage.h.length);
        }
    }

    private static final byte[] a(s[] arrs, GifImage gifImage) {
        byte[] arrby = new byte[3];
        boolean bl = true;
        for (int i2 = 0; i2 < gifImage.t.size(); ++i2) {
            GifFrame gifFrame = (GifFrame)gifImage.t.get(i2);
            for (int i3 = 0; i3 < gifFrame.n.length; ++i3) {
                int n2 = gifFrame.n[i3] & 0xFF;
                int n3 = gifFrame.k[n2] & 0xFF;
                int n4 = gifFrame.l[n2] & 0xFF;
                int n5 = gifFrame.m[n2] & 0xFF;
                if (n2 != gifFrame.r) {
                    int n6 = ((n3 >> 3) + 1) * 1089 + ((n4 >> 3) + 1) * 33 + (n5 >> 3) + 1;
                    s s2 = arrs[n6];
                    s2.a += n3;
                    s2.b += n4;
                    s2.c += n5;
                    ++s2.d;
                    s2.e += a[n3] + a[n4] + a[n5];
                    continue;
                }
                if (bl) {
                    arrby[0] = (byte)n3;
                    arrby[1] = (byte)n4;
                    arrby[2] = (byte)n5;
                    bl = false;
                    continue;
                }
                arrby[0] = (byte)((arrby[0] & 0xFF) + n3 >> 1);
                arrby[1] = (byte)((arrby[1] & 0xFF) + n4 >> 1);
                arrby[2] = (byte)((arrby[2] & 0xFF) + n5 >> 1);
            }
        }
        return arrby;
    }

    private static final int a(s[] arrs, o[] arro, int n2) {
        h.a(arrs);
        for (int i2 = 0; i2 < n2; ++i2) {
            arro[i2] = new o();
        }
        float[] arrf = new float[n2];
        arro[0].e = 0;
        arro[0].c = 0;
        arro[0].a = 0;
        arro[0].f = 32;
        arro[0].d = 32;
        arro[0].b = 32;
        int n3 = n2;
        int n4 = 0;
        for (int i3 = 1; i3 < n3; ++i3) {
            if (h.a(arro[n4], arro[i3], arrs) != 0) {
                arrf[n4] = arro[n4].g > 1 ? h.e(arro[n4], arrs) : 0.0f;
                arrf[i3] = arro[i3].g > 1 ? h.e(arro[i3], arrs) : 0.0f;
            } else {
                arrf[n4] = 0.0f;
                --i3;
            }
            n4 = 0;
            float f2 = arrf[0];
            for (int i4 = 1; i4 <= i3; ++i4) {
                if (!(arrf[i4] > f2)) continue;
                f2 = arrf[i4];
                n4 = i4;
            }
            if (!((double)f2 <= 0.0)) continue;
            n3 = i3 + 1;
            break;
        }
        return n3;
    }

    private static final void a(s[] arrs) {
        int[] arrn = new int[33];
        int[] arrn2 = new int[33];
        int[] arrn3 = new int[33];
        int[] arrn4 = new int[33];
        float[] arrf = new float[33];
        for (int i2 = 1; i2 <= 32; ++i2) {
            int n2;
            for (n2 = 0; n2 <= 32; ++n2) {
                arrn3[n2] = 0;
                arrn2[n2] = 0;
                arrn[n2] = 0;
                arrn4[n2] = 0;
                arrf[n2] = 0;
            }
            for (n2 = 1; n2 <= 32; ++n2) {
                float f2 = 0.0f;
                int n3 = 0;
                int n4 = 0;
                int n5 = 0;
                int n6 = 0;
                for (int i3 = 1; i3 <= 32; ++i3) {
                    int n7 = i2 * 1089 + n2 * 33 + i3;
                    int n8 = (i2 - 1) * 1089 + n2 * 33 + i3;
                    s s2 = arrs[n7];
                    s s3 = arrs[n8];
                    int n9 = i3;
                    arrn4[n9] = arrn4[n9] + (n3 += s2.d);
                    int n10 = i3;
                    arrn[n10] = arrn[n10] + (n4 += s2.a);
                    int n11 = i3;
                    arrn2[n11] = arrn2[n11] + (n5 += s2.b);
                    int n12 = i3;
                    arrn3[n12] = arrn3[n12] + (n6 += s2.c);
                    int n13 = i3;
                    arrf[n13] = arrf[n13] + (f2 += s2.e);
                    s2.d = s3.d + arrn4[i3];
                    s2.a = s3.a + arrn[i3];
                    s2.b = s3.b + arrn2[i3];
                    s2.c = s3.c + arrn3[i3];
                    s2.e = s3.e + arrf[i3];
                }
            }
        }
    }

    private static final long a(o o2, s[] arrs) {
        s s2 = arrs[o2.b * 1089 + o2.d * 33 + o2.f];
        s s3 = arrs[o2.b * 1089 + o2.d * 33 + o2.e];
        s s4 = arrs[o2.b * 1089 + o2.c * 33 + o2.f];
        s s5 = arrs[o2.b * 1089 + o2.c * 33 + o2.e];
        s s6 = arrs[o2.a * 1089 + o2.d * 33 + o2.f];
        s s7 = arrs[o2.a * 1089 + o2.d * 33 + o2.e];
        s s8 = arrs[o2.a * 1089 + o2.c * 33 + o2.f];
        s s9 = arrs[o2.a * 1089 + o2.c * 33 + o2.e];
        return s2.a - s3.a - s4.a + s5.a - s6.a + s7.a + s8.a - s9.a;
    }

    private static final long b(o o2, s[] arrs) {
        s s2 = arrs[o2.b * 1089 + o2.d * 33 + o2.f];
        s s3 = arrs[o2.b * 1089 + o2.d * 33 + o2.e];
        s s4 = arrs[o2.b * 1089 + o2.c * 33 + o2.f];
        s s5 = arrs[o2.b * 1089 + o2.c * 33 + o2.e];
        s s6 = arrs[o2.a * 1089 + o2.d * 33 + o2.f];
        s s7 = arrs[o2.a * 1089 + o2.d * 33 + o2.e];
        s s8 = arrs[o2.a * 1089 + o2.c * 33 + o2.f];
        s s9 = arrs[o2.a * 1089 + o2.c * 33 + o2.e];
        return s2.b - s3.b - s4.b + s5.b - s6.b + s7.b + s8.b - s9.b;
    }

    private static final long c(o o2, s[] arrs) {
        s s2 = arrs[o2.b * 1089 + o2.d * 33 + o2.f];
        s s3 = arrs[o2.b * 1089 + o2.d * 33 + o2.e];
        s s4 = arrs[o2.b * 1089 + o2.c * 33 + o2.f];
        s s5 = arrs[o2.b * 1089 + o2.c * 33 + o2.e];
        s s6 = arrs[o2.a * 1089 + o2.d * 33 + o2.f];
        s s7 = arrs[o2.a * 1089 + o2.d * 33 + o2.e];
        s s8 = arrs[o2.a * 1089 + o2.c * 33 + o2.f];
        s s9 = arrs[o2.a * 1089 + o2.c * 33 + o2.e];
        return s2.c - s3.c - s4.c + s5.c - s6.c + s7.c + s8.c - s9.c;
    }

    private static final long d(o o2, s[] arrs) {
        s s2 = arrs[o2.b * 1089 + o2.d * 33 + o2.f];
        s s3 = arrs[o2.b * 1089 + o2.d * 33 + o2.e];
        s s4 = arrs[o2.b * 1089 + o2.c * 33 + o2.f];
        s s5 = arrs[o2.b * 1089 + o2.c * 33 + o2.e];
        s s6 = arrs[o2.a * 1089 + o2.d * 33 + o2.f];
        s s7 = arrs[o2.a * 1089 + o2.d * 33 + o2.e];
        s s8 = arrs[o2.a * 1089 + o2.c * 33 + o2.f];
        s s9 = arrs[o2.a * 1089 + o2.c * 33 + o2.e];
        return s2.d - s3.d - s4.d + s5.d - s6.d + s7.d + s8.d - s9.d;
    }

    private static final long a(o o2, int n2, s[] arrs) {
        switch (n2) {
            case 2: {
                s s2 = arrs[o2.a * 1089 + o2.d * 33 + o2.f];
                s s3 = arrs[o2.a * 1089 + o2.d * 33 + o2.e];
                s s4 = arrs[o2.a * 1089 + o2.c * 33 + o2.f];
                s s5 = arrs[o2.a * 1089 + o2.c * 33 + o2.e];
                return -s2.a + s3.a + s4.a - s5.a;
            }
            case 1: {
                s s6 = arrs[o2.b * 1089 + o2.c * 33 + o2.f];
                s s7 = arrs[o2.b * 1089 + o2.c * 33 + o2.e];
                s s8 = arrs[o2.a * 1089 + o2.c * 33 + o2.f];
                s s9 = arrs[o2.a * 1089 + o2.c * 33 + o2.e];
                return -s6.a + s7.a + s8.a - s9.a;
            }
            case 0: {
                s s10 = arrs[o2.b * 1089 + o2.d * 33 + o2.e];
                s s11 = arrs[o2.b * 1089 + o2.c * 33 + o2.e];
                s s12 = arrs[o2.a * 1089 + o2.d * 33 + o2.e];
                s s13 = arrs[o2.a * 1089 + o2.c * 33 + o2.e];
                return -s10.a + s11.a + s12.a - s13.a;
            }
        }
        return 1L;
    }

    private static final long b(o o2, int n2, s[] arrs) {
        switch (n2) {
            case 2: {
                s s2 = arrs[o2.a * 1089 + o2.d * 33 + o2.f];
                s s3 = arrs[o2.a * 1089 + o2.d * 33 + o2.e];
                s s4 = arrs[o2.a * 1089 + o2.c * 33 + o2.f];
                s s5 = arrs[o2.a * 1089 + o2.c * 33 + o2.e];
                return -s2.b + s3.b + s4.b - s5.b;
            }
            case 1: {
                s s6 = arrs[o2.b * 1089 + o2.c * 33 + o2.f];
                s s7 = arrs[o2.b * 1089 + o2.c * 33 + o2.e];
                s s8 = arrs[o2.a * 1089 + o2.c * 33 + o2.f];
                s s9 = arrs[o2.a * 1089 + o2.c * 33 + o2.e];
                return -s6.b + s7.b + s8.b - s9.b;
            }
            case 0: {
                s s10 = arrs[o2.b * 1089 + o2.d * 33 + o2.e];
                s s11 = arrs[o2.b * 1089 + o2.c * 33 + o2.e];
                s s12 = arrs[o2.a * 1089 + o2.d * 33 + o2.e];
                s s13 = arrs[o2.a * 1089 + o2.c * 33 + o2.e];
                return -s10.b + s11.b + s12.b - s13.b;
            }
        }
        return 1L;
    }

    private static final long c(o o2, int n2, s[] arrs) {
        switch (n2) {
            case 2: {
                s s2 = arrs[o2.a * 1089 + o2.d * 33 + o2.f];
                s s3 = arrs[o2.a * 1089 + o2.d * 33 + o2.e];
                s s4 = arrs[o2.a * 1089 + o2.c * 33 + o2.f];
                s s5 = arrs[o2.a * 1089 + o2.c * 33 + o2.e];
                return -s2.c + s3.c + s4.c - s5.c;
            }
            case 1: {
                s s6 = arrs[o2.b * 1089 + o2.c * 33 + o2.f];
                s s7 = arrs[o2.b * 1089 + o2.c * 33 + o2.e];
                s s8 = arrs[o2.a * 1089 + o2.c * 33 + o2.f];
                s s9 = arrs[o2.a * 1089 + o2.c * 33 + o2.e];
                return -s6.c + s7.c + s8.c - s9.c;
            }
            case 0: {
                s s10 = arrs[o2.b * 1089 + o2.d * 33 + o2.e];
                s s11 = arrs[o2.b * 1089 + o2.c * 33 + o2.e];
                s s12 = arrs[o2.a * 1089 + o2.d * 33 + o2.e];
                s s13 = arrs[o2.a * 1089 + o2.c * 33 + o2.e];
                return -s10.c + s11.c + s12.c - s13.c;
            }
        }
        return 1L;
    }

    private static final long d(o o2, int n2, s[] arrs) {
        switch (n2) {
            case 2: {
                s s2 = arrs[o2.a * 1089 + o2.d * 33 + o2.f];
                s s3 = arrs[o2.a * 1089 + o2.d * 33 + o2.e];
                s s4 = arrs[o2.a * 1089 + o2.c * 33 + o2.f];
                s s5 = arrs[o2.a * 1089 + o2.c * 33 + o2.e];
                return -s2.d + s3.d + s4.d - s5.d;
            }
            case 1: {
                s s6 = arrs[o2.b * 1089 + o2.c * 33 + o2.f];
                s s7 = arrs[o2.b * 1089 + o2.c * 33 + o2.e];
                s s8 = arrs[o2.a * 1089 + o2.c * 33 + o2.f];
                s s9 = arrs[o2.a * 1089 + o2.c * 33 + o2.e];
                return -s6.d + s7.d + s8.d - s9.d;
            }
            case 0: {
                s s10 = arrs[o2.b * 1089 + o2.d * 33 + o2.e];
                s s11 = arrs[o2.b * 1089 + o2.c * 33 + o2.e];
                s s12 = arrs[o2.a * 1089 + o2.d * 33 + o2.e];
                s s13 = arrs[o2.a * 1089 + o2.c * 33 + o2.e];
                return -s10.d + s11.d + s12.d - s13.d;
            }
        }
        return 1L;
    }

    private static final long a(o o2, int n2, int n3, s[] arrs) {
        switch (n2) {
            case 2: {
                s s2 = arrs[n3 * 1089 + o2.d * 33 + o2.f];
                s s3 = arrs[n3 * 1089 + o2.d * 33 + o2.e];
                s s4 = arrs[n3 * 1089 + o2.c * 33 + o2.f];
                s s5 = arrs[n3 * 1089 + o2.c * 33 + o2.e];
                return s2.a - s3.a - s4.a + s5.a;
            }
            case 1: {
                s s6 = arrs[o2.b * 1089 + n3 * 33 + o2.f];
                s s7 = arrs[o2.b * 1089 + n3 * 33 + o2.e];
                s s8 = arrs[o2.a * 1089 + n3 * 33 + o2.f];
                s s9 = arrs[o2.a * 1089 + n3 * 33 + o2.e];
                return s6.a - s7.a - s8.a + s9.a;
            }
            case 0: {
                s s10 = arrs[o2.b * 1089 + o2.d * 33 + n3];
                s s11 = arrs[o2.b * 1089 + o2.c * 33 + n3];
                s s12 = arrs[o2.a * 1089 + o2.d * 33 + n3];
                s s13 = arrs[o2.a * 1089 + o2.c * 33 + n3];
                return s10.a - s11.a - s12.a + s13.a;
            }
        }
        return 1L;
    }

    private static final long b(o o2, int n2, int n3, s[] arrs) {
        switch (n2) {
            case 2: {
                s s2 = arrs[n3 * 1089 + o2.d * 33 + o2.f];
                s s3 = arrs[n3 * 1089 + o2.d * 33 + o2.e];
                s s4 = arrs[n3 * 1089 + o2.c * 33 + o2.f];
                s s5 = arrs[n3 * 1089 + o2.c * 33 + o2.e];
                return s2.b - s3.b - s4.b + s5.b;
            }
            case 1: {
                s s6 = arrs[o2.b * 1089 + n3 * 33 + o2.f];
                s s7 = arrs[o2.b * 1089 + n3 * 33 + o2.e];
                s s8 = arrs[o2.a * 1089 + n3 * 33 + o2.f];
                s s9 = arrs[o2.a * 1089 + n3 * 33 + o2.e];
                return s6.b - s7.b - s8.b + s9.b;
            }
            case 0: {
                s s10 = arrs[o2.b * 1089 + o2.d * 33 + n3];
                s s11 = arrs[o2.b * 1089 + o2.c * 33 + n3];
                s s12 = arrs[o2.a * 1089 + o2.d * 33 + n3];
                s s13 = arrs[o2.a * 1089 + o2.c * 33 + n3];
                return s10.b - s11.b - s12.b + s13.b;
            }
        }
        return 1L;
    }

    private static final long c(o o2, int n2, int n3, s[] arrs) {
        switch (n2) {
            case 2: {
                s s2 = arrs[n3 * 1089 + o2.d * 33 + o2.f];
                s s3 = arrs[n3 * 1089 + o2.d * 33 + o2.e];
                s s4 = arrs[n3 * 1089 + o2.c * 33 + o2.f];
                s s5 = arrs[n3 * 1089 + o2.c * 33 + o2.e];
                return s2.c - s3.c - s4.c + s5.c;
            }
            case 1: {
                s s6 = arrs[o2.b * 1089 + n3 * 33 + o2.f];
                s s7 = arrs[o2.b * 1089 + n3 * 33 + o2.e];
                s s8 = arrs[o2.a * 1089 + n3 * 33 + o2.f];
                s s9 = arrs[o2.a * 1089 + n3 * 33 + o2.e];
                return s6.c - s7.c - s8.c + s9.c;
            }
            case 0: {
                s s10 = arrs[o2.b * 1089 + o2.d * 33 + n3];
                s s11 = arrs[o2.b * 1089 + o2.c * 33 + n3];
                s s12 = arrs[o2.a * 1089 + o2.d * 33 + n3];
                s s13 = arrs[o2.a * 1089 + o2.c * 33 + n3];
                return s10.c - s11.c - s12.c + s13.c;
            }
        }
        return 1L;
    }

    private static final long d(o o2, int n2, int n3, s[] arrs) {
        switch (n2) {
            case 2: {
                s s2 = arrs[n3 * 1089 + o2.d * 33 + o2.f];
                s s3 = arrs[n3 * 1089 + o2.d * 33 + o2.e];
                s s4 = arrs[n3 * 1089 + o2.c * 33 + o2.f];
                s s5 = arrs[n3 * 1089 + o2.c * 33 + o2.e];
                return s2.d - s3.d - s4.d + s5.d;
            }
            case 1: {
                s s6 = arrs[o2.b * 1089 + n3 * 33 + o2.f];
                s s7 = arrs[o2.b * 1089 + n3 * 33 + o2.e];
                s s8 = arrs[o2.a * 1089 + n3 * 33 + o2.f];
                s s9 = arrs[o2.a * 1089 + n3 * 33 + o2.e];
                return s6.d - s7.d - s8.d + s9.d;
            }
            case 0: {
                s s10 = arrs[o2.b * 1089 + o2.d * 33 + n3];
                s s11 = arrs[o2.b * 1089 + o2.c * 33 + n3];
                s s12 = arrs[o2.a * 1089 + o2.d * 33 + n3];
                s s13 = arrs[o2.a * 1089 + o2.c * 33 + n3];
                return s10.d - s11.d - s12.d + s13.d;
            }
        }
        return 1L;
    }

    private static final float e(o o2, s[] arrs) {
        long l2 = h.a(o2, arrs);
        long l3 = h.b(o2, arrs);
        long l4 = h.c(o2, arrs);
        long l5 = h.d(o2, arrs);
        float f2 = arrs[o2.b * 1089 + o2.d * 33 + o2.f].e - arrs[o2.b * 1089 + o2.d * 33 + o2.e].e - arrs[o2.b * 1089 + o2.c * 33 + o2.f].e + arrs[o2.b * 1089 + o2.c * 33 + o2.e].e - arrs[o2.a * 1089 + o2.d * 33 + o2.f].e + arrs[o2.a * 1089 + o2.d * 33 + o2.e].e + arrs[o2.a * 1089 + o2.c * 33 + o2.f].e - arrs[o2.a * 1089 + o2.c * 33 + o2.e].e;
        return f2 - (float)(l2 * l2 + l4 * l4 + l3 * l3) / (float)l5;
    }

    private static final float a(o o2, int n2, int n3, int n4, int[] arrn, long l2, long l3, long l4, long l5, s[] arrs) {
        long l6 = h.a(o2, n2, arrs);
        long l7 = h.b(o2, n2, arrs);
        long l8 = h.c(o2, n2, arrs);
        long l9 = h.d(o2, n2, arrs);
        float f2 = 0.0f;
        float f3 = 0.0f;
        arrn[0] = -1;
        for (int i2 = n3; i2 < n4; ++i2) {
            long l10 = l6 + h.a(o2, n2, i2, arrs);
            long l11 = l7 + h.b(o2, n2, i2, arrs);
            long l12 = l8 + h.c(o2, n2, i2, arrs);
            long l13 = l9 + h.d(o2, n2, i2, arrs);
            if (l13 == 0L) continue;
            f3 = ((float)l10 * (float)l10 + (float)l11 * (float)l11 + (float)l12 * (float)l12) / (float)l13;
            l10 = l2 - l10;
            l11 = l3 - l11;
            l12 = l4 - l12;
            if ((l13 = l5 - l13) == 0L || !((f3 += ((float)l10 * (float)l10 + (float)l11 * (float)l11 + (float)l12 * (float)l12) / (float)l13) > f2)) continue;
            f2 = f3;
            arrn[0] = i2;
        }
        return f2;
    }

    private static final int a(o o2, o o3, s[] arrs) {
        int n2;
        int[] arrn = new int[1];
        int[] arrn2 = new int[1];
        int[] arrn3 = new int[1];
        long l2 = h.a(o2, arrs);
        long l3 = h.b(o2, arrs);
        long l4 = h.c(o2, arrs);
        long l5 = h.d(o2, arrs);
        float f2 = h.a(o2, 2, o2.a + 1, o2.b, arrn, l2, l3, l4, l5, arrs);
        float f3 = h.a(o2, 1, o2.c + 1, o2.d, arrn2, l2, l3, l4, l5, arrs);
        float f4 = h.a(o2, 0, o2.e + 1, o2.f, arrn3, l2, l3, l4, l5, arrs);
        if (f2 >= f3 && f2 >= f3) {
            n2 = 2;
            if (arrn[0] < 0) {
                return 0;
            }
        } else {
            n2 = f3 >= f2 && f3 >= f4 ? 1 : 0;
        }
        o3.b = o2.b;
        o3.d = o2.d;
        o3.f = o2.f;
        switch (n2) {
            case 2: {
                o3.a = o2.b = arrn[0];
                o3.c = o2.c;
                o3.e = o2.e;
                break;
            }
            case 1: {
                o3.a = o2.a;
                o3.c = o2.d = arrn2[0];
                o3.e = o2.e;
                break;
            }
            case 0: {
                o3.a = o2.a;
                o3.c = o2.c;
                o3.e = o2.f = arrn3[0];
            }
        }
        o2.g = (o2.b - o2.a) * (o2.d - o2.c) * (o2.f - o2.e);
        o3.g = (o3.b - o3.a) * (o3.d - o3.c) * (o3.f - o3.e);
        return 1;
    }

    private static final void a(o o2, int n2, byte[] arrby) {
        for (int i2 = o2.a + 1; i2 <= o2.b; ++i2) {
            for (int i3 = o2.c + 1; i3 <= o2.d; ++i3) {
                for (int i4 = o2.e + 1; i4 <= o2.f; ++i4) {
                    arrby[i2 * 1089 + i3 * 33 + i4] = (byte)n2;
                }
            }
        }
    }

    static {
        int n2;
        a = new float[256];
        b = null;
        c = new int[257];
        h.c[0] = 2;
        h.c[3] = 4;
        h.c[2] = 4;
        h.c[1] = 4;
        h.c[7] = 8;
        h.c[6] = 8;
        h.c[5] = 8;
        h.c[4] = 8;
        for (n2 = 8; n2 < 16; ++n2) {
            h.c[n2] = 16;
        }
        for (n2 = 16; n2 < 32; ++n2) {
            h.c[n2] = 32;
        }
        for (n2 = 32; n2 < 64; ++n2) {
            h.c[n2] = 64;
        }
        for (n2 = 64; n2 < 128; ++n2) {
            h.c[n2] = 128;
        }
        for (n2 = 128; n2 <= 256; ++n2) {
            h.c[n2] = 256;
        }
        for (n2 = 0; n2 < 256; ++n2) {
            h.a[n2] = n2 * n2;
        }
        for (n2 = 0; n2 < 256; ++n2) {
            h.a[n2] = n2 * n2;
        }
        b = new d(new t());
    }
}

