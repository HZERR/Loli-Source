/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism;

import com.sun.javafx.geom.Area;
import com.sun.javafx.geom.GeneralShapePair;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathConsumer2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.openpisces.Dasher;
import com.sun.openpisces.Stroker;
import com.sun.prism.impl.shape.OpenPiscesPrismUtils;
import java.util.Arrays;

public final class BasicStroke {
    public static final int CAP_BUTT = 0;
    public static final int CAP_ROUND = 1;
    public static final int CAP_SQUARE = 2;
    public static final int JOIN_BEVEL = 2;
    public static final int JOIN_MITER = 0;
    public static final int JOIN_ROUND = 1;
    public static final int TYPE_CENTERED = 0;
    public static final int TYPE_INNER = 1;
    public static final int TYPE_OUTER = 2;
    float width;
    int type;
    int cap;
    int join;
    float miterLimit;
    float[] dash;
    float dashPhase;
    private static final int SAFE_ACCUMULATE_MASK = 91;
    private float[] tmpMiter = new float[2];
    static final float SQRT_2 = (float)Math.sqrt(2.0);

    public BasicStroke() {
        this.set(0, 1.0f, 2, 0, 10.0f);
    }

    public BasicStroke(float f2, int n2, int n3, float f3) {
        this.set(0, f2, n2, n3, f3);
    }

    public BasicStroke(int n2, float f2, int n3, int n4, float f3) {
        this.set(n2, f2, n3, n4, f3);
    }

    public BasicStroke(float f2, int n2, int n3, float f3, float[] arrf, float f4) {
        this.set(0, f2, n2, n3, f3);
        this.set(arrf, f4);
    }

    public BasicStroke(float f2, int n2, int n3, float f3, double[] arrd, float f4) {
        this.set(0, f2, n2, n3, f3);
        this.set(arrd, f4);
    }

    public BasicStroke(int n2, float f2, int n3, int n4, float f3, float[] arrf, float f4) {
        this.set(n2, f2, n3, n4, f3);
        this.set(arrf, f4);
    }

    public BasicStroke(int n2, float f2, int n3, int n4, float f3, double[] arrd, float f4) {
        this.set(n2, f2, n3, n4, f3);
        this.set(arrd, f4);
    }

    public void set(int n2, float f2, int n3, int n4, float f3) {
        if (n2 != 0 && n2 != 1 && n2 != 2) {
            throw new IllegalArgumentException("illegal type");
        }
        if (f2 < 0.0f) {
            throw new IllegalArgumentException("negative width");
        }
        if (n3 != 0 && n3 != 1 && n3 != 2) {
            throw new IllegalArgumentException("illegal end cap value");
        }
        if (n4 == 0) {
            if (f3 < 1.0f) {
                throw new IllegalArgumentException("miter limit < 1");
            }
        } else if (n4 != 1 && n4 != 2) {
            throw new IllegalArgumentException("illegal line join value");
        }
        this.type = n2;
        this.width = f2;
        this.cap = n3;
        this.join = n4;
        this.miterLimit = f3;
    }

    public void set(float[] arrf, float f2) {
        if (arrf != null) {
            boolean bl = true;
            for (int i2 = 0; i2 < arrf.length; ++i2) {
                float f3 = arrf[i2];
                if ((double)f3 > 0.0) {
                    bl = false;
                    continue;
                }
                if (!((double)f3 < 0.0)) continue;
                throw new IllegalArgumentException("negative dash length");
            }
            if (bl) {
                throw new IllegalArgumentException("dash lengths all zero");
            }
        }
        this.dash = arrf;
        this.dashPhase = f2;
    }

    public void set(double[] arrd, float f2) {
        if (arrd != null) {
            float[] arrf = new float[arrd.length];
            boolean bl = true;
            for (int i2 = 0; i2 < arrd.length; ++i2) {
                float f3 = (float)arrd[i2];
                if ((double)f3 > 0.0) {
                    bl = false;
                } else if ((double)f3 < 0.0) {
                    throw new IllegalArgumentException("negative dash length");
                }
                arrf[i2] = f3;
            }
            if (bl) {
                throw new IllegalArgumentException("dash lengths all zero");
            }
            this.dash = arrf;
        } else {
            this.dash = null;
        }
        this.dashPhase = f2;
    }

