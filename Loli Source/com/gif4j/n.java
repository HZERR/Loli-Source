/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j;

import com.gif4j.GifFrame;
import com.gif4j.GifImage;
import com.gif4j.g;
import com.gif4j.k;
import com.gif4j.p;

class n {
    private static final float[] a;
    private static final int[] b;

    n() {
    }

    public static void a(GifImage gifImage, boolean bl) {
        int n2;
        int n3;
        int n4 = 8;
        int n5 = 256;
        int n6 = n5 - 1;
        p[] arrp = new p[35937];
        for (int i2 = 0; i2 < 35937; ++i2) {
            arrp[i2] = new p();
        }
        byte[] arrby = n.a(arrp, gifImage);
        int n7 = 0;
        for (n3 = 0; n3 < 35937 && n7 <= n6; ++n3) {
            if (arrp[n3].d == 0) continue;
            ++n7;
        }
        if (n7 <= n6) {
            n5 = b[n7];
            n6 = n5 - 1;
            gifImage.f = new byte[n5];
            gifImage.g = new byte[n5];
            gifImage.h = new byte[n5];
            gifImage.f[n6] = arrby[0];
            gifImage.g[n6] = arrby[1];
            gifImage.h[n6] = arrby[2];
            gifImage.c = true;
            gifImage.e = n5;
            gifImage.d = GifFrame.a[n5];
            n7 = 0;
            for (n3 = 0; n3 < 35937 && n7 < n6; ++n3) {
                if (arrp[n3].d == 0) continue;
                gifImage.f[n7] = (byte)(arrp[n3].a / arrp[n3].d);
                gifImage.g[n7] = (byte)(arrp[n3].b / arrp[n3].d);
                gifImage.h[n7] = (byte)(arrp[n3].c / arrp[n3].d);
                arrp[n3].f = (byte)n7;
                ++n7;
            }
            for (n3 = 0; n3 < gifImage.t.size(); ++n3) {
                GifFrame gifFrame = (GifFrame)gifImage.t.get(n3);
                byte[] arrby2 = gifFrame.n;
                for (int i3 = 0; i3 < arrby2.length; ++i3) {
                    int n8 = arrby2[i3] & 0xFF;
                    if (n8 != gifFrame.r) {
                        int n9 = gifFrame.n[i3] & 0xFF;
                        int n10 = gifFrame.k[n9] & 0xFF;
                        int n11 = gifFrame.l[n9] & 0xFF;
                        int n12 = gifFrame.m[n9] & 0xFF;
                        int n13 = ((n10 >> 3) + 1) * 1089 + ((n11 >> 3) + 1) * 33 + (n12 >> 3) + 1;
                        gifFrame.n[i3] = arrp[n13].f;
                        continue;
                    }
                    arrby2[i3] = (byte)n6;
                }
                gifFrame.q = true;
                gifFrame.r = n6;
                gifFrame.f = false;
                gifFrame.i = gifImage.d;
                gifFrame.j = gifImage.e;
                if (bl) {
                    gifFrame.m = null;
                    gifFrame.l = null;
                    gifFrame.k = null;
                    continue;
                }
                gifFrame.k = new byte[gifFrame.j];
                gifFrame.m = new byte[gifFrame.j];
                gifFrame.l = new byte[gifFrame.j];
                System.arraycopy(gifImage.f, 0, gifFrame.k, 0, gifFrame.j);
                System.arraycopy(gifImage.g, 0, gifFrame.l, 0, gifFrame.j);
                System.arraycopy(gifImage.h, 0, gifFrame.m, 0, gifFrame.j);
            }
            return;
        }
        g[] arrg = new g[n6];
        int n14 = n.a(arrp, arrg, n6);
        gifImage.f = new byte[n5];
        gifImage.g = new byte[n5];
        gifImage.h = new byte[n5];
        gifImage.f[n6] = arrby[0];
        gifImage.g[n6] = arrby[1];
        gifImage.h[n6] = arrby[2];
        byte[] arrby3 = new byte[35938];
        arrby3[35937] = (byte)n6;
        for (n2 = 0; n2 < n14; ++n2) {
            n.a(arrg[n2], n2, arrby3);
            long l2 = n.d(arrg[n2], arrp);
            if (l2 != 0L) {
                gifImage.f[n2] = (byte)(n.a(arrg[n2], arrp) / l2 & 0xFFL);
                gifImage.g[n2] = (byte)(n.b(arrg[n2], arrp) / l2 & 0xFFL);
                gifImage.h[n2] = (byte)(n.c(arrg[n2], arrp) / l2 & 0xFFL);
                continue;
            }
            gifImage.h[n2] = 0;
            gifImage.g[n2] = 0;
            gifImage.f[n2] = 0;
        }
        gifImage.c = true;
        gifImage.e = n5;
        gifImage.d = GifFrame.a[n5];
        for (n2 = 0; n2 < gifImage.t.size(); ++n2) {
            GifFrame gifFrame = (GifFrame)gifImage.t.get(n2);
            k k2 = new k(gifImage.f, gifImage.g, gifImage.h, arrby3, n6, gifFrame);
            k2.b();
            gifFrame.q = true;
            gifFrame.r = n6;
            gifFrame.f = false;
            gifFrame.i = gifImage.d;
            gifFrame.j = gifImage.e;
            if (bl) {
                gifFrame.m = null;
                gifFrame.l = null;
                gifFrame.k = null;
                continue;
            }
            gifFrame.k = new byte[gifFrame.j];
            gifFrame.m = new byte[gifFrame.j];
            gifFrame.l = new byte[gifFrame.j];
            System.arraycopy(gifImage.f, 0, gifFrame.k, 0, gifFrame.j);
            System.arraycopy(gifImage.g, 0, gifFrame.l, 0, gifFrame.j);
            System.arraycopy(gifImage.h, 0, gifFrame.m, 0, gifFrame.j);
        }
    }

