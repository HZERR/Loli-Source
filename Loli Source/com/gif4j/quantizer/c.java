/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j.quantizer;

import com.gif4j.quantizer.d;
import com.gif4j.quantizer.g;
import com.gif4j.quantizer.k;
import com.gif4j.quantizer.o;
import com.gif4j.quantizer.p;
import com.gif4j.quantizer.u;
import com.gif4j.quantizer.w;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

class c {
    private static final float[] a;
    private static final int[] b;

    c() {
    }

    public static BufferedImage a(BufferedImage bufferedImage, int n2, boolean bl, boolean bl2, boolean bl3) {
        int n3;
        int n4 = n3 = bl3 ? 256 : 8;
        if (n2 <= n3) {
            return c.a(bufferedImage, n2, bl3);
        }
        return c.b(bufferedImage, n2, bl3);
    }

    public static BufferedImage a(BufferedImage bufferedImage, int n2, boolean bl, boolean bl2) {
        return c.a(bufferedImage, n2, bl, bl2, false);
    }

    private static final BufferedImage a(int[] arrn, int n2, int n3, int n4, byte[] arrby, int n5) {
        int n6 = 1 << n4;
        k k2 = new k(n6);
        int n7 = 0;
        for (int i2 = 0; i2 < arrn.length && n7 <= n6; ++i2) {
            int n8 = arrn[i2] >> 24 & 0xFF;
            if (n8 <= n5 || !k2.c(arrn[i2])) continue;
            ++n7;
        }
        if (n7 >= n6) {
            return null;
        }
        n7 = b[n7];
        byte[] arrby2 = new byte[n7];
        byte[] arrby3 = new byte[n7];
        byte[] arrby4 = new byte[n7];
        arrby2[n7 - 1] = arrby[0];
        arrby3[n7 - 1] = arrby[1];
        arrby4[n7 - 1] = arrby[2];
        int[] arrn2 = (int[])k2.a();
        d d2 = new d(n7);
        int n9 = 0;
        boolean[] arrbl = k2.g;
        for (int i3 = 0; i3 < arrn2.length; ++i3) {
            if (!arrbl[i3]) continue;
            int n10 = arrn2[i3];
            arrby2[n9] = (byte)(n10 >> 16 & 0xFF);
            arrby3[n9] = (byte)(n10 >> 8 & 0xFF);
            arrby4[n9] = (byte)(n10 & 0xFF);
            d2.b(n10, n9);
            ++n9;
        }
        IndexColorModel indexColorModel = new IndexColorModel(n4, n7, arrby2, arrby3, arrby4, n7 - 1);
        DataBufferByte dataBufferByte = new DataBufferByte(arrn.length);
        byte[] arrby5 = dataBufferByte.getData();
        for (int i4 = 0; i4 < arrn.length; ++i4) {
            int n11 = arrn[i4] >> 24 & 0xFF;
            arrby5[i4] = n11 > n5 ? (byte)d2.c(arrn[i4]) : (byte)(n7 - 1);
        }
        WritableRaster writableRaster = Raster.createInterleavedRaster(dataBufferByte, n2, n3, n2, 1, new int[]{0}, null);
        return new BufferedImage(indexColorModel, writableRaster, false, null);
    }