    public int getType() {
        return this.type;
    }

    public float getLineWidth() {
        return this.width;
    }

    public int getEndCap() {
        return this.cap;
    }

    public int getLineJoin() {
        return this.join;
    }

    public float getMiterLimit() {
        return this.miterLimit;
    }

    public boolean isDashed() {
        return this.dash != null;
    }

    public float[] getDashArray() {
        return this.dash;
    }

    public float getDashPhase() {
        return this.dashPhase;
    }

    public Shape createStrokedShape(Shape shape) {
        Shape shape2 = shape instanceof RoundRectangle2D ? this.strokeRoundRectangle((RoundRectangle2D)shape) : null;
        if (shape2 != null) {
            return shape2;
        }
        shape2 = this.createCenteredStrokedShape(shape);
        if (this.type == 1) {
            shape2 = this.makeIntersectedShape(shape2, shape);
        } else if (this.type == 2) {
            shape2 = this.makeSubtractedShape(shape2, shape);
        }
        return shape2;
    }

    private boolean isCW(float f2, float f3, float f4, float f5) {
        return f2 * f5 <= f3 * f4;
    }

    private void computeOffset(float f2, float f3, float f4, float[] arrf, int n2) {
        float f5 = (float)Math.sqrt(f2 * f2 + f3 * f3);
        if (f5 == 0.0f) {
            arrf[n2 + 1] = 0.0f;
            arrf[n2 + 0] = 0.0f;
        } else {
            arrf[n2 + 0] = f3 * f4 / f5;
            arrf[n2 + 1] = -(f2 * f4) / f5;
        }
    }

    private void computeMiter(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float[] arrf, int n2) {
        float f10 = f4 - f2;
        float f11 = f5 - f3;
        float f12 = f8 - f6;
        float f13 = f9 - f7;
        float f14 = f10 * f13 - f12 * f11;
        float f15 = f12 * (f3 - f7) - f13 * (f2 - f6);
        arrf[n2++] = f2 + (f15 /= f14) * f10;
        arrf[n2] = f3 + f15 * f11;
    }

    private void accumulateQuad(float[] arrf, int n2, float f2, float f3, float f4, float f5) {
        float f6;
        float f7 = f2 - f3;
        float f8 = f4 - f3 + f7;
        if (f8 != 0.0f && (f6 = f7 / f8) > 0.0f && f6 < 1.0f) {
            float f9 = 1.0f - f6;
            float f10 = f2 * f9 * f9 + 2.0f * f3 * f6 * f9 + f4 * f6 * f6;
            if (arrf[n2] > f10 - f5) {
                arrf[n2] = f10 - f5;
            }
            if (arrf[n2 + 2] < f10 + f5) {
                arrf[n2 + 2] = f10 + f5;
            }
        }
    }

    private void accumulateCubic(float[] arrf, int n2, float f2, float f3, float f4, float f5, float f6, float f7) {
        if (f2 > 0.0f && f2 < 1.0f) {
            float f8 = 1.0f - f2;
            float f9 = f3 * f8 * f8 * f8 + 3.0f * f4 * f2 * f8 * f8 + 3.0f * f5 * f2 * f2 * f8 + f6 * f2 * f2 * f2;
            if (arrf[n2] > f9 - f7) {
                arrf[n2] = f9 - f7;
            }
            if (arrf[n2 + 2] < f9 + f7) {
                arrf[n2 + 2] = f9 + f7;
            }
        }
    }

    private void accumulateCubic(float[] arrf, int n2, float f2, float f3, float f4, float f5, float f6) {
        float f7 = f3 - f2;
        float f8 = 2.0f * (f4 - f3 - f7);
        float f9 = f5 - f4 - f8 - f7;
        if (f9 == 0.0f) {
            if (f8 == 0.0f) {
                return;
            }
            this.accumulateCubic(arrf, n2, -f7 / f8, f2, f3, f4, f5, f6);
        } else {
            float f10 = f8 * f8 - 4.0f * f9 * f7;
            if (f10 < 0.0f) {
                return;
            }
            f10 = (float)Math.sqrt(f10);
            if (f8 < 0.0f) {
                f10 = -f10;
            }
            float f11 = (f8 + f10) / -2.0f;
            this.accumulateCubic(arrf, n2, f11 / f9, f2, f3, f4, f5, f6);
            if (f11 != 0.0f) {
                this.accumulateCubic(arrf, n2, f7 / f11, f2, f3, f4, f5, f6);
            }
        }
    }

