/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j.quantizer;

import com.gif4j.quantizer.d;
import com.gif4j.quantizer.k;
import com.gif4j.quantizer.o;
import com.gif4j.quantizer.r;
import com.gif4j.quantizer.s;
import com.gif4j.quantizer.w;
import com.gif4j.quantizer.x;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

class q {
    private static final float[] a;
    private static final int[] b;

    q() {
    }

    public static BufferedImage a(BufferedImage bufferedImage, int n2, boolean bl, boolean bl2) {
        return q.a(bufferedImage, n2, bl, bl2, false);
    }

    public static BufferedImage a(BufferedImage bufferedImage, int n2, boolean bl, boolean bl2, boolean bl3) {
        int n3;
        int n4 = n3 = bl3 ? 256 : 8;
        if (n2 <= n3) {
            return q.a(bufferedImage, n2, bl3);
        }
        return q.b(bufferedImage, n2, bl3);
    }

    private static final BufferedImage a(int[] arrn, int n2, int n3, int n4) {
        int n5 = 1 << n4;
        k k2 = new k(n5);
        int n6 = 0;
        for (int i2 = 0; i2 < arrn.length && n6 <= n5; ++i2) {
            if (!k2.c(arrn[i2])) continue;
            ++n6;
        }
        if (n6 >= n5) {
            return null;
        }
        n6 = b[n6];
        byte[] arrby = new byte[n6];
        byte[] arrby2 = new byte[n6];
        byte[] arrby3 = new byte[n6];
        int[] arrn2 = (int[])k2.a();
        d d2 = new d(n6);
        int n7 = 0;
        boolean[] arrbl = k2.g;
        for (int i3 = 0; i3 < arrn2.length; ++i3) {
            if (!arrbl[i3]) continue;
            int n8 = arrn2[i3];
            arrby[n7] = (byte)(n8 >> 16 & 0xFF);
            arrby2[n7] = (byte)(n8 >> 8 & 0xFF);
            arrby3[n7] = (byte)(n8 & 0xFF);
            d2.b(n8, n7);
            ++n7;
        }
        IndexColorModel indexColorModel = new IndexColorModel(n4, n6, arrby, arrby2, arrby3);
        DataBufferByte dataBufferByte = new DataBufferByte(arrn.length);
        byte[] arrby4 = dataBufferByte.getData();
        for (int i4 = 0; i4 < arrn.length; ++i4) {
            arrby4[i4] = (byte)d2.c(arrn[i4]);
        }
        WritableRaster writableRaster = Raster.createInterleavedRaster(dataBufferByte, n2, n3, n2, 1, new int[]{0}, null);
        return new BufferedImage(indexColorModel, writableRaster, false, null);
    }

