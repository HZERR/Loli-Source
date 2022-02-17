/*
 * Decompiled with CFR 0.150.
 */
package com.sun.openpisces;

import com.sun.openpisces.Helpers;
import java.util.Iterator;

final class Curve {
    float ax;
    float ay;
    float bx;
    float by;
    float cx;
    float cy;
    float dx;
    float dy;
    float dax;
    float day;
    float dbx;
    float dby;

    Curve() {
    }

    void set(float[] arrf, int n2) {
        switch (n2) {
            case 8: {
                this.set(arrf[0], arrf[1], arrf[2], arrf[3], arrf[4], arrf[5], arrf[6], arrf[7]);
                break;
            }
            case 6: {
                this.set(arrf[0], arrf[1], arrf[2], arrf[3], arrf[4], arrf[5]);
                break;
            }
            default: {
                throw new InternalError("Curves can only be cubic or quadratic");
            }
        }
    }

    void set(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        this.ax = 3.0f * (f4 - f6) + f8 - f2;
        this.ay = 3.0f * (f5 - f7) + f9 - f3;
        this.bx = 3.0f * (f2 - 2.0f * f4 + f6);
        this.by = 3.0f * (f3 - 2.0f * f5 + f7);
        this.cx = 3.0f * (f4 - f2);
        this.cy = 3.0f * (f5 - f3);
        this.dx = f2;
        this.dy = f3;
        this.dax = 3.0f * this.ax;
        this.day = 3.0f * this.ay;
        this.dbx = 2.0f * this.bx;
        this.dby = 2.0f * this.by;
    }

    void set(float f2, float f3, float f4, float f5, float f6, float f7) {
        this.ay = 0.0f;
        this.ax = 0.0f;
        this.bx = f2 - 2.0f * f4 + f6;
        this.by = f3 - 2.0f * f5 + f7;
        this.cx = 2.0f * (f4 - f2);
        this.cy = 2.0f * (f5 - f3);
        this.dx = f2;
        this.dy = f3;
        this.dax = 0.0f;
        this.day = 0.0f;
        this.dbx = 2.0f * this.bx;
        this.dby = 2.0f * this.by;
    }

    float xat(float f2) {
        return f2 * (f2 * (f2 * this.ax + this.bx) + this.cx) + this.dx;
    }

    float yat(float f2) {
        return f2 * (f2 * (f2 * this.ay + this.by) + this.cy) + this.dy;
    }

    float dxat(float f2) {
        return f2 * (f2 * this.dax + this.dbx) + this.cx;
    }

    float dyat(float f2) {
        return f2 * (f2 * this.day + this.dby) + this.cy;
    }

    int dxRoots(float[] arrf, int n2) {
        return Helpers.quadraticRoots(this.dax, this.dbx, this.cx, arrf, n2);
    }

    int dyRoots(float[] arrf, int n2) {
        return Helpers.quadraticRoots(this.day, this.dby, this.cy, arrf, n2);
    }

    int infPoints(float[] arrf, int n2) {
        float f2 = this.dax * this.dby - this.dbx * this.day;
        float f3 = 2.0f * (this.cy * this.dax - this.day * this.cx);
        float f4 = this.cy * this.dbx - this.cx * this.dby;
        return Helpers.quadraticRoots(f2, f3, f4, arrf, n2);
    }

    private int perpendiculardfddf(float[] arrf, int n2) {
        assert (arrf.length >= n2 + 4);
        float f2 = 2.0f * (this.dax * this.dax + this.day * this.day);
        float f3 = 3.0f * (this.dax * this.dbx + this.day * this.dby);
        float f4 = 2.0f * (this.dax * this.cx + this.day * this.cy) + this.dbx * this.dbx + this.dby * this.dby;
        float f5 = this.dbx * this.cx + this.dby * this.cy;
        return Helpers.cubicRootsInAB(f2, f3, f4, f5, arrf, n2, 0.0f, 1.0f);
    }