    public void accumulateShapeBounds(float[] arrf, Shape shape, BaseTransform baseTransform) {
        if (this.type == 1) {
            Shape.accumulate(arrf, shape, baseTransform);
            return;
        }
        if ((baseTransform.getType() & 0xFFFFFFA4) != 0) {
            Shape.accumulate(arrf, this.createStrokedShape(shape), baseTransform);
            return;
        }
        PathIterator pathIterator = shape.getPathIterator(baseTransform);
        boolean bl = true;
        float[] arrf2 = new float[6];
        float f2 = this.type == 0 ? this.getLineWidth() / 2.0f : this.getLineWidth();
        f2 = (float)((double)f2 * Math.hypot(baseTransform.getMxx(), baseTransform.getMyx()));
        float f3 = 0.0f;
        float f4 = 0.0f;
        float f5 = 0.0f;
        float f6 = 0.0f;
        float f7 = 0.0f;
        float f8 = 0.0f;
        float f9 = 0.0f;
        float f10 = 0.0f;
        float[] arrf3 = new float[4];
        float f11 = 0.0f;
        float f12 = 0.0f;
        float f13 = 0.0f;
        float f14 = 0.0f;
        while (!pathIterator.isDone()) {
            int n2 = pathIterator.currentSegment(arrf2);
            switch (n2) {
                case 0: {
                    if (!bl) {
                        this.accumulateCap(f9, f10, f5, f6, f11, f12, arrf, f2);
                        this.accumulateCap(-f7, -f8, f3, f4, -f13, -f14, arrf, f2);
                    }
                    f5 = f3 = arrf2[0];
                    f6 = f4 = arrf2[1];
                    break;
                }
                case 1: {
                    float f15 = arrf2[0];
                    float f16 = arrf2[1];
                    float f17 = f15 - f5;
                    float f18 = f16 - f6;
                    if (f17 == 0.0f && f18 == 0.0f) {
                        f17 = 1.0f;
                    }
                    this.computeOffset(f17, f18, f2, arrf3, 0);
                    if (!bl) {
                        this.accumulateJoin(f9, f10, f17, f18, f5, f6, f11, f12, arrf3[0], arrf3[1], arrf, f2);
                    }
                    f5 = f15;
                    f6 = f16;
                    f9 = f17;
                    f10 = f18;
                    f11 = arrf3[0];
                    f12 = arrf3[1];
                    if (!bl) break;
                    f7 = f9;
                    f8 = f10;
                    f13 = f11;
                    f14 = f12;
                    break;
                }
                case 2: {
                    float f15 = arrf2[2];
                    float f16 = arrf2[3];
                    float f17 = arrf2[0] - f5;
                    float f18 = arrf2[1] - f6;
                    this.computeOffset(f17, f18, f2, arrf3, 0);
                    if (!bl) {
                        this.accumulateJoin(f9, f10, f17, f18, f5, f6, f11, f12, arrf3[0], arrf3[1], arrf, f2);
                    }
                    if (arrf[0] > arrf2[0] - f2 || arrf[2] < arrf2[0] + f2) {
                        this.accumulateQuad(arrf, 0, f5, arrf2[0], f15, f2);
                    }
                    if (arrf[1] > arrf2[1] - f2 || arrf[3] < arrf2[1] + f2) {
                        this.accumulateQuad(arrf, 1, f6, arrf2[1], f16, f2);
                    }
                    f5 = f15;
                    f6 = f16;
                    if (bl) {
                        f7 = f17;
                        f8 = f18;
                        f13 = arrf3[0];
                        f14 = arrf3[1];
                    }
                    f9 = f15 - arrf2[0];
                    f10 = f16 - arrf2[1];
                    this.computeOffset(f9, f10, f2, arrf3, 0);
                    f11 = arrf3[0];
                    f12 = arrf3[1];
                    break;
                }
                case 3: {
                    float f15 = arrf2[4];
                    float f16 = arrf2[5];
                    float f17 = arrf2[0] - f5;
                    float f18 = arrf2[1] - f6;
                    this.computeOffset(f17, f18, f2, arrf3, 0);
                    if (!bl) {
                        this.accumulateJoin(f9, f10, f17, f18, f5, f6, f11, f12, arrf3[0], arrf3[1], arrf, f2);
                    }
                    if (arrf[0] > arrf2[0] - f2 || arrf[2] < arrf2[0] + f2 || arrf[0] > arrf2[2] - f2 || arrf[2] < arrf2[2] + f2) {
                        this.accumulateCubic(arrf, 0, f5, arrf2[0], arrf2[2], f15, f2);
                    }
                    if (arrf[1] > arrf2[1] - f2 || arrf[3] < arrf2[1] + f2 || arrf[1] > arrf2[3] - f2 || arrf[3] < arrf2[3] + f2) {
                        this.accumulateCubic(arrf, 1, f6, arrf2[1], arrf2[3], f16, f2);
                    }
                    f5 = f15;
                    f6 = f16;
                    if (bl) {
                        f7 = f17;
                        f8 = f18;
                        f13 = arrf3[0];
                        f14 = arrf3[1];
                    }
                    f9 = f15 - arrf2[2];
                    f10 = f16 - arrf2[3];
                    this.computeOffset(f9, f10, f2, arrf3, 0);
                    f11 = arrf3[0];
                    f12 = arrf3[1];
                    break;
                }
                case 4: {
                    float f17 = f3 - f5;
                    float f18 = f4 - f6;
                    float f15 = f3;
                    float f16 = f4;
                    if (!bl) {
                        this.computeOffset(f7, f8, f2, arrf3, 2);
                        if (f17 == 0.0f && f18 == 0.0f) {
                            this.accumulateJoin(f9, f10, f7, f8, f3, f4, f11, f12, arrf3[2], arrf3[3], arrf, f2);
                        } else {
                            this.computeOffset(f17, f18, f2, arrf3, 0);
                            this.accumulateJoin(f9, f10, f17, f18, f5, f6, f11, f12, arrf3[0], arrf3[1], arrf, f2);
                            this.accumulateJoin(f17, f18, f7, f8, f15, f16, arrf3[0], arrf3[1], arrf3[2], arrf3[3], arrf, f2);
                        }
                    }
                    f5 = f15;
                    f6 = f16;
                }
            }
            bl = n2 == 0 || n2 == 4;
            pathIterator.next();
        }
        if (!bl) {
            this.accumulateCap(f9, f10, f5, f6, f11, f12, arrf, f2);
            this.accumulateCap(-f7, -f8, f3, f4, -f13, -f14, arrf, f2);
        }
    }

