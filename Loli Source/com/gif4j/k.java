/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j;

import com.gif4j.GifFrame;
import com.gif4j.l;
import java.util.Random;

class k {
    int[] a;
    int[] b;
    int[] c;
    int[] d;
    int[] e;
    int[] f;
    byte[] g;
    byte[] h;
    byte[] i;
    byte[] j;
    boolean k;
    int l;
    int m;
    GifFrame n;
    l o;

    public k(byte[] arrby, byte[] arrby2, byte[] arrby3, byte[] arrby4, int n2, GifFrame gifFrame) {
        this.g = arrby;
        this.h = arrby2;
        this.i = arrby3;
        this.l = n2;
        this.n = gifFrame;
        this.m = gifFrame.d;
        this.j = arrby4;
        this.a();
    }

    void a() {
        this.a = new int[this.m + 2];
        this.b = new int[this.m + 2];
        this.c = new int[this.m + 2];
        this.d = new int[this.m + 2];
        this.e = new int[this.m + 2];
        this.f = new int[this.m + 2];
        Random random = new Random();
        for (int i2 = 0; i2 < this.m + 2; ++i2) {
            this.a[i2] = random.nextInt(3) - 2;
            this.c[i2] = random.nextInt(3) - 2;
            this.e[i2] = random.nextInt(3) - 2;
        }
        this.k = true;
        this.o = new l(this.g, this.h, this.i, this.l);
    }