    int rootsOfROCMinusW(float[] arrf, int n2, float f2, float f3) {
        assert (n2 <= 6 && arrf.length >= 10);
        int n3 = n2;
        int n4 = this.perpendiculardfddf(arrf, n2);
        float f4 = 0.0f;
        float f5 = this.ROCsq(f4) - f2 * f2;
        arrf[n2 + n4] = 1.0f;
        for (int i2 = n2; i2 < n2 + ++n4; ++i2) {
            float f6 = arrf[i2];
            float f7 = this.ROCsq(f6) - f2 * f2;
            if (f5 == 0.0f) {
                arrf[n3++] = f4;
            } else if (f7 * f5 < 0.0f) {
                arrf[n3++] = this.falsePositionROCsqMinusX(f4, f6, f2 * f2, f3);
            }
            f4 = f6;
            f5 = f7;
        }
        return n3 - n2;
    }

    private static float eliminateInf(float f2) {
        return f2 == Float.POSITIVE_INFINITY ? Float.MAX_VALUE : (f2 == Float.NEGATIVE_INFINITY ? Float.MIN_VALUE : f2);
    }

    private float falsePositionROCsqMinusX(float f2, float f3, float f4, float f5) {
        int n2 = 0;
        float f6 = f3;
        float f7 = Curve.eliminateInf(this.ROCsq(f6) - f4);
        float f8 = f2;
        float f9 = Curve.eliminateInf(this.ROCsq(f8) - f4);
        float f10 = f8;
        for (int i2 = 0; i2 < 100 && Math.abs(f6 - f8) > f5 * Math.abs(f6 + f8); ++i2) {
            f10 = (f9 * f6 - f7 * f8) / (f9 - f7);
            float f11 = this.ROCsq(f10) - f4;
            if (Curve.sameSign(f11, f7)) {
                f7 = f11;
                f6 = f10;
                if (n2 < 0) {
                    f9 /= (float)(1 << -n2);
                    --n2;
                    continue;
                }
                n2 = -1;
                continue;
            }
            if (!(f11 * f9 > 0.0f)) break;
            f9 = f11;
            f8 = f10;
            if (n2 > 0) {
                f7 /= (float)(1 << n2);
                ++n2;
                continue;
            }
            n2 = 1;
        }
        return f10;
    }

    private static boolean sameSign(double d2, double d3) {
        return d2 < 0.0 && d3 < 0.0 || d2 > 0.0 && d3 > 0.0;
    }

    private float ROCsq(float f2) {
        float f3 = f2 * (f2 * this.dax + this.dbx) + this.cx;
        float f4 = f2 * (f2 * this.day + this.dby) + this.cy;
        float f5 = 2.0f * this.dax * f2 + this.dbx;
        float f6 = 2.0f * this.day * f2 + this.dby;
        float f7 = f3 * f3 + f4 * f4;
        float f8 = f5 * f5 + f6 * f6;
        float f9 = f5 * f3 + f6 * f4;
        return f7 * (f7 * f7 / (f7 * f8 - f9 * f9));
    }

    static Iterator<Integer> breakPtsAtTs(final float[] arrf, final int n2, final float[] arrf2, final int n3) {
        assert (arrf.length >= 2 * n2 && n3 <= arrf2.length);
        return new Iterator<Integer>(){
            final Integer i0 = 0;
            final Integer itype = n2;
            int nextCurveIdx = 0;
            Integer curCurveOff = this.i0;
            float prevT = 0.0f;

            @Override
            public boolean hasNext() {
                return this.nextCurveIdx < n3 + 1;
            }

            @Override
            public Integer next() {
                Integer n22;
                if (this.nextCurveIdx < n3) {
                    float f2 = arrf2[this.nextCurveIdx];
                    float f3 = (f2 - this.prevT) / (1.0f - this.prevT);
                    Helpers.subdivideAt(f3, arrf, this.curCurveOff, arrf, 0, arrf, n2, n2);
                    this.prevT = f2;
                    n22 = this.i0;
                    this.curCurveOff = this.itype;
                } else {
                    n22 = this.curCurveOff;
                }
                ++this.nextCurveIdx;
                return n22;
            }

            @Override
            public void remove() {
            }
        };
    }
}