    private void accumulate(float f2, float f3, float f4, float f5, float[] arrf) {
        if (f2 <= f4) {
            if (f2 < arrf[0]) {
                arrf[0] = f2;
            }
            if (f4 > arrf[2]) {
                arrf[2] = f4;
            }
        } else {
            if (f4 < arrf[0]) {
                arrf[0] = f4;
            }
            if (f2 > arrf[2]) {
                arrf[2] = f2;
            }
        }
        if (f3 <= f5) {
            if (f3 < arrf[1]) {
                arrf[1] = f3;
            }
            if (f5 > arrf[3]) {
                arrf[3] = f5;
            }
        } else {
            if (f5 < arrf[1]) {
                arrf[1] = f5;
            }
            if (f3 > arrf[3]) {
                arrf[3] = f3;
            }
        }
    }

    private void accumulateOrdered(float f2, float f3, float f4, float f5, float[] arrf) {
        if (f2 < arrf[0]) {
            arrf[0] = f2;
        }
        if (f4 > arrf[2]) {
            arrf[2] = f4;
        }
        if (f3 < arrf[1]) {
            arrf[1] = f3;
        }
        if (f5 > arrf[3]) {
            arrf[3] = f5;
        }
    }

    private void accumulateJoin(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float[] arrf, float f12) {
        if (this.join == 2) {
            this.accumulateBevel(f6, f7, f8, f9, f10, f11, arrf);
        } else if (this.join == 0) {
            this.accumulateMiter(f2, f3, f4, f5, f8, f9, f10, f11, f6, f7, arrf, f12);
        } else {
            this.accumulateOrdered(f6 - f12, f7 - f12, f6 + f12, f7 + f12, arrf);
        }
    }