    private static final byte[] a(p[] arrp, GifImage gifImage) {
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
                    p p2 = arrp[n6];
                    p2.a += n3;
                    p2.b += n4;
                    p2.c += n5;
                    ++p2.d;
                    p2.e += a[n3] + a[n4] + a[n5];
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

    private static final int a(p[] arrp, g[] arrg, int n2) {
        n.a(arrp);
        for (int i2 = 0; i2 < n2; ++i2) {
            arrg[i2] = new g();
        }
        float[] arrf = new float[n2];
        arrg[0].e = 0;
        arrg[0].c = 0;
        arrg[0].a = 0;
        arrg[0].f = 32;
        arrg[0].d = 32;
        arrg[0].b = 32;
        int n3 = n2;
        int n4 = 0;
        for (int i3 = 1; i3 < n3; ++i3) {
            if (n.a(arrg[n4], arrg[i3], arrp) != 0) {
                arrf[n4] = arrg[n4].g > 1 ? n.e(arrg[n4], arrp) : 0.0f;
                arrf[i3] = arrg[i3].g > 1 ? n.e(arrg[i3], arrp) : 0.0f;
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

    private static final void a(p[] arrp) {
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
                    p p2 = arrp[n7];
                    p p3 = arrp[n8];
                    int n9 = i3;
                    arrn4[n9] = arrn4[n9] + (n3 += p2.d);
                    int n10 = i3;
                    arrn[n10] = arrn[n10] + (n4 += p2.a);
                    int n11 = i3;
                    arrn2[n11] = arrn2[n11] + (n5 += p2.b);
                    int n12 = i3;
                    arrn3[n12] = arrn3[n12] + (n6 += p2.c);
                    int n13 = i3;
                    arrf[n13] = arrf[n13] + (f2 += p2.e);
                    p2.d = p3.d + arrn4[i3];
                    p2.a = p3.a + arrn[i3];
                    p2.b = p3.b + arrn2[i3];
                    p2.c = p3.c + arrn3[i3];
                    p2.e = p3.e + arrf[i3];
                }
            }
        }
    }

    private static final long a(g g2, p[] arrp) {
        p p2 = arrp[g2.b * 1089 + g2.d * 33 + g2.f];
        p p3 = arrp[g2.b * 1089 + g2.d * 33 + g2.e];
        p p4 = arrp[g2.b * 1089 + g2.c * 33 + g2.f];
        p p5 = arrp[g2.b * 1089 + g2.c * 33 + g2.e];
        p p6 = arrp[g2.a * 1089 + g2.d * 33 + g2.f];
        p p7 = arrp[g2.a * 1089 + g2.d * 33 + g2.e];
        p p8 = arrp[g2.a * 1089 + g2.c * 33 + g2.f];
        p p9 = arrp[g2.a * 1089 + g2.c * 33 + g2.e];
        return p2.a - p3.a - p4.a + p5.a - p6.a + p7.a + p8.a - p9.a;
    }

    private static final long b(g g2, p[] arrp) {
        p p2 = arrp[g2.b * 1089 + g2.d * 33 + g2.f];
        p p3 = arrp[g2.b * 1089 + g2.d * 33 + g2.e];
        p p4 = arrp[g2.b * 1089 + g2.c * 33 + g2.f];
        p p5 = arrp[g2.b * 1089 + g2.c * 33 + g2.e];
        p p6 = arrp[g2.a * 1089 + g2.d * 33 + g2.f];
        p p7 = arrp[g2.a * 1089 + g2.d * 33 + g2.e];
        p p8 = arrp[g2.a * 1089 + g2.c * 33 + g2.f];
        p p9 = arrp[g2.a * 1089 + g2.c * 33 + g2.e];
        return p2.b - p3.b - p4.b + p5.b - p6.b + p7.b + p8.b - p9.b;
    }

    private static final long c(g g2, p[] arrp) {
        p p2 = arrp[g2.b * 1089 + g2.d * 33 + g2.f];
        p p3 = arrp[g2.b * 1089 + g2.d * 33 + g2.e];
        p p4 = arrp[g2.b * 1089 + g2.c * 33 + g2.f];
        p p5 = arrp[g2.b * 1089 + g2.c * 33 + g2.e];
        p p6 = arrp[g2.a * 1089 + g2.d * 33 + g2.f];
        p p7 = arrp[g2.a * 1089 + g2.d * 33 + g2.e];
        p p8 = arrp[g2.a * 1089 + g2.c * 33 + g2.f];
        p p9 = arrp[g2.a * 1089 + g2.c * 33 + g2.e];
        return p2.c - p3.c - p4.c + p5.c - p6.c + p7.c + p8.c - p9.c;
    }

    private static final long d(g g2, p[] arrp) {
        p p2 = arrp[g2.b * 1089 + g2.d * 33 + g2.f];
        p p3 = arrp[g2.b * 1089 + g2.d * 33 + g2.e];
        p p4 = arrp[g2.b * 1089 + g2.c * 33 + g2.f];
        p p5 = arrp[g2.b * 1089 + g2.c * 33 + g2.e];
        p p6 = arrp[g2.a * 1089 + g2.d * 33 + g2.f];
        p p7 = arrp[g2.a * 1089 + g2.d * 33 + g2.e];
        p p8 = arrp[g2.a * 1089 + g2.c * 33 + g2.f];
        p p9 = arrp[g2.a * 1089 + g2.c * 33 + g2.e];
        return p2.d - p3.d - p4.d + p5.d - p6.d + p7.d + p8.d - p9.d;
    }

    private static final long a(g g2, int n2, p[] arrp) {
        switch (n2) {
            case 2: {
                p p2 = arrp[g2.a * 1089 + g2.d * 33 + g2.f];
                p p3 = arrp[g2.a * 1089 + g2.d * 33 + g2.e];
                p p4 = arrp[g2.a * 1089 + g2.c * 33 + g2.f];
                p p5 = arrp[g2.a * 1089 + g2.c * 33 + g2.e];
                return -p2.a + p3.a + p4.a - p5.a;
            }
            case 1: {
                p p6 = arrp[g2.b * 1089 + g2.c * 33 + g2.f];
                p p7 = arrp[g2.b * 1089 + g2.c * 33 + g2.e];
                p p8 = arrp[g2.a * 1089 + g2.c * 33 + g2.f];
                p p9 = arrp[g2.a * 1089 + g2.c * 33 + g2.e];
                return -p6.a + p7.a + p8.a - p9.a;
            }
            case 0: {
                p p10 = arrp[g2.b * 1089 + g2.d * 33 + g2.e];
                p p11 = arrp[g2.b * 1089 + g2.c * 33 + g2.e];
                p p12 = arrp[g2.a * 1089 + g2.d * 33 + g2.e];
                p p13 = arrp[g2.a * 1089 + g2.c * 33 + g2.e];
                return -p10.a + p11.a + p12.a - p13.a;
            }
        }
        return 1L;
    }

    private static final long b(g g2, int n2, p[] arrp) {
        switch (n2) {
            case 2: {
                p p2 = arrp[g2.a * 1089 + g2.d * 33 + g2.f];
                p p3 = arrp[g2.a * 1089 + g2.d * 33 + g2.e];
                p p4 = arrp[g2.a * 1089 + g2.c * 33 + g2.f];
                p p5 = arrp[g2.a * 1089 + g2.c * 33 + g2.e];
                return -p2.b + p3.b + p4.b - p5.b;
            }
            case 1: {
                p p6 = arrp[g2.b * 1089 + g2.c * 33 + g2.f];
                p p7 = arrp[g2.b * 1089 + g2.c * 33 + g2.e];
                p p8 = arrp[g2.a * 1089 + g2.c * 33 + g2.f];
                p p9 = arrp[g2.a * 1089 + g2.c * 33 + g2.e];
                return -p6.b + p7.b + p8.b - p9.b;
            }
            case 0: {
                p p10 = arrp[g2.b * 1089 + g2.d * 33 + g2.e];
                p p11 = arrp[g2.b * 1089 + g2.c * 33 + g2.e];
                p p12 = arrp[g2.a * 1089 + g2.d * 33 + g2.e];
                p p13 = arrp[g2.a * 1089 + g2.c * 33 + g2.e];
                return -p10.b + p11.b + p12.b - p13.b;
            }
        }
        return 1L;
    }

    private static final long c(g g2, int n2, p[] arrp) {
        switch (n2) {
            case 2: {
                p p2 = arrp[g2.a * 1089 + g2.d * 33 + g2.f];
                p p3 = arrp[g2.a * 1089 + g2.d * 33 + g2.e];
                p p4 = arrp[g2.a * 1089 + g2.c * 33 + g2.f];
                p p5 = arrp[g2.a * 1089 + g2.c * 33 + g2.e];
                return -p2.c + p3.c + p4.c - p5.c;
            }
            case 1: {
                p p6 = arrp[g2.b * 1089 + g2.c * 33 + g2.f];
                p p7 = arrp[g2.b * 1089 + g2.c * 33 + g2.e];
                p p8 = arrp[g2.a * 1089 + g2.c * 33 + g2.f];
                p p9 = arrp[g2.a * 1089 + g2.c * 33 + g2.e];
                return -p6.c + p7.c + p8.c - p9.c;
            }
            case 0: {
                p p10 = arrp[g2.b * 1089 + g2.d * 33 + g2.e];
                p p11 = arrp[g2.b * 1089 + g2.c * 33 + g2.e];
                p p12 = arrp[g2.a * 1089 + g2.d * 33 + g2.e];
                p p13 = arrp[g2.a * 1089 + g2.c * 33 + g2.e];
                return -p10.c + p11.c + p12.c - p13.c;
            }
        }
        return 1L;
    }

    private static final long d(g g2, int n2, p[] arrp) {
        switch (n2) {
            case 2: {
                p p2 = arrp[g2.a * 1089 + g2.d * 33 + g2.f];
                p p3 = arrp[g2.a * 1089 + g2.d * 33 + g2.e];
                p p4 = arrp[g2.a * 1089 + g2.c * 33 + g2.f];
                p p5 = arrp[g2.a * 1089 + g2.c * 33 + g2.e];
                return -p2.d + p3.d + p4.d - p5.d;
            }
            case 1: {
                p p6 = arrp[g2.b * 1089 + g2.c * 33 + g2.f];
                p p7 = arrp[g2.b * 1089 + g2.c * 33 + g2.e];
                p p8 = arrp[g2.a * 1089 + g2.c * 33 + g2.f];
                p p9 = arrp[g2.a * 1089 + g2.c * 33 + g2.e];
                return -p6.d + p7.d + p8.d - p9.d;
            }
            case 0: {
                p p10 = arrp[g2.b * 1089 + g2.d * 33 + g2.e];
                p p11 = arrp[g2.b * 1089 + g2.c * 33 + g2.e];
                p p12 = arrp[g2.a * 1089 + g2.d * 33 + g2.e];
                p p13 = arrp[g2.a * 1089 + g2.c * 33 + g2.e];
                return -p10.d + p11.d + p12.d - p13.d;
            }
        }
        return 1L;
    }

    private static final long a(g g2, int n2, int n3, p[] arrp) {
        switch (n2) {
            case 2: {
                p p2 = arrp[n3 * 1089 + g2.d * 33 + g2.f];
                p p3 = arrp[n3 * 1089 + g2.d * 33 + g2.e];
                p p4 = arrp[n3 * 1089 + g2.c * 33 + g2.f];
                p p5 = arrp[n3 * 1089 + g2.c * 33 + g2.e];
                return p2.a - p3.a - p4.a + p5.a;
            }
            case 1: {
                p p6 = arrp[g2.b * 1089 + n3 * 33 + g2.f];
                p p7 = arrp[g2.b * 1089 + n3 * 33 + g2.e];
                p p8 = arrp[g2.a * 1089 + n3 * 33 + g2.f];
                p p9 = arrp[g2.a * 1089 + n3 * 33 + g2.e];
                return p6.a - p7.a - p8.a + p9.a;
            }
            case 0: {
                p p10 = arrp[g2.b * 1089 + g2.d * 33 + n3];
                p p11 = arrp[g2.b * 1089 + g2.c * 33 + n3];
                p p12 = arrp[g2.a * 1089 + g2.d * 33 + n3];
                p p13 = arrp[g2.a * 1089 + g2.c * 33 + n3];
                return p10.a - p11.a - p12.a + p13.a;
            }
        }
        return 1L;
    }

    private static final long b(g g2, int n2, int n3, p[] arrp) {
        switch (n2) {
            case 2: {
                p p2 = arrp[n3 * 1089 + g2.d * 33 + g2.f];
                p p3 = arrp[n3 * 1089 + g2.d * 33 + g2.e];
                p p4 = arrp[n3 * 1089 + g2.c * 33 + g2.f];
                p p5 = arrp[n3 * 1089 + g2.c * 33 + g2.e];
                return p2.b - p3.b - p4.b + p5.b;
            }
            case 1: {
                p p6 = arrp[g2.b * 1089 + n3 * 33 + g2.f];
                p p7 = arrp[g2.b * 1089 + n3 * 33 + g2.e];
                p p8 = arrp[g2.a * 1089 + n3 * 33 + g2.f];
                p p9 = arrp[g2.a * 1089 + n3 * 33 + g2.e];
                return p6.b - p7.b - p8.b + p9.b;
            }
            case 0: {
                p p10 = arrp[g2.b * 1089 + g2.d * 33 + n3];
                p p11 = arrp[g2.b * 1089 + g2.c * 33 + n3];
                p p12 = arrp[g2.a * 1089 + g2.d * 33 + n3];
                p p13 = arrp[g2.a * 1089 + g2.c * 33 + n3];
                return p10.b - p11.b - p12.b + p13.b;
            }
        }
        return 1L;
    }

    private static final long c(g g2, int n2, int n3, p[] arrp) {
        switch (n2) {
            case 2: {
                p p2 = arrp[n3 * 1089 + g2.d * 33 + g2.f];
                p p3 = arrp[n3 * 1089 + g2.d * 33 + g2.e];
                p p4 = arrp[n3 * 1089 + g2.c * 33 + g2.f];
                p p5 = arrp[n3 * 1089 + g2.c * 33 + g2.e];
                return p2.c - p3.c - p4.c + p5.c;
            }
            case 1: {
                p p6 = arrp[g2.b * 1089 + n3 * 33 + g2.f];
                p p7 = arrp[g2.b * 1089 + n3 * 33 + g2.e];
                p p8 = arrp[g2.a * 1089 + n3 * 33 + g2.f];
                p p9 = arrp[g2.a * 1089 + n3 * 33 + g2.e];
                return p6.c - p7.c - p8.c + p9.c;
            }
            case 0: {
                p p10 = arrp[g2.b * 1089 + g2.d * 33 + n3];
                p p11 = arrp[g2.b * 1089 + g2.c * 33 + n3];
                p p12 = arrp[g2.a * 1089 + g2.d * 33 + n3];
                p p13 = arrp[g2.a * 1089 + g2.c * 33 + n3];
                return p10.c - p11.c - p12.c + p13.c;
            }
        }
        return 1L;
    }

    private static final long d(g g2, int n2, int n3, p[] arrp) {
        switch (n2) {
            case 2: {
                p p2 = arrp[n3 * 1089 + g2.d * 33 + g2.f];
                p p3 = arrp[n3 * 1089 + g2.d * 33 + g2.e];
                p p4 = arrp[n3 * 1089 + g2.c * 33 + g2.f];
                p p5 = arrp[n3 * 1089 + g2.c * 33 + g2.e];
                return p2.d - p3.d - p4.d + p5.d;
            }
            case 1: {
                p p6 = arrp[g2.b * 1089 + n3 * 33 + g2.f];
                p p7 = arrp[g2.b * 1089 + n3 * 33 + g2.e];
                p p8 = arrp[g2.a * 1089 + n3 * 33 + g2.f];
                p p9 = arrp[g2.a * 1089 + n3 * 33 + g2.e];
                return p6.d - p7.d - p8.d + p9.d;
            }
            case 0: {
                p p10 = arrp[g2.b * 1089 + g2.d * 33 + n3];
                p p11 = arrp[g2.b * 1089 + g2.c * 33 + n3];
                p p12 = arrp[g2.a * 1089 + g2.d * 33 + n3];
                p p13 = arrp[g2.a * 1089 + g2.c * 33 + n3];
                return p10.d - p11.d - p12.d + p13.d;
            }
        }
        return 1L;
    }

    private static final float e(g g2, p[] arrp) {
        long l2 = n.a(g2, arrp);
        long l3 = n.b(g2, arrp);
        long l4 = n.c(g2, arrp);
        long l5 = n.d(g2, arrp);
        float f2 = arrp[g2.b * 1089 + g2.d * 33 + g2.f].e - arrp[g2.b * 1089 + g2.d * 33 + g2.e].e - arrp[g2.b * 1089 + g2.c * 33 + g2.f].e + arrp[g2.b * 1089 + g2.c * 33 + g2.e].e - arrp[g2.a * 1089 + g2.d * 33 + g2.f].e + arrp[g2.a * 1089 + g2.d * 33 + g2.e].e + arrp[g2.a * 1089 + g2.c * 33 + g2.f].e - arrp[g2.a * 1089 + g2.c * 33 + g2.e].e;
        return f2 - (float)(l2 * l2 + l4 * l4 + l3 * l3) / (float)l5;
    }

    private static final float a(g g2, int n2, int n3, int n4, int[] arrn, long l2, long l3, long l4, long l5, p[] arrp) {
        long l6 = n.a(g2, n2, arrp);
        long l7 = n.b(g2, n2, arrp);
        long l8 = n.c(g2, n2, arrp);
        long l9 = n.d(g2, n2, arrp);
        float f2 = 0.0f;
        float f3 = 0.0f;
        arrn[0] = -1;
        for (int i2 = n3; i2 < n4; ++i2) {
            long l10 = l6 + n.a(g2, n2, i2, arrp);
            long l11 = l7 + n.b(g2, n2, i2, arrp);
            long l12 = l8 + n.c(g2, n2, i2, arrp);
            long l13 = l9 + n.d(g2, n2, i2, arrp);
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

    private static final int a(g g2, g g3, p[] arrp) {
        int n2;
        int[] arrn = new int[1];
        int[] arrn2 = new int[1];
        int[] arrn3 = new int[1];
        long l2 = n.a(g2, arrp);
        long l3 = n.b(g2, arrp);
        long l4 = n.c(g2, arrp);
        long l5 = n.d(g2, arrp);
        float f2 = n.a(g2, 2, g2.a + 1, g2.b, arrn, l2, l3, l4, l5, arrp);
        float f3 = n.a(g2, 1, g2.c + 1, g2.d, arrn2, l2, l3, l4, l5, arrp);
        float f4 = n.a(g2, 0, g2.e + 1, g2.f, arrn3, l2, l3, l4, l5, arrp);
        if (f2 >= f3 && f2 >= f3) {
            n2 = 2;
            if (arrn[0] < 0) {
                return 0;
            }
        } else {
            n2 = f3 >= f2 && f3 >= f4 ? 1 : 0;
        }
        g3.b = g2.b;
        g3.d = g2.d;
        g3.f = g2.f;
        switch (n2) {
            case 2: {
                g3.a = g2.b = arrn[0];
                g3.c = g2.c;
                g3.e = g2.e;
                break;
            }
            case 1: {
                g3.a = g2.a;
                g3.c = g2.d = arrn2[0];
                g3.e = g2.e;
                break;
            }
            case 0: {
                g3.a = g2.a;
                g3.c = g2.c;
                g3.e = g2.f = arrn3[0];
            }
        }
        g2.g = (g2.b - g2.a) * (g2.d - g2.c) * (g2.f - g2.e);
        g3.g = (g3.b - g3.a) * (g3.d - g3.c) * (g3.f - g3.e);
        return 1;
    }

    private static final void a(g g2, int n2, byte[] arrby) {
        for (int i2 = g2.a + 1; i2 <= g2.b; ++i2) {
            for (int i3 = g2.c + 1; i3 <= g2.d; ++i3) {
                for (int i4 = g2.e + 1; i4 <= g2.f; ++i4) {
                    arrby[i2 * 1089 + i3 * 33 + i4] = (byte)n2;
                }
            }
        }
    }

    static {
        int n2;
        a = new float[256];
        b = new int[257];
        n.b[0] = 2;
        n.b[3] = 4;
        n.b[2] = 4;
        n.b[1] = 4;
        n.b[7] = 8;
        n.b[6] = 8;
        n.b[5] = 8;
        n.b[4] = 8;
        for (n2 = 8; n2 < 16; ++n2) {
            n.b[n2] = 16;
        }
        for (n2 = 16; n2 < 32; ++n2) {
            n.b[n2] = 32;
        }
        for (n2 = 32; n2 < 64; ++n2) {
            n.b[n2] = 64;
        }
        for (n2 = 64; n2 < 128; ++n2) {
            n.b[n2] = 128;
        }
        for (n2 = 128; n2 <= 256; ++n2) {
            n.b[n2] = 256;
        }
        for (n2 = 0; n2 < 256; ++n2) {
            n.a[n2] = n2 * n2;
        }
        for (n2 = 0; n2 < 256; ++n2) {
            n.a[n2] = n2 * n2;
        }
    }
}

