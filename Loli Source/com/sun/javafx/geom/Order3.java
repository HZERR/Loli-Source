/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.Curve;
import com.sun.javafx.geom.Order2;
import com.sun.javafx.geom.RectBounds;
import java.util.Vector;

final class Order3
extends Curve {
    private double x0;
    private double y0;
    private double cx0;
    private double cy0;
    private double cx1;
    private double cy1;
    private double x1;
    private double y1;
    private double xmin;
    private double xmax;
    private double xcoeff0;
    private double xcoeff1;
    private double xcoeff2;
    private double xcoeff3;
    private double ycoeff0;
    private double ycoeff1;
    private double ycoeff2;
    private double ycoeff3;
    private double TforY1;
    private double YforT1;
    private double TforY2;
    private double YforT2;
    private double TforY3;
    private double YforT3;

    public static void insert(Vector vector, double[] arrd, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, int n2) {
        int n3 = Order3.getHorizontalParams(d3, d5, d7, d9, arrd);
        if (n3 == 0) {
            Order3.addInstance(vector, d2, d3, d4, d5, d6, d7, d8, d9, n2);
            return;
        }
        arrd[3] = d2;
        arrd[4] = d3;
        arrd[5] = d4;
        arrd[6] = d5;
        arrd[7] = d6;
        arrd[8] = d7;
        arrd[9] = d8;
        arrd[10] = d9;
        double d10 = arrd[0];
        if (n3 > 1 && d10 > arrd[1]) {
            arrd[0] = arrd[1];
            arrd[1] = d10;
            d10 = arrd[0];
        }
        Order3.split(arrd, 3, d10);
        if (n3 > 1) {
            d10 = (arrd[1] - d10) / (1.0 - d10);
            Order3.split(arrd, 9, d10);
        }
        int n4 = 3;
        if (n2 == -1) {
            n4 += n3 * 6;
        }
        while (n3 >= 0) {
            Order3.addInstance(vector, arrd[n4 + 0], arrd[n4 + 1], arrd[n4 + 2], arrd[n4 + 3], arrd[n4 + 4], arrd[n4 + 5], arrd[n4 + 6], arrd[n4 + 7], n2);
            --n3;
            if (n2 == 1) {
                n4 += 6;
                continue;
            }
            n4 -= 6;
        }
    }

    public static void addInstance(Vector vector, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, int n2) {
        if (d3 > d9) {
            vector.add(new Order3(d8, d9, d6, d7, d4, d5, d2, d3, -n2));
        } else if (d9 > d3) {
            vector.add(new Order3(d2, d3, d4, d5, d6, d7, d8, d9, n2));
        }
    }

    public static int solveQuadratic(double[] arrd, double[] arrd2) {
        double d2 = arrd[2];
        double d3 = arrd[1];
        double d4 = arrd[0];
        int n2 = 0;
        if (d2 == 0.0) {
            if (d3 == 0.0) {
                return -1;
            }
            arrd2[n2++] = -d4 / d3;
        } else {
            double d5 = d3 * d3 - 4.0 * d2 * d4;
            if (d5 < 0.0) {
                return 0;
            }
            d5 = Math.sqrt(d5);
            if (d3 < 0.0) {
                d5 = -d5;
            }
            double d6 = (d3 + d5) / -2.0;
            arrd2[n2++] = d6 / d2;
            if (d6 != 0.0) {
                arrd2[n2++] = d4 / d6;
            }
        }
        return n2;
    }

    public static int getHorizontalParams(double d2, double d3, double d4, double d5, double[] arrd) {
        if (d2 <= d3 && d3 <= d4 && d4 <= d5) {
            return 0;
        }
        d5 -= d4;
        d4 -= d3;
        arrd[0] = d3 -= d2;
        arrd[1] = (d4 - d3) * 2.0;
        arrd[2] = d5 - d4 - d4 + d3;
        int n2 = Order3.solveQuadratic(arrd, arrd);
        int n3 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            double d6 = arrd[i2];
            if (!(d6 > 0.0) || !(d6 < 1.0)) continue;
            if (n3 < i2) {
                arrd[n3] = d6;
            }
            ++n3;
        }
        return n3;
    }

    public static void split(double[] arrd, int n2, double d2) {
        double d3;
        double d4;
        arrd[n2 + 12] = d4 = arrd[n2 + 6];
        arrd[n2 + 13] = d3 = arrd[n2 + 7];
        double d5 = arrd[n2 + 4];
        double d6 = arrd[n2 + 5];
        d4 = d5 + (d4 - d5) * d2;
        d3 = d6 + (d3 - d6) * d2;
        double d7 = arrd[n2 + 0];
        double d8 = arrd[n2 + 1];
        double d9 = arrd[n2 + 2];
        double d10 = arrd[n2 + 3];
        d7 += (d9 - d7) * d2;
        d8 += (d10 - d8) * d2;
        d9 += (d5 - d9) * d2;
        d10 += (d6 - d10) * d2;
        d5 = d9 + (d4 - d9) * d2;
        d6 = d10 + (d3 - d10) * d2;
        d9 = d7 + (d9 - d7) * d2;
        d10 = d8 + (d10 - d8) * d2;
        arrd[n2 + 2] = d7;
        arrd[n2 + 3] = d8;
        arrd[n2 + 4] = d9;
        arrd[n2 + 5] = d10;
        arrd[n2 + 6] = d9 + (d5 - d9) * d2;
        arrd[n2 + 7] = d10 + (d6 - d10) * d2;
        arrd[n2 + 8] = d5;
        arrd[n2 + 9] = d6;
        arrd[n2 + 10] = d4;
        arrd[n2 + 11] = d3;
    }

    public Order3(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, int n2) {
        super(n2);
        if (d5 < d3) {
            d5 = d3;
        }
        if (d7 > d9) {
            d7 = d9;
        }
        this.x0 = d2;
        this.y0 = d3;
        this.cx0 = d4;
        this.cy0 = d5;
        this.cx1 = d6;
        this.cy1 = d7;
        this.x1 = d8;
        this.y1 = d9;
        this.xmin = Math.min(Math.min(d2, d8), Math.min(d4, d6));
        this.xmax = Math.max(Math.max(d2, d8), Math.max(d4, d6));
        this.xcoeff0 = d2;
        this.xcoeff1 = (d4 - d2) * 3.0;
        this.xcoeff2 = (d6 - d4 - d4 + d2) * 3.0;
        this.xcoeff3 = d8 - (d6 - d4) * 3.0 - d2;
        this.ycoeff0 = d3;
        this.ycoeff1 = (d5 - d3) * 3.0;
        this.ycoeff2 = (d7 - d5 - d5 + d3) * 3.0;
        this.ycoeff3 = d9 - (d7 - d5) * 3.0 - d3;
        this.YforT2 = this.YforT3 = d3;
        this.YforT1 = this.YforT3;
    }

    @Override
    public int getOrder() {
        return 3;
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
        return this.direction == 1 ? this.cx0 : this.cx1;
    }

    public double getCY0() {
        return this.direction == 1 ? this.cy0 : this.cy1;
    }

    public double getCX1() {
        return this.direction == -1 ? this.cx0 : this.cx1;
    }

    public double getCY1() {
        return this.direction == -1 ? this.cy0 : this.cy1;
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
    public double TforY(double d2) {
        double d3;
        double d4;
        if (d2 <= this.y0) {
            return 0.0;
        }
        if (d2 >= this.y1) {
            return 1.0;
        }
        if (d2 == this.YforT1) {
            return this.TforY1;
        }
        if (d2 == this.YforT2) {
            return this.TforY2;
        }
        if (d2 == this.YforT3) {
            return this.TforY3;
        }
        if (this.ycoeff3 == 0.0) {
            return Order2.TforY(d2, this.ycoeff0, this.ycoeff1, this.ycoeff2);
        }
        double d5 = this.ycoeff2 / this.ycoeff3;
        double d6 = this.ycoeff1 / this.ycoeff3;
        double d7 = (this.ycoeff0 - d2) / this.ycoeff3;
        boolean bl = false;
        double d8 = (d5 * d5 - 3.0 * d6) / 9.0;
        double d9 = (2.0 * d5 * d5 * d5 - 9.0 * d5 * d6 + 27.0 * d7) / 54.0;
        double d10 = d9 * d9;
        double d11 = d8 * d8 * d8;
        double d12 = d5 / 3.0;
        if (d10 < d11) {
            d4 = Math.acos(d9 / Math.sqrt(d11));
            d3 = this.refine(d5, d6, d7, d2, (d8 = -2.0 * Math.sqrt(d8)) * Math.cos(d4 / 3.0) - d12);
            if (d3 < 0.0) {
                d3 = this.refine(d5, d6, d7, d2, d8 * Math.cos((d4 + Math.PI * 2) / 3.0) - d12);
            }
            if (d3 < 0.0) {
                d3 = this.refine(d5, d6, d7, d2, d8 * Math.cos((d4 - Math.PI * 2) / 3.0) - d12);
            }
        } else {
            boolean bl2 = d9 < 0.0;
            double d13 = Math.sqrt(d10 - d11);
            if (bl2) {
                d9 = -d9;
            }
            double d14 = Math.pow(d9 + d13, 0.3333333333333333);
            if (!bl2) {
                d14 = -d14;
            }
            double d15 = d14 == 0.0 ? 0.0 : d8 / d14;
            d3 = this.refine(d5, d6, d7, d2, d14 + d15 - d12);
        }
        if (d3 < 0.0) {
            d4 = 0.0;
            double d16 = 1.0;
            while ((d3 = (d4 + d16) / 2.0) != d4 && d3 != d16) {
                double d17 = this.YforT(d3);
                if (d17 < d2) {
                    d4 = d3;
                    continue;
                }
                if (!(d17 > d2)) break;
                d16 = d3;
            }
        }
        if (d3 >= 0.0) {
            this.TforY3 = this.TforY2;
            this.YforT3 = this.YforT2;
            this.TforY2 = this.TforY1;
            this.YforT2 = this.YforT1;
            this.TforY1 = d3;
            this.YforT1 = d2;
        }
        return d3;
    }

    public double refine(double d2, double d3, double d4, double d5, double d6) {
        double d7;
        double d8;
        if (d6 < -0.1 || d6 > 1.1) {
            return -1.0;
        }
        double d9 = this.YforT(d6);
        if (d9 < d5) {
            d8 = d6;
            d7 = 1.0;
        } else {
            d8 = 0.0;
            d7 = d6;
        }
        double d10 = d6;
        double d11 = d9;
        boolean bl = true;
        while (d9 != d5) {
            double d12;
            if (!bl) {
                d12 = (d8 + d7) / 2.0;
                if (d12 == d8 || d12 == d7) break;
                d6 = d12;
            } else {
                d12 = this.dYforT(d6, 1);
                if (d12 == 0.0) {
                    bl = false;
                    continue;
                }
                double d13 = d6 + (d5 - d9) / d12;
                if (d13 == d6 || d13 <= d8 || d13 >= d7) {
                    bl = false;
                    continue;
                }
                d6 = d13;
            }
            d9 = this.YforT(d6);
            if (d9 < d5) {
                d8 = d6;
                continue;
            }
            if (!(d9 > d5)) break;
            d7 = d6;
        }
        boolean bl2 = false;
        return d6 > 1.0 ? -1.0 : d6;
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
    public double XforT(double d2) {
        return ((this.xcoeff3 * d2 + this.xcoeff2) * d2 + this.xcoeff1) * d2 + this.xcoeff0;
    }

    @Override
    public double YforT(double d2) {
        return ((this.ycoeff3 * d2 + this.ycoeff2) * d2 + this.ycoeff1) * d2 + this.ycoeff0;
    }

    @Override
    public double dXforT(double d2, int n2) {
        switch (n2) {
            case 0: {
                return ((this.xcoeff3 * d2 + this.xcoeff2) * d2 + this.xcoeff1) * d2 + this.xcoeff0;
            }
            case 1: {
                return (3.0 * this.xcoeff3 * d2 + 2.0 * this.xcoeff2) * d2 + this.xcoeff1;
            }
            case 2: {
                return 6.0 * this.xcoeff3 * d2 + 2.0 * this.xcoeff2;
            }
            case 3: {
                return 6.0 * this.xcoeff3;
            }
        }
        return 0.0;
    }

    @Override
    public double dYforT(double d2, int n2) {
        switch (n2) {
            case 0: {
                return ((this.ycoeff3 * d2 + this.ycoeff2) * d2 + this.ycoeff1) * d2 + this.ycoeff0;
            }
            case 1: {
                return (3.0 * this.ycoeff3 * d2 + 2.0 * this.ycoeff2) * d2 + this.ycoeff1;
            }
            case 2: {
                return 6.0 * this.ycoeff3 * d2 + 2.0 * this.ycoeff2;
            }
            case 3: {
                return 6.0 * this.ycoeff3;
            }
        }
        return 0.0;
    }

    @Override
    public double nextVertical(double d2, double d3) {
        double[] arrd = new double[]{this.xcoeff1, 2.0 * this.xcoeff2, 3.0 * this.xcoeff3};
        int n2 = Order3.solveQuadratic(arrd, arrd);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (!(arrd[i2] > d2) || !(arrd[i2] < d3)) continue;
            d3 = arrd[i2];
        }
        return d3;
    }

    @Override
    public void enlarge(RectBounds rectBounds) {
        rectBounds.add((float)this.x0, (float)this.y0);
        double[] arrd = new double[]{this.xcoeff1, 2.0 * this.xcoeff2, 3.0 * this.xcoeff3};
        int n2 = Order3.solveQuadratic(arrd, arrd);
        for (int i2 = 0; i2 < n2; ++i2) {
            double d2 = arrd[i2];
            if (!(d2 > 0.0) || !(d2 < 1.0)) continue;
            rectBounds.add((float)this.XforT(d2), (float)this.YforT(d2));
        }
        rectBounds.add((float)this.x1, (float)this.y1);
    }

    @Override
    public Curve getSubCurve(double d2, double d3, int n2) {
        int n3;
        if (d2 <= this.y0 && d3 >= this.y1) {
            return this.getWithDirection(n2);
        }
        double[] arrd = new double[14];
        double d4 = this.TforY(d2);
        double d5 = this.TforY(d3);
        arrd[0] = this.x0;
        arrd[1] = this.y0;
        arrd[2] = this.cx0;
        arrd[3] = this.cy0;
        arrd[4] = this.cx1;
        arrd[5] = this.cy1;
        arrd[6] = this.x1;
        arrd[7] = this.y1;
        if (d4 > d5) {
            double d6 = d4;
            d4 = d5;
            d5 = d6;
        }
        if (d5 < 1.0) {
            Order3.split(arrd, 0, d5);
        }
        if (d4 <= 0.0) {
            n3 = 0;
        } else {
            Order3.split(arrd, 0, d4 / d5);
            n3 = 6;
        }
        return new Order3(arrd[n3 + 0], d2, arrd[n3 + 2], arrd[n3 + 3], arrd[n3 + 4], arrd[n3 + 5], arrd[n3 + 6], d3, n2);
    }

    @Override
    public Curve getReversedCurve() {
        return new Order3(this.x0, this.y0, this.cx0, this.cy0, this.cx1, this.cy1, this.x1, this.y1, -this.direction);
    }

    @Override
    public int getSegment(float[] arrf) {
        if (this.direction == 1) {
            arrf[0] = (float)this.cx0;
            arrf[1] = (float)this.cy0;
            arrf[2] = (float)this.cx1;
            arrf[3] = (float)this.cy1;
            arrf[4] = (float)this.x1;
            arrf[5] = (float)this.y1;
        } else {
            arrf[0] = (float)this.cx1;
            arrf[1] = (float)this.cy1;
            arrf[2] = (float)this.cx0;
            arrf[3] = (float)this.cy0;
            arrf[4] = (float)this.x0;
            arrf[5] = (float)this.y0;
        }
        return 3;
    }

    @Override
    public String controlPointString() {
        return "(" + Order3.round(this.getCX0()) + ", " + Order3.round(this.getCY0()) + "), " + "(" + Order3.round(this.getCX1()) + ", " + Order3.round(this.getCY1()) + "), ";
    }
}