    private void accumulateCap(float f2, float f3, float f4, float f5, float f6, float f7, float[] arrf, float f8) {
        if (this.cap == 2) {
            this.accumulate(f4 + f6 - f7, f5 + f7 + f6, f4 - f6 - f7, f5 - f7 + f6, arrf);
        } else if (this.cap == 0) {
            this.accumulate(f4 + f6, f5 + f7, f4 - f6, f5 - f7, arrf);
        } else {
            this.accumulateOrdered(f4 - f8, f5 - f8, f4 + f8, f5 + f8, arrf);
        }
    }

    private void accumulateMiter(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float[] arrf, float f12) {
        this.accumulateBevel(f10, f11, f6, f7, f8, f9, arrf);
        boolean bl = this.isCW(f2, f3, f4, f5);
        if (bl) {
            f6 = -f6;
            f7 = -f7;
            f8 = -f8;
            f9 = -f9;
        }
        this.computeMiter(f10 - f2 + f6, f11 - f3 + f7, f10 + f6, f11 + f7, f10 + f4 + f8, f11 + f5 + f9, f10 + f8, f11 + f9, this.tmpMiter, 0);
        float f13 = (this.tmpMiter[0] - f10) * (this.tmpMiter[0] - f10) + (this.tmpMiter[1] - f11) * (this.tmpMiter[1] - f11);
        float f14 = this.miterLimit * f12;
        if (f13 < f14 * f14) {
            this.accumulateOrdered(this.tmpMiter[0], this.tmpMiter[1], this.tmpMiter[0], this.tmpMiter[1], arrf);
        }
    }

    private void accumulateBevel(float f2, float f3, float f4, float f5, float f6, float f7, float[] arrf) {
        this.accumulate(f2 + f4, f3 + f5, f2 - f4, f3 - f5, arrf);
        this.accumulate(f2 + f6, f3 + f7, f2 - f6, f3 - f7, arrf);
    }

    public Shape createCenteredStrokedShape(Shape shape) {
        Path2D path2D = new Path2D(1);
        float f2 = this.type == 0 ? this.width : this.width * 2.0f;
        PathConsumer2D pathConsumer2D = new Stroker(path2D, f2, this.cap, this.join, this.miterLimit);
        if (this.dash != null) {
            pathConsumer2D = new Dasher(pathConsumer2D, this.dash, this.dashPhase);
        }
        OpenPiscesPrismUtils.feedConsumer(shape.getPathIterator(null), pathConsumer2D);
        return path2D;
    }

    Shape strokeRoundRectangle(RoundRectangle2D roundRectangle2D) {
        Shape shape;
        float f2;
        float f3;
        int n2;
        if (roundRectangle2D.width < 0.0f || roundRectangle2D.height < 0.0f) {
            return new Path2D();
        }
        if (this.isDashed()) {
            return null;
        }
        float f4 = roundRectangle2D.arcWidth;
        float f5 = roundRectangle2D.arcHeight;
        if (f4 <= 0.0f || f5 <= 0.0f) {
            f5 = 0.0f;
            f4 = 0.0f;
            if (this.type == 1) {
                n2 = 0;
            } else {
                n2 = this.join;
                if (n2 == 0 && this.miterLimit < SQRT_2) {
                    n2 = 2;
                }
            }
        } else {
            if (f4 < f5 * 0.9f || f5 < f4 * 0.9f) {
                return null;
            }
            n2 = 1;
        }
        if (this.type == 1) {
            f3 = 0.0f;
            f2 = this.width;
        } else if (this.type == 2) {
            f3 = this.width;
            f2 = 0.0f;
        } else {
            f3 = f2 = this.width / 2.0f;
        }
        switch (n2) {
            case 0: {
                shape = new RoundRectangle2D(roundRectangle2D.x - f3, roundRectangle2D.y - f3, roundRectangle2D.width + f3 * 2.0f, roundRectangle2D.height + f3 * 2.0f, 0.0f, 0.0f);
                break;
            }
            case 2: {
                shape = BasicStroke.makeBeveledRect(roundRectangle2D.x, roundRectangle2D.y, roundRectangle2D.width, roundRectangle2D.height, f3);
                break;
            }
            case 1: {
                shape = new RoundRectangle2D(roundRectangle2D.x - f3, roundRectangle2D.y - f3, roundRectangle2D.width + f3 * 2.0f, roundRectangle2D.height + f3 * 2.0f, f4 + f3 * 2.0f, f5 + f3 * 2.0f);
                break;
            }
            default: {
                throw new InternalError("Unrecognized line join style");
            }
        }
        if (roundRectangle2D.width <= f2 * 2.0f || roundRectangle2D.height <= f2 * 2.0f) {
            return shape;
        }
        f5 -= f2 * 2.0f;
        if ((f4 -= f2 * 2.0f) <= 0.0f || f5 <= 0.0f) {
            f5 = 0.0f;
            f4 = 0.0f;
        }
        RoundRectangle2D roundRectangle2D2 = new RoundRectangle2D(roundRectangle2D.x + f2, roundRectangle2D.y + f2, roundRectangle2D.width - f2 * 2.0f, roundRectangle2D.height - f2 * 2.0f, f4, f5);
        Path2D path2D = shape instanceof Path2D ? (Path2D)shape : new Path2D(shape);
        path2D.setWindingRule(0);
        path2D.append(roundRectangle2D2, false);
        return path2D;
    }

