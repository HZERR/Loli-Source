/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.CubicCurve2D;
import com.sun.javafx.geom.FlatteningPathIterator;
import com.sun.javafx.geom.Line2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.QuadIterator;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;

public class QuadCurve2D
extends Shape {
    public float x1;
    public float y1;
    public float ctrlx;
    public float ctrly;
    public float x2;
    public float y2;
    private static final int BELOW = -2;
    private static final int LOWEDGE = -1;
    private static final int INSIDE = 0;
    private static final int HIGHEDGE = 1;
    private static final int ABOVE = 2;

    public QuadCurve2D() {
    }

    public QuadCurve2D(float f2, float f3, float f4, float f5, float f6, float f7) {
        this.setCurve(f2, f3, f4, f5, f6, f7);
    }

    public void setCurve(float f2, float f3, float f4, float f5, float f6, float f7) {
        this.x1 = f2;
        this.y1 = f3;
        this.ctrlx = f4;
        this.ctrly = f5;
        this.x2 = f6;
        this.y2 = f7;
    }

    @Override
    public RectBounds getBounds() {
        float f2 = Math.min(Math.min(this.x1, this.x2), this.ctrlx);
        float f3 = Math.min(Math.min(this.y1, this.y2), this.ctrly);
        float f4 = Math.max(Math.max(this.x1, this.x2), this.ctrlx);
        float f5 = Math.max(Math.max(this.y1, this.y2), this.ctrly);
        return new RectBounds(f2, f3, f4, f5);
    }

    public CubicCurve2D toCubic() {
        return new CubicCurve2D(this.x1, this.y1, (this.x1 + 2.0f * this.ctrlx) / 3.0f, (this.y1 + 2.0f * this.ctrly) / 3.0f, (2.0f * this.ctrlx + this.x2) / 3.0f, (2.0f * this.ctrly + this.y2) / 3.0f, this.x2, this.y2);
    }

    public void setCurve(float[] arrf, int n2) {
        this.setCurve(arrf[n2 + 0], arrf[n2 + 1], arrf[n2 + 2], arrf[n2 + 3], arrf[n2 + 4], arrf[n2 + 5]);
    }

    public void setCurve(Point2D point2D, Point2D point2D2, Point2D point2D3) {
        this.setCurve(point2D.x, point2D.y, point2D2.x, point2D2.y, point2D3.x, point2D3.y);
    }

    public void setCurve(Point2D[] arrpoint2D, int n2) {
        this.setCurve(arrpoint2D[n2 + 0].x, arrpoint2D[n2 + 0].y, arrpoint2D[n2 + 1].x, arrpoint2D[n2 + 1].y, arrpoint2D[n2 + 2].x, arrpoint2D[n2 + 2].y);
    }

    public void setCurve(QuadCurve2D quadCurve2D) {
        this.setCurve(quadCurve2D.x1, quadCurve2D.y1, quadCurve2D.ctrlx, quadCurve2D.ctrly, quadCurve2D.x2, quadCurve2D.y2);
    }

    public static float getFlatnessSq(float f2, float f3, float f4, float f5, float f6, float f7) {
        return Line2D.ptSegDistSq(f2, f3, f6, f7, f4, f5);
    }

    public static float getFlatness(float f2, float f3, float f4, float f5, float f6, float f7) {
        return Line2D.ptSegDist(f2, f3, f6, f7, f4, f5);
    }

    public static float getFlatnessSq(float[] arrf, int n2) {
        return Line2D.ptSegDistSq(arrf[n2 + 0], arrf[n2 + 1], arrf[n2 + 4], arrf[n2 + 5], arrf[n2 + 2], arrf[n2 + 3]);
    }

    public static float getFlatness(float[] arrf, int n2) {
        return Line2D.ptSegDist(arrf[n2 + 0], arrf[n2 + 1], arrf[n2 + 4], arrf[n2 + 5], arrf[n2 + 2], arrf[n2 + 3]);
    }

    public float getFlatnessSq() {
        return Line2D.ptSegDistSq(this.x1, this.y1, this.x2, this.y2, this.ctrlx, this.ctrly);
    }

    public float getFlatness() {
        return Line2D.ptSegDist(this.x1, this.y1, this.x2, this.y2, this.ctrlx, this.ctrly);
    }

    public void subdivide(QuadCurve2D quadCurve2D, QuadCurve2D quadCurve2D2) {
        QuadCurve2D.subdivide(this, quadCurve2D, quadCurve2D2);
    }

    public static void subdivide(QuadCurve2D quadCurve2D, QuadCurve2D quadCurve2D2, QuadCurve2D quadCurve2D3) {
        float f2 = quadCurve2D.x1;
        float f3 = quadCurve2D.y1;
        float f4 = quadCurve2D.ctrlx;
        float f5 = quadCurve2D.ctrly;
        float f6 = quadCurve2D.x2;
        float f7 = quadCurve2D.y2;
        float f8 = (f2 + f4) / 2.0f;
        float f9 = (f3 + f5) / 2.0f;
        float f10 = (f6 + f4) / 2.0f;
        float f11 = (f7 + f5) / 2.0f;
        f4 = (f8 + f10) / 2.0f;
        f5 = (f9 + f11) / 2.0f;
        if (quadCurve2D2 != null) {
            quadCurve2D2.setCurve(f2, f3, f8, f9, f4, f5);
        }
        if (quadCurve2D3 != null) {
            quadCurve2D3.setCurve(f4, f5, f10, f11, f6, f7);
        }
    }

    public static void subdivide(float[] arrf, int n2, float[] arrf2, int n3, float[] arrf3, int n4) {
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

    public static int solveQuadratic(float[] arrf) {
        return QuadCurve2D.solveQuadratic(arrf, arrf);
    }

    public static int solveQuadratic(float[] arrf, float[] arrf2) {
        float f2 = arrf[2];
        float f3 = arrf[1];
        float f4 = arrf[0];
        int n2 = 0;
        if (f2 == 0.0f) {
            if (f3 == 0.0f) {
                return -1;
            }
            arrf2[n2++] = -f4 / f3;
        } else {
            float f5 = f3 * f3 - 4.0f * f2 * f4;
            if (f5 < 0.0f) {
                return 0;
            }
            f5 = (float)Math.sqrt(f5);
            if (f3 < 0.0f) {
                f5 = -f5;
            }
            float f6 = (f3 + f5) / -2.0f;
            arrf2[n2++] = f6 / f2;
            if (f6 != 0.0f) {
                arrf2[n2++] = f4 / f6;
            }
        }
        return n2;
    }

    @Override
    public boolean contains(float f2, float f3) {
        float f4 = this.x1;
        float f5 = f2 - f4;
        float f6 = this.y1;
        float f7 = this.ctrly;
        float f8 = this.y2;
        float f9 = f6 - 2.0f * f7 + f8;
        float f10 = f3 - f6;
        float f11 = this.ctrlx;
        float f12 = this.x2;
        float f13 = f4 - 2.0f * f11 + f12;
        float f14 = f12 - f4;
        float f15 = f8 - f6;
        float f16 = (f5 * f9 - f10 * f13) / (f14 * f9 - f15 * f13);
        if (f16 < 0.0f || f16 > 1.0f || f16 != f16) {
            return false;
        }
        float f17 = f13 * f16 * f16 + 2.0f * (f11 - f4) * f16 + f4;
        float f18 = f9 * f16 * f16 + 2.0f * (f7 - f6) * f16 + f6;
        float f19 = f14 * f16 + f4;
        float f20 = f15 * f16 + f6;
        return f2 >= f17 && f2 < f19 || f2 >= f19 && f2 < f17 || f3 >= f18 && f3 < f20 || f3 >= f20 && f3 < f18;
    }

    @Override
    public boolean contains(Point2D point2D) {
        return this.contains(point2D.x, point2D.y);
    }

    private static void fillEqn(float[] arrf, float f2, float f3, float f4, float f5) {
        arrf[0] = f3 - f2;
        arrf[1] = f4 + f4 - f3 - f3;
        arrf[2] = f3 - f4 - f4 + f5;
    }

    private static int evalQuadratic(float[] arrf, int n2, boolean bl, boolean bl2, float[] arrf2, float f2, float f3, float f4) {
        int n3 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            float f5 = arrf[i2];
            if (!(bl ? f5 >= 0.0f : f5 > 0.0f) || !(bl2 ? f5 <= 1.0f : f5 < 1.0f) || arrf2 != null && arrf2[1] + 2.0f * arrf2[2] * f5 == 0.0f) continue;
            float f6 = 1.0f - f5;
            arrf[n3++] = f2 * f6 * f6 + 2.0f * f3 * f5 * f6 + f4 * f5 * f5;
        }
        return n3;
    }

    private static int getTag(float f2, float f3, float f4) {
        if (f2 <= f3) {
            return f2 < f3 ? -2 : -1;
        }
        if (f2 >= f4) {
            return f2 > f4 ? 2 : 1;
        }
        return 0;
    }

    private static boolean inwards(int n2, int n3, int n4) {
        switch (n2) {
            default: {
                return false;
            }
            case -1: {
                return n3 >= 0 || n4 >= 0;
            }
            case 0: {
                return true;
            }
            case 1: 
        }
        return n3 <= 0 || n4 <= 0;
    }

    @Override
    public boolean intersects(float f2, float f3, float f4, float f5) {
        int n2;
        boolean bl;
        if (f4 <= 0.0f || f5 <= 0.0f) {
            return false;
        }
        float f6 = this.x1;
        float f7 = this.y1;
        int n3 = QuadCurve2D.getTag(f6, f2, f2 + f4);
        int n4 = QuadCurve2D.getTag(f7, f3, f3 + f5);
        if (n3 == 0 && n4 == 0) {
            return true;
        }
        float f8 = this.x2;
        float f9 = this.y2;
        int n5 = QuadCurve2D.getTag(f8, f2, f2 + f4);
        int n6 = QuadCurve2D.getTag(f9, f3, f3 + f5);
        if (n5 == 0 && n6 == 0) {
            return true;
        }
        float f10 = this.ctrlx;
        float f11 = this.ctrly;
        int n7 = QuadCurve2D.getTag(f10, f2, f2 + f4);
        int n8 = QuadCurve2D.getTag(f11, f3, f3 + f5);
        if (n3 < 0 && n5 < 0 && n7 < 0) {
            return false;
        }
        if (n4 < 0 && n6 < 0 && n8 < 0) {
            return false;
        }
        if (n3 > 0 && n5 > 0 && n7 > 0) {
            return false;
        }
        if (n4 > 0 && n6 > 0 && n8 > 0) {
            return false;
        }
        if (QuadCurve2D.inwards(n3, n5, n7) && QuadCurve2D.inwards(n4, n6, n8)) {
            return true;
        }
        if (QuadCurve2D.inwards(n5, n3, n7) && QuadCurve2D.inwards(n6, n4, n8)) {
            return true;
        }
        boolean bl2 = n3 * n5 <= 0;
        boolean bl3 = bl = n4 * n6 <= 0;
        if (n3 == 0 && n5 == 0 && bl) {
            return true;
        }
        if (n4 == 0 && n6 == 0 && bl2) {
            return true;
        }
        float[] arrf = new float[3];
        float[] arrf2 = new float[3];
        if (!bl) {
            QuadCurve2D.fillEqn(arrf, n4 < 0 ? f3 : f3 + f5, f7, f11, f9);
            return QuadCurve2D.solveQuadratic(arrf, arrf2) == 2 && QuadCurve2D.evalQuadratic(arrf2, 2, true, true, null, f6, f10, f8) == 2 && QuadCurve2D.getTag(arrf2[0], f2, f2 + f4) * QuadCurve2D.getTag(arrf2[1], f2, f2 + f4) <= 0;
        }
        if (!bl2) {
            QuadCurve2D.fillEqn(arrf, n3 < 0 ? f2 : f2 + f4, f6, f10, f8);
            return QuadCurve2D.solveQuadratic(arrf, arrf2) == 2 && QuadCurve2D.evalQuadratic(arrf2, 2, true, true, null, f7, f11, f9) == 2 && QuadCurve2D.getTag(arrf2[0], f3, f3 + f5) * QuadCurve2D.getTag(arrf2[1], f3, f3 + f5) <= 0;
        }
        float f12 = f8 - f6;
        float f13 = f9 - f7;
        float f14 = f9 * f6 - f8 * f7;
        int n9 = n4 == 0 ? n3 : QuadCurve2D.getTag((f14 + f12 * (n4 < 0 ? f3 : f3 + f5)) / f13, f2, f2 + f4);
        if (n9 * (n2 = n6 == 0 ? n5 : QuadCurve2D.getTag((f14 + f12 * (n6 < 0 ? f3 : f3 + f5)) / f13, f2, f2 + f4)) <= 0) {
            return true;
        }
        n9 = n9 * n3 <= 0 ? n4 : n6;
        QuadCurve2D.fillEqn(arrf, n2 < 0 ? f2 : f2 + f4, f6, f10, f8);
        int n10 = QuadCurve2D.solveQuadratic(arrf, arrf2);
        QuadCurve2D.evalQuadratic(arrf2, n10, true, true, null, f7, f11, f9);
        n2 = QuadCurve2D.getTag(arrf2[0], f3, f3 + f5);
        return n9 * n2 <= 0;
    }

    @Override
    public boolean contains(float f2, float f3, float f4, float f5) {
        if (f4 <= 0.0f || f5 <= 0.0f) {
            return false;
        }
        return this.contains(f2, f3) && this.contains(f2 + f4, f3) && this.contains(f2 + f4, f3 + f5) && this.contains(f2, f3 + f5);
    }

    @Override
    public PathIterator getPathIterator(BaseTransform baseTransform) {
        return new QuadIterator(this, baseTransform);
    }

    @Override
    public PathIterator getPathIterator(BaseTransform baseTransform, float f2) {
        return new FlatteningPathIterator(this.getPathIterator(baseTransform), f2);
    }

    @Override
    public QuadCurve2D copy() {
        return new QuadCurve2D(this.x1, this.y1, this.ctrlx, this.ctrly, this.x2, this.y2);
    }

    public int hashCode() {
        int n2 = Float.floatToIntBits(this.x1);
        n2 += Float.floatToIntBits(this.y1) * 37;
        n2 += Float.floatToIntBits(this.x2) * 43;
        n2 += Float.floatToIntBits(this.y2) * 47;
        n2 += Float.floatToIntBits(this.ctrlx) * 53;
        return n2 += Float.floatToIntBits(this.ctrly) * 59;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof QuadCurve2D) {
            QuadCurve2D quadCurve2D = (QuadCurve2D)object;
            return this.x1 == quadCurve2D.x1 && this.y1 == quadCurve2D.y1 && this.x2 == quadCurve2D.x2 && this.y2 == quadCurve2D.y2 && this.ctrlx == quadCurve2D.ctrlx && this.ctrly == quadCurve2D.ctrly;
        }
        return false;
    }
}

