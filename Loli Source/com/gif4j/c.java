/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j;

class c {
    String a = null;

    c(String string) {
        if (string == null) {
            throw new IllegalArgumentException("Comment String is null");
        }
        this.a = string;
    }

    byte[] a() {
        int n2;
        int n3 = this.a.length();
        byte[] arrby = new byte[n3];
        for (n2 = 0; n2 < n3; ++n2) {
            arrby[n2] = (byte)this.a.charAt(n2);
        }
        n2 = n3 / 255;
        int n4 = n3 % 255;
        byte[] arrby2 = new byte[n3 + n2 + (n4 > 0 ? 4 : 3)];
        arrby2[0] = 33;
        arrby2[1] = -2;
        int n5 = 2;
        int n6 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            arrby2[n5] = -1;
            System.arraycopy(arrby, n6, arrby2, ++n5, 255);
            n5 += 255;
            n6 += 255;
        }
        if (n4 > 0) {
            arrby2[n5] = (byte)n4;
            System.arraycopy(arrby, n6, arrby2, ++n5, n4);
            n5 += n4;
        }
        arrby2[n5] = 0;
        return arrby2;
    }
}

