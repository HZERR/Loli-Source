/*
 * Decompiled with CFR 0.150.
 */
package com.sun.openpisces;

import com.sun.javafx.geom.PathConsumer2D;
import com.sun.openpisces.Helpers;

public final class Dasher
implements PathConsumer2D {
    private final PathConsumer2D out;
    private float[] dash;
    private float startPhase;
    private boolean startDashOn;
    private int startIdx;
    private boolean starting;
    private boolean needsMoveTo;
    private int idx;
    private boolean dashOn;
    private float phase;
    private float sx;
    private float sy;
    private float x0;
    private float y0;
    private float[] curCurvepts;
    static float MAX_CYCLES = 1.6E7f;
    private float[] firstSegmentsBuffer = new float[7];
    private int firstSegidx = 0;
    private LengthIterator li = null;

    public Dasher(PathConsumer2D pathConsumer2D, float[] arrf, float f2) {
        this(pathConsumer2D);
        this.reset(arrf, f2);
    }

    public Dasher(PathConsumer2D pathConsumer2D) {
        this.out = pathConsumer2D;
        this.curCurvepts = new float[16];
    }

    public void reset(float[] arrf, float f2) {
        int n2;
        int n3 = 0;
        this.dashOn = true;
        float f3 = 0.0f;
        for (float f4 : arrf) {
            f3 += f4;
        }
        float f5 = f2 / f3;
        if (f2 < 0.0f) {
            if (-f5 >= MAX_CYCLES) {
                f2 = 0.0f;
            } else {
                n2 = (int)Math.floor(-f5);
                if ((n2 & arrf.length & 1) != 0) {
                    this.dashOn = !this.dashOn;
                }
                f2 += (float)n2 * f3;
                while (f2 < 0.0f) {
                    if (--n3 < 0) {
                        n3 = arrf.length - 1;
                    }
                    f2 += arrf[n3];
                    this.dashOn = !this.dashOn;
                }
            }
        } else if (f2 > 0.0f) {
            if (f5 >= MAX_CYCLES) {
                f2 = 0.0f;
            } else {
                n2 = (int)Math.floor(f5);
                if ((n2 & arrf.length & 1) != 0) {
                    this.dashOn = !this.dashOn;
                }
                f2 -= (float)n2 * f3;
                while (true) {
                    float f6;
                    float f7 = arrf[n3];
                    if (!(f2 >= f6)) break;
                    f2 -= f7;
                    n3 = (n3 + 1) % arrf.length;
                    this.dashOn = !this.dashOn;
                }
            }
        }
        this.dash = arrf;
        this.startPhase = this.phase = f2;
        this.startDashOn = this.dashOn;
        this.startIdx = n3;
        this.starting = true;
    }

    @Override
    public void moveTo(float f2, float f3) {
        if (this.firstSegidx > 0) {
            this.out.moveTo(this.sx, this.sy);
            this.emitFirstSegments();
        }
        this.needsMoveTo = true;
        this.idx = this.startIdx;
        this.dashOn = this.startDashOn;
        this.phase = this.startPhase;
        this.sx = this.x0 = f2;
        this.sy = this.y0 = f3;
        this.starting = true;
    }

    private void emitSeg(float[] arrf, int n2, int n3) {
        switch (n3) {
            case 8: {
                this.out.curveTo(arrf[n2 + 0], arrf[n2 + 1], arrf[n2 + 2], arrf[n2 + 3], arrf[n2 + 4], arrf[n2 + 5]);
                break;
            }
            case 6: {
                this.out.quadTo(arrf[n2 + 0], arrf[n2 + 1], arrf[n2 + 2], arrf[n2 + 3]);
                break;
            }
            case 4: {
                this.out.lineTo(arrf[n2], arrf[n2 + 1]);
            }
        }
    }

    private void emitFirstSegments() {
        for (int i2 = 0; i2 < this.firstSegidx; i2 += (int)this.firstSegmentsBuffer[i2] - 1) {
            this.emitSeg(this.firstSegmentsBuffer, i2 + 1, (int)this.firstSegmentsBuffer[i2]);
        }
        this.firstSegidx = 0;
    }

    private void goTo(float[] arrf, int n2, int n3) {
        float f2 = arrf[n2 + n3 - 4];
        float f3 = arrf[n2 + n3 - 3];
        if (this.dashOn) {
            if (this.starting) {
                this.firstSegmentsBuffer = Helpers.widenArray(this.firstSegmentsBuffer, this.firstSegidx, n3 - 1);
                this.firstSegmentsBuffer[this.firstSegidx++] = n3;
                System.arraycopy(arrf, n2, this.firstSegmentsBuffer, this.firstSegidx, n3 - 2);
                this.firstSegidx += n3 - 2;
            } else {
                if (this.needsMoveTo) {
                    this.out.moveTo(this.x0, this.y0);
                    this.needsMoveTo = false;
                }
                this.emitSeg(arrf, n2, n3);
            }
        } else {
            this.starting = false;
            this.needsMoveTo = true;
        }
        this.x0 = f2;
        this.y0 = f3;
    }

    @Override
    public void lineTo(float f2, float f3) {
        float f4 = f2 - this.x0;
        float f5 = f3 - this.y0;
        float f6 = (float)Math.sqrt(f4 * f4 + f5 * f5);
        if (f6 == 0.0f) {
            return;
        }
        float f7 = f4 / f6;
        float f8 = f5 / f6;
        while (true) {
            float f9;
            if (f6 <= (f9 = this.dash[this.idx] - this.phase)) {
                this.curCurvepts[0] = f2;
                this.curCurvepts[1] = f3;
                this.goTo(this.curCurvepts, 0, 4);
                this.phase += f6;
                if (f6 == f9) {
                    this.phase = 0.0f;
                    this.idx = (this.idx + 1) % this.dash.length;
                    this.dashOn = !this.dashOn;
                }
                return;
            }
            float f10 = this.dash[this.idx] * f7;
            float f11 = this.dash[this.idx] * f8;
            if (this.phase == 0.0f) {
                this.curCurvepts[0] = this.x0 + f10;
                this.curCurvepts[1] = this.y0 + f11;
            } else {
                float f12 = f9 / this.dash[this.idx];
                this.curCurvepts[0] = this.x0 + f12 * f10;
                this.curCurvepts[1] = this.y0 + f12 * f11;
            }
            this.goTo(this.curCurvepts, 0, 4);
            f6 -= f9;
            this.idx = (this.idx + 1) % this.dash.length;
            this.dashOn = !this.dashOn;
            this.phase = 0.0f;
        }
    }

    private void somethingTo(int n2) {
        if (Dasher.pointCurve(this.curCurvepts, n2)) {
            return;
        }
        if (this.li == null) {
            this.li = new LengthIterator(4, 0.01f);
        }
        this.li.initializeIterationOnCurve(this.curCurvepts, n2);
        int n3 = 0;
        float f2 = 0.0f;
        float f3 = 0.0f;
        float f4 = this.dash[this.idx] - this.phase;
        while (true) {
            float f5;
            f3 = this.li.next(f4);
            if (!(f5 < 1.0f)) break;
            if (f3 != 0.0f) {
                Helpers.subdivideAt((f3 - f2) / (1.0f - f2), this.curCurvepts, n3, this.curCurvepts, 0, this.curCurvepts, n2, n2);
                f2 = f3;
                this.goTo(this.curCurvepts, 2, n2);
                n3 = n2;
            }
            this.idx = (this.idx + 1) % this.dash.length;
            this.dashOn = !this.dashOn;
            this.phase = 0.0f;
            f4 = this.dash[this.idx];
        }
        this.goTo(this.curCurvepts, n3 + 2, n2);
        this.phase += this.li.lastSegLen();
        if (this.phase >= this.dash[this.idx]) {
            this.phase = 0.0f;
            this.idx = (this.idx + 1) % this.dash.length;
            this.dashOn = !this.dashOn;
        }
    }

    private static boolean pointCurve(float[] arrf, int n2) {
        for (int i2 = 2; i2 < n2; ++i2) {
            if (arrf[i2] == arrf[i2 - 2]) continue;
            return false;
        }
        return true;
    }

    @Override
    public void curveTo(float f2, float f3, float f4, float f5, float f6, float f7) {
        this.curCurvepts[0] = this.x0;
        this.curCurvepts[1] = this.y0;
        this.curCurvepts[2] = f2;
        this.curCurvepts[3] = f3;
        this.curCurvepts[4] = f4;
        this.curCurvepts[5] = f5;
        this.curCurvepts[6] = f6;
        this.curCurvepts[7] = f7;
        this.somethingTo(8);
    }

    @Override
    public void quadTo(float f2, float f3, float f4, float f5) {
        this.curCurvepts[0] = this.x0;
        this.curCurvepts[1] = this.y0;
        this.curCurvepts[2] = f2;
        this.curCurvepts[3] = f3;
        this.curCurvepts[4] = f4;
        this.curCurvepts[5] = f5;
        this.somethingTo(6);
    }

    @Override
    public void closePath() {
        this.lineTo(this.sx, this.sy);
        if (this.firstSegidx > 0) {
            if (!this.dashOn || this.needsMoveTo) {
                this.out.moveTo(this.sx, this.sy);
            }
            this.emitFirstSegments();
        }
        this.moveTo(this.sx, this.sy);
    }

    @Override
    public void pathDone() {
        if (this.firstSegidx > 0) {
            this.out.moveTo(this.sx, this.sy);
            this.emitFirstSegments();
        }
        this.out.pathDone();
    }

    private static class LengthIterator {
        private float[][] recCurveStack;
        private Side[] sides;
        private int curveType;
        private final int limit;
        private final float ERR;
        private final float minTincrement;
        private float nextT;
        private float lenAtNextT;
        private float lastT;
        private float lenAtLastT;
        private float lenAtLastSplit;
        private float lastSegLen;
        private int recLevel;
        private boolean done;
        private float[] curLeafCtrlPolyLengths = new float[3];
        private int cachedHaveLowAcceleration = -1;
        private float[] nextRoots = new float[4];
        private float[] flatLeafCoefCache = new float[]{0.0f, 0.0f, -1.0f, 0.0f};

        public LengthIterator(int n2, float f2) {
            this.limit = n2;
            this.minTincrement = 1.0f / (float)(1 << this.limit);
            this.ERR = f2;
            this.recCurveStack = new float[n2 + 1][8];
            this.sides = new Side[n2];
            this.nextT = Float.MAX_VALUE;
            this.lenAtNextT = Float.MAX_VALUE;
            this.lenAtLastSplit = Float.MIN_VALUE;
            this.recLevel = Integer.MIN_VALUE;
            this.lastSegLen = Float.MAX_VALUE;
            this.done = true;
        }

        public void initializeIterationOnCurve(float[] arrf, int n2) {
            System.arraycopy(arrf, 0, this.recCurveStack[0], 0, n2);
            this.curveType = n2;
            this.recLevel = 0;
            this.lastT = 0.0f;
            this.lenAtLastT = 0.0f;
            this.nextT = 0.0f;
            this.lenAtNextT = 0.0f;
            this.goLeft();
            this.lenAtLastSplit = 0.0f;
            if (this.recLevel > 0) {
                this.sides[0] = Side.LEFT;
                this.done = false;
            } else {
                this.sides[0] = Side.RIGHT;
                this.done = true;
            }
            this.lastSegLen = 0.0f;
        }

        private boolean haveLowAcceleration(float f2) {
            if (this.cachedHaveLowAcceleration == -1) {
                float f3;
                float f4 = this.curLeafCtrlPolyLengths[0];
                float f5 = this.curLeafCtrlPolyLengths[1];
                if (!Helpers.within(f4, f5, f2 * f5)) {
                    this.cachedHaveLowAcceleration = 0;
                    return false;
                }
                if (!(this.curveType != 8 || Helpers.within(f5, f3 = this.curLeafCtrlPolyLengths[2], f2 * f3) && Helpers.within(f4, f3, f2 * f3))) {
                    this.cachedHaveLowAcceleration = 0;
                    return false;
                }
                this.cachedHaveLowAcceleration = 1;
                return true;
            }
            return this.cachedHaveLowAcceleration == 1;
        }

        public float next(float f2) {
            float f3 = this.lenAtLastSplit + f2;
            while (this.lenAtNextT < f3) {
                if (this.done) {
                    this.lastSegLen = this.lenAtNextT - this.lenAtLastSplit;
                    return 1.0f;
                }
                this.goToNextLeaf();
            }
            this.lenAtLastSplit = f3;
            float f4 = this.lenAtNextT - this.lenAtLastT;
            float f5 = (f3 - this.lenAtLastT) / f4;
            if (!this.haveLowAcceleration(0.05f)) {
                float f6;
                int n2;
                float f7;
                float f8;
                float f9;
                if (this.flatLeafCoefCache[2] < 0.0f) {
                    f9 = 0.0f + this.curLeafCtrlPolyLengths[0];
                    f8 = f9 + this.curLeafCtrlPolyLengths[1];
                    if (this.curveType == 8) {
                        f7 = f8 + this.curLeafCtrlPolyLengths[2];
                        this.flatLeafCoefCache[0] = 3.0f * (f9 - f8) + f7;
                        this.flatLeafCoefCache[1] = 3.0f * (f8 - 2.0f * f9);
                        this.flatLeafCoefCache[2] = 3.0f * f9;
                        this.flatLeafCoefCache[3] = -f7;
                    } else if (this.curveType == 6) {
                        this.flatLeafCoefCache[0] = 0.0f;
                        this.flatLeafCoefCache[1] = f8 - 2.0f * f9;
                        this.flatLeafCoefCache[2] = 2.0f * f9;
                        this.flatLeafCoefCache[3] = -f8;
                    }
                }
                if ((n2 = Helpers.cubicRootsInAB(f9 = this.flatLeafCoefCache[0], f8 = this.flatLeafCoefCache[1], f7 = this.flatLeafCoefCache[2], f6 = f5 * this.flatLeafCoefCache[3], this.nextRoots, 0, 0.0f, 1.0f)) == 1 && !Float.isNaN(this.nextRoots[0])) {
                    f5 = this.nextRoots[0];
                }
            }
            if ((f5 = f5 * (this.nextT - this.lastT) + this.lastT) >= 1.0f) {
                f5 = 1.0f;
                this.done = true;
            }
            this.lastSegLen = f2;
            return f5;
        }

        public float lastSegLen() {
            return this.lastSegLen;
        }

        private void goToNextLeaf() {
            --this.recLevel;
            while (this.sides[this.recLevel] == Side.RIGHT) {
                if (this.recLevel == 0) {
                    this.done = true;
                    return;
                }
                --this.recLevel;
            }
            this.sides[this.recLevel] = Side.RIGHT;
            System.arraycopy(this.recCurveStack[this.recLevel], 0, this.recCurveStack[this.recLevel + 1], 0, this.curveType);
            ++this.recLevel;
            this.goLeft();
        }

        private void goLeft() {
            float f2 = this.onLeaf();
            if (f2 >= 0.0f) {
                this.lastT = this.nextT;
                this.lenAtLastT = this.lenAtNextT;
                this.nextT += (float)(1 << this.limit - this.recLevel) * this.minTincrement;
                this.lenAtNextT += f2;
                this.flatLeafCoefCache[2] = -1.0f;
                this.cachedHaveLowAcceleration = -1;
            } else {
                Helpers.subdivide(this.recCurveStack[this.recLevel], 0, this.recCurveStack[this.recLevel + 1], 0, this.recCurveStack[this.recLevel], 0, this.curveType);
                this.sides[this.recLevel] = Side.LEFT;
                ++this.recLevel;
                this.goLeft();
            }
        }

        private float onLeaf() {
            float[] arrf = this.recCurveStack[this.recLevel];
            float f2 = 0.0f;
            float f3 = arrf[0];
            float f4 = arrf[1];
            for (int i2 = 2; i2 < this.curveType; i2 += 2) {
                float f5 = arrf[i2];
                float f6 = arrf[i2 + 1];
                float f7 = Helpers.linelen(f3, f4, f5, f6);
                f2 += f7;
                this.curLeafCtrlPolyLengths[i2 / 2 - 1] = f7;
                f3 = f5;
                f4 = f6;
            }
            float f8 = Helpers.linelen(arrf[0], arrf[1], arrf[this.curveType - 2], arrf[this.curveType - 1]);
            if (f2 - f8 < this.ERR || this.recLevel == this.limit) {
                return (f2 + f8) / 2.0f;
            }
            return -1.0f;
        }

        private static enum Side {
            LEFT,
            RIGHT;

        }
    }
}

