/*
 * Decompiled with CFR 0.150.
 */
package com.sun.openpisces;

import java.util.Arrays;

final class Helpers {
    private Helpers() {
        throw new Error("This is a non instantiable class");
    }

    static boolean within(float f2, float f3, float f4) {
        float f5 = f3 - f2;
        return f5 <= f4 && f5 >= -f4;
    }

    static boolean within(double d2, double d3, double d4) {
        double d5 = d3 - d2;
        return d5 <= d4 && d5 >= -d4;
    }

    static int quadraticRoots(float f2, float f3, float f4, float[] arrf, int n2) {
        int n3 = n2;
        if (f2 != 0.0f) {
            float f5 = f3 * f3 - 4.0f * f2 * f4;
            if (f5 > 0.0f) {
                float f6 = (float)Math.sqrt(f5);
                if (f3 >= 0.0f) {
                    arrf[n3++] = 2.0f * f4 / (-f3 - f6);
                    arrf[n3++] = (-f3 - f6) / (2.0f * f2);
                } else {
                    arrf[n3++] = (-f3 + f6) / (2.0f * f2);
                    arrf[n3++] = 2.0f * f4 / (-f3 + f6);
                }
            } else if (f5 == 0.0f) {
                float f7 = -f3 / (2.0f * f2);
                arrf[n3++] = f7;
            }
        } else if (f3 != 0.0f) {
            float f8 = -f4 / f3;
            arrf[n3++] = f8;
        }
        return n3 - n2;
    }

    static int cubicRootsInAB(float f2, float f3, float f4, float f5, float[] arrf, int n2, float f6, float f7) {
        int n3;
        double d2;
        double d3;
        double d4;
        double d5;
        double d6;
        double d7;
        double d8;
        if (f2 == 0.0f) {
            int n4 = Helpers.quadraticRoots(f3, f4, f5, arrf, n2);
            return Helpers.filterOutNotInAB(arrf, n2, n4, f6, f7) - n2;
        }
        if ((d8 = (d7 = 0.5 * (0.07407407407407407 * (double)(f3 /= f2) * (d6 = (double)(f3 * f3)) - 0.3333333333333333 * (double)f3 * (double)(f4 /= f2) + (double)(f5 /= f2))) * d7 + (d5 = (d4 = 0.3333333333333333 * (-0.3333333333333333 * d6 + (double)f4)) * d4 * d4)) < 0.0) {
            d3 = 0.3333333333333333 * Math.acos(-d7 / Math.sqrt(-d5));
            d2 = 2.0 * Math.sqrt(-d4);
            arrf[n2 + 0] = (float)(d2 * Math.cos(d3));
            arrf[n2 + 1] = (float)(-d2 * Math.cos(d3 + 1.0471975511965976));
            arrf[n2 + 2] = (float)(-d2 * Math.cos(d3 - 1.0471975511965976));
            n3 = 3;
        } else {
            d3 = Math.sqrt(d8);
            d2 = Math.cbrt(d3 - d7);
            double d9 = -Math.cbrt(d3 + d7);
            arrf[n2] = (float)(d2 + d9);
            n3 = 1;
            if (Helpers.within(d8, 0.0, 1.0E-8)) {
                arrf[n2 + 1] = -(arrf[n2] / 2.0f);
                n3 = 2;
            }
        }
        float f8 = 0.33333334f * f3;
        for (int i2 = 0; i2 < n3; ++i2) {
            int n5 = n2 + i2;
            arrf[n5] = arrf[n5] - f8;
        }
        return Helpers.filterOutNotInAB(arrf, n2, n3, f6, f7) - n2;
    }

    static float[] widenArray(float[] arrf, int n2, int n3) {
        if (arrf.length >= n2 + n3) {
            return arrf;
        }
        return Arrays.copyOf(arrf, 2 * (n2 + n3));
    }

    static int[] widenArray(int[] arrn, int n2, int n3) {
        if (arrn.length >= n2 + n3) {
            return arrn;
        }
        return Arrays.copyOf(arrn, 2 * (n2 + n3));
    }

    static float evalCubic(float f2, float f3, float f4, float f5, float f6) {
        return f6 * (f6 * (f6 * f2 + f3) + f4) + f5;
    }

    static float evalQuad(float f2, float f3, float f4, float f5) {
        return f5 * (f5 * f2 + f3) + f4;
    }

    static int filterOutNotInAB(float[] arrf, int n2, int n3, float f2, float f3) {
        int n4 = n2;
        for (int i2 = n2; i2 < n2 + n3; ++i2) {
            if (!(arrf[i2] >= f2) || !(arrf[i2] < f3)) continue;
            arrf[n4++] = arrf[i2];
        }
        return n4;
    }