    private static final BufferedImage a(BufferedImage bufferedImage, int n2, boolean bl) {
        int n3 = bufferedImage.getColorModel().hasAlpha() ? 126 : -1;
        int n4 = bufferedImage.getWidth();
        int n5 = bufferedImage.getHeight();
        int n6 = n2;
        if (!bl) {
            n6 = 1 << n2;
        } else {
            n2 = o.a(n6);
        }
        int n7 = n6 - 1;
        int[] arrn = o.a(bufferedImage);
        u[] arru = new u[4913];
        for (int i2 = 0; i2 < 4913; ++i2) {
            arru[i2] = new u();
        }
        byte[] arrby = c.a(arru, arrn, n3);
        int n8 = 0;
        for (int i3 = 0; i3 < 4913 && n8 <= n7; ++i3) {
            if (arru[i3].d == 0) continue;
            ++n8;
        }
        if (n8 <= n7) {
            BufferedImage bufferedImage2;
            if (n8 == 1 && arrby[0] == 0 && arrby[1] == 0 && arrby[2] == 0) {
                arrby[2] = -1;
                arrby[1] = -1;
                arrby[0] = -1;
            }
            if ((bufferedImage2 = c.a(arrn, n4, n5, n2, arrby, n3)) != null) {
                return bufferedImage2;
            }
            n6 = b[n8];
            n7 = n6 - 1;
            byte[] arrby2 = new byte[n6];
            byte[] arrby3 = new byte[n6];
            byte[] arrby4 = new byte[n6];
            arrby2[n7] = arrby[0];
            arrby3[n7] = arrby[1];
            arrby4[n7] = arrby[2];
            n8 = 0;
            for (int i4 = 0; i4 < 4913 && n8 < n7; ++i4) {
                if (arru[i4].d == 0) continue;
                arrby2[n8] = (byte)(arru[i4].a / arru[i4].d);
                arrby3[n8] = (byte)(arru[i4].b / arru[i4].d);
                arrby4[n8] = (byte)(arru[i4].c / arru[i4].d);
                arru[i4].f = (byte)n8;
                ++n8;
            }
            IndexColorModel indexColorModel = new IndexColorModel(n2, n6, arrby2, arrby3, arrby4, n7);
            DataBufferByte dataBufferByte = new DataBufferByte(arrn.length);
            byte[] arrby5 = dataBufferByte.getData();
            for (int i5 = 0; i5 < arrn.length; ++i5) {
                int n9 = arrn[i5] >> 24 & 0xFF;
                if (n9 > n3) {
                    int n10 = arrn[i5] >> 16 & 0xFF;
                    int n11 = arrn[i5] >> 8 & 0xFF;
                    int n12 = arrn[i5] & 0xFF;
                    int n13 = ((n10 >> 4) + 1) * 289 + ((n11 >> 4) + 1) * 17 + (n12 >> 4) + 1;
                    arrby5[i5] = arru[n13].f;
                    continue;
                }
                arrby5[i5] = (byte)n7;
            }
            WritableRaster writableRaster = Raster.createInterleavedRaster(dataBufferByte, n4, n5, n4, 1, new int[]{0}, null);
            return new BufferedImage(indexColorModel, writableRaster, false, null);
        }
        byte[] arrby6 = new byte[n6];
        byte[] arrby7 = new byte[n6];
        byte[] arrby8 = new byte[n6];
        arrby6[n7] = arrby[0];
        arrby7[n7] = arrby[1];
        arrby8[n7] = arrby[2];
        g[] arrg = new g[n7];
        int n14 = c.a(arru, arrg, n7);
        for (int i6 = 0; i6 < n14; ++i6) {
            long l2 = c.h(arrg[i6], arru);
            if (l2 != 0L) {
                arrby6[i6] = (byte)(c.e(arrg[i6], arru) / l2 & 0xFFL);
                arrby7[i6] = (byte)(c.f(arrg[i6], arru) / l2 & 0xFFL);
                arrby8[i6] = (byte)(c.g(arrg[i6], arru) / l2 & 0xFFL);
                continue;
            }
            arrby8[i6] = 0;
            arrby7[i6] = 0;
            arrby6[i6] = 0;
        }
        IndexColorModel indexColorModel = new IndexColorModel(n2, n6, arrby6, arrby7, arrby8, n7);
        DataBufferByte dataBufferByte = new DataBufferByte(arrn.length);
        byte[] arrby9 = dataBufferByte.getData();
        w w2 = new w(arrby6, arrby7, arrby8, n7);
        for (int i7 = 0; i7 < arrn.length; ++i7) {
            arrby9[i7] = (arrn[i7] >> 24 & 0xFF) > n3 ? w2.a(arrn[i7]) : (byte)n7;
        }
        WritableRaster writableRaster = Raster.createInterleavedRaster(dataBufferByte, n4, n5, n4, 1, new int[]{0}, null);
        return new BufferedImage(indexColorModel, writableRaster, false, null);
    }

