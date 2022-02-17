/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.CubicIterator;
import com.sun.javafx.geom.FlatteningPathIterator;
import com.sun.javafx.geom.Line2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.QuadCurve2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import java.util.Arrays;

public class CubicCurve2D
extends Shape {
    public float x1;
    public float y1;
    public float ctrlx1;
    public float ctrly1;
    public float ctrlx2;
    public float ctrly2;
    public float x2;
    public float y2;
    private static final int BELOW = -2;
    private static final int LOWEDGE = -1;
    private static final int INSIDE = 0;
    private static final int HIGHEDGE = 1;
    private static final int ABOVE = 2;

    public CubicCurve2D() {
    }

    public CubicCurve2D(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        this.setCurve(f2, f3, f4, f5, f6, f7, f8, f9);
    }

    public void setCurve(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        this.x1 = f2;
        this.y1 = f3;
        this.ctrlx1 = f4;
        this.ctrly1 = f5;
        this.ctrlx2 = f6;
        this.ctrly2 = f7;
        this.x2 = f8;
        this.y2 = f9;
    }

    @Override
    public RectBounds getBounds() {
        float f2 = Math.min(Math.min(this.x1, this.x2), Math.min(this.ctrlx1, this.ctrlx2));
        float f3 = Math.min(Math.min(this.y1, this.y2), Math.min(this.ctrly1, this.ctrly2));
        float f4 = Math.max(Math.max(this.x1, this.x2), Math.max(this.ctrlx1, this.ctrlx2));
        float f5 = Math.max(Math.max(this.y1, this.y2), Math.max(this.ctrly1, this.ctrly2));
        return new RectBounds(f2, f3, f4, f5);
    }

    public Point2D eval(float f2) {
        Point2D point2D = new Point2D();
        this.eval(f2, point2D);
        return point2D;
    }

    public void eval(float f2, Point2D point2D) {
        point2D.setLocation(this.calcX(f2), this.calcY(f2));
    }

    public Point2D evalDt(float f2) {
        Point2D point2D = new Point2D();
        this.evalDt(f2, point2D);
        return point2D;
    }

    public void evalDt(float f2, Point2D point2D) {
        float f3 = f2;
        float f4 = 1.0f - f3;
        float f5 = 3.0f * ((this.ctrlx1 - this.x1) * f4 * f4 + 2.0f * (this.ctrlx2 - this.ctrlx1) * f4 * f3 + (this.x2 - this.ctrlx2) * f3 * f3);
        float f6 = 3.0f * ((this.ctrly1 - this.y1) * f4 * f4 + 2.0f * (this.ctrly2 - this.ctrly1) * f4 * f3 + (this.y2 - this.ctrly2) * f3 * f3);
        point2D.setLocation(f5, f6);
    }

    public void setCurve(float[] arrf, int n2) {
        this.setCurve(arrf[n2 + 0], arrf[n2 + 1], arrf[n2 + 2], arrf[n2 + 3], arrf[n2 + 4], arrf[n2 + 5], arrf[n2 + 6], arrf[n2 + 7]);
    }

    public void setCurve(Point2D point2D, Point2D point2D2, Point2D point2D3, Point2D point2D4) {
        this.setCurve(point2D.x, point2D.y, point2D2.x, point2D2.y, point2D3.x, point2D3.y, point2D4.x, point2D4.y);
    }

    public void setCurve(Point2D[] arrpoint2D, int n2) {
        this.setCurve(arrpoint2D[n2 + 0].x, arrpoint2D[n2 + 0].y, arrpoint2D[n2 + 1].x, arrpoint2D[n2 + 1].y, arrpoint2D[n2 + 2].x, arrpoint2D[n2 + 2].y, arrpoint2D[n2 + 3].x, arrpoint2D[n2 + 3].y);
    }

    public void setCurve(CubicCurve2D cubicCurve2D) {
        this.setCurve(cubicCurve2D.x1, cubicCurve2D.y1, cubicCurve2D.ctrlx1, cubicCurve2D.ctrly1, cubicCurve2D.ctrlx2, cubicCurve2D.ctrly2, cubicCurve2D.x2, cubicCurve2D.y2);
    }

    public static float getFlatnessSq(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        return Math.max(Line2D.ptSegDistSq(f2, f3, f8, f9, f4, f5), Line2D.ptSegDistSq(f2, f3, f8, f9, f6, f7));
    }

    public static float getFlatness(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        return (float)Math.sqrt(CubicCurve2D.getFlatnessSq(f2, f3, f4, f5, f6, f7, f8, f9));
    }

    public static float getFlatnessSq(float[] arrf, int n2) {
        return CubicCurve2D.getFlatnessSq(arrf[n2 + 0], arrf[n2 + 1], arrf[n2 + 2], arrf[n2 + 3], arrf[n2 + 4], arrf[n2 + 5], arrf[n2 + 6], arrf[n2 + 7]);
    }

    public static float getFlatness(float[] arrf, int n2) {
        return CubicCurve2D.getFlatness(arrf[n2 + 0], arrf[n2 + 1], arrf[n2 + 2], arrf[n2 + 3], arrf[n2 + 4], arrf[n2 + 5], arrf[n2 + 6], arrf[n2 + 7]);
    }

    public float getFlatnessSq() {
        return CubicCurve2D.getFlatnessSq(this.x1, this.y1, this.ctrlx1, this.ctrly1, this.ctrlx2, this.ctrly2, this.x2, this.y2);
    }

    public float getFlatness() {
        return CubicCurve2D.getFlatness(this.x1, this.y1, this.ctrlx1, this.ctrly1, this.ctrlx2, this.ctrly2, this.x2, this.y2);
    }

    public void subdivide(float f2, CubicCurve2D cubicCurve2D, CubicCurve2D cubicCurve2D2) {
        float f3;
        float f4;
        float f5;
        float f6;
        float f7;
        float f8;
        float f9;
        float f10;
        if (cubicCurve2D == null && cubicCurve2D2 == null) {
            return;
        }
        float f11 = this.calcX(f2);
        float f12 = this.calcY(f2);
        float f13 = this.x1;
        float f14 = this.y1;
        float f15 = this.ctrlx1;
        float f16 = this.ctrly1;
        float f17 = this.ctrlx2;
        float f18 = this.ctrly2;
        float f19 = this.x2;
        float f20 = this.y2;
        float f21 = 1.0f - f2;
        float f22 = f21 * f15 + f2 * f17;
        float f23 = f21 * f16 + f2 * f18;
        if (cubicCurve2D != null) {
            f10 = f13;
            f9 = f14;
            f8 = f21 * f13 + f2 * f15;
            f7 = f21 * f14 + f2 * f16;
            f6 = f21 * f8 + f2 * f22;
            f5 = f21 * f7 + f2 * f23;
            f4 = f11;
            f3 = f12;
            cubicCurve2D.setCurve(f10, f9, f8, f7, f6, f5, f4, f3);
        }
        if (cubicCurve2D2 != null) {
            f10 = f11;
            f9 = f12;
            f8 = f21 * f17 + f2 * f19;
            f7 = f21 * f18 + f2 * f20;
            f6 = f21 * f22 + f2 * f8;
            f5 = f21 * f23 + f2 * f7;
            f4 = f19;
            f3 = f20;
            cubicCurve2D2.setCurve(f10, f9, f6, f5, f8, f7, f4, f3);
        }
    }

    public void subdivide(CubicCurve2D cubicCurve2D, CubicCurve2D cubicCurve2D2) {
        CubicCurve2D.subdivide(this, cubicCurve2D, cubicCurve2D2);
    }

    public static void subdivide(CubicCurve2D cubicCurve2D, CubicCurve2D cubicCurve2D2, CubicCurve2D cubicCurve2D3) {
        float f2 = cubicCurve2D.x1;
        float f3 = cubicCurve2D.y1;
        float f4 = cubicCurve2D.ctrlx1;
        float f5 = cubicCurve2D.ctrly1;
        float f6 = cubicCurve2D.ctrlx2;
        float f7 = cubicCurve2D.ctrly2;
        float f8 = cubicCurve2D.x2;
        float f9 = cubicCurve2D.y2;
        float f10 = (f4 + f6) / 2.0f;
        float f11 = (f5 + f7) / 2.0f;
        f4 = (f2 + f4) / 2.0f;
        f5 = (f3 + f5) / 2.0f;
        f6 = (f8 + f6) / 2.0f;
        f7 = (f9 + f7) / 2.0f;
        float f12 = (f4 + f10) / 2.0f;
        float f13 = (f5 + f11) / 2.0f;
        float f14 = (f6 + f10) / 2.0f;
        float f15 = (f7 + f11) / 2.0f;
        f10 = (f12 + f14) / 2.0f;
        f11 = (f13 + f15) / 2.0f;
        if (cubicCurve2D2 != null) {
            cubicCurve2D2.setCurve(f2, f3, f4, f5, f12, f13, f10, f11);
        }
        if (cubicCurve2D3 != null) {
            cubicCurve2D3.setCurve(f10, f11, f14, f15, f6, f7, f8, f9);
        }
    }

    public static void subdivide(float[] arrf, int n2, float[] arrf2, int n3, float[] arrf3, int n4) {
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

    public static int solveCubic(float[] arrf) {
        return CubicCurve2D.solveCubic(arrf, arrf);
    }

    public static int solveCubic(float[] arrf, float[] arrf2) {
        float f2 = arrf[3];
        if (f2 == 0.0f) {
            return QuadCurve2D.solveQuadratic(arrf, arrf2);
        }
        float f3 = arrf[2] / f2;
        float f4 = arrf[1] / f2;
        float f5 = arrf[0] / f2;
        int n2 = 0;
        float f6 = (f3 * f3 - 3.0f * f4) / 9.0f;
        float f7 = (2.0f * f3 * f3 * f3 - 9.0f * f3 * f4 + 27.0f * f5) / 54.0f;
        float f8 = f7 * f7;
        float f9 = f6 * f6 * f6;
        f3 /= 3.0f;
        if (f8 < f9) {
            float f10 = (float)Math.acos((double)f7 / Math.sqrt(f9));
            f6 = (float)(-2.0 * Math.sqrt(f6));
            if (arrf2 == arrf) {
                arrf = new float[4];
                System.arraycopy(arrf2, 0, arrf, 0, 4);
            }
            arrf2[n2++] = (float)((double)f6 * Math.cos(f10 / 3.0f) - (double)f3);
            arrf2[n2++] = (float)((double)f6 * Math.cos(((double)f10 + Math.PI * 2) / 3.0) - (double)f3);
            arrf2[n2++] = (float)((double)f6 * Math.cos(((double)f10 - Math.PI * 2) / 3.0) - (double)f3);
            CubicCurve2D.fixRoots(arrf2, arrf);
        } else {
            boolean bl = f7 < 0.0f;
            float f11 = (float)Math.sqrt(f8 - f9);
            if (bl) {
                f7 = -f7;
            }
            float f12 = (float)Math.pow(f7 + f11, 0.3333333432674408);
            if (!bl) {
                f12 = -f12;
            }
            float f13 = f12 == 0.0f ? 0.0f : f6 / f12;
            arrf2[n2++] = f12 + f13 - f3;
        }
        return n2;
    }

    private static void fixRoots(float[] arrf, float[] arrf2) {
        for (int i2 = 0; i2 < 3; ++i2) {
            float f2 = arrf[i2];
            if (Math.abs(f2) < 1.0E-5f) {
                arrf[i2] = CubicCurve2D.findZero(f2, 0.0f, arrf2);
                continue;
            }
            if (!(Math.abs(f2 - 1.0f) < 1.0E-5f)) continue;
            arrf[i2] = CubicCurve2D.findZero(f2, 1.0f, arrf2);
        }
    }

    private static float solveEqn(float[] arrf, int n2, float f2) {
        float f3 = arrf[n2];
        while (--n2 >= 0) {
            f3 = f3 * f2 + arrf[n2];
        }
        return f3;
    }

    private static float findZero(float f2, float f3, float[] arrf) {
        float[] arrf2 = new float[]{arrf[1], 2.0f * arrf[2], 3.0f * arrf[3]};
        float f4 = 0.0f;
        float f5 = f2;
        float f6;
        while ((f6 = CubicCurve2D.solveEqn(arrf2, 2, f2)) != 0.0f) {
            float f7;
            float f8 = CubicCurve2D.solveEqn(arrf, 3, f2);
            if (f8 == 0.0f) {
                return f2;
            }
            float f9 = -(f8 / f6);
            if (f4 == 0.0f) {
                f4 = f9;
            }
            if (f2 < f3) {
                if (f9 < 0.0f) {
                    return f2;
                }
            } else if (f2 > f3) {
                if (f9 > 0.0f) {
                    return f2;
                }
            } else {
                return f9 > 0.0f ? f3 + Float.MIN_VALUE : f3 - Float.MIN_VALUE;
            }
            if (f2 == (f7 = f2 + f9)) {
                return f2;
            }
            if (f9 * f4 < 0.0f) {
                int n2;
                int n3 = n2 = f5 < f2 ? CubicCurve2D.getTag(f3, f5, f2) : CubicCurve2D.getTag(f3, f2, f5);
                if (n2 != 0) {
                    return (f5 + f2) / 2.0f;
                }
                f2 = f3;
                continue;
            }
            f2 = f7;
        }
        return f2;
    }

    @Override
    public boolean contains(float f2, float f3) {
        if (f2 * 0.0f + f3 * 0.0f != 0.0f) {
            return false;
        }
        int n2 = Shape.pointCrossingsForLine(f2, f3, this.x1, this.y1, this.x2, this.y2) + Shape.pointCrossingsForCubic(f2, f3, this.x1, this.y1, this.ctrlx1, this.ctrly1, this.ctrlx2, this.ctrly2, this.x2, this.y2, 0);
        return (n2 & 1) == 1;
    }

    @Override
    public boolean contains(Point2D point2D) {
        return this.contains(point2D.x, point2D.y);
    }

    private static void fillEqn(float[] arrf, float f2, float f3, float f4, float f5, float f6) {
        arrf[0] = f3 - f2;
        arrf[1] = (f4 - f3) * 3.0f;
        arrf[2] = (f5 - f4 - f4 + f3) * 3.0f;
        arrf[3] = f6 + (f4 - f5) * 3.0f - f3;
    }

    private static int evalCubic(float[] arrf, int n2, boolean bl, boolean bl2, float[] arrf2, float f2, float f3, float f4, float f5) {
        int n3 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            float f6 = arrf[i2];
            if (!(bl ? f6 >= 0.0f : f6 > 0.0f) || !(bl2 ? f6 <= 1.0f : f6 < 1.0f) || arrf2 != null && arrf2[1] + (2.0f * arrf2[2] + 3.0f * arrf2[3] * f6) * f6 == 0.0f) continue;
            float f7 = 1.0f - f6;
            arrf[n3++] = f2 * f7 * f7 * f7 + 3.0f * f3 * f6 * f7 * f7 + 3.0f * f4 * f6 * f6 * f7 + f5 * f6 * f6 * f6;
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
        int n3 = CubicCurve2D.getTag(f6, f2, f2 + f4);
        int n4 = CubicCurve2D.getTag(f7, f3, f3 + f5);
        if (n3 == 0 && n4 == 0) {
            return true;
        }
        float f8 = this.x2;
        float f9 = this.y2;
        int n5 = CubicCurve2D.getTag(f8, f2, f2 + f4);
        int n6 = CubicCurve2D.getTag(f9, f3, f3 + f5);
        if (n5 == 0 && n6 == 0) {
            return true;
        }
        float f10 = this.ctrlx1;
        float f11 = this.ctrly1;
        float f12 = this.ctrlx2;
        float f13 = this.ctrly2;
        int n7 = CubicCurve2D.getTag(f10, f2, f2 + f4);
        int n8 = CubicCurve2D.getTag(f11, f3, f3 + f5);
        int n9 = CubicCurve2D.getTag(f12, f2, f2 + f4);
        int n10 = CubicCurve2D.getTag(f13, f3, f3 + f5);
        if (n3 < 0 && n5 < 0 && n7 < 0 && n9 < 0) {
            return false;
        }
        if (n4 < 0 && n6 < 0 && n8 < 0 && n10 < 0) {
            return false;
        }
        if (n3 > 0 && n5 > 0 && n7 > 0 && n9 > 0) {
            return false;
        }
        if (n4 > 0 && n6 > 0 && n8 > 0 && n10 > 0) {
            return false;
        }
        if (CubicCurve2D.inwards(n3, n5, n7) && CubicCurve2D.inwards(n4, n6, n8)) {
            return true;
        }
        if (CubicCurve2D.inwards(n5, n3, n9) && CubicCurve2D.inwards(n6, n4, n10)) {
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
        float[] arrf = new float[4];
        float[] arrf2 = new float[4];
        if (!bl) {
            CubicCurve2D.fillEqn(arrf, n4 < 0 ? f3 : f3 + f5, f7, f11, f13, f9);
            int n11 = CubicCurve2D.solveCubic(arrf, arrf2);
            n11 = CubicCurve2D.evalCubic(arrf2, n11, true, true, null, f6, f10, f12, f8);
            return n11 == 2 && CubicCurve2D.getTag(arrf2[0], f2, f2 + f4) * CubicCurve2D.getTag(arrf2[1], f2, f2 + f4) <= 0;
        }
        if (!bl2) {
            CubicCurve2D.fillEqn(arrf, n3 < 0 ? f2 : f2 + f4, f6, f10, f12, f8);
            int n12 = CubicCurve2D.solveCubic(arrf, arrf2);
            n12 = CubicCurve2D.evalCubic(arrf2, n12, true, true, null, f7, f11, f13, f9);
            return n12 == 2 && CubicCurve2D.getTag(arrf2[0], f3, f3 + f5) * CubicCurve2D.getTag(arrf2[1], f3, f3 + f5) <= 0;
        }
        float f14 = f8 - f6;
        float f15 = f9 - f7;
        float f16 = f9 * f6 - f8 * f7;
        int n13 = n4 == 0 ? n3 : CubicCurve2D.getTag((f16 + f14 * (n4 < 0 ? f3 : f3 + f5)) / f15, f2, f2 + f4);
        if (n13 * (n2 = n6 == 0 ? n5 : CubicCurve2D.getTag((f16 + f14 * (n6 < 0 ? f3 : f3 + f5)) / f15, f2, f2 + f4)) <= 0) {
            return true;
        }
        n13 = n13 * n3 <= 0 ? n4 : n6;
        CubicCurve2D.fillEqn(arrf, n2 < 0 ? f2 : f2 + f4, f6, f10, f12, f8);
        int n14 = CubicCurve2D.solveCubic(arrf, arrf2);
        n14 = CubicCurve2D.evalCubic(arrf2, n14, true, true, null, f7, f11, f13, f9);
        int[] arrn = new int[n14 + 1];
        for (int i2 = 0; i2 < n14; ++i2) {
            arrn[i2] = CubicCurve2D.getTag(arrf2[i2], f3, f3 + f5);
        }
        arrn[n14] = n13;
        Arrays.sort(arrn);
        return n14 >= 1 && arrn[0] * arrn[1] <= 0 || n14 >= 3 && arrn[2] * arrn[3] <= 0;
    }

    @Override
    public boolean contains(float f2, float f3, float f4, float f5) {
        if (f4 <= 0.0f || f5 <= 0.0f) {
            return false;
        }
        if (!(this.contains(f2, f3) && this.contains(f2 + f4, f3) && this.contains(f2 + f4, f3 + f5) && this.contains(f2, f3 + f5))) {
            return false;
        }
        return !Shape.intersectsLine(f2, f3, f4, f5, this.x1, this.y1, this.x2, this.y2);
    }

    @Override
    public PathIterator getPathIterator(BaseTransform baseTransform) {
        return new CubicIterator(this, baseTransform);
    }

    @Override
    public PathIterator getPathIterator(BaseTransform baseTransform, float f2) {
        return new FlatteningPathIterator(this.getPathIterator(baseTransform), f2);
    }

    @Override
    public CubicCurve2D copy() {
        return new CubicCurve2D(this.x1, this.y1, this.ctrlx1, this.ctrly1, this.ctrlx2, this.ctrly2, this.x2, this.y2);
    }

    public int hashCode() {
        int n2 = Float.floatToIntBits(this.x1);
        n2 += Float.floatToIntBits(this.y1) * 37;
        n2 += Float.floatToIntBits(this.x2) * 43;
        n2 += Float.floatToIntBits(this.y2) * 47;
        n2 += Float.floatToIntBits(this.ctrlx1) * 53;
        n2 += Float.floatToIntBits(this.ctrly1) * 59;
        n2 += Float.floatToIntBits(this.ctrlx2) * 61;
        return n2 += Float.floatToIntBits(this.ctrly2) * 101;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof CubicCurve2D) {
            CubicCurve2D cubicCurve2D = (CubicCurve2D)object;
            return this.x1 == cubicCurve2D.x1 && this.y1 == cubicCurve2D.y1 && this.x2 == cubicCurve2D.x2 && this.y2 == cubicCurve2D.y2 && this.ctrlx1 == cubicCurve2D.ctrlx1 && this.ctrly1 == cubicCurve2D.ctrly1 && this.ctrlx2 == cubicCurve2D.ctrlx2 && this.ctrly2 == cubicCurve2D.ctrly2;
        }
        return false;
    }

    private float calcX(float f2) {
        float f3 = 1.0f - f2;
        return f3 * f3 * f3 * this.x1 + 3.0f * (f2 * f3 * f3 * this.ctrlx1 + f2 * f2 * f3 * this.ctrlx2) + f2 * f2 * f2 * this.x2;
    }

    private float calcY(float f2) {
        float f3 = 1.0f - f2;
        return f3 * f3 * f3 * this.y1 + 3.0f * (f2 * f3 * f3 * this.ctrly1 + f2 * f2 * f3 * this.ctrly2) + f2 * f2 * f2 * this.y2;
    }
}