    private static final BufferedImage a(BufferedImage bufferedImage, int n2, boolean bl) {
        int n3;
        int n4 = bufferedImage.getWidth();
        int n5 = bufferedImage.getHeight();
        int n6 = n2;
        if (!bl) {
            n6 = 1 << n2;
        } else {
            n2 = o.a(n6);
        }
        int[] arrn = o.a(bufferedImage);
        s[] arrs = new s[4913];
        for (n3 = 0; n3 < 4913; ++n3) {
            arrs[n3] = new s();
        }
        q.b(arrs, arrn);
        n3 = 0;
        for (int i2 = 0; i2 < 4913 && n3 <= n6; ++i2) {
            if (arrs[i2].d == 0) continue;
            ++n3;
        }
        if (n3 <= n6) {
            BufferedImage bufferedImage2 = q.a(arrn, n4, n5, n2);
            if (bufferedImage2 != null) {
                return bufferedImage2;
            }
            n6 = b[n3];
            byte[] arrby = new byte[n6];
            byte[] arrby2 = new byte[n6];
            byte[] arrby3 = new byte[n6];
            n3 = 0;
            n3 = 0;
            for (int i3 = 0; i3 < 4913 && n3 < n6; ++i3) {
                if (arrs[i3].d == 0) continue;
                arrby[n3] = (byte)(arrs[i3].a / arrs[i3].d);
                arrby2[n3] = (byte)(arrs[i3].b / arrs[i3].d);
                arrby3[n3] = (byte)(arrs[i3].c / arrs[i3].d);
                arrs[i3].f = (byte)n3;
                ++n3;
            }
            IndexColorModel indexColorModel = new IndexColorModel(n2, n6, arrby, arrby2, arrby3);
            DataBufferByte dataBufferByte = new DataBufferByte(arrn.length);
            byte[] arrby4 = dataBufferByte.getData();
            for (int i4 = 0; i4 < arrn.length; ++i4) {
                int n7 = arrn[i4] >> 16 & 0xFF;
                int n8 = arrn[i4] >> 8 & 0xFF;
                int n9 = arrn[i4] & 0xFF;
                int n10 = ((n7 >> 4) + 1) * 289 + ((n8 >> 4) + 1) * 17 + (n9 >> 4) + 1;
                arrby4[i4] = arrs[n10].f;
            }
            WritableRaster writableRaster = Raster.createInterleavedRaster(dataBufferByte, n4, n5, n4, 1, new int[]{0}, null);
            return new BufferedImage(indexColorModel, writableRaster, false, null);
        }
        byte[] arrby = new byte[n6];
        byte[] arrby5 = new byte[n6];
        byte[] arrby6 = new byte[n6];
        r[] arrr = new r[n6];
        int n11 = q.a(arrs, arrr, n6);
        for (int i5 = 0; i5 < n11; ++i5) {
            long l2 = q.h(arrr[i5], arrs);
            if (l2 != 0L) {
                arrby[i5] = (byte)(q.e(arrr[i5], arrs) / l2 & 0xFFL);
                arrby5[i5] = (byte)(q.f(arrr[i5], arrs) / l2 & 0xFFL);
                arrby6[i5] = (byte)(q.g(arrr[i5], arrs) / l2 & 0xFFL);
                continue;
            }
            arrby6[i5] = 0;
            arrby5[i5] = 0;
            arrby[i5] = 0;
        }
        IndexColorModel indexColorModel = new IndexColorModel(n2, n6, arrby, arrby5, arrby6);
        DataBufferByte dataBufferByte = new DataBufferByte(arrn.length);
        byte[] arrby7 = dataBufferByte.getData();
        w w2 = new w(arrby, arrby5, arrby6);
        for (int i6 = 0; i6 < arrn.length; ++i6) {
            arrby7[i6] = w2.a(arrn[i6]);
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
        s[] arrs = new s[4913];
        for (int i2 = 0; i2 < 4913; ++i2) {
            arrs[i2] = new s();
        }
        q.a(arrs, arrn);
        r[] arrr = new r[n6];
        int n7 = q.a(arrs, arrr, n6);
        int[] arrn2 = new int[4913];
        byte[] arrby = new byte[n6];
        byte[] arrby2 = new byte[n6];
        byte[] arrby3 = new byte[n6];
        if (n4 < -2) {
            x.a(arrr[0], arrr[1], arrs);
        }
        for (n3 = 0; n3 < n7; ++n3) {
            q.a(arrr[n3], n3, arrn2);
            long l2 = q.h(arrr[n3], arrs);
            if (l2 != 0L) {
                arrby[n3] = (byte)(q.e(arrr[n3], arrs) / l2 & 0xFFL);
                arrby2[n3] = (byte)(q.f(arrr[n3], arrs) / l2 & 0xFFL);
                arrby3[n3] = (byte)(q.g(arrr[n3], arrs) / l2 & 0xFFL);
                continue;
            }
            arrby3[n3] = 0;
            arrby2[n3] = 0;
            arrby[n3] = 0;
        }
        n3 = 1;
        BufferedImage bufferedImage2 = new BufferedImage(n4, n5, n3);
        int[] arrn3 = ((DataBufferInt)bufferedImage2.getRaster().getDataBuffer()).getData();
        for (int i3 = 0; i3 < arrn.length; ++i3) {
            int n8 = arrn2[arrn[i3]];
            arrn3[i3] = (arrby[n8] & 0xFF) << 16 | (arrby2[n8] & 0xFF) << 8 | arrby3[n8] & 0xFF;
        }
        return bufferedImage2;
    }

    private static final int a(s[] arrs, r[] arrr, int n2) {
        q.a(arrs);
        for (int i2 = 0; i2 < n2; ++i2) {
            arrr[i2] = new r();
        }
        float[] arrf = new float[n2];
        arrr[0].e = 0;
        arrr[0].c = 0;
        arrr[0].a = 0;
        arrr[0].f = 16;
        arrr[0].d = 16;
        arrr[0].b = 16;
        int n3 = n2;
        int n4 = 0;
        for (int i3 = 1; i3 < n3; ++i3) {
            if (q.a(arrr[n4], arrr[i3], arrs) != 0) {
                arrf[n4] = arrr[n4].g > 1 ? q.i(arrr[n4], arrs) : 0.0f;
                arrf[i3] = arrr[i3].g > 1 ? q.i(arrr[i3], arrs) : 0.0f;
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

    private static final void a(s[] arrs, int[] arrn) {
        for (int i2 = 0; i2 < arrn.length; ++i2) {
            int n2;
            int n3 = arrn[i2] >> 16 & 0xFF;
            int n4 = arrn[i2] >> 8 & 0xFF;
            int n5 = arrn[i2] & 0xFF;
            arrn[i2] = n2 = ((n3 >> 4) + 1) * 289 + ((n4 >> 4) + 1) * 17 + (n5 >> 4) + 1;
            s s2 = arrs[n2];
            s2.a += n3;
            s2.b += n4;
            s2.c += n5;
            ++s2.d;
            s2.e += a[n3] + a[n4] + a[n5];
        }
    }

    private static final void b(s[] arrs, int[] arrn) {
        for (int i2 = 0; i2 < arrn.length; ++i2) {
            int n2 = arrn[i2] >> 16 & 0xFF;
            int n3 = arrn[i2] >> 8 & 0xFF;
            int n4 = arrn[i2] & 0xFF;
            int n5 = ((n2 >> 4) + 1) * 289 + ((n3 >> 4) + 1) * 17 + (n4 >> 4) + 1;
            s s2 = arrs[n5];
            s2.a += n2;
            s2.b += n3;
            s2.c += n4;
            ++s2.d;
            s2.e += a[n2] + a[n3] + a[n4];
        }
    }

    private static final void a(s[] arrs) {
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

    private static final long e(r r2, s[] arrs) {
        s s2 = arrs[r2.b * 289 + r2.d * 17 + r2.f];
        s s3 = arrs[r2.b * 289 + r2.d * 17 + r2.e];
        s s4 = arrs[r2.b * 289 + r2.c * 17 + r2.f];
        s s5 = arrs[r2.b * 289 + r2.c * 17 + r2.e];
        s s6 = arrs[r2.a * 289 + r2.d * 17 + r2.f];
        s s7 = arrs[r2.a * 289 + r2.d * 17 + r2.e];
        s s8 = arrs[r2.a * 289 + r2.c * 17 + r2.f];
        s s9 = arrs[r2.a * 289 + r2.c * 17 + r2.e];
        return s2.a - s3.a - s4.a + s5.a - s6.a + s7.a + s8.a - s9.a;
    }

    private static final long f(r r2, s[] arrs) {
        s s2 = arrs[r2.b * 289 + r2.d * 17 + r2.f];
        s s3 = arrs[r2.b * 289 + r2.d * 17 + r2.e];
        s s4 = arrs[r2.b * 289 + r2.c * 17 + r2.f];
        s s5 = arrs[r2.b * 289 + r2.c * 17 + r2.e];
        s s6 = arrs[r2.a * 289 + r2.d * 17 + r2.f];
        s s7 = arrs[r2.a * 289 + r2.d * 17 + r2.e];
        s s8 = arrs[r2.a * 289 + r2.c * 17 + r2.f];
        s s9 = arrs[r2.a * 289 + r2.c * 17 + r2.e];
        return s2.b - s3.b - s4.b + s5.b - s6.b + s7.b + s8.b - s9.b;
    }

    private static final long g(r r2, s[] arrs) {
        s s2 = arrs[r2.b * 289 + r2.d * 17 + r2.f];
        s s3 = arrs[r2.b * 289 + r2.d * 17 + r2.e];
        s s4 = arrs[r2.b * 289 + r2.c * 17 + r2.f];
        s s5 = arrs[r2.b * 289 + r2.c * 17 + r2.e];
        s s6 = arrs[r2.a * 289 + r2.d * 17 + r2.f];
        s s7 = arrs[r2.a * 289 + r2.d * 17 + r2.e];
        s s8 = arrs[r2.a * 289 + r2.c * 17 + r2.f];
        s s9 = arrs[r2.a * 289 + r2.c * 17 + r2.e];
        return s2.c - s3.c - s4.c + s5.c - s6.c + s7.c + s8.c - s9.c;
    }

    private static final long h(r r2, s[] arrs) {
        s s2 = arrs[r2.b * 289 + r2.d * 17 + r2.f];
        s s3 = arrs[r2.b * 289 + r2.d * 17 + r2.e];
        s s4 = arrs[r2.b * 289 + r2.c * 17 + r2.f];
        s s5 = arrs[r2.b * 289 + r2.c * 17 + r2.e];
        s s6 = arrs[r2.a * 289 + r2.d * 17 + r2.f];
        s s7 = arrs[r2.a * 289 + r2.d * 17 + r2.e];
        s s8 = arrs[r2.a * 289 + r2.c * 17 + r2.f];
        s s9 = arrs[r2.a * 289 + r2.c * 17 + r2.e];
        return s2.d - s3.d - s4.d + s5.d - s6.d + s7.d + s8.d - s9.d;
    }

    private static final long a(r r2, int n2, s[] arrs) {
        switch (n2) {
            case 2: {
                s s2 = arrs[r2.a * 289 + r2.d * 17 + r2.f];
                s s3 = arrs[r2.a * 289 + r2.d * 17 + r2.e];
                s s4 = arrs[r2.a * 289 + r2.c * 17 + r2.f];
                s s5 = arrs[r2.a * 289 + r2.c * 17 + r2.e];
                return -s2.a + s3.a + s4.a - s5.a;
            }
            case 1: {
                s s6 = arrs[r2.b * 289 + r2.c * 17 + r2.f];
                s s7 = arrs[r2.b * 289 + r2.c * 17 + r2.e];
                s s8 = arrs[r2.a * 289 + r2.c * 17 + r2.f];
                s s9 = arrs[r2.a * 289 + r2.c * 17 + r2.e];
                return -s6.a + s7.a + s8.a - s9.a;
            }
            case 0: {
                s s10 = arrs[r2.b * 289 + r2.d * 17 + r2.e];
                s s11 = arrs[r2.b * 289 + r2.c * 17 + r2.e];
                s s12 = arrs[r2.a * 289 + r2.d * 17 + r2.e];
                s s13 = arrs[r2.a * 289 + r2.c * 17 + r2.e];
                return -s10.a + s11.a + s12.a - s13.a;
            }
        }
        return 1L;
    }

    private static final long b(r r2, int n2, s[] arrs) {
        switch (n2) {
            case 2: {
                s s2 = arrs[r2.a * 289 + r2.d * 17 + r2.f];
                s s3 = arrs[r2.a * 289 + r2.d * 17 + r2.e];
                s s4 = arrs[r2.a * 289 + r2.c * 17 + r2.f];
                s s5 = arrs[r2.a * 289 + r2.c * 17 + r2.e];
                return -s2.b + s3.b + s4.b - s5.b;
            }
            case 1: {
                s s6 = arrs[r2.b * 289 + r2.c * 17 + r2.f];
                s s7 = arrs[r2.b * 289 + r2.c * 17 + r2.e];
                s s8 = arrs[r2.a * 289 + r2.c * 17 + r2.f];
                s s9 = arrs[r2.a * 289 + r2.c * 17 + r2.e];
                return -s6.b + s7.b + s8.b - s9.b;
            }
            case 0: {
                s s10 = arrs[r2.b * 289 + r2.d * 17 + r2.e];
                s s11 = arrs[r2.b * 289 + r2.c * 17 + r2.e];
                s s12 = arrs[r2.a * 289 + r2.d * 17 + r2.e];
                s s13 = arrs[r2.a * 289 + r2.c * 17 + r2.e];
                return -s10.b + s11.b + s12.b - s13.b;
            }
        }
        return 1L;
    }

    private static final long c(r r2, int n2, s[] arrs) {
        switch (n2) {
            case 2: {
                s s2 = arrs[r2.a * 289 + r2.d * 17 + r2.f];
                s s3 = arrs[r2.a * 289 + r2.d * 17 + r2.e];
                s s4 = arrs[r2.a * 289 + r2.c * 17 + r2.f];
                s s5 = arrs[r2.a * 289 + r2.c * 17 + r2.e];
                return -s2.c + s3.c + s4.c - s5.c;
            }
            case 1: {
                s s6 = arrs[r2.b * 289 + r2.c * 17 + r2.f];
                s s7 = arrs[r2.b * 289 + r2.c * 17 + r2.e];
                s s8 = arrs[r2.a * 289 + r2.c * 17 + r2.f];
                s s9 = arrs[r2.a * 289 + r2.c * 17 + r2.e];
                return -s6.c + s7.c + s8.c - s9.c;
            }
            case 0: {
                s s10 = arrs[r2.b * 289 + r2.d * 17 + r2.e];
                s s11 = arrs[r2.b * 289 + r2.c * 17 + r2.e];
                s s12 = arrs[r2.a * 289 + r2.d * 17 + r2.e];
                s s13 = arrs[r2.a * 289 + r2.c * 17 + r2.e];
                return -s10.c + s11.c + s12.c - s13.c;
            }
        }
        return 1L;
    }

    private static final long d(r r2, int n2, s[] arrs) {
        switch (n2) {
            case 2: {
                s s2 = arrs[r2.a * 289 + r2.d * 17 + r2.f];
                s s3 = arrs[r2.a * 289 + r2.d * 17 + r2.e];
                s s4 = arrs[r2.a * 289 + r2.c * 17 + r2.f];
                s s5 = arrs[r2.a * 289 + r2.c * 17 + r2.e];
                return -s2.d + s3.d + s4.d - s5.d;
            }
            case 1: {
                s s6 = arrs[r2.b * 289 + r2.c * 17 + r2.f];
                s s7 = arrs[r2.b * 289 + r2.c * 17 + r2.e];
                s s8 = arrs[r2.a * 289 + r2.c * 17 + r2.f];
                s s9 = arrs[r2.a * 289 + r2.c * 17 + r2.e];
                return -s6.d + s7.d + s8.d - s9.d;
            }
            case 0: {
                s s10 = arrs[r2.b * 289 + r2.d * 17 + r2.e];
                s s11 = arrs[r2.b * 289 + r2.c * 17 + r2.e];
                s s12 = arrs[r2.a * 289 + r2.d * 17 + r2.e];
                s s13 = arrs[r2.a * 289 + r2.c * 17 + r2.e];
                return -s10.d + s11.d + s12.d - s13.d;
            }
        }
        return 1L;
    }

    private static final long a(r r2, int n2, int n3, s[] arrs) {
        switch (n2) {
            case 2: {
                s s2 = arrs[n3 * 289 + r2.d * 17 + r2.f];
                s s3 = arrs[n3 * 289 + r2.d * 17 + r2.e];
                s s4 = arrs[n3 * 289 + r2.c * 17 + r2.f];
                s s5 = arrs[n3 * 289 + r2.c * 17 + r2.e];
                return s2.a - s3.a - s4.a + s5.a;
            }
            case 1: {
                s s6 = arrs[r2.b * 289 + n3 * 17 + r2.f];
                s s7 = arrs[r2.b * 289 + n3 * 17 + r2.e];
                s s8 = arrs[r2.a * 289 + n3 * 17 + r2.f];
                s s9 = arrs[r2.a * 289 + n3 * 17 + r2.e];
                return s6.a - s7.a - s8.a + s9.a;
            }
            case 0: {
                s s10 = arrs[r2.b * 289 + r2.d * 17 + n3];
                s s11 = arrs[r2.b * 289 + r2.c * 17 + n3];
                s s12 = arrs[r2.a * 289 + r2.d * 17 + n3];
                s s13 = arrs[r2.a * 289 + r2.c * 17 + n3];
                return s10.a - s11.a - s12.a + s13.a;
            }
        }
        return 1L;
    }

    private static final long b(r r2, int n2, int n3, s[] arrs) {
        switch (n2) {
            case 2: {
                s s2 = arrs[n3 * 289 + r2.d * 17 + r2.f];
                s s3 = arrs[n3 * 289 + r2.d * 17 + r2.e];
                s s4 = arrs[n3 * 289 + r2.c * 17 + r2.f];
                s s5 = arrs[n3 * 289 + r2.c * 17 + r2.e];
                return s2.b - s3.b - s4.b + s5.b;
            }
            case 1: {
                s s6 = arrs[r2.b * 289 + n3 * 17 + r2.f];
                s s7 = arrs[r2.b * 289 + n3 * 17 + r2.e];
                s s8 = arrs[r2.a * 289 + n3 * 17 + r2.f];
                s s9 = arrs[r2.a * 289 + n3 * 17 + r2.e];
                return s6.b - s7.b - s8.b + s9.b;
            }
            case 0: {
                s s10 = arrs[r2.b * 289 + r2.d * 17 + n3];
                s s11 = arrs[r2.b * 289 + r2.c * 17 + n3];
                s s12 = arrs[r2.a * 289 + r2.d * 17 + n3];
                s s13 = arrs[r2.a * 289 + r2.c * 17 + n3];
                return s10.b - s11.b - s12.b + s13.b;
            }
        }
        return 1L;
    }

    private static final long c(r r2, int n2, int n3, s[] arrs) {
        switch (n2) {
            case 2: {
                s s2 = arrs[n3 * 289 + r2.d * 17 + r2.f];
                s s3 = arrs[n3 * 289 + r2.d * 17 + r2.e];
                s s4 = arrs[n3 * 289 + r2.c * 17 + r2.f];
                s s5 = arrs[n3 * 289 + r2.c * 17 + r2.e];
                return s2.c - s3.c - s4.c + s5.c;
            }
            case 1: {
                s s6 = arrs[r2.b * 289 + n3 * 17 + r2.f];
                s s7 = arrs[r2.b * 289 + n3 * 17 + r2.e];
                s s8 = arrs[r2.a * 289 + n3 * 17 + r2.f];
                s s9 = arrs[r2.a * 289 + n3 * 17 + r2.e];
                return s6.c - s7.c - s8.c + s9.c;
            }
            case 0: {
                s s10 = arrs[r2.b * 289 + r2.d * 17 + n3];
                s s11 = arrs[r2.b * 289 + r2.c * 17 + n3];
                s s12 = arrs[r2.a * 289 + r2.d * 17 + n3];
                s s13 = arrs[r2.a * 289 + r2.c * 17 + n3];
                return s10.c - s11.c - s12.c + s13.c;
            }
        }
        return 1L;
    }

    private static final long d(r r2, int n2, int n3, s[] arrs) {
        switch (n2) {
            case 2: {
                s s2 = arrs[n3 * 289 + r2.d * 17 + r2.f];
                s s3 = arrs[n3 * 289 + r2.d * 17 + r2.e];
                s s4 = arrs[n3 * 289 + r2.c * 17 + r2.f];
                s s5 = arrs[n3 * 289 + r2.c * 17 + r2.e];
                return s2.d - s3.d - s4.d + s5.d;
            }
            case 1: {
                s s6 = arrs[r2.b * 289 + n3 * 17 + r2.f];
                s s7 = arrs[r2.b * 289 + n3 * 17 + r2.e];
                s s8 = arrs[r2.a * 289 + n3 * 17 + r2.f];
                s s9 = arrs[r2.a * 289 + n3 * 17 + r2.e];
                return s6.d - s7.d - s8.d + s9.d;
            }
            case 0: {
                s s10 = arrs[r2.b * 289 + r2.d * 17 + n3];
                s s11 = arrs[r2.b * 289 + r2.c * 17 + n3];
                s s12 = arrs[r2.a * 289 + r2.d * 17 + n3];
                s s13 = arrs[r2.a * 289 + r2.c * 17 + n3];
                return s10.d - s11.d - s12.d + s13.d;
            }
        }
        return 1L;
    }

    private static final float i(r r2, s[] arrs) {
        long l2 = q.e(r2, arrs);
        long l3 = q.f(r2, arrs);
        long l4 = q.g(r2, arrs);
        long l5 = q.h(r2, arrs);
        float f2 = arrs[r2.b * 289 + r2.d * 17 + r2.f].e - arrs[r2.b * 289 + r2.d * 17 + r2.e].e - arrs[r2.b * 289 + r2.c * 17 + r2.f].e + arrs[r2.b * 289 + r2.c * 17 + r2.e].e - arrs[r2.a * 289 + r2.d * 17 + r2.f].e + arrs[r2.a * 289 + r2.d * 17 + r2.e].e + arrs[r2.a * 289 + r2.c * 17 + r2.f].e - arrs[r2.a * 289 + r2.c * 17 + r2.e].e;
        return f2 - (float)(l2 * l2 + l4 * l4 + l3 * l3) / (float)l5;
    }

    private static final float b(r r2, int n2, int n3, int n4, int[] arrn, long l2, long l3, long l4, long l5, s[] arrs) {
        long l6 = q.a(r2, n2, arrs);
        long l7 = q.b(r2, n2, arrs);
        long l8 = q.c(r2, n2, arrs);
        long l9 = q.d(r2, n2, arrs);
        float f2 = 0.0f;
        float f3 = 0.0f;
        arrn[0] = -1;
        for (int i2 = n3; i2 < n4; ++i2) {
            long l10 = l6 + q.a(r2, n2, i2, arrs);
            long l11 = l7 + q.b(r2, n2, i2, arrs);
            long l12 = l8 + q.c(r2, n2, i2, arrs);
            long l13 = l9 + q.d(r2, n2, i2, arrs);
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

    private static final int a(r r2, r r3, s[] arrs) {
        int n2;
        int[] arrn = new int[1];
        int[] arrn2 = new int[1];
        int[] arrn3 = new int[1];
        long l2 = q.e(r2, arrs);
        long l3 = q.f(r2, arrs);
        long l4 = q.g(r2, arrs);
        long l5 = q.h(r2, arrs);
        float f2 = q.b(r2, 2, r2.a + 1, r2.b, arrn, l2, l3, l4, l5, arrs);
        float f3 = q.b(r2, 1, r2.c + 1, r2.d, arrn2, l2, l3, l4, l5, arrs);
        float f4 = q.b(r2, 0, r2.e + 1, r2.f, arrn3, l2, l3, l4, l5, arrs);
        if (f2 >= f3 && f2 >= f3) {
            n2 = 2;
            if (arrn[0] < 0) {
                return 0;
            }
        } else {
            n2 = f3 >= f2 && f3 >= f4 ? 1 : 0;
        }
        r3.b = r2.b;
        r3.d = r2.d;
        r3.f = r2.f;
        switch (n2) {
            case 2: {
                r3.a = r2.b = arrn[0];
                r3.c = r2.c;
                r3.e = r2.e;
                break;
            }
            case 1: {
                r3.a = r2.a;
                r3.c = r2.d = arrn2[0];
                r3.e = r2.e;
                break;
            }
            case 0: {
                r3.a = r2.a;
                r3.c = r2.c;
                r3.e = r2.f = arrn3[0];
            }
        }
        r2.g = (r2.b - r2.a) * (r2.d - r2.c) * (r2.f - r2.e);
        r3.g = (r3.b - r3.a) * (r3.d - r3.c) * (r3.f - r3.e);
        return 1;
    }

    private static final void a(r r2, int n2, int[] arrn) {
        for (int i2 = r2.a + 1; i2 <= r2.b; ++i2) {
            for (int i3 = r2.c + 1; i3 <= r2.d; ++i3) {
                for (int i4 = r2.e + 1; i4 <= r2.f; ++i4) {
                    arrn[i2 * 289 + i3 * 17 + i4] = n2;
                }
            }
        }
    }

    static long a(r r2, s[] arrs) {
        return q.e(r2, arrs);
    }

    static long b(r r2, s[] arrs) {
        return q.f(r2, arrs);
    }

    static long c(r r2, s[] arrs) {
        return q.g(r2, arrs);
    }

    static long d(r r2, s[] arrs) {
        return q.h(r2, arrs);
    }

    static float a(r r2, int n2, int n3, int n4, int[] arrn, long l2, long l3, long l4, long l5, s[] arrs) {
        return q.b(r2, n2, n3, n4, arrn, l2, l3, l4, l5, arrs);
    }

    static {
        int n2;
        a = new float[256];
        b = new int[257];
        q.b[2] = 2;
        q.b[1] = 2;
        q.b[0] = 2;
        q.b[4] = 4;
        q.b[3] = 4;
        q.b[8] = 8;
        q.b[7] = 8;
        q.b[6] = 8;
        q.b[5] = 8;
        for (n2 = 9; n2 <= 16; ++n2) {
            q.b[n2] = 16;
        }
        for (n2 = 17; n2 <= 32; ++n2) {
            q.b[n2] = 32;
        }
        for (n2 = 33; n2 <= 64; ++n2) {
            q.b[n2] = 64;
        }
        for (n2 = 65; n2 <= 128; ++n2) {
            q.b[n2] = 128;
        }
        for (n2 = 129; n2 <= 256; ++n2) {
            q.b[n2] = 256;
        }
        for (n2 = 0; n2 < 256; ++n2) {
            q.a[n2] = n2 * n2;
        }
    }
}

