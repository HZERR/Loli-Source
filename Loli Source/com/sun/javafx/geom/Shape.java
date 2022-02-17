/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.IllegalPathStateException;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;

public abstract class Shape {
    public static final int RECT_INTERSECTS = Integer.MIN_VALUE;
    public static final int OUT_LEFT = 1;
    public static final int OUT_TOP = 2;
    public static final int OUT_RIGHT = 4;
    public static final int OUT_BOTTOM = 8;

    public abstract RectBounds getBounds();

    public abstract boolean contains(float var1, float var2);

    public boolean contains(Point2D point2D) {
        return this.contains(point2D.x, point2D.y);
    }

    public abstract boolean intersects(float var1, float var2, float var3, float var4);

    public boolean intersects(RectBounds rectBounds) {
        float f2 = rectBounds.getMinX();
        float f3 = rectBounds.getMinY();
        float f4 = rectBounds.getMaxX() - f2;
        float f5 = rectBounds.getMaxY() - f3;
        return this.intersects(f2, f3, f4, f5);
    }

    public abstract boolean contains(float var1, float var2, float var3, float var4);

    public boolean contains(RectBounds rectBounds) {
        float f2 = rectBounds.getMinX();
        float f3 = rectBounds.getMinY();
        float f4 = rectBounds.getMaxX() - f2;
        float f5 = rectBounds.getMaxY() - f3;
        return this.contains(f2, f3, f4, f5);
    }

    public abstract PathIterator getPathIterator(BaseTransform var1);

    public abstract PathIterator getPathIterator(BaseTransform var1, float var2);

    public abstract Shape copy();

    public static int pointCrossingsForPath(PathIterator pathIterator, float f2, float f3) {
        if (pathIterator.isDone()) {
            return 0;
        }
        float[] arrf = new float[6];
        if (pathIterator.currentSegment(arrf) != 0) {
            throw new IllegalPathStateException("missing initial moveto in path definition");
        }
        pathIterator.next();
        float f4 = arrf[0];
        float f5 = arrf[1];
        float f6 = f4;
        float f7 = f5;
        int n2 = 0;
        while (!pathIterator.isDone()) {
            switch (pathIterator.currentSegment(arrf)) {
                case 0: {
                    if (f7 != f5) {
                        n2 += Shape.pointCrossingsForLine(f2, f3, f6, f7, f4, f5);
                    }
                    f4 = f6 = arrf[0];
                    f5 = f7 = arrf[1];
                    break;
                }
                case 1: {
                    float f8 = arrf[0];
                    float f9 = arrf[1];
                    n2 += Shape.pointCrossingsForLine(f2, f3, f6, f7, f8, f9);
                    f6 = f8;
                    f7 = f9;
                    break;
                }
                case 2: {
                    float f8 = arrf[2];
                    float f9 = arrf[3];
                    n2 += Shape.pointCrossingsForQuad(f2, f3, f6, f7, arrf[0], arrf[1], f8, f9, 0);
                    f6 = f8;
                    f7 = f9;
                    break;
                }
                case 3: {
                    float f8 = arrf[4];
                    float f9 = arrf[5];
                    n2 += Shape.pointCrossingsForCubic(f2, f3, f6, f7, arrf[0], arrf[1], arrf[2], arrf[3], f8, f9, 0);
                    f6 = f8;
                    f7 = f9;
                    break;
                }
                case 4: {
                    if (f7 != f5) {
                        n2 += Shape.pointCrossingsForLine(f2, f3, f6, f7, f4, f5);
                    }
                    f6 = f4;
                    f7 = f5;
                }
            }
            pathIterator.next();
        }
        if (f7 != f5) {
            n2 += Shape.pointCrossingsForLine(f2, f3, f6, f7, f4, f5);
        }
        return n2;
    }

