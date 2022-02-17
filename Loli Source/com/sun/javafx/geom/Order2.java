/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.Curve;
import com.sun.javafx.geom.RectBounds;
import java.util.Vector;

final class Order2
extends Curve {
    private double x0;
    private double y0;
    private double cx0;
    private double cy0;
    private double x1;
    private double y1;
    private double xmin;
    private double xmax;
    private double xcoeff0;
    private double xcoeff1;
    private double xcoeff2;
    private double ycoeff0;
    private double ycoeff1;
    private double ycoeff2;

    public static void insert(Vector vector, double[] arrd, double d2, double d3, double d4, double d5, double d6, double d7, int n2) {
        int n3 = Order2.getHorizontalParams(d3, d5, d7, arrd);
        if (n3 == 0) {
            Order2.addInstance(vector, d2, d3, d4, d5, d6, d7, n2);
            return;
        }
        double d8 = arrd[0];
        arrd[0] = d2;
        arrd[1] = d3;
        arrd[2] = d4;
        arrd[3] = d5;
        arrd[4] = d6;
        arrd[5] = d7;
        Order2.split(arrd, 0, d8);
        int n4 = n2 == 1 ? 0 : 4;
        int n5 = 4 - n4;
        Order2.addInstance(vector, arrd[n4], arrd[n4 + 1], arrd[n4 + 2], arrd[n4 + 3], arrd[n4 + 4], arrd[n4 + 5], n2);
        Order2.addInstance(vector, arrd[n5], arrd[n5 + 1], arrd[n5 + 2], arrd[n5 + 3], arrd[n5 + 4], arrd[n5 + 5], n2);
    }

    public static void addInstance(Vector vector, double d2, double d3, double d4, double d5, double d6, double d7, int n2) {
        if (d3 > d7) {
            vector.add(new Order2(d6, d7, d4, d5, d2, d3, -n2));
        } else if (d7 > d3) {
            vector.add(new Order2(d2, d3, d4, d5, d6, d7, n2));
        }
    }

    public static int getHorizontalParams(double d2, double d3, double d4, double[] arrd) {
        if (d2 <= d3 && d3 <= d4) {
            return 0;
        }
        double d5 = (d2 -= d3) + (d4 -= d3);
        if (d5 == 0.0) {
            return 0;
        }
        double d6 = d2 / d5;
        if (d6 <= 0.0 || d6 >= 1.0) {
            return 0;
        }
        arrd[0] = d6;
        return 1;
    }

    public static void split(double[] arrd, int n2, double d2) {
        double d3;
        double d4;
        arrd[n2 + 8] = d4 = arrd[n2 + 4];
        arrd[n2 + 9] = d3 = arrd[n2 + 5];
        double d5 = arrd[n2 + 2];
        double d6 = arrd[n2 + 3];
        d4 = d5 + (d4 - d5) * d2;
        d3 = d6 + (d3 - d6) * d2;
        double d7 = arrd[n2 + 0];
        double d8 = arrd[n2 + 1];
        d7 += (d5 - d7) * d2;
        d8 += (d6 - d8) * d2;
        d5 = d7 + (d4 - d7) * d2;
        d6 = d8 + (d3 - d8) * d2;
        arrd[n2 + 2] = d7;
        arrd[n2 + 3] = d8;
        arrd[n2 + 4] = d5;
        arrd[n2 + 5] = d6;
        arrd[n2 + 6] = d4;
        arrd[n2 + 7] = d3;
    }

    public Order2(double d2, double d3, double d4, double d5, double d6, double d7, int n2) {
        super(n2);
        if (d5 < d3) {
            d5 = d3;
        } else if (d5 > d7) {
            d5 = d7;
        }
        this.x0 = d2;
        this.y0 = d3;
        this.cx0 = d4;
        this.cy0 = d5;
        this.x1 = d6;
        this.y1 = d7;
        this.xmin = Math.min(Math.min(d2, d6), d4);
        this.xmax = Math.max(Math.max(d2, d6), d4);
        this.xcoeff0 = d2;
        this.xcoeff1 = d4 + d4 - d2 - d2;
        this.xcoeff2 = d2 - d4 - d4 + d6;
        this.ycoeff0 = d3;
        this.ycoeff1 = d5 + d5 - d3 - d3;
        this.ycoeff2 = d3 - d5 - d5 + d7;
    }

    @Override
    public int getOrder() {
        return 2;
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

    public double getCX0() {
        return this.cx0;
    }

    public double getCY0() {
        return this.cy0;
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
        if (d2 <= this.y0) {
            return this.x0;
        }
        if (d2 >= this.y1) {
            return this.x1;
        }
        return this.XforT(this.TforY(d2));
    }

    @Override
    public double TforY(double d2) {
        if (d2 <= this.y0) {
            return 0.0;
        }
        if (d2 >= this.y1) {
            return 1.0;
        }
        return Order2.TforY(d2, this.ycoeff0, this.ycoeff1, this.ycoeff2);
    }

    public static double TforY(double d2, double d3, double d4, double d5) {
        double d6;
        double d7;
        d3 -= d2;
        if (d5 == 0.0) {
            d7 = -d3 / d4;
            if (d7 >= 0.0 && d7 <= 1.0) {
                return d7;
            }
        } else {
            d7 = d4 * d4 - 4.0 * d5 * d3;
            if (d7 >= 0.0) {
                double d8;
                d7 = Math.sqrt(d7);
                if (d4 < 0.0) {
                    d7 = -d7;
                }
                if ((d8 = (d6 = (d4 + d7) / -2.0) / d5) >= 0.0 && d8 <= 1.0) {
                    return d8;
                }
                if (d6 != 0.0 && (d8 = d3 / d6) >= 0.0 && d8 <= 1.0) {
                    return d8;
                }
            }
        }
        return 0.0 < ((d7 = d3) + (d6 = d3 + d4 + d5)) / 2.0 ? 0.0 : 1.0;
    }

    @Override
    public double XforT(double d2) {
        return (this.xcoeff2 * d2 + this.xcoeff1) * d2 + this.xcoeff0;
    }

    @Override
    public double YforT(double d2) {
        return (this.ycoeff2 * d2 + this.ycoeff1) * d2 + this.ycoeff0;
    }

    @Override
    public double dXforT(double d2, int n2) {
        switch (n2) {
            case 0: {
                return (this.xcoeff2 * d2 + this.xcoeff1) * d2 + this.xcoeff0;
            }
            case 1: {
                return 2.0 * this.xcoeff2 * d2 + this.xcoeff1;
            }
            case 2: {
                return 2.0 * this.xcoeff2;
            }
        }
        return 0.0;
    }

    @Override
    public double dYforT(double d2, int n2) {
        switch (n2) {
            case 0: {
                return (this.ycoeff2 * d2 + this.ycoeff1) * d2 + this.ycoeff0;
            }
            case 1: {
                return 2.0 * this.ycoeff2 * d2 + this.ycoeff1;
            }
            case 2: {
                return 2.0 * this.ycoeff2;
            }
        }
        return 0.0;
    }

    @Override
    public double nextVertical(double d2, double d3) {
        double d4 = -this.xcoeff1 / (2.0 * this.xcoeff2);
        if (d4 > d2 && d4 < d3) {
            return d4;
        }
        return d3;
    }

    @Override
    public void enlarge(RectBounds rectBounds) {
        rectBounds.add((float)this.x0, (float)this.y0);
        double d2 = -this.xcoeff1 / (2.0 * this.xcoeff2);
        if (d2 > 0.0 && d2 < 1.0) {
            rectBounds.add((float)this.XforT(d2), (float)this.YforT(d2));
        }
        rectBounds.add((float)this.x1, (float)this.y1);
    }

    @Override
    public Curve getSubCurve(double d2, double d3, int n2) {
        int n3;
        double d4;
        if (d2 <= this.y0) {
            if (d3 >= this.y1) {
                return this.getWithDirection(n2);
            }
            d4 = 0.0;
        } else {
            d4 = Order2.TforY(d2, this.ycoeff0, this.ycoeff1, this.ycoeff2);
        }
        double d5 = d3 >= this.y1 ? 1.0 : Order2.TforY(d3, this.ycoeff0, this.ycoeff1, this.ycoeff2);
        double[] arrd = new double[10];
        arrd[0] = this.x0;
        arrd[1] = this.y0;
        arrd[2] = this.cx0;
        arrd[3] = this.cy0;
        arrd[4] = this.x1;
        arrd[5] = this.y1;
        if (d5 < 1.0) {
            Order2.split(arrd, 0, d5);
        }
        if (d4 <= 0.0) {
            n3 = 0;
        } else {
            Order2.split(arrd, 0, d4 / d5);
            n3 = 4;
        }
        return new Order2(arrd[n3 + 0], d2, arrd[n3 + 2], arrd[n3 + 3], arrd[n3 + 4], d3, n2);
    }

    @Override
    public Curve getReversedCurve() {
        return new Order2(this.x0, this.y0, this.cx0, this.cy0, this.x1, this.y1, -this.direction);
    }

    @Override
    public int getSegment(float[] arrf) {
        arrf[0] = (float)this.cx0;
        arrf[1] = (float)this.cy0;
        if (this.direction == 1) {
            arrf[2] = (float)this.x1;
            arrf[3] = (float)this.y1;
        } else {
            arrf[2] = (float)this.x0;
            arrf[3] = (float)this.y0;
        }
        return 2;
    }

    @Override
    public String controlPointString() {
        return "(" + Order2.round(this.cx0) + ", " + Order2.round(this.cy0) + "), ";
    }
}