    private static final BufferedImage b(BufferedImage bufferedImage, int n2, boolean bl) {
        int n3;
        int n4 = bufferedImage.getWidth();
        int n5 = bufferedImage.getHeight();
        int n6 = n2;
        if (!bl) {
            n6 = 1 << n2;
        }
        int[] arrn = o.a(bufferedImage);
        u[] arru = new u[4913];
        for (int i2 = 0; i2 < 4913; ++i2) {
            arru[i2] = new u();
        }
        c.a(arru, arrn);
        g[] arrg = new g[n6];
        int n7 = c.a(arru, arrg, n6);
        byte[] arrby = new byte[n6];
        byte[] arrby2 = new byte[n6];
        byte[] arrby3 = new byte[n6];
        if (n4 < -2) {
            p.a(arrg[0], arrg[1], arru);
        }
        int[] arrn2 = new int[4913];
        for (n3 = 0; n3 < n7; ++n3) {
            c.a(arrg[n3], n3, arrn2);
            long l2 = c.h(arrg[n3], arru);
            if (l2 != 0L) {
                arrby[n3] = (byte)(c.e(arrg[n3], arru) / l2 & 0xFFL);
                arrby2[n3] = (byte)(c.f(arrg[n3], arru) / l2 & 0xFFL);
                arrby3[n3] = (byte)(c.g(arrg[n3], arru) / l2 & 0xFFL);
                continue;
            }
            arrby3[n3] = 0;
            arrby2[n3] = 0;
            arrby[n3] = 0;
        }
        n3 = 2;
        BufferedImage bufferedImage2 = new BufferedImage(n4, n5, n3);
        int[] arrn3 = ((DataBufferInt)bufferedImage2.getRaster().getDataBuffer()).getData();
        for (int i3 = 0; i3 < arrn.length; ++i3) {
            int n8 = arrn[i3] >> 16 & 0xFF;
            int n9 = arrn[i3] >> 8 & 0xFF;
            int n10 = arrn[i3] & 0xFF;
            int n11 = arrn2[((n8 >> 4) + 1) * 289 + ((n9 >> 4) + 1) * 17 + (n10 >> 4) + 1];
            arrn3[i3] = arrn[i3] & 0xFF000000 | (arrby[n11] & 0xFF) << 16 | (arrby2[n11] & 0xFF) << 8 | arrby3[n11] & 0xFF;
        }
        return bufferedImage2;
    }