    public static int pointCrossingsForLine(float f2, float f3, float f4, float f5, float f6, float f7) {
        if (f3 < f5 && f3 < f7) {
            return 0;
        }
        if (f3 >= f5 && f3 >= f7) {
            return 0;
        }
        if (f2 >= f4 && f2 >= f6) {
            return 0;
        }
        if (f2 < f4 && f2 < f6) {
            return f5 < f7 ? 1 : -1;
        }
        float f8 = f4 + (f3 - f5) * (f6 - f4) / (f7 - f5);
        if (f2 >= f8) {
            return 0;
        }
        return f5 < f7 ? 1 : -1;
    }

    public static int pointCrossingsForQuad(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, int n2) {
        if (f3 < f5 && f3 < f7 && f3 < f9) {
            return 0;
        }
        if (f3 >= f5 && f3 >= f7 && f3 >= f9) {
            return 0;
        }
        if (f2 >= f4 && f2 >= f6 && f2 >= f8) {
            return 0;
        }
        if (f2 < f4 && f2 < f6 && f2 < f8) {
            if (f3 >= f5) {
                if (f3 < f9) {
                    return 1;
                }
            } else if (f3 >= f9) {
                return -1;
            }
            return 0;
        }
        if (n2 > 52) {
            return Shape.pointCrossingsForLine(f2, f3, f4, f5, f8, f9);
        }
        float f10 = (f4 + f6) / 2.0f;
        float f11 = (f5 + f7) / 2.0f;
        float f12 = (f6 + f8) / 2.0f;
        float f13 = (f7 + f9) / 2.0f;
        f6 = (f10 + f12) / 2.0f;
        f7 = (f11 + f13) / 2.0f;
        if (Float.isNaN(f6) || Float.isNaN(f7)) {
            return 0;
        }
        return Shape.pointCrossingsForQuad(f2, f3, f4, f5, f10, f11, f6, f7, n2 + 1) + Shape.pointCrossingsForQuad(f2, f3, f6, f7, f12, f13, f8, f9, n2 + 1);
    }

    public static int pointCrossingsForCubic(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, int n2) {
        if (f3 < f5 && f3 < f7 && f3 < f9 && f3 < f11) {
            return 0;
        }
        if (f3 >= f5 && f3 >= f7 && f3 >= f9 && f3 >= f11) {
            return 0;
        }
        if (f2 >= f4 && f2 >= f6 && f2 >= f8 && f2 >= f10) {
            return 0;
        }
        if (f2 < f4 && f2 < f6 && f2 < f8 && f2 < f10) {
            if (f3 >= f5) {
                if (f3 < f11) {
                    return 1;
                }
            } else if (f3 >= f11) {
                return -1;
            }
            return 0;
        }
        if (n2 > 52) {
            return Shape.pointCrossingsForLine(f2, f3, f4, f5, f10, f11);
        }
        float f12 = (f6 + f8) / 2.0f;
        float f13 = (f7 + f9) / 2.0f;
        f6 = (f4 + f6) / 2.0f;
        f7 = (f5 + f7) / 2.0f;
        f8 = (f8 + f10) / 2.0f;
        f9 = (f9 + f11) / 2.0f;
        float f14 = (f6 + f12) / 2.0f;
        float f15 = (f7 + f13) / 2.0f;
        float f16 = (f12 + f8) / 2.0f;
        float f17 = (f13 + f9) / 2.0f;
        f12 = (f14 + f16) / 2.0f;
        f13 = (f15 + f17) / 2.0f;
        if (Float.isNaN(f12) || Float.isNaN(f13)) {
            return 0;
        }
        return Shape.pointCrossingsForCubic(f2, f3, f4, f5, f6, f7, f14, f15, f12, f13, n2 + 1) + Shape.pointCrossingsForCubic(f2, f3, f12, f13, f16, f17, f8, f9, f10, f11, n2 + 1);
    }