    static Shape makeBeveledRect(float f2, float f3, float f4, float f5, float f6) {
        float f7 = f2;
        float f8 = f3;
        float f9 = f2 + f4;
        float f10 = f3 + f5;
        Path2D path2D = new Path2D();
        path2D.moveTo(f7, f8 - f6);
        path2D.lineTo(f9, f8 - f6);
        path2D.lineTo(f9 + f6, f8);
        path2D.lineTo(f9 + f6, f10);
        path2D.lineTo(f9, f10 + f6);
        path2D.lineTo(f7, f10 + f6);
        path2D.lineTo(f7 - f6, f10);
        path2D.lineTo(f7 - f6, f8);
        path2D.closePath();
        return path2D;
    }

    protected Shape makeIntersectedShape(Shape shape, Shape shape2) {
        return new CAGShapePair(shape, shape2, 4);
    }

    protected Shape makeSubtractedShape(Shape shape, Shape shape2) {
        return new CAGShapePair(shape, shape2, 1);
    }

    public int hashCode() {
        int n2 = Float.floatToIntBits(this.width);
        n2 = n2 * 31 + this.join;
        n2 = n2 * 31 + this.cap;
        n2 = n2 * 31 + Float.floatToIntBits(this.miterLimit);
        if (this.dash != null) {
            n2 = n2 * 31 + Float.floatToIntBits(this.dashPhase);
            for (int i2 = 0; i2 < this.dash.length; ++i2) {
                n2 = n2 * 31 + Float.floatToIntBits(this.dash[i2]);
            }
        }
        return n2;
    }

    public boolean equals(Object object) {
        if (!(object instanceof BasicStroke)) {
            return false;
        }
        BasicStroke basicStroke = (BasicStroke)object;
        if (this.width != basicStroke.width) {
            return false;
        }
        if (this.join != basicStroke.join) {
            return false;
        }
        if (this.cap != basicStroke.cap) {
            return false;
        }
        if (this.miterLimit != basicStroke.miterLimit) {
            return false;
        }
        if (this.dash != null) {
            if (this.dashPhase != basicStroke.dashPhase) {
                return false;
            }
            if (!Arrays.equals(this.dash, basicStroke.dash)) {
                return false;
            }
        } else if (basicStroke.dash != null) {
            return false;
        }
        return true;
    }

    public BasicStroke copy() {
        return new BasicStroke(this.type, this.width, this.cap, this.join, this.miterLimit, this.dash, this.dashPhase);
    }

    static class CAGShapePair
    extends GeneralShapePair {
        private Shape cagshape;

        public CAGShapePair(Shape shape, Shape shape2, int n2) {
            super(shape, shape2, n2);
        }

        @Override
        public PathIterator getPathIterator(BaseTransform baseTransform) {
            if (this.cagshape == null) {
                Area area = new Area(this.getOuterShape());
                Area area2 = new Area(this.getInnerShape());
                if (this.getCombinationType() == 4) {
                    area.intersect(area2);
                } else {
                    area.subtract(area2);
                }
                this.cagshape = area;
            }
            return this.cagshape.getPathIterator(baseTransform);
        }
    }
}

