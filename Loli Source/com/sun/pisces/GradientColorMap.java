/*
 * Decompiled with CFR 0.150.
 */
package com.sun.pisces;

public final class GradientColorMap {
    public static final int CYCLE_NONE = 0;
    public static final int CYCLE_REPEAT = 1;
    public static final int CYCLE_REFLECT = 2;
    int cycleMethod;
    private static final int LG_RAMP_SIZE = 8;
    private static final int RAMP_SIZE = 256;
    int[] fractions = null;
    int[] rgba = null;
    int[] colors = null;

    GradientColorMap(int[] arrn, int[] arrn2, int n2) {
        int[] arrn3;
        int[] arrn4;
        this.cycleMethod = n2;
        int n3 = arrn.length;
        if (arrn[0] != 0) {
            arrn4 = new int[n3 + 1];
            arrn3 = new int[n3 + 1];
            System.arraycopy(arrn, 0, arrn4, 1, n3);
            System.arraycopy(arrn2, 0, arrn3, 1, n3);
            arrn4[0] = 0;
            arrn3[0] = arrn2[0];
            arrn = arrn4;
            arrn2 = arrn3;
            ++n3;
        }
        if (arrn[n3 - 1] != 65536) {
            arrn4 = new int[n3 + 1];
            arrn3 = new int[n3 + 1];
            System.arraycopy(arrn, 0, arrn4, 0, n3);
            System.arraycopy(arrn2, 0, arrn3, 0, n3);
            arrn4[n3] = 65536;
            arrn3[n3] = arrn2[n3 - 1];
            arrn = arrn4;
            arrn2 = arrn3;
        }
        this.fractions = new int[arrn.length];
        System.arraycopy(arrn, 0, this.fractions, 0, arrn.length);
        this.rgba = new int[arrn2.length];
        System.arraycopy(arrn2, 0, this.rgba, 0, arrn2.length);
        this.createRamp();
    }

    private int pad(int n2) {
        switch (this.cycleMethod) {
            case 0: {
                if (n2 < 0) {
                    return 0;
                }
                if (n2 > 65535) {
                    return 65535;
                }
                return n2;
            }
            case 1: {
                return n2 & 0xFFFF;
            }
            case 2: {
                if (n2 < 0) {
                    n2 = -n2;
                }
                if ((n2 &= 0x1FFFF) > 65535) {
                    n2 = 131071 - n2;
                }
                return n2;
            }
        }
        throw new RuntimeException("Unknown cycle method: " + this.cycleMethod);
    }

    private int findStop(int n2) {
        int n3 = this.fractions.length;
        for (int i2 = 1; i2 < n3; ++i2) {
            if (this.fractions[i2] <= n2) continue;
            return i2;
        }
        return 1;
    }

    private void accumColor(int n2, int[] arrn, int[] arrn2, int[] arrn3, int[] arrn4, int[] arrn5, int[] arrn6, int[] arrn7, int[] arrn8) {
        int n3 = this.findStop(n2);
        int n4 = this.fractions[n3] - this.fractions[n3 - 1];
        arrn5[0] = arrn5[0] + (arrn[n3 - 1] + (n2 -= this.fractions[n3 - 1]) * (arrn[n3] - arrn[n3 - 1]) / n4);
        arrn6[0] = arrn6[0] + (arrn2[n3 - 1] + n2 * (arrn2[n3] - arrn2[n3 - 1]) / n4);
        arrn7[0] = arrn7[0] + (arrn3[n3 - 1] + n2 * (arrn3[n3] - arrn3[n3 - 1]) / n4);
        arrn8[0] = arrn8[0] + (arrn4[n3 - 1] + n2 * (arrn4[n3] - arrn4[n3 - 1]) / n4);
    }

    private int getColorAA(int n2, int[] arrn, int[] arrn2, int[] arrn3, int[] arrn4, int[] arrn5, int[] arrn6, int[] arrn7, int[] arrn8) {
        int n3;
        int n4 = this.findStop(n2);
        if (this.fractions[n4 - 1] < this.pad(n2 - (n3 = 192)) && this.pad(n2 + n3) < this.fractions[n4]) {
            n3 = 0;
        }
        int n5 = 64;
        int n6 = 0;
        for (int i2 = -n3; i2 <= n3; i2 += n5) {
            int n7 = this.pad(n2 + i2);
            this.accumColor(n7, arrn, arrn2, arrn3, arrn4, arrn5, arrn6, arrn7, arrn8);
            ++n6;
        }
        arrn8[0] = arrn8[0] / n6;
        arrn5[0] = arrn5[0] / n6;
        arrn6[0] = arrn6[0] / n6;
        arrn7[0] = arrn7[0] / n6;
        return arrn8[0] << 24 | arrn5[0] << 16 | arrn6[0] << 8 | arrn7[0];
    }

    private void createRamp() {
        int n2;
        this.colors = new int[256];
        int[] arrn = new int[1];
        int[] arrn2 = new int[1];
        int[] arrn3 = new int[1];
        int[] arrn4 = new int[1];
        int n3 = this.fractions.length;
        int[] arrn5 = new int[n3];
        int[] arrn6 = new int[n3];
        int[] arrn7 = new int[n3];
        int[] arrn8 = new int[n3];
        for (n2 = 0; n2 < n3; ++n2) {
            arrn5[n2] = this.rgba[n2] >> 24 & 0xFF;
            arrn6[n2] = this.rgba[n2] >> 16 & 0xFF;
            arrn7[n2] = this.rgba[n2] >> 8 & 0xFF;
            arrn8[n2] = this.rgba[n2] & 0xFF;
        }
        n2 = 255;
        int n4 = 8;
        this.colors[0] = this.rgba[0];
        this.colors[n2] = this.rgba[n3 - 1];
        for (int i2 = 1; i2 < n2; ++i2) {
            arrn[0] = 0;
            arrn4[0] = 0;
            arrn3[0] = 0;
            arrn2[0] = 0;
            this.colors[i2] = this.getColorAA(i2 << n4, arrn6, arrn7, arrn8, arrn5, arrn2, arrn3, arrn4, arrn);
        }
    }
}