    public void b() {
        byte[] arrby = new byte[this.m];
        byte[] arrby2 = new byte[this.m];
        for (int i2 = 0; i2 < this.n.e; ++i2) {
            System.arraycopy(this.n.n, i2 * this.m, arrby2, 0, this.m);
            this.a(arrby2, arrby);
            System.arraycopy(arrby, 0, this.n.n, i2 * this.m, this.m);
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void a(byte[] var1_1, byte[] var2_2) {
        var3_3 = this.b.length;
        while (--var3_3 >= 0) {
            this.f[var3_3] = 0;
            this.d[var3_3] = 0;
            this.b[var3_3] = 0;
        }
        if (this.k) {
            var5_4 = 0;
            var3_3 = 0;
            var4_5 = this.m;
        } else {
            var5_4 = this.m - 1;
            var3_3 = this.m - 1;
            var4_5 = -1;
        }
        do lbl-1000:
        // 5 sources

        {
            block33: {
                block31: {
                    block32: {
                        if ((var6_6 = var1_1[var3_3] & 255) != this.n.r) break block31;
                        if (!this.k) break block32;
                        var2_2[var5_4++] = (byte)this.l;
                        if (++var3_3 < var4_5) ** GOTO lbl-1000
                        break;
                    }
                    var2_2[var5_4--] = (byte)this.l;
                    if (--var3_3 > var4_5) ** GOTO lbl-1000
                    break;
                }
                var7_8 = this.n.k[var6_6] & 255;
                var8_9 = this.n.l[var6_6] & 255;
                var9_10 = this.n.m[var6_6] & 255;
                if (var7_8 < 0) {
                    var7_8 = 0;
                } else if (var7_8 > 255) {
                    var7_8 = 255;
                }
                if (var8_9 < 0) {
                    var8_9 = 0;
                } else if (var8_9 > 255) {
                    var8_9 = 255;
                }
                if (var9_10 < 0) {
                    var9_10 = 0;
                } else if (var9_10 > 255) {
                    var9_10 = 255;
                }
                var10_11 = this.o.a(var7_8, var8_9, var9_10);
                var11_12 = this.g[var10_11] & 255;
                var12_13 = this.h[var10_11] & 255;
                var13_14 = this.i[var10_11] & 255;
                var14_15 = 0;
                if (!this.k) break block33;
                var2_2[var5_4++] = (byte)var10_11;
                var14_15 = var7_8 - var11_12;
                if (var14_15 > 32) {
                    var14_15 = 32;
                } else if (var14_15 < -32) {
                    var14_15 = -32;
                }
                v0 = var3_3 + 2;
                this.a[v0] = this.a[v0] + (var14_15 * 7 >> 4);
                v1 = var3_3;
                this.b[v1] = this.b[v1] + (var14_15 >> 3);
                v2 = var3_3 + 1;
                this.b[v2] = this.b[v2] + (var14_15 >> 2);
                v3 = var3_3 + 2;
                this.b[v3] = this.b[v3] + (var14_15 >> 4);
                var14_15 = var8_9 - var12_13;
                if (var14_15 > 32) {
                    var14_15 = 32;
                } else if (var14_15 < -32) {
                    var14_15 = -32;
                }
                v4 = var3_3 + 2;
                this.c[v4] = this.c[v4] + (var14_15 * 3 >> 3);
                v5 = var3_3;
                this.d[v5] = this.d[v5] + (var14_15 * 3 >> 5);
                v6 = var3_3 + 1;
                this.d[v6] = this.d[v6] + (var14_15 * 3 >> 4);
                v7 = var3_3 + 2;
                this.d[v7] = this.d[v7] + (var14_15 * 3 >> 6);
                var14_15 = var9_10 - var13_14;
                if (var14_15 > 32) {
                    var14_15 = 32;
                } else if (var14_15 < -32) {
                    var14_15 = -32;
                }
                v8 = var3_3 + 2;
                this.e[v8] = this.e[v8] + (var14_15 * 7 >> 4);
                v9 = var3_3;
                this.f[v9] = this.f[v9] + (var14_15 >> 3);
                v10 = var3_3 + 1;
                this.f[v10] = this.f[v10] + (var14_15 >> 2);
                v11 = var3_3 + 2;
                this.f[v11] = this.f[v11] + (var14_15 >> 4);
                if (++var3_3 < var4_5) ** GOTO lbl-1000
                break;
            }
            var2_2[var5_4--] = (byte)var10_11;
            var14_15 = var7_8 - var11_12;
            if (var14_15 > 32) {
                var14_15 = 32;
            } else if (var14_15 < -32) {
                var14_15 = -32;
            }
            v12 = var3_3;
            this.a[v12] = this.a[v12] + (var14_15 * 7 >> 4);
            v13 = var3_3 + 2;
            this.b[v13] = this.b[v13] + (var14_15 >> 3);
            v14 = var3_3 + 1;
            this.b[v14] = this.b[v14] + (var14_15 >> 2);
            v15 = var3_3;
            this.b[v15] = this.b[v15] + (var14_15 >> 4);
            var14_15 = var8_9 - var12_13;
            if (var14_15 > 32) {
                var14_15 = 32;
            } else if (var14_15 < -32) {
                var14_15 = -32;
            }
            v16 = var3_3;
            this.c[v16] = this.c[v16] + (var14_15 * 3 >> 3);
            v17 = var3_3 + 2;
            this.d[v17] = this.d[v17] + (var14_15 * 3 >> 5);
            v18 = var3_3 + 1;
            this.d[v18] = this.d[v18] + (var14_15 * 3 >> 4);
            v19 = var3_3;
            this.d[v19] = this.d[v19] + (var14_15 * 3 >> 6);
            var14_15 = var9_10 - var13_14;
            if (var14_15 > 32) {
                var14_15 = 32;
            } else if (var14_15 < -32) {
                var14_15 = -32;
            }
            v20 = var3_3;
            this.e[v20] = this.e[v20] + (var14_15 * 7 >> 4);
            v21 = var3_3 + 2;
            this.f[v21] = this.f[v21] + (var14_15 >> 3);
            v22 = var3_3 + 1;
            this.f[v22] = this.f[v22] + (var14_15 >> 2);
            v23 = var3_3--;
            this.f[v23] = this.f[v23] + (var14_15 >> 4);
        } while (var3_3 > var4_5);
        var6_7 = this.a;
        this.a = this.b;
        this.b = var6_7;
        var6_7 = this.c;
        this.c = this.d;
        this.d = var6_7;
        var6_7 = this.e;
        this.e = this.f;
        this.f = var6_7;
        this.k = this.k == false;
    }
}

