/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.Crossings;
import com.sun.javafx.geom.Curve;
import com.sun.javafx.geom.RectBounds;

final class Order1
extends Curve {
    private double x0;
    private double y0;
    private double x1;
    private double y1;
    private double xmin;
    private double xmax;

    public Order1(double d2, double d3, double d4, double d5, int n2) {
        super(n2);
        this.x0 = d2;
        this.y0 = d3;
        this.x1 = d4;
        this.y1 = d5;
        if (d2 < d4) {
            this.xmin = d2;
            this.xmax = d4;
        } else {
            this.xmin = d4;
            this.xmax = d2;
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public double getXTop() {
        return this.x0;
    }

    @Override
    public double getYTop() {
        return this.y0;
    }

    @Override
    public double getXBot() {
        return this.x1;
    }

    @Override
    public double getYBot() {
        return this.y1;
    }

    @Override
    public double getXMin() {
        return this.xmin;
    }

    @Override
    public double getXMax() {
        return this.xmax;
    }

    @Override
    public double getX0() {
        return this.direction == 1 ? this.x0 : this.x1;
    }

    @Override
    public double getY0() {
        return this.direction == 1 ? this.y0 : this.y1;
    }

    @Override
    public double getX1() {
        return this.direction == -1 ? this.x0 : this.x1;
    }

    @Override
    public double getY1() {
        return this.direction == -1 ? this.y0 : this.y1;
    }

    @Override
    public double XforY(double d2) {
        if (this.x0 == this.x1 || d2 <= this.y0) {
            return this.x0;
        }
        if (d2 >= this.y1) {
            return this.x1;
        }
        return this.x0 + (d2 - this.y0) * (this.x1 - this.x0) / (this.y1 - this.y0);
    }

    @Override
    public double TforY(double d2) {
        if (d2 <= this.y0) {
            return 0.0;
        }
        if (d2 >= this.y1) {
            return 1.0;
        }
        return (d2 - this.y0) / (this.y1 - this.y0);
    }

    @Override
    public double XforT(double d2) {
        return this.x0 + d2 * (this.x1 - this.x0);
    }

    @Override
    public double YforT(double d2) {
        return this.y0 + d2 * (this.y1 - this.y0);
    }

    @Override
    public double dXforT(double d2, int n2) {
        switch (n2) {
            case 0: {
                return this.x0 + d2 * (this.x1 - this.x0);
            }
            case 1: {
                return this.x1 - this.x0;
            }
        }
        return 0.0;
    }

    @Override
    public double dYforT(double d2, int n2) {
        switch (n2) {
            case 0: {
                return this.y0 + d2 * (this.y1 - this.y0);
            }
            case 1: {
                return this.y1 - this.y0;
            }
        }
        return 0.0;
    }

    @Override
    public double nextVertical(double d2, double d3) {
        return d3;
    }

    @Override
    public boolean accumulateCrossings(Crossings crossings) {
        double d2;
        double d3;
        double d4;
        double d5;
        double d6 = crossings.getXLo();
        double d7 = crossings.getYLo();
        double d8 = crossings.getXHi();
        double d9 = crossings.getYHi();
        if (this.xmin >= d8) {
            return false;
        }
        if (this.y0 < d7) {
            if (this.y1 <= d7) {
                return false;
            }
            d5 = d7;
            d4 = this.XforY(d7);
        } else {
            if (this.y0 >= d9) {
                return false;
            }
            d5 = this.y0;
            d4 = this.x0;
        }
        if (this.y1 > d9) {
            d3 = d9;
            d2 = this.XforY(d9);
        } else {
            d3 = this.y1;
            d2 = this.x1;
        }
        if (d4 >= d8 && d2 >= d8) {
            return false;
        }
        if (d4 > d6 || d2 > d6) {
            return true;
        }
        crossings.record(d5, d3, this.direction);
        return false;
    }

    @Override
    public void enlarge(RectBounds rectBounds) {
        rectBounds.add((float)this.x0, (float)this.y0);
        rectBounds.add((float)this.x1, (float)this.y1);
    }

    @Override
    public Curve getSubCurve(double d2, double d3, int n2) {
        if (d2 == this.y0 && d3 == this.y1) {
            return this.getWithDirection(n2);
        }
        if (this.x0 == this.x1) {
            return new Order1(this.x0, d2, this.x1, d3, n2);
        }
        double d4 = this.x0 - this.x1;
        double d5 = this.y0 - this.y1;
        double d6 = this.x0 + (d2 - this.y0) * d4 / d5;
        double d7 = this.x0 + (d3 - this.y0) * d4 / d5;
        return new Order1(d6, d2, d7, d3, n2);
    }

    @Override
    public Curve getReversedCurve() {
        return new Order1(this.x0, this.y0, this.x1, this.y1, -this.direction);
    }

    @Override
    public int compareTo(Curve curve, double[] arrd) {
        double d2;
        if (!(curve instanceof Order1)) {
            return super.compareTo(curve, arrd);
        }
        Order1 order1 = (Order1)curve;
        if (arrd[1] <= arrd[0]) {
            throw new InternalError("yrange already screwed up...");
        }
        arrd[1] = Math.min(Math.min(arrd[1], this.y1), order1.y1);
        if (arrd[1] <= arrd[0]) {
            throw new InternalError("backstepping from " + arrd[0] + " to " + arrd[1]);
        }
        if (this.xmax <= order1.xmin) {
            return this.xmin == order1.xmax ? 0 : -1;
        }
        if (this.xmin >= order1.xmax) {
            return 1;
        }
        double d3 = order1.x1 - order1.x0;
        double d4 = this.y1 - this.y0;
        double d5 = this.x1 - this.x0;
        double d6 = order1.y1 - order1.y0;
        double d7 = d3 * d4 - d5 * d6;
        if (d7 != 0.0) {
            double d8 = (this.x0 - order1.x0) * d4 * d6 - this.y0 * d5 * d6 + order1.y0 * d3 * d4;
            d2 = d8 / d7;
            if (d2 <= arrd[0]) {
                d2 = Math.min(this.y1, order1.y1);
            } else {
                if (d2 < arrd[1]) {
                    arrd[1] = d2;
                }
                d2 = Math.max(this.y0, order1.y0);
            }
        } else {
            d2 = Math.max(this.y0, order1.y0);
        }
        return Order1.orderof(this.XforY(d2), order1.XforY(d2));
    }

    @Override
    public int getSegment(float[] arrf) {
        if (this.direction == 1) {
            arrf[0] = (float)this.x1;
            arrf[1] = (float)this.y1;
        } else {
            arrf[0] = (float)this.x0;
            arrf[1] = (float)this.y0;
        }
        return 1;
    }
}