    private static final int a(u[] arru, g[] arrg, int n2) {
        c.a(arru);
        for (int i2 = 0; i2 < n2; ++i2) {
            arrg[i2] = new g();
        }
        float[] arrf = new float[n2];
        arrg[0].e = 0;
        arrg[0].c = 0;
        arrg[0].a = 0;
        arrg[0].f = 16;
        arrg[0].d = 16;
        arrg[0].b = 16;
        int n3 = n2;
        int n4 = 0;
        for (int i3 = 1; i3 < n3; ++i3) {
            if (c.a(arrg[n4], arrg[i3], arru) != 0) {
                arrf[n4] = arrg[n4].g > 1 ? c.i(arrg[n4], arru) : 0.0f;
                arrf[i3] = arrg[i3].g > 1 ? c.i(arrg[i3], arru) : 0.0f;
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

    private static final byte[] a(u[] arru, int[] arrn, int n2) {
        byte[] arrby = new byte[3];
        boolean bl = true;
        for (int i2 = 0; i2 < arrn.length; ++i2) {
            int n3 = arrn[i2] >> 24 & 0xFF;
            int n4 = arrn[i2] >> 16 & 0xFF;
            int n5 = arrn[i2] >> 8 & 0xFF;
            int n6 = arrn[i2] & 0xFF;
            if (n3 > n2) {
                int n7 = ((n4 >> 4) + 1) * 289 + ((n5 >> 4) + 1) * 17 + (n6 >> 4) + 1;
                u u2 = arru[n7];
                u2.a += n4;
                u2.b += n5;
                u2.c += n6;
                ++u2.d;
                u2.e += a[n4] + a[n5] + a[n6];
                continue;
            }
            if (bl) {
                arrby[0] = (byte)n4;
                arrby[1] = (byte)n5;
                arrby[2] = (byte)n6;
                bl = false;
                continue;
            }
            arrby[0] = (byte)((arrby[0] & 0xFF) + n4 >> 1);
            arrby[1] = (byte)((arrby[1] & 0xFF) + n5 >> 1);
            arrby[2] = (byte)((arrby[2] & 0xFF) + n6 >> 1);
        }
        return arrby;
    }

    private static final void a(u[] arru, int[] arrn) {
        for (int i2 = 0; i2 < arrn.length; ++i2) {
            int n2 = arrn[i2] >> 16 & 0xFF;
            int n3 = arrn[i2] >> 8 & 0xFF;
            int n4 = arrn[i2] & 0xFF;
            int n5 = ((n2 >> 4) + 1) * 289 + ((n3 >> 4) + 1) * 17 + (n4 >> 4) + 1;
            u u2 = arru[n5];
            u2.a += n2;
            u2.b += n3;
            u2.c += n4;
            ++u2.d;
            u2.e += a[n2] + a[n3] + a[n4];
        }
    }

    private static final void a(u[] arru) {
        int[] arrn = new int[17];
        int[] arrn2 = new int[17];
        int[] arrn3 = new int[17];
        int[] arrn4 = new int[17];
        float[] arrf = new float[17];
        for (int i2 = 1; i2 <= 16; ++i2) {
            int n2;
            for (n2 = 0; n2 <= 16; ++n2) {
                arrn3[n2] = 0;
                arrn2[n2] = 0;
                arrn[n2] = 0;
                arrn4[n2] = 0;
                arrf[n2] = 0;
            }
            for (n2 = 1; n2 <= 16; ++n2) {
                float f2 = 0.0f;
                int n3 = 0;
                int n4 = 0;
                int n5 = 0;
                int n6 = 0;
                for (int i3 = 1; i3 <= 16; ++i3) {
                    int n7 = i2 * 289 + n2 * 17 + i3;
                    int n8 = (i2 - 1) * 289 + n2 * 17 + i3;
                    u u2 = arru[n7];
                    u u3 = arru[n8];
                    int n9 = i3;
                    arrn4[n9] = arrn4[n9] + (n3 += u2.d);
                    int n10 = i3;
                    arrn[n10] = arrn[n10] + (n4 += u2.a);
                    int n11 = i3;
                    arrn2[n11] = arrn2[n11] + (n5 += u2.b);
                    int n12 = i3;
                    arrn3[n12] = arrn3[n12] + (n6 += u2.c);
                    int n13 = i3;
                    arrf[n13] = arrf[n13] + (f2 += u2.e);
                    u2.d = u3.d + arrn4[i3];
                    u2.a = u3.a + arrn[i3];
                    u2.b = u3.b + arrn2[i3];
                    u2.c = u3.c + arrn3[i3];
                    u2.e = u3.e + arrf[i3];
                }
            }
        }
    }

    private static final long e(g g2, u[] arru) {
        u u2 = arru[g2.b * 289 + g2.d * 17 + g2.f];
        u u3 = arru[g2.b * 289 + g2.d * 17 + g2.e];
        u u4 = arru[g2.b * 289 + g2.c * 17 + g2.f];
        u u5 = arru[g2.b * 289 + g2.c * 17 + g2.e];
        u u6 = arru[g2.a * 289 + g2.d * 17 + g2.f];
        u u7 = arru[g2.a * 289 + g2.d * 17 + g2.e];
        u u8 = arru[g2.a * 289 + g2.c * 17 + g2.f];
        u u9 = arru[g2.a * 289 + g2.c * 17 + g2.e];
        return u2.a - u3.a - u4.a + u5.a - u6.a + u7.a + u8.a - u9.a;
    }

    private static final long f(g g2, u[] arru) {
        u u2 = arru[g2.b * 289 + g2.d * 17 + g2.f];
        u u3 = arru[g2.b * 289 + g2.d * 17 + g2.e];
        u u4 = arru[g2.b * 289 + g2.c * 17 + g2.f];
        u u5 = arru[g2.b * 289 + g2.c * 17 + g2.e];
        u u6 = arru[g2.a * 289 + g2.d * 17 + g2.f];
        u u7 = arru[g2.a * 289 + g2.d * 17 + g2.e];
        u u8 = arru[g2.a * 289 + g2.c * 17 + g2.f];
        u u9 = arru[g2.a * 289 + g2.c * 17 + g2.e];
        return u2.b - u3.b - u4.b + u5.b - u6.b + u7.b + u8.b - u9.b;
    }

    private static final long g(g g2, u[] arru) {
        u u2 = arru[g2.b * 289 + g2.d * 17 + g2.f];
        u u3 = arru[g2.b * 289 + g2.d * 17 + g2.e];
        u u4 = arru[g2.b * 289 + g2.c * 17 + g2.f];
        u u5 = arru[g2.b * 289 + g2.c * 17 + g2.e];
        u u6 = arru[g2.a * 289 + g2.d * 17 + g2.f];
        u u7 = arru[g2.a * 289 + g2.d * 17 + g2.e];
        u u8 = arru[g2.a * 289 + g2.c * 17 + g2.f];
        u u9 = arru[g2.a * 289 + g2.c * 17 + g2.e];
        return u2.c - u3.c - u4.c + u5.c - u6.c + u7.c + u8.c - u9.c;
    }

    private static final long h(g g2, u[] arru) {
        u u2 = arru[g2.b * 289 + g2.d * 17 + g2.f];
        u u3 = arru[g2.b * 289 + g2.d * 17 + g2.e];
        u u4 = arru[g2.b * 289 + g2.c * 17 + g2.f];
        u u5 = arru[g2.b * 289 + g2.c * 17 + g2.e];
        u u6 = arru[g2.a * 289 + g2.d * 17 + g2.f];
        u u7 = arru[g2.a * 289 + g2.d * 17 + g2.e];
        u u8 = arru[g2.a * 289 + g2.c * 17 + g2.f];
        u u9 = arru[g2.a * 289 + g2.c * 17 + g2.e];
        return u2.d - u3.d - u4.d + u5.d - u6.d + u7.d + u8.d - u9.d;
    }

    private static final long a(g g2, int n2, u[] arru) {
        switch (n2) {
            case 2: {
                u u2 = arru[g2.a * 289 + g2.d * 17 + g2.f];
                u u3 = arru[g2.a * 289 + g2.d * 17 + g2.e];
                u u4 = arru[g2.a * 289 + g2.c * 17 + g2.f];
                u u5 = arru[g2.a * 289 + g2.c * 17 + g2.e];
                return -u2.a + u3.a + u4.a - u5.a;
            }
            case 1: {
                u u6 = arru[g2.b * 289 + g2.c * 17 + g2.f];
                u u7 = arru[g2.b * 289 + g2.c * 17 + g2.e];
                u u8 = arru[g2.a * 289 + g2.c * 17 + g2.f];
                u u9 = arru[g2.a * 289 + g2.c * 17 + g2.e];
                return -u6.a + u7.a + u8.a - u9.a;
            }
            case 0: {
                u u10 = arru[g2.b * 289 + g2.d * 17 + g2.e];
                u u11 = arru[g2.b * 289 + g2.c * 17 + g2.e];
                u u12 = arru[g2.a * 289 + g2.d * 17 + g2.e];
                u u13 = arru[g2.a * 289 + g2.c * 17 + g2.e];
                return -u10.a + u11.a + u12.a - u13.a;
            }
        }
        return 1L;
    }

    private static final long b(g g2, int n2, u[] arru) {
        switch (n2) {
            case 2: {
                u u2 = arru[g2.a * 289 + g2.d * 17 + g2.f];
                u u3 = arru[g2.a * 289 + g2.d * 17 + g2.e];
                u u4 = arru[g2.a * 289 + g2.c * 17 + g2.f];
                u u5 = arru[g2.a * 289 + g2.c * 17 + g2.e];
                return -u2.b + u3.b + u4.b - u5.b;
            }
            case 1: {
                u u6 = arru[g2.b * 289 + g2.c * 17 + g2.f];
                u u7 = arru[g2.b * 289 + g2.c * 17 + g2.e];
                u u8 = arru[g2.a * 289 + g2.c * 17 + g2.f];
                u u9 = arru[g2.a * 289 + g2.c * 17 + g2.e];
                return -u6.b + u7.b + u8.b - u9.b;
            }
            case 0: {
                u u10 = arru[g2.b * 289 + g2.d * 17 + g2.e];
                u u11 = arru[g2.b * 289 + g2.c * 17 + g2.e];
                u u12 = arru[g2.a * 289 + g2.d * 17 + g2.e];
                u u13 = arru[g2.a * 289 + g2.c * 17 + g2.e];
                return -u10.b + u11.b + u12.b - u13.b;
            }
        }
        return 1L;
    }

    private static final long c(g g2, int n2, u[] arru) {
        switch (n2) {
            case 2: {
                u u2 = arru[g2.a * 289 + g2.d * 17 + g2.f];
                u u3 = arru[g2.a * 289 + g2.d * 17 + g2.e];
                u u4 = arru[g2.a * 289 + g2.c * 17 + g2.f];
                u u5 = arru[g2.a * 289 + g2.c * 17 + g2.e];
                return -u2.c + u3.c + u4.c - u5.c;
            }
            case 1: {
                u u6 = arru[g2.b * 289 + g2.c * 17 + g2.f];
                u u7 = arru[g2.b * 289 + g2.c * 17 + g2.e];
                u u8 = arru[g2.a * 289 + g2.c * 17 + g2.f];
                u u9 = arru[g2.a * 289 + g2.c * 17 + g2.e];
                return -u6.c + u7.c + u8.c - u9.c;
            }
            case 0: {
                u u10 = arru[g2.b * 289 + g2.d * 17 + g2.e];
                u u11 = arru[g2.b * 289 + g2.c * 17 + g2.e];
                u u12 = arru[g2.a * 289 + g2.d * 17 + g2.e];
                u u13 = arru[g2.a * 289 + g2.c * 17 + g2.e];
                return -u10.c + u11.c + u12.c - u13.c;
            }
        }
        return 1L;
    }

    private static final long d(g g2, int n2, u[] arru) {
        switch (n2) {
            case 2: {
                u u2 = arru[g2.a * 289 + g2.d * 17 + g2.f];
                u u3 = arru[g2.a * 289 + g2.d * 17 + g2.e];
                u u4 = arru[g2.a * 289 + g2.c * 17 + g2.f];
                u u5 = arru[g2.a * 289 + g2.c * 17 + g2.e];
                return -u2.d + u3.d + u4.d - u5.d;
            }
            case 1: {
                u u6 = arru[g2.b * 289 + g2.c * 17 + g2.f];
                u u7 = arru[g2.b * 289 + g2.c * 17 + g2.e];
                u u8 = arru[g2.a * 289 + g2.c * 17 + g2.f];
                u u9 = arru[g2.a * 289 + g2.c * 17 + g2.e];
                return -u6.d + u7.d + u8.d - u9.d;
            }
            case 0: {
                u u10 = arru[g2.b * 289 + g2.d * 17 + g2.e];
                u u11 = arru[g2.b * 289 + g2.c * 17 + g2.e];
                u u12 = arru[g2.a * 289 + g2.d * 17 + g2.e];
                u u13 = arru[g2.a * 289 + g2.c * 17 + g2.e];
                return -u10.d + u11.d + u12.d - u13.d;
            }
        }
        return 1L;
    }

    private static final long a(g g2, int n2, int n3, u[] arru) {
        switch (n2) {
            case 2: {
                u u2 = arru[n3 * 289 + g2.d * 17 + g2.f];
                u u3 = arru[n3 * 289 + g2.d * 17 + g2.e];
                u u4 = arru[n3 * 289 + g2.c * 17 + g2.f];
                u u5 = arru[n3 * 289 + g2.c * 17 + g2.e];
                return u2.a - u3.a - u4.a + u5.a;
            }
            case 1: {
                u u6 = arru[g2.b * 289 + n3 * 17 + g2.f];
                u u7 = arru[g2.b * 289 + n3 * 17 + g2.e];
                u u8 = arru[g2.a * 289 + n3 * 17 + g2.f];
                u u9 = arru[g2.a * 289 + n3 * 17 + g2.e];
                return u6.a - u7.a - u8.a + u9.a;
            }
            case 0: {
                u u10 = arru[g2.b * 289 + g2.d * 17 + n3];
                u u11 = arru[g2.b * 289 + g2.c * 17 + n3];
                u u12 = arru[g2.a * 289 + g2.d * 17 + n3];
                u u13 = arru[g2.a * 289 + g2.c * 17 + n3];
                return u10.a - u11.a - u12.a + u13.a;
            }
        }
        return 1L;
    }

    private static final long b(g g2, int n2, int n3, u[] arru) {
        switch (n2) {
            case 2: {
                u u2 = arru[n3 * 289 + g2.d * 17 + g2.f];
                u u3 = arru[n3 * 289 + g2.d * 17 + g2.e];
                u u4 = arru[n3 * 289 + g2.c * 17 + g2.f];
                u u5 = arru[n3 * 289 + g2.c * 17 + g2.e];
                return u2.b - u3.b - u4.b + u5.b;
            }
            case 1: {
                u u6 = arru[g2.b * 289 + n3 * 17 + g2.f];
                u u7 = arru[g2.b * 289 + n3 * 17 + g2.e];
                u u8 = arru[g2.a * 289 + n3 * 17 + g2.f];
                u u9 = arru[g2.a * 289 + n3 * 17 + g2.e];
                return u6.b - u7.b - u8.b + u9.b;
            }
            case 0: {
                u u10 = arru[g2.b * 289 + g2.d * 17 + n3];
                u u11 = arru[g2.b * 289 + g2.c * 17 + n3];
                u u12 = arru[g2.a * 289 + g2.d * 17 + n3];
                u u13 = arru[g2.a * 289 + g2.c * 17 + n3];
                return u10.b - u11.b - u12.b + u13.b;
            }
        }
        return 1L;
    }

    private static final long c(g g2, int n2, int n3, u[] arru) {
        switch (n2) {
            case 2: {
                u u2 = arru[n3 * 289 + g2.d * 17 + g2.f];
                u u3 = arru[n3 * 289 + g2.d * 17 + g2.e];
                u u4 = arru[n3 * 289 + g2.c * 17 + g2.f];
                u u5 = arru[n3 * 289 + g2.c * 17 + g2.e];
                return u2.c - u3.c - u4.c + u5.c;
            }
            case 1: {
                u u6 = arru[g2.b * 289 + n3 * 17 + g2.f];
                u u7 = arru[g2.b * 289 + n3 * 17 + g2.e];
                u u8 = arru[g2.a * 289 + n3 * 17 + g2.f];
                u u9 = arru[g2.a * 289 + n3 * 17 + g2.e];
                return u6.c - u7.c - u8.c + u9.c;
            }
            case 0: {
                u u10 = arru[g2.b * 289 + g2.d * 17 + n3];
                u u11 = arru[g2.b * 289 + g2.c * 17 + n3];
                u u12 = arru[g2.a * 289 + g2.d * 17 + n3];
                u u13 = arru[g2.a * 289 + g2.c * 17 + n3];
                return u10.c - u11.c - u12.c + u13.c;
            }
        }
        return 1L;
    }

    private static final long d(g g2, int n2, int n3, u[] arru) {
        switch (n2) {
            case 2: {
                u u2 = arru[n3 * 289 + g2.d * 17 + g2.f];
                u u3 = arru[n3 * 289 + g2.d * 17 + g2.e];
                u u4 = arru[n3 * 289 + g2.c * 17 + g2.f];
                u u5 = arru[n3 * 289 + g2.c * 17 + g2.e];
                return u2.d - u3.d - u4.d + u5.d;
            }
            case 1: {
                u u6 = arru[g2.b * 289 + n3 * 17 + g2.f];
                u u7 = arru[g2.b * 289 + n3 * 17 + g2.e];
                u u8 = arru[g2.a * 289 + n3 * 17 + g2.f];
                u u9 = arru[g2.a * 289 + n3 * 17 + g2.e];
                return u6.d - u7.d - u8.d + u9.d;
            }
            case 0: {
                u u10 = arru[g2.b * 289 + g2.d * 17 + n3];
                u u11 = arru[g2.b * 289 + g2.c * 17 + n3];
                u u12 = arru[g2.a * 289 + g2.d * 17 + n3];
                u u13 = arru[g2.a * 289 + g2.c * 17 + n3];
                return u10.d - u11.d - u12.d + u13.d;
            }
        }
        return 1L;
    }

    private static final float i(g g2, u[] arru) {
        long l2 = c.e(g2, arru);
        long l3 = c.f(g2, arru);
        long l4 = c.g(g2, arru);
        long l5 = c.h(g2, arru);
        float f2 = arru[g2.b * 289 + g2.d * 17 + g2.f].e - arru[g2.b * 289 + g2.d * 17 + g2.e].e - arru[g2.b * 289 + g2.c * 17 + g2.f].e + arru[g2.b * 289 + g2.c * 17 + g2.e].e - arru[g2.a * 289 + g2.d * 17 + g2.f].e + arru[g2.a * 289 + g2.d * 17 + g2.e].e + arru[g2.a * 289 + g2.c * 17 + g2.f].e - arru[g2.a * 289 + g2.c * 17 + g2.e].e;
        return f2 - (float)(l2 * l2 + l4 * l4 + l3 * l3) / (float)l5;
    }

    private static final float b(g g2, int n2, int n3, int n4, int[] arrn, long l2, long l3, long l4, long l5, u[] arru) {
        long l6 = c.a(g2, n2, arru);
        long l7 = c.b(g2, n2, arru);
        long l8 = c.c(g2, n2, arru);
        long l9 = c.d(g2, n2, arru);
        float f2 = 0.0f;
        float f3 = 0.0f;
        arrn[0] = -1;
        for (int i2 = n3; i2 < n4; ++i2) {
            long l10 = l6 + c.a(g2, n2, i2, arru);
            long l11 = l7 + c.b(g2, n2, i2, arru);
            long l12 = l8 + c.c(g2, n2, i2, arru);
            long l13 = l9 + c.d(g2, n2, i2, arru);
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

    private static final int a(g g2, g g3, u[] arru) {
        int n2;
        int[] arrn = new int[1];
        int[] arrn2 = new int[1];
        int[] arrn3 = new int[1];
        long l2 = c.e(g2, arru);
        long l3 = c.f(g2, arru);
        long l4 = c.g(g2, arru);
        long l5 = c.h(g2, arru);
        float f2 = c.b(g2, 2, g2.a + 1, g2.b, arrn, l2, l3, l4, l5, arru);
        float f3 = c.b(g2, 1, g2.c + 1, g2.d, arrn2, l2, l3, l4, l5, arru);
        float f4 = c.b(g2, 0, g2.e + 1, g2.f, arrn3, l2, l3, l4, l5, arru);
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

    private static final void a(g g2, int n2, int[] arrn) {
        for (int i2 = g2.a + 1; i2 <= g2.b; ++i2) {
            for (int i3 = g2.c + 1; i3 <= g2.d; ++i3) {
                for (int i4 = g2.e + 1; i4 <= g2.f; ++i4) {
                    arrn[i2 * 289 + i3 * 17 + i4] = n2;
                }
            }
        }
    }

    static long a(g g2, u[] arru) {
        return c.e(g2, arru);
    }

    static long b(g g2, u[] arru) {
        return c.f(g2, arru);
    }

    static long c(g g2, u[] arru) {
        return c.g(g2, arru);
    }

    static long d(g g2, u[] arru) {
        return c.h(g2, arru);
    }

    static float a(g g2, int n2, int n3, int n4, int[] arrn, long l2, long l3, long l4, long l5, u[] arru) {
        return c.b(g2, n2, n3, n4, arrn, l2, l3, l4, l5, arru);
    }

    static {
        int n2;
        a = new float[256];
        b = new int[257];
        c.b[0] = 2;
        c.b[3] = 4;
        c.b[2] = 4;
        c.b[1] = 4;
        c.b[7] = 8;
        c.b[6] = 8;
        c.b[5] = 8;
        c.b[4] = 8;
        for (n2 = 8; n2 < 16; ++n2) {
            c.b[n2] = 16;
        }
        for (n2 = 16; n2 < 32; ++n2) {
            c.b[n2] = 32;
        }
        for (n2 = 32; n2 < 64; ++n2) {
            c.b[n2] = 64;
        }
        for (n2 = 64; n2 < 128; ++n2) {
            c.b[n2] = 128;
        }
        for (n2 = 128; n2 <= 256; ++n2) {
            c.b[n2] = 256;
        }
        for (n2 = 0; n2 < 256; ++n2) {
            c.a[n2] = n2 * n2;
        }
    }
}

