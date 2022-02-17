/*
 * Decompiled with CFR 0.150.
 */
package com.sun.openpisces;

import com.sun.javafx.geom.PathConsumer2D;
import com.sun.openpisces.Curve;
import com.sun.openpisces.Helpers;
import java.util.Arrays;

public final class Stroker
implements PathConsumer2D {
    private static final int MOVE_TO = 0;
    private static final int DRAWING_OP_TO = 1;
    private static final int CLOSE = 2;
    public static final int JOIN_MITER = 0;
    public static final int JOIN_ROUND = 1;
    public static final int JOIN_BEVEL = 2;
    public static final int CAP_BUTT = 0;
    public static final int CAP_ROUND = 1;
    public static final int CAP_SQUARE = 2;
    private PathConsumer2D out;
    private int capStyle;
    private int joinStyle;
    private float lineWidth2;
    private final float[][] offset = new float[3][2];
    private final float[] miter = new float[2];
    private float miterLimitSq;
    private int prev;
    private float sx0;
    private float sy0;
    private float sdx;
    private float sdy;
    private float cx0;
    private float cy0;
    private float cdx;
    private float cdy;
    private float smx;
    private float smy;
    private float cmx;
    private float cmy;
    private final PolyStack reverse = new PolyStack();
    private static final float ROUND_JOIN_THRESHOLD = 0.015258789f;
    private float[] middle = new float[88];
    private float[] lp = new float[8];
    private float[] rp = new float[8];
    private static final int MAX_N_CURVES = 11;
    private float[] subdivTs = new float[10];
    private static Curve c = new Curve();

    public Stroker(PathConsumer2D pathConsumer2D, float f2, int n2, int n3, float f3) {
        this(pathConsumer2D);
        this.reset(f2, n2, n3, f3);
    }

    public Stroker(PathConsumer2D pathConsumer2D) {
        this.setConsumer(pathConsumer2D);
    }

    public void setConsumer(PathConsumer2D pathConsumer2D) {
        this.out = pathConsumer2D;
    }

    public void reset(float f2, int n2, int n3, float f3) {
        this.lineWidth2 = f2 / 2.0f;
        this.capStyle = n2;
        this.joinStyle = n3;
        float f4 = f3 * this.lineWidth2;
        this.miterLimitSq = f4 * f4;
        this.prev = 2;
    }

    private static void computeOffset(float f2, float f3, float f4, float[] arrf) {
        float f5 = (float)Math.sqrt(f2 * f2 + f3 * f3);
        if (f5 == 0.0f) {
            arrf[1] = 0.0f;
            arrf[0] = 0.0f;
        } else {
            arrf[0] = f3 * f4 / f5;
            arrf[1] = -(f2 * f4) / f5;
        }
    }

    private static boolean isCW(float f2, float f3, float f4, float f5) {
        return f2 * f5 <= f3 * f4;
    }

    private void drawRoundJoin(float f2, float f3, float f4, float f5, float f6, float f7, boolean bl, float f8) {
        if (f4 == 0.0f && f5 == 0.0f || f6 == 0.0f && f7 == 0.0f) {
            return;
        }
        float f9 = f4 - f6;
        float f10 = f5 - f7;
        float f11 = f9 * f9 + f10 * f10;
        if (f11 < f8) {
            return;
        }
        if (bl) {
            f4 = -f4;
            f5 = -f5;
            f6 = -f6;
            f7 = -f7;
        }
        this.drawRoundJoin(f2, f3, f4, f5, f6, f7, bl);
    }

    private void drawRoundJoin(float f2, float f3, float f4, float f5, float f6, float f7, boolean bl) {
        double d2 = f4 * f6 + f5 * f7;
        int n2 = d2 >= 0.0 ? 1 : 2;
        switch (n2) {
            case 1: {
                this.drawBezApproxForArc(f2, f3, f4, f5, f6, f7, bl);
                break;
            }
            case 2: {
                float f8 = f7 - f5;
                float f9 = f4 - f6;
                float f10 = (float)Math.sqrt(f8 * f8 + f9 * f9);
                float f11 = this.lineWidth2 / f10;
                float f12 = f8 * f11;
                float f13 = f9 * f11;
                if (bl) {
                    f12 = -f12;
                    f13 = -f13;
                }
                this.drawBezApproxForArc(f2, f3, f4, f5, f12, f13, bl);
                this.drawBezApproxForArc(f2, f3, f12, f13, f6, f7, bl);
            }
        }
    }

    private void drawBezApproxForArc(float f2, float f3, float f4, float f5, float f6, float f7, boolean bl) {
        float f8 = (f4 * f6 + f5 * f7) / (2.0f * this.lineWidth2 * this.lineWidth2);
        float f9 = (float)(1.3333333333333333 * Math.sqrt(0.5 - (double)f8) / (1.0 + Math.sqrt((double)f8 + 0.5)));
        if (bl) {
            f9 = -f9;
        }
        float f10 = f2 + f4;
        float f11 = f3 + f5;
        float f12 = f10 - f9 * f5;
        float f13 = f11 + f9 * f4;
        float f14 = f2 + f6;
        float f15 = f3 + f7;
        float f16 = f14 + f9 * f7;
        float f17 = f15 - f9 * f6;
        this.emitCurveTo(f10, f11, f12, f13, f16, f17, f14, f15, bl);
    }

    private void drawRoundCap(float f2, float f3, float f4, float f5) {
        this.emitCurveTo(f2 + f4, f3 + f5, f2 + f4 - 0.5522848f * f5, f3 + f5 + 0.5522848f * f4, f2 - f5 + 0.5522848f * f4, f3 + f4 + 0.5522848f * f5, f2 - f5, f3 + f4, false);
        this.emitCurveTo(f2 - f5, f3 + f4, f2 - f5 - 0.5522848f * f4, f3 + f4 - 0.5522848f * f5, f2 - f4 - 0.5522848f * f5, f3 - f5 + 0.5522848f * f4, f2 - f4, f3 - f5, false);
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

    private void safecomputeMiter(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float[] arrf, int n2) {
        float f10 = f4 - f2;
        float f11 = f9 - f7;
        float f12 = f8 - f6;
        float f13 = f5 - f3;
        float f14 = f10 * f11 - f12 * f13;
        if (f14 == 0.0f) {
            arrf[n2++] = (f2 + f6) / 2.0f;
            arrf[n2] = (f3 + f7) / 2.0f;
            return;
        }
        float f15 = f12 * (f3 - f7) - f11 * (f2 - f6);
        arrf[n2++] = f2 + (f15 /= f14) * f10;
        arrf[n2] = f3 + f15 * f13;
    }

    private void drawMiter(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, boolean bl) {
        if (f10 == f8 && f11 == f9 || f2 == 0.0f && f3 == 0.0f || f6 == 0.0f && f7 == 0.0f) {
            return;
        }
        if (bl) {
            f8 = -f8;
            f9 = -f9;
            f10 = -f10;
            f11 = -f11;
        }
        this.computeMiter(f4 - f2 + f8, f5 - f3 + f9, f4 + f8, f5 + f9, f6 + f4 + f10, f7 + f5 + f11, f4 + f10, f5 + f11, this.miter, 0);
        float f12 = (this.miter[0] - f4) * (this.miter[0] - f4) + (this.miter[1] - f5) * (this.miter[1] - f5);
        if (f12 < this.miterLimitSq) {
            this.emitLineTo(this.miter[0], this.miter[1], bl);
        }
    }

    @Override
    public void moveTo(float f2, float f3) {
        if (this.prev == 1) {
            this.finish();
        }
        this.sx0 = this.cx0 = f2;
        this.sy0 = this.cy0 = f3;
        this.sdx = 1.0f;
        this.cdx = 1.0f;
        this.sdy = 0.0f;
        this.cdy = 0.0f;
        this.prev = 0;
    }

    @Override
    public void lineTo(float f2, float f3) {
        float f4 = f2 - this.cx0;
        float f5 = f3 - this.cy0;
        if (f4 == 0.0f && f5 == 0.0f) {
            f4 = 1.0f;
        }
        Stroker.computeOffset(f4, f5, this.lineWidth2, this.offset[0]);
        float f6 = this.offset[0][0];
        float f7 = this.offset[0][1];
        this.drawJoin(this.cdx, this.cdy, this.cx0, this.cy0, f4, f5, this.cmx, this.cmy, f6, f7);
        this.emitLineTo(this.cx0 + f6, this.cy0 + f7);
        this.emitLineTo(f2 + f6, f3 + f7);
        this.emitLineTo(this.cx0 - f6, this.cy0 - f7, true);
        this.emitLineTo(f2 - f6, f3 - f7, true);
        this.cmx = f6;
        this.cmy = f7;
        this.cdx = f4;
        this.cdy = f5;
        this.cx0 = f2;
        this.cy0 = f3;
        this.prev = 1;
    }

    @Override
    public void closePath() {
        if (this.prev != 1) {
            if (this.prev == 2) {
                return;
            }
            this.emitMoveTo(this.cx0, this.cy0 - this.lineWidth2);
            this.smx = 0.0f;
            this.cmx = 0.0f;
            this.cmy = this.smy = -this.lineWidth2;
            this.sdx = 1.0f;
            this.cdx = 1.0f;
            this.sdy = 0.0f;
            this.cdy = 0.0f;
            this.finish();
            return;
        }
        if (this.cx0 != this.sx0 || this.cy0 != this.sy0) {
            this.lineTo(this.sx0, this.sy0);
        }
        this.drawJoin(this.cdx, this.cdy, this.cx0, this.cy0, this.sdx, this.sdy, this.cmx, this.cmy, this.smx, this.smy);
        this.emitLineTo(this.sx0 + this.smx, this.sy0 + this.smy);
        this.emitMoveTo(this.sx0 - this.smx, this.sy0 - this.smy);
        this.emitReverse();
        this.prev = 2;
        this.emitClose();
    }

    private void emitReverse() {
        while (!this.reverse.isEmpty()) {
            this.reverse.pop(this.out);
        }
    }

    @Override
    public void pathDone() {
        if (this.prev == 1) {
            this.finish();
        }
        this.out.pathDone();
        this.prev = 2;
    }

    private void finish() {
        if (this.capStyle == 1) {
            this.drawRoundCap(this.cx0, this.cy0, this.cmx, this.cmy);
        } else if (this.capStyle == 2) {
            this.emitLineTo(this.cx0 - this.cmy + this.cmx, this.cy0 + this.cmx + this.cmy);
            this.emitLineTo(this.cx0 - this.cmy - this.cmx, this.cy0 + this.cmx - this.cmy);
        }
        this.emitReverse();
        if (this.capStyle == 1) {
            this.drawRoundCap(this.sx0, this.sy0, -this.smx, -this.smy);
        } else if (this.capStyle == 2) {
            this.emitLineTo(this.sx0 + this.smy - this.smx, this.sy0 - this.smx - this.smy);
            this.emitLineTo(this.sx0 + this.smy + this.smx, this.sy0 - this.smx + this.smy);
        }
        this.emitClose();
    }

    private void emitMoveTo(float f2, float f3) {
        this.out.moveTo(f2, f3);
    }

    private void emitLineTo(float f2, float f3) {
        this.out.lineTo(f2, f3);
    }

    private void emitLineTo(float f2, float f3, boolean bl) {
        if (bl) {
            this.reverse.pushLine(f2, f3);
        } else {
            this.emitLineTo(f2, f3);
        }
    }

    private void emitQuadTo(float f2, float f3, float f4, float f5, float f6, float f7, boolean bl) {
        if (bl) {
            this.reverse.pushQuad(f2, f3, f4, f5);
        } else {
            this.out.quadTo(f4, f5, f6, f7);
        }
    }

    private void emitCurveTo(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, boolean bl) {
        if (bl) {
            this.reverse.pushCubic(f2, f3, f4, f5, f6, f7);
        } else {
            this.out.curveTo(f4, f5, f6, f7, f8, f9);
        }
    }

    private void emitClose() {
        this.out.closePath();
    }

    private void drawJoin(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11) {
        if (this.prev != 1) {
            this.emitMoveTo(f4 + f10, f5 + f11);
            this.sdx = f6;
            this.sdy = f7;
            this.smx = f10;
            this.smy = f11;
        } else {
            boolean bl = Stroker.isCW(f2, f3, f6, f7);
            if (this.joinStyle == 0) {
                this.drawMiter(f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, bl);
            } else if (this.joinStyle == 1) {
                this.drawRoundJoin(f4, f5, f8, f9, f10, f11, bl, 0.015258789f);
            }
            this.emitLineTo(f4, f5, !bl);
        }
        this.prev = 1;
    }

    private static boolean within(float f2, float f3, float f4, float f5, float f6) {
        assert (f6 > 0.0f) : "";
        return Helpers.within(f2, f4, f6) && Helpers.within(f3, f5, f6);
    }

    private void getLineOffsets(float f2, float f3, float f4, float f5, float[] arrf, float[] arrf2) {
        Stroker.computeOffset(f4 - f2, f5 - f3, this.lineWidth2, this.offset[0]);
        arrf[0] = f2 + this.offset[0][0];
        arrf[1] = f3 + this.offset[0][1];
        arrf[2] = f4 + this.offset[0][0];
        arrf[3] = f5 + this.offset[0][1];
        arrf2[0] = f2 - this.offset[0][0];
        arrf2[1] = f3 - this.offset[0][1];
        arrf2[2] = f4 - this.offset[0][0];
        arrf2[3] = f5 - this.offset[0][1];
    }

    private int computeOffsetCubic(float[] arrf, int n2, float[] arrf2, float[] arrf3) {
        float f2 = arrf[n2 + 0];
        float f3 = arrf[n2 + 1];
        float f4 = arrf[n2 + 2];
        float f5 = arrf[n2 + 3];
        float f6 = arrf[n2 + 4];
        float f7 = arrf[n2 + 5];
        float f8 = arrf[n2 + 6];
        float f9 = arrf[n2 + 7];
        float f10 = f8 - f6;
        float f11 = f9 - f7;
        float f12 = f4 - f2;
        float f13 = f5 - f3;
        boolean bl = Stroker.within(f2, f3, f4, f5, 6.0f * Math.ulp(f5));
        boolean bl2 = Stroker.within(f6, f7, f8, f9, 6.0f * Math.ulp(f9));
        if (bl && bl2) {
            this.getLineOffsets(f2, f3, f8, f9, arrf2, arrf3);
            return 4;
        }
        if (bl) {
            f12 = f6 - f2;
            f13 = f7 - f3;
        } else if (bl2) {
            f10 = f8 - f4;
            f11 = f9 - f5;
        }
        float f14 = f12 * f10 + f13 * f11;
        f14 *= f14;
        float f15 = f12 * f12 + f13 * f13;
        float f16 = f10 * f10 + f11 * f11;
        if (Helpers.within(f14, f15 * f16, 4.0f * Math.ulp(f14))) {
            this.getLineOffsets(f2, f3, f8, f9, arrf2, arrf3);
            return 4;
        }
        float f17 = 0.125f * (f2 + 3.0f * (f4 + f6) + f8);
        float f18 = 0.125f * (f3 + 3.0f * (f5 + f7) + f9);
        float f19 = f6 + f8 - f2 - f4;
        float f20 = f7 + f9 - f3 - f5;
        Stroker.computeOffset(f12, f13, this.lineWidth2, this.offset[0]);
        Stroker.computeOffset(f19, f20, this.lineWidth2, this.offset[1]);
        Stroker.computeOffset(f10, f11, this.lineWidth2, this.offset[2]);
        float f21 = f2 + this.offset[0][0];
        float f22 = f3 + this.offset[0][1];
        float f23 = f17 + this.offset[1][0];
        float f24 = f18 + this.offset[1][1];
        float f25 = f8 + this.offset[2][0];
        float f26 = f9 + this.offset[2][1];
        float f27 = 4.0f / (3.0f * (f12 * f11 - f13 * f10));
        float f28 = 2.0f * f23 - f21 - f25;
        float f29 = 2.0f * f24 - f22 - f26;
        float f30 = f27 * (f11 * f28 - f10 * f29);
        float f31 = f27 * (f12 * f29 - f13 * f28);
        float f32 = f21 + f30 * f12;
        float f33 = f22 + f30 * f13;
        float f34 = f25 + f31 * f10;
        float f35 = f26 + f31 * f11;
        arrf2[0] = f21;
        arrf2[1] = f22;
        arrf2[2] = f32;
        arrf2[3] = f33;
        arrf2[4] = f34;
        arrf2[5] = f35;
        arrf2[6] = f25;
        arrf2[7] = f26;
        f21 = f2 - this.offset[0][0];
        f22 = f3 - this.offset[0][1];
        f25 = f8 - this.offset[2][0];
        f26 = f9 - this.offset[2][1];
        f28 = 2.0f * (f23 -= 2.0f * this.offset[1][0]) - f21 - f25;
        f29 = 2.0f * (f24 -= 2.0f * this.offset[1][1]) - f22 - f26;
        f30 = f27 * (f11 * f28 - f10 * f29);
        f31 = f27 * (f12 * f29 - f13 * f28);
        f32 = f21 + f30 * f12;
        f33 = f22 + f30 * f13;
        f34 = f25 + f31 * f10;
        f35 = f26 + f31 * f11;
        arrf3[0] = f21;
        arrf3[1] = f22;
        arrf3[2] = f32;
        arrf3[3] = f33;
        arrf3[4] = f34;
        arrf3[5] = f35;
        arrf3[6] = f25;
        arrf3[7] = f26;
        return 8;
    }

    private int computeOffsetQuad(float[] arrf, int n2, float[] arrf2, float[] arrf3) {
        float f2 = arrf[n2 + 0];
        float f3 = arrf[n2 + 1];
        float f4 = arrf[n2 + 2];
        float f5 = arrf[n2 + 3];
        float f6 = arrf[n2 + 4];
        float f7 = arrf[n2 + 5];
        float f8 = f6 - f4;
        float f9 = f7 - f5;
        float f10 = f4 - f2;
        float f11 = f5 - f3;
        boolean bl = Stroker.within(f2, f3, f4, f5, 6.0f * Math.ulp(f5));
        boolean bl2 = Stroker.within(f4, f5, f6, f7, 6.0f * Math.ulp(f7));
        if (bl || bl2) {
            this.getLineOffsets(f2, f3, f6, f7, arrf2, arrf3);
            return 4;
        }
        float f12 = f10 * f8 + f11 * f9;
        float f13 = f10 * f10 + f11 * f11;
        float f14 = f8 * f8 + f9 * f9;
        if (Helpers.within(f12 *= f12, f13 * f14, 4.0f * Math.ulp(f12))) {
            this.getLineOffsets(f2, f3, f6, f7, arrf2, arrf3);
            return 4;
        }
        Stroker.computeOffset(f10, f11, this.lineWidth2, this.offset[0]);
        Stroker.computeOffset(f8, f9, this.lineWidth2, this.offset[1]);
        float f15 = f2 + this.offset[0][0];
        float f16 = f3 + this.offset[0][1];
        float f17 = f6 + this.offset[1][0];
        float f18 = f7 + this.offset[1][1];
        this.safecomputeMiter(f15, f16, f15 + f10, f16 + f11, f17, f18, f17 - f8, f18 - f9, arrf2, 2);
        arrf2[0] = f15;
        arrf2[1] = f16;
        arrf2[4] = f17;
        arrf2[5] = f18;
        f15 = f2 - this.offset[0][0];
        f16 = f3 - this.offset[0][1];
        f17 = f6 - this.offset[1][0];
        f18 = f7 - this.offset[1][1];
        this.safecomputeMiter(f15, f16, f15 + f10, f16 + f11, f17, f18, f17 - f8, f18 - f9, arrf3, 2);
        arrf3[0] = f15;
        arrf3[1] = f16;
        arrf3[4] = f17;
        arrf3[5] = f18;
        return 6;
    }

    private static int findSubdivPoints(float[] arrf, float[] arrf2, int n2, float f2) {
        float f3 = arrf[2] - arrf[0];
        float f4 = arrf[3] - arrf[1];
        if (f4 != 0.0f && f3 != 0.0f) {
            float f5 = (float)Math.sqrt(f3 * f3 + f4 * f4);
            float f6 = f3 / f5;
            float f7 = f4 / f5;
            float f8 = f6 * arrf[0] + f7 * arrf[1];
            float f9 = f6 * arrf[1] - f7 * arrf[0];
            float f10 = f6 * arrf[2] + f7 * arrf[3];
            float f11 = f6 * arrf[3] - f7 * arrf[2];
            float f12 = f6 * arrf[4] + f7 * arrf[5];
            float f13 = f6 * arrf[5] - f7 * arrf[4];
            switch (n2) {
                case 8: {
                    float f14 = f6 * arrf[6] + f7 * arrf[7];
                    float f15 = f6 * arrf[7] - f7 * arrf[6];
                    c.set(f8, f9, f10, f11, f12, f13, f14, f15);
                    break;
                }
                case 6: {
                    c.set(f8, f9, f10, f11, f12, f13);
                }
            }
        } else {
            c.set(arrf, n2);
        }
        int n3 = 0;
        n3 += c.dxRoots(arrf2, n3);
        n3 += c.dyRoots(arrf2, n3);
        if (n2 == 8) {
            n3 += c.infPoints(arrf2, n3);
        }
        n3 += c.rootsOfROCMinusW(arrf2, n3, f2, 1.0E-4f);
        n3 = Helpers.filterOutNotInAB(arrf2, 0, n3, 1.0E-4f, 0.9999f);
        Helpers.isort(arrf2, 0, n3);
        return n3;
    }

    @Override
    public void curveTo(float f2, float f3, float f4, float f5, float f6, float f7) {
        int n2;
        float f8;
        boolean bl;
        this.middle[0] = this.cx0;
        this.middle[1] = this.cy0;
        this.middle[2] = f2;
        this.middle[3] = f3;
        this.middle[4] = f4;
        this.middle[5] = f5;
        this.middle[6] = f6;
        this.middle[7] = f7;
        float f9 = this.middle[6];
        float f10 = this.middle[7];
        float f11 = this.middle[2] - this.middle[0];
        float f12 = this.middle[3] - this.middle[1];
        float f13 = this.middle[6] - this.middle[4];
        float f14 = this.middle[7] - this.middle[5];
        boolean bl2 = f11 == 0.0f && f12 == 0.0f;
        boolean bl3 = bl = f13 == 0.0f && f14 == 0.0f;
        if (bl2) {
            f11 = this.middle[4] - this.middle[0];
            f12 = this.middle[5] - this.middle[1];
            if (f11 == 0.0f && f12 == 0.0f) {
                f11 = this.middle[6] - this.middle[0];
                f12 = this.middle[7] - this.middle[1];
            }
        }
        if (bl) {
            f13 = this.middle[6] - this.middle[2];
            f14 = this.middle[7] - this.middle[3];
            if (f13 == 0.0f && f14 == 0.0f) {
                f13 = this.middle[6] - this.middle[0];
                f14 = this.middle[7] - this.middle[1];
            }
        }
        if (f11 == 0.0f && f12 == 0.0f) {
            this.lineTo(this.middle[0], this.middle[1]);
            return;
        }
        if (Math.abs(f11) < 0.1f && Math.abs(f12) < 0.1f) {
            f8 = (float)Math.sqrt(f11 * f11 + f12 * f12);
            f11 /= f8;
            f12 /= f8;
        }
        if (Math.abs(f13) < 0.1f && Math.abs(f14) < 0.1f) {
            f8 = (float)Math.sqrt(f13 * f13 + f14 * f14);
            f13 /= f8;
            f14 /= f8;
        }
        Stroker.computeOffset(f11, f12, this.lineWidth2, this.offset[0]);
        f8 = this.offset[0][0];
        float f15 = this.offset[0][1];
        this.drawJoin(this.cdx, this.cdy, this.cx0, this.cy0, f11, f12, this.cmx, this.cmy, f8, f15);
        int n3 = Stroker.findSubdivPoints(this.middle, this.subdivTs, 8, this.lineWidth2);
        float f16 = 0.0f;
        for (n2 = 0; n2 < n3; ++n2) {
            float f17 = this.subdivTs[n2];
            Helpers.subdivideCubicAt((f17 - f16) / (1.0f - f16), this.middle, n2 * 6, this.middle, n2 * 6, this.middle, n2 * 6 + 6);
            f16 = f17;
        }
        n2 = 0;
        for (int i2 = 0; i2 <= n3; ++i2) {
            n2 = this.computeOffsetCubic(this.middle, i2 * 6, this.lp, this.rp);
            if (n2 == 0) continue;
            this.emitLineTo(this.lp[0], this.lp[1]);
            switch (n2) {
                case 8: {
                    this.emitCurveTo(this.lp[0], this.lp[1], this.lp[2], this.lp[3], this.lp[4], this.lp[5], this.lp[6], this.lp[7], false);
                    this.emitCurveTo(this.rp[0], this.rp[1], this.rp[2], this.rp[3], this.rp[4], this.rp[5], this.rp[6], this.rp[7], true);
                    break;
                }
                case 4: {
                    this.emitLineTo(this.lp[2], this.lp[3]);
                    this.emitLineTo(this.rp[0], this.rp[1], true);
                }
            }
            this.emitLineTo(this.rp[n2 - 2], this.rp[n2 - 1], true);
        }
        this.cmx = (this.lp[n2 - 2] - this.rp[n2 - 2]) / 2.0f;
        this.cmy = (this.lp[n2 - 1] - this.rp[n2 - 1]) / 2.0f;
        this.cdx = f13;
        this.cdy = f14;
        this.cx0 = f9;
        this.cy0 = f10;
        this.prev = 1;
    }

    @Override
    public void quadTo(float f2, float f3, float f4, float f5) {
        int n2;
        float f6;
        this.middle[0] = this.cx0;
        this.middle[1] = this.cy0;
        this.middle[2] = f2;
        this.middle[3] = f3;
        this.middle[4] = f4;
        this.middle[5] = f5;
        float f7 = this.middle[4];
        float f8 = this.middle[5];
        float f9 = this.middle[2] - this.middle[0];
        float f10 = this.middle[3] - this.middle[1];
        float f11 = this.middle[4] - this.middle[2];
        float f12 = this.middle[5] - this.middle[3];
        if (f9 == 0.0f && f10 == 0.0f || f11 == 0.0f && f12 == 0.0f) {
            f9 = f11 = this.middle[4] - this.middle[0];
            f10 = f12 = this.middle[5] - this.middle[1];
        }
        if (f9 == 0.0f && f10 == 0.0f) {
            this.lineTo(this.middle[0], this.middle[1]);
            return;
        }
        if (Math.abs(f9) < 0.1f && Math.abs(f10) < 0.1f) {
            f6 = (float)Math.sqrt(f9 * f9 + f10 * f10);
            f9 /= f6;
            f10 /= f6;
        }
        if (Math.abs(f11) < 0.1f && Math.abs(f12) < 0.1f) {
            f6 = (float)Math.sqrt(f11 * f11 + f12 * f12);
            f11 /= f6;
            f12 /= f6;
        }
        Stroker.computeOffset(f9, f10, this.lineWidth2, this.offset[0]);
        f6 = this.offset[0][0];
        float f13 = this.offset[0][1];
        this.drawJoin(this.cdx, this.cdy, this.cx0, this.cy0, f9, f10, this.cmx, this.cmy, f6, f13);
        int n3 = Stroker.findSubdivPoints(this.middle, this.subdivTs, 6, this.lineWidth2);
        float f14 = 0.0f;
        for (n2 = 0; n2 < n3; ++n2) {
            float f15 = this.subdivTs[n2];
            Helpers.subdivideQuadAt((f15 - f14) / (1.0f - f14), this.middle, n2 * 4, this.middle, n2 * 4, this.middle, n2 * 4 + 4);
            f14 = f15;
        }
        n2 = 0;
        for (int i2 = 0; i2 <= n3; ++i2) {
            n2 = this.computeOffsetQuad(this.middle, i2 * 4, this.lp, this.rp);
            if (n2 == 0) continue;
            this.emitLineTo(this.lp[0], this.lp[1]);
            switch (n2) {
                case 6: {
                    this.emitQuadTo(this.lp[0], this.lp[1], this.lp[2], this.lp[3], this.lp[4], this.lp[5], false);
                    this.emitQuadTo(this.rp[0], this.rp[1], this.rp[2], this.rp[3], this.rp[4], this.rp[5], true);
                    break;
                }
                case 4: {
                    this.emitLineTo(this.lp[2], this.lp[3]);
                    this.emitLineTo(this.rp[0], this.rp[1], true);
                }
            }
            this.emitLineTo(this.rp[n2 - 2], this.rp[n2 - 1], true);
        }
        this.cmx = (this.lp[n2 - 2] - this.rp[n2 - 2]) / 2.0f;
        this.cmy = (this.lp[n2 - 1] - this.rp[n2 - 1]) / 2.0f;
        this.cdx = f11;
        this.cdy = f12;
        this.cx0 = f7;
        this.cy0 = f8;
        this.prev = 1;
    }

    private static final class PolyStack {
        float[] curves = new float[400];
        int end = 0;
        int[] curveTypes = new int[50];
        int numCurves = 0;
        private static final int INIT_SIZE = 50;

        PolyStack() {
        }

        public boolean isEmpty() {
            return this.numCurves == 0;
        }

        private void ensureSpace(int n2) {
            int n3;
            if (this.end + n2 >= this.curves.length) {
                n3 = (this.end + n2) * 2;
                this.curves = Arrays.copyOf(this.curves, n3);
            }
            if (this.numCurves >= this.curveTypes.length) {
                n3 = this.numCurves * 2;
                this.curveTypes = Arrays.copyOf(this.curveTypes, n3);
            }
        }

        public void pushCubic(float f2, float f3, float f4, float f5, float f6, float f7) {
            this.ensureSpace(6);
            this.curveTypes[this.numCurves++] = 8;
            this.curves[this.end++] = f6;
            this.curves[this.end++] = f7;
            this.curves[this.end++] = f4;
            this.curves[this.end++] = f5;
            this.curves[this.end++] = f2;
            this.curves[this.end++] = f3;
        }

        public void pushQuad(float f2, float f3, float f4, float f5) {
            this.ensureSpace(4);
            this.curveTypes[this.numCurves++] = 6;
            this.curves[this.end++] = f4;
            this.curves[this.end++] = f5;
            this.curves[this.end++] = f2;
            this.curves[this.end++] = f3;
        }

        public void pushLine(float f2, float f3) {
            this.ensureSpace(2);
            this.curveTypes[this.numCurves++] = 4;
            this.curves[this.end++] = f2;
            this.curves[this.end++] = f3;
        }

        public int pop(float[] arrf) {
            int n2 = this.curveTypes[this.numCurves - 1];
            --this.numCurves;
            this.end -= n2 - 2;
            System.arraycopy(this.curves, this.end, arrf, 0, n2 - 2);
            return n2;
        }

        public void pop(PathConsumer2D pathConsumer2D) {
            --this.numCurves;
            int n2 = this.curveTypes[this.numCurves];
            this.end -= n2 - 2;
            switch (n2) {
                case 8: {
                    pathConsumer2D.curveTo(this.curves[this.end + 0], this.curves[this.end + 1], this.curves[this.end + 2], this.curves[this.end + 3], this.curves[this.end + 4], this.curves[this.end + 5]);
                    break;
                }
                case 6: {
                    pathConsumer2D.quadTo(this.curves[this.end + 0], this.curves[this.end + 1], this.curves[this.end + 2], this.curves[this.end + 3]);
                    break;
                }
                case 4: {
                    pathConsumer2D.lineTo(this.curves[this.end], this.curves[this.end + 1]);
                }
            }
        }

        public String toString() {
            String string = "";
            int n2 = this.numCurves;
            int n3 = this.end;
            while (n2 > 0) {
                --n2;
                int n4 = this.curveTypes[this.numCurves];
                n3 -= n4 - 2;
                switch (n4) {
                    case 8: {
                        string = string + "cubic: ";
                        break;
                    }
                    case 6: {
                        string = string + "quad: ";
                        break;
                    }
                    case 4: {
                        string = string + "line: ";
                    }
                }
                string = string + Arrays.toString(Arrays.copyOfRange(this.curves, n3, n3 + n4 - 2)) + "\n";
            }
            return string;
        }
    }
}