    public static int rectCrossingsForPath(PathIterator pathIterator, float f2, float f3, float f4, float f5) {
        float f6;
        float f7;
        if (f4 <= f2 || f5 <= f3) {
            return 0;
        }
        if (pathIterator.isDone()) {
            return 0;
        }
        float[] arrf = new float[6];
        if (pathIterator.currentSegment(arrf) != 0) {
            throw new IllegalPathStateException("missing initial moveto in path definition");
        }
        pathIterator.next();
        float f8 = f7 = arrf[0];
        float f9 = f6 = arrf[1];
        int n2 = 0;
        while (n2 != Integer.MIN_VALUE && !pathIterator.isDone()) {
            switch (pathIterator.currentSegment(arrf)) {
                case 0: {
                    if (f8 != f7 || f9 != f6) {
                        n2 = Shape.rectCrossingsForLine(n2, f2, f3, f4, f5, f8, f9, f7, f6);
                    }
                    f7 = f8 = arrf[0];
                    f6 = f9 = arrf[1];
                    break;
                }
                case 1: {
                    float f10 = arrf[0];
                    float f11 = arrf[1];
                    n2 = Shape.rectCrossingsForLine(n2, f2, f3, f4, f5, f8, f9, f10, f11);
                    f8 = f10;
                    f9 = f11;
                    break;
                }
                case 2: {
                    float f10 = arrf[2];
                    float f11 = arrf[3];
                    n2 = Shape.rectCrossingsForQuad(n2, f2, f3, f4, f5, f8, f9, arrf[0], arrf[1], f10, f11, 0);
                    f8 = f10;
                    f9 = f11;
                    break;
                }
                case 3: {
                    float f10 = arrf[4];
                    float f11 = arrf[5];
                    n2 = Shape.rectCrossingsForCubic(n2, f2, f3, f4, f5, f8, f9, arrf[0], arrf[1], arrf[2], arrf[3], f10, f11, 0);
                    f8 = f10;
                    f9 = f11;
                    break;
                }
                case 4: {
                    if (f8 != f7 || f9 != f6) {
                        n2 = Shape.rectCrossingsForLine(n2, f2, f3, f4, f5, f8, f9, f7, f6);
                    }
                    f8 = f7;
                    f9 = f6;
                }
            }
            pathIterator.next();
        }
        if (n2 != Integer.MIN_VALUE && (f8 != f7 || f9 != f6)) {
            n2 = Shape.rectCrossingsForLine(n2, f2, f3, f4, f5, f8, f9, f7, f6);
        }
        return n2;
    }

    public static int rectCrossingsForLine(int n2, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        if (f7 >= f5 && f9 >= f5) {
            return n2;
        }
        if (f7 <= f3 && f9 <= f3) {
            return n2;
        }
        if (f6 <= f2 && f8 <= f2) {
            return n2;
        }
        if (f6 >= f4 && f8 >= f4) {
            if (f7 < f9) {
                if (f7 <= f3) {
                    ++n2;
                }
                if (f9 >= f5) {
                    ++n2;
                }
            } else if (f9 < f7) {
                if (f9 <= f3) {
                    --n2;
                }
                if (f7 >= f5) {
                    --n2;
                }
            }
            return n2;
        }
        if (f6 > f2 && f6 < f4 && f7 > f3 && f7 < f5 || f8 > f2 && f8 < f4 && f9 > f3 && f9 < f5) {
            return Integer.MIN_VALUE;
        }
        float f10 = f6;
        if (f7 < f3) {
            f10 += (f3 - f7) * (f8 - f6) / (f9 - f7);
        } else if (f7 > f5) {
            f10 += (f5 - f7) * (f8 - f6) / (f9 - f7);
        }
        float f11 = f8;
        if (f9 < f3) {
            f11 += (f3 - f9) * (f6 - f8) / (f7 - f9);
        } else if (f9 > f5) {
            f11 += (f5 - f9) * (f6 - f8) / (f7 - f9);
        }
        if (f10 <= f2 && f11 <= f2) {
            return n2;
        }
        if (f10 >= f4 && f11 >= f4) {
            if (f7 < f9) {
                if (f7 <= f3) {
                    ++n2;
                }
                if (f9 >= f5) {
                    ++n2;
                }
            } else if (f9 < f7) {
                if (f9 <= f3) {
                    --n2;
                }
                if (f7 >= f5) {
                    --n2;
                }
            }
            return n2;
        }
        return Integer.MIN_VALUE;
    }

