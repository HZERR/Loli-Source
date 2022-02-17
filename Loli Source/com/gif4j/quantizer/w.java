/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j.quantizer;

class w {
    byte[] a;
    byte[] b;
    byte[] c;
    byte[] d;
    int e;

    public w(byte[] arrby, byte[] arrby2, byte[] arrby3) {
        this.a = arrby;
        this.b = arrby2;
        this.c = arrby3;
        this.e = arrby.length;
        this.d = new byte[32768];
        this.a();
    }

    public w(byte[] arrby, byte[] arrby2, byte[] arrby3, int n2) {
        this.a = arrby;
        this.b = arrby2;
        this.c = arrby3;
        this.e = n2;
        this.d = new byte[32768];
        this.a();
    }

    private void a() {
        int n2 = 8;
        int n3 = 64;
        int n4 = n3 + n3;
        int[] arrn = new int[32768];
        for (int i2 = 0; i2 < this.e; ++i2) {
            int n5 = this.a[i2] & 0xFF;
            int n6 = this.b[i2] & 0xFF;
            int n7 = this.c[i2] & 0xFF;
            int n8 = n5 - n2 / 2;
            int n9 = n6 - n2 / 2;
            int n10 = n7 - n2 / 2;
            n8 = n8 * n8 + n9 * n9 + n10 * n10;
            int n11 = 2 * (n3 - (n5 << 3));
            int n12 = 2 * (n3 - (n6 << 3));
            int n13 = 2 * (n3 - (n7 << 3));
            int n14 = 0;
            int n15 = 0;
            int n16 = n11;
            while (n15 < 32) {
                int n17 = 0;
                n9 = n8;
                int n18 = n12;
                while (n17 < 32) {
                    int n19 = 0;
                    n10 = n9;
                    int n20 = n13;
                    while (n19 < 32) {
                        if (i2 == 0 || arrn[n14] > n10) {
                            arrn[n14] = n10;
                            this.d[n14] = (byte)i2;
                        }
                        n10 += n20;
                        ++n19;
                        ++n14;
                        n20 += n4;
                    }
                    n9 += n18;
                    ++n17;
                    n18 += n4;
                }
                n8 += n16;
                ++n15;
                n16 += n4;
            }
        }
    }

    public final byte a(int n2) {
        return this.d[(n2 >> 9 & 0x7C00) + (n2 >> 6 & 0x3E0) + (n2 >> 3 & 0x1F)];
    }
}