    static float polyLineLength(float[] arrf, int n2, int n3) {
        assert (n3 % 2 == 0 && arrf.length >= n2 + n3) : "";
        float f2 = 0.0f;
        for (int i2 = n2 + 2; i2 < n2 + n3; i2 += 2) {
            f2 += Helpers.linelen(arrf[i2], arrf[i2 + 1], arrf[i2 - 2], arrf[i2 - 1]);
        }
        return f2;
    }

    static float linelen(float f2, float f3, float f4, float f5) {
        float f6 = f4 - f2;
        float f7 = f5 - f3;
        return (float)Math.sqrt(f6 * f6 + f7 * f7);
    }

    static void subdivide(float[] arrf, int n2, float[] arrf2, int n3, float[] arrf3, int n4, int n5) {
        switch (n5) {
            case 6: {
                Helpers.subdivideQuad(arrf, n2, arrf2, n3, arrf3, n4);
                break;
            }
            case 8: {
                Helpers.subdivideCubic(arrf, n2, arrf2, n3, arrf3, n4);
                break;
            }
            default: {
                throw new InternalError("Unsupported curve type");
            }
        }
    }

    static void isort(float[] arrf, int n2, int n3) {
        for (int i2 = n2 + 1; i2 < n2 + n3; ++i2) {
            float f2 = arrf[i2];
            for (int i3 = i2 - 1; i3 >= n2 && arrf[i3] > f2; --i3) {
                arrf[i3 + 1] = arrf[i3];
            }
            arrf[i3 + 1] = f2;
        }
    }

    static void subdivideCubic(float[] arrf, int n2, float[] arrf2, int n3, float[] arrf3, int n4) {
        float f2 = arrf[n2 + 0];
        float f3 = arrf[n2 + 1];
        float f4 = arrf[n2 + 2];
        float f5 = arrf[n2 + 3];
        float f6 = arrf[n2 + 4];
        float f7 = arrf[n2 + 5];
        float f8 = arrf[n2 + 6];
        float f9 = arrf[n2 + 7];
        if (arrf2 != null) {
            arrf2[n3 + 0] = f2;
            arrf2[n3 + 1] = f3;
        }
        if (arrf3 != null) {
            arrf3[n4 + 6] = f8;
            arrf3[n4 + 7] = f9;
        }
        f2 = (f2 + f4) / 2.0f;
        f3 = (f3 + f5) / 2.0f;
        f8 = (f8 + f6) / 2.0f;
        f9 = (f9 + f7) / 2.0f;
        float f10 = (f4 + f6) / 2.0f;
        float f11 = (f5 + f7) / 2.0f;
        f4 = (f2 + f10) / 2.0f;
        f5 = (f3 + f11) / 2.0f;
        f6 = (f8 + f10) / 2.0f;
        f7 = (f9 + f11) / 2.0f;
        f10 = (f4 + f6) / 2.0f;
        f11 = (f5 + f7) / 2.0f;
        if (arrf2 != null) {
            arrf2[n3 + 2] = f2;
            arrf2[n3 + 3] = f3;
            arrf2[n3 + 4] = f4;
            arrf2[n3 + 5] = f5;
            arrf2[n3 + 6] = f10;
            arrf2[n3 + 7] = f11;
        }
        if (arrf3 != null) {
            arrf3[n4 + 0] = f10;
            arrf3[n4 + 1] = f11;
            arrf3[n4 + 2] = f6;
            arrf3[n4 + 3] = f7;
            arrf3[n4 + 4] = f8;
            arrf3[n4 + 5] = f9;
        }
    }