    public static int rectCrossingsForQuad(int n2, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, int n3) {
        if (f7 >= f5 && f9 >= f5 && f11 >= f5) {
            return n2;
        }
        if (f7 <= f3 && f9 <= f3 && f11 <= f3) {
            return n2;
        }
        if (f6 <= f2 && f8 <= f2 && f10 <= f2) {
            return n2;
        }
        if (f6 >= f4 && f8 >= f4 && f10 >= f4) {
            if (f7 < f11) {
                if (f7 <= f3 && f11 > f3) {
                    ++n2;
                }
                if (f7 < f5 && f11 >= f5) {
                    ++n2;
                }
            } else if (f11 < f7) {
                if (f11 <= f3 && f7 > f3) {
                    --n2;
                }
                if (f11 < f5 && f7 >= f5) {
                    --n2;
                }
            }
            return n2;
        }
        if (f6 < f4 && f6 > f2 && f7 < f5 && f7 > f3 || f10 < f4 && f10 > f2 && f11 < f5 && f11 > f3) {
            return Integer.MIN_VALUE;
        }
        if (n3 > 52) {
            return Shape.rectCrossingsForLine(n2, f2, f3, f4, f5, f6, f7, f10, f11);
        }
        float f12 = (f6 + f8) / 2.0f;
        float f13 = (f7 + f9) / 2.0f;
        float f14 = (f8 + f10) / 2.0f;
        float f15 = (f9 + f11) / 2.0f;
        f8 = (f12 + f14) / 2.0f;
        f9 = (f13 + f15) / 2.0f;
        if (Float.isNaN(f8) || Float.isNaN(f9)) {
            return 0;
        }
        if ((n2 = Shape.rectCrossingsForQuad(n2, f2, f3, f4, f5, f6, f7, f12, f13, f8, f9, n3 + 1)) != Integer.MIN_VALUE) {
            n2 = Shape.rectCrossingsForQuad(n2, f2, f3, f4, f5, f8, f9, f14, f15, f10, f11, n3 + 1);
        }
        return n2;
    }

    public static int rectCrossingsForCubic(int n2, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, int n3) {
        if (f7 >= f5 && f9 >= f5 && f11 >= f5 && f13 >= f5) {
            return n2;
        }
        if (f7 <= f3 && f9 <= f3 && f11 <= f3 && f13 <= f3) {
            return n2;
        }
        if (f6 <= f2 && f8 <= f2 && f10 <= f2 && f12 <= f2) {
            return n2;
        }
        if (f6 >= f4 && f8 >= f4 && f10 >= f4 && f12 >= f4) {
            if (f7 < f13) {
                if (f7 <= f3 && f13 > f3) {
                    ++n2;
                }
                if (f7 < f5 && f13 >= f5) {
                    ++n2;
                }
            } else if (f13 < f7) {
                if (f13 <= f3 && f7 > f3) {
                    --n2;
                }
                if (f13 < f5 && f7 >= f5) {
                    --n2;
                }
            }
            return n2;
        }
        if (f6 > f2 && f6 < f4 && f7 > f3 && f7 < f5 || f12 > f2 && f12 < f4 && f13 > f3 && f13 < f5) {
            return Integer.MIN_VALUE;
        }
        if (n3 > 52) {
            return Shape.rectCrossingsForLine(n2, f2, f3, f4, f5, f6, f7, f12, f13);
        }
        float f14 = (f8 + f10) / 2.0f;
        float f15 = (f9 + f11) / 2.0f;
        f8 = (f6 + f8) / 2.0f;
        f9 = (f7 + f9) / 2.0f;
        f10 = (f10 + f12) / 2.0f;
        f11 = (f11 + f13) / 2.0f;
        float f16 = (f8 + f14) / 2.0f;
        float f17 = (f9 + f15) / 2.0f;
        float f18 = (f14 + f10) / 2.0f;
        float f19 = (f15 + f11) / 2.0f;
        f14 = (f16 + f18) / 2.0f;
        f15 = (f17 + f19) / 2.0f;
        if (Float.isNaN(f14) || Float.isNaN(f15)) {
            return 0;
        }
        if ((n2 = Shape.rectCrossingsForCubic(n2, f2, f3, f4, f5, f6, f7, f8, f9, f16, f17, f14, f15, n3 + 1)) != Integer.MIN_VALUE) {
            n2 = Shape.rectCrossingsForCubic(n2, f2, f3, f4, f5, f14, f15, f18, f19, f10, f11, f12, f13, n3 + 1);
        }
        return n2;
    }

