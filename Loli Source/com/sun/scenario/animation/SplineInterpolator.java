/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.animation;

import javafx.animation.Interpolator;

public class SplineInterpolator
extends Interpolator {
    private final double x1;
    private final double y1;
    private final double x2;
    private final double y2;
    private final boolean isCurveLinear;
    private static final int SAMPLE_SIZE = 16;
    private static final double SAMPLE_INCREMENT = 0.0625;
    private final double[] xSamples = new double[17];

    public SplineInterpolator(double d2, double d3, double d4, double d5) {
        if (d2 < 0.0 || d2 > 1.0 || d3 < 0.0 || d3 > 1.0 || d4 < 0.0 || d4 > 1.0 || d5 < 0.0 || d5 > 1.0) {
            throw new IllegalArgumentException("Control point coordinates must all be in range [0,1]");
        }
        this.x1 = d2;
        this.y1 = d3;
        this.x2 = d4;
        this.y2 = d5;
        boolean bl = this.isCurveLinear = this.x1 == this.y1 && this.x2 == this.y2;
        if (!this.isCurveLinear) {
            for (int i2 = 0; i2 < 17; ++i2) {
                this.xSamples[i2] = this.eval((double)i2 * 0.0625, this.x1, this.x2);
            }
        }
    }

    public double getX1() {
        return this.x1;
    }

    public double getY1() {
        return this.y1;
    }

    public double getX2() {
        return this.x2;
    }

    public double getY2() {
        return this.y2;
    }

    public int hashCode() {
        int n2 = 7;
        n2 = 19 * n2 + (int)(Double.doubleToLongBits(this.x1) ^ Double.doubleToLongBits(this.x1) >>> 32);
        n2 = 19 * n2 + (int)(Double.doubleToLongBits(this.y1) ^ Double.doubleToLongBits(this.y1) >>> 32);
        n2 = 19 * n2 + (int)(Double.doubleToLongBits(this.x2) ^ Double.doubleToLongBits(this.x2) >>> 32);
        n2 = 19 * n2 + (int)(Double.doubleToLongBits(this.y2) ^ Double.doubleToLongBits(this.y2) >>> 32);
        return n2;
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        SplineInterpolator splineInterpolator = (SplineInterpolator)object;
        if (Double.doubleToLongBits(this.x1) != Double.doubleToLongBits(splineInterpolator.x1)) {
            return false;
        }
        if (Double.doubleToLongBits(this.y1) != Double.doubleToLongBits(splineInterpolator.y1)) {
            return false;
        }
        if (Double.doubleToLongBits(this.x2) != Double.doubleToLongBits(splineInterpolator.x2)) {
            return false;
        }
        return Double.doubleToLongBits(this.y2) == Double.doubleToLongBits(splineInterpolator.y2);
    }

    @Override
    public double curve(double d2) {
        if (d2 < 0.0 || d2 > 1.0) {
            throw new IllegalArgumentException("x must be in range [0,1]");
        }
        if (this.isCurveLinear || d2 == 0.0 || d2 == 1.0) {
            return d2;
        }
        return this.eval(this.findTForX(d2), this.y1, this.y2);
    }

    private double eval(double d2, double d3, double d4) {
        double d5 = 1.0 - d2;
        return d2 * (3.0 * d5 * (d5 * d3 + d2 * d4) + d2 * d2);
    }

    private double evalDerivative(double d2, double d3, double d4) {
        double d5 = 1.0 - d2;
        return 3.0 * (d5 * (d5 * d3 + 2.0 * d2 * (d4 - d3)) + d2 * d2 * (1.0 - d4));
    }

    private double getInitialGuessForT(double d2) {
        for (int i2 = 1; i2 < 17; ++i2) {
            if (!(this.xSamples[i2] >= d2)) continue;
            double d3 = this.xSamples[i2] - this.xSamples[i2 - 1];
            if (d3 == 0.0) {
                return (double)(i2 - 1) * 0.0625;
            }
            return ((double)(i2 - 1) + (d2 - this.xSamples[i2 - 1]) / d3) * 0.0625;
        }
        return 1.0;
    }

    private double findTForX(double d2) {
        double d3;
        double d4;
        double d5 = this.getInitialGuessForT(d2);
        for (int i2 = 0; i2 < 4 && (d4 = this.eval(d5, this.x1, this.x2) - d2) != 0.0 && (d3 = this.evalDerivative(d5, this.x1, this.x2)) != 0.0; ++i2) {
            d5 -= d4 / d3;
        }
        return d5;
    }

    public String toString() {
        return "SplineInterpolator [x1=" + this.x1 + ", y1=" + this.y1 + ", x2=" + this.x2 + ", y2=" + this.y2 + "]";
    }
}