    static void subdivideCubicAt(float f2, float[] arrf, int n2, float[] arrf2, int n3, float[] arrf3, int n4) {
        float f3 = arrf[n2 + 0];
        float f4 = arrf[n2 + 1];
        float f5 = arrf[n2 + 2];
        float f6 = arrf[n2 + 3];
        float f7 = arrf[n2 + 4];
        float f8 = arrf[n2 + 5];
        float f9 = arrf[n2 + 6];
        float f10 = arrf[n2 + 7];
        if (arrf2 != null) {
            arrf2[n3 + 0] = f3;
            arrf2[n3 + 1] = f4;
        }
        if (arrf3 != null) {
            arrf3[n4 + 6] = f9;
            arrf3[n4 + 7] = f10;
        }
        f3 += f2 * (f5 - f3);
        f4 += f2 * (f6 - f4);
        f9 = f7 + f2 * (f9 - f7);
        f10 = f8 + f2 * (f10 - f8);
        float f11 = f5 + f2 * (f7 - f5);
        float f12 = f6 + f2 * (f8 - f6);
        f5 = f3 + f2 * (f11 - f3);
        f6 = f4 + f2 * (f12 - f4);
        f7 = f11 + f2 * (f9 - f11);
        f8 = f12 + f2 * (f10 - f12);
        f11 = f5 + f2 * (f7 - f5);
        f12 = f6 + f2 * (f8 - f6);
        if (arrf2 != null) {
            arrf2[n3 + 2] = f3;
            arrf2[n3 + 3] = f4;
            arrf2[n3 + 4] = f5;
            arrf2[n3 + 5] = f6;
            arrf2[n3 + 6] = f11;
            arrf2[n3 + 7] = f12;
        }
        if (arrf3 != null) {
            arrf3[n4 + 0] = f11;
            arrf3[n4 + 1] = f12;
            arrf3[n4 + 2] = f7;
            arrf3[n4 + 3] = f8;
            arrf3[n4 + 4] = f9;
            arrf3[n4 + 5] = f10;
        }
    }

    static void subdivideQuad(float[] arrf, int n2, float[] arrf2, int n3, float[] arrf3, int n4) {
        float f2 = arrf[n2 + 0];
        float f3 = arrf[n2 + 1];
        float f4 = arrf[n2 + 2];
        float f5 = arrf[n2 + 3];
        float f6 = arrf[n2 + 4];
        float f7 = arrf[n2 + 5];
        if (arrf2 != null) {
            arrf2[n3 + 0] = f2;
            arrf2[n3 + 1] = f3;
        }
        if (arrf3 != null) {
            arrf3[n4 + 4] = f6;
            arrf3[n4 + 5] = f7;
        }
        f2 = (f2 + f4) / 2.0f;
        f3 = (f3 + f5) / 2.0f;
        f6 = (f6 + f4) / 2.0f;
        f7 = (f7 + f5) / 2.0f;
        f4 = (f2 + f6) / 2.0f;
        f5 = (f3 + f7) / 2.0f;
        if (arrf2 != null) {
            arrf2[n3 + 2] = f2;
            arrf2[n3 + 3] = f3;
            arrf2[n3 + 4] = f4;
            arrf2[n3 + 5] = f5;
        }
        if (arrf3 != null) {
            arrf3[n4 + 0] = f4;
            arrf3[n4 + 1] = f5;
            arrf3[n4 + 2] = f6;
            arrf3[n4 + 3] = f7;
        }
    }

    static void subdivideQuadAt(float f2, float[] arrf, int n2, float[] arrf2, int n3, float[] arrf3, int n4) {
        float f3 = arrf[n2 + 0];
        float f4 = arrf[n2 + 1];
        float f5 = arrf[n2 + 2];
        float f6 = arrf[n2 + 3];
        float f7 = arrf[n2 + 4];
        float f8 = arrf[n2 + 5];
        if (arrf2 != null) {
            arrf2[n3 + 0] = f3;
            arrf2[n3 + 1] = f4;
        }
        if (arrf3 != null) {
            arrf3[n4 + 4] = f7;
            arrf3[n4 + 5] = f8;
        }
        f3 += f2 * (f5 - f3);
        f4 += f2 * (f6 - f4);
        f7 = f5 + f2 * (f7 - f5);
        f8 = f6 + f2 * (f8 - f6);
        f5 = f3 + f2 * (f7 - f3);
        f6 = f4 + f2 * (f8 - f4);
        if (arrf2 != null) {
            arrf2[n3 + 2] = f3;
            arrf2[n3 + 3] = f4;
            arrf2[n3 + 4] = f5;
            arrf2[n3 + 5] = f6;
        }
        if (arrf3 != null) {
            arrf3[n4 + 0] = f5;
            arrf3[n4 + 1] = f6;
            arrf3[n4 + 2] = f7;
            arrf3[n4 + 3] = f8;
        }
    }

    static void subdivideAt(float f2, float[] arrf, int n2, float[] arrf2, int n3, float[] arrf3, int n4, int n5) {
        switch (n5) {
            case 8: {
                Helpers.subdivideCubicAt(f2, arrf, n2, arrf2, n3, arrf3, n4);
                break;
            }
            case 6: {
                Helpers.subdivideQuadAt(f2, arrf, n2, arrf2, n3, arrf3, n4);
            }
        }
    }
}