    static boolean intersectsLine(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        int n2;
        int n3 = Shape.outcode(f2, f3, f4, f5, f8, f9);
        if (n3 == 0) {
            return true;
        }
        while ((n2 = Shape.outcode(f2, f3, f4, f5, f6, f7)) != 0) {
            if ((n2 & n3) != 0) {
                return false;
            }
            if ((n2 & 5) != 0) {
                if ((n2 & 4) != 0) {
                    f2 += f4;
                }
                f7 += (f2 - f6) * (f9 - f7) / (f8 - f6);
                f6 = f2;
                continue;
            }
            if ((n2 & 8) != 0) {
                f3 += f5;
            }
            f6 += (f3 - f7) * (f8 - f6) / (f9 - f7);
            f7 = f3;
        }
        return true;
    }

    static int outcode(float f2, float f3, float f4, float f5, float f6, float f7) {
        int n2 = 0;
        if (f4 <= 0.0f) {
            n2 |= 5;
        } else if (f6 < f2) {
            n2 |= 1;
        } else if ((double)f6 > (double)f2 + (double)f4) {
            n2 |= 4;
        }
        if (f5 <= 0.0f) {
            n2 |= 0xA;
        } else if (f7 < f3) {
            n2 |= 2;
        } else if ((double)f7 > (double)f3 + (double)f5) {
            n2 |= 8;
        }
        return n2;
    }

    public static void accumulate(float[] arrf, Shape shape, BaseTransform baseTransform) {
        PathIterator pathIterator = shape.getPathIterator(baseTransform);
        float[] arrf2 = new float[6];
        float f2 = 0.0f;
        float f3 = 0.0f;
        float f4 = 0.0f;
        float f5 = 0.0f;
        while (!pathIterator.isDone()) {
            switch (pathIterator.currentSegment(arrf2)) {
                case 0: {
                    f2 = arrf2[0];
                    f3 = arrf2[1];
                }
                case 1: {
                    f4 = arrf2[0];
                    f5 = arrf2[1];
                    if (arrf[0] > f4) {
                        arrf[0] = f4;
                    }
                    if (arrf[1] > f5) {
                        arrf[1] = f5;
                    }
                    if (arrf[2] < f4) {
                        arrf[2] = f4;
                    }
                    if (!(arrf[3] < f5)) break;
                    arrf[3] = f5;
                    break;
                }
                case 2: {
                    float f6 = arrf2[2];
                    float f7 = arrf2[3];
                    if (arrf[0] > f6) {
                        arrf[0] = f6;
                    }
                    if (arrf[1] > f7) {
                        arrf[1] = f7;
                    }
                    if (arrf[2] < f6) {
                        arrf[2] = f6;
                    }
                    if (arrf[3] < f7) {
                        arrf[3] = f7;
                    }
                    if (arrf[0] > arrf2[0] || arrf[2] < arrf2[0]) {
                        Shape.accumulateQuad(arrf, 0, f4, arrf2[0], f6);
                    }
                    if (arrf[1] > arrf2[1] || arrf[3] < arrf2[1]) {
                        Shape.accumulateQuad(arrf, 1, f5, arrf2[1], f7);
                    }
                    f4 = f6;
                    f5 = f7;
                    break;
                }
                case 3: {
                    float f6 = arrf2[4];
                    float f7 = arrf2[5];
                    if (arrf[0] > f6) {
                        arrf[0] = f6;
                    }
                    if (arrf[1] > f7) {
                        arrf[1] = f7;
                    }
                    if (arrf[2] < f6) {
                        arrf[2] = f6;
                    }
                    if (arrf[3] < f7) {
                        arrf[3] = f7;
                    }
                    if (arrf[0] > arrf2[0] || arrf[2] < arrf2[0] || arrf[0] > arrf2[2] || arrf[2] < arrf2[2]) {
                        Shape.accumulateCubic(arrf, 0, f4, arrf2[0], arrf2[2], f6);
                    }
                    if (arrf[1] > arrf2[1] || arrf[3] < arrf2[1] || arrf[1] > arrf2[3] || arrf[3] < arrf2[3]) {
                        Shape.accumulateCubic(arrf, 1, f5, arrf2[1], arrf2[3], f7);
                    }
                    f4 = f6;
                    f5 = f7;
                    break;
                }
                case 4: {
                    f4 = f2;
                    f5 = f3;
                }
            }
            pathIterator.next();
        }
    }

    public static void accumulateQuad(float[] arrf, int n2, float f2, float f3, float f4) {
        float f5;
        float f6 = f2 - f3;
        float f7 = f4 - f3 + f6;
        if (f7 != 0.0f && (f5 = f6 / f7) > 0.0f && f5 < 1.0f) {
            float f8 = 1.0f - f5;
            float f9 = f2 * f8 * f8 + 2.0f * f3 * f5 * f8 + f4 * f5 * f5;
            if (arrf[n2] > f9) {
                arrf[n2] = f9;
            }
            if (arrf[n2 + 2] < f9) {
                arrf[n2 + 2] = f9;
            }
        }
    }

    public static void accumulateCubic(float[] arrf, int n2, float f2, float f3, float f4, float f5) {
        float f6 = f3 - f2;
        float f7 = 2.0f * (f4 - f3 - f6);
        float f8 = f5 - f4 - f7 - f6;
        if (f8 == 0.0f) {
            if (f7 == 0.0f) {
                return;
            }
            Shape.accumulateCubic(arrf, n2, -f6 / f7, f2, f3, f4, f5);
        } else {
            float f9 = f7 * f7 - 4.0f * f8 * f6;
            if (f9 < 0.0f) {
                return;
            }
            f9 = (float)Math.sqrt(f9);
            if (f7 < 0.0f) {
                f9 = -f9;
            }
            float f10 = (f7 + f9) / -2.0f;
            Shape.accumulateCubic(arrf, n2, f10 / f8, f2, f3, f4, f5);
            if (f10 != 0.0f) {
                Shape.accumulateCubic(arrf, n2, f6 / f10, f2, f3, f4, f5);
            }
        }
    }

    public static void accumulateCubic(float[] arrf, int n2, float f2, float f3, float f4, float f5, float f6) {
        if (f2 > 0.0f && f2 < 1.0f) {
            float f7 = 1.0f - f2;
            float f8 = f3 * f7 * f7 * f7 + 3.0f * f4 * f2 * f7 * f7 + 3.0f * f5 * f2 * f2 * f7 + f6 * f2 * f2 * f2;
            if (arrf[n2] > f8) {
                arrf[n2] = f8;
            }
            if (arrf[n2 + 2] < f8) {
                arrf[n2 + 2] = f8;
            }
        }
    }
}

